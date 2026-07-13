package estacionamiento.domain.claves;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class LugarTipoEstadiaId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String lugar;         
    private int tipoEstadia;       
    private LocalDateTime fechaDesde;

    public LugarTipoEstadiaId() {}

    public LugarTipoEstadiaId(String lugar, int tipoEstadia, LocalDateTime fechaDesde) {
        this.lugar = lugar;
        this.tipoEstadia = tipoEstadia;
        this.fechaDesde = fechaDesde;
    }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public int getTipoEstadia() { return tipoEstadia; }
    public void setTipoEstadia(int tipoEstadia) { this.tipoEstadia = tipoEstadia; }

    public LocalDateTime getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDateTime fechaDesde) { this.fechaDesde = fechaDesde; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LugarTipoEstadiaId that = (LugarTipoEstadiaId) o;
        return tipoEstadia == that.tipoEstadia &&
               Objects.equals(lugar, that.lugar) &&
               Objects.equals(fechaDesde, that.fechaDesde);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lugar, tipoEstadia, fechaDesde);
    }
}