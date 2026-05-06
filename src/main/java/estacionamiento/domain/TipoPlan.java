package estacionamiento.domain;

public class TipoPlan {
	private int codigo;
	private String nombre;
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