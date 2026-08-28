package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.Beneficio;
import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.BeneficioRepository;
import estacionamiento.repository.TipoPlanRepository;

public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final TipoPlanRepository tipoPlanRepository;

    public BeneficioService(BeneficioRepository beneficioRepository, TipoPlanRepository tipoPlanRepository) {
        this.beneficioRepository = beneficioRepository;
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarBeneficio(int codigoPlan, Beneficio nuevoBeneficio) {
        if (nuevoBeneficio == null) {
            throw new IllegalArgumentException("El beneficio no puede ser nulo.");
        }
        if (nuevoBeneficio.getDescripcion() == null || nuevoBeneficio.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del beneficio es obligatoria.");
        }

        TipoPlan planAsociado = tipoPlanRepository.buscarPorClave(codigoPlan);
        if (planAsociado == null) {
            throw new IllegalArgumentException("El tipo de plan asociado no existe en la base de datos.");
        }
        nuevoBeneficio.setTipoPlan(planAsociado);

        int proximoNumero = beneficioRepository.obtenerProximoNumeroPorPlan(codigoPlan);
        nuevoBeneficio.setNumero(proximoNumero);

        nuevoBeneficio.setFechaCreacion(LocalDateTime.now());
        nuevoBeneficio.setFechaBaja(null);

        beneficioRepository.guardar(nuevoBeneficio);
        System.out.println("Servicio: Beneficio #" + proximoNumero + " del Plan " + codigoPlan + " registrado.");
    }

    public List<Beneficio> obtenerTodos() {
        return beneficioRepository.obtenerTodos();
    }

    public Beneficio buscarPorClaveCompuesta(int codigoPlan, int numero) {
        Beneficio beneficio = beneficioRepository.buscarPorClave(codigoPlan, numero);
        
        if (beneficio == null) {
            throw new IllegalArgumentException("No se encontró el beneficio #" + numero + " del plan " + codigoPlan);
        }
        return beneficio;
    }

    public void actualizar(int codigoPlan, int numero, Beneficio beneficioAActualizar) {
        if (beneficioAActualizar == null || beneficioAActualizar.getDescripcion() == null || beneficioAActualizar.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria para actualizar.");
        }

        Beneficio beneficioExistente = buscarPorClaveCompuesta(codigoPlan, numero);

        beneficioAActualizar.setTipoPlan(beneficioExistente.getTipoPlan());
        beneficioAActualizar.setNumero(beneficioExistente.getNumero());
        beneficioAActualizar.setFechaCreacion(beneficioExistente.getFechaCreacion());
        beneficioAActualizar.setFechaBaja(beneficioExistente.getFechaBaja());

        beneficioRepository.actualizar(codigoPlan, numero, beneficioAActualizar);
    }

    public void darDeBaja(int codigoPlan, int numero) throws Exception {
        Beneficio beneficio = buscarPorClaveCompuesta(codigoPlan, numero);
        beneficio.setFechaBaja(LocalDateTime.now());
        beneficioRepository.actualizar(codigoPlan, numero, beneficio);
    }

    public void darDeAltaPostBaja(int codigoPlan, int numero) throws Exception {
        Beneficio beneficio = buscarPorClaveCompuesta(codigoPlan, numero);
        beneficio.setFechaBaja(null);
        beneficioRepository.actualizar(codigoPlan, numero, beneficio);
    }
}