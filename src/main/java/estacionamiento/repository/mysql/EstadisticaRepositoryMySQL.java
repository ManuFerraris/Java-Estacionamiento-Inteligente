package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.EstadoReserva;
import estacionamiento.dto.EstadisticaVehiculoDTO;
import estacionamiento.dto.EstadisticaEstadoDTO;
import estacionamiento.dto.EstadisticaResumenDTO;
import estacionamiento.repository.EstadisticaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class EstadisticaRepositoryMySQL implements EstadisticaRepository {

    private EntityManagerFactory emf;

    public EstadisticaRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    @Override
    public List<EstadisticaVehiculoDTO> obtenerReservasPorTipoVehiculo(
            LocalDateTime desde, LocalDateTime hasta, String estado) {

        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "SELECT new estacionamiento.dto.EstadisticaVehiculoDTO(" +
                    "r.vehiculo.tipoVehiculo.nombre, COUNT(r)) " +
                    "FROM Reserva r WHERE 1=1";

            jpql += construirFiltroFechas();

            if (estado != null && !estado.isEmpty()) {
                jpql += " AND r.estado = :estado";
            }

            jpql += " GROUP BY r.vehiculo.tipoVehiculo.nombre";

            TypedQuery<EstadisticaVehiculoDTO> query =
                    em.createQuery(jpql, EstadisticaVehiculoDTO.class);

            aplicarFechas(query, desde, hasta);

            if (estado != null && !estado.isEmpty()) {
                query.setParameter("estado", EstadoReserva.valueOf(estado));
            }

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<EstadisticaEstadoDTO> obtenerReservasPorEstado(
            LocalDateTime desde, LocalDateTime hasta, String tipoVehiculo) {

        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "SELECT new estacionamiento.dto.EstadisticaEstadoDTO(" +
                    "r.estado, COUNT(r)) " +
                    "FROM Reserva r WHERE 1=1";

            jpql += construirFiltroFechas();

            if (tipoVehiculo != null && !tipoVehiculo.isEmpty()) {
                jpql += " AND r.vehiculo.tipoVehiculo.nombre = :tipoVehiculo";
            }

            jpql += " GROUP BY r.estado";

            TypedQuery<EstadisticaEstadoDTO> query =
                    em.createQuery(jpql, EstadisticaEstadoDTO.class);

            aplicarFechas(query, desde, hasta);

            if (tipoVehiculo != null && !tipoVehiculo.isEmpty()) {
                query.setParameter("tipoVehiculo", tipoVehiculo);
            }

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public EstadisticaResumenDTO obtenerResumen(
            LocalDateTime desde, LocalDateTime hasta,
            String tipoVehiculo, String estado) {

        EntityManager em = emf.createEntityManager();

        try {
            String base = " FROM Reserva r WHERE 1=1" + construirFiltroFechas();

            if (tipoVehiculo != null && !tipoVehiculo.isEmpty()) {
                base += " AND r.vehiculo.tipoVehiculo.nombre = :tipoVehiculo";
            }

            Long finalizadas = contarEstado(em, base, desde, hasta,
                    tipoVehiculo, EstadoReserva.FINALIZADA);

            Long canceladas = contarEstado(em, base, desde, hasta,
                    tipoVehiculo, EstadoReserva.CANCELADA);

            Long pendientes = contarEstado(em, base, desde, hasta,
                    tipoVehiculo, EstadoReserva.PENDIENTE);

            Long noPresentadas = contarEstado(em, base, desde, hasta,
                    tipoVehiculo, EstadoReserva.NO_PRESENTADO);

            // El total se calcula después de obtener los cuatro valores
            Long total = finalizadas + canceladas + pendientes + noPresentadas;

            double ratio = (finalizadas + canceladas == 0)
                    ? 0.0
                    : finalizadas * 100.0 / (finalizadas + canceladas);

            return new EstadisticaResumenDTO(
                    total,
                    finalizadas,
                    canceladas,
                    pendientes,
                    noPresentadas,
                    ratio
            );

        } finally {
            em.close();
        }
    }

    private String construirFiltroFechas() {
        return " AND (:desde IS NULL OR r.id.fechaDesde >= :desde)" +
               " AND (:hasta IS NULL OR r.id.fechaDesde <= :hasta)";
    }

    private void aplicarFechas(
            TypedQuery<?> query, LocalDateTime desde, LocalDateTime hasta) {

        query.setParameter("desde", desde);
        query.setParameter("hasta", hasta);
    }

    private Long contar(
            EntityManager em, String jpql,
            LocalDateTime desde, LocalDateTime hasta,
            String tipoVehiculo, EstadoReserva estado) {

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        aplicarFechas(query, desde, hasta);

        if (tipoVehiculo != null && !tipoVehiculo.isEmpty()) {
            query.setParameter("tipoVehiculo", tipoVehiculo);
        }

        return query.getSingleResult();
    }

    private Long contarEstado(
            EntityManager em, String base,
            LocalDateTime desde, LocalDateTime hasta,
            String tipoVehiculo, EstadoReserva estado) {

        String jpql = "SELECT COUNT(r)" + base + " AND r.estado = :estado";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        aplicarFechas(query, desde, hasta);

        if (tipoVehiculo != null && !tipoVehiculo.isEmpty()) {
            query.setParameter("tipoVehiculo", tipoVehiculo);
        }

        query.setParameter("estado", estado);

        return query.getSingleResult();
    }
}