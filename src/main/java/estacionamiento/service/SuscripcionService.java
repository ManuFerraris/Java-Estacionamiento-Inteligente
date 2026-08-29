package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.EstadoSuscripcion;
import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.TipoPago;
import estacionamiento.domain.claves.SuscripcionId;
import estacionamiento.domain.TipoPlan;
import estacionamiento.domain.Usuario;
import estacionamiento.repository.PagoSuscripcionRepository;
import estacionamiento.repository.PrecioHistoricoTPRepository;
import estacionamiento.repository.SuscripcionRepository;
import estacionamiento.repository.TipoPlanRepository;
import estacionamiento.repository.UsuarioRepository;

public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoPlanRepository tipoPlanRepository;
    private final PrecioHistoricoTPRepository precioRepository;
    private final PagoSuscripcionRepository pagoRepository;

    public SuscripcionService(
    		SuscripcionRepository suscripcionRepository, 
    		UsuarioRepository usuarioRepository, 
    		TipoPlanRepository tipoPlanRepository,
    		PrecioHistoricoTPRepository precioRepository,
            PagoSuscripcionRepository pagoRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoPlanRepository = tipoPlanRepository;
        this.precioRepository = precioRepository;
        this.pagoRepository = pagoRepository;
    }

    public void registrarOActualizarSuscripcion(int numeroUsuario, int codigoPlan) {
        Usuario usuario = usuarioRepository.buscarPorNumero(numeroUsuario);
        if (usuario == null) throw new IllegalArgumentException("El usuario seleccionado no existe.");

        TipoPlan plan = tipoPlanRepository.buscarPorClave(codigoPlan);
        if (plan == null) throw new IllegalArgumentException("El tipo de plan seleccionado no existe.");

        // 1. Verificamos que el plan tenga un precio vigente antes de hacer cualquier cosa
        PrecioHistoricoTP precioVigente = precioRepository.obtenerPrecioVigente(codigoPlan);
        if (precioVigente == null) {
            throw new IllegalArgumentException("No se puede asignar el plan. Aún no tiene un precio configurado.");
        }

        Suscripcion activaActual = suscripcionRepository.buscarActivaPorUsuario(numeroUsuario);
        LocalDateTime ahora = LocalDateTime.now();

        // 2. Cancelamos la suscripción anterior si es un Upgrade
        if (activaActual != null) {
            if (activaActual.getTipoPlan().getCodigo() == codigoPlan) {
                throw new IllegalArgumentException("El usuario ya posee este mismo plan activo.");
            }
            activaActual.setEstado(EstadoSuscripcion.CANCELADA);
            activaActual.setFechaHasta(ahora);
            suscripcionRepository.actualizar(activaActual);
        }

        // 3. Creamos y guardamos la nueva suscripción
        Suscripcion nuevaSuscripcion = new Suscripcion(plan, usuario, ahora, ahora.plusDays(30));
        nuevaSuscripcion.setEstado(EstadoSuscripcion.ACTIVA);
        suscripcionRepository.guardar(nuevaSuscripcion);
        
        // 4. FACTURACIÓN AUTOMÁTICA
        System.out.println("\n--- DEBUG SERVICE: INICIO CREACIÓN DE PAGO ---");
        System.out.println("Monto del precio vigente: " + precioVigente.getPrecio());
        PagoSuscripcion comprobante = new PagoSuscripcion(
            nuevaSuscripcion, 
            ahora,                      // fechaEmision
            null,                       // fechaPago (Aún no pagó)
            precioVigente.getPrecio(),  // Monto extraído del historial vigente
            TipoPago.A_DEFINIR,                       // TipoPago nulo porque aún no va a la caja
            EstadoPago.PENDIENTE
        );
        
        System.out.println("Objeto Comprobante armado. ¿El tipoPago es nulo? " + (comprobante.getTipoPago() == null));
        System.out.println("Enviando al repositorio...");
        
        pagoRepository.guardar(comprobante);
        System.out.println("--- DEBUG SERVICE: FIN CREACIÓN DE PAGO ---\n");
        System.out.println("Servicio: Suscripción creada y comprobante PENDIENTE generado automáticamente.");
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