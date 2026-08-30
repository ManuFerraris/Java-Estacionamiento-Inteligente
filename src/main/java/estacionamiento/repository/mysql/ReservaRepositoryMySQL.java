package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

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
    public Reserva buscarPorClave(ReservaId claveCompuesta) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Reserva.class, claveCompuesta);
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
	public Reserva buscarPorClave(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde, Reserva reserva) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde) {
		// TODO Auto-generated method stub
		
	}
}