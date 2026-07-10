package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import estacionamiento.domain.claves.PrecioHistoricoTVId;
import java.math.BigDecimal; 

@Entity
//@IdClass(PrecioHistoricoTVId.class)
@Table(name="precio_historicotv")
public class PrecioHistoricoTV {
	
	@EmbeddedId
    private PrecioHistoricoTVId id;
	
	//@Id
	@ManyToOne
	@MapsId("numeroTipoVehiculo")
	@JoinColumn(name="numero", nullable=false)
	private TipoVehiculo tipoVehiculo;
	
	//@Id
	@Column(name="fecha_desde", nullable=false)
	private LocalDateTime fechaDesde;
	
	@Column(name="precio", nullable=false, precision = 10, scale = 2)
	private BigDecimal precio;
	
	public PrecioHistoricoTV() {
	}
	
	public PrecioHistoricoTV(TipoVehiculo tipoVehiculo, LocalDateTime fechaDesde, BigDecimal precio) {
		this.tipoVehiculo = tipoVehiculo;
		this.fechaDesde = fechaDesde;
		this.precio = precio;
	}
	
	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
}