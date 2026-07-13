package estacionamiento.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import estacionamiento.domain.claves.HistoricoSalidasId;

@Entity
@Table(name = "historico_salidas")
@IdClass(HistoricoSalidasId.class)
public class HistoricoSalidas {

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "numero_tipo_estadia", referencedColumnName = "numero_tipo_estadia"),
        @JoinColumn(name = "numero_usuario", referencedColumnName = "numero_usuario"),
        @JoinColumn(name = "patente", referencedColumnName = "patente"),
        @JoinColumn(name = "fecha_desde", referencedColumnName = "fecha_desde")
    })
    private Reserva reserva;

    @Id
    @Column(name = "fecha_hora_salida_parcial")
    private LocalDateTime fechaHoraSalidaParcial;
    
    @Column(name = "fecha_hora_regreso_parcial")
    private LocalDateTime fechaHoraRegresoParcial;
    
    @Column(name = "fecha_hora_regreso_real")
    private LocalDateTime fechaHoraRegresoReal;

    public HistoricoSalidas() {}

    public HistoricoSalidas(Reserva reserva, LocalDateTime salida, LocalDateTime regParcial, LocalDateTime regReal) {
        this.reserva = reserva;
        this.fechaHoraSalidaParcial = salida;
        this.fechaHoraRegresoParcial = regParcial;
        this.fechaHoraRegresoReal = regReal;    
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