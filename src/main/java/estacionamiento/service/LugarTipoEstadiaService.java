package estacionamiento.service;

import estacionamiento.domain.Lugar;
import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.LugarTipoEstadiaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LugarTipoEstadiaService {

    private LugarTipoEstadiaRepository repository;

    public LugarTipoEstadiaService(LugarTipoEstadiaRepository repository) {
        this.repository = repository;
    }

    public LugarTipoEstadia asignarTipoEstadiaALugar(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
        Lugar lugar = new Lugar();
        lugar.setCodigo(codigoLugar);
        
        TipoEstadia tipoEstadia = new TipoEstadia();
        tipoEstadia.setNumero(numeroTipoEstadia);

        LugarTipoEstadia nuevoRegistro = new LugarTipoEstadia(lugar, tipoEstadia, fechaDesde);
        
        return repository.save(nuevoRegistro);
    }

    public List<LugarTipoEstadia> obtenerTodos() {
        return repository.findAll();
    }

    public LugarTipoEstadia buscarPorId(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) throws Exception {
        Optional<LugarTipoEstadia> registro = repository.findById(codigoLugar, numeroTipoEstadia, fechaDesde);
        
        if (registro.isPresent()) {
            return registro.get();
        } else {
            throw new Exception("No se encontró el registro con Código: " + codigoLugar + 
                                ", Tipo: " + numeroTipoEstadia + " y Fecha: " + fechaDesde);
        }
    }

    public void eliminarRegistro(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        repository.delete(codigoLugar, numeroTipoEstadia, fechaDesde);
    }
}