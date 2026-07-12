package estacionamiento.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

@Entity
@Table(name="tipo_estadia")
public class TipoEstadia {
	
	@Id
	@Column(name="numero")
	private int numero;
	
	@Column(name="descripcion", nullable = false)
	private String descripcion;
	
	@Column(name="cupo", nullable = false)
	private int cupo;
	
	@OneToMany(mappedBy = "tipoEstadia", targetEntity = Reserva.class)
	private List<Reserva> reservas;
	
	public TipoEstadia(){}
	
	public TipoEstadia(int numero, String descripcion, int cupo){
		this.numero = numero;
		this.descripcion = descripcion;
		this.cupo = cupo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCupo() {
		return cupo;
	}

	public void setCupo(int cupo) {
		this.cupo = cupo;
	}
}
