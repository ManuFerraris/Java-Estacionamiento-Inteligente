package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.claves.SuscripcionId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class SuscripcionRepositoryMySQL implements SuscripcionRepository{

	private EntityManager em;
    
    public SuscripcionRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
	@Override
	public Suscripcion buscarPorClave(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
		SuscripcionId claveCompuesta = new SuscripcionId(codigoTP, numeroUsuario, fechaDesde);
		return em.find(Suscripcion.class, claveCompuesta);
	}

	@Override
	public void guardar(Suscripcion suscripcion) {
		em.getTransaction().begin();
        em.persist(suscripcion);
        em.getTransaction().commit();
        System.out.println("MySQL: Suscripcion registrada correctamente en la base de datos.");
	}

	@Override
	public List<Suscripcion> obtenerTodos() {
		return em.createQuery("SELECT s FROM Suscripcion s", Suscripcion.class).getResultList();
	}

	@Override
	public void actualizar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde, Suscripcion suscripcionNuevosDatos) {
		Suscripcion suscripcionExistente = buscarPorClave(codigoTP, numeroUsuario, fechaDesde);
        
        if (suscripcionExistente != null) {
            em.getTransaction().begin();
            suscripcionExistente.setFechaHasta(suscripcionNuevosDatos.getFechaHasta());
        	suscripcionExistente.setEstado(suscripcionNuevosDatos.getEstado());
            em.getTransaction().commit();
            System.out.println("MySQL: Suscripcion actualizada correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró suscripcion para actualizar.");
        }
	}

	@Override
	public void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
		Suscripcion susAEliminar = buscarPorClave(codigoTP, numeroUsuario, fechaDesde);
        
        if (susAEliminar != null) {
            em.getTransaction().begin();
            em.remove(susAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Suscripcion eliminada correctamente.");
        } else {
            System.out.println("MySQL: La suscripcion no fue encontrada para ser eliminada.");
        }
	}
}
