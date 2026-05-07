package estacionamiento.repository;
import estacionamiento.domain.Pago;

import java.util.ArrayList;
import java.util.List;

public class PagoRepositoryMemoria implements PagoRepository{
	private List<Pago> baseDeDatosMemoria;

    public PagoRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Pago> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public Pago buscarPorClave(int numero) {
        for (Pago p : this.baseDeDatosMemoria) {
            
            if (p.getNumero() == numero) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void guardar(Pago pago) {
        
        int numero = pago.getNumero();

        if (buscarPorClave(numero) != null) {
            throw new IllegalArgumentException("Ya existe un Pago en el sistema con ese numero.");
        }
        this.baseDeDatosMemoria.add(pago);
        System.out.println("Pago guardado con éxito, numero de pago: " + numero);
    }

    @Override
    public void actualizar(int numero, Pago pagoNuevosDatos) {
        Pago pagoExistente = buscarPorClave(numero);

        if (pagoExistente != null) {
        	pagoExistente.setFechaHora(pagoNuevosDatos.getFechaHora());
            pagoExistente.setMonto(pagoNuevosDatos.getMonto());
            pagoExistente.setEstado(pagoNuevosDatos.getEstado());
            pagoExistente.setTipoPago(pagoNuevosDatos.getTipoPago());
            System.out.println("Pago actualizado con exito, numero: " + numero + " "
            		+ pagoNuevosDatos.getTipoPago() + " "
            		+ pagoNuevosDatos.getEstado() + " "
            );
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el pago.");
        }
    }

    @Override
    public void eliminar(int numero) {
        Pago pagoAEliminar = buscarPorClave(numero);

        if (pagoAEliminar != null) {
            this.baseDeDatosMemoria.remove(pagoAEliminar);
            System.out.println("Pago eliminado con éxito.");
        } else {
            System.out.println("No se encontró el pago para eliminar.");
        }
    }
}
