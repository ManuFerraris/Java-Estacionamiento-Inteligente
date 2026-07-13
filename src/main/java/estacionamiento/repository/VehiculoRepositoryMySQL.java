package estacionamiento.repository;

import java.util.List;
import java.util.Optional;
import estacionamiento.domain.Vehiculo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class VehiculoRepositoryMySQL implements VehiculoRepository {

    private EntityManager em;
    
    public VehiculoRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        em.getTransaction().begin();
        em.persist(vehiculo);
        em.getTransaction().commit();
        System.out.println("MySQL: Vehículo guardado correctamente (Patente: " + vehiculo.getPatente() + ").");
        
        return vehiculo;
    }

    @Override
    public Optional<Vehiculo> buscarPorPatente(String patente) {
        // em.find buscará por la @Id definida en la entidad (patente)
        Vehiculo vehiculoEncontrado = em.find(Vehiculo.class, patente);
        return Optional.ofNullable(vehiculoEncontrado);
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        return em.createQuery("SELECT v FROM Vehiculo v", Vehiculo.class).getResultList();
    }

    @Override
    public void actualizar(String patente, Vehiculo vehiculoNuevosDatos) {
        Optional<Vehiculo> vehiculoExistenteOpt = buscarPorPatente(patente);
        
        if (vehiculoExistenteOpt.isPresent()) {
            Vehiculo vehiculoExistente = vehiculoExistenteOpt.get();
            
            em.getTransaction().begin();
            // Actualizamos los campos administrados por JPA
            vehiculoExistente.setDescripcion(vehiculoNuevosDatos.getDescripcion());
            vehiculoExistente.setTipoVehiculo(vehiculoNuevosDatos.getTipoVehiculo());
            em.getTransaction().commit();
            
            System.out.println("MySQL: Vehículo actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el vehículo con patente " + patente + " para actualizar.");
        }
    }

    @Override
    public void eliminar(String patente) {
        Optional<Vehiculo> vehiculoAEliminarOpt = buscarPorPatente(patente);
        
        if (vehiculoAEliminarOpt.isPresent()) {
            em.getTransaction().begin();
            em.remove(vehiculoAEliminarOpt.get());
            em.getTransaction().commit();
            System.out.println("MySQL: Vehículo eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el vehículo para eliminar.");
        }
    }
}