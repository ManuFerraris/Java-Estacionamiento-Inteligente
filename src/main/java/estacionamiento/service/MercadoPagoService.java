package estacionamiento.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Properties;

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
import estacionamiento.util.AppLogger;

public class MercadoPagoService {

    private final String mpAccessToken;
    private final String appBaseUrl;
    private final String webhookUrl;
    private final String testPayerEmail;

    public MercadoPagoService() {
        this.mpAccessToken = obtenerConfig("MP_ACCESS_TOKEN", "mercadopago.access.token", "");
        // En Clever Cloud: https://tu-app.cleverapps.io (sin barra al final)
        // En Local: http://localhost:8080/backend-estacionamiento
        this.appBaseUrl = obtenerConfig("APP_BASE_URL", "app.base.url", "http://localhost:8080/backend-estacionamiento");
        this.testPayerEmail = obtenerConfig("MP_TEST_PAYER_EMAIL", "mercadopago.test.payer.email", "");
        AppLogger.info("[INFORMACION] Sistema de MercadoPagoService iniciando en la ruta :" + appBaseUrl);
        
        // Si estamos en producción (APP_BASE_URL), el webhook usa el dominio real.
        // Si estamos en local, lee la URL de Ngrok desde config.properties.
        String envBaseUrl = System.getenv("APP_BASE_URL");
        if (envBaseUrl != null && !envBaseUrl.trim().isEmpty()) {
            this.webhookUrl = this.appBaseUrl + "/api/webhook-mp";
        } else {
            this.webhookUrl = obtenerConfig("MP_WEBHOOK_URL", "mercadopago.webhook.url", this.appBaseUrl + "/api/webhook-mp");
        }
    }

    private String obtenerConfig(String envKey, String propKey, String defaultValue) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                String propVal = props.getProperty(propKey);
                if (propVal != null && !propVal.trim().isEmpty()) {
                    return propVal.trim();
                }
            }
        } catch (Exception e) {
            AppLogger.error("Error leyendo config.properties para: " + propKey, e);
        }
        return defaultValue;
    }

    private PreferenceBackUrlsRequest construirBackUrls() {
        return PreferenceBackUrlsRequest.builder()
                .success(this.appBaseUrl + "/api/pago-exito")
                .pending(this.appBaseUrl + "/api/pago-pendiente")
                .failure(this.appBaseUrl + "/api/pago-fallo")
                .build();
    }
    
    private String resolverEmailPagador(String emailReal) {
    	if(!this.testPayerEmail.isEmpty()) {
    		return this.testPayerEmail;
    	}
    	return emailReal;
    }

    public String crearPreferencia(String titulo, BigDecimal monto, Integer idPagoLocal, String emailCliente) throws MPException, MPApiException {
        BigDecimal montoSeguro = (monto.compareTo(new BigDecimal("15.00")) < 0) ? new BigDecimal("150.00") : monto;

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(titulo)
                .quantity(1)
                .unitPrice(montoSeguro)
                .currencyId("ARS")
                .build();

        PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                .email(resolverEmailPagador(emailCliente))
                .build();

        PreferenceRequest.PreferenceRequestBuilder requestBuilder = PreferenceRequest.builder()
                .items(Collections.singletonList(itemRequest))
                .backUrls(construirBackUrls())
                .payer(payerRequest)
                .externalReference("RES_" + idPagoLocal)
                .notificationUrl(this.webhookUrl);
		        if (this.appBaseUrl.startsWith("https://")) {
		            requestBuilder.autoReturn("approved");
		        }
        PreferenceRequest preferenceRequest = requestBuilder.build();

        MPRequestOptions options = MPRequestOptions.builder()
                .accessToken(this.mpAccessToken)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest, options);

        return preference.getInitPoint();
    }

    public String crearPreferenciaSuscripcion(PagoSuscripcion pago) throws Exception {
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Suscripción - " + pago.getSuscripcion().getTipoPlan().getNombre())
                .quantity(1)
                .unitPrice(pago.getMonto())
                .currencyId("ARS")
                .build();

        String emailUsuario = (pago.getSuscripcion() != null && pago.getSuscripcion().getUsuario() != null) ?
        		pago.getSuscripcion().getUsuario().getMail() : "";
        
        PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                .email(resolverEmailPagador(emailUsuario))
                .build();

        PreferenceRequest.PreferenceRequestBuilder requestBuilder = PreferenceRequest.builder()
                .items(Collections.singletonList(itemRequest))
                .backUrls(construirBackUrls())
                .externalReference("SUSC_" + pago.getId())
                .payer(payerRequest)
                .notificationUrl(this.webhookUrl);
                if (this.appBaseUrl.startsWith("https://")) {
                    requestBuilder.autoReturn("approved");
                }
        PreferenceRequest preferenceRequest = requestBuilder.build();
                
        MPRequestOptions options = MPRequestOptions.builder()
                .accessToken(this.mpAccessToken)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest, options);

        return preference.getInitPoint();
    }
}