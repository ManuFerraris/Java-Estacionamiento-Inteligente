package estacionamiento.service;

import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.TipoPlanRepository;

public class TipoPlanService {

    private final TipoPlanRepository tipoPlanRepository;

    public TipoPlanService(TipoPlanRepository tipoPlanRepository) {
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarTipoPlan(TipoPlan nuevoTipoPlan) {
        if (nuevoTipoPlan == null) {
            throw new IllegalArgumentException("No se puede registrar un tipo de plan nulo.");
        }

        // Validación del código identificador
        if (nuevoTipoPlan.getCodigo() <= 0) {
            throw new IllegalArgumentException("El código del tipo de plan debe ser un número válido mayor a cero.");
        }

        // Validación del nombre (obligatorio según nullable = false)
        if (nuevoTipoPlan.getNombre() == null || nuevoTipoPlan.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de plan es obligatorio.");
        }

        // Validación del detalle o descripción del plan (obligatorio según nullable = false)
        if (nuevoTipoPlan.getDetalle() == null || nuevoTipoPlan.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle del tipo de plan es obligatorio.");
        }

        // Si pasa todas las validaciones de consistencia, delegamos al repositorio
        tipoPlanRepository.guardar(nuevoTipoPlan);

        System.out.println("Servicio: Tipo de plan validado y procesado correctamente.");
    }
}