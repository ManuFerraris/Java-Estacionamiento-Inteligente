package estacionamiento.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="tipo_plan")
public class TipoPlan {
	
	@Id
	@Column(name="codigo")
	private int codigo;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@Column(name="detalle", nullable = false)
	private String detalle;

	public TipoPlan() {
	}
	
	public TipoPlan(int cod, String nombre, String det) {
		this.nombre = nombre;
		this.codigo = cod;
		this.detalle = det;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDetalle() {
		return detalle;
	}
	
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
}