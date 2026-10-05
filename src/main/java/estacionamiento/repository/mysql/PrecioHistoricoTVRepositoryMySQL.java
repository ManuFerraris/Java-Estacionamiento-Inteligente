package estacionamiento.repository.mysql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.domain.TipoVehiculo;
import estacionamiento.domain.claves.PrecioHistoricoTVId; 
import estacionamiento.repository.PrecioHistoricoTVRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class PrecioHistoricoTVRepositoryMySQL implements PrecioHistoricoTVRepository {
	
    private final EntityManagerFactory emf;
    
    public PrecioHistoricoTVRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public PrecioHistoricoTV buscarPorClave(int codigoTV, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        try {
            PrecioHistoricoTVId claveCompuesta = new PrecioHistoricoTVId(codigoTV, fechaDesde);
            return em.find(PrecioHistoricoTV.class, claveCompuesta);
        } finally {
            em.close();
        }
    }
	
    @Override
    public List<PrecioHistoricoTV> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT ph FROM PrecioHistoricoTV ph ORDER BY ph.id.fechaDesde DESC", PrecioHistoricoTV.class).getResultList();
        } finally {
            em.close();
        }
    }
	
    @Override
    public void guardar(PrecioHistoricoTV precioHistoricoTV) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            TipoVehiculo tvDesconectado = precioHistoricoTV.getTipoVehiculo();
            if (tvDesconectado != null) {
                TipoVehiculo tvReenganchado = em.merge(tvDesconectado);
                // Le volvemos a setear el objeto, ahora sí gestionado por Hibernate
                precioHistoricoTV.setTipoVehiculo(tvReenganchado);
            }
            
            em.persist(precioHistoricoTV);
            
            em.getTransaction().commit();
            System.out.println("MySQL: PrecioHistoricoTV registrado correctamente en la base de datos.");
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
    public void actualizar(PrecioHistoricoTV precioHistoricoTV) {
    	EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(precioHistoricoTV);
            em.getTransaction().commit();
            System.out.println("MySQL: Precio histórico de vehículo actualizado mediante merge.");
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
    public void eliminar(PrecioHistoricoTV precioHistoricoTV) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            PrecioHistoricoTV entidadGestionada = em.merge(precioHistoricoTV);
            em.remove(entidadGestionada);
            
            em.getTransaction().commit();
            System.out.println("MySQL: Histórico eliminado físicamente con éxito.");
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
    public BigDecimal obtenerPrecioVigente(int numeroTipoVehiculo) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT p.precio FROM PrecioHistoricoTV p " +
                          "WHERE p.tipoVehiculo.numero = :numeroTV " +
                          "ORDER BY p.id.fechaDesde DESC";
                          
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("numeroTV", numeroTipoVehiculo);
            query.setMaxResults(1); 

            List<BigDecimal> resultados = query.getResultList();
            
            if (resultados.isEmpty()) {
                return null; 
            }
            
            return resultados.get(0);
            
        } finally {
            em.close();
        }
    }
}