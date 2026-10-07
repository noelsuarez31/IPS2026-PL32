package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import giis.demo.exceptions.EmployeeException;

public class EmpleadoDeportivo extends BaseEmpleado{
	private Posicion posicion;
	
	private int idEquipo;
	
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
	    return Period.between(getFechaNacimiento(), LocalDate.now()).getYears();
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s", getApellido(), getNombre());
	}
	
	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}
}
