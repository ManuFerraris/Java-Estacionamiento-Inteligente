package estacionamiento.dto;

public class EstadisticaResumenDTO {

    private Long totalReservas;
    private Long reservasFinalizadas;
    private Long reservasCanceladas;
    private Long reservasPendientes;
    private Long reservasNoPresentadas;
    private Double ratioConcrecion;

    public EstadisticaResumenDTO(Long totalReservas,
                                 Long reservasFinalizadas,
                                 Long reservasCanceladas,
                                 Long reservasPendientes,
                                 Long reservasNoPresentadas,
                                 Double ratioConcrecion) {
        this.totalReservas = totalReservas;
        this.reservasFinalizadas = reservasFinalizadas;
        this.reservasCanceladas = reservasCanceladas;
        this.reservasPendientes = reservasPendientes;
        this.reservasNoPresentadas = reservasNoPresentadas;
        this.ratioConcrecion = ratioConcrecion;
    }

    public Long getTotalReservas() {
        return totalReservas;
    }

    public Long getReservasFinalizadas() {
        return reservasFinalizadas;
    }

    public Long getReservasCanceladas() {
        return reservasCanceladas;
    }

    public Long getReservasPendientes() {
        return reservasPendientes;
    }

    public Long getReservasNoPresentadas() {
        return reservasNoPresentadas;
    }

    public Double getRatioConcrecion() {
        return ratioConcrecion;
    }
}