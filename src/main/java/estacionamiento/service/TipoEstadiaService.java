
package estacionamiento.service;

import java.util.List;

import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.TipoEstadiaRepository;

public class TipoEstadiaService {

    private final TipoEstadiaRepository tipoEstadiaRepository;

    public TipoEstadiaService(TipoEstadiaRepository tipoEstadiaRepository) {
        this.tipoEstadiaRepository = tipoEstadiaRepository;
    }

    // ALTA
    public void registrarTipoEstadia(TipoEstadia nuevoTipoEstadia) {

        if (nuevoTipoEstadia == null) {
            throw new IllegalArgumentException(
                "No se puede registrar un tipo de estadía nulo."
            );
        }

        validarDatos(nuevoTipoEstadia);

        tipoEstadiaRepository.guardar(nuevoTipoEstadia);
    }

    // CONSULTA GENERAL
    public List<TipoEstadia> obtenerTodos() {
        return tipoEstadiaRepository.obtenerTodos();
    }

    // CONSULTA INDIVIDUAL
    public TipoEstadia buscarPorNumero(int numero) {

        if (numero <= 0) {
            throw new IllegalArgumentException(
                "El número del tipo de estadía debe ser válido."
            );
        }

        TipoEstadia tipoEstadia =
            tipoEstadiaRepository.buscarPorNumero(numero);

        if (tipoEstadia == null) {
            throw new IllegalArgumentException(
                "No existe un tipo de estadía con el número " + numero + "."
            );
        }

        return tipoEstadia;
    }

    // MODIFICACIÓN
    public void actualizarTipoEstadia(
            int numero,
            TipoEstadia nuevosDatos) {

        if (nuevosDatos == null) {
            throw new IllegalArgumentException(
                "Los datos del tipo de estadía no pueden ser nulos."
            );
        }

        buscarPorNumero(numero);
        validarDatos(nuevosDatos);

        tipoEstadiaRepository.actualizar(numero, nuevosDatos);
    }

 // BAJA
    public void eliminarTipoEstadia(int numero) {

        // Verificamos que el tipo de estadía exista.
        buscarPorNumero(numero);

        // Comprobamos si tiene reservas asociadas.
        if (tipoEstadiaRepository.tieneReservasAsociadas(numero)) {

            throw new IllegalArgumentException(
                "No se puede eliminar este tipo de estadía porque tiene reservas asociadas."
            );
        }

        // Si no tiene reservas, permitimos la baja.
        tipoEstadiaRepository.eliminar(numero);
    }

    // VALIDACIONES COMPARTIDAS
    private void validarDatos(TipoEstadia tipoEstadia) {

        if (tipoEstadia.getDescripcion() == null
                || tipoEstadia.getDescripcion().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "La descripción del tipo de estadía es obligatoria."
            );
        }

        if (tipoEstadia.getCupo() <= 0) {
            throw new IllegalArgumentException(
                "El cupo del tipo de estadía debe ser mayor a cero."
            );
        }
    }
}
