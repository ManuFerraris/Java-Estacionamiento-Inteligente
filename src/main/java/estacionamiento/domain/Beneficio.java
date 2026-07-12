package estacionamiento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import estacionamiento.domain.claves.BeneficioId;

@Entity
@Table(name="beneficio")
public class Beneficio {
	
	@EmbeddedId
	private BeneficioId id;
	
	@ManyToOne
	@MapsId("codigoPlan") //codigoPlan referencia al nombre del atributo en BeneficioId
	@JoinColumn(name="codigo", nullable=false) // este referencia al de la BD nuestra.
	private TipoPlan tipoPlan;
	
	// Este ya esta encapsulado en BeneficioId
	//private int numero;
	
	@Column(name="descripcion", nullable=false)
	private String descripcion;
	
	@Column(name="fecha_creacion", nullable=false)
	private LocalDateTime fechaCreacion;
	
	@Column(name="fecha_baja")
	private LocalDateTime fechaBaja;
	
	public Beneficio() {}
	
	public Beneficio(TipoPlan tipoPlan, int numero, String descripcion, LocalDateTime fechaCreacion, LocalDateTime fechaBaja) {
		this.tipoPlan = tipoPlan;
		//this.numero = numero;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.fechaBaja = fechaBaja;
		
		// Inicializamos el ID compuesto usando el TipoPlan y el número que se pasa
		int codPlan = (tipoPlan != null) ? tipoPlan.getCodigo() : 0;
		this.id = new BeneficioId(codPlan, numero);
	}

	public BeneficioId getId() {
		return id;
	}

	public void setId(BeneficioId id) {
		this.id = id;
	}

	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
		if (this.id == null) {
			this.id = new BeneficioId();
		}
		if (tipoPlan != null) {
			this.id.setCodigoPlan(tipoPlan.getCodigo());
		}
	}

	// Delegamos el Getter al ID
	public int getNumero() {
		return (id != null) ? id.getNumero() : 0;
	}

	// Delegamos el Setter al ID
	public void setNumero(int numero) {
		if (this.id == null) {
			this.id = new BeneficioId();
		}
		this.id.setNumero(numero);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDateTime fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
}
