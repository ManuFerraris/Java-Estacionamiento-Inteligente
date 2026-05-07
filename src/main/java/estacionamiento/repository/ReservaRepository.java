package estacionamiento.repository;
import java.util.List;
import estacionamiento.domain.Reserva;
import java.time.LocalDateTime;

public interface ReservaRepository {
	void guardar(Reserva reserva);
    Reserva buscarPorClave(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde);
    List<Reserva> obtenerTodos();
    void actualizar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde, Reserva reserva);
    void eliminar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde);
}