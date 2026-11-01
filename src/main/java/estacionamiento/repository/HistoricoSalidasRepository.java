package estacionamiento.repository;

import estacionamiento.domain.HistoricoSalidas;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoricoSalidasRepository {
    void guardar(HistoricoSalidas historicoSalidas);
    HistoricoSalidas buscarPorClave(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial);
    List<HistoricoSalidas> obtenerTodos();
    void actualizar(HistoricoSalidas historicoSalidasNuevo);
    void eliminar(HistoricoSalidas historicoAEliminar);
    
}