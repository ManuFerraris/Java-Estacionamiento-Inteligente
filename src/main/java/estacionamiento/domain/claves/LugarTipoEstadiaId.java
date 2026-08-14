package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class LugarTipoEstadiaId implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Column(name = "codigo_lugar")
    private int codigoLugar;
    
    @Column(name = "numero_tipo_estadia") 
    private int numeroTipoEstadia;
    
    @Column(name = "fecha_desde")
    private LocalDateTime fechaDesde;

    public LugarTipoEstadiaId() {}

    public LugarTipoEstadiaId(int codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        this.codigoLugar = codigoLugar;
        this.numeroTipoEstadia = numeroTipoEstadia;
        this.fechaDesde = fechaDesde;
    }

    public int getCodigoLugar() { return codigoLugar; }
    public void setCodigoLugar(int codigoLugar) { this.codigoLugar = codigoLugar; }

    public int getNumeroTipoEstadia() { return numeroTipoEstadia; }
    public void setNumeroTipoEstadia(int numeroTipoEstadia) { this.numeroTipoEstadia = numeroTipoEstadia; }

    public LocalDateTime getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDateTime fechaDesde) { this.fechaDesde = fechaDesde; }

    @Override
    public int hashCode() {
        return Objects.hash(codigoLugar, fechaDesde, numeroTipoEstadia);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LugarTipoEstadiaId other = (LugarTipoEstadiaId) obj;
        return codigoLugar == other.codigoLugar && 
               numeroTipoEstadia == other.numeroTipoEstadia && 
               Objects.equals(fechaDesde, other.fechaDesde);
    }
}
