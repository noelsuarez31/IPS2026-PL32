package giis.demo.jdbc.reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioJdbc {

	private static final String QUERY_FIND_USER = "SELECT id_usuario FROM Usuario WHERE nombre = ? AND tarjeta_bancaria=?";
	private static final String QUERY_INSERT_USER = "INSERT INTO Usuario (nombre, tarjeta_bancaria) VALUES (?, ?)";

	/**
	 * Compruba si tenemos un usuario con el nombre y la tarjeta pasados como
	 * parámetros ya registrado en la base de datos.
	 * 
	 * @param con
	 * @param name
	 * @param card
	 * @return el id del usuario si existe, -1 si no existe
	 * @throws SQLException
	 */
	public static int checkUserExists(Connection con, String name, String card) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_FIND_USER)) {

			ps.setString(1, name);
			ps.setString(2, card);

			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					return rs.getInt(1);
				}
				return -1;
			}
		}
	}

	/**
	 * Inserta en la base de datos un nuevo usuario
	 * @param con
	 * @param name
	 * @param card
	 * @throws SQLException
	 * @return devuelve el id del nuevo usuario
	 */
	public static int guardarUsuario(Connection con, String name, String card) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_USER)) {

			ps.setString(1, name);
			ps.setString(2, card);

			ps.executeUpdate();
			
			try(ResultSet rs = ps.getGeneratedKeys()){
				
				rs.next();
				return rs.getInt(1);
			}
		}
	}
	
	/**
	 * Obtiene el id del usuario si ya existe y si no lo crea
	 * @param con
	 * @param name
	 * @param card
	 * @return el id del usuario
	 * @throws SQLException
	 */
	public static int obtenerOCrear(Connection con, String name, String card) throws SQLException {
		
		int id = checkUserExists(con, name, card);
		
		if(id != -1) {
			return id;
		}
		
		return guardarUsuario(con, name, card);
	}
	

}
