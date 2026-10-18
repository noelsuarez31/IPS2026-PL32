package giis.demo.jdbc.empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import giis.demo.model.empleado.BaseEmpleado;

public class BaseEmpleadoJdbc {
	private static final String QUERY_INSERT_BASE = "INSERT INTO BaseEmpleado "
			+ "(dni, nombre, apellido, salario, fecha_nacimiento, numero_de_telefono) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String QUERY_EXISTE_DNI = "SELECT 1 FROM BaseEmpleado WHERE dni = ?";
	private static final String QUERY_EXISTE_TELEFONO = "SELECT 1 FROM BaseEmpleado WHERE numero_de_telefono = ?";

	/**
	 * Inserta los datos comunes de un empleado (deportivo o no deportivo)
	 * @param con
	 * @param emp
	 * @throws SQLException
	 */
	public static void insertarBaseEmpleado(Connection con, BaseEmpleado emp) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_BASE)) {
			pst.setString(1, emp.getDni());
			pst.setString(2, emp.getNombre());
			pst.setString(3, emp.getApellido());
			pst.setBigDecimal(4, emp.getSalario());
			pst.setString(5, emp.getFechaNacimiento().toString()); // yyyy-MM-dd, igual que en el resto de la BBDD
			pst.setString(6, emp.getNumeroTelefono());
			pst.executeUpdate();
		}
	}

	/**
	 * @return true si ya existe un empleado con ese dni
	 */
	public static boolean existeDni(Connection con, String dni) throws SQLException {
		return existe(con, QUERY_EXISTE_DNI, dni);
	}

	/**
	 * @return true si ya existe un empleado con ese numero de telefono
	 */
	public static boolean existeTelefono(Connection con, String telefono) throws SQLException {
		return existe(con, QUERY_EXISTE_TELEFONO, telefono);
	}

	private static boolean existe(Connection con, String query, String valor) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setString(1, valor);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next();
			}
		}
	}
}
