package giis.demo.jdbc.empleado;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Posicion;

public class EmpleadoNoDeportivoJdbc {
	private static final String QUERY_INSERT_NO_DEPORTIVO = "INSERT INTO EmpleadoNoDeportivo (dni, posicion) VALUES (?, ?)";

	/**
	 * Inserta un empleado no deportivo recien dado de alta. Los datos comunes se
	 * insertan antes con BaseEmpleadoJdbc
	 * 
	 * @param con
	 * @param emp
	 * @throws SQLException
	 */
	public static void insertarEmpleadoNoDeportivo(Connection con, EmpleadoNoDeportivo emp) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_NO_DEPORTIVO)) {
			pst.setString(1, emp.getDni());
			pst.setString(2, emp.getPosicion().name());
			pst.executeUpdate();
		}
	}

	// consulta los empleados no deportivos
	private static final String GET_NO_DEPORTIVOS = "SELECT be.dni, be.nombre, be.apellido, be.salario, be.fecha_nacimiento, be.numero_de_telefono, end.posicion "
			+ "FROM EmpleadoNoDeportivo end " + "JOIN BaseEmpleado be ON end.dni = be.dni";

	/*
	 * devuelve una lista de todos los empleados no deportivos
	 */
	public static List<EmpleadoNoDeportivo> getEmpleadosNoDeportivos(Connection con) throws SQLException {
		List<EmpleadoNoDeportivo> empleados = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(GET_NO_DEPORTIVOS); ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				EmpleadoNoDeportivo emp = new EmpleadoNoDeportivo(rs.getString("dni"), rs.getString("nombre"),
						rs.getString("apellido"), new BigDecimal(rs.getString("salario")),
						LocalDate.parse(rs.getString("fecha_nacimiento")), rs.getString("numero_de_telefono"),
						Posicion.valueOf(rs.getString("posicion")));
				empleados.add(emp);
			}
		}
		return empleados;
	}
}