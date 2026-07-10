package estacionamiento.domain.claves;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

//Esta clase representa la clave compuesta (Tipo Vehiculo + Fecha)
@Embeddable
public class PrecioHistoricoTVId implements Serializable {

	private static final long serialVersionUID = 1L; //Para cuando hagamos modificaciones no tengamos problemas
	
	// Estos nombres deben coincidir con los atributos de PrecioHistoricoTV
	@Column(name="numero")
	private int numeroTipoVehiculo; // Hibernate sabe extraer el ID del objeto TipoVehiculo
	
	@Column(name="fecha_desde")
	private LocalDateTime fechaDesde;
	
	public PrecioHistoricoTVId() {}
	
	public PrecioHistoricoTVId(int tipoVehiculo, LocalDateTime fechaDesde) {
	    this.numeroTipoVehiculo = tipoVehiculo;
	    this.fechaDesde = fechaDesde;
	}
	
	// --- MÉTODOS EQUALS Y HASHCODE (Generados por Eclipse) ---
    // Son vitales para que Hibernate sepa comparar si dos claves son iguales en la memoria
	@Override
	public int hashCode() {
	    return Objects.hash(fechaDesde, numeroTipoVehiculo);
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    PrecioHistoricoTVId other = (PrecioHistoricoTVId) obj;
	    return Objects.equals(fechaDesde, other.fechaDesde) && numeroTipoVehiculo == other.numeroTipoVehiculo;
	}
}
