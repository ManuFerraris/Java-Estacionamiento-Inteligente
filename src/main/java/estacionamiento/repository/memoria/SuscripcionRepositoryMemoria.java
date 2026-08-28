package estacionamiento.repository.memoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.EstadoSuscripcion;
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
            // Aprovechamos tu excelente método equals() en SuscripcionId
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
        // En colecciones de memoria, si el Servicio modificó el objeto, 
        // ya está actualizado aquí por referencia de memoria. 
        // Solo verificamos que realmente exista para imitar el comportamiento de la base de datos.
        Suscripcion existente = buscarPorClave(suscripcion.getId());

        if (existente != null) {
            System.out.println("Memoria: Suscripción actualizada con éxito.");
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la suscripción.");
        }
    }

    // ¡Este es el método vital para la regla de negocio del "Upgrade"!
    @Override
    public Suscripcion buscarActivaPorUsuario(int numeroUsuario) {
        for (Suscripcion s : this.baseDeDatosMemoria) {
            if (s.getId().getNumero() == numeroUsuario && s.getEstado() == EstadoSuscripcion.ACTIVA) {
                return s; // Retorna la única suscripción activa que encuentre
            }
        }
        return null; // Retorna null si el usuario no tiene ninguna activa
    }

	@Override
	public void eliminar(int codigoTP, int numeroUsuario, LocalDateTime fechaDesde) {
		// Aqui si lo podemos implementar, total cuanto mucho hacemos tests y todo en memoria.		
	}
}