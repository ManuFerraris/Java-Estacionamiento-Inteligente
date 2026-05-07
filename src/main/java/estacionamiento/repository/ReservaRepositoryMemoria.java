package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.Reserva;

public class ReservaRepositoryMemoria implements ReservaRepository {

    private List<Reserva> baseDeDatosMemoria;

    public ReservaRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Reserva> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public Reserva buscarPorClave(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde) {
        for (Reserva r : this.baseDeDatosMemoria) {
            // Aca debemos tener cuidado porque la reserva contiene FKs que referencian a objetos
            if (r.getVehiculo().getPatente().equalsIgnoreCase(patente) &&
                r.getUsuario().getNumero() == numeroUsu &&
                r.getTipoEstadia().getNumero() == numeroTE &&
                r.getFechaDesde().equals(fechaDesde)) {
                return r; // Encontramos la reserva
            }
        }
        return null;
    }

    @Override
    public void guardar(Reserva reserva) {
        // Extraemos los datos para validar la clave primaria
        String patente = reserva.getVehiculo().getPatente();
        int numeroUsu = reserva.getUsuario().getNumero();
        int numeroTE = reserva.getTipoEstadia().getNumero();
        LocalDateTime fechaDesde = reserva.getFechaDesde();

        if (buscarPorClave(patente, numeroUsu, numeroTE, fechaDesde) != null) {
            throw new IllegalArgumentException("Ya existe una Reserva en el sistema con esa clave compuesta.");
        }
        this.baseDeDatosMemoria.add(reserva);
        System.out.println("Reserva guardada con éxito para el vehículo: " + patente + " y usuario: " + numeroUsu);
    }

    @Override
    public void actualizar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde, Reserva reservaNuevosDatos) {
        Reserva reservaExistente = buscarPorClave(patente, numeroUsu, numeroTE, fechaDesde);

        if (reservaExistente != null) {
            // IMPORTANTE: En una actualización no modificamos los atributos que forman la clave primaria.
        	// Si despues queremos cambiar el titular de la reserve hay que juntarnos y hablarlo.
        	// Quizas se da de baja la misma y se crea otra o vemos como lo manejamos.
            reservaExistente.setFechaHastaTentativa(reservaNuevosDatos.getFechaHastaTentativa());
            reservaExistente.setFechaHastaReal(reservaNuevosDatos.getFechaHastaReal());
            reservaExistente.setEstado(reservaNuevosDatos.getEstado());
            reservaExistente.setSenia(reservaNuevosDatos.getSenia());
            reservaExistente.setLugar(reservaNuevosDatos.getLugar());
            reservaExistente.setPago(reservaNuevosDatos.getPago());
            
            System.out.println("Reserva actualizada con éxito para el vehículo: " + patente);
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró la Reserva.");
        }
    }

    @Override
    public void eliminar(String patente, int numeroUsu, int numeroTE, LocalDateTime fechaDesde) {
        Reserva reservaAEliminar = buscarPorClave(patente, numeroUsu, numeroTE, fechaDesde);

        if (reservaAEliminar != null) {
            this.baseDeDatosMemoria.remove(reservaAEliminar);
            System.out.println("Reserva eliminada con éxito.");
        } else {
            System.out.println("No se encontró la Reserva para eliminar.");
        }
    }
}