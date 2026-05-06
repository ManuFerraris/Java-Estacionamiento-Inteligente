package estacionamiento.domain;

import java.time.LocalDateTime;

public class LugarTipoEstadia {
    
    private Lugar lugar;
    private TipoEstadia tipoEstadia;
    private LocalDateTime fechaDesde;

    public LugarTipoEstadia() {
    }

    public LugarTipoEstadia(Lugar lugar, TipoEstadia tipoEstadia, LocalDateTime fechaDesde) {
        this.lugar = lugar;
        this.tipoEstadia = tipoEstadia;
        this.fechaDesde = fechaDesde;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public TipoEstadia getTipoEstadia() {
        return tipoEstadia;
    }

    public void setTipoEstadia(TipoEstadia tipoEstadia) {
        this.tipoEstadia = tipoEstadia;
    }

    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
}