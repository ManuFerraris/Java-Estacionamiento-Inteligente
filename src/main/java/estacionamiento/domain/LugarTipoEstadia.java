package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import estacionamiento.domain.claves.LugarTipoEstadiaId;

@Entity
@Table(name="lugar_tipo_estadia")
public class LugarTipoEstadia {
	
	@EmbeddedId
    private LugarTipoEstadiaId id;
	@Column(name = "fecha_hasta")
	private LocalDateTime fechaHasta;
    
	@ManyToOne
    @MapsId("codigoLugar")
    @JoinColumn(name="codigo_lugar", nullable = false)
    private Lugar lugar;

	@ManyToOne
    @MapsId("numeroTipoEstadia")
    @JoinColumn(name="numero_tipo_estadia", nullable = false)
    private TipoEstadia tipoEstadia;

    public LugarTipoEstadia() {
    }

    public LugarTipoEstadia(Lugar lugar, TipoEstadia tipoEstadia, LocalDateTime fechaDesde) {
        if (lugar == null || tipoEstadia == null || fechaDesde == null) {
            throw new IllegalArgumentException("Los datos de la asignación son obligatorios.");
        }

        this.lugar = lugar;
        this.tipoEstadia = tipoEstadia;

        this.id = new LugarTipoEstadiaId(
            lugar.getCodigo(),
            tipoEstadia.getNumero(),
            fechaDesde
        );

        this.fechaHasta = null;
    }

    public LugarTipoEstadiaId getId() { return id; }
    public void setId(LugarTipoEstadiaId id) { this.id = id; }
    
    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Lugar getLugar() { return lugar; }
    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
        if (this.id == null) this.id = new LugarTipoEstadiaId();
        if (lugar != null) this.id.setCodigoLugar(lugar.getCodigo());
    }

    public TipoEstadia getTipoEstadia() { return tipoEstadia; }
    public void setTipoEstadia(TipoEstadia tipoEstadia) {
        this.tipoEstadia = tipoEstadia;
        if (this.id == null) this.id = new LugarTipoEstadiaId();
        if (tipoEstadia != null) this.id.setNumeroTipoEstadia(tipoEstadia.getNumero());
    }

    // Delegamos Getter y Setter al ID compuesto
    public LocalDateTime getFechaDesde() {
        return (id != null) ? id.getFechaDesde() : null;
    }
    public void setFechaDesde(LocalDateTime fechaDesde) {
        if (this.id == null) this.id = new LugarTipoEstadiaId();
        this.id.setFechaDesde(fechaDesde);
    }
}