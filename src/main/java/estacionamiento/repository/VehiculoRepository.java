package estacionamiento.repository;

import estacionamiento.domain.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository {
    Vehiculo guardar(Vehiculo vehiculo);
    Optional<Vehiculo> buscarPorPatente(String patente);
    List<Vehiculo> obtenerTodos();
    void actualizar(String patente, Vehiculo vehiculo);
    void eliminar(String patente);
}