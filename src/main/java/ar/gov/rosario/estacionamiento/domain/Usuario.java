package ar.gov.rosario.estacionamiento.domain;

import java.sql.Date;

public class Usuario {
	private int numero;
	private String nombre;
	private String apellido;
	private String numeroTelefono;
	private String direccion;
	private String mail;
	private Date fechaBaja;
	private String nombreUsuario;
	private String contrasenia;

	public Usuario() {
	}

	public Usuario(int numero, String nombre, String apellido, String numeroTelefono, String direccion, String mail, Date fechaBaja,String nombreUsuario, String contrasenia) {
		this.nombre = nombre;
		this.numero = numero;
		this.apellido = apellido;
		this.numeroTelefono = numeroTelefono;
		this.direccion = direccion;
		this.mail = mail;
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

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
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
