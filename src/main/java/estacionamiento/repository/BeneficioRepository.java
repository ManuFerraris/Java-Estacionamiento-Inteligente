package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.Beneficio;

public interface BeneficioRepository {
	void guardar(Beneficio beneficio);
	Beneficio buscarPorClave(int codTP, int numero);
    List<Beneficio> obtenerTodos();
    void actualizar(int codTP, int numero, Beneficio beneficio);
    void eliminar(int codTP, int numero);
}
