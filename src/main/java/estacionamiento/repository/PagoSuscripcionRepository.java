package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.PagoSuscripcion;

public interface PagoSuscripcionRepository {
    void guardar(PagoSuscripcion pago);
    void actualizar(PagoSuscripcion pago);
    PagoSuscripcion buscarPorClave(Integer id);
    List<PagoSuscripcion> obtenerTodos();
}
