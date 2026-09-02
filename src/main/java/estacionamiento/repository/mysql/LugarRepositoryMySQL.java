package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.Lugar;
import estacionamiento.repository.LugarRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class LugarRepositoryMySQL implements LugarRepository {

    private final EntityManagerFactory emf;
    
    public LugarRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public Lugar guardar(Lugar lugar) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(lugar);
            em.getTransaction().commit();
            System.out.println("MySQL: Lugar insertado correctamente en la base de datos.");
            return lugar;
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
    public Lugar buscarPorClave(int codigo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Lugar.class, codigo);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Lugar> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Lugar l", Lugar.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void actualizar(int codigo, Lugar lugarNuevosDatos) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            // Volvemos a buscar el lugar DENTRO del mismo EntityManager para que quede "attachado" (gestionado por JPA)
            Lugar lugarExistente = em.find(Lugar.class, codigo);
            
            if (lugarExistente != null) {
                lugarExistente.setDescripcion(lugarNuevosDatos.getDescripcion());
                lugarExistente.setNumeroPiso(lugarNuevosDatos.getNumeroPiso());
                lugarExistente.setCochera(lugarNuevosDatos.getCochera());
                
                em.getTransaction().commit();
                System.out.println("MySQL: Lugar actualizado correctamente.");
            } else {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("MySQL: No se encontró el lugar para actualizar.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {em.getTransaction().rollback();}
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override
    public void eliminar(int codigo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            Lugar lugarAEliminar = em.find(Lugar.class, codigo);
            
            if (lugarAEliminar != null) {
                em.remove(lugarAEliminar);
                em.getTransaction().commit();
                System.out.println("MySQL: Lugar eliminado correctamente.");
            } else {
                em.getTransaction().rollback();
                System.out.println("MySQL: No se encontró el lugar para eliminar.");
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
    public Lugar obtenerPrimerLugarLibre(int idCochera, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        EntityManager em = emf.createEntityManager();
        try {
        	// Agrego el adicional de 10 minutos antes y despues para evitar que los autos
        	// se queden estancados en la entrada.
        	LocalDateTime inicioConBuffer = fechaDesde.minusMinutes(10);
        	LocalDateTime finConBuffer = fechaHasta.plusMinutes(10);
            String jpql = "SELECT l FROM Lugar l WHERE l.cochera.codigo = :idCochera AND l.codigo NOT IN (" +
                          "    SELECT r.lugar.codigo FROM Reserva r " +
                          "    WHERE r.estado IN ('PENDIENTE', 'EN_CURSO', 'SALIDA_PARCIAL') " +
                          "    AND (r.id.fechaDesde < :fechaHasta AND r.fechaHastaTentativa > :fechaDesde)" +
                          ")";
                          
            TypedQuery<Lugar> query = em.createQuery(jpql, Lugar.class);
            query.setParameter("idCochera", idCochera); // Filtro crucial porque sino podemos filtrar por otras cocheras o todas.
            query.setParameter("fechaDesde", inicioConBuffer);
            query.setParameter("fechaHasta", finConBuffer);
            query.setMaxResults(1); 

            List<Lugar> resultados = query.getResultList();
            
            if (resultados.isEmpty()) return null;
            return resultados.get(0); // Devolvemos el primero
            
        } finally {
            em.close();
        }
    }
}