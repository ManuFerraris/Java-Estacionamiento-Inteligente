package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.Beneficio;
import estacionamiento.domain.claves.BeneficioId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class BeneficioRepositoryMySQL implements BeneficioRepository{

	private EntityManager em;
    
    public BeneficioRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
	@Override
	public void guardar(Beneficio beneficio) {
		em.getTransaction().begin();
        em.persist(beneficio);
        em.getTransaction().commit();
        System.out.println("MySQL: Beneficio registrado correctamente en la base de datos.");
	}

	@Override
	public Beneficio buscarPorClave(int codTP, int numero) {
		BeneficioId claveCompuesta = new BeneficioId(codTP, numero);
		return em.find(Beneficio.class, claveCompuesta);
	}

	@Override
	public List<Beneficio> obtenerTodos() {
		return em.createQuery("SELECT b FROM Beneficio b", Beneficio.class).getResultList();
	}

	@Override
	public void actualizar(int codTP, int numero, Beneficio beneficioNuevosDatos) {
		Beneficio benExistente = buscarPorClave(codTP, numero);
        
        if (benExistente != null) {
            em.getTransaction().begin();
            benExistente.setDescripcion(beneficioNuevosDatos.getDescripcion());
            benExistente.setFechaBaja(beneficioNuevosDatos.getFechaBaja());
            em.getTransaction().commit();
            System.out.println("MySQL: Beneficio actualizado con éxito.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el beneficio para actualizar.");
        }
	}

	@Override
	public void eliminar(int codTP, int numero) {
		Beneficio benAEliminar = buscarPorClave(codTP, numero);
        
        if (benAEliminar != null) {
            em.getTransaction().begin();
            em.remove(benAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Beneficio eliminado correctamente.");
        } else {
            System.out.println("MySQL: El beneficio no fue encontrado para ser eliminado.");
        }
	}

}
