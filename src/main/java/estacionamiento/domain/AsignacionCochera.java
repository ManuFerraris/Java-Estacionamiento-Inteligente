package estacionamiento.domain;

import java.time.LocalDate;
import estacionamiento.domain.claves.AsignacionCocheraId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "asignacion_cochera")
public class AsignacionCochera {
	
	@EmbeddedId
    private AsignacionCocheraId id;

	// Pruebo a ver que onda con FetchType.EAGER para no
	// modificar las futuras jpql y evitar el LazyInitializationException ;)
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("numeroUsuario")
    @JoinColumn(name = "numero_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("codigoCochera")
    @JoinColumn(name = "codigo_cochera", nullable = false)
    private Cochera cochera;

    @Column(name = "fecha_hasta", nullable = true)
    private LocalDate fechaHasta;

    public AsignacionCochera() {}
    
    public AsignacionCochera(Usuario usuario, Cochera cochera, LocalDate fechaDesde) {
        this.usuario = usuario;
        this.cochera = cochera;
        this.id = new AsignacionCocheraId(usuario.getNumero(), cochera.getCodigo(), fechaDesde);
        this.fechaHasta = null; // Seteada en null por defecto al crearse
    }
    
    public AsignacionCocheraId getId() {
        return id;
    }

    public void setId(AsignacionCocheraId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cochera getCochera() {
        return cochera;
    }

    public void setCochera(Cochera cochera) {
        this.cochera = cochera;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
