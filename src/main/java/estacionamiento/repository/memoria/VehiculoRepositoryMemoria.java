package estacionamiento.repository.memoria;

import estacionamiento.domain.Vehiculo;
import estacionamiento.repository.VehiculoRepository;

import java.util.ArrayList;
import java.util.List;

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
    public Vehiculo buscarPorPatente(String patente) {
        return this.baseDeDatosMemoria.stream()
                .filter(v -> v.getPatente().equalsIgnoreCase(patente))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public void guardar(Vehiculo vehiculo) {
        if (buscarPorPatente(vehiculo.getPatente()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con la patente: " + vehiculo.getPatente());
        }
        this.baseDeDatosMemoria.add(vehiculo);
        System.out.println("Vehículo guardado con éxito: " + vehiculo.getPatente());
    }
    
    @Override
    public void actualizar(String patenteBuscada, Vehiculo vehiculoNuevosDatos) {
        Vehiculo vehiculoExistente = buscarPorPatente(patenteBuscada);
        
        if (vehiculoExistente != null ) {
            vehiculoExistente.setDescripcion(vehiculoNuevosDatos.getDescripcion());
            vehiculoExistente.setTipoVehiculo(vehiculoNuevosDatos.getTipoVehiculo());
            System.out.println("Vehículo actualizado: " + patenteBuscada);
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la patente: " + patenteBuscada);
        }
    }
    
    @Override
    public void eliminar(String patente) {
        Vehiculo vehiculoAEliminarOpt = buscarPorPatente(patente);
        
        if (vehiculoAEliminarOpt != null) {
            this.baseDeDatosMemoria.remove(vehiculoAEliminarOpt);
            System.out.println("Vehículo eliminado: " + patente);
        } else {
            System.out.println("No se encontró el vehículo para eliminar.");
        }
    }

	@Override
	public List<Vehiculo> buscarPorUsuario(Integer numeroUsuario) {
		// TODO Auto-generated method stub
		return null;
	}
}