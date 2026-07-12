package estacionamiento.domain;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="vehiculo")
public class Vehiculo {
	
	@Id
	@Column(name="patente")
	private String patente;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="numero")
	private TipoVehiculo tipoVehiculo;
	
	@OneToMany(mappedBy = "vehiculo", targetEntity = Reserva.class)
	private List<Reserva> reservas;
	
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
