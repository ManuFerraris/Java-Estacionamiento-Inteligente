package estacionamiento.repository.mysql;

import java.util.List;

import estacionamiento.domain.Usuario;
import estacionamiento.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class UsuarioRepositoryMySQL implements UsuarioRepository{

	private EntityManagerFactory emf;
    
    public UsuarioRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
	@Override
	public void guardar(Usuario usuario) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			if(usuario.getNumero() == null) {
				em.persist(usuario);
			}else {
				em.merge(usuario);
			}
			tx.commit();
	        System.out.println("MySQL: Usuario insertado correctamente en la base de datos.");
		}catch(Exception e){
			if(tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}finally {
			em.close();
		}
	}

	@Override
	public Usuario buscarPorNumero(Integer numero) {
		EntityManager em = emf.createEntityManager();
		try {
			return em.find(Usuario.class, numero);
		}finally {
			em.close();
		}
	}

	@Override
	public List<Usuario> obtenerTodos() {
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
		}finally {
			em.close();
		}
	}

	@Override
	public void actualizar(Integer numero, Usuario usuarioNuevosDatos) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
        	tx.begin();
        	Usuario usuarioExistente = em.find(Usuario.class, numero);
        	if(usuarioExistente != null) {
        		usuarioExistente.setNombre(usuarioNuevosDatos.getNombre());
    			usuarioExistente.setApellido(usuarioNuevosDatos.getApellido());
    			usuarioExistente.setNumeroTelefono(usuarioNuevosDatos.getNumeroTelefono());
    			usuarioExistente.setDireccion(usuarioNuevosDatos.getDireccion());
    			usuarioExistente.setMail(usuarioNuevosDatos.getMail());
    			usuarioExistente.setMailRecuperacion(usuarioNuevosDatos.getMailRecuperacion());
    			usuarioExistente.setFechaBaja(usuarioNuevosDatos.getFechaBaja());
    			usuarioExistente.setNombreUsuario(usuarioNuevosDatos.getNombreUsuario());
    			usuarioExistente.setContrasenia(usuarioNuevosDatos.getContrasenia());
    			em.merge(usuarioExistente);
    			tx.commit();
        	}else {
        		throw new IllegalArgumentException("MySQL: No se encontro el usuario.");
        	}
        }catch(Exception e) {
        	if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
	}

	@Override
	public void eliminar(Integer numero) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
        	Usuario usuarioAEliminar = em.find(Usuario.class, numero);
        	if(usuarioAEliminar != null) {
        		em.remove(usuarioAEliminar);
        		tx.commit();
        	}
        }catch(Exception e) {
        	if (tx != null && tx.isActive()) tx.rollback();
        	throw e;
        }finally {
        	em.close();
        }
	}

}
