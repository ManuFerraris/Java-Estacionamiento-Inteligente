package estacionamiento.service;

import java.time.LocalDate;
import java.util.List;
import estacionamiento.domain.AsignacionCochera;
import estacionamiento.domain.Cochera;
import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;
import estacionamiento.repository.AsignacionCocheraRepository;
import estacionamiento.repository.CocheraRepository;
import estacionamiento.repository.UsuarioRepository;

public class AsignacionCocheraService {
	
	private AsignacionCocheraRepository asignacionRepository;
    private UsuarioRepository usuarioRepository;
    private CocheraRepository cocheraRepository;
    
    public AsignacionCocheraService(AsignacionCocheraRepository asignacionRepository,
            UsuarioRepository usuarioRepository,
            CocheraRepository cocheraRepository) {
		this.asignacionRepository = asignacionRepository;
		this.usuarioRepository = usuarioRepository;
		this.cocheraRepository = cocheraRepository;
    }
    
    // Asigna un usuario con rol ENCARGADO a una cochera a partir de la fecha actual.
    public void asignarEncargadoACochera(Integer numeroUsuario, Integer codigoCochera) {
        Usuario usuario = usuarioRepository.buscarPorNumero(numeroUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario seleccionado no existe.");
        }
        
        if (usuario.getRol() != RolesUsuario.ENCARGADO) {
            throw new IllegalArgumentException("Solo se pueden asignar cocheras a usuarios con el rol ENCARGADO.");
        }

        Cochera cochera = cocheraRepository.buscarPorClave(codigoCochera);
        if (cochera == null) {
            throw new IllegalArgumentException("La cochera seleccionada no existe.");
        }

        AsignacionCochera asignacionExistente = asignacionRepository.buscarActivaPorUsuarioYCochera(numeroUsuario, codigoCochera);
        if (asignacionExistente != null) {
            throw new IllegalArgumentException("El encargado ya tiene una asignación activa en esta cochera.");
        }

        AsignacionCochera nuevaAsignacion = new AsignacionCochera(usuario, cochera, LocalDate.now());
        asignacionRepository.guardar(nuevaAsignacion);
    }
    
    // Da de baja lógica la asignación actual (fecha_hasta = hoy)
    public void finalizarAsignacion(Integer numeroUsuario, Integer codigoCochera) {
        AsignacionCochera activa = asignacionRepository.buscarActivaPorUsuarioYCochera(numeroUsuario, codigoCochera);
        if (activa == null) {
            throw new IllegalArgumentException("No se encontró una asignación activa para finalizar.");
        }
        
        activa.setFechaHasta(LocalDate.now());
        asignacionRepository.actualizar(activa);
    }
    
    // Devuelve las cocheras donde el encargado trabaja actualmente (para su panel operativo).
    public List<Cochera> obtenerCocherasDeEncargado(Integer numeroUsuario) {
        return asignacionRepository.obtenerCocherasActivasPorEncargado(numeroUsuario);
    }

    // Lista todas las asignaciones vigentes en el municipio (para la vista del Trabajador Municipal).
    public List<AsignacionCochera> listarAsignacionesActivas() {
        return asignacionRepository.obtenerTodasLasActivas();
    }
    
}
