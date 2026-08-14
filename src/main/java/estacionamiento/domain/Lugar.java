package estacionamiento.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name="lugar")
public class Lugar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="codigo")
	private Integer codigo;
	
	@Column(name="descripcion", nullable = false)
	private String descripcion;
	
	@Column(name="numero_piso", nullable = false)
	private int numeroPiso;
	
	@ManyToOne
	@JoinColumn(name="codigo_cochera", nullable = false)
	private Cochera cochera;
	
	public Lugar() {
	}

	public Lugar(Integer cod, String desc, int numPi, Cochera nroCoch) {
		this.codigo = cod;
		this.descripcion = desc;
		this.numeroPiso = numPi;
		this.cochera = nroCoch;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getNumeroPiso() {
		return numeroPiso;
	}

	public void setNumeroPiso(int numeroPiso) {
		this.numeroPiso = numeroPiso;
	}

	public Cochera getCochera() {
		return cochera;
	}

	public void setCochera(Cochera cochera) {
		this.cochera = cochera;
	}
	
}