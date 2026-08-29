package estacionamiento.service;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.time.LocalDate;

import estacionamiento.domain.RolesUsuario;
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
        // ENCRIPTACIÓN BCRYPT
        // Genera un "salt" aleatorio y hashea la contraseña plana
        String hashContrasenia = BCrypt.hashpw(nuevoUsuario.getContrasenia(), BCrypt.gensalt());
        nuevoUsuario.setContrasenia(hashContrasenia);

        // Validar el Rol en el Alta 
        if (nuevoUsuario.getRol() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio para el registro.");
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
	
	public List<Usuario> obtenerTodos() {
		return usuarioRepository.obtenerTodos();
	}
	
	public Usuario buscarPorNumero(Integer numero) {
		if (numero <= 0) {
            throw new IllegalArgumentException("El numero de búsqueda debe ser válido.");
        }
		Usuario usuario = usuarioRepository.buscarPorNumero(numero);
		if(usuario == null) {
			throw new IllegalArgumentException("No se encontró ningun usuario con el numero " + numero + ".");
		}
		return usuario;
	}
	
	public void darDeBaja(Integer numero) throws Exception {
		Usuario usuario = buscarPorNumero(numero);
		if(usuario != null) {
			usuario.setFechaBaja(LocalDate.now());
			usuarioRepository.actualizar(numero, usuario);
		}else {
			throw new Exception("El usuario a dar de baja no existe.");
		}
	}
	
	public void darDeAltaPostBaja(Integer numero) throws Exception {
		Usuario usuario = buscarPorNumero(numero);
		if(usuario != null) {
			usuario.setFechaBaja(null);
			usuarioRepository.actualizar(numero, usuario);
		}else {
			throw new Exception("El usuario a volver a dar de alta no existe.");
		}
	}
	
	public void actualizar(Integer numero, Usuario usuarioAActualizar) {
	    if(usuarioAActualizar == null) {
	        throw new IllegalArgumentException("El usuario a actualizar no puede ser nulo.");
	    }
	    
	    Usuario usuarioExistente = buscarPorNumero(numero);
	    
	    usuarioAActualizar.setFechaBaja(usuarioExistente.getFechaBaja());
	    
	    if (esNuloOBlanco(usuarioAActualizar.getContrasenia())) {
	        usuarioAActualizar.setContrasenia(usuarioExistente.getContrasenia());
	    } else if (usuarioAActualizar.getContrasenia().length() < 6) {
	        throw new IllegalArgumentException("Si desea cambiar la contraseña, debe tener al menos 6 caracteres.");
	    } else {
	        // ENCRIPTACIÓN DE NUEVA CLAVE 
	        String nuevoHash = BCrypt.hashpw(usuarioAActualizar.getContrasenia(), BCrypt.gensalt());
	        usuarioAActualizar.setContrasenia(nuevoHash);
	    }
	    
	    if (usuarioAActualizar.getRol() == null) {
	        // Mantenemos el que ya tenía si vino vacio.
	        usuarioAActualizar.setRol(usuarioExistente.getRol());
	    }

	    if (esNuloOBlanco(usuarioAActualizar.getNombre()) || esNuloOBlanco(usuarioAActualizar.getApellido())) {
	        throw new IllegalArgumentException("El nombre y el apellido son obligatorios.");
	    }
	    if (esNuloOBlanco(usuarioAActualizar.getDireccion())) {
	        throw new IllegalArgumentException("La dirección es obligatoria.");
	    }
	    if (esNuloOBlanco(usuarioAActualizar.getNumeroTelefono())) {
	        throw new IllegalArgumentException("El número de teléfono es obligatorio.");
	    }
	    if (esNuloOBlanco(usuarioAActualizar.getMail()) || !usuarioAActualizar.getMail().contains("@")) {
	        throw new IllegalArgumentException("Se debe proporcionar un correo electrónico válido.");
	    }
	    if (usuarioAActualizar.getMailRecuperacion() != null && !usuarioAActualizar.getMailRecuperacion().trim().isEmpty()) {
	        if (!usuarioAActualizar.getMailRecuperacion().contains("@")) {
	            throw new IllegalArgumentException("El correo de recuperación proporcionado no es válido.");
	        }
	    }
	    if (esNuloOBlanco(usuarioAActualizar.getNombreUsuario())) {
	        throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
	    }
	    
	    usuarioRepository.actualizar(numero, usuarioAActualizar);
	    System.out.println("Servicio: Usuario " + usuarioAActualizar.getNumero() + " validado y actualizado con éxito.");
	}

	// Siempre devolvemos el mensaje generico para evitar ataque de fuerza bruta.
	public Usuario autenticar(String nombreUsuario, String contraseniaPlana) {
        if (esNuloOBlanco(nombreUsuario) || esNuloOBlanco(contraseniaPlana)) {
            throw new IllegalArgumentException("Debe ingresar usuario y contraseña.");
        }

        Usuario usuario = usuarioRepository.buscarPorNombreUsuario(nombreUsuario);
        
        // Si el usuario no existe o la contraseña encriptada no coincide con la plana
        if (usuario == null || !BCrypt.checkpw(contraseniaPlana, usuario.getContrasenia())) {
            throw new IllegalArgumentException("Credenciales incorrectas o usuario inactivo.");
        }

        System.out.println("Servicio: Login exitoso para el usuario " + usuario.getNombreUsuario());
        return usuario;
    }

	//Metodo UNICO para el signUp
	public void registrarCliente(Usuario nuevoCliente) {
        if (nuevoCliente == null) {
            throw new IllegalArgumentException("Los datos del registro están vacíos.");
        }
        
        // Regla de negocio: Todo registro público es CLIENTE
        nuevoCliente.setRol(RolesUsuario.CLIENTE);
        
        // Reutilizamos el método que ya valida campos, hashea la clave y la guarda.
        this.registrarUsuario(nuevoCliente);
        
        System.out.println("Servicio: Nuevo CLIENTE registrado exitosamente desde la web pública.");
    }
}
