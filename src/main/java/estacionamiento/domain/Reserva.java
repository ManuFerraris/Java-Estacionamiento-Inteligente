package estacionamiento.domain;
import java.time.LocalDateTime; // Clase moderna para fechas y horas
import java.math.BigDecimal; // Clase estandar para el dinero

import jakarta.persistence.*;
import estacionamiento.domain.claves.ReservaId;

@Entity
@Table(name="reserva")
public class Reserva {
	
	@EmbeddedId
    private ReservaId id;
	
	@ManyToOne
    @MapsId("patenteVehiculo")
    @JoinColumn(name="patente", nullable=false)
    private Vehiculo vehiculo;

    @ManyToOne
    @MapsId("numeroUsuario")
    @JoinColumn(name="numero_usuario", nullable=false)
    private Usuario usuario;

    @ManyToOne
    @MapsId("numeroTipoEstadia")
    @JoinColumn(name="numero_tipo_estadia", nullable=false)
    private TipoEstadia tipoEstadia;

    // La fechaDesde está contenida dentro del 'id'
	//private LocalDateTime fechaDesde;
    
    @Column(name="fecha_hasta_tentativa", nullable=false)
	private LocalDateTime fechaHastaTentativa;
    
    @Column(name="fecha_hasta_real")
	private LocalDateTime fechaHastaReal;
	
    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable=false)
	private EstadoReserva estado;
	
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name="numero_pago")
	private Pago pago;
	
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="codigo_lugar")
	private Lugar lugar;
	
	@Column(name="senia", precision = 10, scale = 2)
	private BigDecimal senia;
	
	public Reserva() {}
	
	public Reserva(Vehiculo vehiculo, Usuario usuario, TipoEstadia tipoEstadia, LocalDateTime fechaHastaTentativa, 
	            LocalDateTime fechaHastaReal, EstadoReserva estado, BigDecimal senia, Pago pago, Lugar lugar) {
		this.vehiculo = vehiculo;
		this.usuario = usuario;
		this.tipoEstadia = tipoEstadia;
		//this.fechaDesde = fechaDesde;
		this.fechaHastaTentativa = fechaHastaTentativa;
		this.fechaHastaReal = fechaHastaReal;
		this.estado = estado;
		this.senia = senia;
		this.pago = pago;
		this.lugar = lugar;
	}
	
	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public Lugar getLugar() {
		return lugar;
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}

	public ReservaId getId() {
		return id;
	
	}
	
    public void setId(ReservaId id) {
    	this.id = id;
    }

    public Vehiculo getVehiculo() { 
    	return vehiculo; 
    }
    
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        if (this.id == null) this.id = new ReservaId();
        if (vehiculo != null) this.id.setPatenteVehiculo(vehiculo.getPatente());
    }

    public Usuario getUsuario() {
    	return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (this.id == null) this.id = new ReservaId();
        if (usuario != null) this.id.setNumeroUsuario(usuario.getNumero());
    }

    public TipoEstadia getTipoEstadia() { 
    	return tipoEstadia; 
    }
    
    public void setTipoEstadia(TipoEstadia tipoEstadia) {
        this.tipoEstadia = tipoEstadia;
        if (this.id == null) this.id = new ReservaId();
        if (tipoEstadia != null) this.id.setNumeroTipoEstadia(tipoEstadia.getNumero());
    }

    // Delegamos fechaDesde al ID
    public LocalDateTime getFechaDesde() {
        return (id != null) ? id.getFechaDesde() : null;
    }
    
    public void setFechaDesde(LocalDateTime fechaDesde) {
        if (this.id == null) this.id = new ReservaId();
        this.id.setFechaDesde(fechaDesde);
    }

	public LocalDateTime getFechaHastaTentativa() {
		return fechaHastaTentativa;
	}

	public void setFechaHastaTentativa(LocalDateTime fechaHastaTentativa) {
		this.fechaHastaTentativa = fechaHastaTentativa;
	}

	public LocalDateTime getFechaHastaReal() {
		return fechaHastaReal;
	}

	public void setFechaHastaReal(LocalDateTime fechaHastaReal) {
		this.fechaHastaReal = fechaHastaReal;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public BigDecimal getSenia() {
		return senia;
	}

	public void setSenia(BigDecimal senia) {
		this.senia = senia;
	}
}
