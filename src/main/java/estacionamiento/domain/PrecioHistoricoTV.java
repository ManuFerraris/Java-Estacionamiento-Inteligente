package estacionamiento.domain;

import java.time.LocalDateTime; 
import java.math.BigDecimal; 

public class PrecioHistoricoTV {
	private TipoVehiculo tipoVehiculo;
	private LocalDateTime fechaDesde;
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