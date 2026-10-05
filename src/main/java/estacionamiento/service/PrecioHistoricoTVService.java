package estacionamiento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.PrecioHistoricoTVRepository;
import estacionamiento.repository.TipoVehiculoRepository;

public class PrecioHistoricoTVService {
    
    private PrecioHistoricoTVRepository precioRepo;
    private TipoVehiculoRepository tipoVehiculoRepo;

    public PrecioHistoricoTVService( PrecioHistoricoTVRepository phTVRepo, TipoVehiculoRepository tvRepo) {
        this.precioRepo = phTVRepo;
        this.tipoVehiculoRepo = tvRepo;
    }

    public List<PrecioHistoricoTV> obtenerTodosOrdenadosPorFechaDesc() {
        return precioRepo.obtenerTodos();
    }

    public void registrarNuevoPrecio(Integer numeroTipoVehiculo, BigDecimal precioValor) {
        if (precioValor == null || precioValor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        TipoVehiculo tv = tipoVehiculoRepo.buscarPorClave(numeroTipoVehiculo);
        if (tv == null) {
            throw new IllegalArgumentException("El tipo de vehículo seleccionado no existe.");
        }

        PrecioHistoricoTV nuevoPrecio = new PrecioHistoricoTV();
        nuevoPrecio.setTipoVehiculo(tv);
        nuevoPrecio.setPrecio(precioValor);
        nuevoPrecio.getId().setFechaDesde(LocalDateTime.now());
        nuevoPrecio.getId().setNumeroTipoVehiculo(numeroTipoVehiculo);
        precioRepo.guardar(nuevoPrecio);
    }

    public void actualizarPrecio(Integer numeroTipoVehiculo, LocalDateTime fechaDesde, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        PrecioHistoricoTV precioHistorico = precioRepo.buscarPorClave(numeroTipoVehiculo, fechaDesde);
        if (precioHistorico == null) {
            throw new IllegalArgumentException("El registro histórico que intentas editar no existe.");
        }

        precioHistorico.setPrecio(nuevoPrecio);
        precioRepo.actualizar(precioHistorico); 
    }

    public void eliminarPrecioFisico(Integer numeroTipoVehiculo, LocalDateTime fechaDesde) {
        PrecioHistoricoTV precioHistorico = precioRepo.buscarPorClave(numeroTipoVehiculo, fechaDesde);
        if (precioHistorico == null) {
            throw new IllegalArgumentException("El registro ya no existe en la base de datos.");
        }

        precioRepo.eliminar(precioHistorico);
    }
}