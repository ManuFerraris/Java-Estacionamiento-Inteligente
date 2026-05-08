package estacionamiento.service;

import java.math.BigDecimal;
import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.repository.PrecioHistoricoTPRepository;
import estacionamiento.repository.TipoPlanRepository;

public class PrecioHistoricoTPService {

    private final PrecioHistoricoTPRepository precioHistoricoRepository;
    private final TipoPlanRepository tipoPlanRepository;

    public PrecioHistoricoTPService(PrecioHistoricoTPRepository precioHistoricoRepository, 
                                    TipoPlanRepository tipoPlanRepository) {
        this.precioHistoricoRepository = precioHistoricoRepository;
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarPrecioHistorico(PrecioHistoricoTP nuevoPrecio) {
        
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El registro de precio histórico no puede ser nulo.");
        }

        if (nuevoPrecio.getFechaDesde() == null) {
            throw new IllegalArgumentException("La fecha de inicio de vigencia es obligatoria.");
        }

        if (nuevoPrecio.getPrecio() == null || nuevoPrecio.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del plan debe ser mayor a cero.");
        }

        int codTP = nuevoPrecio.getTipoPlan().getCodigo();

        // =====================================================================
        // TODO: @Nati - Descomentar esta validación cuando termines tu parte.
        /*
        if (tipoPlanRepository.buscarPorCodigo(codTP) == null) {
            throw new IllegalArgumentException("No se puede asignar el precio: El tipo de plan no existe.");
        }
        */
        // =====================================================================

        if (precioHistoricoRepository.buscarPorClave(codTP, nuevoPrecio.getFechaDesde()) != null) {
            throw new IllegalArgumentException("Ya existe una tarifa configurada para este plan en esa fecha exacta.");
        }

        precioHistoricoRepository.guardar(nuevoPrecio);
        System.out.println("Servicio: Nueva tarifa de suscripción validada y registrada correctamente.");
    }
}