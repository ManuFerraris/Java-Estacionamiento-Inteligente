package estacionamiento.domain.claves;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class ReservaId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "patente")
    private String patenteVehiculo;

    @Column(name = "numero_usuario")
    private int numeroUsuario;

    @Column(name = "numero_tipo_estadia")
    private int numeroTipoEstadia;

    @Column(name = "fecha_desde")
    private LocalDateTime fechaDesde;
    
    public ReservaId() {}

    public ReservaId(String patenteVehiculo, int numeroUsuario, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        this.patenteVehiculo = patenteVehiculo;
        this.numeroUsuario = numeroUsuario;
        this.numeroTipoEstadia = numeroTipoEstadia;
        this.fechaDesde = fechaDesde;
    }

	public String getPatenteVehiculo() {
		return patenteVehiculo;
	}

	public void setPatenteVehiculo(String patenteVehiculo) {
		this.patenteVehiculo = patenteVehiculo;
	}

	public int getNumeroUsuario() {
		return numeroUsuario;
	}

	public void setNumeroUsuario(int numeroUsuario) {
		this.numeroUsuario = numeroUsuario;
	}

	public int getNumeroTipoEstadia() {
		return numeroTipoEstadia;
	}

	public void setNumeroTipoEstadia(int numeroTipoEstadia) {
		this.numeroTipoEstadia = numeroTipoEstadia;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
    
	@Override
    public int hashCode() {
        return Objects.hash(fechaDesde, numeroTipoEstadia, numeroUsuario, patenteVehiculo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReservaId other = (ReservaId) obj;
        return Objects.equals(fechaDesde, other.fechaDesde) && 
               numeroTipoEstadia == other.numeroTipoEstadia && 
               numeroUsuario == other.numeroUsuario && 
               Objects.equals(patenteVehiculo, other.patenteVehiculo);
    }
}
