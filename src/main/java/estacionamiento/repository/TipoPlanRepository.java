package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.TipoPlan;

public interface TipoPlanRepository {
	void guardar(TipoPlan tp);
	TipoPlan buscarPorClave(Integer codigo);
    List<TipoPlan> obtenerTodos();
    void actualizar(Integer codigo, TipoPlan tp);
    void eliminar(Integer codigo);
}
