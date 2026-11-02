package estacionamiento.service;

import java.util.ResourceBundle;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    
	private static final ResourceBundle config = ResourceBundle.getBundle("config");
	
    private static final String CORREO_REMITENTE = config.getString("mail.remitente");
    private static final String PASSWORD_APP = config.getString("mail.password");

    public static void enviarCorreo(String destinatario, String asunto, String cuerpo) throws MessagingException {
        // Configuraciones para el servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "58B7");
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

        // Disparo a la red
        System.out.println("Mail leído: [" + CORREO_REMITENTE + "] - Clave leída: [" + PASSWORD_APP + "]");
        Transport.send(message);
    }
}
