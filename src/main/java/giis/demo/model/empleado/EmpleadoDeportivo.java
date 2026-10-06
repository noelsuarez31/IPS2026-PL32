package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import giis.demo.exceptions.EmployeeException;

public class EmpleadoDeportivo extends BaseEmpleado{
	private String dni;
	private String nombre;
	private String apellido;
	private BigDecimal salario;
	private LocalDate fechaDeNacimiento;
	private String numeroDeTelefono;
	private Posicion posicion;
	
	public EmpleadoDeportivo(String dni, String nombre, String apellido, BigDecimal salario,
			LocalDate fechaDeNacimiento, String numeroTelefono, Posicion posicion) {
		super(dni, nombre, apellido, salario, fechaDeNacimiento, numeroTelefono);
		
		if(posicion == null && posicion != Posicion.JUGADOR && posicion != Posicion.ENTRENADOR
				&& posicion != Posicion.TECNICO_ADICIONAL) {
			throw new EmployeeException("Posicion invalida");
		}
		
		this.posicion = posicion;
	}

	@Override
	public Posicion getPosicion() {
		return this.posicion;
	}

	public int calcularEdad() {
		return Period.between(LocalDate.now(), fechaDeNacimiento).getYears();
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s", getApellido(), getNombre());
	}
}
