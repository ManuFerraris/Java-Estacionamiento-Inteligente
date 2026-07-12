package estacionamiento.service;

import estacionamiento.domain.Suscripcion;
import estacionamiento.repository.SuscripcionRepository;

public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public void registrarSuscripcion(Suscripcion nuevaSuscripcion) {
        if (nuevaSuscripcion == null) {
            throw new IllegalArgumentException("No se puede registrar una suscripción nula.");
        }

        // 1. Validación de las entidades fuertes de las que depende
        if (nuevaSuscripcion.getUsuario() == null) {
            throw new IllegalArgumentException("La suscripción debe estar asociada a un usuario válido.");
        }

        if (nuevaSuscripcion.getTipoPlan() == null) {
            throw new IllegalArgumentException("La suscripción debe estar asociada a un tipo de plan válido.");
        }

        // 2. Validación de la fecha de inicio (que ahora está dentro del EmbeddedId)
        if (nuevaSuscripcion.getFechaDesde() == null) {
            throw new IllegalArgumentException("La fecha de inicio (fechaDesde) es obligatoria.");
        }

        // 3. Validación lógica de coherencia de fechas
        // Si tiene fecha de fin programada, jamás puede ser anterior a la fecha de inicio
        if (nuevaSuscripcion.getFechaHasta() != null && 
            nuevaSuscripcion.getFechaHasta().isBefore(nuevaSuscripcion.getFechaDesde())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
        }

        // 4. Validación del estado
        if (nuevaSuscripcion.getEstado() == null) {
            throw new IllegalArgumentException("Se debe especificar el estado inicial de la suscripción (ACTIVA, PAUSADA o CANCELADA).");
        }

        // Si pasa todas las validaciones, delegamos la persistencia al repositorio
        suscripcionRepository.guardar(nuevaSuscripcion);

        System.out.println("Servicio: Suscripción validada y procesada correctamente.");
    }
}