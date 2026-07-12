package estacionamiento.service;

import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.TipoVehiculoRepository;

public class TipoVehiculoService {

    private final TipoVehiculoRepository tipoVehiculoRepository;

    public TipoVehiculoService(TipoVehiculoRepository tipoVehiculoRepository) {
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    public void registrarTipoVehiculo(TipoVehiculo nuevoTipoVehiculo) {
        if (nuevoTipoVehiculo == null) {
            throw new IllegalArgumentException("No se puede registrar un tipo de vehículo nulo.");
        }

        // Validación del nombre (obligatorio)
        if (nuevoTipoVehiculo.getNombre() == null || nuevoTipoVehiculo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de vehículo es obligatorio.");
        }

        // Si pasa todas las validaciones, delegamos la persistencia al repositorio
        tipoVehiculoRepository.guardar(nuevoTipoVehiculo);

        System.out.println("Servicio: Tipo de vehículo validado y procesado correctamente.");
    }
}