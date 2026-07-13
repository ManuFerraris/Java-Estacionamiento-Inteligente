package estacionamiento.domain.claves;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class HistoricoSalidasId implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReservaId reserva; 
    private LocalDateTime fechaHoraSalidaParcial;

    public HistoricoSalidasId() {}

    public HistoricoSalidasId(ReservaId reserva, LocalDateTime fechaHoraSalidaParcial) {
        this.reserva = reserva;
        this.fechaHoraSalidaParcial = fechaHoraSalidaParcial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricoSalidasId)) return false;
        HistoricoSalidasId that = (HistoricoSalidasId) o;
        return Objects.equals(reserva, that.reserva) && 
               Objects.equals(fechaHoraSalidaParcial, that.fechaHoraSalidaParcial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reserva, fechaHoraSalidaParcial);
    }
}