package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Lugar;

public interface LugarRepository {
	void guardar(Lugar lugar);
    Lugar buscarPorClave(int codigo);
    List<Lugar> obtenerTodos();
    void actualizar(int codigo, Lugar lugar);
    void eliminar(int codigo);
}