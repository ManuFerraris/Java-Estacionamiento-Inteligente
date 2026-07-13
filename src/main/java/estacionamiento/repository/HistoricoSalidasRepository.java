package estacionamiento.repository;

import estacionamiento.domain.HistoricoSalidas;
import java.util.List;

public interface HistoricoSalidasRepository {
    HistoricoSalidas save(HistoricoSalidas historico);
    List<HistoricoSalidas> findAll();
}