package estacionamiento.repository.mysql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import estacionamiento.dto.FilaReporteFinancieroDTO;

public class ReporteFinancieroRepository {

    private final EntityManagerFactory emf;

    public ReporteFinancieroRepository() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }

    public List<FilaReporteFinancieroDTO> generarReporte(LocalDateTime fechaDesde, LocalDateTime fechaHasta, String filtroOrigen) {
        EntityManager em = emf.createEntityManager();
        List<FilaReporteFinancieroDTO> resultados = new ArrayList<>();

        try {
            // Consulta de pagos de reservas
            if (filtroOrigen == null || filtroOrigen.equals("TODOS") || filtroOrigen.equals("RESERVAS")) {
                StringBuilder jpqlReservas = new StringBuilder(
                    "SELECT new estacionamiento.dto.FilaReporteFinancieroDTO(" +
                    "p.fechaHora, 'Reserva Eventual', CONCAT('Pago Reserva #', p.numero), p.monto, p.estado) " +
                    "FROM Pago p WHERE 1=1"
                );

                if (fechaDesde != null) jpqlReservas.append(" AND p.fechaHora >= :desde");
                if (fechaHasta != null) jpqlReservas.append(" AND p.fechaHora <= :hasta");

                TypedQuery<FilaReporteFinancieroDTO> queryReservas = em.createQuery(jpqlReservas.toString(), FilaReporteFinancieroDTO.class);
                
                if (fechaDesde != null) queryReservas.setParameter("desde", fechaDesde);
                if (fechaHasta != null) queryReservas.setParameter("hasta", fechaHasta);

                resultados.addAll(queryReservas.getResultList());
            }

            // Consultar pagos de suscripciones
            if (filtroOrigen == null || filtroOrigen.equals("TODOS") || filtroOrigen.equals("SUSCRIPCIONES")) {
            	
            	StringBuilder jpqlSuscripciones = new StringBuilder(
                    "SELECT new estacionamiento.dto.FilaReporteFinancieroDTO(" +
                    "ps.fechaHoraPago, 'Suscripción Mensual', CONCAT('Suscripción #', ps.suscripcion.id.numero, ' - Usuario ', ps.suscripcion.usuario.nombreUsuario), ps.monto, ps.estado) " +
                    "FROM PagoSuscripcion ps WHERE ps.fechaHoraPago IS NOT NULL"
                );

                if (fechaDesde != null) jpqlSuscripciones.append(" AND ps.fechaHoraPago >= :desde");
                if (fechaHasta != null) jpqlSuscripciones.append(" AND ps.fechaHoraPago <= :hasta");

                TypedQuery<FilaReporteFinancieroDTO> querySuscripciones = em.createQuery(jpqlSuscripciones.toString(), FilaReporteFinancieroDTO.class);
                
                if (fechaDesde != null) querySuscripciones.setParameter("desde", fechaDesde);
                if (fechaHasta != null) querySuscripciones.setParameter("hasta", fechaHasta);

                resultados.addAll(querySuscripciones.getResultList());
            }

            resultados.sort(Comparator.comparing(FilaReporteFinancieroDTO::getFechaOperacion).reversed());

            return resultados;

        } finally {
            em.close();
        }
    }
}
