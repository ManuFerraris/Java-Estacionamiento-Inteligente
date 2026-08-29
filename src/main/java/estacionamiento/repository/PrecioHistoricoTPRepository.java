package estacionamiento.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.claves.PrecioHistoricoTPId;

public interface PrecioHistoricoTPRepository {
	void guardar(PrecioHistoricoTP precio);
    PrecioHistoricoTP buscarPorClave(PrecioHistoricoTPId id);
    List<PrecioHistoricoTP> obtenerTodos();
    void actualizar(int codigoPlan, LocalDateTime fechaDesde, BigDecimal nuevoPrecio);
    void eliminar(int codigoPlan, LocalDateTime fechaDesde);
    PrecioHistoricoTP obtenerPrecioVigente(int codigoPlan);
}