package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoSuscripcion;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.claves.SuscripcionId;
import estacionamiento.repository.SuscripcionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class SuscripcionRepositoryMySQL implements SuscripcionRepository {

    private EntityManagerFactory emf;

    public SuscripcionRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public void guardar(Suscripcion suscripcion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(suscripcion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(Suscripcion suscripcion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Como mandamos el objeto modificado desde el service, merge actualiza los datos.
            em.merge(suscripcion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Suscripcion buscarPorClave(SuscripcionId id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Suscripcion.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Suscripcion> obtenerTodas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Suscripcion s ORDER BY s.id.fechaDesde DESC", Suscripcion.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Suscripcion buscarActivaPorUsuario(int numeroUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL para buscar la suscripción activa del usuario
            return em.createQuery(
                "SELECT s FROM Suscripcion s WHERE s.id.numero = :numUsuario AND s.estado = :estadoActiva", Suscripcion.class)
                .setParameter("numUsuario", numeroUsuario)
                .setParameter("estadoActiva", EstadoSuscripcion.ACTIVA)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

	@Override
	public void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
		// Como para auditoria no lo vamos a usar, simplemente lo nombramos aqui para mantener consistencia.
	}
}