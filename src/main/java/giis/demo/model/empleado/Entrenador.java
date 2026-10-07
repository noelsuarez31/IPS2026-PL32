package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Entrenador extends EmpleadoDeportivo {
	private TipoEntrenador tipoEntrenador;
	
	public Entrenador(String dni, String nombre, String apellido, BigDecimal salario, 
			LocalDate fechaDeNacimiento, String numeroTelefono) {
		super(dni, nombre, apellido, salario, fechaDeNacimiento, numeroTelefono, Posicion.ENTRENADOR);
	}
	
	public void setTipoEntrenador(TipoEntrenador tipo) {
		this.tipoEntrenador = tipo;
	}
	
	public boolean esPrimerEntrenador() {
		return tipoEntrenador == TipoEntrenador.PRIMER_ENTRENADOR;
	}
	
	public boolean esSegundoEntrenador() {
		return tipoEntrenador == TipoEntrenador.SEGUNDO_ENTRENADOR;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s", getApellido(), getNombre());
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipoEntrenador);
	}

	public boolean esMismoEntrenador(Entrenador other) {
		return (this.getDni().equals(other.getDni()))
				&& (this.getNombre().equals(other.getNombre()))
				&& (this.getApellido().equals(other.getApellido()));
	}
	
	
}


