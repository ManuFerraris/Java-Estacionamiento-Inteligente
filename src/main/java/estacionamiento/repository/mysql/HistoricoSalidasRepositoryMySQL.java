package estacionamiento.repository.mysql;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.domain.claves.HistoricoSalidasId;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.repository.HistoricoSalidasRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

public class HistoricoSalidasRepositoryMySQL implements HistoricoSalidasRepository {
    
    private EntityManager em;

    public HistoricoSalidasRepositoryMySQL() {
        this.em = Persistence.createEntityManagerFactory("EstacionamientoPU").createEntityManager();
    }

    @Override
    public void guardar(HistoricoSalidas historicoSalidas) {
        em.getTransaction().begin();
        em.persist(historicoSalidas);
        em.getTransaction().commit();
        System.out.println("MySQL: Histórico de salidas registrado con éxito.");
    }

    @Override
    public HistoricoSalidas buscarPorClave(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial) {
    	// Armamos la clave anidada
        ReservaId reservaId = new ReservaId(patente, numeroUsuario, numeroTipoEstadia, fechaDesde);
        HistoricoSalidasId idCompuesto = new HistoricoSalidasId(reservaId, fechaHoraSalidaParcial);
        
        return em.find(HistoricoSalidas.class, idCompuesto);
    }

    @Override
    public List<HistoricoSalidas> obtenerTodos() {
        return em.createQuery("SELECT h FROM HistoricoSalidas h", HistoricoSalidas.class).getResultList();
    }

    @Override
    public void actualizar(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial, HistoricoSalidas historicoSalidasNuevo) {

        HistoricoSalidas historicoExistente = buscarPorClave(numeroTipoEstadia, numeroUsuario, patente, fechaDesde, fechaHoraSalidaParcial);

        if (historicoExistente != null) {
            em.getTransaction().begin();

            // Solo se modifican campos que no son clave compuesta
            historicoExistente.setFechaHoraRegresoParcial(historicoSalidasNuevo.getFechaHoraRegresoParcial());
            historicoExistente.setFechaHoraRegresoReal(historicoSalidasNuevo.getFechaHoraRegresoReal());
            
            em.merge(historicoExistente);
            em.getTransaction().commit();
            System.out.println("MySQL: Histórico de salidas actualizado con éxito.");
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el registro.");
        }
    }

    @Override
    public void eliminar(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial) {

        HistoricoSalidas historicoAEliminar = buscarPorClave(numeroTipoEstadia, numeroUsuario, patente, fechaDesde, fechaHoraSalidaParcial);

        if (historicoAEliminar != null) {
            em.getTransaction().begin();
            em.remove(historicoAEliminar);
            em.getTransaction().commit();
            System.out.println("MySQL: Histórico de salidas eliminado con éxito.");
        } else {
            System.out.println("MySQL: No se encontró el registro para eliminar.");
        }
    }
}