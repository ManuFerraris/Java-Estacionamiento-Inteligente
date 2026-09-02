package estacionamiento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.EstadoReserva;
import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;
import estacionamiento.domain.Lugar;
import estacionamiento.domain.Pago;
import estacionamiento.repository.ReservaRepository;
import estacionamiento.repository.LugarRepository;
import estacionamiento.repository.PagoRepository;
import estacionamiento.repository.VehiculoRepository;
import estacionamiento.repository.UsuarioRepository;
import estacionamiento.repository.TipoEstadiaRepository;
import estacionamiento.repository.PrecioHistoricoTVRepository;

public class ReservaService {

	private final ReservaRepository reservaRepository;
    private final LugarRepository lugarRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoEstadiaRepository tipoEstadiaRepository;
    private final PrecioHistoricoTVRepository precioRepository;
    private final SuscripcionService suscripcionService;
    private final PagoRepository pagoRepository;
 
    public ReservaService(
    		ReservaRepository reservaRepository, 
            LugarRepository lugarRepository,
            VehiculoRepository vehiculoRepository,
            UsuarioRepository usuarioRepository,
            TipoEstadiaRepository tipoEstadiaRepository,
            PrecioHistoricoTVRepository precioRepository,
            SuscripcionService suscripcionService,
            PagoRepository pagoRepository) {
    	this.reservaRepository = reservaRepository;
        this.lugarRepository = lugarRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoEstadiaRepository = tipoEstadiaRepository;
        this.precioRepository = precioRepository;
        this.suscripcionService = suscripcionService;
        this.pagoRepository = pagoRepository;
    }
    
    public Reserva generarReserva(String patente, Integer numeroUsuario, Integer idCochera, Integer idTipoEstadia, LocalDateTime fechaDesde, LocalDateTime fechaHastaTentativa) {
        
        Lugar lugarDisponible = lugarRepository.obtenerPrimerLugarLibre(idCochera, fechaDesde, fechaHastaTentativa);
        if (lugarDisponible == null) {
            throw new IllegalArgumentException("No hay cupo disponible en la cochera para ese rango horario.");
        }

        Vehiculo vehiculo = vehiculoRepository.buscarPorPatente(patente);
        if (vehiculo == null) {
        	throw new IllegalArgumentException( "El vehículo no se encuentra registrado.");
        }

        Usuario usuario = usuarioRepository.buscarPorNumero(numeroUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe.");
        }

        TipoEstadia tipoEstadia = tipoEstadiaRepository.buscarPorNumero(idTipoEstadia);
        if (tipoEstadia == null) {
            throw new IllegalArgumentException("El tipo de estadía seleccionado no es válido.");
        }

        BigDecimal montoFinal = calcularMontoConBeneficios(numeroUsuario, vehiculo);

        ReservaId id = new ReservaId(patente, numeroUsuario, idTipoEstadia, fechaDesde);

        Reserva nuevaReserva = new Reserva(
            vehiculo, 
            usuario, 
            tipoEstadia, 
            fechaHastaTentativa, 
            null, // La fecha hastaTotal la asignamos cuando se retira el usuario fisicamente.
            EstadoReserva.PENDIENTE, 
            montoFinal, 
            null,
            null,
            lugarDisponible
        );
        
        nuevaReserva.setId(id);
        reservaRepository.guardar(nuevaReserva);
        
        return nuevaReserva;
    }

    public void registrarIngreso(ReservaId idReserva) {
        Reserva reserva = reservaRepository.buscarPorClave(idReserva);

        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no existe.");
        }

        if (reserva.getEstado() != EstadoReserva.PENDIENTE && reserva.getEstado() != EstadoReserva.SALIDA_PARCIAL) {
            throw new IllegalArgumentException("El vehículo no está habilitado para ingresar. Estado actual: " + reserva.getEstado());
        }

        // Validamos que no intente entrar 3 horas antes. 
        // Le damos un margen de 15 minutos de ingreso anticipado.
        LocalDateTime limiteIngresoTemprano = reserva.getId().getFechaDesde().minusMinutes(15);
        if (LocalDateTime.now().isBefore(limiteIngresoTemprano)) {
            throw new IllegalArgumentException("Es demasiado temprano para ingresar. Tu horario de reserva comienza a las " + reserva.getId().getFechaDesde().toLocalTime());
        }
        
        reserva.setEstado(EstadoReserva.EN_CURSO);
        reservaRepository.actualizar(reserva);
    }

    public void registrarSalida(ReservaId idReserva, boolean esSalidaDefinitiva) {
        Reserva reserva = reservaRepository.buscarPorClave(idReserva);

        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no existe.");
        }

        if (reserva.getEstado() != EstadoReserva.EN_CURSO) {
            throw new IllegalArgumentException("El vehículo no se encuentra físicamente en la cochera.");
        }

        if (reserva.getTipoEstadia().getDescripcion().equalsIgnoreCase("Por Hora")) {
            esSalidaDefinitiva = true;
        }

        if (esSalidaDefinitiva) {

            reserva.setFechaHastaReal(LocalDateTime.now());
            
            //Calculo de tiempo extra y los costos que corresponden (debemos debatirlo)
            Duration duracion = Duration.between(reserva.getFechaHastaTentativa(), reserva.getFechaHastaReal());
            long minutosExtra = duracion.toMinutes();

            if (minutosExtra > 10) {
            	// Buscamos cuánto cuesta la hora para este tipo de vehículo hoy
                BigDecimal tarifaHoraBase = precioRepository.obtenerPrecioVigente(reserva.getVehiculo().getTipoVehiculo().getNumero());
                BigDecimal recargo = BigDecimal.ZERO;
                if (minutosExtra < 30) {
                	// Fracción de media hora (50% del valor de la hora)
                    recargo = tarifaHoraBase.multiply(new BigDecimal("0.5"));
                } else {
                	// Hora completa iniciada
                    long horasACobrar = (long) Math.ceil(minutosExtra / 60.0); 
                    recargo = tarifaHoraBase.multiply(new BigDecimal(horasACobrar));
                }
                // Generamos la deuda (El objeto Pago queda PENDIENTE)
                Pago deudaExtra = new Pago();
                deudaExtra.setMonto(recargo);
                deudaExtra.setFechaHora(LocalDateTime.now());
                deudaExtra.setEstado(EstadoPago.PENDIENTE);
                
                pagoRepository.guardar(deudaExtra); // Guardamos el comprobante en nuestra BD
                
                reserva.setPagoSaldo(deudaExtra); // Lo enlazamos a la reserva
                
                // OJO: NO pasamos a FINALIZADA todavía. La barrera sigue baja hasta que pague.
                // Podríamos crear un estado "PENDIENTE_PAGO_SALDO" o dejarla EN_CURSO con el saldo cargado.
            }else {
            	// Salió a tiempo, se va sin deudas
                reserva.setEstado(EstadoReserva.FINALIZADA);
            }
        }else {
        	// Es solo una salida parcial
            reserva.setEstado(EstadoReserva.SALIDA_PARCIAL);
        }
        reservaRepository.actualizar(reserva);
    }

    private BigDecimal calcularMontoConBeneficios(Integer numeroUsuario, Vehiculo vehiculo) {
        BigDecimal precioBase = precioRepository.obtenerPrecioVigente(vehiculo.getTipoVehiculo().getNumero());
        
        if (precioBase == null) {
            throw new IllegalArgumentException("No hay un cuadro tarifario vigente para este tipo de vehículo.");
        }

        Suscripcion suscripcionActiva = suscripcionService.obtenerActivaPorUsuario(numeroUsuario);

        if (suscripcionActiva != null) {
            String plan = suscripcionActiva.getTipoPlan().getNombre().toUpperCase();
            
            if (plan.equals("PREMIUM")) {
                return BigDecimal.ZERO; 
            } else if (plan.equals("MEDIUM")) {
                return precioBase.multiply(new BigDecimal("0.80"));
            }
        }

        return precioBase;
    }
    
    // Tarea rutinaria: Cancela las reservas impagas a punto de expirar.
    public void cancelarReservasNoPagadasPorVencer() {
        // Calculamos el límite: Ahora + 60 minutos
        LocalDateTime limiteTolerancia = LocalDateTime.now().plusMinutes(60);
        
        List<Reserva> reservasEnRiesgo = reservaRepository.buscarReservasPendientesSinPagoCompleto(limiteTolerancia);
        
        for (Reserva reserva : reservasEnRiesgo) {
            reserva.setEstado(EstadoReserva.CANCELADA);
            try {
                reservaRepository.actualizar(reserva);
                notificarCancelacion(reserva.getUsuario(), reserva);
            } catch (Exception e) {
                System.err.println("Error al cancelar la reserva de patente: " + reserva.getVehiculo().getPatente());
                e.printStackTrace();
            }
        }
    }
    
    // Módulo de envío de correos (Es un mock para futura integración con Jakarta Mail)
    // Ahora esta hardcodeado
    private void notificarCancelacion(Usuario usuario, Reserva reserva) {
        String correoDestino = usuario.getMail();
        String patente = reserva.getVehiculo().getPatente();
        
        // Aca iria la configuración de JavaMailSender
        String asunto = "Aviso Importante: Reserva Cancelada por Falta de Pago";
        String cuerpo = "Hola " + usuario.getNombre() + ",\n\n" +
                "Te informamos que tu reserva para el vehículo " + patente + " " +
                "programada para el " + reserva.getId().getFechaDesde() + " ha sido CANCELADA.\n\n" +
                "Motivo: El saldo total no fue abonado dentro de la ventana de tiempo permitida " +
                "(hasta 60 minutos antes del ingreso).\n\n" +
                "Por políticas de la Municipalidad, la seña abonada no es reembolsable. " +
                "El lugar ha sido liberado para otro ciudadano.";
                
        // Simulación de salida del correo
        System.out.println("=========================================");
        System.out.println("ENVIANDO CORREO A: " + correoDestino);
        System.out.println("ASUNTO: " + asunto);
        System.out.println("MENSAJE:\n" + cuerpo);
        System.out.println("=========================================");
    }
    
    // Metodo para las reservas que son entre las 00:00hs y 06:00hs porque no hay trabajadores.
    private void procesarLogisticaNocturna(Reserva reserva) {
        int horaIngreso = reserva.getId().getFechaDesde().getHour();
        int horaSalida = reserva.getFechaHastaTentativa().getHour();

        // Rango nocturno: 00:00 a 06:00
        boolean ingresoNocturno = (horaIngreso >= 0 && horaIngreso < 6);
        boolean salidaNocturna = (horaSalida >= 0 && horaSalida < 6);

        if (ingresoNocturno || salidaNocturna) {
            String correoDestino = reserva.getUsuario().getMail();
            
            String asunto = "Instrucciones de Acceso Nocturno - Reserva " + reserva.getVehiculo().getPatente();
            String cuerpo = "Hola " + reserva.getUsuario().getNombre() + ".\n\n" +
                    "Tu reserva abarca el horario de guardia nocturna (00:00 a 06:00 hs) en la cochera " + 
                    reserva.getLugar().getCochera().getDireccion() + ".\n\n" +
                    "IMPORTANTE: Durante este horario, la terminal física puede encontrarse sin atención de operarios. " +
                    "Si al llegar la barrera está cerrada, deberás ingresar el siguiente PIN de acceso en el teclado numérico " +
                    "o presentar tu código QR en el lector automático para validar tu entrada o salida.\n\n" +
                    "Si tienes demoras y superas tu horario de salida durante la madrugada, el recargo se generará automáticamente " +
                    "en tu perfil y se facturará a través del sistema municipal de multas y patentes.\n\n" +
                    "Gracias por usar MR Estacionamiento.";

            System.out.println("Disparando correo nocturno asíncrono a: " + correoDestino);
            // Aca llamamos a la clase utilitaria de JavaMail (despues veo como se hace).
        }
    }

}
