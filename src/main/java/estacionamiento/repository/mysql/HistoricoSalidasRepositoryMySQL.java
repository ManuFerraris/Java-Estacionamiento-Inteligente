package estacionamiento.repository.mysql;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.domain.claves.HistoricoSalidasId;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.repository.HistoricoSalidasRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

public class HistoricoSalidasRepositoryMySQL implements HistoricoSalidasRepository {
    
    private final EntityManagerFactory emf;

    public HistoricoSalidasRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public void guardar(HistoricoSalidas historicoSalidas) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(historicoSalidas);
            em.getTransaction().commit();
            System.out.println("MySQL: Histórico de salidas registrado con éxito.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public HistoricoSalidas buscarPorClave(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial) {
        EntityManager em = emf.createEntityManager();
        try {
            ReservaId reservaId = new ReservaId(patente, numeroUsuario, numeroTipoEstadia, fechaDesde);
            HistoricoSalidasId idCompuesto = new HistoricoSalidasId(reservaId, fechaHoraSalidaParcial);
            
            return em.find(HistoricoSalidas.class, idCompuesto);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HistoricoSalidas> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT h FROM HistoricoSalidas h ORDER BY h.id.fechaHoraSalidaParcial DESC", HistoricoSalidas.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(HistoricoSalidas historicoSalidasNuevo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(historicoSalidasNuevo);
            em.getTransaction().commit();
            
            System.out.println("MySQL: Histórico de salidas actualizado con éxito.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(HistoricoSalidas historicoAEliminar) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            HistoricoSalidas entidadGestionada = em.merge(historicoAEliminar);
            em.remove(entidadGestionada);
            em.getTransaction().commit();
            
            System.out.println("MySQL: Histórico de salidas eliminado físicamente.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}