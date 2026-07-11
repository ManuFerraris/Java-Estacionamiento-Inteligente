package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import estacionamiento.domain.claves.SuscripcionId;

@Entity
@Table(name="suscripcion")
public class Suscripcion {
	
	@EmbeddedId
	private SuscripcionId id;
	
	@ManyToOne
	@MapsId("codigo") // Apunta al codigo del tipo de plan de la clase SuscripcionId.
	@JoinColumn(name="codigo", nullable=false)
	private TipoPlan tipoPlan;
	
	@ManyToOne
	@MapsId("numero") // Apunta al numero de usuario de la clase SuscripcionId.
	@JoinColumn(name="numero", nullable=false)
	private Usuario usuario;
	
	
	//private LocalDateTime fechaDesde; Ahora fechaDesde se inicializa directamente en SuscripcionId.
	
	@Column(name="fecha_hasta")
	private LocalDateTime fechaHasta;
	
	@Enumerated(EnumType.STRING) 
	@Column(name="estado", nullable=false)
	private EstadoSuscripcion estado;
	
	public Suscripcion() {}
	
	public Suscripcion(TipoPlan tipoPlan, Usuario usuario, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		this.tipoPlan = tipoPlan;
		this.usuario = usuario;
		//this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		
		// Inicializamos el ID compuesto para no tener NullPointerException en alguna prueba en memoria.
		int codigo = (tipoPlan != null) ? tipoPlan.getCodigo() : 0;
		int numero = (usuario != null) ? usuario.getNumero() : 0;
		this.id = new SuscripcionId(numero, codigo, fechaDesde);
	}

	public SuscripcionId getId() {
		return id;
	}

	public void setId(SuscripcionId id) {
		this.id = id;
	}
	
	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
		
		if (this.id == null) {
			this.id = new SuscripcionId();
		}
		
		if (tipoPlan != null) {
			this.id.setCodigo(tipoPlan.getCodigo());
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		if (this.id == null) {
			this.id = new SuscripcionId();
		}
		if (usuario != null) {
			this.id.setNumero(usuario.getNumero());
		}
	}

	public LocalDateTime getFechaDesde() {
		return (id != null) ? id.getFechaDesde() : null;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		if (this.id == null) {
			this.id = new SuscripcionId();
		}
		this.id.setFechaDesde(fechaDesde);
	}

	public LocalDateTime getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(LocalDateTime fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public EstadoSuscripcion getEstado() {
		return estado;
	}

	public void setEstado(EstadoSuscripcion estado) {
		this.estado = estado;
	}
	
	
}