package giis.demo.model.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Entrenador extends EmpleadoDeportivo {
	
	public Entrenador(String dni, String nombre, String apellido, BigDecimal salario, 
            LocalDate fechaDeNacimiento, String numeroTelefono) {
        // Un entrenador es simplemente un EmpleadoDeportivo con posición ENTRENADOR
        super(dni, nombre, apellido, salario, fechaDeNacimiento, numeroTelefono, Posicion.ENTRENADOR);
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s", getApellido(), getNombre());
    }
}




