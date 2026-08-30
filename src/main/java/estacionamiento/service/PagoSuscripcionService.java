package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.claves.PagoSuscripcionId;
import estacionamiento.domain.claves.SuscripcionId;
import estacionamiento.domain.TipoPago;
import estacionamiento.repository.PagoSuscripcionRepository;

public class PagoSuscripcionService {

    private final PagoSuscripcionRepository pagoRepository;

    public PagoSuscripcionService(PagoSuscripcionRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public void registrarCobro(int numUsuario, int codPlan, LocalDateTime fechaSub, LocalDateTime fechaEmi, TipoPago tipoPago) {
        PagoSuscripcion pago = buscarComprobanteValidado(numUsuario, codPlan, fechaSub, fechaEmi);

        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden cobrar comprobantes en estado PENDIENTE.");
        }
        
        if (tipoPago == null) {
            throw new IllegalArgumentException("Debe especificar un medio de pago.");
        }

        // Aplicamos el cobro
        pago.setEstado(EstadoPago.PAGADO);
        pago.setTipoPago(tipoPago);
        pago.setFechaHoraPago(LocalDateTime.now());

        pagoRepository.actualizar(pago);
        System.out.println("Servicio: Pago cobrado vía " + tipoPago);
    }

    public void anularComprobante(int numUsuario, int codPlan, LocalDateTime fechaSub, LocalDateTime fechaEmi) {
        PagoSuscripcion pago = buscarComprobanteValidado(numUsuario, codPlan, fechaSub, fechaEmi);

        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new IllegalArgumentException("No se puede anular un comprobante que ya está procesado o cancelado.");
        }

        pago.setEstado(EstadoPago.CANCELADO);
        pagoRepository.actualizar(pago);
        System.out.println("Servicio: Comprobante anulado.");
    }

    public List<PagoSuscripcion> obtenerTodos() {
        return pagoRepository.obtenerTodos();
    }
    
    private PagoSuscripcion buscarComprobanteValidado(int numUsuario, int codPlan, LocalDateTime fechaSub, LocalDateTime fechaEmi) {
        // 1. Reconstruimos el ID de la entidad fuerte
        SuscripcionId subId = new SuscripcionId(numUsuario, codPlan, fechaSub);
        
        // 2. Lo inyectamos en el ID de la entidad débil
        PagoSuscripcionId pagoId = new PagoSuscripcionId(subId, fechaEmi);
        
        PagoSuscripcion pago = pagoRepository.buscarPorClave(pagoId);
        
        if (pago == null) {
            throw new IllegalArgumentException("El comprobante de pago solicitado no existe.");
        }
        
        return pago;
    }
    
    public List<PagoSuscripcion> obtenerPendientesPorUsuario(int numUsuario) {
        return pagoRepository.obtenerTodos().stream()
                .filter(pago -> pago.getId().getSuscripcionId().getNumero() == numUsuario)
                .filter(pago -> pago.getEstado() == EstadoPago.PENDIENTE)
                .toList();
    }
}