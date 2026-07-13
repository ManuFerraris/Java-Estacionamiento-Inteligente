package estacionamiento.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name="lugar")
public class Lugar {
	
	@Id
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="descripcion", nullable = false)
	private String descripcion;
	
	@Column(name="numero_piso", nullable = false)
	private int numeroPiso;
	
	@ManyToOne
	@JoinColumn(name="codigo_cochera", nullable = false)
	private Cochera cochera;
	
	public Lugar() {}

	public Lugar(String cod, String desc, int numPi, Cochera nroCoch) {
		this.codigo = cod;
		this.descripcion = desc;
		this.numeroPiso = numPi;
		this.cochera = nroCoch;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
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

	public Cochera getCodigoCochera() {
		return cochera;
	}

	public void setCodigoCochera(Cochera codigo_cochera) {
		this.cochera = codigo_cochera;
	}
	
}