package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.dto.EstadisticaVehiculoDTO;
import estacionamiento.dto.EstadisticaEstadoDTO;
import estacionamiento.dto.EstadisticaResumenDTO;
import estacionamiento.repository.EstadisticaRepository;
import estacionamiento.repository.mysql.EstadisticaRepositoryMySQL;

public class EstadisticaService {

    private EstadisticaRepository repository;

    public EstadisticaService() {
        this.repository = new EstadisticaRepositoryMySQL();
    }

    public List<EstadisticaVehiculoDTO> obtenerReservasPorTipoVehiculo(
            LocalDateTime desde, LocalDateTime hasta, String estado) {

        validarFechas(desde, hasta);

        return repository.obtenerReservasPorTipoVehiculo(desde, hasta, estado);
    }

    public List<EstadisticaEstadoDTO> obtenerReservasPorEstado(
            LocalDateTime desde, LocalDateTime hasta, String tipoVehiculo) {

        validarFechas(desde, hasta);

        return repository.obtenerReservasPorEstado(desde, hasta, tipoVehiculo);
    }

    public EstadisticaResumenDTO obtenerResumen(
            LocalDateTime desde, LocalDateTime hasta,
            String tipoVehiculo, String estado) {

        validarFechas(desde, hasta);

        return repository.obtenerResumen(desde, hasta, tipoVehiculo, estado);
    }

    private void validarFechas(LocalDateTime desde, LocalDateTime hasta) {

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha desde no puede ser posterior a la fecha hasta.");
        }
    }
}