package estacionamiento.service;
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
        if (nuevoPago.getMonto() == null || nuevoPago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }

        // El tipo de pago es obligatorio (Efectivo, Tarjeta, etc.)
        if (nuevoPago.getTipoPago() == null) {
            throw new IllegalArgumentException("Se debe especificar un tipo de pago válido.");
        }

        // La fecha no puede ser en el futuro extremo
        if (nuevoPago.getFechaHora().isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("La fecha del pago no puede ser futura.");
        }

        // Si pasa todas las validaciones, delegamos al repositorio
        pagoRepository.guardar(nuevoPago);
        
        System.out.println("Servicio: Pago validado y procesado correctamente.");
	}
}