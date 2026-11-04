package estacionamiento.repository.mysql;

import java.util.List;
import estacionamiento.domain.AsignacionCochera;
import estacionamiento.domain.Cochera;
import estacionamiento.repository.AsignacionCocheraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class AsignacionCocheraRepositoryMySQL implements AsignacionCocheraRepository {

	private EntityManagerFactory emf;
    public AsignacionCocheraRepositoryMySQL() {
    	this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public void guardar(AsignacionCochera asignacion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(asignacion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override
    public void actualizar(AsignacionCochera asignacion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(asignacion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Cochera> obtenerCocherasActivasPorEncargado(Integer numeroUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            // Trae las cocheras donde el encargado está activo al dia de hoy.
            String jpql = "SELECT a.cochera FROM AsignacionCochera a " +
                          "WHERE a.id.numeroUsuario = :numUsuario " +
                          "AND a.fechaHasta IS NULL";
            return em.createQuery(jpql, Cochera.class)
                     .setParameter("numUsuario", numeroUsuario)
                     .getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<AsignacionCochera> obtenerTodasLasActivas() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT a FROM AsignacionCochera a WHERE a.fechaHasta IS NULL ORDER BY a.cochera.nombre";
            return em.createQuery(jpql, AsignacionCochera.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public AsignacionCochera buscarActivaPorUsuarioYCochera(Integer numeroUsuario, Integer codigoCochera) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT a FROM AsignacionCochera a " +
                          "WHERE a.id.numeroUsuario = :numUsuario " +
                          "AND a.id.codigoCochera = :codCochera " +
                          "AND a.fechaHasta IS NULL";
            TypedQuery<AsignacionCochera> query = em.createQuery(jpql, AsignacionCochera.class);
            query.setParameter("numUsuario", numeroUsuario);
            query.setParameter("codCochera", codigoCochera);
            List<AsignacionCochera> res = query.getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }
}
