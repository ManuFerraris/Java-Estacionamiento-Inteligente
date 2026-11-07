package estacionamiento.repository.mysql;

import java.util.List;

import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.TipoVehiculoRepository;
import estacionamiento.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class TipoVehiculoRepositoryMySQL implements TipoVehiculoRepository {

	private EntityManagerFactory emf;
	
	public TipoVehiculoRepositoryMySQL() {
		this.emf = JPAUtil.getEntityManagerFactory();
	}
	
	@Override
	public void guardar(TipoVehiculo tipoVehiculo) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(tipoVehiculo);
			em.getTransaction().commit();
			System.out.println("MySQL: Tipo de vehiculo insertado correctamente en la base de datos.");
		} catch(Exception e) {
			if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public TipoVehiculo buscarPorClave(int numero) {
		EntityManager em = emf.createEntityManager();
		try {
			return em.find(TipoVehiculo.class, numero);
		} finally {
			em.close(); // Las lecturas no necesitan transacción ni rollback, solo cerrarse.
		}
	}

	@Override
	public List<TipoVehiculo> obtenerTodos() {
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery("SELECT tv FROM TipoVehiculo tv", TipoVehiculo.class).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void actualizar(int numero, TipoVehiculo tvNuevosDatos) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			// Buscamos con el MISMO EntityManager que va a hacer el commit
			TipoVehiculo tvExistente = em.find(TipoVehiculo.class, numero);
	        
	        if (tvExistente != null) {
	            tvExistente.setNombre(tvNuevosDatos.getNombre());
	            em.getTransaction().commit();
	            System.out.println("MySQL: Tipo de vehiculo actualizado correctamente.");
	        } else {
	            throw new IllegalArgumentException("MySQL: No se encontró el tipo de vehiculo para actualizar.");
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
	public void eliminar(int numero) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			// Buscamos con el MISMO EntityManager antes de remover
			TipoVehiculo tvAEliminar = em.find(TipoVehiculo.class, numero);
	        
	        if (tvAEliminar != null) {
	            em.remove(tvAEliminar);
	            em.getTransaction().commit();
	            System.out.println("MySQL: Tipo de vehiculo eliminado correctamente.");
	        } else {
	            System.out.println("MySQL: No se encontró el tipo de vehiculo para eliminar.");
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