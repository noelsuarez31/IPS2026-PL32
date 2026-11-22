package giis.demo.model.medico;

import java.sql.SQLException;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.util.ApplicationException;

public class GestionLesiones {
	
	private JDBC jdbc;
	
	public GestionLesiones() {
		jdbc = new JDBC();
	}
	
	public EmpleadoDeportivo[] getJugadores() {
		try {
			return jdbc.getJugadores().toArray(new EmpleadoDeportivo[0]);
		} catch (SQLException e) {
			throw new ApplicationException("Error al obtener los jugadores: " + e.getMessage());
		}
	}

}
