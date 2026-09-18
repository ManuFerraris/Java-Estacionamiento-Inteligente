package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.domain.claves.LugarTipoEstadiaId;
import estacionamiento.repository.LugarTipoEstadiaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class LugarTipoEstadiaRepositoryMySQL implements LugarTipoEstadiaRepository {

    private final EntityManagerFactory emf;

    public LugarTipoEstadiaRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Usamos merge porque la clave primaria compuesta se construye manualmente antes de guardar
            em.merge(lugarTipoEstadia); 
            em.getTransaction().commit();
            System.out.println("MySQL: Asignación de Lugar-Estadía guardada exitosamente.");
            return lugarTipoEstadia;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error guardando en BD: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public LugarTipoEstadia findById(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        try {
            LugarTipoEstadiaId id = new LugarTipoEstadiaId(codigoLugar, numeroTipoEstadia, fechaDesde);
            return em.find(LugarTipoEstadia.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<LugarTipoEstadia> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM LugarTipoEstadia l", LugarTipoEstadia.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LugarTipoEstadiaId id = new LugarTipoEstadiaId(codigoLugar, numeroTipoEstadia, fechaDesde);
            LugarTipoEstadia obj = em.find(LugarTipoEstadia.class, id);
            
            if (obj != null) {
                em.remove(obj);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar asignación: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}