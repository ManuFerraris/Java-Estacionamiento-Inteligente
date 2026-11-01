package estacionamiento.dto;

import java.math.BigDecimal;

public class SalidaDTO {
	
	private boolean barreraAbierta;
    private BigDecimal montoDeuda;
    private String mensaje;

    public SalidaDTO(boolean barreraAbierta, BigDecimal montoDeuda, String mensaje) {
        this.barreraAbierta = barreraAbierta;
        this.montoDeuda = montoDeuda;
        this.mensaje = mensaje;
    }

	public boolean isBarreraAbierta() {
		return barreraAbierta;
	}

	public void setBarreraAbierta(boolean barreraAbierta) {
		this.barreraAbierta = barreraAbierta;
	}

	public BigDecimal getMontoDeuda() {
		return montoDeuda;
	}

	public void setMontoDeuda(BigDecimal montoDeuda) {
		this.montoDeuda = montoDeuda;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
    
    
}
