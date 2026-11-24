
package estacionamiento.service;

import java.time.LocalDate;
import java.util.List;

import estacionamiento.domain.Cochera;
import estacionamiento.repository.CocheraRepository;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;

public class CocheraService {

    private final CocheraRepository cocheraRepository;
    private final CocheraRepositoryMySQL cocheraRepositoryMySQL;

    public CocheraService(CocheraRepository cocheraRepository) {
        this.cocheraRepository = cocheraRepository;

        if (!(cocheraRepository instanceof CocheraRepositoryMySQL)) {
            throw new IllegalArgumentException(
                "Se requiere CocheraRepositoryMySQL para gestionar las bajas."
            );
        }

        this.cocheraRepositoryMySQL =
            (CocheraRepositoryMySQL) cocheraRepository;
    }

    private void validarDatos(Cochera cochera) {
        if (cochera == null) {
            throw new IllegalArgumentException(
                "La cochera no puede ser nula."
            );
        }

        if (cochera.getNombre() == null ||
            cochera.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El nombre es obligatorio."
            );
        }

        if (cochera.getNombre().length() > 50) {
            throw new IllegalArgumentException(
                "El nombre no puede superar los 50 caracteres."
            );
        }

        if (cochera.getDireccion() == null ||
            cochera.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "La dirección es obligatoria."
            );
        }

        if (cochera.getDireccion().length() > 50) {
            throw new IllegalArgumentException(
                "La dirección no puede superar los 50 caracteres."
            );
        }

        if (cochera.getDescripcion() == null ||
            cochera.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "La descripción es obligatoria."
            );
        }

        if (cochera.getDescripcion().length() > 255) {
            throw new IllegalArgumentException(
                "La descripción no puede superar los 255 caracteres."
            );
        }
    }

    public void guardar(Cochera cochera) {
        validarDatos(cochera);

        cochera.setFechaBaja(null);
        cocheraRepository.guardar(cochera);
    }

    public void actualizar(Integer codigo, Cochera cocheraActualizada) {
        Cochera existente = buscarPorCodigo(codigo);

        if (existente.getFechaBaja() != null) {
            throw new IllegalArgumentException(
                "No se puede modificar una cochera inactiva."
            );
        }

        validarDatos(cocheraActualizada);

        cocheraActualizada.setFechaBaja(null);
        cocheraRepository.actualizar(codigo, cocheraActualizada);
    }

    public List<Cochera> obtenerTodas() {
        return cocheraRepository.obtenerTodos();
    }

    public Cochera buscarPorCodigo(Integer codigo) {
        if (codigo == null || codigo <= 0) {
            throw new IllegalArgumentException(
                "El código de búsqueda debe ser válido."
            );
        }

        Cochera cochera = cocheraRepository.buscarPorClave(codigo);

        if (cochera == null) {
            throw new IllegalArgumentException(
                "No se encontró ninguna cochera con el código "
                + codigo + "."
            );
        }

        return cochera;
    }

    public void eliminar(Integer codigo) {
        darDeBaja(codigo);
    }

    public void darDeBaja(Integer codigo) {
        Cochera cochera = buscarPorCodigo(codigo);

        if (cochera.getFechaBaja() != null) {
            throw new IllegalArgumentException(
                "La cochera ya se encuentra inactiva."
            );
        }

        cocheraRepository.eliminar(codigo);
    }

    public void darDeAltaPostBaja(Integer codigo) {
        Cochera cochera = buscarPorCodigo(codigo);

        if (cochera.getFechaBaja() == null) {
            throw new IllegalArgumentException(
                "La cochera ya se encuentra activa."
            );
        }

        cocheraRepositoryMySQL.reactivar(codigo);
    }
}
