package estacionamiento.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import estacionamiento.domain.claves.PrecioHistoricoTPId;

@Entity
@Table(name="precio_historicotp")
public class PrecioHistoricoTP {
	
	@EmbeddedId
	private PrecioHistoricoTPId id;
	
	@ManyToOne
	@MapsId("codigoPlan") // Vincula este objeto al atributo 'codigoPlan' del PrecioHistoricoTPId
	@JoinColumn(name="codigo_plan", nullable=false)
	private TipoPlan tipoPlan;
	
	//private LocalDateTime fechaDesde; Se manda en el constructor y se asigna en la clase Id.
	
	@Column(name="precio", nullable=false, precision = 10, scale = 2)
	private BigDecimal precio;
	
	public PrecioHistoricoTP() {
	}
	
	public PrecioHistoricoTP(TipoPlan tipoPlan, LocalDateTime fechaDesde, BigDecimal precio) {
		this.tipoPlan = tipoPlan;
		//this.fechaDesde = fechaDesde;
		this.precio = precio;
		int codigo = (tipoPlan != null) ? tipoPlan.getCodigo() : 0;
		this.id = new PrecioHistoricoTPId(codigo, fechaDesde);
	}
	
	public PrecioHistoricoTPId getId() {
		return id;
	}

	public void setId(PrecioHistoricoTPId id) {
		this.id = id;
	}

	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
		if (this.id == null) {
			this.id = new PrecioHistoricoTPId();
		}
		if (tipoPlan != null) {
			this.id.setCodigoPlan(tipoPlan.getCodigo());
		}
	}

	public LocalDateTime getFechaDesde() {
		return (id != null) ? id.getFechaDesde() : null;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		if (this.id == null) {
			this.id = new PrecioHistoricoTPId();
		}
		this.id.setFechaDesde(fechaDesde);
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
}
