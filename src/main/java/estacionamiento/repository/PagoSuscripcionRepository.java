package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.claves.PagoSuscripcionId;

public interface PagoSuscripcionRepository {
    void guardar(PagoSuscripcion pago);
    void actualizar(PagoSuscripcion pago);
    PagoSuscripcion buscarPorClave(PagoSuscripcionId id);
    List<PagoSuscripcion> obtenerTodos();
}
