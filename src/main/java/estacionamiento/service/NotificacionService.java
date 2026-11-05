package estacionamiento.service;

import java.util.concurrent.CompletableFuture;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacionService {

    public void enviarConfirmacionReserva(Reserva reserva) {
        String correoDestino = reserva.getUsuario().getMail();
        String patente = reserva.getVehiculo().getPatente();
        String direccion = reserva.getLugar().getCochera().getDireccion();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaIngresoStr = reserva.getId().getFechaDesde().format(dtf);
        String fechaSalidaStr = reserva.getFechaHastaTentativa().format(dtf);

        // 1. Armamos el Asunto
        String asunto = "Confirmación de Reserva - MR Estacionamiento (" + patente + ")";

        // 2. Armamos el Cuerpo Base (Para todos)
        StringBuilder cuerpo = new StringBuilder();
        cuerpo.append("Hola ").append(reserva.getUsuario().getNombre()).append(",\n\n");
        cuerpo.append("Tu lugar ha sido reservado exitosamente.\n\n");
        cuerpo.append("Detalles de tu estadía:\n");
        cuerpo.append("- Cochera: ").append(direccion).append("\n");
        cuerpo.append("- Vehículo: ").append(patente).append("\n");
        cuerpo.append("- Ingreso: ").append(fechaIngresoStr).append("\n");
        cuerpo.append("- Salida estimada: ").append(fechaSalidaStr).append("\n\n");

        // 3. Validación Nocturna Mejorada
        int horaIngreso = reserva.getId().getFechaDesde().getHour();
        int horaSalida = reserva.getFechaHastaTentativa().getHour();
        
        // Detecta si empieza en la noche, termina en la noche, o si cruza la noche entera (ej: 22 a 08)
        boolean tocaNoche = (horaIngreso >= 0 && horaIngreso < 6) || 
                            (horaSalida >= 0 && horaSalida < 6) || 
                            (horaIngreso > 6 && horaSalida > horaIngreso && horaSalida >= 24) || // Si manejas salidas cruzando la medianoche
                            (horaIngreso > 18 && horaSalida >= 6); 

        if (tocaNoche) {
            cuerpo.append("--- INSTRUCCIONES DE ACCESO NOCTURNO ---\n");
            cuerpo.append("Tu reserva abarca el horario de guardia nocturna (00:00 a 06:00 hs).\n");
            cuerpo.append("IMPORTANTE: Durante este horario, la terminal física puede encontrarse sin atención de operarios. ");
            cuerpo.append("Si al llegar la barrera está cerrada, deberás acercar tu código QR al lector automático ");
            cuerpo.append("para validar tu entrada o salida.\n\n");
            cuerpo.append("Si tienes demoras y superas tu horario de salida durante la madrugada, el recargo se generará automáticamente ");
            cuerpo.append("y se facturará a través del sistema municipal de multas y patentes.\n\n");
        }

        cuerpo.append("Gracias por usar la red de cocheras municipales.\n");

        // 4. Disparo Asíncrono (Para no trabar el Tomcat ni hacer esperar al cliente)
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Enviando correo en segundo plano a: " + correoDestino);
                EmailUtil.enviarCorreo(correoDestino, asunto, cuerpo.toString());
            } catch (Exception e) {
                System.err.println("Error al enviar el email a " + correoDestino + ": " + e.getMessage());
            }
        });
    }
    
    public void enviarAvisoIngreso(Usuario usuario, Vehiculo vehiculo, LocalDateTime fechaHora) {
    	
    	String correoDestino = usuario.getMail();
        String asunto = "Seguridad MR Estacionamiento: Ingreso Registrado";
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te informamos que el vehículo con patente " + vehiculo.getPatente() + 
                " acaba de ingresar a nuestra cochera el " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".\n\n" +
                "Si no fuiste vos, comunicate urgente con la administración.";

        // Logica de envio de jakarta
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Enviando correo en segundo plano a: " + correoDestino);
                EmailUtil.enviarCorreo(correoDestino, asunto, cuerpo.toString());
            } catch (Exception e) {
                System.err.println("Error al enviar el email a " + correoDestino + ": " + e.getMessage());
            }
        });
        
        System.out.println("[EMAIL ENVIADO] " + asunto + " a " + usuario.getMail());
    }

    public void enviarAvisoSalida(Usuario usuario, Vehiculo vehiculo, LocalDateTime fechaHora) {
    	
    	String correoDestino = usuario.getMail();
        String asunto = "Seguridad MR Estacionamiento: Salida Registrada";
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te informamos que el vehículo con patente " + vehiculo.getPatente() + 
                " se ha retirado de la cochera el " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".\n\n" +
                "¡Gracias por elegirnos!";

        // Logica de envio de jakarta
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Enviando correo en segundo plano a: " + correoDestino);
                EmailUtil.enviarCorreo(correoDestino, asunto, cuerpo.toString());
            } catch (Exception e) {
                System.err.println("Error al enviar el email a " + correoDestino + ": " + e.getMessage());
            }
        });
        
        System.out.println("[EMAIL ENVIADO] " + asunto + " a " + usuario.getMail());
    }

    public void enviarRecordatorioEstadia(Usuario usuario, Reserva r) {
		String correoDestino = usuario.getMail();
        String asunto = "Aviso MR Estacionamiento: Tu tiempo de estadía está por finalizar";
        
        String horaVencimiento = r.getFechaHastaTentativa().format(DateTimeFormatter.ofPattern("HH:mm"));
        String patente = r.getVehiculo().getPatente();

        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te recordamos que el tiempo prepagado para tu vehículo patente " + patente + 
                " finaliza a las " + horaVencimiento + " hs.\n\n" +
                "Si necesitás quedarte más tiempo, recordá que podés extender tu estadía ingresando a la sección 'Mis Reservas' en nuestra aplicación para evitar recargos en la barrera de salida.\n\n" +
                "¡Gracias por elegirnos!";

        // Lógica de envío asíncrono
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Enviando correo en segundo plano a: " + correoDestino);
                EmailUtil.enviarCorreo(correoDestino, asunto, cuerpo);
            } catch (Exception e) {
                System.err.println("Error al enviar el recordatorio a " + correoDestino + ": " + e.getMessage());
            }
        });
	}

	public void enviarAlertaTolerancia(Usuario usuario, Reserva r) {
		String correoDestino = usuario.getMail();
        String asunto = "Alerta MR Estacionamiento: Tu reserva está por expirar";
        
        String horaDesde = r.getId().getFechaDesde().format(DateTimeFormatter.ofPattern("HH:mm"));
        String patente = r.getVehiculo().getPatente();
        
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Notamos que aún no ingresaste al predio con tu vehículo patente " + patente + ".\n\n" +
                "Tu horario de reserva inició a las " + horaDesde + " hs. Te avisamos que el plazo máximo de tolerancia está a punto de agotarse (restan 5 minutos).\n\n" +
                "Si no ingresás o no extendés tu reserva desde la app antes de que finalice el tiempo, el sistema la cancelará automáticamente y se liberará el lugar.\n\n" +
                "¡Te estamos esperando!";

        // Lógica de envío asíncrono
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Enviando alerta de tolerancia a: " + correoDestino);
                EmailUtil.enviarCorreo(correoDestino, asunto, cuerpo);
            } catch (Exception e) {
                System.err.println("Error al enviar alerta de tolerancia a " + correoDestino + ": " + e.getMessage());
            }
        });
	}
}
