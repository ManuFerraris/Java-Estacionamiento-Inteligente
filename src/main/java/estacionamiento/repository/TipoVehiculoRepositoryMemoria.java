package estacionamiento.repository;

import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.TipoVehiculo;

public class TipoVehiculoRepositoryMemoria implements TipoVehiculoRepository {

	private List<TipoVehiculo> baseDeDatosMemoria;

    public TipoVehiculoRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }
    
	@Override
	public void guardar(TipoVehiculo tipoVehiculo) {
        
        int numero = tipoVehiculo.getNumero();
        if (buscarPorClave(numero) != null) {
            throw new IllegalArgumentException("Ya existe un tipo de vehiculo en el sistema con ese numero.");
        }
        this.baseDeDatosMemoria.add(tipoVehiculo);
        System.out.println("Tipo de vehiculo guardado con éxito, numero de tipo de vehiculo: " + numero);
	}

	@Override
	public TipoVehiculo buscarPorClave(int numero) {
		for (TipoVehiculo tv : this.baseDeDatosMemoria) {
            
            if (tv.getNumero() == numero) {
                return tv;
            }
        }
        return null;
	}

	@Override
	public List<TipoVehiculo> obtenerTodos() {
		return this.baseDeDatosMemoria;
	}

	@Override
	public void actualizar(int numero, TipoVehiculo tvNuevosDatos) {
		TipoVehiculo tvExistente = buscarPorClave(numero);

        if (tvExistente != null) {
        	tvExistente.setNombre(tvExistente.getNombre());
        	
            System.out.println("Tipo de vehiculo actualizado con exito, numero: " + numero + " "
            		+ tvNuevosDatos.getNombre() + " "
            );
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el tipo de vehiculo.");
        }
	}

	@Override
	public void eliminar(int numero) {
		TipoVehiculo tvAEliminar = buscarPorClave(numero);

        if (tvAEliminar != null) {
            this.baseDeDatosMemoria.remove(tvAEliminar );
            System.out.println("Tipo de vehiculo eliminado con éxito.");
        } else {
            System.out.println("No se encontró el tipo de vehiculo a eliminar.");
        }
	}
}
