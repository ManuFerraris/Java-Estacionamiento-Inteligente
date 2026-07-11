package estacionamiento.repository;
import estacionamiento.domain.Suscripcion;
import java.time.LocalDateTime;
import java.util.List;

// Nati cree la interfez (de manera parcial) para poder hacer una prueba del pago de suscripcion
// pero le faltan los otros metodos.

public interface SuscripcionRepository {
	Suscripcion buscarPorClave(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde);
    void guardar(Suscripcion suscripcion);
    List<Suscripcion> obtenerTodos();
    void actualizar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde, Suscripcion suscripcion);
    void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde);
}
