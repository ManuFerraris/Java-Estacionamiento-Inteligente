package estacionamiento.repository.mysql;

import java.util.List;
import estacionamiento.domain.Cochera;
import estacionamiento.repository.CocheraRepository;
import estacionamiento.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDate;

public class CocheraRepositoryMySQL implements CocheraRepository {
	private EntityManagerFactory emf;
    
    public CocheraRepositoryMySQL() {
    	this.emf = JPAUtil.getEntityManagerFactory();
    }
    
    // El famoso 'getOne()' de diseño
    @Override
    public Cochera buscarPorClave(Integer codigo) {
    	EntityManager em = emf.createEntityManager();
    	try {
    		return em.find(Cochera.class, codigo);
    	}finally {
    		em.close(); // liberamos la memoria
    	}
    }
    
    //El famoso 'FindAll()' de diseño
    @Override
    public List<Cochera> obtenerTodos() {
    	EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cochera c", Cochera.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    //El 'save()' del amado Aldo que si te lo olvidavas te bochaba.
    @Override
    public void guardar(Cochera cochera) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            if (cochera.getCodigo() == null) {
                em.persist(cochera); // Nuevo -> INSERT
            } else {
                em.merge(cochera);   // Ya existe -> UPDATE
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Limpia la memoria si algo sale mal
            }
            throw e; // Le tira el error al Servlet
        } finally {
            em.close();
        }
    }
    
    @Override
    public void actualizar(Integer codigo, Cochera cocheraNuevosDatos) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cochera cocheraExistente = em.find(Cochera.class, codigo);
            if (cocheraExistente != null) {
                cocheraExistente.setNombre(cocheraNuevosDatos.getNombre());
                cocheraExistente.setDescripcion(cocheraNuevosDatos.getDescripcion());
                cocheraExistente.setDireccion(cocheraNuevosDatos.getDireccion());
                em.merge(cocheraExistente);
                tx.commit();
            } else {
                throw new IllegalArgumentException("MySQL: No se encontró la cochera.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override
    public void eliminar(Integer codigo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cochera cochera = em.find(Cochera.class, codigo);

            if (cochera != null) {
                if (cochera.getFechaBaja() == null) {
                    cochera.setFechaBaja(LocalDate.now());
                }
                tx.commit();
            } else {
                throw new IllegalArgumentException(
                    "No se encontró la cochera para dar de baja."
                );
            }

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void reactivar(Integer codigo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cochera cochera = em.find(Cochera.class, codigo);

            if (cochera != null) {
                cochera.setFechaBaja(null);
                tx.commit();
            } else {
                throw new IllegalArgumentException(
                    "No se encontró la cochera para reactivar."
                );
            }

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
