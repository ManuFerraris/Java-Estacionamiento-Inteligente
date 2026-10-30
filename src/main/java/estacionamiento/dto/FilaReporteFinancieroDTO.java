package estacionamiento.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import estacionamiento.domain.EstadoPago;

public class FilaReporteFinancieroDTO {
	private LocalDateTime fechaOperacion;
    private String origenIngreso;
    private String concepto;      // Ej: "Reserva #125 - Cochera 12" o "Suscripción Plan Premium - Usuario x"
    private BigDecimal monto;
    private String estadoPago;    // "Aprobado", "Pendiente", etc.

    public FilaReporteFinancieroDTO(LocalDateTime fechaOperacion, String origenIngreso, String concepto, BigDecimal monto, EstadoPago estadoPago) {
        this.fechaOperacion = fechaOperacion;
        this.origenIngreso = origenIngreso;
        this.concepto = concepto;
        this.monto = monto;
        this.estadoPago = estadoPago != null ? estadoPago.name() : "DESCONOCIDO";
    }

    public LocalDateTime getFechaOperacion() { return fechaOperacion; }
    public String getOrigenIngreso() { return origenIngreso; }
    public String getConcepto() { return concepto; }
    public BigDecimal getMonto() { return monto; }
    public String getEstadoPago() { return estadoPago; }

	public void setFechaOperacion(LocalDateTime fechaOperacion) { this.fechaOperacion = fechaOperacion; }
	public void setOrigenIngreso(String origenIngreso) { this.origenIngreso = origenIngreso; }
	public void setConcepto(String concepto) { this.concepto = concepto; }
	public void setMonto(BigDecimal monto) { this.monto = monto; }
	public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    
}
