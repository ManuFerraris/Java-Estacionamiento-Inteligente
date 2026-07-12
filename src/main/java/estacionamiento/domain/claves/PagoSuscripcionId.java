package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class PagoSuscripcionId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Anidamos el ID de la entidad a la que depende que es suscripcion (la agregacion).
	private SuscripcionId suscripcionId;

	@Column(name = "fecha_hora_emision")
	private LocalDateTime fechaHoraEmision;

	public PagoSuscripcionId() {}

	public PagoSuscripcionId(SuscripcionId suscripcionId, LocalDateTime fechaHoraEmision) {
		this.suscripcionId = suscripcionId;
		this.fechaHoraEmision = fechaHoraEmision;
	}

	public SuscripcionId getSuscripcionId() {
		return suscripcionId;
	}

	public void setSuscripcionId(SuscripcionId suscripcionId) {
		this.suscripcionId = suscripcionId;
	}

	public LocalDateTime getFechaHoraEmision() {
		return fechaHoraEmision;
	}

	public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
		this.fechaHoraEmision = fechaHoraEmision;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fechaHoraEmision, suscripcionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		PagoSuscripcionId other = (PagoSuscripcionId) obj;
		return Objects.equals(fechaHoraEmision, other.fechaHoraEmision) && 
		       Objects.equals(suscripcionId, other.suscripcionId);
	}
}
