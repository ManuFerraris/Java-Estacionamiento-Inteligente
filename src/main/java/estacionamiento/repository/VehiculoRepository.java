package estacionamiento.repository;
import estacionamiento.domain.Vehiculo;
import java.util.List;

public interface VehiculoRepository {
	// Solo declaramos los métodos. Quien implemente esta interfaz está obligado a programarlos.
    void guardar(Vehiculo vehiculo);
    Vehiculo buscarPorPatente(String patente);
    List<Vehiculo> obtenerTodos();
    void actualizar(String patente, Vehiculo vehiculo);
    void eliminar(String patente);
}