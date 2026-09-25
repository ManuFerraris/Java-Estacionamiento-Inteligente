package estacionamiento.service;

import java.util.List;

import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.TipoVehiculoRepository;

public class TipoVehiculoService {

    private final TipoVehiculoRepository tipoVehiculoRepository;

    public TipoVehiculoService(TipoVehiculoRepository tipoVehiculoRepository) {
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    public void registrarTipoVehiculo(TipoVehiculo nuevoTipoVehiculo) {

        if (nuevoTipoVehiculo == null) {
            throw new IllegalArgumentException(
                    "No se puede registrar un tipo de vehículo nulo."
            );
        }

        if (nuevoTipoVehiculo.getNombre() == null ||
            nuevoTipoVehiculo.getNombre().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre del tipo de vehículo es obligatorio."
            );
        }

        tipoVehiculoRepository.guardar(nuevoTipoVehiculo);

        System.out.println(
                "Servicio: Tipo de vehículo validado y procesado correctamente."
        );
    }

    public List<TipoVehiculo> obtenerTodosLosTiposDeVehiculo() {
        return tipoVehiculoRepository.obtenerTodos();
    }

    public TipoVehiculo buscarTipoVehiculoPorNumero(int numero) {

        if (numero <= 0) {
            throw new IllegalArgumentException(
                    "El número del tipo de vehículo debe ser válido."
            );
        }

        TipoVehiculo tipoVehiculo =
                tipoVehiculoRepository.buscarPorClave(numero);

        if (tipoVehiculo == null) {
            throw new IllegalArgumentException(
                    "No existe un tipo de vehículo con el número "
                    + numero + "."
            );
        }

        return tipoVehiculo;
    }

    public void actualizarTipoVehiculo(
            int numero,
            TipoVehiculo tipoVehiculoAActualizar) {

        if (tipoVehiculoAActualizar == null) {
            throw new IllegalArgumentException(
                    "El tipo de vehículo a actualizar no puede ser nulo."
            );
        }

        buscarTipoVehiculoPorNumero(numero);

        if (tipoVehiculoAActualizar.getNombre() == null ||
            tipoVehiculoAActualizar.getNombre().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre del tipo de vehículo es obligatorio."
            );
        }

        tipoVehiculoRepository.actualizar(
                numero,
                tipoVehiculoAActualizar
        );

        System.out.println(
                "Servicio: Tipo de vehículo actualizado correctamente."
        );
    }

    public void eliminarTipoVehiculo(int numero) {

        buscarTipoVehiculoPorNumero(numero);

        tipoVehiculoRepository.eliminar(numero);

        System.out.println(
                "Servicio: Tipo de vehículo eliminado correctamente."
        );
    }
}