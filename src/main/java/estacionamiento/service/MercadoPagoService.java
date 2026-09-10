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

import java.math.BigDecimal;
import java.util.Collections;

public class MercadoPagoService {

    public MercadoPagoService() {
    }

    public String crearPreferencia(String titulo, BigDecimal monto, Integer idPagoLocal, String emailCliente) throws MPException, MPApiException {
        
        // Si la seña calculada es menor a 15 ARS, forzamos un valor para evitar el rechazo del PolicyAgent.
        BigDecimal montoSeguro = (monto.compareTo(new BigDecimal("15.00")) < 0) ? new BigDecimal("150.00") : monto;

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
                .email("test_user_2000603192@testuser.com") 
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(Collections.singletonList(itemRequest))
                .backUrls(backUrls) 
                .payer(payerRequest)
                .autoReturn("approved")
                .externalReference(idPagoLocal.toString())
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
}