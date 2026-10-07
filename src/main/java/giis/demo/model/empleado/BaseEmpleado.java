package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

import giis.demo.exceptions.EmployeeException;

public abstract class BaseEmpleado {
	private String dni;
	private String nombre;
	private String apellido;
	private BigDecimal salario;
	private LocalDate fechaNacimiento;
	private String numeroDeTelefono;
	
	public BaseEmpleado(String dni, String nombre, String apellido, BigDecimal salario, LocalDate fechaDeNacimiento,
			String numeroTelefono) {
		if(dni == null || dni.isBlank()) {
			throw new EmployeeException("dni no puede ser null ni blanco");
		}
		
		if(nombre == null || nombre.isBlank()) {
			throw new EmployeeException("dni no puede ser null ni blanco");
		}
		
		if(apellido == null || apellido.isBlank()) {
			throw new EmployeeException("apellido no puede ser null ni blanco");
		}
		
		if(salario.compareTo(BigDecimal.ZERO) < 0) {
			throw new EmployeeException("salario no puede ser negativo");
		}
		
		if(fechaDeNacimiento == null || fechaDeNacimiento.isAfter(LocalDate.now())) {
			throw new EmployeeException("fecha de nacimiento no puede ser null ni posterior a hoy");
		}
		
		if(numeroTelefono == null || numeroTelefono.isBlank()) {
			throw new EmployeeException("numeroTelefono no puede ser null ni blanco");
		}
		
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.salario = salario;
		this.fechaNacimiento = fechaDeNacimiento;
		this.numeroDeTelefono = numeroTelefono;
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
}