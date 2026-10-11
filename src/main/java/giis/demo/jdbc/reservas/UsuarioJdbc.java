package giis.demo.jdbc.reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioJdbc {

	private static final String QUERY_CHECK_USER_EXISTS = "SELECT id_usuario FROM Usuario WHERE nombre = ? AND tarjeta_bancaria=?";
	private static final String QUERY_INSERT_USER = "INSERT INTO Usuario (nombre, tarjeta_bancaria) VALUES (?, ?)";

	/**
	 * Compruba si tenemos un usuario con el nombre y la tarjeta pasados como
	 * parámetros ya registrado en la base de datos.
	 * 
	 * @param con
	 * @param name
	 * @param card
	 * @return true si ya existe ese usuario, false en caso contrario
	 * @throws SQLException
	 */
	public static boolean checkUserExists(Connection con, String name, String card) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_CHECK_USER_EXISTS)) {

			ps.setString(1, name);
			ps.setString(2, card);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Inserta en la base de datos un nuevo usuario
	 * @param con
	 * @param name
	 * @param card
	 * @throws SQLException
	 * @return devuelve el número de filas modificadas
	 */
	public static int guardarUsuario(Connection con, String name, String card) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_USER)) {

			ps.setString(1, name);
			ps.setString(2, card);

			return ps.executeUpdate();
		}
	}
	

}
