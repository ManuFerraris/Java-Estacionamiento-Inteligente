package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.TipoVehiculo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TipoVehiculoRepositoryMySQL implements TipoVehiculoRepository {

	private EntityManager em;
	
	public TipoVehiculoRepositoryMySQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
	}
	
	@Override
	public void guardar(TipoVehiculo tipoVehiculo) {
		em.getTransaction().begin();
        em.persist(tipoVehiculo);
        em.getTransaction().commit();
        System.out.println("MySQL: Tipo de vehiculo insertado correctamente en la base de datos.");
	}

	@Override
	public TipoVehiculo buscarPorClave(int numero) {
		return em.find(TipoVehiculo.class, numero);
	}

	@Override
	public List<TipoVehiculo> obtenerTodos() {
		return em.createQuery("SELECT tv FROM TipoVehiculo tv", TipoVehiculo.class).getResultList();
	}

	@Override
	public void actualizar(int numero, TipoVehiculo tvNuevosDatos) {
		TipoVehiculo tvExistente = buscarPorClave(numero);
        
        if (tvExistente != null) {
            em.getTransaction().begin();
            tvExistente.setNombre(tvNuevosDatos.getNombre());
            em.getTransaction().commit();
            System.out.println("MySQL: Tipo de vehiculo actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el tipo de vehiculo para actualizar.");
        }
	}

	@Override
	public void eliminar(int numero) {
		TipoVehiculo tvAEliminar = buscarPorClave(numero);
        
        if (tvAEliminar != null) {
            em.getTransaction().begin();
            em.remove(tvAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Tipo de vehiculo eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el tipo de vehiculo para eliminar.");
        }
	}
}
