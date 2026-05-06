package ar.gov.rosario.estacionamiento.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PrecioHistoricoTP {
	private TipoPlan tipoPlan;
	private LocalDateTime fechaDesde;
	private BigDecimal precio;
	
	public PrecioHistoricoTP() {
	}
	
	public PrecioHistoricoTP(TipoPlan tipoPlan, LocalDateTime fechaDesde, BigDecimal precio) {
		this.tipoPlan = tipoPlan;
		this.fechaDesde = fechaDesde;
		this.precio = precio;
	}
	
	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
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
