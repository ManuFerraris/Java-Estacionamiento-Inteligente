package ar.gov.rosario.estacionamiento.domain;

public class Vehiculo {
	private String patente;
	private String descripcion;
	private TipoVehiculo tipoVehiculo;
	
	public Vehiculo() {
	}
	
	public Vehiculo(String patente, String descripcion, TipoVehiculo tipoVehiculo) {
		this.patente = patente;
        this.descripcion = descripcion;
        this.tipoVehiculo = tipoVehiculo;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	
}
