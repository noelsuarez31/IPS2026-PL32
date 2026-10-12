package giis.demo.model.contrataciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import giis.demo.jdbc.JDBC;
import giis.demo.jdbc.empleado.BaseEmpleadoJdbc;
import giis.demo.jdbc.empleado.EmpleadoDeportivoJdbc;
import giis.demo.jdbc.empleado.EmpleadoNoDeportivoJdbc;
import giis.demo.jdbc.empleado.EntrenadorJdbc;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Entrenador;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.empleado.TipoEntrenador;

public class GestorEmpleados {

	private JDBC jdbc;

	public GestorEmpleados() {
		this.jdbc = new JDBC();
	}

	public void registrarNuevoEmpleado(String dni, String nombre, String apellido, BigDecimal salario,
			LocalDate fechaNacimiento, String telefono, boolean esDeportivo, Posicion posicion, String tipoEntrenador)
			throws Exception {

		// Regla de menores de edad
		int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
		if (edad < 18 && posicion != Posicion.JUGADOR) {
			throw new Exception("Regla de negocio: Solo los jugadores pueden ser menores de edad.");
		}

		BaseEmpleado empleado;
		if (esDeportivo) {
			empleado = new EmpleadoDeportivo(dni, nombre, apellido, salario, fechaNacimiento, telefono, posicion);
		} else {
			empleado = new EmpleadoNoDeportivo(dni, nombre, apellido, salario, fechaNacimiento, telefono, posicion);
		}

		// Obtener conexión
		Connection con = jdbc.getCon();

		try {
			con.setAutoCommit(false); // Iniciar transacción
			
			if (BaseEmpleadoJdbc.existeTelefono(con, telefono)) {
		        throw new IllegalArgumentException("El número de teléfono introducido ya está registrado en el sistema.");
		    }

			BaseEmpleadoJdbc.insertarBaseEmpleado(con, empleado);

			if (esDeportivo) {
				EmpleadoDeportivoJdbc.insertarEmpleadoDeportivo(con, (EmpleadoDeportivo) empleado);
			} else {
				EmpleadoNoDeportivoJdbc.insertarEmpleadoNoDeportivo(con, (EmpleadoNoDeportivo) empleado);
			}

			if (posicion == Posicion.ENTRENADOR) {
			    
			    // Convertir String a Enum.
			    TipoEntrenador tipo = TipoEntrenador.valueOf(); 
			    
			    Entrenador entrenador = new Entrenador(dni, nombre, apellido, salario, fechaNacimiento, telefono, tipo);
			    
			    // 3. Persistimos los datos aprovechando el polimorfismo.
			    // Dado que Entrenador hereda de EmpleadoDeportivo, y este de BaseEmpleado, 
			    // es completamente lícito pasar el mismo objeto 'entrenador' a los métodos JDBC padre.
			    BaseEmpleadoJdbc.insertarBaseEmpleado(con, entrenador);
			    EmpleadoDeportivoJdbc.insertarEmpleadoDeportivo(con, entrenador);
			    
			    // 4. Inserción final específica de Entrenador usando el método recién refactorizado
			    EntrenadorJdbc entrenadorJdbc = new EntrenadorJdbc();
			    entrenadorJdbc.insertarEntrenador(entrenador, con); 
			}

			con.commit(); // Confirmar transacción

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
					// Log error del rollback
				}
			}
			throw new Exception("Error al guardar en la base de datos: " + e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.setAutoCommit(true);
				} catch (SQLException ex) {
				}
			}
		}
	}

	public void close() throws SQLException {
		jdbc.close();
	}
}
