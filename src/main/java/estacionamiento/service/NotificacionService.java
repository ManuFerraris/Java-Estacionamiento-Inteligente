package estacionamiento.service;

import java.util.concurrent.CompletableFuture;

import estacionamiento.domain.Reserva;

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
}
