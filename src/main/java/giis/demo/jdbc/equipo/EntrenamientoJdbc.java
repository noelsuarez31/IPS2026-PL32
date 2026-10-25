package giis.demo.jdbc.equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EntrenamientoJdbc {

	private static final String QUERY_TRAINERS_WITH_TEAM = 
			"SELECT ed.dni FROM EmpleadoDeportivo ed "
			+ "WHERE ed.posicion = 'ENTRENADOR' AND ed.id_equipo IS NOT NULL ORDER BY ed.dni";

	private static final String QUERY_TEAM_NAME_BY_TRAINER = 
			"SELECT e.nombre FROM Equipo e "
			+ "JOIN EmpleadoDeportivo ed ON e.id_equipo = ed.id_equipo "
			+ "WHERE ed.dni = ?";

	private static final String QUERY_TEAM_ID_BY_TRAINER = 
			"SELECT id_equipo FROM EmpleadoDeportivo WHERE dni = ?";

	private static final String QUERY_TEAM_PLAYER_INTERVIEWS = 
			"SELECT be.nombre, be.apellido, f.hora_inicio, f.hora_fin, ent.nombre_medio "
			+ "FROM Entrevista ent "
			+ "JOIN FranjaHoraria f ON ent.id_franja = f.id_franja "
			+ "JOIN EmpleadoDeportivo ed ON f.dni_jugador = ed.dni "
			+ "JOIN BaseEmpleado be ON ed.dni = be.dni "
			+ "WHERE ed.id_equipo = ? AND f.fecha = ? "
			+ "ORDER BY f.hora_inicio";

	private static final String QUERY_INSERT_TRAINING = 
			"INSERT INTO Entrenamiento (fecha, hora_inicio, hora_fin, id_equipo, id_instalacion) "
			+ "VALUES (?, ?, ?, ?, ?)";

	/**
	 * Devuelve una lista de entrenadores que tienen un equipo asignado en el sistema
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static List<String> obtenerDnisEntrenadoresConEquipo(Connection con) throws SQLException {
		List<String> dnis = new ArrayList<>();
		try (PreparedStatement pst = con.prepareStatement(QUERY_TRAINERS_WITH_TEAM);
			 ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				dnis.add(rs.getString("dni"));
			}
		}
		return dnis;
	}

	/**
	 * Devuelve un string con el nombre del equipo al que entrena el entrenador con dni
	 * pasado por parametro
	 * @param con
	 * @param dniEntrenador
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerNombreEquipoPorDniEntrenador(Connection con, String dniEntrenador) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_TEAM_NAME_BY_TRAINER)) {
			pst.setString(1, dniEntrenador);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getString("nombre");
				}
			}
		}
		return "Sin equipo";
	}

	/**
	 * Devuelve el id del equipo que entrena el entrenador que tiene por nif
	 * el pasado por parametro
	 * @param con
	 * @param dniEntrenador
	 * @return
	 * @throws SQLException
	 */
	public static int obtenerIdEquipoPorDniEntrenador(Connection con, String dniEntrenador) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_TEAM_ID_BY_TRAINER)) {
			pst.setString(1, dniEntrenador);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id_equipo");
				}
			}
		}
		return 0;
	}

	/**
	 * Devuelve una lista con las entrevistas que tienen asignadas los jugadores
	 * que pertenecen al equipo con id el pasado por parametro para la 
	 * fecha pasada por parametro
	 * @param con
	 * @param idEquipo
	 * @param fecha
	 * @return
	 * @throws SQLException
	 */
	public static List<String> obtenerEntrevistasJugadoresEquipo(Connection con, int idEquipo, LocalDate fecha) throws SQLException {
		List<String> entrevistas = new ArrayList<>();
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

		try (PreparedStatement pst = con.prepareStatement(QUERY_TEAM_PLAYER_INTERVIEWS)) {
			pst.setInt(1, idEquipo);
			pst.setString(2, fecha.toString());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String medio = rs.getString("nombre_medio");
					String hIniStr = rs.getString("hora_inicio");
					String hFinStr = rs.getString("hora_fin");

					LocalTime hIni = LocalTime.parse(hIniStr, DateTimeFormatter.ofPattern(hIniStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					LocalTime hFin = LocalTime.parse(hFinStr, DateTimeFormatter.ofPattern(hFinStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));

					entrevistas.add("• [ENTREVISTA] " + nombre + " " + apellido + " (" + medio + "): "
							+ hIni.format(formatoHora) + " a " + hFin.format(formatoHora));
				}
			}
		}
		return entrevistas;
	}

	/**
	 * Guarda el entrenamiento seleccionado para la fecha, horas, equipo e
	 * instalacion seleccionados
	 * @param con
	 * @param fecha
	 * @param horaInicio
	 * @param horaFin
	 * @param idEquipo
	 * @param idInstalacion
	 * @throws SQLException
	 */
	public static void guardarEntrenamiento(Connection con, LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin, int idEquipo, int idInstalacion) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_TRAINING)) {
			pst.setString(1, fecha.toString());
			pst.setString(2, horaInicio.toString());
			pst.setString(3, horaFin.toString());
			pst.setInt(4, idEquipo);
			pst.setInt(5, idInstalacion);

			int filas = pst.executeUpdate();
			if (filas != 1) {
				throw new SQLException("Error al registrar el entrenamiento en la base de datos.");
			}
		}
	}
}
