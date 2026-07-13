package estacionamiento.service;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.repository.HistoricoSalidasRepository;
import java.util.List;

public class HistoricoSalidasService {
    private final HistoricoSalidasRepository repo;

    public HistoricoSalidasService(HistoricoSalidasRepository repo) { this.repo = repo; }

    public void registrarSalida(HistoricoSalidas h) {
        if (h == null || h.getReserva() == null) 
            throw new IllegalArgumentException("La salida debe estar vinculada a una reserva.");
        repo.save(h);
    }

    public List<HistoricoSalidas> obtenerHistorial() { return repo.findAll(); }
}