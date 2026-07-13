package estacionamiento.repository;

import java.util.List;
import java.util.Optional;
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
    public Lugar guardar(Lugar lugar) {
        em.getTransaction().begin();
        em.persist(lugar);
        em.getTransaction().commit();
        System.out.println("MySQL: Lugar insertado correctamente en la base de datos.");

        return lugar;
    }
    
    @Override
    public Optional<Lugar> buscarPorClave(String codigo) {
    
        Lugar lugarEncontrado = em.find(Lugar.class, codigo);
        return Optional.ofNullable(lugarEncontrado);
    }
    
    @Override
    public List<Lugar> obtenerTodos() {
        return em.createQuery("SELECT l FROM Lugar l", Lugar.class).getResultList();
    }
    
    @Override
    public void actualizar(String codigo, Lugar lugarNuevosDatos) {
        Optional<Lugar> lugarExistenteOpt = buscarPorClave(codigo);
        
        if (lugarExistenteOpt.isPresent()) {
            Lugar lugarExistente = lugarExistenteOpt.get(); 
            
            em.getTransaction().begin();
            
            lugarExistente.setDescripcion(lugarNuevosDatos.getDescripcion());
            lugarExistente.setNumeroPiso(lugarNuevosDatos.getNumeroPiso());
            lugarExistente.setCodigoCochera(lugarNuevosDatos.getCodigoCochera());
            em.getTransaction().commit();
            
            System.out.println("MySQL: Lugar actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el lugar para actualizar.");
        }
    }
    
    @Override
    public void eliminar(String codigo) {
        Optional<Lugar> lugarAEliminarOpt = buscarPorClave(codigo);
        
        if (lugarAEliminarOpt.isPresent()) {
            em.getTransaction().begin();
           
            em.remove(lugarAEliminarOpt.get());
            em.getTransaction().commit();
            System.out.println("MySQL: Lugar eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el lugar para eliminar.");
        }
    }
}