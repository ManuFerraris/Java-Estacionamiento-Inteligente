package estacionamiento.service;

import estacionamiento.util.AppLogger;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {
    
	private static final String CORREO_REMITENTE = obtenerConfig("MAIL_REMITENTE", "mail.remitente");
    private static final String PASSWORD_APP = obtenerConfig("MAIL_PASSWORD", "mail.password");

    private static String obtenerConfig(String envKey, String propKey) {
        //Variable de entorno (Clever Cloud)
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal;
        }
        // config.properties (Local en Eclipse)
        try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                return props.getProperty(propKey);
            }
        } catch (Exception e) {
            AppLogger.error("Error leyendo config.properties para la clave: " + propKey, e);
        }
        return null;
    }
    
    public static void enviarCorreo(String destinatario, String asunto, String cuerpo) throws MessagingException {
        // Configuraciones para el servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Autenticación de la sesión
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CORREO_REMITENTE, PASSWORD_APP);
            }
        });

        // Armado del paquete (El correo en sí)
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(CORREO_REMITENTE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setText(cuerpo);

        Transport.send(message);
    }
}
