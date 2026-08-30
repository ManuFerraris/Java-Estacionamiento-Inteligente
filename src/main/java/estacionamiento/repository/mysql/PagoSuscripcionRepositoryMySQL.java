package estacionamiento.repository.mysql;

import java.util.List;

import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.claves.PagoSuscripcionId;
import estacionamiento.repository.PagoSuscripcionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class PagoSuscripcionRepositoryMySQL implements PagoSuscripcionRepository {

    private EntityManagerFactory emf;

    public PagoSuscripcionRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public void guardar(PagoSuscripcion pago) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            System.out.println("\n--- DEBUG REPOSITORIO: ANTES DEL MERGE ---");
            System.out.println("Suscripción ID (Num): " + pago.getId().getSuscripcionId().getNumero());
            System.out.println("Suscripción ID (Cod): " + pago.getId().getSuscripcionId().getCodigo());
            System.out.println("Monto: " + pago.getMonto());
            System.out.println("Estado: " + pago.getEstado());
            System.out.println("Tipo Pago: " + pago.getTipoPago());
            
            em.merge(pago);
            tx.commit();
            
            System.out.println("--- DEBUG REPOSITORIO: COMMIT EXITOSO ---\n");
        } catch (Exception e) {
        	System.err.println("\n--- DEBUG REPOSITORIO: EXPLOTÓ LA TRANSACCIÓN ---");
            System.err.println("Mensaje de Exception: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Causa Raíz: " + e.getCause().getMessage());
            }
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(PagoSuscripcion pago) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pago);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public PagoSuscripcion buscarPorClave(PagoSuscripcionId id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(PagoSuscripcion.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PagoSuscripcion> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM PagoSuscripcion p ORDER BY p.id.fechaHoraEmision DESC", PagoSuscripcion.class)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
