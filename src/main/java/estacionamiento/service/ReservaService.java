package estacionamiento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;

import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.EstadoReserva;
import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.dto.SalidaDTO;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.TipoPago;
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
    private NotificacionService notificacionService = new NotificacionService();
 
    public ReservaService(
    		ReservaRepository reservaRepository, 
            LugarRepository lugarRepository,
            VehiculoRepository vehiculoRepository,
            UsuarioRepository usuarioRepository,
            TipoEstadiaRepository tipoEstadiaRepository,
            PrecioHistoricoTVRepository precioRepository,
            SuscripcionService suscripcionService,
            PagoRepository pagoRepository,
            NotificacionService notificacionService) {
    	this.reservaRepository = reservaRepository;
        this.lugarRepository = lugarRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoEstadiaRepository = tipoEstadiaRepository;
        this.precioRepository = precioRepository;
        this.suscripcionService = suscripcionService;
        this.pagoRepository = pagoRepository;
        this.notificacionService = new NotificacionService();
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
        
        // Envio del mail con EmailUtil
        notificacionService.enviarConfirmacionReserva(nuevaReserva);
        
        return nuevaReserva;
    }

    public void actualizarReserva(Reserva reservaActualizada) {
        if (reservaActualizada == null || reservaActualizada.getId() == null) {
            throw new IllegalArgumentException("La reserva o su identificador principal no pueden ser nulos.");
        }
        
        // Regla de negocio de protección:
        if (reservaActualizada.getEstado() == EstadoReserva.CANCELADA || 
            reservaActualizada.getEstado() == EstadoReserva.FINALIZADA) {
            throw new IllegalArgumentException("No se pueden alterar los datos de una reserva inactiva o finalizada.");
        }

        reservaRepository.actualizar(reservaActualizada);
        System.out.println("Servicio: Reserva actualizada para la patente " + reservaActualizada.getVehiculo().getPatente());
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
        if (LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).isBefore(limiteIngresoTemprano)) {
            throw new IllegalArgumentException("Es demasiado temprano para ingresar. Tu horario de reserva comienza a las " + reserva.getId().getFechaDesde().toLocalTime());
        }
        
        // Validamos que la reserva no esté vencida
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        if (ahora.isAfter(reserva.getFechaHastaTentativa())) {
            // Podrías incluso pasar el estado a CANCELADA acá si quisieras
            throw new IllegalArgumentException("Operación denegada. La reserva se encuentra vencida desde las " + reserva.getFechaHastaTentativa().toLocalTime());
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

            reserva.setFechaHastaReal(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            
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
                deudaExtra.setFechaHora(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
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
        LocalDateTime limiteTolerancia = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusMinutes(60);
        
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

    public SalidaDTO procesarIntentoSalida(ReservaId idReserva, boolean esSalidaDefinitiva) {
        
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

        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        //LocalDateTime limiteSalida = reserva.getFechaHastaTentativa().plusMinutes(10);
        
        if (esSalidaDefinitiva) {
            long minutosExtra = java.time.Duration.between(reserva.getFechaHastaTentativa(), ahora).toMinutes();

            if (minutosExtra > 10) {
                // Si la deuda no fue generada antes, la calculamos y guardamos
                if (reserva.getPagoSaldo() == null || reserva.getPagoSaldo().getEstado() != EstadoPago.PENDIENTE) {
                    BigDecimal tarifaHoraBase = precioRepository.obtenerPrecioVigente(reserva.getVehiculo().getTipoVehiculo().getNumero());
                    BigDecimal recargo = calcularMontoExcedente(minutosExtra, tarifaHoraBase);
                    
                    Pago deudaExtra = new Pago();
                    deudaExtra.setMonto(recargo);
                    deudaExtra.setFechaHora(ahora);
                    deudaExtra.setEstado(EstadoPago.PENDIENTE);
                    // Pienso dejarlo en efectivo pero hay que ver como hacemos para
                    // que acepte transferencias y MP aca tambien. Por ahora sirve.
                    deudaExtra.setTipoPago(TipoPago.EFECTIVO);
                    
                    pagoRepository.guardar(deudaExtra);
                    reserva.setPagoSaldo(deudaExtra);
                    reservaRepository.actualizar(reserva);
                }
                // Sin que salga le damos la deuda (no subimos la barrera en la vida real)
                return new SalidaDTO(false, reserva.getPagoSaldo().getMonto(), "Tiempo de cortesía excedido. Debe abonar saldo para salir.");
            } else {
                // Todo en orden entonces se va
                confirmarSalidaFisica(reserva, ahora, true);
                return new SalidaDTO(true, BigDecimal.ZERO, "Salida registrada exitosamente. ¡Buen viaje!");
            }
        } else {
            // Era una salida parcial
            confirmarSalidaFisica(reserva, ahora, false);
            return new SalidaDTO(true, BigDecimal.ZERO, "Salida parcial registrada. Recuerde regresar.");
        }
    }

    // MÉTODO AUXILIAR: Calcula el costo de lo excedido
    private BigDecimal calcularMontoExcedente(long minutosExtra, BigDecimal tarifaHoraBase) {
        if (minutosExtra < 30) {
            return tarifaHoraBase.multiply(new BigDecimal("0.5"));
        } else {
            long horasACobrar = (long) Math.ceil(minutosExtra / 60.0);
            return tarifaHoraBase.multiply(new BigDecimal(horasACobrar));
        }
    }

    // MÉTODO AUXILIAR: Guardamos en la base de datos cuando la barrera se abre
    private void confirmarSalidaFisica(Reserva reserva, LocalDateTime ahora, boolean esDefinitiva) {
        if (esDefinitiva) {
            reserva.setFechaHastaReal(ahora);
            reserva.setEstado(EstadoReserva.FINALIZADA);
        } else {
            reserva.setEstado(EstadoReserva.SALIDA_PARCIAL);
        }
        reservaRepository.actualizar(reserva);
    }

    // NUEVO MÉTODO: El empleado confirma que recibió la plata
    public SalidaDTO cobrarYLiberarBarrera(ReservaId idReserva) {
        Reserva reserva = reservaRepository.buscarPorClave(idReserva);
        
        if (reserva == null || reserva.getPagoSaldo() == null) {
            throw new IllegalArgumentException("No hay deuda pendiente para esta reserva.");
        }

        Pago deuda = reserva.getPagoSaldo();
        deuda.setEstado(EstadoPago.APROBADO);
        deuda.setFechaHora(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        pagoRepository.actualizar(deuda);

        confirmarSalidaFisica(reserva, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")), true);
        
        return new SalidaDTO(true, BigDecimal.ZERO, "Pago registrado correctamente. Barrera abierta.");
    }

    public List<Reserva> obtenerReservasPorUsuario(Integer idUsuario) {
        // 1. Traemos SOLO las reservas de este cliente
        List<Reserva> misReservas = reservaRepository.buscarPorUsuario(idUsuario);
        
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        
        for (Reserva r : misReservas) {
            
            if (r.getEstado() == EstadoReserva.PENDIENTE && ahora.isAfter(r.getFechaHastaTentativa())) {
                r.setEstado(EstadoReserva.NO_PRESENTADO); // Se le pasó la hora
                reservaRepository.actualizar(r);
            }
            
            if (r.getEstado() == EstadoReserva.SALIDA_PARCIAL && ahora.isAfter(r.getFechaHastaTentativa())) {
                r.setEstado(EstadoReserva.FINALIZADA); // Salió, se le venció y nunca volvió (el rata)
                reservaRepository.actualizar(r);
            }
        }
        
        
        return misReservas;
    }
}
