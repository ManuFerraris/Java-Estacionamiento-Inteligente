package estacionamiento.repository;
import estacionamiento.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryMemoria implements UsuarioRepository {
	
	private List<Usuario> baseDeDatosMemoria;
	
	public UsuarioRepositoryMemoria() {
		this.baseDeDatosMemoria = new ArrayList<>();
	}
	
	@Override
    public List<Usuario> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }
	
	 @Override
	 public Usuario buscarPorNumero(int numero) {
		 for (Usuario u : this.baseDeDatosMemoria) {
			 if (u.getNumero() == numero) {
	         	return u;
	         }
	     }
	     return null;
	}
	
	 @Override
	 public void guardar(Usuario usuario) {
		 // Validamos el numero porque estamos en MEMORIA (@Nati @Marilu) pero con la BD es incremental
		 if (buscarPorNumero(usuario.getNumero()) != null) {
			 throw new IllegalArgumentException("Ya existe un usuario con dicho numero: " + usuario.getNumero());
	     }
	     this.baseDeDatosMemoria.add(usuario);
	     System.out.println("Usuario guardado con éxito: " + usuario.getNumero());
	 }
	 
	 @Override
	 public void actualizar(int numeroBuscado, Usuario usuarioNuevosDatos) {
		 Usuario usuarioExistente = buscarPorNumero(numeroBuscado);
	        
		 if (usuarioExistente != null) {
			 usuarioExistente.setNombre(usuarioNuevosDatos.getNombre());
			 usuarioExistente.setApellido(usuarioNuevosDatos.getApellido());
			 usuarioExistente.setNumeroTelefono(usuarioNuevosDatos.getNumeroTelefono());
			 usuarioExistente.setDireccion(usuarioNuevosDatos.getDireccion());
			 usuarioExistente.setMail(usuarioNuevosDatos.getMail());
			 usuarioExistente.setMailRecuperacion(usuarioNuevosDatos.getMailRecuperacion());
			 usuarioExistente.setFechaBaja(usuarioNuevosDatos.getFechaBaja());
			 usuarioExistente.setNombreUsuario(usuarioNuevosDatos.getNombreUsuario());
			 usuarioExistente.setContrasenia(usuarioNuevosDatos.getContrasenia());
			 System.out.println("Usuario actualizado: " + numeroBuscado + " " + usuarioExistente.getNombre() + " " + usuarioExistente.getApellido());
		 } else {
			 throw new IllegalArgumentException("No se puede actualizar. No se encontró el numero de usuario: " + numeroBuscado);
		 }
	 }
	 
	 @Override
	 public void eliminar(int numero) {
		 Usuario usuarioAEliminar = buscarPorNumero(numero);
		 
		 if (usuarioAEliminar != null) {
			 this.baseDeDatosMemoria.remove(usuarioAEliminar);
			 System.out.println("usuario eliminado: " + numero);
		 } else {
			 System.out.println("No se encontró el usuario para eliminar.");
		 }
	 }
}
