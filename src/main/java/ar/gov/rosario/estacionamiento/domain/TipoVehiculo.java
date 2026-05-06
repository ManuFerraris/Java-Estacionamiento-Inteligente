package ar.gov.rosario.estacionamiento.domain;

public class TipoVehiculo {
	private int numero;
	private String nombre;

	public TipoVehiculo() {
	}

	public TipoVehiculo(int numero, String nombre) {
		this.nombre = nombre;
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
