package estacionamiento.service;

import java.util.List;

import estacionamiento.domain.Vehiculo;
import estacionamiento.repository.VehiculoRepository;

public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public void registrarVehiculo(Vehiculo nuevoVehiculo) {
        if (nuevoVehiculo == null) {
            throw new IllegalArgumentException("No se puede registrar un vehículo nulo.");
        }

        if (nuevoVehiculo.getPatente() == null || nuevoVehiculo.getPatente().trim().isEmpty()) {
            throw new IllegalArgumentException("La patente del vehículo es obligatoria.");
        }

        if (nuevoVehiculo.getTipoVehiculo() == null) {
            throw new IllegalArgumentException("El vehículo debe tener un tipo asignado (moto, auto, utilitario, etc.).");
        }

        vehiculoRepository.guardar(nuevoVehiculo);
        System.out.println("Servicio: Vehículo validado y procesado correctamente.");
        
    }

    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoRepository.obtenerTodos();
    }

    public Vehiculo buscarVehiculoPorPatente(String patente) throws Exception {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente a buscar no puede estar vacía.");
        }

        Vehiculo vehiculo = vehiculoRepository.buscarPorPatente(patente);
        
        if (vehiculo != null) {
            return vehiculo;
        } else {
            throw new Exception("No se encontró ningún vehículo con la patente: " + patente);
        }
    }

    public void actualizarVehiculo(String patente, Vehiculo vehiculoNuevosDatos) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente no puede estar vacía para actualizar.");
        }
        if (vehiculoNuevosDatos == null) {
            throw new IllegalArgumentException("Los nuevos datos del vehículo son obligatorios.");
        }

        vehiculoRepository.actualizar(patente, vehiculoNuevosDatos);
    }

    public void eliminarVehiculo(String patente) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente no puede estar vacía para eliminar.");
        }
        
        vehiculoRepository.eliminar(patente);
    }
}
