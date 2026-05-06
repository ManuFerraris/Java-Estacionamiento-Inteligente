package estacionamiento.domain;
import java.sql.Date;

public class Pago {
	private int numero;
	private Date fechaHora;
	private float monto;
	private String tipoPago;
	private String estado;
	
	public Pago() {}
	
	public Pago(int num, Date fh, float monto, String tp, String est) {
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

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
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
