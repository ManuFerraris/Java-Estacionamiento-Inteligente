package estacionamiento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.repository.PagoSuscripcionRepository;
import estacionamiento.repository.SuscripcionRepository;

public class PagoSuscripcionService {

    private final PagoSuscripcionRepository pagoSuscripcionRepository;
    private final SuscripcionRepository suscripcionRepository;

    public PagoSuscripcionService(PagoSuscripcionRepository pagoSuscripcionRepository, 
                                  SuscripcionRepository suscripcionRepository) {
        this.pagoSuscripcionRepository = pagoSuscripcionRepository;
        this.suscripcionRepository = suscripcionRepository;
    }

    public void registrarPagoSuscripcion(PagoSuscripcion nuevoPago) {
        
        // Validar existencia de la suscripción a la que pertenece.
        int codTP = nuevoPago.getSuscripcion().getTipoPlan().getCodigo();
        int numUsu = nuevoPago.getSuscripcion().getUsuario().getNumero();
        LocalDateTime desde = nuevoPago.getSuscripcion().getFechaDesde();

        if (suscripcionRepository.buscarPorClave(codTP, numUsu, desde) == null) {
            throw new IllegalArgumentException("No se puede registrar el pago: La suscripción no existe.");
        }

        // Monto debe ser positivo y exacto
        if (nuevoPago.getMonto() == null || nuevoPago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }

        // Coherencia de fechas
        if (nuevoPago.getFechaHoraPago().isBefore(desde)) {
            throw new IllegalArgumentException("La fecha de pago no puede ser anterior al inicio de la suscripción.");
        }

        // NO podemos permitir pagos en el futuro.
        if (nuevoPago.getFechaHoraPago().isAfter(LocalDateTime.now().plusMinutes(1))) {
            throw new IllegalArgumentException("No se pueden registrar pagos con fecha futura.");
        }

        // Verificación de duplicados
        if (pagoSuscripcionRepository.buscarPorClave(codTP, numUsu, desde, nuevoPago.getFechaHoraEmision()) != null) {
            throw new IllegalArgumentException("Ya existe un registro de pago para este comprobante de suscripción.");
        }

        pagoSuscripcionRepository.guardar(nuevoPago);
        
        System.out.println("Servicio: Pago de suscripción procesado con éxito para el usuario " + numUsu);
    }
}