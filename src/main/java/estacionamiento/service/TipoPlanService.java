package estacionamiento.service;


import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.TipoPlanRepository;
import java.util.List;

public class TipoPlanService {

    private final TipoPlanRepository tipoPlanRepository;

    public TipoPlanService(TipoPlanRepository tipoPlanRepository) {
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarTipoPlan(TipoPlan nuevoTipoPlan) {
        if (nuevoTipoPlan == null) {
            throw new IllegalArgumentException("No se puede registrar un tipo de plan nulo.");
        }
        if (nuevoTipoPlan.getCodigo() <= 0) {
            throw new IllegalArgumentException("El código del tipo de plan debe ser mayor a cero.");
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
    public List<TipoPlan> obtenerTodosLosTiposDePlan() {
        return tipoPlanRepository.obtenerTodos();
    }
    public TipoPlan buscarTipoPlanPorCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del tipo de plan debe ser mayor a cero.");
        }

        TipoPlan tipoPlan = tipoPlanRepository.buscarPorClave(codigo);

        if (tipoPlan == null) {
            throw new IllegalArgumentException("No existe un tipo de plan con el código " + codigo + ".");
        }

        return tipoPlan;
    }
    public void actualizarTipoPlan(int codigo, TipoPlan nuevosDatos) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del tipo de plan debe ser mayor a cero.");
        }

        if (nuevosDatos == null) {
            throw new IllegalArgumentException("Los nuevos datos del tipo de plan son obligatorios.");
        }

        if (nuevosDatos.getNombre() == null || nuevosDatos.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de plan es obligatorio.");
        }

        if (nuevosDatos.getDetalle() == null || nuevosDatos.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle del tipo de plan es obligatorio.");
        }

        buscarTipoPlanPorCodigo(codigo);
        tipoPlanRepository.actualizar(codigo, nuevosDatos);
    }
    public void eliminarTipoPlan(int codigo) {
        buscarTipoPlanPorCodigo(codigo);
        tipoPlanRepository.eliminar(codigo);
    }
}