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

        if (nuevoTipoPlan.getDetalle() == null || nuevoTipoPlan.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle del tipo de plan es obligatorio.");
        }

        if (nuevoTipoPlan.getFechaBaja() != null) {
            throw new IllegalArgumentException("Un plan nuevo no puede registrarse con una fecha de baja.");
        }

        tipoPlanRepository.guardar(nuevoTipoPlan);
        System.out.println("Servicio: Tipo de Plan validado y procesado correctamente.");
    }
    
    public List<TipoPlan> obtenerTodosLosTiposDePlan() {
        return tipoPlanRepository.obtenerTodos();
    }
    
    public TipoPlan buscarTipoPlanPorCodigo(Integer codigo) {
        if (codigo == null || codigo <= 0) {
            throw new IllegalArgumentException("El código del tipo de plan debe ser valido.");
        }

        TipoPlan tipoPlan = tipoPlanRepository.buscarPorClave(codigo);
        if (tipoPlan == null) {
            throw new IllegalArgumentException("No existe un tipo de plan con el código " + codigo + ".");
        }

        return tipoPlan;
    }
    
    public void actualizarTipoPlan(Integer codigo, TipoPlan planAActualizar) {

    	if(planAActualizar == null) {
            throw new IllegalArgumentException("El plan a actualizar no puede ser nulo.");
        }

        TipoPlan planExistente = buscarTipoPlanPorCodigo(codigo);
        
        planAActualizar.setFechaBaja(planExistente.getFechaBaja());
        
        if (esNuloOBlanco(planAActualizar.getNombre())) {
            throw new IllegalArgumentException("El nombre del plan es obligatorio.");
        }

        planAActualizar.setFechaBaja(planExistente.getFechaBaja()); // para mantener la fecha actual y no cambiarla a null.
        
        tipoPlanRepository.actualizar(codigo, planAActualizar);
        System.out.println("Servicio: Plan " + planAActualizar.getCodigo() + " validado y actualizado con éxito.");
    }

    public void eliminarTipoPlan(Integer codigo) {
        buscarTipoPlanPorCodigo(codigo);
        tipoPlanRepository.eliminar(codigo);
    }

	public void darDeBaja(Integer codigo) throws Exception {
		TipoPlan tp = buscarTipoPlanPorCodigo(codigo);
		if(tp != null) {
			tp.setFechaBaja(LocalDate.now());
			tipoPlanRepository.actualizar(codigo, tp);
		}else {
			throw new Exception("El tipo de plan a dar de baja no existe.");
		}
	}
	
	public void darDeAltaPostBaja(Integer codigo) throws Exception {
		TipoPlan tp = buscarTipoPlanPorCodigo(codigo);
		if(tp != null) {
			tp.setFechaBaja(null);
			tipoPlanRepository.actualizar(codigo, tp);
		}else {
			throw new Exception("El tipo de plan a volver a dar de alta no existe.");
		}
	}
}