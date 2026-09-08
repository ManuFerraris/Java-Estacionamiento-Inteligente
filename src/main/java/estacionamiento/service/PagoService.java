package estacionamiento.service;
import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.Pago;
import estacionamiento.repository.PagoRepository;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class PagoService {

	private final PagoRepository pagoRepository;
	
	public PagoService(PagoRepository pagoRepository) {
		this.pagoRepository = pagoRepository;
	} 
	
	public void registrarPago(Pago nuevoPago) {
        if (nuevoPago == null) {
            throw new IllegalArgumentException("No se puede registrar un pago nulo.");
        }

        // El monto debe ser mayor a cero
        if (nuevoPago.getMonto() == null || nuevoPago.getMonto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto del pago no puede ser negativo.");
        }

        // Flexibilidad para MP: Si el pago está APROBADO, sí o sí debe tener un tipo de pago asociado.
        // Si está PENDIENTE (recién generado para Mercado Pago), permitimos que sea nulo momentáneamente.
        if ( (EstadoPago.APROBADO == nuevoPago.getEstado() ) && nuevoPago.getTipoPago() == null) {
            throw new IllegalArgumentException("Se debe especificar un tipo de pago válido para pagos aprobados.");
        }

        // La fecha no puede ser en el futuro extremo
        if (nuevoPago.getFechaHora() != null && nuevoPago.getFechaHora().isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("La fecha del pago no puede ser futura.");
        }

        // Asignamos fecha por defecto si viene nula en la creación
        if (nuevoPago.getFechaHora() == null) {
            nuevoPago.setFechaHora(LocalDateTime.now());
        }

        pagoRepository.guardar(nuevoPago);
        System.out.println("Servicio: Pago validado y persistido correctamente. Estado: " + nuevoPago.getEstado());
    }

	public void actualizarPago(Integer numeroPago, Pago pagoActualizado) {
        if (numeroPago == null || pagoActualizado == null) {
            throw new IllegalArgumentException("El pago a actualizar no puede ser nulo.");
        }

        // Validación estricta: Si el Webhook de MP o el empleado lo pasa a APROBADO, 
        // tiene que quedar registrado cómo se pagó (MP, Efectivo, etc.)
        if (EstadoPago.APROBADO.equals(pagoActualizado.getEstado()) && pagoActualizado.getTipoPago() == null) {
            throw new IllegalArgumentException("Un pago aprobado debe tener un tipo de pago asignado.");
        }

        // Interesante, si tuviéramos lógica extra, como enviar factura por mail al aprobarse, iría aca.

        pagoRepository.actualizar(numeroPago, pagoActualizado);
        System.out.println("Servicio: Pago ID " + numeroPago + " actualizado al estado " + pagoActualizado.getEstado());
    }
	
    public Pago buscarPago(int id) {
        return pagoRepository.buscarPorClave(id);
    }
}