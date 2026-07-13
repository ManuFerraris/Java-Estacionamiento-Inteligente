package estacionamiento.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import estacionamiento.domain.claves.LugarTipoEstadiaId;

@Entity
@Table(name="lugar_tipo_estadia")
@IdClass(LugarTipoEstadiaId.class) 
public class LugarTipoEstadia {
    
    @Id
    @ManyToOne
    @JoinColumn(name="codigo_lugar", nullable = false)
    private Lugar lugar;

    @Id
    @ManyToOne
    @JoinColumn(name="numero", nullable = false) 
    private TipoEstadia tipoEstadia;

    @Id
    @Column(name="fecha_desde", nullable = false)
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