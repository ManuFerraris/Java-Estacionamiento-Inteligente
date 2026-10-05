package estacionamiento.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTV;

public interface PrecioHistoricoTVRepository {
    void guardar(PrecioHistoricoTV precioHistorico);
    PrecioHistoricoTV buscarPorClave(int codigoTV, LocalDateTime fechaDesde);
    List<PrecioHistoricoTV> obtenerTodos();
    void actualizar(PrecioHistoricoTV precioHistorico);
    void eliminar(PrecioHistoricoTV precioHistorico);
	BigDecimal obtenerPrecioVigente(int numero);
}