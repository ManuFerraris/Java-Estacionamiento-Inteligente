package estacionamiento.repository.mysql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PrecioHistoricoTV;
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
            return em.createQuery("SELECT ph FROM PrecioHistoricoTV ph", PrecioHistoricoTV.class).getResultList();
        } finally {
            em.close();
        }
    }
	
    @Override
    public void guardar(PrecioHistoricoTV precioHistoricoTV) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
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
    public void actualizar(int codigoTV, LocalDateTime fechaDesde, PrecioHistoricoTV phTVNuevosDatos) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            PrecioHistoricoTVId claveCompuesta = new PrecioHistoricoTVId(codigoTV, fechaDesde);
            PrecioHistoricoTV phTVExistente = em.find(PrecioHistoricoTV.class, claveCompuesta);
            
            if (phTVExistente != null) {
                // Actualizamos el precio, porque la clave primaria no se puede modificar
                phTVExistente.setPrecio(phTVNuevosDatos.getPrecio());
                em.getTransaction().commit();
                System.out.println("MySQL: Precio histórico de vehículo actualizado correctamente.");
            } else {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("MySQL: No se encontró histórico para actualizar.");
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
    public void eliminar(int codigoTV, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            PrecioHistoricoTVId claveCompuesta = new PrecioHistoricoTVId(codigoTV, fechaDesde);
            PrecioHistoricoTV historioAEliminar = em.find(PrecioHistoricoTV.class, claveCompuesta);
            
            if (historioAEliminar != null) {
                em.remove(historioAEliminar);
                em.getTransaction().commit();
                System.out.println("MySQL: Histórico eliminado correctamente.");
            } else {
                em.getTransaction().rollback();
                System.out.println("MySQL: El histórico no fue encontrado para ser eliminado.");
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
    public BigDecimal obtenerPrecioVigente(int numeroTipoVehiculo) {
        EntityManager em = emf.createEntityManager();
        try {
            // Como fechaDesde vive adentro de la clave compuesta (@EmbeddedId), 
            // en la consulta JPQL accedemos a ella a través de 'id.fechaDesde'
            String jpql = "SELECT p.precio FROM PrecioHistoricoTV p " +
                          "WHERE p.tipoVehiculo.numero = :numeroTV " +
                          "ORDER BY p.id.fechaDesde DESC";
                          
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("numeroTV", numeroTipoVehiculo);
            
            // Limitamos a 1 para traer únicamente el precio más actual
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