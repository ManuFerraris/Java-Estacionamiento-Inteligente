package estacionamiento.repository;

import estacionamiento.domain.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculoRepositoryMemoria implements VehiculoRepository {
    
    private List<Vehiculo> baseDeDatosMemoria;
    
    public VehiculoRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }
    
    @Override
    public List<Vehiculo> obtenerTodos() {
        // Retornamos una copia para proteger los datos en memoria
        return new ArrayList<>(this.baseDeDatosMemoria);
    }

    @Override
    public Optional<Vehiculo> buscarPorPatente(String patente) {
        return this.baseDeDatosMemoria.stream()
                .filter(v -> v.getPatente().equalsIgnoreCase(patente))
                .findFirst();
    }
    
    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        if (buscarPorPatente(vehiculo.getPatente()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vehículo con la patente: " + vehiculo.getPatente());
        }
        this.baseDeDatosMemoria.add(vehiculo);
        System.out.println("Vehículo guardado con éxito: " + vehiculo.getPatente());
        
        return vehiculo;
    }
    
    @Override
    public void actualizar(String patenteBuscada, Vehiculo vehiculoNuevosDatos) {
        Optional<Vehiculo> vehiculoExistenteOpt = buscarPorPatente(patenteBuscada);
        
        if (vehiculoExistenteOpt.isPresent()) {
            Vehiculo vehiculoExistente = vehiculoExistenteOpt.get();
            vehiculoExistente.setDescripcion(vehiculoNuevosDatos.getDescripcion());
            vehiculoExistente.setTipoVehiculo(vehiculoNuevosDatos.getTipoVehiculo());
            System.out.println("Vehículo actualizado: " + patenteBuscada);
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la patente: " + patenteBuscada);
        }
    }
    
    @Override
    public void eliminar(String patente) {
        Optional<Vehiculo> vehiculoAEliminarOpt = buscarPorPatente(patente);
        
        if (vehiculoAEliminarOpt.isPresent()) {
            this.baseDeDatosMemoria.remove(vehiculoAEliminarOpt.get());
            System.out.println("Vehículo eliminado: " + patente);
        } else {
            System.out.println("No se encontró el vehículo para eliminar.");
        }
    }
}