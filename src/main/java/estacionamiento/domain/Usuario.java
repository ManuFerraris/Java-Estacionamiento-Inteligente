package estacionamiento.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

@Entity
@Table(name="usuario")
public class Usuario {
	
	@Id
	@Column(name="numero")
	private int numero;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@Column(name="apellido", nullable = false)
	private String apellido;
	
	@Column(name="numero_telefono", nullable = false)
	private String numeroTelefono;
	
	@Column(name="direccion", nullable = false)
	private String direccion;
	
	@Column(name="mail", nullable = false)
	private String mail;
	
	@Column(name="mail_recuperacion", nullable = true)
	private String mailRecuperacion;
	
	@Column(name="fecha_baja", nullable = true)
	private LocalDate fechaBaja;
	
	@Column(name="nombre_usuario", nullable = false)
	private String nombreUsuario;
	
	@Column(name="contrasenia", nullable = false)
	private String contrasenia;
	
	@OneToMany(mappedBy = "usuario", targetEntity = Suscripcion.class)
	private List<Suscripcion> suscripciones;

	@OneToMany(mappedBy = "usuario", targetEntity = Reserva.class)
	private List<Reserva> reservas;
	
	public Usuario() {
	}

	public Usuario(int numero, String nombre, String apellido, String numeroTelefono
			, String direccion, String mail, String mailRecuperacion
			, LocalDate fechaBaja,String nombreUsuario, String contrasenia) {
		this.nombre = nombre;
		this.numero = numero;
		this.apellido = apellido;
		this.numeroTelefono = numeroTelefono;
		this.direccion = direccion;
		this.mail = mail;
		this.mailRecuperacion = mailRecuperacion;
		this.fechaBaja = fechaBaja;
		this.nombreUsuario = nombreUsuario;
		this.contrasenia = contrasenia;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getMail() {
		return mail;
	}

	public String getMailRecuperacion() {
		return mailRecuperacion;
	}

	public void setMailRecuperacion(String mailRecuperacion) {
		this.mailRecuperacion = mailRecuperacion;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
	
	
}
