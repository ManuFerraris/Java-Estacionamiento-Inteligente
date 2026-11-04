package estacionamiento.repository;

import java.util.List;
import estacionamiento.domain.AsignacionCochera;
import estacionamiento.domain.Cochera;

public interface AsignacionCocheraRepository {
	void guardar(AsignacionCochera asignacion);
    void actualizar(AsignacionCochera asignacion);
    List<Cochera> obtenerCocherasActivasPorEncargado(Integer numeroUsuario);
    List<AsignacionCochera> obtenerTodasLasActivas();
    AsignacionCochera buscarActivaPorUsuarioYCochera(Integer numeroUsuario, Integer codigoCochera);
}
