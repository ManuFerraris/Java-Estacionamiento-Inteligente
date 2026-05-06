package estacionamiento.repository;

import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.Vehiculo;

public class VehiculoRepository {
	// 1. Nuestra "BD" en memoria.
	// Usamos la interfaz 'List' para la declaración y la clase 'ArrayList' para la instanciación.
    // Esto es una buena práctica de programación orientada a interfaces.
	private List<Vehiculo> baseDeDatosMemoria;
	
	public VehiculoRepository() {
		this.baseDeDatosMemoria = new ArrayList<>();
	}
    
    // R - READ (findAll)
    // ===================
    // Obtener todos los registros
    public List<Vehiculo> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    /// R - READ (findOne)
    // =======================
    // Obtener un registro por su ID (patente)
    public Vehiculo buscarPorPatente(String patente) {
        for (Vehiculo v : this.baseDeDatosMemoria) {
            if (v.getPatente().equalsIgnoreCase(patente)) {
                return v; // Lo encontramos, lo devolvemos
            }
        }
        return null; // Si termina el ciclo y no lo encontró, devuelve null (vacío)
    }
    
    // C - CREATE
    // ==============
    public void guardar(Vehiculo vehiculo) {
        // Validamos que no exista un vehículo con esa patente antes de guardarlo
        if (buscarPorPatente(vehiculo.getPatente()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con la patente: " + vehiculo.getPatente());
        }
        this.baseDeDatosMemoria.add(vehiculo);
        System.out.println("Vehículo guardado con éxito: " + vehiculo.getPatente());
    }
    
    // U - UPDATE
    // ==============
    public void actualizar(String patenteBuscada, Vehiculo vehiculoNuevosDatos) {
        Vehiculo vehiculoExistente = buscarPorPatente(patenteBuscada);
        
        if (vehiculoExistente != null) {
            // Actualizamos los datos usando los Setters
            vehiculoExistente.setDescripcion(vehiculoNuevosDatos.getDescripcion());
            vehiculoExistente.setTipoVehiculo(vehiculoNuevosDatos.getTipoVehiculo());
            System.out.println("Vehículo actualizado: " + patenteBuscada);
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la patente: " + patenteBuscada);
        }
    }
    
    // D - DELETE
    // ==============
    public void eliminar(String patente) {
        Vehiculo vehiculoAEliminar = buscarPorPatente(patente);
        
        if (vehiculoAEliminar != null) {
            this.baseDeDatosMemoria.remove(vehiculoAEliminar);
            System.out.println("Vehículo eliminado: " + patente);
        } else {
            System.out.println("No se encontró el vehículo para eliminar.");
        }
    }
}
