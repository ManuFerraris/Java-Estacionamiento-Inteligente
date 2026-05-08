package estacionamiento.service;

import java.math.BigDecimal;
import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.repository.PrecioHistoricoTVRepository;
import estacionamiento.repository.TipoVehiculoRepository;

public class PrecioHistoricoTVService {

    private final PrecioHistoricoTVRepository precioHistoricoRepository;
    private final TipoVehiculoRepository tipoVehiculoRepository;

    public PrecioHistoricoTVService(PrecioHistoricoTVRepository precioHistoricoRepository, 
                                    TipoVehiculoRepository tipoVehiculoRepository) {
        this.precioHistoricoRepository = precioHistoricoRepository;
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    public void registrarPrecioHistorico(PrecioHistoricoTV nuevoPrecio) {
        
        // Integridad básica del objeto
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El registro de precio histórico no puede ser nulo.");
        }

        // La fecha de vigencia es obligatoria
        if (nuevoPrecio.getFechaDesde() == null) {
            throw new IllegalArgumentException("La fecha de inicio de vigencia es obligatoria.");
        }

        // El precio debe ser un número positivo (Usando BigDecimal correctamente)
        if (nuevoPrecio.getPrecio() == null || nuevoPrecio.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio configurado debe ser mayor a cero.");
        }

        int numTV = nuevoPrecio.getTipoVehiculo().getNumero();

        // =====================================================================
        // TODO: @Marilu - Descomenta esta validación cuando termines tu parte.
        /*
        if (tipoVehiculoRepository.buscarPorCodigo(codTV) == null) {
            throw new IllegalArgumentException("No se puede asignar el precio: El tipo de vehículo no existe.");
        }
        */
        // =====================================================================

        // Evitar duplicados exactos en el historial
        if (precioHistoricoRepository.buscarPorClave(numTV, nuevoPrecio.getFechaDesde()) != null) {
            throw new IllegalArgumentException("Ya existe una tarifa configurada para este vehículo en esa fecha exacta.");
        }

        precioHistoricoRepository.guardar(nuevoPrecio);
        
        System.out.println("Servicio: Nueva tarifa validada y registrada correctamente en el historial.");
    }
}