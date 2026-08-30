package estacionamiento.repository.memoria;

import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.claves.PagoSuscripcionId;
import estacionamiento.repository.PagoSuscripcionRepository;


public class PagoSuscripcionRepositoryMemoria implements PagoSuscripcionRepository {
    
    private List<PagoSuscripcion> baseDeDatosMemoria;

    public PagoSuscripcionRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<PagoSuscripcion> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public PagoSuscripcion buscarPorClave(PagoSuscripcionId id) {
        for (PagoSuscripcion ps : this.baseDeDatosMemoria) {
            // La magia de tener un @Embeddable con su método equals() bien definido
            if (ps.getId().equals(id)) {
                return ps;
            }
        }
        return null;
    }

    @Override
    public void guardar(PagoSuscripcion pagoSuscripcion) {
        if (buscarPorClave(pagoSuscripcion.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un Pago idéntico para esta suscripción en el sistema.");
        }
        
        this.baseDeDatosMemoria.add(pagoSuscripcion);
        System.out.println("Memoria: Pago guardado con éxito. Cliente ID: " + 
            pagoSuscripcion.getSuscripcion().getUsuario().getNumero() + " | Plan: " +
            pagoSuscripcion.getSuscripcion().getTipoPlan().getNombre()
        );
    }

    @Override
    public void actualizar(PagoSuscripcion pago) {
        // En memoria, el objeto ya fue modificado por el Servicio.
        // Solo verificamos que exista para simular el comportamiento de una BD real.
        PagoSuscripcion psExistente = buscarPorClave(pago.getId());

        if (psExistente != null) {
            System.out.println("Memoria: Pago actualizado con éxito a estado: " + pago.getEstado());
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el pago en memoria.");
        }
    }

}
