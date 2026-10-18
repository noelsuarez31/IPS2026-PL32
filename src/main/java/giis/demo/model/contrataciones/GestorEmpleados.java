package giis.demo.model.contrataciones;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import giis.demo.exceptions.EmployeeException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Posicion;

public class GestorEmpleados {

	private static final int EDAD_MAYORIA = 18;

	private JDBC jdbc = new JDBC();

	/**
	 * Da de alta un nuevo empleado. Los menores de edad solo pueden ser jugadores.
	 * @throws EmployeeException si algun dato incumple las reglas de negocio o si
	 * el dni o el telefono ya estan registrados, o si falla el acceso a la BBDD
	 */
	public void registrarNuevoEmpleado(String dni, String nombre, String apellido, BigDecimal salario,
			LocalDate fechaNacimiento, String telefono, boolean esDeportivo, Posicion posicion) {

		BaseEmpleado empleado = crearEmpleado(dni, nombre, apellido, salario, fechaNacimiento, telefono, esDeportivo,
				posicion);

		try {
			if (jdbc.existeDniEmpleado(empleado.getDni())) {
				throw new EmployeeException("Ya existe un empleado con el DNI " + empleado.getDni());
			}
			if (jdbc.existeTelefonoEmpleado(empleado.getNumeroTelefono())) {
				throw new EmployeeException("El numero de telefono ya esta registrado en el sistema");
			}
			jdbc.almacenarEmpleado(empleado);
		} catch (SQLException e) {
			throw new EmployeeException("Error al guardar el empleado en la BBDD: " + e.getMessage());
		}
	}

	private BaseEmpleado crearEmpleado(String dni, String nombre, String apellido, BigDecimal salario,
			LocalDate fechaNacimiento, String telefono, boolean esDeportivo, Posicion posicion) {

		if (fechaNacimiento != null && posicion != Posicion.JUGADOR
				&& Period.between(fechaNacimiento, LocalDate.now()).getYears() < EDAD_MAYORIA) {
			throw new EmployeeException("Solo los jugadores pueden ser menores de edad");
		}

		// Los constructores validan el resto de datos y que la posicion sea del tipo correcto
		if (esDeportivo) {
			return new EmpleadoDeportivo(dni, nombre, apellido, salario, fechaNacimiento, telefono, posicion);
		}
		return new EmpleadoNoDeportivo(dni, nombre, apellido, salario, fechaNacimiento, telefono, posicion);
	}

	public void close() throws SQLException {
		jdbc.close();
	}
}
