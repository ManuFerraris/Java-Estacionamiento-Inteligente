package estacionamiento.domain;

import java.time.LocalDateTime; 
import java.math.BigDecimal;

public class Pago {
	private int numero;
	private LocalDateTime fechaHora;
	private BigDecimal monto;
	private String tipoPago;
	private String estado;
	
	public Pago() {}
	
	public Pago(int num, LocalDateTime fh, BigDecimal monto, String tp, String est) {
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

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
