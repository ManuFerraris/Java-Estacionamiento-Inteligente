package estacionamiento.domain;

public class Cochera {
	private int codigo;
	private String nombre;
	private String direccion;
	private String descripcion;
	
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
