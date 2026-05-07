package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Pago;

public interface PagoRepository {
	void guardar(Pago pago);
    Pago buscarPorClave(int numero);
    List<Pago> obtenerTodos();
    void actualizar(int numero, Pago pago);
    void eliminar(int numero);

}
