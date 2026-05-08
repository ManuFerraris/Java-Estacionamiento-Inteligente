package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTV;

public interface PrecioHistoricoTVRepository {
    void guardar(PrecioHistoricoTV precioHistorico);
    PrecioHistoricoTV buscarPorClave(int codigoTV, LocalDateTime fechaDesde);
    List<PrecioHistoricoTV> obtenerTodos();
    void actualizar(int codigoTV, LocalDateTime fechaDesde, PrecioHistoricoTV precioHistorico);
    void eliminar(int codigoTV, LocalDateTime fechaDesde);
}