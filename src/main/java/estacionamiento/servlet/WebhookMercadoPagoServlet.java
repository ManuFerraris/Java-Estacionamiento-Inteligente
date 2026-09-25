package estacionamiento.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.resources.payment.Payment;

import estacionamiento.domain.EstadoPago;
import estacionamiento.repository.mysql.PagoRepositoryMySQL;
import estacionamiento.repository.mysql.PagoSuscripcionRepositoryMySQL;
import estacionamiento.service.PagoService;
import estacionamiento.service.PagoSuscripcionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/webhook-mp")
public class WebhookMercadoPagoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private PagoService pagoService;
    private PagoSuscripcionService pagoSuscripcionService;
    
    @Override
    public void init() throws ServletException {
        this.pagoService = new PagoService(new PagoRepositoryMySQL());
        this.pagoSuscripcionService = new PagoSuscripcionService(new PagoSuscripcionRepositoryMySQL());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n========== [WEBHOOK] INICIO DE INTERCEPCIÓN ==========");
        
        // Retorno inmediato del 200 OK
        response.setStatus(HttpServletResponse.SC_OK);

        try {
            // 1. Priorizamos extracción de parámetros nativos (Evita vaciado del Reader en formato IPN)
            String eventType = request.getParameter("type");
            if (eventType == null) {
                eventType = request.getParameter("topic");
            }
            
            String paymentIdStr = request.getParameter("data.id");
            if (paymentIdStr == null) {
                paymentIdStr = request.getParameter("id");
            }

            // 2. Fallback: Parseo JSON solo si los parámetros URL están vacíos (Formato Webhook V1)
            if (eventType == null || paymentIdStr == null) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = request.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String payload = sb.toString();
                System.out.println("[WEBHOOK] Payload crudo recibido: " + payload);
                
                if (!payload.trim().isEmpty()) {
                    JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
                    eventType = jsonObject.has("type") ? jsonObject.get("type").getAsString() : 
                               (jsonObject.has("topic") ? jsonObject.get("topic").getAsString() : null);
                    
                    if (jsonObject.has("data") && jsonObject.getAsJsonObject("data").has("id")) {
                        paymentIdStr = jsonObject.getAsJsonObject("data").get("id").getAsString();
                    } else if (jsonObject.has("resource")) {
                        String resourceUrl = jsonObject.get("resource").getAsString();
                        paymentIdStr = resourceUrl.substring(resourceUrl.lastIndexOf('/') + 1);
                    }
                }
            }

            System.out.println("[WEBHOOK] Tipo de evento: " + eventType + " | ID extraído: " + paymentIdStr);

            Long paymentId = null;

            // 3. Manejo inteligente según si llega como pago directo o como orden comercial (merchant_order)
            if ("payment".equals(eventType) && paymentIdStr != null) {
                paymentId = Long.parseLong(paymentIdStr);
            } 
            else if ("merchant_order".equals(eventType) && paymentIdStr != null) {
                System.out.println("[WEBHOOK] Procesando Orden Comercial para extraer el pago asociado...");
                com.mercadopago.client.merchantorder.MerchantOrderClient orderClient = new com.mercadopago.client.merchantorder.MerchantOrderClient();
                MPRequestOptions options = MPRequestOptions.builder()
                        .accessToken("APP_USR-713702972657291-090815-59d577f977930b4ffdd43d2e1a5ff00d-1997921995")
                        .build();
                
                com.mercadopago.resources.merchantorder.MerchantOrder order = orderClient.get(Long.parseLong(paymentIdStr), options);
                
                if (order != null && order.getPayments() != null) {
                    for (var pay : order.getPayments()) {
                        if ("approved".equals(pay.getStatus().toString())) {
                            paymentId = pay.getId();
                            System.out.println("[WEBHOOK] ¡Pago aprobado encontrado dentro de la orden comercial ID: " + paymentId + "!");
                            break;
                        }
                    }
                }
            }

            // 4. Ejecución del negocio aislada si logramos rescatar el ID del pago
            if (paymentId != null) {
                System.out.println("[WEBHOOK] Consultando API oficial de MP para ID de pago: " + paymentId);
                
                PaymentClient client = new PaymentClient();
                MPRequestOptions options = MPRequestOptions.builder()
                        .accessToken("APP_USR-713702972657291-090815-59d577f977930b4ffdd43d2e1a5ff00d-1997921995")
                        .build();
                
                Payment payment = client.get(paymentId, options);
                
                if (payment != null && payment.getExternalReference() != null) {
                    String refExterna = payment.getExternalReference();
                    String estadoMp = payment.getStatus();
                    
                    System.out.println("[WEBHOOK] API MP -> Estado: " + estadoMp + " | Ref: " + refExterna);
                    
                    if (refExterna.startsWith("SUSC_")) {
                        Integer idPagoSuscripcion = Integer.parseInt(refExterna.replace("SUSC_", ""));
                        if ("approved".equals(estadoMp)) {
                            pagoSuscripcionService.registrarCobroMercadoPago(idPagoSuscripcion, paymentId.toString());
                            System.out.println("[WEBHOOK] Suscripción " + idPagoSuscripcion + " actualizada a APROBADO.");
                        }
                    } else if (refExterna.startsWith("RES_") || refExterna.matches("\\d+")) {
                        String idReservaLimpiado = refExterna.replace("RES_", "");
                        Integer idPagoLocal = Integer.parseInt(idReservaLimpiado);
                        if ("approved".equals(estadoMp)) {
                            pagoService.actualizarEstado(idPagoLocal, EstadoPago.APROBADO, paymentId.toString());
                            System.out.println("[WEBHOOK] Reserva " + idPagoLocal + " actualizada a APROBADO.");
                        }
                    }
                }
            } else {
                System.out.println("[WEBHOOK] Evento recibido pero sin un ID de pago asociado listo para procesar.");
            }
            
        } catch (Exception e) {
            System.err.println("\n[WEBHOOK] EXCEPCIÓN FATAL DETECTADA:");
            e.printStackTrace();
        }
        System.out.println("========== [WEBHOOK] FIN DE INTERCEPCIÓN ==========\n");
    }
    
}
