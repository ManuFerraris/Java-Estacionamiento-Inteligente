package estacionamiento.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="tipo_plan")
public class TipoPlan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="codigo")
	private Integer codigo;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@Column(name="detalle", nullable = false)
	private String detalle;
	
	@Column(name="fecha_baja", nullable = true)
	private LocalDate fechaBaja;

	@OneToMany(mappedBy = "tipoPlan", targetEntity = Beneficio.class)
	private List<Beneficio> beneficios;
	
	public TipoPlan() {
	}
	
	public TipoPlan(Integer cod, String nombre, String det, LocalDate fechaBaja) {
		this.nombre = nombre;
		this.codigo = cod;
		this.detalle = det;
		this.fechaBaja = fechaBaja;
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDetalle() {
		return detalle;
	}
	
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public List<Beneficio> getBeneficios() {
		return beneficios;
	}

	public void setBeneficios(List<Beneficio> beneficios) {
		this.beneficios = beneficios;
	}
	
	
	
}