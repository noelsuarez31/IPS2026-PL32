package giis.demo.jdbc.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MiembroJdbc {

// 1. Insertar un nuevo miembro
	private static final String QUERY_INSERT_MIEMBRO = "INSERT INTO Miembro (dni, nombre_usuario, contrasena, tipo) "
			+ "VALUES (?, ?, ?, ?)";

// 2. Comprobar si existe un miembro por DNI y tipo
	private static final String QUERY_EXISTE_MIEMBRO = "SELECT 1 FROM Miembro WHERE UPPER(dni) = UPPER(?) AND tipo = ?";

// 3. Comprobar si existe un nombre de usuario
	private static final String QUERY_EXISTE_USUARIO = "SELECT 1 FROM Miembro WHERE nombre_usuario = ? COLLATE NOCASE";

// 4. Comprobar si otro miembro utiliza un nombre de usuario
	private static final String QUERY_EXISTE_USUARIO_OTRO_MIEMBRO = "SELECT 1 FROM Miembro "
			+ "WHERE nombre_usuario = ? COLLATE NOCASE " + "AND (UPPER(dni) != UPPER(?) OR tipo != ?)";

// 5. Actualizar las credenciales de un miembro
	private static final String QUERY_UPDATE_CREDENCIALES = "UPDATE Miembro SET nombre_usuario = ?, contrasena = ? "
			+ "WHERE UPPER(dni) = UPPER(?) AND tipo = ?";

// 6. Eliminar un miembro que no tenga perfiles asociados
	private static final String QUERY_DELETE_MIEMBRO = "DELETE FROM Miembro "
			+ "WHERE UPPER(dni) = UPPER(?) AND tipo = ? " + "AND NOT EXISTS (" + "    SELECT 1 FROM Accionista "
			+ "    WHERE Accionista.id_miembro = Miembro.id_miembro" + ") " + "AND NOT EXISTS ("
			+ "    SELECT 1 FROM BaseEmpleado " + "    WHERE BaseEmpleado.id_miembro = Miembro.id_miembro" + ")";

// 7. Obtener las credenciales de una cuenta por nombre de usuario
	private static final String QUERY_GET_CREDENCIALES = "SELECT dni, contrasena, tipo " + "FROM Miembro "
			+ "WHERE nombre_usuario = ? COLLATE NOCASE";

// 8. Obtener las credenciales de una cuenta por DNI y tipo
	private static final String QUERY_GET_CREDENCIALES_POR_DNI = "SELECT nombre_usuario, contrasena " + "FROM Miembro "
			+ "WHERE UPPER(dni) = UPPER(?) AND tipo = ?";

	private static final String QUERY_GET_ROL_POR_DNI = "SELECT COALESCE("
			+ "(SELECT posicion FROM EmpleadoNoDeportivo WHERE UPPER(dni) = UPPER(?)), "
			+ "(SELECT posicion FROM EmpleadoDeportivo WHERE UPPER(dni) = UPPER(?)), "
			+ "(SELECT 'ACCIONISTA' FROM Accionista WHERE UPPER(dni) = UPPER(?)), "
			+ "(SELECT 'ABONADO' FROM Abonado WHERE UPPER(dni) = UPPER(?)), "
			+ "'MIEMBRO') AS rol";

	/**
	 * Inserta un nuevo miembro en la base de datos.
	 */
	public static void insertarMiembro(Connection con, String dni, String usuario, String contrasena, String tipo)
			throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_MIEMBRO)) {
			ps.setString(1, dni);
			ps.setString(2, usuario);
			ps.setString(3, contrasena);
			ps.setString(4, tipo);
			ps.executeUpdate();
		}
	}

	/**
	 * Comprueba si existe un miembro con el DNI y tipo indicados.
	 */
	public static boolean existeMiembro(Connection con, String dni, String tipo) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_EXISTE_MIEMBRO)) {
			ps.setString(1, dni);
			ps.setString(2, tipo);

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
	public static boolean existeNombreUsuarioOtroMiembro(Connection con, String usuario, String dni, String tipo)
			throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_EXISTE_USUARIO_OTRO_MIEMBRO)) {
			ps.setString(1, usuario);
			ps.setString(2, dni);
			ps.setString(3, tipo);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Actualiza el nombre de usuario y el hash de contraseña de una cuenta.
	 */
	public static boolean actualizarCredenciales(Connection con, String dni, String tipo, String usuario,
			String contrasena) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_UPDATE_CREDENCIALES)) {
			ps.setString(1, usuario);
			ps.setString(2, contrasena);
			ps.setString(3, dni);
			ps.setString(4, tipo);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Elimina una cuenta únicamente si no tiene un perfil asociado.
	 */
	public static boolean eliminarMiembro(Connection con, String dni, String tipo) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_DELETE_MIEMBRO)) {
			ps.setString(1, dni);
			ps.setString(2, tipo);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Obtiene las credenciales de una cuenta a partir de su nombre de usuario.
	 *
	 * @return {dni, hashContrasena, tipo}, o null si no existe.
	 */
	public static String[] getCredenciales(Connection con, String nombreUsuario) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_CREDENCIALES)) {
			ps.setString(1, nombreUsuario);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String[] { rs.getString("dni"), rs.getString("contrasena"), rs.getString("tipo") };
				}
			}
		}

		return null;
	}

	/**
	 * Obtiene las credenciales de una cuenta a partir del DNI y tipo.
	 *
	 * @return {nombreUsuario, hashContrasena}, o null si no existe.
	 */
	public static String[] getCredencialesPorDni(Connection con, String dni, String tipo) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_CREDENCIALES_POR_DNI)) {
			ps.setString(1, dni);
			ps.setString(2, tipo);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String[] { rs.getString("nombre_usuario"), rs.getString("contrasena") };
				}
			}
		}

		return null;
	}

	/**
	 * Obtiene las credenciales de todas las cuentas asociadas a un DNI.
	 *
	 * @return Lista de arrays {nombreUsuario, hashContrasena, tipo}.
	 */
	public static List<String[]> getCredencialesPorDni(Connection con, String dni) throws SQLException {

		List<String[]> credenciales = new ArrayList<>();

		String sql = "SELECT nombre_usuario, contrasena, tipo " + "FROM Miembro WHERE UPPER(dni) = UPPER(?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, dni);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					credenciales.add(new String[] { rs.getString("nombre_usuario"), rs.getString("contrasena"),
							rs.getString("tipo") });
				}
			}
		}

		return credenciales;
	}

	/**
	 * Devuelve el rol del miembro a partir de su DNI.
	 */
	public static String obtenerRolPorDni(Connection con, String dni) throws SQLException {
		if (dni == null || dni.isBlank()) {
			return "SIN_ROL";
		}
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ROL_POR_DNI)) {
			ps.setString(1, dni);
			ps.setString(2, dni);
			ps.setString(3, dni);
			ps.setString(4, dni);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("rol");
				}
			}
		}
		return "MIEMBRO";
	}

	/**
	 * Devuelve el rol del miembro a partir de su DNI.
	 */
	public static String obtenerRolPorDni(Connection con, String dni) throws SQLException {
		if (dni == null || dni.isBlank()) {
			return "SIN_ROL";
		}
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ROL_POR_DNI)) {
			ps.setString(1, dni);
			ps.setString(2, dni);
			ps.setString(3, dni);
			ps.setString(4, dni);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("rol");
				}
			}
		}
		return "MIEMBRO";
	}

	/**
	 * Devuelve el rol del miembro a partir de su DNI.
	 */
	public static String obtenerRolPorDni(Connection con, String dni) throws SQLException {
		if (dni == null || dni.isBlank()) {
			return "SIN_ROL";
		}
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ROL_POR_DNI)) {
			ps.setString(1, dni);
			ps.setString(2, dni);
			ps.setString(3, dni);
			ps.setString(4, dni);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("rol");
				}
			}
		}
		return "MIEMBRO";
	}

}
