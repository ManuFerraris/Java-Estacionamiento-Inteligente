package estacionamiento.repository;

import java.time.LocalDateTime;

import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.claves.PrecioHistoricoTPId;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PrecioHistoricoTPRepositoryMySQL implements PrecioHistoricoTPRepository {
	
	private EntityManager em;
    
    public PrecioHistoricoTPRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }

	@Override
	public void guardar(PrecioHistoricoTP precioHistorico) {
		em.getTransaction().begin();
        em.persist(precioHistorico);
        em.getTransaction().commit();
        System.out.println("MySQL: PrecioHistoricoTP registrado correctamente en la base de datos.");
		
	}

	@Override
	public PrecioHistoricoTP buscarPorClave(int codigoTP, LocalDateTime fechaDesde) {
		PrecioHistoricoTPId claveCompuesta = new PrecioHistoricoTPId(codigoTP, fechaDesde);
		return em.find(PrecioHistoricoTP.class, claveCompuesta);
	}

	@Override
	public List<PrecioHistoricoTP> obtenerTodos() {
		return em.createQuery("SELECT tp FROM PrecioHistoricoTP tp", PrecioHistoricoTP.class).getResultList();
	}

	@Override
	public void actualizar(int codigoTP, LocalDateTime fechaDesde, PrecioHistoricoTP phTPNuevosDatos) {
		PrecioHistoricoTP phTPExistente = buscarPorClave(codigoTP, fechaDesde);
        
        if (phTPExistente != null) {
            em.getTransaction().begin();
            phTPExistente.setPrecio(phTPNuevosDatos.getPrecio());
            em.getTransaction().commit();
            System.out.println("MySQL: Precio historico del tipo de plan actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el historico para actualizar.");
        }
	}

	@Override
	public void eliminar(int codigoTP, LocalDateTime fechaDesde) {
		PrecioHistoricoTP historioAEliminar = buscarPorClave(codigoTP, fechaDesde);
        
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
