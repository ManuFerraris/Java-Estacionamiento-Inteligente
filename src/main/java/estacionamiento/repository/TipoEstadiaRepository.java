package estacionamiento.repository;
import java.util.List;

import estacionamiento.domain.TipoEstadia;

public interface TipoEstadiaRepository {
	void guardar(TipoEstadia tipoEstadia);
    TipoEstadia buscarPorNumero(int numero);
    List<TipoEstadia> obtenerTodos();
    void actualizar(int numero, TipoEstadia tipoEstadia);
    void eliminar(int numero);
}