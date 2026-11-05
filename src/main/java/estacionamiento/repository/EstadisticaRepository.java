package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.dto.EstadisticaVehiculoDTO;
import estacionamiento.dto.EstadisticaEstadoDTO;
import estacionamiento.dto.EstadisticaResumenDTO;

public interface EstadisticaRepository {

    List<EstadisticaVehiculoDTO> obtenerReservasPorTipoVehiculo(
            LocalDateTime desde,
            LocalDateTime hasta,
            String estado
    );

    List<EstadisticaEstadoDTO> obtenerReservasPorEstado(
            LocalDateTime desde,
            LocalDateTime hasta,
            String tipoVehiculo
    );

    EstadisticaResumenDTO obtenerResumen(
            LocalDateTime desde,
            LocalDateTime hasta,
            String tipoVehiculo,
            String estado
    );
}