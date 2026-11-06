
package estacionamiento.service;

import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.TipoPlanRepository;

import java.time.LocalDate;
import java.util.List;

public class TipoPlanService {

    private final TipoPlanRepository tipoPlanRepository;

    public TipoPlanService(TipoPlanRepository tipoPlanRepository) {
        this.tipoPlanRepository = tipoPlanRepository;
    }

    private boolean esNuloOBlanco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public void registrarTipoPlan(TipoPlan nuevoTipoPlan) {
        if (nuevoTipoPlan == null) {
            throw new IllegalArgumentException("No se puede registrar un tipo de plan nulo.");
        }

        if (esNuloOBlanco(nuevoTipoPlan.getNombre())) {
            throw new IllegalArgumentException("El nombre del tipo de plan es obligatorio.");
        }

        if (esNuloOBlanco(nuevoTipoPlan.getDetalle())) {
            throw new IllegalArgumentException("El detalle del tipo de plan es obligatorio.");
        }

        if (nuevoTipoPlan.getFechaBaja() != null) {
            throw new IllegalArgumentException("Un plan nuevo no puede registrarse con una fecha de baja.");
        }

        tipoPlanRepository.guardar(nuevoTipoPlan);
    }

    public List<TipoPlan> obtenerTodosLosTiposDePlan() {
        return tipoPlanRepository.obtenerTodos();
    }

    public TipoPlan buscarTipoPlanPorCodigo(Integer codigo) {
        if (codigo == null || codigo <= 0) {
            throw new IllegalArgumentException("El código del tipo de plan debe ser válido.");
        }

        TipoPlan tipoPlan = tipoPlanRepository.buscarPorClave(codigo);

        if (tipoPlan == null) {
            throw new IllegalArgumentException(
                "No existe un tipo de plan con el código " + codigo + ".");
        }

        return tipoPlan;
    }

    public void actualizarTipoPlan(Integer codigo, TipoPlan planAActualizar) {
        if (planAActualizar == null) {
            throw new IllegalArgumentException("El plan a actualizar no puede ser nulo.");
        }

        TipoPlan planExistente = buscarTipoPlanPorCodigo(codigo);

        if (esNuloOBlanco(planAActualizar.getNombre())) {
            throw new IllegalArgumentException("El nombre del plan es obligatorio.");
        }

        if (esNuloOBlanco(planAActualizar.getDetalle())) {
            throw new IllegalArgumentException("El detalle del plan es obligatorio.");
        }

        planAActualizar.setFechaBaja(planExistente.getFechaBaja());

        tipoPlanRepository.actualizar(codigo, planAActualizar);
    }

    public void eliminarTipoPlan(Integer codigo) {
        darDeBaja(codigo);
    }

    public void darDeBaja(Integer codigo) {
        TipoPlan tp = buscarTipoPlanPorCodigo(codigo);

        if (tp.getFechaBaja() == null) {
            tp.setFechaBaja(LocalDate.now());
            tipoPlanRepository.actualizar(codigo, tp);
        }
    }

    public void darDeAltaPostBaja(Integer codigo) {
        TipoPlan tp = buscarTipoPlanPorCodigo(codigo);

        if (tp.getFechaBaja() != null) {
            tp.setFechaBaja(null);
            tipoPlanRepository.actualizar(codigo, tp);
        }
    }
}
