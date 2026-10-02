package estacionamiento.service;

import java.util.List;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.TipoEstadiaRepository;

public class TipoEstadiaService {

    private final TipoEstadiaRepository tipoEstadiaRepository;

    public TipoEstadiaService(TipoEstadiaRepository tipoEstadiaRepository) {
        this.tipoEstadiaRepository = tipoEstadiaRepository;
    }

    // 1. Método de Alta con tus validaciones intactas
    public void registrarTipoEstadia(TipoEstadia nuevoTipoEstadia) {
        if (nuevoTipoEstadia == null) {
            throw new IllegalArgumentException("No se puede registrar un tipo de estadía nulo.");
        }

        if (nuevoTipoEstadia.getDescripcion() == null || nuevoTipoEstadia.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del tipo de estadía es obligatoria.");
        }

        if (nuevoTipoEstadia.getCupo() <= 0) {
            throw new IllegalArgumentException("El cupo del tipo de estadía debe ser mayor a cero.");
        }

        // Si pasa todas las validaciones, delegamos la persistencia al repositorio
        tipoEstadiaRepository.guardar(nuevoTipoEstadia);

        System.out.println("Servicio: Tipo de estadía validado y procesado correctamente.");
    }
    
    // 2. Método de Lectura (Necesario para los <select> del Servlet)
    public List<TipoEstadia> obtenerTodos() {
        return tipoEstadiaRepository.obtenerTodos();
    }    
}