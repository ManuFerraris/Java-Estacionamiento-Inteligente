package estacionamiento.repository.mysql;

import java.util.List;
import estacionamiento.domain.Vehiculo;
import estacionamiento.repository.VehiculoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class VehiculoRepositoryMySQL implements VehiculoRepository {

	private final EntityManagerFactory emf;

    public VehiculoRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public void guardar(Vehiculo vehiculo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vehiculo);
            em.getTransaction().commit();
            System.out.println("MySQL: Vehículo registrado correctamente.");
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
    public Vehiculo buscarPorPatente(String patente) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Vehiculo.class, patente);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Vehiculo> buscarPorUsuario(Integer numeroUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT v FROM Vehiculo v WHERE v.usuario.numero = :numeroUsuario";
            return em.createQuery(jpql, Vehiculo.class)
                     .setParameter("numeroUsuario", numeroUsuario)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vehiculo v", Vehiculo.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(String patente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Vehiculo vehiculoAEliminar = em.find(Vehiculo.class, patente);
            
            if (vehiculoAEliminar != null) {
                em.remove(vehiculoAEliminar);
                em.getTransaction().commit();
                System.out.println("MySQL: Vehículo eliminado correctamente.");
            } else {
                em.getTransaction().rollback();
                System.out.println("MySQL: El vehículo no fue encontrado.");
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
	public void actualizar(String patente, Vehiculo vehiculo) {
		// TODO Auto-generated method stub
		
	}
}