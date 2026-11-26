package estacionamiento.service;

import java.util.concurrent.CompletableFuture;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;
import estacionamiento.util.AppLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacionService {
	
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm 'hs'");

	private void enviarCorreoAsync(String correoDestino, String asunto, String cuerpo, String contexto) {
		
		if (correoDestino == null || correoDestino.trim().isEmpty()) {
	        AppLogger.error("Omitido envío de correo (" + contexto + "): destinatario vacío.", null);
	        return;
	    }
		CompletableFuture.runAsync(() -> {
	        try {
	            System.out.println("Enviando correo [" + contexto + "] a: " + correoDestino);
	            EmailUtil.enviarCorreo(correoDestino, asunto, cuerpo);
	        } catch (Throwable t) {
	        	Exception ex = (t instanceof Exception) ? (Exception) t : new Exception(t);
	        	AppLogger.error("Falló el envío de correo [" + contexto + "] a " + correoDestino, ex);
	        }
	    });
	}
	
    public void enviarConfirmacionReserva(Reserva reserva) {
    	
    	if(reserva == null) {
			throw new IllegalArgumentException("No se encontro la reserva para notificar");
		}
    	
    	String correoDestino = reserva.getUsuario().getMail();
        String nombre = reserva.getUsuario().getNombre();
        String patente = reserva.getId().getPatenteVehiculo();
        String direccion = reserva.getLugar().getCochera().getDireccion();

        LocalDateTime desde = reserva.getId().getFechaDesde();
        LocalDateTime hasta = reserva.getFechaHastaTentativa();

        String fechaIngresoStr = desde.format(FORMATO_FECHA);
        String fechaSalidaStr = hasta.format(FORMATO_FECHA);

        // 1. Armamos el Asunto
        String asunto = "Confirmación de Reserva - MR Estacionamiento (" + patente + ")";
        
        // 2. Armamos el Cuerpo Base
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Tu lugar ha sido reservado exitosamente.\n\n"
                + "Detalles de tu estadía:\n"
                + "- Cochera: " + direccion + "\n"
                + "- Vehículo: " + patente + "\n"
                + "- Ingreso: " + fechaIngresoStr + "\n"
                + "- Salida estimada: " + fechaSalidaStr + "\n\n";

        // 3. Validación Nocturna
        LocalDateTime siguienteMedianoche = desde.toLocalDate().plusDays(1).atStartOfDay();
        boolean tocaNoche = (desde.getHour() < 6) || !hasta.isBefore(siguienteMedianoche);

        if (tocaNoche) {
            cuerpo += "--- INSTRUCCIONES DE ACCESO NOCTURNO ---\n"
                    + "Tu reserva abarca el horario de guardia nocturna (00:00 a 06:00 hs).\n"
                    + "IMPORTANTE: Durante este horario, la terminal física puede encontrarse sin atención de operarios. "
                    + "Si al llegar la barrera está cerrada, deberás acercar tu código QR al lector automático "
                    + "para validar tu entrada o salida.\n\n"
                    + "Si tenés demoras y superás tu horario de salida durante la madrugada, el recargo se generará automáticamente "
                    + "y se facturará a través del sistema municipal de multas y patentes.\n\n";
        }

        cuerpo += "Gracias por usar la red de cocheras municipales.\n\n"
                + "Saludos,\n"
                + "Municipalidad de Rosario - Estacionamiento Inteligente";

        // 4. Disparo Asíncrono
        enviarCorreoAsync(correoDestino, asunto, cuerpo, "Confirmación de Reserva");
    }
    
    public void enviarAvisoIngreso(Usuario usuario, Vehiculo vehiculo, LocalDateTime fechaHora) {
    	
       	if(usuario == null) {
    		throw new IllegalArgumentException("No se encontro el usuario para notificar");
    	}
        	
        if(vehiculo == null) {
    		throw new IllegalArgumentException("No se encontro el vehiculo");
   		}
        
    	String correoDestino = usuario.getMail();
    	String fechaFormateada = fechaHora.format(FORMATO_FECHA);
        String asunto = "Seguridad MR Estacionamiento: Ingreso Registrado";
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te informamos que el vehículo con patente " + vehiculo.getPatente() + 
                " acaba de ingresar a nuestra cochera el " + fechaFormateada + ".\n\n" +
                "Si no fuiste vos, comunicate urgente con la administración.\n\n" +
                "Saludos,\n" + 
                "Municipalidad de Rosario - Estacionamiento Inteligente";

        enviarCorreoAsync(correoDestino, asunto, cuerpo, "Aviso de Ingreso");
    }

    public void enviarAvisoSalida(Usuario usuario, Vehiculo vehiculo, LocalDateTime fechaHora) {
    	
    	if(usuario == null) {
			throw new IllegalArgumentException("No se encontro el usuario para notificar");
		}
    	
    	if(vehiculo == null) {
			throw new IllegalArgumentException("No se encontro el vehiculo");
		}
    	
    	String correoDestino = usuario.getMail();
    	String fechaFormateada = fechaHora.format(FORMATO_FECHA);
        String asunto = "Seguridad MR Estacionamiento: Salida Registrada";
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te informamos que el vehículo con patente " + vehiculo.getPatente() + 
                " se ha retirado de la cochera el " + fechaFormateada + ".\n\n" +
                "¡Gracias por elegirnos!";

        enviarCorreoAsync(correoDestino, asunto, cuerpo, "Aviso de Salida");
    }

    public void enviarRecordatorioEstadia(Usuario usuario, Reserva reserva) {
		
    	if(reserva == null) {
			throw new IllegalArgumentException("No se encontro la reserva para notificar");
		}
		
		if(usuario == null) {
			throw new IllegalArgumentException("No se encontro el usuario para notificar");
		}
		
		String correoDestino = usuario.getMail();
        String asunto = "Aviso MR Estacionamiento: Tu tiempo de estadía está por finalizar";
        
        String horaVencimiento = reserva.getFechaHastaTentativa().format(DateTimeFormatter.ofPattern("HH:mm"));
        String patente = reserva.getId().getPatenteVehiculo();

        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te recordamos que el tiempo prepagado para tu vehículo patente " + patente + 
                " finaliza a las " + horaVencimiento + " hs.\n\n" +
                "Si necesitás quedarte más tiempo, recordá que podés extender tu estadía ingresando a la sección 'Mis Reservas' en nuestra aplicación para evitar recargos en la barrera de salida.\n\n" +
                "¡Gracias por elegirnos!";

        enviarCorreoAsync(correoDestino, asunto, cuerpo, "Recordatorio de Estadia");
	}

	public void enviarAlertaTolerancia(Usuario usuario, Reserva reserva) {
		
		if(reserva == null) {
			throw new IllegalArgumentException("No se encontro la reserva para notificar");
		}
		
		if(usuario == null) {
			throw new IllegalArgumentException("No se encontro el usuario para notificar");
		}
		
		String correoDestino = usuario.getMail();
        String asunto = "Alerta MR Estacionamiento: Tu reserva está por expirar";
        
        String horaDesde = reserva.getId().getFechaDesde().format(DateTimeFormatter.ofPattern("HH:mm"));
        String patente = reserva.getId().getPatenteVehiculo();
        
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Notamos que aún no ingresaste al predio con tu vehículo patente " + patente + ".\n\n" +
                "Tu horario de reserva inició a las " + horaDesde + " hs. Te avisamos que el plazo máximo de tolerancia está a punto de agotarse (restan 5 minutos).\n\n" +
                "Si no ingresás o no extendés tu reserva desde la app antes de que finalice el tiempo, el sistema la cancelará automáticamente y se liberará el lugar.\n\n" +
                "¡Te estamos esperando!";

        enviarCorreoAsync(correoDestino, asunto, cuerpo, "Alerta de tolerancia");
	}
	
	public void enviarNotificacionCancelacion(Reserva reserva) {
		
		if(reserva == null) {
			throw new IllegalArgumentException("No se encontro la reserva para notificar");
		}

		String emailDestino = reserva.getUsuario().getMail();
		String nombreUsuario = reserva.getUsuario().getNombre();
		String apellidoUsuario = reserva.getUsuario().getApellido();
		
		String fechaFormateada = reserva.getId().getFechaDesde().format(FORMATO_FECHA);
		String patente = reserva.getId().getPatenteVehiculo();
		
		String asunto = "Confirmación de Cancelación de Reserva - " + patente; 
	    String cuerpo = "Hola " + nombreUsuario + " " + apellidoUsuario + ",\n\n"
	            + "Te confirmamos que tu reserva para el vehículo " + patente 
	            + " programada para el " + fechaFormateada + " ha sido CANCELADA a tu solicitud.\n\n"
	            + "El lugar asignado ya fue liberado para otro ciudadano. "
	            + "Recordá que, por políticas de la Municipalidad, la seña abonada no es reembolsable.\n\n"
	            + "Saludos,\n"
	            + "Municipalidad de Rosario - Estacionamiento Inteligente";
	   
	    enviarCorreoAsync(emailDestino, asunto, cuerpo, "Notificar Cancelacion");
	}
	
    public void notificarCancelacionPorFaltaDePago(Reserva reserva) {
    	
    	if(reserva == null) {
			throw new IllegalArgumentException("No se encontro la reserva para notificar");
		}
    	
        String correoDestino = reserva.getUsuario().getMail();
        String fechaFormateada = reserva.getId().getFechaDesde().format(FORMATO_FECHA);
        String patente = reserva.getId().getPatenteVehiculo();
        String nombre = reserva.getUsuario().getNombre();
        String apellido = reserva.getUsuario().getApellido();
        
        String asunto = "Aviso Importante: Reserva Cancelada por Falta de Pago";
        String cuerpo = "Hola " + nombre + " " + apellido + ",\n\n" +
                "Te informamos que tu reserva para el vehículo " + patente + " " +
                "programada para el " + 
                fechaFormateada + 
                " ha sido CANCELADA.\n\n" +
                "Motivo: El saldo total no fue abonado dentro de la ventana de tiempo permitida " +
                "(hasta 60 minutos antes del ingreso).\n\n" +
                "Por políticas de la Municipalidad, la seña abonada no es reembolsable. " +
                "El lugar ha sido liberado para otro ciudadano.\n\n" +
                "Saludos,\n" +
                "Municipalidad de Rosario - Estacionamiento Inteligente";
        
        enviarCorreoAsync(correoDestino, asunto, cuerpo, "Notificar Cancelacion Por falta de Pago");
    }
}
