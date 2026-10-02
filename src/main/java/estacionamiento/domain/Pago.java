package estacionamiento.domain;

import java.time.LocalDateTime; 
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity //@Entity le indica a Hibernate que esta clase será una tabla en la base de datos.
@Table(name = "pago") //@Table Es opcional y permite elegir el nombre exacto de la tabla en MySQL.
public class Pago {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "numero")
	private Integer numero;
	
	@Column(name = "fecha_hora", nullable = false)
	private LocalDateTime fechaHora;
	
	@Column(name = "monto", nullable = false, precision = 10, scale = 2)
	private BigDecimal monto;
	
	
    @Enumerated(EnumType.STRING) // @Enumerated guarda el Enum como un texto plano en la base de datos en lugar de un número.
    @Column(name = "tipo_pago", columnDefinition = "VARCHAR(30)", nullable = true)
	private TipoPago tipoPago;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "VARCHAR(30)", nullable = false)
	private EstadoPago estado;
    
    @Column(name = "id_mercado_pago", columnDefinition = "VARCHAR(255)", nullable = true)
    private String idTransaccionMp;
	
	public Pago() {}
	
	public Pago(int num, LocalDateTime fh, BigDecimal monto, TipoPago tp, EstadoPago est) {
		this.numero = num;
		this.fechaHora = fh;
		this.monto = monto;
		this.tipoPago = tp;
		this.estado = est;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
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

	@Override
	public String toString() {
		return "Pago [numero=" + numero + ", fechaHora=" + fechaHora + ", monto=" + monto + ", tipoPago=" + tipoPago
				+ ", estado=" + estado + ", idTransaccionMp=" + idTransaccionMp + "]";
	}

	public String getIdTransaccionMp() {
		return idTransaccionMp;
	}

	public void setIdTransaccionMp(String idTransaccionMp) {
		this.idTransaccionMp = idTransaccionMp;
	}
	
}
