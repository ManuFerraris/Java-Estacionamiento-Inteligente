package estacionamiento.repository;

import java.util.List;
import java.time.LocalDateTime;
import estacionamiento.domain.PagoSuscripcion;

public interface PagoSuscripcionRepository {
	void guardar(PagoSuscripcion pagoSuscripcion);
	PagoSuscripcion buscarPorClave(int codigoTP, int numeroUsuario,LocalDateTime fechaDesde,LocalDateTime fechaHoraEmision);
    List<PagoSuscripcion> obtenerTodos();
    void actualizar(int codigoTP, int numeroUsuario,LocalDateTime fechaDesde,LocalDateTime fechaHoraEmision, PagoSuscripcion pagoSuscripcion);
    void eliminar(int codigoTP, int numeroUsuario,LocalDateTime fechaDesde,LocalDateTime fechaHoraEmision);
}
