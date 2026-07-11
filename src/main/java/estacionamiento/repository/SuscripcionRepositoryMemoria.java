package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import estacionamiento.domain.Suscripcion;

public class SuscripcionRepositoryMemoria implements SuscripcionRepository{

	private List<Suscripcion> baseDeDatosMemoria;

    public SuscripcionRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Suscripcion> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }
    
	@Override
	public Suscripcion buscarPorClave(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
		for (Suscripcion s : this.baseDeDatosMemoria) {
            if (s.getTipoPlan().getCodigo() == codigoTP &&
            	s.getUsuario().getNumero() == numeroUsuario &&
                s.getFechaDesde().equals(fechaDesde)) {
                return s;
            }
        }
        return null;
	}

	@Override
	public void guardar(Suscripcion suscripcion) {
		int codTp = suscripcion.getTipoPlan().getCodigo();
		int numUsu = suscripcion.getUsuario().getNumero();
        LocalDateTime fecha = suscripcion.getFechaDesde();

        if (buscarPorClave(codTp, numUsu, fecha) != null) {
            throw new IllegalArgumentException("Ya existe una suscripcion para el usuario " + suscripcion.getUsuario().getNombre() + " " + suscripcion.getUsuario().getApellido()
            		+ " a la fecha " + fecha + "para el tipo de plan " + suscripcion.getTipoPlan().getNombre());
        }
        
        this.baseDeDatosMemoria.add(suscripcion);
        System.out.println("Suscripcion guardada, usuario: " + suscripcion.getUsuario().getNombre() + " " + suscripcion.getUsuario().getApellido()
        		+ "\n Fecha: " + fecha + "\n Tipo de plan: " + suscripcion.getTipoPlan().getNombre());
	}



	@Override
	public void actualizar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde, Suscripcion suscripcionNuevosDatos) {
		Suscripcion suscripcionExistente = buscarPorClave(codigoTP, numeroUsuario, fechaDesde);

        if (suscripcionExistente != null) {
        	suscripcionExistente.setFechaHasta(suscripcionNuevosDatos.getFechaHasta());
        	suscripcionExistente.setEstado(suscripcionNuevosDatos.getEstado());
            
            System.out.println("Suscripcion actualizada con éxito " + suscripcionNuevosDatos);
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la suscripcion.");
        }
	}

	@Override
	public void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
		
		Suscripcion suscripcionAEliminar = buscarPorClave(codigoTP, numeroUsuario, fechaDesde);
        if (suscripcionAEliminar != null) {
            this.baseDeDatosMemoria.remove(suscripcionAEliminar);
            System.out.println("Suscripcion eliminada con éxito.");
        } else {
            System.out.println("No se encontró la suscripcion para eliminar.");
        }
	}
}
