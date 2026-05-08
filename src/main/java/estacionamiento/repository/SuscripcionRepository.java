package estacionamiento.repository;
import estacionamiento.domain.Suscripcion;
import java.time.LocalDateTime;

// Nati cree la interfez (de manera parcial) para poder hacer una prueba del pago de suscripcion
// pero le faltan los otros metodos.

public interface SuscripcionRepository {
	Suscripcion buscarPorClave(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde);
}
