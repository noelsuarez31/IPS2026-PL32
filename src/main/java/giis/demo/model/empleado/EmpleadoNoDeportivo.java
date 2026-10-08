package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

import giis.demo.exceptions.EmployeeException;

public class EmpleadoNoDeportivo extends BaseEmpleado {
	private Posicion posicion; 
	
	public EmpleadoNoDeportivo(String dni, String nombre, String apellido, BigDecimal salario,
			LocalDate fechaDeNacimiento, String numeroTelefono, Posicion posicion) {
		super(dni, nombre, apellido, salario, fechaDeNacimiento, numeroTelefono);
		
		if(posicion == null || !posicionValida(posicion)) {
			throw new EmployeeException("Posicion invalida");
		}
		
		this.posicion = posicion;
	}

	private boolean posicionValida(Posicion posicion) {
		if(posicion == Posicion.ENTRENADOR || posicion == Posicion.JUGADOR || posicion == Posicion.TECNICO_ADICIONAL) {
			return false;
		}
		
		return true;
	}

	@Override
	public Posicion getPosicion() {
		return posicion;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s - %s", getApellido(), getNombre(), getPosicion().name());
	}
	
}
