package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Cochera;

public interface CocheraRepository {
    void guardar(Cochera cochera);
    Cochera buscarPorClave(Integer codigo);
    List<Cochera> obtenerTodos();
    void actualizar(Integer codigo, Cochera cochera);
    void eliminar(Integer codigo);
}