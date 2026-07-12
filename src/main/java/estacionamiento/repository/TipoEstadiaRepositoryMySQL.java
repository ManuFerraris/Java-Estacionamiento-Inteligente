package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.TipoEstadia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TipoEstadiaRepositoryMySQL implements TipoEstadiaRepository {

	private EntityManager em;
    
    public TipoEstadiaRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
	@Override
	public void guardar(TipoEstadia tipoEstadia) {
		em.getTransaction().begin();
        em.persist(tipoEstadia);
        em.getTransaction().commit();
        System.out.println("MySQL: Usuario insertado correctamente en la base de datos.");
	}

	@Override
	public TipoEstadia buscarPorNumero(int numero) {
		return em.find(TipoEstadia.class, numero);
	}

	@Override
	public List<TipoEstadia> obtenerTodos() {
		return em.createQuery("SELECT te FROM TipoEstadia te", TipoEstadia.class).getResultList();
	}

	@Override
	public void actualizar(int numero, TipoEstadia tipoEstadiaNuevosDatos) {
		TipoEstadia teExistente = buscarPorNumero(numero);
        
        if (teExistente != null) {
            em.getTransaction().begin();
            teExistente.setDescripcion(tipoEstadiaNuevosDatos.getDescripcion());
            teExistente.setCupo(tipoEstadiaNuevosDatos.getCupo());
            em.getTransaction().commit();
            System.out.println("Tipo De Estadia actualizado: " + numero 
					 + " " + teExistente.getDescripcion() 
					 + " " + teExistente.getCupo());
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el usuario para actualizar.");
        }
	}

	@Override
	public void eliminar(int numero) {
		TipoEstadia teAEliminar = buscarPorNumero(numero);
        
        if (teAEliminar != null) {
            em.getTransaction().begin();
            em.remove(teAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Tipo Estadia eliminada correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el tipo de estadia para eliminar.");
        }
	}
	

}
