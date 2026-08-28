package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoSuscripcion;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.claves.SuscripcionId;
import estacionamiento.domain.TipoPlan;
import estacionamiento.domain.Usuario;
import estacionamiento.repository.SuscripcionRepository;
import estacionamiento.repository.TipoPlanRepository;
import estacionamiento.repository.UsuarioRepository;

public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoPlanRepository tipoPlanRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository, UsuarioRepository usuarioRepository, TipoPlanRepository tipoPlanRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarOActualizarSuscripcion(int numeroUsuario, int codigoPlan) {
        Usuario usuario = usuarioRepository.buscarPorNumero(numeroUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario seleccionado no existe.");
        }

        TipoPlan plan = tipoPlanRepository.buscarPorClave(codigoPlan);
        if (plan == null) {
            throw new IllegalArgumentException("El tipo de plan seleccionado no existe.");
        }

        // 1. Buscamos si el usuario ya tiene una suscripción activa
        Suscripcion activaActual = suscripcionRepository.buscarActivaPorUsuario(numeroUsuario);
        LocalDateTime ahora = LocalDateTime.now();

        // 2. Si ya tiene una activa, la "pisamos" (Upgrade/Downgrade)
        if (activaActual != null) {
            if (activaActual.getTipoPlan().getCodigo() == codigoPlan) {
                throw new IllegalArgumentException("El usuario ya posee este mismo plan activo.");
            }
            // Cancelamos la vieja y le cortamos la fechaHasta al momento exacto de ahora
            activaActual.setEstado(EstadoSuscripcion.CANCELADA);
            activaActual.setFechaHasta(ahora);
            suscripcionRepository.actualizar(activaActual);
        }

        // 3. Creamos la nueva suscripción
        Suscripcion nuevaSuscripcion = new Suscripcion(plan, usuario, ahora, ahora.plusDays(30));
        nuevaSuscripcion.setEstado(EstadoSuscripcion.ACTIVA);

        // 4. Guardamos
        suscripcionRepository.guardar(nuevaSuscripcion);
    }

    public void cancelarSuscripcionManual(int numeroUsuario, int codigoPlan, LocalDateTime fechaDesde) {
        SuscripcionId id = new SuscripcionId(numeroUsuario, codigoPlan, fechaDesde);
        Suscripcion suscripcion = suscripcionRepository.buscarPorClave(id);

        if (suscripcion == null) {
            throw new IllegalArgumentException("La suscripción no existe.");
        }
        
        if (suscripcion.getEstado() == EstadoSuscripcion.CANCELADA) {
            throw new IllegalArgumentException("Esta suscripción ya se encuentra cancelada.");
        }

        suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
        suscripcion.setFechaHasta(LocalDateTime.now()); // Cortamos la vigencia hoy
        
        suscripcionRepository.actualizar(suscripcion);
    }

    public List<Suscripcion> obtenerTodas() {
        return suscripcionRepository.obtenerTodas();
    }
}