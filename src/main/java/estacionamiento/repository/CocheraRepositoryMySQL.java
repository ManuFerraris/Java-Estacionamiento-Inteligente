package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Cochera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CocheraRepositoryMySQL implements CocheraRepository {
	private EntityManager em;
    
    public CocheraRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
    // El famoso 'getOne()' de diseño
    @Override
    public Cochera buscarPorClave(int codigo) {
        return em.find(Cochera.class, codigo);
    }
    
    //El famoso 'FindAll()' de diseño
    @Override
    public List<Cochera> obtenerTodos() {
        return em.createQuery("SELECT c FROM Cochera c", Cochera.class).getResultList();
    }
    
    //El 'save()' del amado Aldo que si te lo olvidavas te bochaba.
    @Override
    public void guardar(Cochera cochera) {
        em.getTransaction().begin();
        em.persist(cochera);
        em.getTransaction().commit();
        System.out.println("MySQL: Cochera registrada correctamente en la base de datos.");
    }
    
    @Override
    public void actualizar(int codigo, Cochera cocheraNuevosDatos) {
        Cochera cocheraExistente = buscarPorClave(codigo);
        
        if (cocheraExistente != null) {
            em.getTransaction().begin();
            cocheraExistente.setNombre(cocheraNuevosDatos.getNombre());
            cocheraExistente.setDescripcion(cocheraNuevosDatos.getDescripcion());
            cocheraExistente.setDireccion(cocheraNuevosDatos.getDireccion());
            em.getTransaction().commit();
            System.out.println("MySQL: Cochera actualizada correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró la cochera para actualizar.");
        }
    }
    
    @Override
    public void eliminar(int codigo) {
        Cochera cocheraAEliminar = buscarPorClave(codigo);
        
        if (cocheraAEliminar != null) {
            em.getTransaction().begin();
            em.remove(cocheraAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Cochera eliminada correctamente.");
        } else {
            System.out.println("MySQL: La cochera no fue encontrada para ser eliminada.");
        }
    }
}
