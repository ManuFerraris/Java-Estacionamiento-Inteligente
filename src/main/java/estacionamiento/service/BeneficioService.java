package estacionamiento.service;

import java.util.List;
import estacionamiento.domain.Beneficio;
import estacionamiento.repository.BeneficioRepository;
import estacionamiento.repository.TipoPlanRepository;

public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final TipoPlanRepository tipoPlanRepository;

    public BeneficioService(BeneficioRepository beneficioRepository, TipoPlanRepository tipoPlanRepository) {
        this.beneficioRepository = beneficioRepository;
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarBeneficio(Beneficio nuevoBeneficio) {
    	
        // VALIDACION 1 
        if (nuevoBeneficio == null) {
            throw new IllegalArgumentException("El beneficio no puede ser nulo.");
        }

        // Validar descripción
        if (nuevoBeneficio.getDescripcion() == null || nuevoBeneficio.getDescripcion().length() <= 0) {
            throw new IllegalArgumentException("La descripción del beneficio es obligatoria.");
        }

        // Validar la fecha baja
        // Pensar entre los 3 como vamos a manejar la fecha de baja (si se puede programar a futuro o no, etc)

        // =====================================================================
        // TODO: @Nati - Descomentar cuando el TipoPlanRepository esté listo
        /*
        if (tipoPlanRepository.buscarPorCodigo(nuevoBeneficio.getTipoPlan().getCodigo()) == null) {
            throw new IllegalArgumentException("El tipo de plan asociado al beneficio no existe.");
        }
        */
        // =====================================================================

        beneficioRepository.guardar(nuevoBeneficio);
        System.out.println("Servicio: Beneficio validado y enviado al repositorio.");
    }

    public List<Beneficio> listarBeneficios() {
        return beneficioRepository.obtenerTodos();
    }
}