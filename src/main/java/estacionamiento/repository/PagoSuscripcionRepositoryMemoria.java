package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.PagoSuscripcion;


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
    public PagoSuscripcion buscarPorClave(int codigoTP, int numeroUsuario,
    		LocalDateTime fechaDesde,LocalDateTime fechaHoraEmision) {
        for (PagoSuscripcion ps : this.baseDeDatosMemoria) {
            
            if (ps.getSuscripcion().getTipoPlan().getCodigo() == codigoTP &&
                    ps.getSuscripcion().getUsuario().getNumero() == numeroUsuario &&
                    ps.getSuscripcion().getFechaDesde().equals(fechaDesde) &&
                    ps.getFechaHoraEmision().equals(fechaHoraEmision)) {
                return ps;
            }
        }
        return null;
    }

    @Override
    public void guardar(PagoSuscripcion pagoSuscripcion) {
        
    	int numCli = pagoSuscripcion.getSuscripcion().getUsuario().getNumero();
    	int codTP = pagoSuscripcion.getSuscripcion().getTipoPlan().getCodigo();
    	LocalDateTime susFech = pagoSuscripcion.getSuscripcion().getFechaDesde();
    	LocalDateTime psEmi = pagoSuscripcion.getFechaHoraEmision();
        if (buscarPorClave(codTP, numCli, susFech, psEmi) != null) {
            throw new IllegalArgumentException("Ya existe un Pago para esta suscripcion en el sistema.");
        }
        this.baseDeDatosMemoria.add(pagoSuscripcion);
        System.out.println("Pago guardado con éxito, datos del pago: " + 
        	pagoSuscripcion.getSuscripcion().getUsuario().getNumero() + " "  +
        	pagoSuscripcion.getSuscripcion().getTipoPlan().getNombre() + " "  +
        	pagoSuscripcion.getSuscripcion().getFechaDesde()
        );
    }

    @Override
    public void actualizar(int codigoTP, int numeroUsuario,
    		LocalDateTime fechaDesde,LocalDateTime fechaHoraEmision, PagoSuscripcion psNuevosDatos) {
    	PagoSuscripcion psExistente = buscarPorClave(codigoTP, numeroUsuario, fechaDesde, fechaHoraEmision);

        if (psExistente != null) {
        	psExistente.setFechaHoraPago(psNuevosDatos.getFechaHoraPago());
        	psExistente.setMonto(psNuevosDatos.getMonto());
        	psExistente.setEstado(psNuevosDatos.getEstado());
        	psExistente.setTipoPago(psNuevosDatos.getTipoPago());
        	System.out.println("Pago actualizado con éxito, datos del pago: " + 
        		psNuevosDatos.getSuscripcion().getUsuario().getNumero() + " "  +
        		psNuevosDatos.getSuscripcion().getTipoPlan().getNombre() + " "  +
                psNuevosDatos.getSuscripcion().getFechaDesde() + " "  +
                psNuevosDatos.getFechaHoraPago() + " "  +
        		psNuevosDatos.getMonto() + " "  +
        		psNuevosDatos.getEstado()
            );
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el pago de la suscripcion.");
        }
    }

    @Override
    public void eliminar(int codigoTP, int numeroUsuario,
    		LocalDateTime fechaDesde,LocalDateTime fechaHoraEmision) {
    	PagoSuscripcion psExistente = buscarPorClave(codigoTP, numeroUsuario, fechaDesde, fechaHoraEmision);

        if (psExistente != null) {
            this.baseDeDatosMemoria.remove(psExistente);
            System.out.println("Pago de la suscripcion eliminado con éxito.");
        } else {
            System.out.println("No se encontró el pago de dicha suscripcion para eliminar.");
        }
    }

}
