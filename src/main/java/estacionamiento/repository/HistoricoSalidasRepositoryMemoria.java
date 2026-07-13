package estacionamiento.repository;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.domain.claves.HistoricoSalidasId;
import estacionamiento.domain.claves.ReservaId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoricoSalidasRepositoryMemoria implements HistoricoSalidasRepository {

    private List<HistoricoSalidas> baseDeDatos = new ArrayList<>();

    @Override
    public HistoricoSalidas save(HistoricoSalidas historico) {
        
        HistoricoSalidasId id = extraerId(historico);
        
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Ya existe un registro con esa clave primaria.");
        }
        baseDeDatos.add(historico);
        return historico;
    }

    @Override
    public List<HistoricoSalidas> findAll() {
        return new ArrayList<>(baseDeDatos);
    }

    // MÉTODO AUXILIAR PARA EXTRAER EL ID
    public HistoricoSalidasId extraerId(HistoricoSalidas h) {
        // Extraemos los campos de la reserva para formar su propio ID
        ReservaId resId = new ReservaId(
            h.getReserva().getVehiculo().getPatente(),
            h.getReserva().getTipoEstadia().getNumero(),
            h.getReserva().getUsuario().getNumero(),
            h.getReserva().getFechaDesde()
        );
        return new HistoricoSalidasId(resId, h.getFechaHoraSalidaParcial());
    }

    public Optional<HistoricoSalidas> findById(HistoricoSalidasId id) {
        return baseDeDatos.stream()
                .filter(h -> extraerId(h).equals(id))
                .findFirst();
    }
}