package giis.demo.jdbc.empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import giis.demo.model.empleado.BaseEmpleado;

public class BaseEmpleadoJdbc {
	private final static String QUERY_INSERT_BASE = "INSERT INTO BaseEmpleado (dni, nombre, apellido, salario, fecha_nacimiento, numero_de_telefono) VALUES (?, ?, ?, ?, ?, ?)";
	private final static String QUERY_TELEPHONE_NUMBER = "SELECT 1 FROM BaseEmpleado WHERE numero_de_telefono = ?";

	public static void insertarBaseEmpleado(Connection con, BaseEmpleado emp) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_BASE)) {
			pst.setString(1, emp.getDni());
			pst.setString(2, emp.getNombre());
			pst.setString(3, emp.getApellido());
			pst.setBigDecimal(4, emp.getSalario());
			pst.setString(5, emp.getFechaNacimiento().toString()); // Formato YYYY-MM-DD para SQLite
			pst.setString(6, emp.getNumeroTelefono());

			pst.executeUpdate();
		}
	}
	
	public static boolean existeTelefono(Connection con, String telefono) throws SQLException {
	    try (PreparedStatement ps = con.prepareStatement(QUERY_TELEPHONE_NUMBER)) {
	        
	        ps.setString(1, telefono);
	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next();
	        }
	    }
	}
}
