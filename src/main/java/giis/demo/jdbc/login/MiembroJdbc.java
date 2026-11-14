package giis.demo.jdbc.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MiembroJdbc {

	// 1. Insertar un nuevo miembro
	private static final String QUERY_INSERT_MIEMBRO = "INSERT INTO Miembro (dni, nombre_usuario, contrasena) "
			+ "VALUES (?, ?, ?)";

	// 2. Comprobar si existe un miembro por DNI
	private static final String QUERY_EXISTE_MIEMBRO = "SELECT 1 FROM Miembro WHERE UPPER(dni) = UPPER(?)";

	// 3. Comprobar si existe un nombre de usuario
	private static final String QUERY_EXISTE_USUARIO = "SELECT 1 FROM Miembro WHERE nombre_usuario = ? COLLATE NOCASE";

	// 4. Comprobar si otro miembro utiliza un nombre de usuario
	private static final String QUERY_EXISTE_USUARIO_OTRO_MIEMBRO =
	        "SELECT 1 FROM Miembro "
	        + "WHERE nombre_usuario = ? COLLATE NOCASE AND UPPER(dni) != UPPER(?)";

	// 5. Actualizar las credenciales de un miembro
	private static final String QUERY_UPDATE_CREDENCIALES = "UPDATE Miembro "
			+ "SET nombre_usuario = ?, contrasena = ? " + "WHERE UPPER(dni) = UPPER(?)";

	// 6. Eliminar un miembro que no tenga perfiles asociados
	private static final String QUERY_DELETE_MIEMBRO = "DELETE FROM Miembro " + "WHERE UPPER(dni) = UPPER(?) "
			+ "AND NOT EXISTS (" + "    SELECT 1 FROM Accionista "
			+ "    WHERE UPPER(Accionista.dni) = UPPER(Miembro.dni)" + ") " + "AND NOT EXISTS ("
			+ "    SELECT 1 FROM BaseEmpleado " + "    WHERE UPPER(BaseEmpleado.dni) = UPPER(Miembro.dni)" + ")";

	private static final String QUERY_GET_CREDENCIALES = "SELECT dni, contrasena " + "FROM Miembro "
			+ "WHERE nombre_usuario = ?";

	private static final String QUERY_GET_CREDENCIALES_POR_DNI = "SELECT nombre_usuario, contrasena FROM Miembro WHERE UPPER(dni) = UPPER(?)";

	/**
	 * Inserta un nuevo miembro en la base de datos.
	 */
	public static void insertarMiembro(Connection con, String dni, String usuario, String contrasena)
			throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_MIEMBRO)) {
			ps.setString(1, dni);
			ps.setString(2, usuario);
			ps.setString(3, contrasena);
			ps.executeUpdate();
		}
	}

	/**
	 * Comprueba si existe un miembro con el DNI indicado.
	 */
	public static boolean existeMiembro(Connection con, String dni) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_EXISTE_MIEMBRO)) {
			ps.setString(1, dni);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Comprueba si un nombre de usuario ya está registrado.
	 */
	public static boolean existeNombreUsuario(Connection con, String usuario) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_EXISTE_USUARIO)) {
			ps.setString(1, usuario);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Comprueba si otro miembro distinto al indicado utiliza ese nombre de usuario.
	 */
	public static boolean existeNombreUsuarioOtroMiembro(Connection con, String usuario, String dni)
			throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_EXISTE_USUARIO_OTRO_MIEMBRO)) {
			ps.setString(1, usuario);
			ps.setString(2, dni);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Actualiza el nombre de usuario y el hash de contraseña de un miembro.
	 */
	public static boolean actualizarCredenciales(Connection con, String dni, String usuario, String contrasena)
			throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_UPDATE_CREDENCIALES)) {
			ps.setString(1, usuario);
			ps.setString(2, contrasena);
			ps.setString(3, dni);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Elimina un miembro únicamente si no tiene perfiles de accionista ni empleado.
	 */
	public static boolean eliminarMiembro(Connection con, String dni) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_DELETE_MIEMBRO)) {
			ps.setString(1, dni);

			return ps.executeUpdate() > 0;
		}
	}

	public static String[] getCredenciales(Connection con, String nombreUsuario) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_CREDENCIALES)) {
			ps.setString(1, nombreUsuario);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String[] { rs.getString("dni"), rs.getString("contrasena") };
				}
			}
		}

		return null;
	}

	/**
	 * Devuelve el nombre de usuario y el hash de contraseña de un miembro.
	 * 
	 * @return {nombreUsuario, hashContrasena}, o null si no existe.
	 */
	public static String[] getCredencialesPorDni(Connection con, String dni) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_CREDENCIALES_POR_DNI)) {
			ps.setString(1, dni);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String[] { rs.getString("nombre_usuario"), rs.getString("contrasena") };
				}
			}
		}

		return null;
	}

}
