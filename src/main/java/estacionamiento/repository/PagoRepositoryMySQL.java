package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Pago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PagoRepositoryMySQL implements PagoRepository {
	
	// El EntityManager es nuestro canal de comunicación directo con Hibernate/MySQL
    private EntityManager em;
    
    public PagoRepositoryMySQL() {
        // "EstacionamientoPU" debe coincidir EXACTAMENTE con el nombre que pusimos en el persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
    @Override
    public void guardar(Pago pago) {
        // Toda operación de escritura en BD requiere abrir y cerrar una transacción (begin y commit).
        em.getTransaction().begin();
        
        // El método persist() es lo mismo que hacer un INSERT INTO en SQL.
        em.persist(pago);
        
        em.getTransaction().commit();
        System.out.println("MySQL: Pago insertado correctamente en la base de datos.");
    }
    
    @Override
    public Pago buscarPorClave(int numero) {
        // El método find() es como hacer un SELECT * FROM pagos WHERE numero_pago = ?
        return em.find(Pago.class, numero);
    }
    
    @Override
    public List<Pago> obtenerTodos() {
        // Aca usamos JPQL (Java Persistence Query Language)
        // No consultamos a la tabla 'pagos', consultamos a la clase 'Pago'
        return em.createQuery("SELECT p FROM Pago p", Pago.class).getResultList();
    }
    
    @Override
    public void actualizar(int numero, Pago pagoNuevosDatos) {
        Pago pagoExistente = buscarPorClave(numero);
        
        if (pagoExistente != null) {
            em.getTransaction().begin();
            // Al estar dentro de una transacción, los setters modifican la BD automáticamente
            pagoExistente.setMonto(pagoNuevosDatos.getMonto());
            pagoExistente.setEstado(pagoNuevosDatos.getEstado());
            em.getTransaction().commit();
            System.out.println("MySQL: Pago actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el pago para actualizar.");
        }
    }
    
    @Override
    public void eliminar(int numero) {
        Pago pagoAEliminar = buscarPorClave(numero);
        
        if (pagoAEliminar != null) {
            em.getTransaction().begin();
            // El método remove() equivale a DELETE FROM
            em.remove(pagoAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Pago eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el pago para eliminar.");
        }
    }
}
