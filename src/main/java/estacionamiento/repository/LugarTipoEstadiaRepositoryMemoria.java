package estacionamiento.repository;

import estacionamiento.domain.Cochera;
import estacionamiento.domain.LugarTipoEstadia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LugarTipoEstadiaRepositoryMemoria implements LugarTipoEstadiaRepository {

    private List<LugarTipoEstadia> baseDeDatosMemoria = new ArrayList<>();

    @Override
    public LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia) {
        
        int codigoLugar = lugarTipoEstadia.getLugar().getCodigo();
        int numeroTipoEstadia = lugarTipoEstadia.getTipoEstadia().getNumero();
        LocalDateTime fechaDesde = lugarTipoEstadia.getFechaDesde();

        
        baseDeDatosMemoria.removeIf(l -> l.getLugar().getCodigo() == codigoLugar && 
                                  l.getTipoEstadia().getNumero() == numeroTipoEstadia &&
                                  l.getFechaDesde().equals(fechaDesde));
        
        baseDeDatosMemoria.add(lugarTipoEstadia);
        System.out.println("Memoria: Registro de LugarTipoEstadia guardado con éxito.");
        return lugarTipoEstadia;
    }

    @Override
    public LugarTipoEstadia findById(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
    	for (LugarTipoEstadia l : this.baseDeDatosMemoria) {
            
			 if (l.getLugar().getCodigo() == codigoLugar && l.getTipoEstadia().getNumero() == numeroTipoEstadia && l.getFechaDesde() == fechaDesde) {
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
    public void delete(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
        boolean eliminado = baseDeDatosMemoria.removeIf(l -> l.getLugar().getCodigo() == codigoLugar && 
                                  l.getTipoEstadia().getNumero() == numeroTipoEstadia &&
                                  l.getFechaDesde().equals(fechaDesde));
                                  
        if (eliminado) {
            System.out.println("Memoria: Registro de LugarTipoEstadia eliminado con éxito.");
        } else {
            System.out.println("Memoria: No se encontró el registro para eliminar.");
        }
    }
}