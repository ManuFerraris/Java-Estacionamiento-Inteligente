package estacionamiento.domain.claves;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AsignacionCocheraId implements Serializable {
	private static final long serialVersionUID = 1L;

    @Column(name = "numero_usuario", insertable = false, updatable = false)
    private Integer numeroUsuario;

    @Column(name = "codigo_cochera", insertable = false, updatable = false)
    private Integer codigoCochera;

    @Column(name = "fecha_desde", insertable = false, nullable = false)
    private LocalDate fechaDesde;

    public AsignacionCocheraId() {}

    public AsignacionCocheraId(Integer numeroUsuario, Integer codigoCochera, LocalDate fechaDesde) {
        this.numeroUsuario = numeroUsuario;
        this.codigoCochera = codigoCochera;
        this.fechaDesde = fechaDesde;
    }
    
    public Integer getNumeroUsuario() {
        return numeroUsuario;
    }

    public void setNumeroUsuario(Integer numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }

    public Integer getCodigoCochera() {
        return codigoCochera;
    }

    public void setCodigoCochera(Integer codigoCochera) {
        this.codigoCochera = codigoCochera;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoCochera, fechaDesde, numeroUsuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AsignacionCocheraId other = (AsignacionCocheraId) obj;
        return Objects.equals(codigoCochera, other.codigoCochera) &&
               Objects.equals(fechaDesde, other.fechaDesde) &&
               Objects.equals(numeroUsuario, other.numeroUsuario);
    }
}
