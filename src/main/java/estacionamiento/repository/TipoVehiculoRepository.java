package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.TipoVehiculo;

public interface TipoVehiculoRepository {
	void guardar(TipoVehiculo tipoVehiculo);
	TipoVehiculo buscarPorClave(int numero);
    List<TipoVehiculo> obtenerTodos();
    void actualizar(int numero, TipoVehiculo tipoVehiculo);
    void eliminar(int numero);
}
