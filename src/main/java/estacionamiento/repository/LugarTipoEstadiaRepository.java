package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import estacionamiento.domain.LugarTipoEstadia;

public interface LugarTipoEstadiaRepository {

	LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia);
	Optional<LugarTipoEstadia> findById(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde);
    List<LugarTipoEstadia> findAll();
    void delete(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde);
    
}
