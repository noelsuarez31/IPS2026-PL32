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

	    // Obtener conexión
	    Connection con = jdbc.getCon();

	    try {
	        con.setAutoCommit(false); 
	        
	        if (BaseEmpleadoJdbc.existeTelefono(con, telefono)) {
	            throw new IllegalArgumentException("El número de teléfono introducido ya está registrado en el sistema.");
	        }
	        
	        if (!esDeportivo) {
	            // No deportivo
	            EmpleadoNoDeportivo empleado = new EmpleadoNoDeportivo(dni, nombre, apellido, salario, fechaNacimiento, telefono, posicion);
	            BaseEmpleadoJdbc.insertarBaseEmpleado(con, empleado);
	            EmpleadoNoDeportivoJdbc.insertarEmpleadoNoDeportivo(con, empleado);
	            
	        } else if (posicion == Posicion.ENTRENADOR) {
	            // Es Deportivo Y es Entrenador
	            TipoEntrenador tipo = TipoEntrenador.valueOf(tipoEntrenador); 
	            Entrenador entrenador = new Entrenador(dni, nombre, apellido, salario, fechaNacimiento, telefono, tipo);
	            
	            BaseEmpleadoJdbc.insertarBaseEmpleado(con, entrenador);
	            EmpleadoDeportivoJdbc.insertarEmpleadoDeportivo(con, entrenador);
	            
	            EntrenadorJdbc entrenadorJdbc = new EntrenadorJdbc();
	            entrenadorJdbc.insertarEntrenador(entrenador, con); 
	            
	        } else {
	            // Es Deportivo pero no es Entrenador
	            EmpleadoDeportivo empleado = new EmpleadoDeportivo(dni, nombre, apellido, salario, fechaNacimiento, telefono, posicion);
	            BaseEmpleadoJdbc.insertarBaseEmpleado(con, empleado);
	            EmpleadoDeportivoJdbc.insertarEmpleadoDeportivo(con, empleado);
	        }

	        con.commit();

	    } catch (SQLException e) {
	        if (con != null) {
	            try {
	                con.rollback();
	            } catch (SQLException ex) { }
	        }
	        throw new Exception("Error al guardar en la base de datos: " + e.getMessage());
	    } finally {
	        if (con != null) {
	            try {
	                con.setAutoCommit(true);
	            } catch (SQLException ex) { }
	        }
	    }
	}

	public void close() throws SQLException {
		jdbc.close();
	}
}
