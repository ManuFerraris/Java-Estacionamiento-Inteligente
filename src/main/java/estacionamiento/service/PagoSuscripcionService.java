package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.TipoPago;
import estacionamiento.repository.PagoSuscripcionRepository;

public class PagoSuscripcionService {

    private final PagoSuscripcionRepository pagoRepository;

    public PagoSuscripcionService(PagoSuscripcionRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public void registrarCobro(Integer idPagoSuscripcion, TipoPago tipoPago) {
        PagoSuscripcion pago = buscarComprobanteValidado(idPagoSuscripcion);

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
        System.out.println("Servicio: Pago " + idPagoSuscripcion + " cobrado vía " + tipoPago);
    }

    public void anularComprobante(Integer idPagoSuscripcion) {
        PagoSuscripcion pago = buscarComprobanteValidado(idPagoSuscripcion);

        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new IllegalArgumentException("No se puede anular un comprobante que ya está procesado o cancelado.");
        }

        pago.setEstado(EstadoPago.CANCELADO);
        pagoRepository.actualizar(pago);
        System.out.println("Servicio: Comprobante " + idPagoSuscripcion + " anulado.");
    }

    public List<PagoSuscripcion> obtenerTodos() {
        return pagoRepository.obtenerTodos();
    }
    
    public PagoSuscripcion buscarComprobanteValidado(Integer idPagoSuscripcion) {
        
        PagoSuscripcion pago = pagoRepository.buscarPorClave(idPagoSuscripcion);
        if (pago == null) {
            throw new IllegalArgumentException("El comprobante de pago solicitado no existe.");
        }
        
        return pago;
    }
    
    public List<PagoSuscripcion> obtenerPendientesPorUsuario(int numUsuario) {
        return pagoRepository.obtenerTodos().stream()
                .filter(pago -> pago.getSuscripcion().getUsuario().getNumero() == numUsuario)
                .filter(pago -> pago.getEstado() == EstadoPago.PENDIENTE)
                .toList();
    }
    
    public void registrarCobroMercadoPago(Integer idPagoSuscripcion, String idTransaccionMp) {
    	System.out.println("   -> [SERVICIO] Entrando a actualizar comprobante ID: " + idPagoSuscripcion);
    	PagoSuscripcion pago = buscarComprobanteValidado(idPagoSuscripcion);
    	System.out.println("   -> [SERVICIO] Comprobante encontrado. Estado actual: " + pago.getEstado());
    	
    	if(pago.getEstado() == EstadoPago.PENDIENTE) {
    		pago.setEstado(EstadoPago.APROBADO);
    		pago.setTipoPago(TipoPago.MERCADO_PAGO);
    		pago.setFechaHoraPago(LocalDateTime.now());
    		pago.setIdTransaccionMp(idTransaccionMp);System.out.println("   -> [SERVICIO] Ejecutando repositorio.actualizar(pago)...");
            pagoRepository.actualizar(pago);
            System.out.println("   -> [SERVICIO] ¡ACTUALIZACIÓN EXITOSA EN MYSQL!");
        } else {
            System.out.println("   -> [SERVICIO] Omitido: El comprobante no estaba PENDIENTE.");
        }
    }
}