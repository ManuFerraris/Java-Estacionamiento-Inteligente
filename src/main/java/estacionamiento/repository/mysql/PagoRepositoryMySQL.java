package estacionamiento.repository.mysql;

import java.util.List;
import estacionamiento.domain.Pago;
import estacionamiento.repository.PagoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PagoRepositoryMySQL implements PagoRepository {
	
    private EntityManagerFactory emf;
    
    public PagoRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public void guardar(Pago pago) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            // Si el ID es nulo o 0, significa que es un PAGO NUEVO.
            if (pago.getNumero() == null || pago.getNumero() == 0) {
                em.persist(pago); // Persist inserta y nos DEVUELVE el ID generado por MySQL
            } else {
                // Si ya tiene ID, es una actualización
                em.merge(pago);
            }
            
            em.getTransaction().commit();
            System.out.println("MySQL: Pago guardado correctamente. ID generado: " + pago.getNumero());
            
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
    public Pago buscarPorClave(Integer numero) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Pago.class, numero);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Pago> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pago p", Pago.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void actualizar(Integer numero, Pago pagoNuevosDatos) {
        // En JPA moderno, actualizar es simplemente hacer un merge del objeto con el mismo ID
        guardar(pagoNuevosDatos); 
    }
    
    @Override
    public void eliminar(Integer numero) {
        EntityManager em = emf.createEntityManager();
        try {
            Pago pagoAEliminar = em.find(Pago.class, numero);
            if (pagoAEliminar != null) {
                em.getTransaction().begin();
                em.remove(pagoAEliminar);
                em.getTransaction().commit();
                System.out.println("MySQL: Pago eliminado correctamente.");
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
}