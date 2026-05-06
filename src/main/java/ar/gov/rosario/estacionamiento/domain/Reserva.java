package ar.gov.rosario.estacionamiento.domain;
import java.time.LocalDateTime; // Clase moderna para fechas y horas
import java.math.BigDecimal; // Clase estandar para el dinero

public class Reserva {
	private Vehiculo vehiculo;
	private Usuario usuario;
	private TipoEstadia tipoEstadia;
	private LocalDateTime fechaDesde;
	private LocalDateTime fechaHastaTentativa;
	private LocalDateTime fechaHastaReal;
	private EstadoReserva estado;
	private Pago pago;
	private Lugar lugar;
	private BigDecimal senia;
	
	public Reserva() {}
	
	public Reserva(Vehiculo vehiculo, Usuario usuario, TipoEstadia tipoEstadia, LocalDateTime fechaDesde, LocalDateTime fechaHastaTentativa, 
	            LocalDateTime fechaHastaReal, EstadoReserva estado, BigDecimal senia, Pago pago, Lugar lugar) {
		this.vehiculo = vehiculo;
		this.usuario = usuario;
		this.tipoEstadia = tipoEstadia;
		this.fechaDesde = fechaDesde;
		this.fechaHastaTentativa = fechaHastaTentativa;
		this.fechaHastaReal = fechaHastaReal;
		this.estado = estado;
		this.senia = senia;
		this.pago = pago;
		this.lugar = lugar;
	}
	
	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public Lugar getLugar() {
		return lugar;
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoEstadia getTipoEstadia() {
		return tipoEstadia;
	}

	public void setTipoEstadia(TipoEstadia tipoEstadia) {
		this.tipoEstadia = tipoEstadia;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public LocalDateTime getFechaHastaTentativa() {
		return fechaHastaTentativa;
	}

	public void setFechaHastaTentativa(LocalDateTime fechaHastaTentativa) {
		this.fechaHastaTentativa = fechaHastaTentativa;
	}

	public LocalDateTime getFechaHastaReal() {
		return fechaHastaReal;
	}

	public void setFechaHastaReal(LocalDateTime fechaHastaReal) {
		this.fechaHastaReal = fechaHastaReal;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public BigDecimal getSenia() {
		return senia;
	}

	public void setSenia(BigDecimal senia) {
		this.senia = senia;
	}
}
