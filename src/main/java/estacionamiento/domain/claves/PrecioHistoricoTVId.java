package estacionamiento.domain.claves;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

//Esta clase representa la clave compuesta (Tipo Vehiculo + Fecha)
public class PrecioHistoricoTVId implements Serializable {

	private static final long serialVersionUID = 1L; //Para cuando hagamos modificaciones no tengamos problemas
	
	// Estos nombres deben coincidir con los atributos de PrecioHistoricoTV
	private int tipoVehiculo; // Hibernate sabe extraer el ID del objeto TipoVehiculo
	private LocalDateTime fechaDesde;
	
	public PrecioHistoricoTVId() {}
	
	public PrecioHistoricoTVId(int tipoVehiculo, LocalDateTime fechaDesde) {
	    this.tipoVehiculo = tipoVehiculo;
	    this.fechaDesde = fechaDesde;
	}
	
	// --- MÉTODOS EQUALS Y HASHCODE (Generados por Eclipse) ---
    // Son vitales para que Hibernate sepa comparar si dos claves son iguales en la memoria
	@Override
	public int hashCode() {
	    return Objects.hash(fechaDesde, tipoVehiculo);
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    PrecioHistoricoTVId other = (PrecioHistoricoTVId) obj;
	    return Objects.equals(fechaDesde, other.fechaDesde) && tipoVehiculo == other.tipoVehiculo;
	}
}
