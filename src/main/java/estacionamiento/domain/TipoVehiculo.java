package estacionamiento.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="tipo_vehiculo")
public class TipoVehiculo {
	
	@Id
	@Column(name = "numero")
	private int numero;
	
	@Column(name = "nombre")
	private String nombre;

	// "tipoVehiculo" es el nombre EXACTO de la variable dentro de PrecioHistoricoTV.
	@OneToMany(mappedBy = "tipoVehiculo", targetEntity = PrecioHistoricoTV.class)
	private List<PrecioHistoricoTV> preciosHistoricos;
		
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

	public List<PrecioHistoricoTV> getPreciosHistoricos() {
		return preciosHistoricos;
	}

	public void setPreciosHistoricos(List<PrecioHistoricoTV> preciosHistoricos) {
		this.preciosHistoricos = preciosHistoricos;
	}

}
