package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.TipoPlan;

public interface TipoPlanRepository {
	void guardar(TipoPlan tp);
	TipoPlan buscarPorClave(int codigo);
    List<TipoPlan> obtenerTodos();
    void actualizar(int codigo, TipoPlan tp);
    void eliminar(int codigo);
}
