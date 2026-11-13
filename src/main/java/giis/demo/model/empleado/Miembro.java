package giis.demo.model.empleado;

import giis.demo.exceptions.EmployeeException;

public abstract class Miembro {
	
	private String usuario; //en bbdd numero_usuario
	private String contrasena; //en bbdd contrasena
	
	private String dni;
	
	public Miembro(String usuario, String contraseña) {
		
		if(usuario == null || usuario.isBlank()) {
			throw new EmployeeException("usuario no puede ser null ni blanco");
		}
		
		if(contraseña == null || contraseña.isBlank()) {
			throw new EmployeeException("contrasena no puede ser null ni blanco");
		}
		
		this.usuario = usuario;
		this.contrasena = contraseña;
		
	}
	
public Miembro(String usuario, String contraseña, String dni) {
		
		if(usuario == null || usuario.isBlank()) {
			throw new EmployeeException("usuario no puede ser null ni blanco");
		}
		
		if(contrasena == null || contrasena.isBlank()) {
			throw new EmployeeException("contrasena no puede ser null ni blanco");
		}
		
		if(dni == null || dni.isBlank()) {
			throw new EmployeeException("dni no puede ser null ni blanco");
		}
		
		this.usuario = usuario;
		this.contrasena = contraseña;
		this.dni = dni;
		
	}

	public String getUsuario() {
		return usuario;
	}

	public String getContrasena() {
		return contrasena;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String getDni() {
		return this.dni;
	}
}
