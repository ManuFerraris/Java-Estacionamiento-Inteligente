package estacionamiento.repository;

import estacionamiento.domain.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository {
    void guardar(Vehiculo vehiculo);
    Vehiculo buscarPorPatente(String patente);
    List<Vehiculo> buscarPorUsuario(Integer numeroUsuario);
    List<Vehiculo> obtenerTodos();
    void actualizar(String patente, Vehiculo vehiculo);
    void eliminar(String patente);
}