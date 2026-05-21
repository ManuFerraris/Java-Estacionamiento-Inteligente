package estacionamiento.repository;

import java.time.LocalDateTime;

import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.domain.claves.PrecioHistoricoTVId;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PrecioHistoricoTVRepositorySQL implements PrecioHistoricoTVRepository{
	
	private EntityManager em;
    
    public PrecioHistoricoTVRepositorySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
    //GetOne
	@Override
	public PrecioHistoricoTV buscarPorClave(int codigoTV, LocalDateTime fechaDesde) {
		PrecioHistoricoTVId claveCompuesta = new PrecioHistoricoTVId(codigoTV, fechaDesde);
		return em.find(PrecioHistoricoTV.class, claveCompuesta);
	}
	
	//findAll
	@Override
	public List<PrecioHistoricoTV> obtenerTodos(){
		return em.createQuery("SELECT ph FROM PrecioHistoricoTV ph", PrecioHistoricoTV.class).getResultList();
	}
	
	//save
	@Override
    public void guardar(PrecioHistoricoTV precioHistoricoTV) {
        em.getTransaction().begin();
        em.persist(precioHistoricoTV);
        em.getTransaction().commit();
        System.out.println("MySQL: PrecioHistoricoTV registrado correctamente en la base de datos.");
    }
	
	@Override
    public void actualizar(int codigoTV, LocalDateTime fechaDesde, PrecioHistoricoTV phTVNuevosDatos) {
		PrecioHistoricoTV phTVExistente = buscarPorClave(codigoTV, fechaDesde);
        
        if (phTVExistente != null) {
            em.getTransaction().begin();
            phTVExistente.setPrecio(phTVNuevosDatos.getPrecio());
            em.getTransaction().commit();
            System.out.println("MySQL: Precio historico de vehiculo actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró historico para actualizar.");
        }
    }
	
	@Override
    public void eliminar(int codigoTV, LocalDateTime fechaDesde) {
		PrecioHistoricoTV historioAEliminar = buscarPorClave(codigoTV, fechaDesde);
        
        if (historioAEliminar != null) {
            em.getTransaction().begin();
            em.remove(historioAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Historico eliminado correctamente.");
        } else {
            System.out.println("MySQL: El historico no fue encontrado para ser eliminado.");
        }
    }
}
