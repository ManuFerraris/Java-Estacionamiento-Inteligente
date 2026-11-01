package estacionamiento.service;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.repository.HistoricoSalidasRepository;
import estacionamiento.repository.mysql.HistoricoSalidasRepositoryMySQL;

import java.time.LocalDateTime;
import java.util.List;

public class HistoricoSalidasService {
    
    private final HistoricoSalidasRepository historicoSalidasRepository;

    public HistoricoSalidasService() { 
        // Instanciamos el repositorio concreto
        this.historicoSalidasRepository = new HistoricoSalidasRepositoryMySQL(); 
    }

    public void registrarSalida(HistoricoSalidas historico) {
        if (historico == null) {
            throw new IllegalArgumentException("El registro de salida no puede ser nulo.");
        }
        
        if (historico.getReserva() == null) {
            throw new IllegalArgumentException("La salida debe estar obligatoriamente vinculada a una reserva válida.");
        }

        if (historico.getId().getFechaHoraSalidaParcial() == null) {
            throw new IllegalArgumentException("La fecha y hora de la salida parcial es obligatoria.");
        }

        if (historico.getFechaHoraRegresoParcial() != null && 
            historico.getFechaHoraRegresoParcial().isBefore(historico.getId().getFechaHoraSalidaParcial())) {
            throw new IllegalArgumentException("Error de consistencia: La fecha de regreso estimada no puede ser anterior a la salida.");
        }

        if (historico.getFechaHoraRegresoReal() != null && 
            historico.getFechaHoraRegresoReal().isBefore(historico.getId().getFechaHoraSalidaParcial())) {
            throw new IllegalArgumentException("Error de consistencia: La fecha de regreso real no puede ser anterior a la salida.");
        }

        historicoSalidasRepository.guardar(historico);
        System.out.println("Servicio: Salida registrada correctamente.");
    }

    public void actualizarRegresoVehiculo(HistoricoSalidas historico) {
        if (historico == null) {
            throw new IllegalArgumentException("El registro a actualizar no puede ser nulo.");
        }
        
        // Regla de negocio para el control de barrera
        if (historico.getFechaHoraRegresoReal() != null && 
            historico.getFechaHoraRegresoReal().isBefore(historico.getId().getFechaHoraSalidaParcial())) {
            throw new IllegalArgumentException("La fecha de reingreso no puede ser anterior a la fecha en que el vehículo salió.");
        }

        historicoSalidasRepository.actualizar(historico);
    }

    public HistoricoSalidas buscarSalida(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial) {
        return historicoSalidasRepository.buscarPorClave(numeroTipoEstadia, numeroUsuario, patente, fechaDesde, fechaHoraSalidaParcial);
    }

    public List<HistoricoSalidas> obtenerHistorial() { 
        return historicoSalidasRepository.obtenerTodos(); 
    }
    
    public void eliminarSalidaFisica(HistoricoSalidas historico) {
        // No se borran históricos en auditorías
        //historicoSalidasRepository.eliminar(historico);
    }
}