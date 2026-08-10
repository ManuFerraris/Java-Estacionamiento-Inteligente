package estacionamiento.service;

import java.util.List;

import estacionamiento.domain.Usuario;
import estacionamiento.repository.UsuarioRepository;

public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	} 
	
	public void registrarUsuario(Usuario nuevoUsuario) {
		if (nuevoUsuario == null) {
            throw new IllegalArgumentException("No se puede registrar un usuario nulo.");
        }
		if (nuevoUsuario.getNumero() <= 0) {
		    throw new IllegalArgumentException("El número de usuario debe ser mayor a cero.");
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
	
	public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.obtenerTodos();
    }
	public Usuario buscarUsuarioPorNumero(int numero) {
	    if (numero <= 0) {
	        throw new IllegalArgumentException("El número de usuario debe ser mayor a cero.");
	    }

	    Usuario usuario = usuarioRepository.buscarPorNumero(numero);

	    if (usuario == null) {
	        throw new IllegalArgumentException("No existe un usuario con el número " + numero + ".");
	    }

	    return usuario;
	}
	
	public void actualizarUsuario(int numero, Usuario nuevosDatos) {
	    buscarUsuarioPorNumero(numero);

	    if (nuevosDatos == null) {
	        throw new IllegalArgumentException("Los nuevos datos del usuario son obligatorios.");
	    }

	    if (esNuloOBlanco(nuevosDatos.getNombre()) || esNuloOBlanco(nuevosDatos.getApellido())) {
	        throw new IllegalArgumentException("El nombre y el apellido son obligatorios.");
	    }

	    if (esNuloOBlanco(nuevosDatos.getDireccion())) {
	        throw new IllegalArgumentException("La dirección es obligatoria.");
	    }

	    if (esNuloOBlanco(nuevosDatos.getNumeroTelefono())) {
	        throw new IllegalArgumentException("El número de teléfono es obligatorio.");
	    }

	    if (esNuloOBlanco(nuevosDatos.getMail()) || !nuevosDatos.getMail().contains("@")) {
	        throw new IllegalArgumentException("Se debe proporcionar un correo electrónico válido.");
	    }

	    if (nuevosDatos.getMailRecuperacion() != null
	            && !nuevosDatos.getMailRecuperacion().trim().isEmpty()
	            && !nuevosDatos.getMailRecuperacion().contains("@")) {
	        throw new IllegalArgumentException("El correo de recuperación proporcionado no es válido.");
	    }

	    if (esNuloOBlanco(nuevosDatos.getNombreUsuario())) {
	        throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
	    }

	    if (esNuloOBlanco(nuevosDatos.getContrasenia()) || nuevosDatos.getContrasenia().length() < 6) {
	        throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
	    }

	    usuarioRepository.actualizar(numero, nuevosDatos);
	}
	
	public void eliminarUsuario(int numero) {
	    buscarUsuarioPorNumero(numero);
	    usuarioRepository.eliminar(numero);
	}
	// Método auxiliar privado para no repetir la lógica de validación de Strings vacíos
	private boolean esNuloOBlanco(String valor) {
		return valor == null || valor.trim().isEmpty();
	}
}
