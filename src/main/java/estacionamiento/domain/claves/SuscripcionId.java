package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class SuscripcionId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "numero")
	private int numero; 

	@Column(name = "codigo")
	private int codigo;

	@Column(name = "fecha_desde")
	private LocalDateTime fechaDesde;

	public SuscripcionId() {}
	
	public SuscripcionId(int numeroUsuario, int codigoPlan, LocalDateTime fechaDesde) {
		this.numero = numeroUsuario;
		this.codigo = codigoPlan;
		this.fechaDesde = fechaDesde;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, fechaDesde, numero);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		SuscripcionId other = (SuscripcionId) obj;
		return codigo == other.codigo && 
		       Objects.equals(fechaDesde, other.fechaDesde) && 
		       numero == other.numero;
	}
}
