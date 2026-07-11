package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class UsuarioRepositoryMySQL implements UsuarioRepository{

	private EntityManager em;
    
    public UsuarioRepositoryMySQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
    }
    
	@Override
	public void guardar(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        System.out.println("MySQL: Usuario insertado correctamente en la base de datos.");
	}

	@Override
	public Usuario buscarPorNumero(int numero) {
		return em.find(Usuario.class, numero);
	}

	@Override
	public List<Usuario> obtenerTodos() {
		return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
	}

	@Override
	public void actualizar(int numero, Usuario usuarioNuevosDatos) {
		Usuario usuarioExistente = buscarPorNumero(numero);
        
        if (usuarioExistente != null) {
            em.getTransaction().begin();
            // Al estar dentro de una transacción, los setters modifican la BD automáticamente
            usuarioExistente.setNombre(usuarioNuevosDatos.getNombre());
			usuarioExistente.setApellido(usuarioNuevosDatos.getApellido());
			usuarioExistente.setNumeroTelefono(usuarioNuevosDatos.getNumeroTelefono());
			usuarioExistente.setDireccion(usuarioNuevosDatos.getDireccion());
			usuarioExistente.setMail(usuarioNuevosDatos.getMail());
			usuarioExistente.setMailRecuperacion(usuarioNuevosDatos.getMailRecuperacion());
			usuarioExistente.setFechaBaja(usuarioNuevosDatos.getFechaBaja());
			usuarioExistente.setNombreUsuario(usuarioNuevosDatos.getNombreUsuario());
			usuarioExistente.setContrasenia(usuarioNuevosDatos.getContrasenia());
            em.getTransaction().commit();
            System.out.println("Usuario actualizado: " + numero + " " + usuarioExistente.getNombre() + " " + usuarioExistente.getApellido());
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el usuario para actualizar.");
        }
	}

	@Override
	public void eliminar(int numero) {
		Usuario usuarioAEliminar = buscarPorNumero(numero);
        
        if (usuarioAEliminar != null) {
            em.getTransaction().begin();
            em.remove(usuarioAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Usuario eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el usuario para eliminar.");
        }
	}

}
