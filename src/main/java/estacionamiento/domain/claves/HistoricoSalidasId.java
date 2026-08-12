package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class HistoricoSalidasId implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReservaId reservaId; 

    @Column(name = "fecha_hora_salida_parcial", insertable=false, updatable=false)
    private LocalDateTime fechaHoraSalidaParcial;

    public HistoricoSalidasId() {}

    public HistoricoSalidasId(ReservaId reservaId, LocalDateTime fechaHoraSalidaParcial) {
        this.reservaId = reservaId;
        this.fechaHoraSalidaParcial = fechaHoraSalidaParcial;
    }

    public ReservaId getReservaId() {
        return reservaId;
    }

    public void setReservaId(ReservaId reservaId) {
        this.reservaId = reservaId;
    }

    public LocalDateTime getFechaHoraSalidaParcial() {
        return fechaHoraSalidaParcial;
    }

    public void setFechaHoraSalidaParcial(LocalDateTime fechaHoraSalidaParcial) {
        this.fechaHoraSalidaParcial = fechaHoraSalidaParcial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoSalidasId that = (HistoricoSalidasId) o;
        return Objects.equals(reservaId, that.reservaId) && 
               Objects.equals(fechaHoraSalidaParcial, that.fechaHoraSalidaParcial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservaId, fechaHoraSalidaParcial);
    }
}