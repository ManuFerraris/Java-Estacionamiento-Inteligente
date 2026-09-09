package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import estacionamiento.domain.claves.PrecioHistoricoTVId;
import java.math.BigDecimal; 

@Entity
@Table(name="precio_historicotv")
public class PrecioHistoricoTV {
	
	@EmbeddedId
    private PrecioHistoricoTVId id;
	
	@ManyToOne
	@MapsId("numeroTipoVehiculo")
	@JoinColumn(name="numero", nullable=false)
	private TipoVehiculo tipoVehiculo;
	
	@Column(name="precio", nullable=false, precision = 10, scale = 2)
	private BigDecimal precio;
	
	public PrecioHistoricoTV() {
	}
	
	public PrecioHistoricoTV(TipoVehiculo tipoVehiculo, LocalDateTime fechaDesde, BigDecimal precio) {
		this.tipoVehiculo = tipoVehiculo;
		this.precio = precio;
		//this.fechaDesde = fechaDesde;
		int numero = (tipoVehiculo != null) ? tipoVehiculo.getNumero() : 0;
		this.id = new PrecioHistoricoTVId(numero, fechaDesde);
	}
	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
	    this.tipoVehiculo = tipoVehiculo;

	    if (this.id == null) {
	        this.id = new PrecioHistoricoTVId();
	    }

	    if (tipoVehiculo != null) {
	        this.id.setNumeroTipoVehiculo(tipoVehiculo.getNumero());
	    }
	}
	public LocalDateTime getFechaDesde() {
	    return (id != null) ? id.getFechaDesde() : null;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
	    if (this.id == null) {
	        this.id = new PrecioHistoricoTVId();
	    }

	    this.id.setFechaDesde(fechaDesde);
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	
	public PrecioHistoricoTVId getId() {
        return id;
    }

    public void setId(PrecioHistoricoTVId id) {
        this.id = id;
    }
    
}