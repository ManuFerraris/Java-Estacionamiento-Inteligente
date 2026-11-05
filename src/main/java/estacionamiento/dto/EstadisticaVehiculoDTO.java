
package estacionamiento.dto;

public class EstadisticaVehiculoDTO {

    private String tipoVehiculo;
    private Long cantidadReservas;

    public EstadisticaVehiculoDTO(String tipoVehiculo, Long cantidadReservas) {
        this.tipoVehiculo = tipoVehiculo;
        this.cantidadReservas = cantidadReservas;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Long getCantidadReservas() {
        return cantidadReservas;
    }

    public void setCantidadReservas(Long cantidadReservas) {
        this.cantidadReservas = cantidadReservas;
    }
}
