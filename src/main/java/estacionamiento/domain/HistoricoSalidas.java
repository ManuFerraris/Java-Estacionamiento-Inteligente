package estacionamiento.domain;

import java.time.LocalDateTime;

public class HistoricoSalidas {
    
    private Reserva reserva; 
    private LocalDateTime fechaHoraSalidaParcial;
    private LocalDateTime fechaHoraRegresoParcial;
    private LocalDateTime fechaHoraRegresoReal;

    public HistoricoSalidas() {
    }

    public HistoricoSalidas(Reserva reserva, LocalDateTime fechaHoraSalidaParcial, 
                            LocalDateTime fechaHoraRegresoParcial, LocalDateTime fechaHoraRegresoReal) {
        this.reserva = reserva;
        this.fechaHoraSalidaParcial = fechaHoraSalidaParcial;
        this.fechaHoraRegresoParcial = fechaHoraRegresoParcial;
        this.fechaHoraRegresoReal = fechaHoraRegresoReal;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public LocalDateTime getFechaHoraSalidaParcial() {
        return fechaHoraSalidaParcial;
    }

    public void setFechaHoraSalidaParcial(LocalDateTime fechaHoraSalidaParcial) {
        this.fechaHoraSalidaParcial = fechaHoraSalidaParcial;
    }

    public LocalDateTime getFechaHoraRegresoParcial() {
        return fechaHoraRegresoParcial;
    }

    public void setFechaHoraRegresoParcial(LocalDateTime fechaHoraRegresoParcial) {
        this.fechaHoraRegresoParcial = fechaHoraRegresoParcial;
    }

    public LocalDateTime getFechaHoraRegresoReal() {
        return fechaHoraRegresoReal;
    }

    public void setFechaHoraRegresoReal(LocalDateTime fechaHoraRegresoReal) {
        this.fechaHoraRegresoReal = fechaHoraRegresoReal;
    }
}