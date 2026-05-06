package estacionamiento.domain;
import java.time.LocalDateTime;

public class Suscripcion {
	private TipoPlan tipoPlan;
	private Usuario usuario;
	private LocalDateTime fechaDesde;
	private LocalDateTime fechaHasta;
	
	public Suscripcion() {}
	
	public Suscripcion(TipoPlan tipoPlan, Usuario usuario, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		this.tipoPlan = tipoPlan;
		this.usuario = usuario;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
	}

	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public LocalDateTime getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(LocalDateTime fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
}