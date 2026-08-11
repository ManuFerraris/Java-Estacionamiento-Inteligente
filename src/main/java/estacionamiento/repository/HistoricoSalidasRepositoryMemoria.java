package estacionamiento.repository;

import estacionamiento.domain.HistoricoSalidas;
import estacionamiento.domain.claves.HistoricoSalidasId;
import estacionamiento.domain.claves.ReservaId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoSalidasRepositoryMemoria implements HistoricoSalidasRepository {
    
    private List<HistoricoSalidas> baseDeDatosMemoria;
    
    public HistoricoSalidasRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }
    
    @Override
    public List<HistoricoSalidas> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public HistoricoSalidas buscarPorClave(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial) {
        // Creamos la clave compuesta anidada
        ReservaId reservaIdBusqueda = new ReservaId(patente, numeroUsuario, numeroTipoEstadia, fechaDesde);
        HistoricoSalidasId idBusqueda = new HistoricoSalidasId(reservaIdBusqueda, fechaHoraSalidaParcial);
        
        for (HistoricoSalidas hs : this.baseDeDatosMemoria) {
            if (hs.getId().equals(idBusqueda)) {
                return hs;
            }
        }
        return null;
    }
    
    @Override
    public void guardar(HistoricoSalidas historicoSalidas) {

        ReservaId rId = historicoSalidas.getId().getReservaId();
        
        if (buscarPorClave(rId.getNumeroTipoEstadia(), rId.getNumeroUsuario(), 
                           rId.getPatenteVehiculo(), rId.getFechaDesde(), 
                           historicoSalidas.getId().getFechaHoraSalidaParcial()) != null) {
            throw new IllegalArgumentException("Ya existe un registro de Histórico de Salidas con esa clave primaria.");
        }
        
        this.baseDeDatosMemoria.add(historicoSalidas);
        System.out.println("Histórico de salidas registrado con éxito para la patente: " + rId.getPatenteVehiculo());
    }

    @Override
    public void actualizar(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial, HistoricoSalidas historicoSalidasNuevo) {
        
        HistoricoSalidas historicoExistente = buscarPorClave(numeroTipoEstadia, numeroUsuario, patente, fechaDesde, fechaHoraSalidaParcial);

        if (historicoExistente != null) {
        	// Recuerden que no debemos actualizar campos de la clave compuesta.
            historicoExistente.setFechaHoraRegresoParcial(historicoSalidasNuevo.getFechaHoraRegresoParcial());
            historicoExistente.setFechaHoraRegresoReal(historicoSalidasNuevo.getFechaHoraRegresoReal());

            System.out.println("Histórico de salidas actualizado con éxito, patente: " + patente);
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el registro de salida.");
        }
    }

    @Override
    public void eliminar(int numeroTipoEstadia, int numeroUsuario, String patente, LocalDateTime fechaDesde, LocalDateTime fechaHoraSalidaParcial) {
        
        HistoricoSalidas historicoAEliminar = buscarPorClave(numeroTipoEstadia, numeroUsuario, patente, fechaDesde, fechaHoraSalidaParcial);

        if (historicoAEliminar != null) {
            this.baseDeDatosMemoria.remove(historicoAEliminar);
            System.out.println("Histórico de salidas eliminado con éxito.");
        } else {
            System.out.println("No se encontró el registro histórico para eliminar.");
        }
    }
}