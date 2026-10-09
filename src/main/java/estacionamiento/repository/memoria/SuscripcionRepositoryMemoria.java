package estacionamiento.repository.memoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.EstadoSuscripcion;
import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.claves.SuscripcionId;
import estacionamiento.repository.SuscripcionRepository;

public class SuscripcionRepositoryMemoria implements SuscripcionRepository {

    private List<Suscripcion> baseDeDatosMemoria;

    public SuscripcionRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Suscripcion> obtenerTodas() {
        return this.baseDeDatosMemoria;
    }
    
    @Override
    public Suscripcion buscarPorClave(SuscripcionId id) {
        for (Suscripcion s : this.baseDeDatosMemoria) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void guardar(Suscripcion suscripcion) {
        if (buscarPorClave(suscripcion.getId()) != null) {
            throw new IllegalArgumentException("Ya existe una suscripción idéntica en esa fecha para este usuario y plan.");
        }
        
        this.baseDeDatosMemoria.add(suscripcion);
        System.out.println("Memoria: Suscripción guardada. Usuario ID: " + suscripcion.getId().getNumero() 
                + " | Plan ID: " + suscripcion.getId().getCodigo());
    }

    @Override
    public void actualizar(Suscripcion suscripcion) {
        Suscripcion existente = buscarPorClave(suscripcion.getId());

        if (existente != null) {
            System.out.println("Memoria: Suscripción actualizada con éxito.");
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la suscripción.");
        }
    }

    @Override
    public Suscripcion buscarActivaPorUsuario(int numeroUsuario) {
        for (Suscripcion s : this.baseDeDatosMemoria) {
            if (s.getId().getNumero() == numeroUsuario && s.getEstado() == EstadoSuscripcion.ACTIVA) {
                return s; 
            }
        }
        return null; 
    }

    @Override
    public void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
        // Implementación pendiente
    }
	
    @Override
    public List<Suscripcion> filtrarPadron(String estadoSub, String estadoPago) {
        List<Suscripcion> filtradas = new ArrayList<>();

        for (Suscripcion sub : this.baseDeDatosMemoria) { 
            boolean coincideEstado = true;
            boolean coincidePago = true;

            if (estadoSub != null && !estadoSub.isEmpty()) {
                EstadoSuscripcion filtroS = EstadoSuscripcion.valueOf(estadoSub);
                if (sub.getEstado() != filtroS) {
                    coincideEstado = false;
                }
            }

            if (estadoPago != null && !estadoPago.isEmpty()) {
                EstadoPago filtroP = EstadoPago.valueOf(estadoPago);
                if (sub.getUltimoPago() != filtroP) {
                    coincidePago = false;
                }
            }

            if (coincideEstado && coincidePago) {
                filtradas.add(sub);
            }
        }

        return filtradas;
    }
}