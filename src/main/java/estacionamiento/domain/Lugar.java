package estacionamiento.domain;

public class Lugar {
	private int codigo;
	private String descripcion;
	private String numeroPiso;
	
	public Lugar() {}
	
	public Lugar(int cod, String desc, String numPi) {
		this.codigo = cod;
		this.descripcion = desc;
		this.numeroPiso = numPi;
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
	
	
}