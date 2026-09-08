package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Pago;

public interface PagoRepository {
	void guardar(Pago pago);
    Pago buscarPorClave(Integer numero);
    List<Pago> obtenerTodos();
    void actualizar(Integer numero, Pago pago);
    void eliminar(Integer numero);

}
