package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Cochera;

public interface CocheraRepository {
    void guardar(Cochera cochera);
    Cochera buscarPorClave(int codigo);
    List<Cochera> obtenerTodos();
    void actualizar(int codigo, Cochera cochera);
    void eliminar(int codigo);
}