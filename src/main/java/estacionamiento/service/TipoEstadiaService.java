package estacionamiento.service;

import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.TipoEstadiaRepository;

public class TipoEstadiaService {

    private final TipoEstadiaRepository tipoEstadiaRepository;

    public TipoEstadiaService(TipoEstadiaRepository tipoEstadiaRepository) {
        this.tipoEstadiaRepository = tipoEstadiaRepository;
    }

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

        if (nuevoTipoEstadia.getNumero() <= 0) {
            throw new IllegalArgumentException("El número de tipo de estadía debe ser válido y mayor a cero.");
        }

        // Si pasa todas las validaciones, delegamos la persistencia al repositorio
        tipoEstadiaRepository.guardar(nuevoTipoEstadia);

        System.out.println("Servicio: Tipo de estadía validado y procesado correctamente.");
    }
}