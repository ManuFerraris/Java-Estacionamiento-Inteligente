package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;
import estacionamiento.domain.Lugar;

public interface LugarRepository {
    Lugar guardar(Lugar lugar);
    Lugar buscarPorClave(int codigo); 
    List<Lugar> obtenerTodos();
    void actualizar(int codigo, Lugar lugar);
    void eliminar(int codigo);
    Lugar obtenerPrimerLugarLibre(int idCochera, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
