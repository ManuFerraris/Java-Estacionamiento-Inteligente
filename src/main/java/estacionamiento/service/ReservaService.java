package estacionamiento.service;

import estacionamiento.domain.Reserva;
import estacionamiento.repository.ReservaRepository;
import estacionamiento.repository.VehiculoRepository;
import estacionamiento.repository.UsuarioRepository;
import estacionamiento.repository.TipoEstadiaRepository;

import java.time.LocalDateTime;

public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoEstadiaRepository tipoEstadiaRepository;

    // Inyectamos todas las dependencias necesarias
    public ReservaService(ReservaRepository reservaRepository, VehiculoRepository vehiculoRepository,
                          UsuarioRepository usuarioRepository, TipoEstadiaRepository tipoEstadiaRepository) {
        this.reservaRepository = reservaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoEstadiaRepository = tipoEstadiaRepository;
    }

    // REGLAS DE NEGOCIO (CASOS DE USO)
    // ================================
    
    public void registrarNuevaReserva(Reserva nuevaReserva) {
        // Regla 1: Verificar que el vehículo exista
        if (vehiculoRepository.buscarPorPatente(nuevaReserva.getVehiculo().getPatente()) == null) {
            throw new IllegalArgumentException("No se puede reservar: El vehículo no está registrado en el sistema.");
        }

        // Regla 2: Verificar que el usuario exista
        if (usuarioRepository.buscarPorNumero(nuevaReserva.getUsuario().getNumero()) == null) {
            throw new IllegalArgumentException("No se puede reservar: El usuario no existe.");
        }

        // Regla 3: Verificar que el Tipo de Estadía exista
        if (tipoEstadiaRepository.buscarPorNumero(nuevaReserva.getTipoEstadia().getNumero()) == null) {
            throw new IllegalArgumentException("No se puede reservar: El tipo de estadía no es válido.");
        }

        // Regla 4: Validar que la fecha no sea en el pasado (damos 1 minuto de margen por el tiempo de ejecución)
        if (nuevaReserva.getFechaDesde().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new IllegalArgumentException("La reserva no puede iniciar en el pasado.");
        }

        // Regla 5: Configurar la tolerancia de 20 minutos
        LocalDateTime limiteTolerancia = nuevaReserva.getFechaDesde().plusMinutes(20);
        nuevaReserva.setFechaHastaTentativa(limiteTolerancia);

        // Si sobrevive a todas las validaciones, el Servicio autoriza al Repositorio a guardar
        reservaRepository.guardar(nuevaReserva);
        
        System.out.println("Reserva validada y registrada vía Servicio. Límite de llegada: " + limiteTolerancia);
    }
}