package estacionamiento.repository;

import java.util.List;
import java.util.Optional;
import estacionamiento.domain.Lugar;

public interface LugarRepository {

    Lugar guardar(Lugar lugar);

    Optional<Lugar> buscarPorClave(String codigo);
    
    List<Lugar> obtenerTodos();
    
    void actualizar(String codigo, Lugar lugar);
    
    void eliminar(String codigo);
}