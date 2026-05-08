package estacionamiento.domain;

import java.time.LocalDateTime; 
import java.math.BigDecimal;

public class Pago {
	private int numero;
	private LocalDateTime fechaHora;
	private BigDecimal monto;
	private TipoPago tipoPago;
	private EstadoPago estado;
	
	public Pago() {}
	
	public Pago(int num, LocalDateTime fh, BigDecimal monto, TipoPago tp, EstadoPago est) {
		this.numero = num;
		this.fechaHora = fh;
		this.monto = monto;
		this.tipoPago = tp;
		this.estado = est;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public EstadoPago getEstado() {
		return estado;
	}

	public void setEstado(EstadoPago estado) {
		this.estado = estado;
	}
	
	
}
