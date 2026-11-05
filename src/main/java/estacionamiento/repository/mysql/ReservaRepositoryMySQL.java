package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoReserva;
import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.repository.ReservaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ReservaRepositoryMySQL implements ReservaRepository {

    private final EntityManagerFactory emf;
    
    public ReservaRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public void guardar(Reserva reserva) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(reserva);
            em.getTransaction().commit();
            System.out.println("MySQL: Reserva registrada correctamente en la base de datos.");
            System.out.println("Reserva guardada: "+ reserva+"\n");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Reserva buscarPorClave(ReservaId id) {
        EntityManager em = emf.createEntityManager(); 
        try {
            String fechaTexto = id.getFechaDesde().toString().replace("T", " ") + "%";
            String sql = "SELECT * FROM reserva WHERE patente = :patente " +
                         "AND numero_usuario = :usuario " +
                         "AND numero_tipo_estadia = :tipo " +
                         "AND fecha_desde LIKE :fecha";

            Reserva reservaEncontrada = (Reserva) em.createNativeQuery(sql, Reserva.class)
                .setParameter("patente", id.getPatenteVehiculo())
                .setParameter("usuario", id.getNumeroUsuario())
                .setParameter("tipo", id.getNumeroTipoEstadia())
                .setParameter("fecha", fechaTexto)
                .getSingleResult();
            
            // INICIALIZACIÓN FORZADA (ANTI-LAZY)
            // Como la conexión sigue abierta, le pedimos un dato al Pago 
            // para que Hibernate lo traiga a la memoria real y no sea un Proxy fantasma.
            if (reservaEncontrada.getPagoSaldo() != null) {
                reservaEncontrada.getPagoSaldo().getEstado();
            }

            return reservaEncontrada;
            
        } catch (jakarta.persistence.NoResultException e) {
            return null; 
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reserva> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(Reserva reservaNuevosDatos) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(reservaNuevosDatos);
            em.getTransaction().commit();
            System.out.println("MySQL: Reserva actualizada correctamente.");
            //System.out.println("Datos de la reserva actualizada: " + reservaNuevosDatos + "\n");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(ReservaId claveCompuesta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Reserva resAEliminar = em.find(Reserva.class, claveCompuesta);
            
            if (resAEliminar != null) {
                em.remove(resAEliminar);
                em.getTransaction().commit();
                System.out.println("MySQL: Reserva eliminada correctamente.");
            } else {
                em.getTransaction().rollback();
                System.out.println("MySQL: La reserva no fue encontrada para ser eliminada.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

	@Override
    public List<Reserva> buscarReservasPendientesSinPagoCompleto(LocalDateTime limiteTiempo) {
        EntityManager em = emf.createEntityManager();
        try {
            // Buscamos reservas PENDIENTES, sin un pago total asociado, 
            // que comiencen antes de la fecha límite establecida (ej: dentro de 60 min)
            String jpql = "SELECT r FROM Reserva r " +
                          "WHERE r.estado = :estado " +
                          "AND r.pagoSaldo IS NULL " +
                          "AND r.id.fechaDesde <= :limite";
                          
            return em.createQuery(jpql, Reserva.class)
                     .setParameter("estado", EstadoReserva.PENDIENTE)
                     .setParameter("limite", limiteTiempo)
                     .getResultList();
                     
        } finally {
            em.close();
        }
    }
	
	public List<Reserva> buscarPorUsuario(Integer numeroUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
        	String jpql = "SELECT r FROM Reserva r " +
                    "LEFT JOIN FETCH r.pagoSenia " +
                    "LEFT JOIN FETCH r.pagoSaldo " +
                    "LEFT JOIN FETCH r.vehiculo " +
                    "LEFT JOIN FETCH r.tipoEstadia " +
                    "LEFT JOIN FETCH r.lugar " +
                    "WHERE r.id.numeroUsuario = :id " +
                    "ORDER BY r.id.fechaDesde DESC";
        	
            return em.createQuery(jpql, Reserva.class)
                     .setParameter("id", numeroUsuario)
                     .getResultList();
        } finally {
            em.close();
        }
    }
	
	@Override
	public List<Reserva> obtenerActivasPorCochera(int codigoCochera) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        // Traemos las que están EN_CURSO, SALIDA_PARCIAL y las PENDIENTES 
	        // para que el encargado vea quién está por entrar o quién está estacionado
	        String jpql = "SELECT r FROM Reserva r " +
	                      "LEFT JOIN FETCH r.vehiculo " +
	                      "LEFT JOIN FETCH r.usuario " +
	                      "LEFT JOIN FETCH r.tipoEstadia " +
	                      "LEFT JOIN FETCH r.lugar l " +
	                      "LEFT JOIN FETCH r.pagoSenia " +
	                      "LEFT JOIN FETCH r.pagoSaldo " +
	                      "WHERE l.cochera.codigo = :codCochera " +
	                      "AND r.estado IN (estacionamiento.domain.EstadoReserva.PENDIENTE, " +
	                      "                 estacionamiento.domain.EstadoReserva.EN_CURSO, " +
	                      "                 estacionamiento.domain.EstadoReserva.SALIDA_PARCIAL) " +
	                      "ORDER BY r.id.fechaDesde ASC";
	        return em.createQuery(jpql, Reserva.class)
	                 .setParameter("codCochera", codigoCochera)
	                 .getResultList();
	    } finally {
	        em.close();
	    }
	}

	@Override
	public List<Reserva> obtenerTodasLasActivas() {
		EntityManager em = emf.createEntityManager();
		try {
			
	        String jpql = "SELECT r FROM Reserva r " +
	                      "LEFT JOIN FETCH r.vehiculo " +
	                      "LEFT JOIN FETCH r.usuario " +
	                      "LEFT JOIN FETCH r.tipoEstadia " +
	                      "LEFT JOIN FETCH r.lugar " +
	                      "LEFT JOIN FETCH r.pagoSenia " +
	                      "LEFT JOIN FETCH r.pagoSaldo " +
	                      "WHERE r.estado IN (estacionamiento.domain.EstadoReserva.PENDIENTE, " +
	                      "                   estacionamiento.domain.EstadoReserva.EN_CURSO, " +
	                      "                   estacionamiento.domain.EstadoReserva.SALIDA_PARCIAL) " +
	                      "ORDER BY r.id.fechaDesde ASC";  
	        return em.createQuery(jpql, Reserva.class).getResultList();
	    } finally {
	        em.close();
	    }
	}
}