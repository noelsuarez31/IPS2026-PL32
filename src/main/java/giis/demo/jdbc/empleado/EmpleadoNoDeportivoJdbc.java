package giis.demo.jdbc.empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import giis.demo.model.empleado.EmpleadoNoDeportivo;

public class EmpleadoNoDeportivoJdbc {
	private final static String QUERY_INSERT_NO_DEPORTIVO = 
	        "INSERT INTO EmpleadoNoDeportivo (dni, posicion) VALUES (?, ?)";

	    public static void insertarEmpleadoNoDeportivo(Connection con, EmpleadoNoDeportivo emp) throws SQLException {
	        try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_NO_DEPORTIVO)) {
	            pst.setString(1, emp.getDni());
	            pst.setString(2, emp.getPosicion().name());
	            
	            pst.executeUpdate();
	        }
	    }
}
