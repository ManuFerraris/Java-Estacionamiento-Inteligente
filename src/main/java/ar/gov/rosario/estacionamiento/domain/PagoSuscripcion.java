package ar.gov.rosario.estacionamiento.domain;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoSuscripcion {
	private Suscripcion suscripcion;
	private LocalDateTime fechaHoraEmision;
	private LocalDateTime fechaHoraPago;
	private BigDecimal monto;
	private TipoPago tipoPago;
	private EstadoPago estado;
	
public PagoSuscripcion() {}
	
    public PagoSuscripcion(Suscripcion suscripcion, LocalDateTime fechaHoraEmision, LocalDateTime fechaHoraPago, 
    		BigDecimal monto,TipoPago tipoPago, EstadoPago estado) {
        this.suscripcion = suscripcion;
        this.fechaHoraEmision = fechaHoraEmision;
        this.fechaHoraPago = fechaHoraPago;
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.estado = estado;
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
}
