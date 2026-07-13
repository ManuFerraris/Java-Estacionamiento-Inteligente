package estacionamiento.repository;

import estacionamiento.domain.LugarTipoEstadia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LugarTipoEstadiaRepositoryMemoria implements LugarTipoEstadiaRepository {

    private List<LugarTipoEstadia> baseDeDatos = new ArrayList<>();

    @Override
    public LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia) {
        
        String codigoLugar = lugarTipoEstadia.getLugar().getCodigo();
        int numeroTipoEstadia = lugarTipoEstadia.getTipoEstadia().getNumero();
        LocalDateTime fechaDesde = lugarTipoEstadia.getFechaDesde();

        
        baseDeDatos.removeIf(l -> l.getLugar().getCodigo().equals(codigoLugar) && 
                                  l.getTipoEstadia().getNumero() == numeroTipoEstadia &&
                                  l.getFechaDesde().equals(fechaDesde));
        
        baseDeDatos.add(lugarTipoEstadia);
        System.out.println("Memoria: Registro de LugarTipoEstadia guardado con éxito.");
        return lugarTipoEstadia;
    }

    @Override
    public Optional<LugarTipoEstadia> findById(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
        return baseDeDatos.stream()
                .filter(l -> l.getLugar().getCodigo().equals(codigoLugar) && 
                             l.getTipoEstadia().getNumero() == numeroTipoEstadia &&
                             l.getFechaDesde().equals(fechaDesde))
                .findFirst();
    }

    @Override
    public List<LugarTipoEstadia> findAll() {
        return new ArrayList<>(baseDeDatos);
    }

    @Override
    public void delete(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
        boolean eliminado = baseDeDatos.removeIf(l -> l.getLugar().getCodigo().equals(codigoLugar) && 
                                  l.getTipoEstadia().getNumero() == numeroTipoEstadia &&
                                  l.getFechaDesde().equals(fechaDesde));
                                  
        if (eliminado) {
            System.out.println("Memoria: Registro de LugarTipoEstadia eliminado con éxito.");
        } else {
            System.out.println("Memoria: No se encontró el registro para eliminar.");
        }
    }
}