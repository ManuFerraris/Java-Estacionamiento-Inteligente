package estacionamiento;
import estacionamiento.domain.*;

import estacionamiento.repository.PagoSuscripcionRepository;
import estacionamiento.repository.PagoSuscripcionRepositoryMemoria;

import estacionamiento.service.PagoSuscripcionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Aca podemos probar las distintas clases que vamos creando
public class MainPrueba {
	public static void main(String[] args) {
		System.out.println("\n==================================================");
	    System.out.println("   PRUEBA: MÓDULO DE PAGOS DE SUSCRIPCIÓN");
	    System.out.println("==================================================\n");

	    System.out.println("--- 1. INICIALIZANDO (Y SIMULANDO) OBJETOS PADRES (DATOS MOCK) ---");
	    // Simulamos los datos que Nati va a crear (suscripcion y tipoPlan)
	    Usuario usuarioNati = new Usuario();
	    usuarioNati.setNumero(1);
	    usuarioNati.setNombre("Natalia");

	    TipoPlan planMensual = new TipoPlan();
	    planMensual.setCodigo(10);
	    planMensual.setNombre("Plan Mensual Full");

	    LocalDateTime fechaInicioSuscripcion = LocalDateTime.now().minusDays(5); // La suscripción empezó hace 5 días

	    Suscripcion miSuscripcion = new Suscripcion();
	    miSuscripcion.setUsuario(usuarioNati);
	    miSuscripcion.setTipoPlan(planMensual);
	    miSuscripcion.setFechaDesde(fechaInicioSuscripcion);

	    System.out.println("\n--- 2. INICIALIZANDO REPOSITORIOS Y SERVICIO ---");
	    PagoSuscripcionRepository repoPagosSus = new PagoSuscripcionRepositoryMemoria();
	    
	    // Le pasamos 'null' al segundo repositorio porque Nati todavia no lo programó (usamos la Opción 1 del TODO )
	    // HAGANME ACORDAR ASI LES EXPLICO COMO FUNCIONA LO DEL 'TODO'
	    PagoSuscripcionService servicioPagosSus = new PagoSuscripcionService(repoPagosSus, null);

	    System.out.println("\n--- 3. CREANDO EL PAGO DE LA SUSCRIPCIÓN ---");
	    PagoSuscripcion pagoExitoso = new PagoSuscripcion();
	    pagoExitoso.setSuscripcion(miSuscripcion);
	    pagoExitoso.setFechaHoraEmision(LocalDateTime.now().minusHours(1)); // La factura/cuota/lo que sea se emitió hace 1 hora
	    pagoExitoso.setFechaHoraPago(LocalDateTime.now()); // Se está pagando en este exacto momento
	    pagoExitoso.setMonto(new BigDecimal("15000.00"));
	    pagoExitoso.setTipoPago(TipoPago.TRANSFERENCIA); 
	    pagoExitoso.setEstado(EstadoPago.PAGADO); 

	    try {
	        servicioPagosSus.registrarPagoSuscripcion(pagoExitoso);
	    } catch (Exception e) {
	        System.out.println("Error al registrar: " + e.getMessage());
	    }

	    System.out.println("\n--- 4. COMPROBANDO LA PERSISTENCIA EN MEMORIA ---");
	    // Buscamos usando la clave compuesta
	    PagoSuscripcion recuperado = repoPagosSus.buscarPorClave(
	        10, // codTP
	        1,  // numUsu
	        fechaInicioSuscripcion,
	        pagoExitoso.getFechaHoraEmision()
	    );

	    if (recuperado != null) {
	        System.out.println("¡ÉXITO AL PAGAR LA SUSCRIPCION!");
	        System.out.println("Se encontró el pago de la suscripción del plan: " + recuperado.getSuscripcion().getTipoPlan().getNombre());
	        System.out.println("Monto abonado: $" + recuperado.getMonto());
	        System.out.println("Estado actual: " + recuperado.getEstado());
	    } else {
	        System.out.println("Fallo: No se pudo recuperar el pago de la memoria.");
	    }
	}
}