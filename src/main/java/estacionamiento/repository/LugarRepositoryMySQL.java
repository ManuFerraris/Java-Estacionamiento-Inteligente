package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Lugar;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class LugarRepositoryMySQL implements LugarRepository {

	private EntityManager em;
	
	public LugarRepositoryMySQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
	}
	
	@Override
    public void guardar(Lugar lugar) {
        em.getTransaction().begin();
        em.persist(lugar);
        em.getTransaction().commit();
        System.out.println("MySQL: Lugar insertado correctamente en la base de datos.");
    }
	
	@Override
    public Lugar buscarPorClave(int codigo) {
        return em.find(Lugar.class, codigo);
    }
    
    @Override
    public List<Lugar> obtenerTodos() {
        return em.createQuery("SELECT l FROM Lugar l", Lugar.class).getResultList();
    }
    
    @Override
    public void actualizar(int codigo, Lugar lugarNuevosDatos) {
        Lugar lugarExistente = buscarPorClave(codigo);
        
        if (lugarExistente != null) {
            em.getTransaction().begin();
            lugarExistente.setDescripcion(lugarNuevosDatos.getDescripcion());
            lugarExistente.setNumeroPiso(lugarNuevosDatos.getNumeroPiso());
            lugarExistente.setCodigo_cochera(lugarNuevosDatos.getCodigo_cochera());
            em.getTransaction().commit();
            System.out.println("MySQL: Lugar actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el lugar para actualizar.");
        }
    }
    
    @Override
    public void eliminar(int codigo) {
        Lugar lugarAEliminar = buscarPorClave(codigo);
        
        if (lugarAEliminar != null) {
            em.getTransaction().begin();
            em.remove(lugarAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Lugar eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el lugar para eliminar.");
        }
    }
}
