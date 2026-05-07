package estacionamiento;
import estacionamiento.domain.Pago;
import estacionamiento.repository.PagoRepository;
import estacionamiento.repository.PagoRepositoryMemoria;
import estacionamiento.service.PagoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Aca podemos probar las distintas clases que vamos creando
public class MainPrueba {
	public static void main(String[] args) {
	    // 1. Inicialización
	    PagoRepository repoPagos = new PagoRepositoryMemoria();
	    PagoService servicioPagos = new PagoService(repoPagos);

	    System.out.println("--- PRUEBA 1: Registro de Pago Exitoso ---");
	    try {
	        Pago pagoExitoso = new Pago();
	        pagoExitoso.setNumero(101);
	        pagoExitoso.setMonto(new BigDecimal("2500.50"));
	        pagoExitoso.setFechaHora(LocalDateTime.now());
	        pagoExitoso.setTipoPago("TARJETA_DEBITO");
	        pagoExitoso.setEstado("APROBADO");

	        servicioPagos.registrarPago(pagoExitoso);
	    } catch (Exception e) {
	        System.out.println("Error inesperado: " + e.getMessage());
	    }

	    System.out.println("\n--- PRUEBA 2: Intento de Pago con Monto Negativo ---");
	    try {
	        Pago pagoInvalido = new Pago();
	        pagoInvalido.setNumero(102);
	        pagoInvalido.setMonto(new BigDecimal("-100.00")); // Monto inválido
	        pagoInvalido.setFechaHora(LocalDateTime.now());
	        pagoInvalido.setTipoPago("EFECTIVO");

	        servicioPagos.registrarPago(pagoInvalido);
	    } catch (IllegalArgumentException e) {
	        System.out.println("Validación capturada correctamente: " + e.getMessage());
	    }

	    System.out.println("\n--- PRUEBA 3: Verificación de Persistencia ---");
	    if (repoPagos.buscarPorClave(101) != null) {
	        System.out.println("Confirmado: El pago 101 reside en el repositorio.");
	    }
	}
}