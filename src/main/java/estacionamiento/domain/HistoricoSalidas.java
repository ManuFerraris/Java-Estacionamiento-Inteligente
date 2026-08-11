package estacionamiento.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import estacionamiento.domain.claves.HistoricoSalidasId;
import estacionamiento.domain.claves.ReservaId;

@Entity
@Table(name = "historico_salidas")
public class HistoricoSalidas {

	@EmbeddedId
    private HistoricoSalidasId id;
	
	@ManyToOne
    @MapsId("reservaId")
    @JoinColumns({
        @JoinColumn(name = "numero_tipo_estadia", referencedColumnName = "numero_tipo_estadia"),
        @JoinColumn(name = "numero_usuario", referencedColumnName = "numero_usuario"),
        @JoinColumn(name = "patente", referencedColumnName = "patente"),
        @JoinColumn(name = "fecha_desde", referencedColumnName = "fecha_desde")
    })
    private Reserva reserva;

	@Column(name = "fecha_hora_regreso_parcial")
    private LocalDateTime fechaHoraRegresoParcial;
    
    @Column(name = "fecha_hora_regreso_real")
    private LocalDateTime fechaHoraRegresoReal;

    public HistoricoSalidas() {}

    public HistoricoSalidas(Reserva reserva, LocalDateTime fechaHoraSalidaParcial, 
                            LocalDateTime fechaHoraRegresoParcial, LocalDateTime fechaHoraRegresoReal) {
        
        this.reserva = reserva;
        this.fechaHoraRegresoParcial = fechaHoraRegresoParcial;
        this.fechaHoraRegresoReal = fechaHoraRegresoReal;
        ReservaId rId = (reserva != null && reserva.getId() != null) ? reserva.getId() : new ReservaId();
        this.id = new HistoricoSalidasId(rId, fechaHoraSalidaParcial);
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
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
    
    public HistoricoSalidasId getId() {
        return id;
    }

    public void setId(HistoricoSalidasId id) {
        this.id = id;
    }

    // Delegación para facilitar el acceso a la fecha
    public LocalDateTime getFechaHoraSalidaParcial() {
        return (id != null) ? id.getFechaHoraSalidaParcial() : null;
    }

    public void setFechaHoraSalidaParcial(LocalDateTime fechaHoraSalidaParcial) {
        if (this.id == null) {
            this.id = new HistoricoSalidasId();
        }
        this.id.setFechaHoraSalidaParcial(fechaHoraSalidaParcial);
    }
}