package estacionamiento.domain;

import java.time.LocalDateTime; 
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity //@Entity le indica a Hibernate que esta clase será una tabla en la base de datos.
@Table(name = "pago") //@Table Es opcional y permite elegir el nombre exacto de la tabla en MySQL.
public class Pago {
		
	@Id // Marca este atributo como la Clave Primaria (Primary Key).
	@Column(name = "numero")
	private int numero;
	
	@Column(name = "fecha_hora", nullable = false)
	private LocalDateTime fechaHora;
	
	@Column(name = "monto", nullable = false, precision = 10, scale = 2)
	private BigDecimal monto;
	
	
    @Enumerated(EnumType.STRING) // @Enumerated guarda el Enum como un texto plano en la base de datos en lugar de un número.
    @Column(name = "tipo_pago", nullable = false)
	private TipoPago tipoPago;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
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
