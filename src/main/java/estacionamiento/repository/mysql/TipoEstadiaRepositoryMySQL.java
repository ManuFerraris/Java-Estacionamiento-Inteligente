package estacionamiento.repository.mysql;

import java.util.List;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.TipoEstadiaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TipoEstadiaRepositoryMySQL implements TipoEstadiaRepository {

    private final EntityManagerFactory emf;
    
    public TipoEstadiaRepositoryMySQL() {
        // Solo guardamos el Factory globalmente
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public void guardar(TipoEstadia tipoEstadia) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tipoEstadia);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public TipoEstadia buscarPorNumero(int numero) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TipoEstadia.class, numero);
        } finally {
            em.close();
        }
    }

    @Override
    public List<TipoEstadia> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT te FROM TipoEstadia te", TipoEstadia.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(int numero, TipoEstadia tipoEstadiaNuevosDatos) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            // Buscamos dentro de la misma transacción
            TipoEstadia teExistente = em.find(TipoEstadia.class, numero);
            
            if (teExistente != null) {
                teExistente.setDescripcion(tipoEstadiaNuevosDatos.getDescripcion());
                teExistente.setCupo(tipoEstadiaNuevosDatos.getCupo());
                
                em.getTransaction().commit();
                System.out.println("MySQL: Cupo actualizado correctamente en BD.");
            } else {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("MySQL: No se encontró el tipo de estadía.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(int numero) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TipoEstadia teAEliminar = em.find(TipoEstadia.class, numero);
            
            if (teAEliminar != null) {
                em.remove(teAEliminar);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}