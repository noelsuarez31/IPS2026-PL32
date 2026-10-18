package giis.demo.model.contrataciones;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import giis.demo.exceptions.EmployeeException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.equipo.CategoriaEquipo;

public class GestorEmpleados {

	private static final int EDAD_MAYORIA = 18;
	private static final String TIPO_EQUIPO_EN_FORMACION = "En formacion";

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

		if (fechaNacimiento != null && posicion != Posicion.JUGADOR && calcularEdad(fechaNacimiento) < EDAD_MAYORIA) {
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
	
	/**
	 * Devuelve todos los empleados del sistema
	 * @throws EmployeeException si falla el acceso a la BBDD
	 */
	public List<BaseEmpleado> obtenerEmpleados() {
		try {
			return jdbc.getEmpleados();
		} catch (SQLException e) {
			throw new EmployeeException("Error al obtener los empleados: " + e.getMessage());
		}
	}
	
	/**
	 * Modifica los datos personales y el salario de un empleado existente.
	 * El puesto de trabajo (posicion y tipo de empleado) no se puede cambiar,
	 * por eso se toma siempre del empleado original
	 * @param original empleado tal y como esta en la BBDD
	 * @throws EmployeeException si algun dato incumple las reglas de negocio, si el
	 * nuevo dni o telefono ya pertenecen a otro empleado, o si falla la BBDD
	 */
	public void modificarEmpleado(BaseEmpleado original, String dni, String nombre, String apellido,
			BigDecimal salario, LocalDate fechaNacimiento, String telefono) {
		if (original == null) {
			throw new EmployeeException("Debe seleccionar un empleado");
		}

		boolean esDeportivo = original instanceof EmpleadoDeportivo;
		BaseEmpleado modificado = crearEmpleado(dni, nombre, apellido, salario, fechaNacimiento, telefono,
				esDeportivo, original.getPosicion());

		try {
			if (esDeportivo) {
				comprobarEdadParaSuEquipo((EmpleadoDeportivo) original, fechaNacimiento);
			}
			if (!original.getDni().equals(modificado.getDni()) && jdbc.existeDniEmpleado(modificado.getDni())) {
				throw new EmployeeException("Ya existe un empleado con el DNI " + modificado.getDni());
			}
			if (jdbc.existeTelefonoEnOtroEmpleado(modificado.getNumeroTelefono(), original.getDni())) {
				throw new EmployeeException("El numero de telefono ya esta registrado en otro empleado");
			}
			jdbc.modificarEmpleado(original.getDni(), modificado);
		} catch (SQLException e) {
			throw new EmployeeException("Error al modificar el empleado en la BBDD: " + e.getMessage());
		}
	}
	
	/**
	 * Elimina al empleado del sistema junto con sus horarios, franjas y entrevistas
	 * @throws EmployeeException si falla el acceso a la BBDD
	 */
	public void eliminarEmpleado(BaseEmpleado empleado) {
		if (empleado == null) {
			throw new EmployeeException("Debe seleccionar un empleado");
		}
		try {
			jdbc.eliminarEmpleado(empleado.getDni());
		} catch (SQLException e) {
			throw new EmployeeException("Error al eliminar el empleado de la BBDD: " + e.getMessage());
		}
	}
	
	/**
	 * @return true si el empleado pertenece a algun equipo
	 */
	public boolean perteneceAEquipo(BaseEmpleado empleado) {
		return empleado instanceof EmpleadoDeportivo && ((EmpleadoDeportivo) empleado).getIdEquipo() > 0;
	}
	
	/**
	 * Si un jugador esta en un equipo en formacion, su nueva fecha de nacimiento
	 * debe seguir cumpliendo los limites de edad de la categoria (HU de equipos)
	 */
	private void comprobarEdadParaSuEquipo(EmpleadoDeportivo jugador, LocalDate nuevaFecha) throws SQLException {
		if (jugador.getPosicion() != Posicion.JUGADOR || !perteneceAEquipo(jugador)) {
			return;
		}
		CategoriaEquipo categoria = jdbc.obtenerCategoriaDeEquipo(jugador.getIdEquipo());
		if (categoria != null && TIPO_EQUIPO_EN_FORMACION.equals(categoria.getTipo())
				&& !categoria.esEdadValida(calcularEdad(nuevaFecha))) {
			throw new EmployeeException("Con esa fecha de nacimiento el jugador no cumple la edad de la categoria "
					+ categoria.getNombre() + " de su equipo");
		}
	}
	
	private static int calcularEdad(LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}
}
