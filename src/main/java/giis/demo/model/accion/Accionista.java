package giis.demo.model.accion;

import giis.demo.model.empleado.Miembro;

/**
 * Representa a un accionista del club con sus datos identificativos.
 */
public class Accionista extends Miembro{

	private String dni;
	private String nombre;
	private String apellidos;
	private String email;

	public Accionista(String user, String password) {
		
		super(user, password);
	}

	public Accionista(String dni, String nombre, String apellidos, String email, String usuario, String contrasena) {

		super(usuario, contrasena);
		
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		
		super.setDni(dni);

	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombreCompleto() {
		return nombre + " " + apellidos;
	}

	@Override
	public String toString() {
		return dni + " - " + getNombreCompleto();
	}
}
