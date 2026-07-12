package estacionamiento.domain;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;


@Entity()
@Table(name="cochera")
public class Cochera {
	
	@Id
	@Column(name="codigo")
	private int codigo;
	
	@Column(name="nombre", nullable=false)
	private String nombre;
	
	@Column(name="direccion", nullable=false)
	private String direccion;
	
	@Column(name="descripcion", nullable=false)
	private String descripcion;
	
	@OneToMany(mappedBy = "cochera", targetEntity = Lugar.class)
	private List<Lugar> lugares;
	
	public Cochera() {}
	
	public Cochera(int cod, String nom, String direc, String desc) {
		this.codigo = cod;
		this.nombre = nom;
		this.direccion = direc;
		this.descripcion = desc;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
