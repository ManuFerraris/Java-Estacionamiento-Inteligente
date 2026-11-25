package estacionamiento.repository.memoria;

import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.repository.LugarTipoEstadiaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LugarTipoEstadiaRepositoryMemoria implements LugarTipoEstadiaRepository {

    private List<LugarTipoEstadia> baseDeDatosMemoria = new ArrayList<>();

    @Override
    public LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia) {

        int codigoLugar = lugarTipoEstadia.getLugar().getCodigo();
        int numeroTipoEstadia = lugarTipoEstadia.getTipoEstadia().getNumero();
        LocalDateTime fechaDesde = lugarTipoEstadia.getFechaDesde();

        if (findById(codigoLugar, numeroTipoEstadia, fechaDesde) != null) {
            throw new IllegalArgumentException(
                "Ya existe una asignación con esos datos."
            );
        }

        baseDeDatosMemoria.add(lugarTipoEstadia);

        System.out.println("Memoria: Registro de LugarTipoEstadia guardado con éxito.");

        return lugarTipoEstadia;
    }

    @Override
    public LugarTipoEstadia findById(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
    	for (LugarTipoEstadia l : this.baseDeDatosMemoria) {
            
			 if (l.getLugar().getCodigo() == codigoLugar && l.getTipoEstadia().getNumero() == numeroTipoEstadia && l.getFechaDesde().equals(fechaDesde)) {
				 return l;
			 }
		 }
		 return null;
    }

    @Override
    public List<LugarTipoEstadia> findAll() {
        return new ArrayList<>(baseDeDatosMemoria);
    }
    
    @Override
    public List<LugarTipoEstadia> findByLugar(int codigoLugar) {
        return baseDeDatosMemoria.stream()
            .filter(asignacion ->
                asignacion.getLugar().getCodigo() == codigoLugar
            )
            .sorted((a, b) ->
                b.getFechaDesde().compareTo(a.getFechaDesde())
            )
            .toList();
    }

    @Override
    public void delete(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        throw new UnsupportedOperationException(
            "No se permite eliminar físicamente una asignación. Utilice darDeBaja()."
        );
    }
    @Override
    public void darDeBaja(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        for (LugarTipoEstadia asignacion : baseDeDatosMemoria) {
            if (asignacion.getLugar().getCodigo() == codigoLugar
                    && asignacion.getTipoEstadia().getNumero() == numeroTipoEstadia
                    && asignacion.getFechaDesde().equals(fechaDesde)
                    && asignacion.getFechaHasta() == null) {

                asignacion.setFechaHasta(LocalDateTime.now());
                return;
            }
        }

        throw new RuntimeException("No se encontró una asignación vigente.");
    }

    @Override
    public boolean existeAsignacionVigente(int codigoLugar, int numeroTipoEstadia) {
        return baseDeDatosMemoria.stream().anyMatch(asignacion ->
            asignacion.getLugar().getCodigo() == codigoLugar
            && asignacion.getTipoEstadia().getNumero() == numeroTipoEstadia
            && asignacion.getFechaHasta() == null
        );
    }
}