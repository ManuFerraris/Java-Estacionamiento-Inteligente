package estacionamiento.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.resources.payment.Payment;

import estacionamiento.domain.EstadoPago;
import estacionamiento.repository.mysql.PagoRepositoryMySQL;
import estacionamiento.service.PagoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/webhook-mp")
public class WebhookMercadoPagoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private PagoService pagoService;
    
    @Override
    public void init() throws ServletException {
        this.pagoService = new PagoService(new PagoRepositoryMySQL());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Si no respondemos con un 200 OK en menos de 10 segundos, MP asume que el servidor se cayó y reintenta el envío.
        response.setStatus(HttpServletResponse.SC_OK);

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String payload = sb.toString();
            
            // Verificamos que sea una notificación de pago
            if (payload.contains("\"type\":\"payment\"") || payload.contains("\"type\": \"payment\"")) {
                
                // Extraemos el ID del pago usando Regex puro
                Pattern pattern = Pattern.compile("\"data\"\\s*:\\s*\\{\\s*\"id\"\\s*:\\s*\"?(\\d+)\"?");
                Matcher matcher = pattern.matcher(payload);
                
                if (matcher.find()) {
                    Long paymentId = Long.parseLong(matcher.group(1));
                    
                    // CONSULTA SEGURA: Vamos a buscar el pago real a MP
                    PaymentClient client = new PaymentClient();
                    
                    // Reutilizamos la estrategia de MPRequestOptions para no depender de Tomcat
                    MPRequestOptions options = MPRequestOptions.builder()
                            .accessToken("APP_USR-713702972657291-090815-59d577f977930b4ffdd43d2e1a5ff00d-1997921995")
                            .build();
                    
                    Payment payment = client.get(paymentId, options);
                    
                    // Actualizar la Base de Datos
                    if (payment != null && payment.getExternalReference() != null) {
                        Integer idPagoLocal = Integer.parseInt(payment.getExternalReference());
                        String estadoMp = payment.getStatus();
                        
                        if ("approved".equals(estadoMp)) {
                            pagoService.actualizarEstado(idPagoLocal, EstadoPago.APROBADO, paymentId.toString());
                            System.out.println("WEBHOOK AVISO: Pago local " + idPagoLocal + " APROBADO asíncronamente.");
                        } else if ("rejected".equals(estadoMp) || "cancelled".equals(estadoMp)) {
                            pagoService.actualizarEstado(idPagoLocal, EstadoPago.RECHAZADO, paymentId.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando el Webhook de MP: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
