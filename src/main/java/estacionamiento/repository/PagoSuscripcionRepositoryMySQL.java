package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.claves.PagoSuscripcionId;
import estacionamiento.domain.claves.SuscripcionId;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PagoSuscripcionRepositoryMySQL implements PagoSuscripcionRepository {

	private EntityManager em;
    
    public PagoSuscripcionRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
	@Override
	public void guardar(PagoSuscripcion pagoSuscripcion) {
		em.getTransaction().begin();
        em.persist(pagoSuscripcion);
        em.getTransaction().commit();
        System.out.println("MySQL: Pago de suscripcion registrado correctamente en la base de datos.");
	}

	@Override
	public PagoSuscripcion buscarPorClave(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde,
			LocalDateTime fechaHoraEmision) {
		SuscripcionId suscripcionId = new SuscripcionId(numeroUsuario, codigoTP, fechaDesde);
		PagoSuscripcionId claveCompuesta = new PagoSuscripcionId(suscripcionId, fechaHoraEmision);
		return em.find(PagoSuscripcion.class, claveCompuesta);
	}

	@Override
	public List<PagoSuscripcion> obtenerTodos() {
		return em.createQuery("SELECT ps FROM PagoSuscripcion ps", PagoSuscripcion.class).getResultList();
	}

	@Override
	public void actualizar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde, LocalDateTime fechaHoraEmision,
			PagoSuscripcion psNuevosDatos) {
		PagoSuscripcion psExistente = buscarPorClave(codigoTP,numeroUsuario, fechaDesde, fechaHoraEmision);
        
        if (psExistente != null) {
            em.getTransaction().begin();
            psExistente.setFechaHoraPago(psNuevosDatos.getFechaHoraPago());
        	psExistente.setMonto(psNuevosDatos.getMonto());
        	psExistente.setEstado(psNuevosDatos.getEstado());
        	psExistente.setTipoPago(psNuevosDatos.getTipoPago());
            em.getTransaction().commit();
            System.out.println("Pago actualizado con éxito, datos del pago: " + 
            		psNuevosDatos.getSuscripcion().getUsuario().getNumero() + " "  +
            		psNuevosDatos.getSuscripcion().getTipoPlan().getNombre() + " "  +
                    psNuevosDatos.getSuscripcion().getFechaDesde() + " "  +
                    psNuevosDatos.getFechaHoraPago() + " "  +
            		psNuevosDatos.getMonto() + " "  +
            		psNuevosDatos.getEstado()
                );
        } else {
            throw new IllegalArgumentException("MySQL: No se encontro el pago de suscripcion para actualizar.");
        }
	}

	@Override
	public void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde, LocalDateTime fechaHoraEmision) {
		PagoSuscripcion psAEliminar = buscarPorClave(codigoTP,numeroUsuario, fechaDesde, fechaHoraEmision);
        
        if (psAEliminar != null) {
            em.getTransaction().begin();
            em.remove(psAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Pago de suscripcion eliminado correctamente.");
        } else {
            System.out.println("MySQL: El pago a dicha suscripcion no fue encontrado para ser eliminado.");
        }
	}

}
