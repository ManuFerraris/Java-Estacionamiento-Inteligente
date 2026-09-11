package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name="pago_suscripcion")
public class PagoSuscripcion {
	
	// Nueva clave primaria simple y autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_pago_suscripcion")
    private Integer idPagoSuscripcion;
	
	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name="numero", referencedColumnName="numero", nullable=false),
		@JoinColumn(name="codigo", referencedColumnName="codigo", nullable=false),
		@JoinColumn(name="fecha_desde", referencedColumnName="fecha_desde", nullable=false)
	})
	private Suscripcion suscripcion;

	@Column(name="fecha_hora_emision", nullable=false)
	private LocalDateTime fechaHoraEmision;

	@Column(name="fecha_hora_pago")
	private LocalDateTime fechaHoraPago;
	
	@Column(name="monto", nullable=false, precision = 10, scale = 2)
	private BigDecimal monto;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_pago", columnDefinition = "VARCHAR(30)", nullable=true)
	private TipoPago tipoPago;
	
	@Enumerated(EnumType.STRING)
	@Column(name="estado", columnDefinition = "VARCHAR(30)", nullable=false)
	private EstadoPago estado;
	
    @Column(name = "id_mercado_pago", columnDefinition = "VARCHAR(255)", nullable = true)
    private String idTransaccionMp;
	
    public PagoSuscripcion() {
    }
	
	// Constructor completo para Queries nativas o posibles DTOs
    public PagoSuscripcion(Integer idPagoSusc, Suscripcion suscripcion,
    		LocalDateTime fechaHoraEmision, LocalDateTime fechaHoraPago, 
    		BigDecimal monto, TipoPago tipoPago, EstadoPago estado) {
    	this.idPagoSuscripcion = idPagoSusc;
    	this.suscripcion = suscripcion;
        this.fechaHoraEmision = fechaHoraEmision;
        this.fechaHoraPago = fechaHoraPago;
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.estado = estado;
    }
    
    // Constructor sin ID para crear pagos nuevos 
    public PagoSuscripcion(Suscripcion suscripcion, LocalDateTime fechaHoraEmision,
    		LocalDateTime fechaHoraPago, BigDecimal monto,
    		TipoPago tipoPago, EstadoPago estado) {
    	this.suscripcion = suscripcion;
        this.fechaHoraEmision = fechaHoraEmision;
        this.fechaHoraPago = fechaHoraPago;
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.estado = estado;
    }

    public Integer getId() {
		return idPagoSuscripcion;
	}

	public void setId(Integer idPagoSuscripcion) {
		this.idPagoSuscripcion = idPagoSuscripcion;
	}

	public Suscripcion getSuscripcion() {
		return suscripcion;
	}

	public void setSuscripcion(Suscripcion suscripcion) {
		this.suscripcion = suscripcion;
	}

	public LocalDateTime getFechaHoraEmision() {
		return fechaHoraEmision;
	}

	public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
		this.fechaHoraEmision = fechaHoraEmision;
	}

	public LocalDateTime getFechaHoraPago() {
		return fechaHoraPago;
	}

	public void setFechaHoraPago(LocalDateTime fechaHoraPago) {
		this.fechaHoraPago = fechaHoraPago;
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

	public String getIdTransaccionMp() {
		return idTransaccionMp;
	}

	public void setIdTransaccionMp(String idTransaccionMp) {
		this.idTransaccionMp = idTransaccionMp;
	}
}
