package estacionamiento.service;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.repository.HistoricoSalidasRepository;
import java.util.List;

public class HistoricoSalidasService {
    
    private final HistoricoSalidasRepository historicoSalidasRepository;

    public HistoricoSalidasService(HistoricoSalidasRepository historicoSalidasRepository) { 
        this.historicoSalidasRepository = historicoSalidasRepository; 
    }

    public void registrarSalida(HistoricoSalidas historico) {
        if (historico == null) {
            throw new IllegalArgumentException("El registro de salida no puede ser nulo.");
        }
        
        if (historico.getReserva() == null) {
            throw new IllegalArgumentException("La salida debe estar obligatoriamente vinculada a una reserva válida.");
        }

        if (historico.getFechaHoraSalidaParcial() == null) {
            throw new IllegalArgumentException("La fecha y hora de la salida parcial es obligatoria.");
        }

        if (historico.getFechaHoraRegresoParcial() != null && 
            historico.getFechaHoraRegresoParcial().isBefore(historico.getFechaHoraSalidaParcial())) {
            throw new IllegalArgumentException("Error de consistencia: La fecha de regreso parcial no puede ser anterior a la salida.");
        }

        if (historico.getFechaHoraRegresoReal() != null && 
            historico.getFechaHoraRegresoReal().isBefore(historico.getFechaHoraSalidaParcial())) {
            throw new IllegalArgumentException("Error de consistencia: La fecha de regreso real no puede ser anterior a la salida.");
        }

        historicoSalidasRepository.guardar(historico);
        
        System.out.println("Servicio: Histórico de salidas validado y procesado correctamente.");
    }

    public List<HistoricoSalidas> obtenerHistorial() { 
        return historicoSalidasRepository.obtenerTodos(); 
    }
}