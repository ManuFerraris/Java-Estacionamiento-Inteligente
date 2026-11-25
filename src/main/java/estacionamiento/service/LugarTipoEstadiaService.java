
package estacionamiento.service;

import estacionamiento.domain.Lugar;
import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.LugarTipoEstadiaRepository;
import estacionamiento.repository.LugarRepository;
import estacionamiento.repository.TipoEstadiaRepository;
import estacionamiento.repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.List;

public class LugarTipoEstadiaService {

    private final LugarTipoEstadiaRepository repository;
    private final LugarRepository lugarRepository;
    private final TipoEstadiaRepository tipoEstadiaRepository;
    private final ReservaRepository reservaRepository;

    public LugarTipoEstadiaService(
            LugarTipoEstadiaRepository repository,
            LugarRepository lugarRepository,
            TipoEstadiaRepository tipoEstadiaRepository,
            ReservaRepository reservaRepository) {

        this.repository = repository;
        this.lugarRepository = lugarRepository;
        this.tipoEstadiaRepository = tipoEstadiaRepository;
        this.reservaRepository = reservaRepository;
    }

    public LugarTipoEstadia asignarTipoEstadiaALugar(
            int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {

        if (fechaDesde == null) {
            throw new IllegalArgumentException("La fecha desde es obligatoria.");
        }

        Lugar lugar = lugarRepository.buscarPorClave(codigoLugar);

        if (lugar == null) {
            throw new IllegalArgumentException("El lugar no existe.");
        }

        if (lugar.getFechaBaja() != null) {
            throw new IllegalArgumentException("No se puede asignar un tipo de estadía a un lugar inactivo.");
        }

        TipoEstadia tipoEstadia = tipoEstadiaRepository.buscarPorNumero(numeroTipoEstadia);

        if (tipoEstadia == null) {
            throw new IllegalArgumentException("El tipo de estadía no existe.");
        }

        if (repository.existeAsignacionVigente(codigoLugar, numeroTipoEstadia)) {
            throw new IllegalArgumentException("Ya existe una asignación vigente para ese lugar y tipo de estadía.");
        }

        LugarTipoEstadia nuevaAsignacion =
                new LugarTipoEstadia(lugar, tipoEstadia, fechaDesde);

        return repository.save(nuevaAsignacion);
    }

    public List<LugarTipoEstadia> obtenerTodos() {
        return repository.findAll();
    }

    public LugarTipoEstadia buscarPorId(
            int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) throws Exception {

        LugarTipoEstadia registro =
                repository.findById(codigoLugar, numeroTipoEstadia, fechaDesde);

        if (registro == null) {
            throw new Exception("No se encontró la asignación solicitada.");
        }

        return registro;
    }

    public void darDeBaja(
            int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {

        LugarTipoEstadia registro;

        try {
            registro = buscarPorId(codigoLugar, numeroTipoEstadia, fechaDesde);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (registro.getFechaHasta() != null) {
            throw new IllegalArgumentException("La asignación ya está dada de baja.");
        }

        if (reservaRepository.tieneReservasActivasPorLugarYTipoEstadia(
                codigoLugar, numeroTipoEstadia)) {
            throw new IllegalArgumentException(
                    "No se puede dar de baja: existen reservas activas para ese lugar y tipo de estadía.");
        }

        repository.darDeBaja(codigoLugar, numeroTipoEstadia, fechaDesde);
    }
    public List<LugarTipoEstadia> obtenerPorLugar(int codigoLugar) {
        return repository.findByLugar(codigoLugar);
    }
}
