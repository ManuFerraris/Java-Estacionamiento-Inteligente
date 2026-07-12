package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BeneficioId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo")
	private int codigoPlan;

	@Column(name = "numero")
	private int numero;

	public BeneficioId() {}

	public BeneficioId(int codigoPlan, int numero) {
		this.codigoPlan = codigoPlan;
		this.numero = numero;
	}

	public int getCodigoPlan() {
		return codigoPlan;
	}

	public void setCodigoPlan(int codigoPlan) {
		this.codigoPlan = codigoPlan;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoPlan, numero);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		BeneficioId other = (BeneficioId) obj;
		return codigoPlan == other.codigoPlan && numero == other.numero;
	}
}