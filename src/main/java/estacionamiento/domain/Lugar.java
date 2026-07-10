package estacionamiento.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="lugar")
public class Lugar {
	
	@Id
	@Column(name="codigo")
	private int codigo;
	
	@Column(name="descripcion", nullable = false)
	private String descripcion;
	
	@Column(name="numero_piso", nullable = false)
	private String numeroPiso;
	
	@Column(name="codigo_cochera", nullable = false)
	private Cochera codigo_cochera;
	
	public Lugar() {}
	
	public Lugar(int cod, String desc, String numPi, Cochera nroCoch) {
		this.codigo = cod;
		this.descripcion = desc;
		this.numeroPiso = numPi;
		this.codigo_cochera = nroCoch;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumeroPiso() {
		return numeroPiso;
	}

	public void setNumeroPiso(String numeroPiso) {
		this.numeroPiso = numeroPiso;
	}

	public Cochera getCodigo_cochera() {
		return codigo_cochera;
	}

	public void setCodigo_cochera(Cochera codigo_cochera) {
		this.codigo_cochera = codigo_cochera;
	}
	
	
}