package estacionamiento.repository;

import java.util.List;

import estacionamiento.domain.TipoPlan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TipoPlanRepositoryMySQL implements TipoPlanRepository{
	
	private EntityManager em;
	
	public TipoPlanRepositoryMySQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
        this.em = emf.createEntityManager();
	}
	
	@Override
	public void guardar(TipoPlan tp) {
		em.getTransaction().begin();
        em.persist(tp);
        em.getTransaction().commit();
        System.out.println("MySQL: Tipo de plan insertado correctamente en la base de datos.");
	}

	@Override
	public TipoPlan buscarPorClave(int codigo) {
		return em.find(TipoPlan.class, codigo);
	}

	@Override
	public List<TipoPlan> obtenerTodos() {
		return em.createQuery("SELECT tp FROM TipoPlan tp", TipoPlan.class).getResultList();
	}

	@Override
	public void actualizar(int codigo, TipoPlan tpNuevosDatos) {
		TipoPlan tpExistente = buscarPorClave(codigo);
        
        if (tpExistente != null) {
            em.getTransaction().begin();
            tpExistente.setNombre(tpNuevosDatos.getNombre());
            tpExistente.setDetalle(tpNuevosDatos.getDetalle());
            em.getTransaction().commit();
            System.out.println("MySQL: Tipo de plan actualizado correctamente.");
        } else {
            throw new IllegalArgumentException("MySQL: No se encontró el ripo de plan para actualizar.");
        }
	}

	@Override
	public void eliminar(int codigo) {
		TipoPlan tpAEliminar = buscarPorClave(codigo);
        
        if (tpAEliminar != null) {
            em.getTransaction().begin();
            em.remove(tpAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Tipo de plan eliminado correctamente.");
        } else {
            System.out.println("MySQL: No se encontró el tipo de plan para eliminar.");
        }
	}
}
