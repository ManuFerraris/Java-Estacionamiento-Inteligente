package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class PrecioHistoricoTPId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo")
	private int codigoPlan;

	@Column(name = "fecha_desde")
	private LocalDateTime fechaDesde;

	public PrecioHistoricoTPId() {}

	public PrecioHistoricoTPId(int codigoPlan, LocalDateTime fechaDesde) {
		this.codigoPlan = codigoPlan;
		this.fechaDesde = fechaDesde;
	}

	public int getCodigoPlan() {
		return codigoPlan;
	}

	public void setCodigoPlan(int codigoPlan) {
		this.codigoPlan = codigoPlan;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoPlan, fechaDesde);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		PrecioHistoricoTPId other = (PrecioHistoricoTPId) obj;
		return codigoPlan == other.codigoPlan && 
		       Objects.equals(fechaDesde, other.fechaDesde);
	}
}