package estacionamiento.service;

import estacionamiento.domain.Usuario;
import estacionamiento.repository.UsuarioRepository;

public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	} 
	
	public void registrarUsuario(Usuario nuevoUsuario) {
		// Validar que el objeto no sea nulo
		if (nuevoUsuario == null) {
            throw new IllegalArgumentException("No se puede registrar un usuario nulo.");
        }

        // Validar datos personales obligatorios
        if (esNuloOBlanco(nuevoUsuario.getNombre()) || esNuloOBlanco(nuevoUsuario.getApellido())) {
            throw new IllegalArgumentException("El nombre y el apellido son obligatorios.");
        }
        
        if (esNuloOBlanco(nuevoUsuario.getDireccion())) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        
        if (esNuloOBlanco(nuevoUsuario.getNumeroTelefono())) {
            throw new IllegalArgumentException("El número de teléfono es obligatorio.");
        }

        // Validar el correo electrónico principal
        if (esNuloOBlanco(nuevoUsuario.getMail()) || !nuevoUsuario.getMail().contains("@")) {
            throw new IllegalArgumentException("Se debe proporcionar un correo electrónico válido.");
        }
        
        // Validar el correo de recuperación (opcional, pero si viene, debe ser válido)
        if (nuevoUsuario.getMailRecuperacion() != null && !nuevoUsuario.getMailRecuperacion().trim().isEmpty()) {
        	if (!nuevoUsuario.getMailRecuperacion().contains("@")) {
        		throw new IllegalArgumentException("El correo de recuperación proporcionado no es válido.");
        	}
        }

        // Validar credenciales de acceso
        if (esNuloOBlanco(nuevoUsuario.getNombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        
        if (esNuloOBlanco(nuevoUsuario.getContrasenia()) || nuevoUsuario.getContrasenia().length() < 6) {
            throw new IllegalArgumentException("La contraseña es obligatoria y debe tener al menos 6 caracteres.");
        }

        // Si es un usuario nuevo, lógicamente no debería tener fecha de baja
        if (nuevoUsuario.getFechaBaja() != null) {
            throw new IllegalArgumentException("Un usuario nuevo no puede registrarse con una fecha de baja.");
        }

        // Si pasa todas las validaciones, delegamos al repositorio
        usuarioRepository.guardar(nuevoUsuario);
        
        System.out.println("Servicio: Usuario validado y procesado correctamente.");
	}
	
	// Método auxiliar privado para no repetir la lógica de validación de Strings vacíos
	private boolean esNuloOBlanco(String valor) {
		return valor == null || valor.trim().isEmpty();
	}
}
