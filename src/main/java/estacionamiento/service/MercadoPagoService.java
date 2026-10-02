package estacionamiento.service;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import estacionamiento.domain.PagoSuscripcion;

import java.math.BigDecimal;
import java.util.Collections;

public class MercadoPagoService {

    public MercadoPagoService() {
    }

    public String crearPreferencia(String titulo, BigDecimal monto, Integer idPagoLocal, String emailCliente) throws MPException, MPApiException {
        
    	System.out.println("[MercadoPagoService] Parametros recibidos: " 
    			+ "Titulo: "+ titulo +"\n" 
    			+ "Monto: "+ monto +"\n"
    			+ "Id Pago Local: " + idPagoLocal +"\n"
    			+ "Email Cliente: " + emailCliente + "\n");
        // Si la seña calculada es menor a 15 ARS, forzamos un valor para evitar el rechazo del PolicyAgent.
        BigDecimal montoSeguro = (monto.compareTo(new BigDecimal("15.00")) < 0) ? new BigDecimal("150.00") : monto;
        
        System.out.println("Monto recibido/calculado: " + montoSeguro);
        
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(titulo)
                .quantity(1)
                .unitPrice(montoSeguro)
                .currencyId("ARS")
                .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("localhost:8080/backend-estacionamiento/api/pago-exito")
                .pending("localhost:8080/backend-estacionamiento/api/pago-pendiente")
                .failure("localhost:8080/backend-estacionamiento/api/pago-fallo")
                .build();

        // 2. Comprador de prueba oficial (Bypass de auto-compra)
        PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                .email("test_user_3682619193@testuser.com") 
                .build();

        String referenciaExterna = "RES_" + idPagoLocal;
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(Collections.singletonList(itemRequest))
                .backUrls(backUrls) 
                .payer(payerRequest)
                .autoReturn("approved")
                .externalReference(referenciaExterna)
                // ATENTISSSS
                // Cada vez que levantamos Ngrok la url CAMBIA!!! 
                // Solamente agreguen la nueva aca y listo "https://.../backend-estacionamiento/api/webhook-mp"
                .notificationUrl("https://canteen-washhouse-clever.ngrok-free.dev/backend-estacionamiento/api/webhook-mp")
                .build();

        // 3. EL BYPASS ARQUITECTÓNICO: MPRequestOptions
        // Inyectamos el token del VENDEDOR explícitamente en esta transacción exacta.
        MPRequestOptions options = MPRequestOptions.builder()
                .accessToken("APP_USR-713702972657291-090815-59d577f977930b4ffdd43d2e1a5ff00d-1997921995")
                .build();

        PreferenceClient client = new PreferenceClient();
        
        // Pasamos el 'options' como segundo parámetro para forzar la credencial
        Preference preference = client.create(preferenceRequest, options);

        return preference.getInitPoint(); 
    }
    
    public String crearPreferenciaSuscripcion(PagoSuscripcion pago) throws Exception {
        
        // 1. Datos del Plan a cobrar
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Suscripción - " + pago.getSuscripcion().getTipoPlan().getNombre())
                .quantity(1)
                .unitPrice(pago.getMonto())
                .currencyId("ARS")
                .build();

        // 2. Las mismas URLs de retorno que ya tenés
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("localhost:8080/backend-estacionamiento/api/pago-exito")
                .pending("localhost:8080/backend-estacionamiento/api/pago-pendiente")
                .failure("localhost:8080/backend-estacionamiento/api/pago-fallo")
                .build();
        
        PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                .email("test_user_3682619193@testuser.com") 
                .build();

        // 3. Identificador Polimórfico (Acá está la magia)
        String referenciaExterna = "SUSC_" + pago.getId();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(Collections.singletonList(itemRequest))
                .backUrls(backUrls) 
                .autoReturn("approved") 
                .externalReference(referenciaExterna)
                .payer(payerRequest)
                .notificationUrl("https://canteen-washhouse-clever.ngrok-free.dev/backend-estacionamiento/api/webhook-mp") 
                .build();

        MPRequestOptions options = MPRequestOptions.builder()
                .accessToken("APP_USR-713702972657291-090815-59d577f977930b4ffdd43d2e1a5ff00d-1997921995")
                .build();

        PreferenceClient client = new PreferenceClient();
        
        System.out.println("Preferencia de SUSCRIPCION a enviar: " + preferenceRequest + "\n" + preferenceRequest);
        Preference preference = client.create(preferenceRequest, options);
        System.out.println("Preferencia final de SUSCRIPCION: " + preference);
        
        return preference.getSandboxInitPoint(); 
    }
}