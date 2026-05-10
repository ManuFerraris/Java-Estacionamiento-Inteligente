package estacionamiento;

import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.Pago;
import estacionamiento.domain.TipoPago;
import estacionamiento.repository.PagoRepository;
import estacionamiento.repository.PagoRepositoryMySQL;
import estacionamiento.service.PagoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MainPruebaSQL {

	public static void main(String[] args) {

		System.out.println("==================================================");
        System.out.println("   PRUEBA: CONEXIÓN A MYSQL - MÓDULO FINANZAS");
        System.out.println("==================================================\n");

        System.out.println("--- 1. INICIALIZANDO REPOSITORIO Y SERVICIO ---");
        try {
            PagoRepository repoPagosMySQL = new PagoRepositoryMySQL();
            PagoService servicioPagos = new PagoService(repoPagosMySQL);

            System.out.println("\n--- 2. CREANDO UN PAGO PARA LA BASE DE DATOS ---");
            Pago pagoReal = new Pago();
            pagoReal.setNumero(999);
            pagoReal.setMonto(new BigDecimal("4500.50"));
            pagoReal.setFechaHora(LocalDateTime.now());
            pagoReal.setTipoPago(TipoPago.TRANSFERENCIA);
            pagoReal.setEstado(EstadoPago.PAGADO);

            System.out.println("Intentando enviar el pago a través del cable de red (Hibernate)...");
            servicioPagos.registrarPago(pagoReal);

            System.out.println("\n--- 3. VERIFICANDO LA PERSISTENCIA EN MYSQL ---");
            // Ahora buscarPorClave funciona con Hibernate y busca en la bd.
            Pago recuperado = repoPagosMySQL.buscarPorClave(999);

            if (recuperado != null) {
                System.out.println("¡ÉXITO TOTAL: EL PUENTE DE DATOS FUNCIONA!");
                System.out.println("Se recuperó de MySQL el pago N°: " + recuperado.getNumero());
                System.out.println("Monto guardado en disco duro: $" + recuperado.getMonto());
                System.out.println("Estado actual en BD: " + recuperado.getEstado());
            } else {
                System.out.println("Fallo: No se encontró el pago en MySQL.");
            }

        } catch (Exception e) {
            System.err.println("¡Ocurrió un error catastrófico al intentar conectar o guardar!");
            System.err.println("Motivo: " + e.getMessage());
            e.printStackTrace();
        }
	}

}
