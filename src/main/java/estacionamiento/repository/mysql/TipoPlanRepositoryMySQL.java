package estacionamiento.repository.mysql;

import java.util.List;

import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.TipoPlanRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class TipoPlanRepositoryMySQL implements TipoPlanRepository{
	
	private EntityManagerFactory emf;
	
	public TipoPlanRepositoryMySQL() {
		this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
	}
	
	@Override
	public void guardar(TipoPlan tp) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			if(tp.getCodigo() == null) {
				em.persist(tp);
			}else {
				em.merge(tp);
			}
			tx.commit();
		}catch(Exception e) {
			if(tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}finally {
			em.close();
		}
	}

	@Override
	public TipoPlan buscarPorClave(Integer codigo) {
		EntityManager em = emf.createEntityManager();
		try {
			return em.find(TipoPlan.class, codigo);
		} finally {
			em.close();
		}
	}

	@Override
	public List<TipoPlan> obtenerTodos() {
		EntityManager em = emf.createEntityManager();

		try {
			return em.createQuery("SELECT tp FROM TipoPlan tp", TipoPlan.class).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void actualizar(Integer codigo, TipoPlan tpNuevosDatos) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
        	tx.begin();
        	TipoPlan tpExistente = em.find(TipoPlan.class, codigo);
        	if(tpExistente != null) {
        		tpExistente.setNombre(tpNuevosDatos.getNombre());
                tpExistente.setDetalle(tpNuevosDatos.getDetalle());
                tpExistente.setFechaBaja(tpNuevosDatos.getFechaBaja());
                em.merge(tpExistente);
                tx.commit();
        	}else {
        		throw new IllegalArgumentException("MySQL: No se encontro el tipo de plan.");
        	}
        }catch(Exception e) {
        	if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }finally {
        	em.close();
        }
	}

	@Override
	public void eliminar(Integer codigo) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
        	tx.begin();
        	TipoPlan tpAEliminar = em.find(TipoPlan.class, codigo);
        	if(tpAEliminar != null) {
        		em.remove(tpAEliminar);
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
