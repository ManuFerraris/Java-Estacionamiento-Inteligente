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

    public BeneficioService(
            BeneficioRepository beneficioRepository,
            TipoPlanRepository tipoPlanRepository) {

        this.beneficioRepository = beneficioRepository;
        this.tipoPlanRepository = tipoPlanRepository;
    }

    // ALTA
    public void registrarBeneficio(int codigoPlan, Beneficio nuevoBeneficio) {

        if (codigoPlan <= 0) {
            throw new IllegalArgumentException(
                "El código del plan debe ser válido.");
        }

        if (nuevoBeneficio == null) {
            throw new IllegalArgumentException(
                "El beneficio no puede ser nulo.");
        }

        if (nuevoBeneficio.getDescripcion() == null ||
                nuevoBeneficio.getDescripcion().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "La descripción del beneficio es obligatoria.");
        }

        TipoPlan planAsociado =
            tipoPlanRepository.buscarPorClave(codigoPlan);

        if (planAsociado == null) {
            throw new IllegalArgumentException(
                "El tipo de plan asociado no existe en la base de datos.");
        }

        if (planAsociado.getFechaBaja() != null) {
            throw new IllegalArgumentException(
                "No se pueden registrar beneficios en un plan dado de baja.");
        }

        // Asociamos el beneficio con el plan.
        nuevoBeneficio.setTipoPlan(planAsociado);

        // Generamos el próximo número dentro del plan.
        int proximoNumero =
            beneficioRepository.obtenerProximoNumeroPorPlan(codigoPlan);

        nuevoBeneficio.setNumero(proximoNumero);

        // Establecemos las fechas.
        nuevoBeneficio.setFechaCreacion(LocalDateTime.now());
        nuevoBeneficio.setFechaBaja(null);

        beneficioRepository.guardar(nuevoBeneficio);

        System.out.println(
            "Servicio: Beneficio #" + proximoNumero +
            " del Plan " + codigoPlan + " registrado.");
    }

    // CONSULTA GENERAL
    public List<Beneficio> obtenerTodos() {
        return beneficioRepository.obtenerTodos();
    }

    // CONSULTA INDIVIDUAL
    public Beneficio buscarPorClaveCompuesta(int codigoPlan, int numero) {

        if (codigoPlan <= 0 || numero <= 0) {
            throw new IllegalArgumentException(
                "El código del plan y el número del beneficio deben ser válidos.");
        }

        Beneficio beneficio =
            beneficioRepository.buscarPorClave(codigoPlan, numero);

        if (beneficio == null) {
            throw new IllegalArgumentException(
                "No se encontró el beneficio #" + numero +
                " del plan " + codigoPlan);
        }

        return beneficio;
    }

    // MODIFICACIÓN
    public void actualizar(
            int codigoPlan,
            int numero,
            Beneficio beneficioAActualizar) {

        if (beneficioAActualizar == null) {
            throw new IllegalArgumentException(
                "El beneficio a actualizar no puede ser nulo.");
        }

        if (beneficioAActualizar.getDescripcion() == null ||
                beneficioAActualizar.getDescripcion().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "La descripción es obligatoria para actualizar.");
        }

        Beneficio beneficioExistente =
            buscarPorClaveCompuesta(codigoPlan, numero);

        // Conservamos los datos que no deben modificarse.
        beneficioAActualizar.setTipoPlan(
            beneficioExistente.getTipoPlan());

        beneficioAActualizar.setNumero(
            beneficioExistente.getNumero());

        beneficioAActualizar.setFechaCreacion(
            beneficioExistente.getFechaCreacion());

        beneficioAActualizar.setFechaBaja(
            beneficioExistente.getFechaBaja());

        beneficioRepository.actualizar(
            codigoPlan, numero, beneficioAActualizar);
    }

    // BAJA LÓGICA
    public void darDeBaja(int codigoPlan, int numero) {

        buscarPorClaveCompuesta(codigoPlan, numero);

        beneficioRepository.eliminar(codigoPlan, numero);
    }

    // REACTIVACIÓN
    public void darDeAltaPostBaja(int codigoPlan, int numero) {

        Beneficio beneficio =
            buscarPorClaveCompuesta(codigoPlan, numero);

        if (beneficio.getFechaBaja() != null) {

            beneficio.setFechaBaja(null);

            beneficioRepository.actualizar(
                codigoPlan, numero, beneficio);
        }
    }
}
