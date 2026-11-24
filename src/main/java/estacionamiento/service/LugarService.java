
package estacionamiento.service;

import java.util.List;

import estacionamiento.domain.Cochera;
import estacionamiento.domain.Lugar;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.repository.mysql.LugarRepositoryMySQL;

public class LugarService {

    private final LugarRepositoryMySQL lugarRepository;
    private final CocheraRepositoryMySQL cocheraRepository;

    public LugarService(LugarRepositoryMySQL lugarRepository) {
        this.lugarRepository = lugarRepository;
        this.cocheraRepository = new CocheraRepositoryMySQL();
    }

    // Validar los datos del lugar
    private void validarDatos(Lugar lugar) {

        if (lugar == null) {
            throw new IllegalArgumentException(
                "El lugar no puede ser nulo."
            );
        }

        if (lugar.getDescripcion() == null ||
            lugar.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "La descripción del lugar es obligatoria."
            );
        }

        if (lugar.getNumeroPiso() < 0) {
            throw new IllegalArgumentException(
                "El número de piso no puede ser negativo."
            );
        }

        if (lugar.getCochera() == null ||
            lugar.getCochera().getCodigo() == null) {
            throw new IllegalArgumentException(
                "El lugar debe pertenecer a una cochera válida."
            );
        }

        Cochera cochera = cocheraRepository.buscarPorClave(
            lugar.getCochera().getCodigo()
        );

        if (cochera == null) {
            throw new IllegalArgumentException(
                "La cochera seleccionada no existe."
            );
        }

        if (cochera.getFechaBaja() != null) {
            throw new IllegalArgumentException(
                "No se pueden asignar lugares a una cochera inactiva."
            );
        }
    }

    // ALTA
    public void registrarLugar(Lugar nuevoLugar) {

        validarDatos(nuevoLugar);

        nuevoLugar.setFechaBaja(null);

        lugarRepository.guardar(nuevoLugar);
    }

    // CONSULTA GENERAL
    public List<Lugar> obtenerTodosLosLugares() {
        return lugarRepository.obtenerTodos();
    }

    // CONSULTA POR CÓDIGO
    public Lugar buscarLugarPorCodigo(Integer codigo) {

        if (codigo == null || codigo <= 0) {
            throw new IllegalArgumentException(
                "El código debe ser un número positivo válido."
            );
        }

        Lugar lugar = lugarRepository.buscarPorClave(codigo);

        if (lugar == null) {
            throw new IllegalArgumentException(
                "No se encontró el lugar con código: " + codigo
            );
        }

        return lugar;
    }

    // MODIFICACIÓN
    public void actualizarLugar(Integer codigo, Lugar lugarActualizado) {

        Lugar lugarExistente = buscarLugarPorCodigo(codigo);

        if (lugarExistente.getFechaBaja() != null) {
            throw new IllegalArgumentException(
                "No se puede modificar un lugar inactivo."
            );
        }

        validarDatos(lugarActualizado);

        lugarExistente.setNumeroPiso(lugarActualizado.getNumeroPiso());
        lugarExistente.setDescripcion(
            lugarActualizado.getDescripcion().trim()
        );
        lugarExistente.setCochera(lugarActualizado.getCochera());

        lugarRepository.actualizar(codigo, lugarExistente);
    }

    // BAJA LÓGICA
    public void darDeBajaLugar(Integer codigo) {

        Lugar lugar = buscarLugarPorCodigo(codigo);

        if (lugar.getFechaBaja() != null) {
            throw new IllegalArgumentException(
                "El lugar ya se encuentra inactivo."
            );
        }

        if (lugarRepository.tieneReservasActivasPorLugar(codigo)) {
            throw new IllegalArgumentException(
                "No se puede dar de baja el lugar porque tiene reservas activas."
            );
        }

        lugarRepository.darDeBajaLugar(codigo);
    }

    // REACTIVACIÓN
    public void reactivarLugar(Integer codigo) {

        Lugar lugar = buscarLugarPorCodigo(codigo);

        if (lugar.getFechaBaja() == null) {
            throw new IllegalArgumentException(
                "El lugar ya se encuentra activo."
            );
        }

        validarDatos(lugar);

        lugarRepository.reactivar(codigo);
    }

    // CONSULTA POR COCHERA
    public List<Lugar> obtenerLugaresPorCochera(int codigoCochera) {
        return lugarRepository.obtenerPorCochera(codigoCochera);
    }
}
