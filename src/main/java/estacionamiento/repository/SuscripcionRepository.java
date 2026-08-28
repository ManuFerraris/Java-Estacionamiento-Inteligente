package estacionamiento.repository;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.claves.SuscripcionId;

import java.time.LocalDateTime;
import java.util.List;

public interface SuscripcionRepository {
	Suscripcion buscarPorClave(SuscripcionId id);
    void guardar(Suscripcion suscripcion);
    List<Suscripcion> obtenerTodas();
    void actualizar(Suscripcion suscripcion);
    void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde);
    Suscripcion buscarActivaPorUsuario(int numeroUsuario);
}
