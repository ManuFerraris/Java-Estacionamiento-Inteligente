package estacionamiento.dto;

import estacionamiento.domain.EstadoReserva;

public class EstadisticaEstadoDTO {

    private String estado;
    private Long cantidadReservas;

    public EstadisticaEstadoDTO(EstadoReserva estado, Long cantidadReservas) {
        this.estado = estado.name();
        this.cantidadReservas = cantidadReservas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getCantidadReservas() {
        return cantidadReservas;
    }

    public void setCantidadReservas(Long cantidadReservas) {
        this.cantidadReservas = cantidadReservas;
    }
}