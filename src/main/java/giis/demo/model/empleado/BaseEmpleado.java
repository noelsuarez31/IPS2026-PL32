package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

import giis.demo.exceptions.EmployeeException;

public abstract class BaseEmpleado {
	private String dni;
	private String nombre;
	private String apellido;
	private BigDecimal salario;
	private LocalDate fechaNacimiento; // en bbdd fecha_nacimiento
	private String numeroDeTelefono; //en bbdd numero_de_telefono
	
	private String usuario; //en bbdd numero_usuario
	private String contrasena; //en bbdd contrasena
	private String salt; //en bbdd salt
	
	public BaseEmpleado(String dni, String nombre, String apellido, BigDecimal salario, LocalDate fechaDeNacimiento,
			String numeroTelefono, String usuario, String contrasena, String salt) throws EmployeeException {
		
		if(dni == null || dni.isBlank()) {
			throw new EmployeeException("dni no puede ser null ni blanco");
		}
		
		if(nombre == null || nombre.isBlank()) {
			throw new EmployeeException("nombre no puede ser null ni blanco");
		}
		
		if(apellido == null || apellido.isBlank()) {
			throw new EmployeeException("apellido no puede ser null ni blanco");
		}
		
		if(salario == null || salario.compareTo(BigDecimal.ZERO) <= 0) {
			throw new EmployeeException("salario debe ser mayor que cero");
		}
		
		if(fechaDeNacimiento == null || fechaDeNacimiento.isAfter(LocalDate.now())) {
			throw new EmployeeException("fecha de nacimiento no puede ser null ni posterior a hoy");
		}
		
		if(numeroTelefono == null || numeroTelefono.isBlank()) {
			throw new EmployeeException("numeroTelefono no puede ser null ni blanco");
		}
		
		if(usuario == null || usuario.isBlank()) {
			throw new EmployeeException("usuario no puede ser null ni blanco");
		}
		
		if(contrasena == null || contrasena.isBlank()) {
			throw new EmployeeException("contrasena no puede ser null ni blanco");
		}
		
		if(salt == null || salt.isBlank()) {
			throw new EmployeeException("salt no puede ser null ni blanco");
		}
		
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.salario = salario;
		this.fechaNacimiento = fechaDeNacimiento;
		this.numeroDeTelefono = numeroTelefono;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.salt = salt;
	}

	public abstract Posicion getPosicion();
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getApellido() {
		return this.apellido;
	}
	
	public String getDni() {
		return this.dni;
	}
	
	public LocalDate getFechaNacimiento() {
		return this.fechaNacimiento;
	}
	
	public String getNumeroTelefono() {
		return this.numeroDeTelefono;
	}
	
	public BigDecimal getSalario() {
		return this.salario;
	}

	public String getNumeroDeTelefono() {
		return numeroDeTelefono;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public String getSalt() {
		return salt;
	}
}