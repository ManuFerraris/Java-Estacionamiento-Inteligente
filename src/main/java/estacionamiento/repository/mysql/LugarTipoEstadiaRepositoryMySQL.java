
package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.domain.claves.LugarTipoEstadiaId;
import estacionamiento.repository.LugarTipoEstadiaRepository;
import estacionamiento.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class LugarTipoEstadiaRepositoryMySQL implements LugarTipoEstadiaRepository {

    private final EntityManagerFactory emf;

    public LugarTipoEstadiaRepositoryMySQL() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }


@Override
public LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia) {
    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        Long cantidad = em.createQuery(
            "SELECT COUNT(l) FROM LugarTipoEstadia l " +
            "WHERE l.id.codigoLugar = :codigoLugar " +
            "AND l.id.numeroTipoEstadia = :numeroTipoEstadia " +
            "AND l.fechaHasta IS NOT NULL " +
            "AND l.fechaHasta >= :fechaDesde",
            Long.class
        )
        .setParameter("codigoLugar", lugarTipoEstadia.getLugar().getCodigo())
        .setParameter("numeroTipoEstadia", lugarTipoEstadia.getTipoEstadia().getNumero())
        .setParameter("fechaDesde", lugarTipoEstadia.getFechaDesde())
        .getSingleResult();

        if (cantidad > 0) {
            throw new IllegalArgumentException(
                "La nueva asignación debe comenzar después de la finalización de la asignación anterior."
            );
        }

        LugarTipoEstadia guardada = em.merge(lugarTipoEstadia);

        em.getTransaction().commit();

        return guardada;

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error guardando asignación: " + e.getMessage(), e);
    } finally {
        em.close();
    }
}


    @Override
    public LugarTipoEstadia findById(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        try {
            LugarTipoEstadiaId id = new LugarTipoEstadiaId(
                codigoLugar, numeroTipoEstadia, fechaDesde
            );
            return em.find(LugarTipoEstadia.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<LugarTipoEstadia> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT l FROM LugarTipoEstadia l ORDER BY l.id.fechaDesde DESC",
                LugarTipoEstadia.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<LugarTipoEstadia> findByLugar(int codigoLugar) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                "SELECT l FROM LugarTipoEstadia l " +
                "WHERE l.id.codigoLugar = :codigoLugar " +
                "ORDER BY l.id.fechaDesde DESC",
                LugarTipoEstadia.class
            )
            .setParameter("codigoLugar", codigoLugar)
            .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public void darDeBaja(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            LugarTipoEstadiaId id = new LugarTipoEstadiaId(
                codigoLugar, numeroTipoEstadia, fechaDesde
            );

            LugarTipoEstadia obj = em.find(LugarTipoEstadia.class, id);

            if (obj == null) {
                throw new RuntimeException("No se encontró la asignación.");
            }

            if (obj.getFechaHasta() != null) {
                throw new RuntimeException("La asignación ya está dada de baja.");
            }

            obj.setFechaHasta(LocalDateTime.now());

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al dar de baja la asignación: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeAsignacionVigente(int codigoLugar, int numeroTipoEstadia) {
        EntityManager em = emf.createEntityManager();
        try {
            Long cantidad = em.createQuery(
                "SELECT COUNT(l) FROM LugarTipoEstadia l " +
                "WHERE l.id.codigoLugar = :codigoLugar " +
                "AND l.id.numeroTipoEstadia = :numeroTipoEstadia " +
                "AND l.fechaHasta IS NULL",
                Long.class
            )
            .setParameter("codigoLugar", codigoLugar)
            .setParameter("numeroTipoEstadia", numeroTipoEstadia)
            .getSingleResult();

            return cantidad > 0;

        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        throw new UnsupportedOperationException(
            "No se permite eliminar físicamente una asignación. Utilice darDeBaja()."
        );
    }
}
