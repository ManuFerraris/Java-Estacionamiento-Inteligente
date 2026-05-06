package ar.gov.rosario.estacionamiento.domain;
import java.time.LocalDateTime;

public class Beneficio {
	private TipoPlan tipoPlan;
	private int numero;
	private String descripcion;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaBaja;
	
	public Beneficio() {}
	
	public Beneficio(TipoPlan tipoPlan, int numero, String descripcion, LocalDateTime fechaCreacion, LocalDateTime fechaBaja) {
		this.tipoPlan = tipoPlan;
		this.numero = numero;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.fechaBaja = fechaBaja;
	}

	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
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

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDateTime fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
}
