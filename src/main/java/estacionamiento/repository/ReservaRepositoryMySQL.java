package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ReservaRepositoryMySQL implements ReservaRepository {

	private EntityManager em;
    
    public ReservaRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
	@Override
	public void guardar(Reserva reserva) {
		em.getTransaction().begin();
        em.persist(reserva);
        em.getTransaction().commit();
        System.out.println("MySQL: Reserva registrada correctamente en la base de datos.");
	}

	@Override
	public Reserva buscarPorClave(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde) {
		ReservaId claveCompuesta = new ReservaId(patente, numeroUsu, numeroTE, fechaDesde);
		return em.find(Reserva.class, claveCompuesta);
	}

	@Override
	public List<Reserva> obtenerTodos() {
		return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
	}

	@Override
	public void actualizar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde, Reserva reservaNuevosDatos) {
		Reserva reservaExistente = buscarPorClave(patente, numeroUsu, numeroTE, fechaDesde);
        
        if (reservaExistente != null) {
            em.getTransaction().begin();
            reservaExistente.setFechaHastaTentativa(reservaNuevosDatos.getFechaHastaTentativa());
            reservaExistente.setFechaHastaReal(reservaNuevosDatos.getFechaHastaReal());
            reservaExistente.setEstado(reservaNuevosDatos.getEstado());
            reservaExistente.setSenia(reservaNuevosDatos.getSenia());
            reservaExistente.setLugar(reservaNuevosDatos.getLugar());
            reservaExistente.setPago(reservaNuevosDatos.getPago());
            em.getTransaction().commit();
            System.out.println("MySQL: Reserva actualizada correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró la reserva para actualizar.");
        }
	}

	@Override
	public void eliminar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde) {
		Reserva resAEliminar = buscarPorClave(patente, numeroUsu, numeroTE, fechaDesde);
        
        if (resAEliminar != null) {
            em.getTransaction().begin();
            em.remove(resAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Reserva eliminada correctamente.");
        } else {
            System.out.println("MySQL: La reserva no fue encontrada para ser eliminada.");
        }
	}
}
