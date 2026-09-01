package estacionamiento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import estacionamiento.domain.EstadoReserva;
import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;
import estacionamiento.domain.Lugar;

import estacionamiento.repository.ReservaRepository;
import estacionamiento.repository.LugarRepository;
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
 
    public ReservaService(
    		ReservaRepository reservaRepository, 
            LugarRepository lugarRepository,
            VehiculoRepository vehiculoRepository,
            UsuarioRepository usuarioRepository,
            TipoEstadiaRepository tipoEstadiaRepository,
            PrecioHistoricoTVRepository precioRepository,
            SuscripcionService suscripcionService) {
    	this.reservaRepository = reservaRepository;
        this.lugarRepository = lugarRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoEstadiaRepository = tipoEstadiaRepository;
        this.precioRepository = precioRepository;
        this.suscripcionService = suscripcionService;
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
            null, 
            EstadoReserva.PENDIENTE, 
            montoFinal, 
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
            reserva.setEstado(EstadoReserva.FINALIZADA);
            reserva.setFechaHastaReal(LocalDateTime.now());
        } else {
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
}