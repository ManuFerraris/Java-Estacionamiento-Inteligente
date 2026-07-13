package estacionamiento.repository;

import estacionamiento.domain.HistoricoSalidas;
import jakarta.persistence.*;
import java.util.List;

public class HistoricoSalidasRepositoryMySQL implements HistoricoSalidasRepository {
    private EntityManager em;

    public HistoricoSalidasRepositoryMySQL() {
        this.em = Persistence.createEntityManagerFactory("EstacionamientoPU").createEntityManager();
    }

    @Override
    public HistoricoSalidas save(HistoricoSalidas h) {
        em.getTransaction().begin();
        em.persist(h);
        em.getTransaction().commit();
        return h;
    }

    @Override
    public List<HistoricoSalidas> findAll() {
        return em.createQuery("SELECT h FROM HistoricoSalidas h", HistoricoSalidas.class).getResultList();
    }
}