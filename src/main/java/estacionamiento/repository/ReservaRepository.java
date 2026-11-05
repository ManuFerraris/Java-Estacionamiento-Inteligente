package estacionamiento.repository;
import java.util.List;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;

import java.time.LocalDateTime;

public interface ReservaRepository {
	void guardar(Reserva reserva);
    List<Reserva> obtenerTodos();
	void eliminar(ReservaId claveCompuesta);
	void actualizar(Reserva reservaNuevosDatos);
	Reserva buscarPorClave(ReservaId claveCompuesta);
	public List<Reserva> buscarReservasPendientesSinPagoCompleto(LocalDateTime limiteTiempo);
	List<Reserva> buscarPorUsuario(Integer numeroUsuario);
	List<Reserva> obtenerActivasPorCochera(int codigoCochera);
    List<Reserva> obtenerTodasLasActivas();
}