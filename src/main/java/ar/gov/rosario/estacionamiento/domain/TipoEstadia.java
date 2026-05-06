package ar.gov.rosario.estacionamiento.domain;

public class TipoEstadia {
	private int numero;
	private String descripcion;
	private int cupo;
	
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
