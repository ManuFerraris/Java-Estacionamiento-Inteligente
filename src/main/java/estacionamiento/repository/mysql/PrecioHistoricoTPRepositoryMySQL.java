package estacionamiento.repository.mysql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.claves.PrecioHistoricoTPId;
import estacionamiento.repository.PrecioHistoricoTPRepository;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class PrecioHistoricoTPRepositoryMySQL implements PrecioHistoricoTPRepository {
	
private EntityManagerFactory emf;
    
    public PrecioHistoricoTPRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public void guardar(PrecioHistoricoTP precio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            // Merge maneja limpiamente la inserción con claves compuestas autogeneradas por constructores
            em.merge(precio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public PrecioHistoricoTP buscarPorClave(PrecioHistoricoTPId id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(PrecioHistoricoTP.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PrecioHistoricoTP> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            // Ordenamos por fecha descendente para que en la vista el precio actual salga primero
            return em.createQuery(
                "SELECT p FROM PrecioHistoricoTP p ORDER BY p.id.fechaDesde DESC", 
                PrecioHistoricoTP.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(int codigoPlan, LocalDateTime fechaDesde, BigDecimal nuevoPrecio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            PrecioHistoricoTPId id = new PrecioHistoricoTPId(codigoPlan, fechaDesde);
            PrecioHistoricoTP precioExistente = em.find(PrecioHistoricoTP.class, id);
            
            if (precioExistente != null) {
            	precioExistente.setPrecio(nuevoPrecio);
                tx.commit();
                System.out.println("MySQL: Precio histórico actualizado con éxito.");
            } else {
                throw new IllegalArgumentException("MySQL: Registro histórico no encontrado.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(int codigoPlan, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            PrecioHistoricoTPId id = new PrecioHistoricoTPId(codigoPlan, fechaDesde);
            PrecioHistoricoTP precioAEliminar = em.find(PrecioHistoricoTP.class, id);
            
            if (precioAEliminar != null) {
                em.remove(precioAEliminar);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
}
