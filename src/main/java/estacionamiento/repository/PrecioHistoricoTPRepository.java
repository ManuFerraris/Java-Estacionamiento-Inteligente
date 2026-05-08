package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTP;

public interface PrecioHistoricoTPRepository {
    void guardar(PrecioHistoricoTP precioHistorico);
    PrecioHistoricoTP buscarPorClave(int codigoTP, LocalDateTime fechaDesde);
    List<PrecioHistoricoTP> obtenerTodos();
    void actualizar(int codigoTP, LocalDateTime fechaDesde, PrecioHistoricoTP precioHistorico);
    void eliminar(int codigoTP, LocalDateTime fechaDesde);
}