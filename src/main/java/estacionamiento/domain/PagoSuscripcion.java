package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import estacionamiento.domain.claves.PagoSuscripcionId;
import estacionamiento.domain.claves.SuscripcionId;

@Entity
@Table(name="pago_suscripcion")
public class PagoSuscripcion {
	
	@EmbeddedId
	private PagoSuscripcionId id;
	
	@ManyToOne
	@MapsId("suscripcionId") // Se vincula al atributo 'suscripcionId' dentro de PagoSuscripcionId
	@JoinColumns({
		@JoinColumn(name="numero", referencedColumnName="numero", nullable=false),
		@JoinColumn(name="codigo", referencedColumnName="codigo", nullable=false),
		@JoinColumn(name="fecha_desde", referencedColumnName="fecha_desde", nullable=false)
	})
	private Suscripcion suscripcion;

	// private LocalDateTime fechaHoraEmision

	@Column(name="fecha_hora_pago")
	private LocalDateTime fechaHoraPago;
	
	@Column(name="monto", nullable=false, precision = 10, scale = 2)
	private BigDecimal monto;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_pago", nullable=false)
	private TipoPago tipoPago;
	
	@Enumerated(EnumType.STRING)
	@Column(name="estado", nullable=false)
	private EstadoPago estado;
	
public PagoSuscripcion() {}
	
    public PagoSuscripcion(Suscripcion suscripcion, LocalDateTime fechaHoraEmision, LocalDateTime fechaHoraPago, 
    		BigDecimal monto, TipoPago tipoPago, EstadoPago estado) {
        this.suscripcion = suscripcion;
        //this.fechaHoraEmision = fechaHoraEmision;
        this.fechaHoraPago = fechaHoraPago;
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.estado = estado;
        
        // Se extiende el ID de la suscripción para inicializar el ID compuesto
     	SuscripcionId sId = (suscripcion != null && suscripcion.getId() != null) ? suscripcion.getId() : new SuscripcionId();
     	this.id = new PagoSuscripcionId(sId, fechaHoraEmision);
    }

    public PagoSuscripcionId getId() {
		return id;
	}

	public void setId(PagoSuscripcionId id) {
		this.id = id;
	}

	public Suscripcion getSuscripcion() {
		return suscripcion;
	}

	public void setSuscripcion(Suscripcion suscripcion) {
		this.suscripcion = suscripcion;
		if (this.id == null) {
			this.id = new PagoSuscripcionId();
		}
		if (suscripcion != null) {
			this.id.setSuscripcionId(suscripcion.getId());
		}
	}

	// Delegamos el getter al ID
	public LocalDateTime getFechaHoraEmision() {
		return (id != null) ? id.getFechaHoraEmision() : null;
	}

	// Delegamos el setter al ID
	public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
		if (this.id == null) {
			this.id = new PagoSuscripcionId();
		}
		this.id.setFechaHoraEmision(fechaHoraEmision);
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
}
