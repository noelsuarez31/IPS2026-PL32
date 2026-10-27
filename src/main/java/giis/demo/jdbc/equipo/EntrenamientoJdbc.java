package giis.demo.jdbc.equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import giis.demo.exceptions.PersistenceException;
import giis.demo.model.equipo.EventoOcupacion;

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
	private static final String QUERY_PARTIDOS_INSTALACION = 
			"SELECT p.hora_inicio, p.hora_fin, eqL.nombre AS local, eqV.nombre AS visitante "
			+ "FROM Partido p JOIN Equipo eqL ON p.id_local = eqL.id_equipo "
			+ "JOIN Equipo eqV ON p.id_visitante = eqV.id_equipo "
			+ "WHERE p.id_instalacion = ? AND p.fecha = ?";
	private static final String QUERY_ENTRENAMIENTOS_INSTALACION = 
			"SELECT e.hora_inicio, e.hora_fin, eq.nombre AS equipo, e.id_equipo "
			+ "FROM Entrenamiento e JOIN Equipo eq ON e.id_equipo = eq.id_equipo "
			+ "WHERE e.id_instalacion = ? AND e.fecha = ?";
	private static final String QUERY_TEAM_PLAYER_INTERVIEWS = 
			"SELECT be.nombre, be.apellido, f.hora_inicio, f.hora_fin, ent.nombre_medio "
			+ "FROM Entrevista ent JOIN FranjaHoraria f ON ent.id_franja = f.id_franja "
			+ "JOIN EmpleadoDeportivo ed ON f.dni_jugador = ed.dni "
			+ "JOIN BaseEmpleado be ON ed.dni = be.dni "
			+ "WHERE ed.id_equipo = ? AND f.fecha = ?";
	private static final String QUERY_RESERVAS_INSTALACION = 
			"SELECT r.hora_inicio, r.hora_fin, u.nombre "
			+ "FROM Reserva r JOIN Usuario u ON r.id_usuario = u.id_usuario "
			+ "WHERE r.id_instalacion = ? AND r.fecha = ?";
	private static final String QUERY_JARDINERIA_ESPECIFICOS = 
			"SELECT be.nombre, be.apellido, he.hora_inicio, he.hora_fin "
			+ "FROM HorarioEspecifico he JOIN EmpleadoNoDeportivo end ON he.dni_empleado = end.dni "
			+ "JOIN BaseEmpleado be ON end.dni = be.dni "
			+ "WHERE end.posicion = 'JARDINERIA' AND he.fecha = ?";
	private static final String QUERY_JARDINERIA_PERIODICOS = 
			"SELECT be.nombre, be.apellido, hp.hora_inicio, hp.hora_fin "
			+ "FROM HorarioPeriodico hp JOIN EmpleadoNoDeportivo end ON hp.dni_empleado = end.dni "
			+ "JOIN BaseEmpleado be ON end.dni = be.dni "
			+ "WHERE end.posicion = 'JARDINERIA' AND hp.dia_semana = ? "
			+ "AND hp.dni_empleado NOT IN (SELECT he.dni_empleado FROM HorarioEspecifico he WHERE he.fecha = ?)";
	private static final String QUERY_INSERT_TRAINING = 
			"INSERT INTO Entrenamiento (fecha, hora_inicio, hora_fin, id_equipo, id_instalacion) "
			+ "VALUES (?, ?, ?, ?, ?)";

	/**
	 * Devuelve una lista de los dnis de los entrenadores que tienen equipos asociados
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static List<String> obtenerDnisEntrenadoresConEquipo(Connection con) 
			throws SQLException {
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
	 * Devuelve el nombre del equipo al que entrena el entrenador
	 * @param con
	 * @param dniEntrenador
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerNombreEquipoPorDniEntrenador(Connection con, String dniEntrenador) 
			throws SQLException {
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
	 * Devuelve el id del equipo a partir del dni del entrenador
	 * @param con
	 * @param dniEntrenador
	 * @return
	 * @throws SQLException
	 */
	public static int obtenerIdEquipoPorDniEntrenador(Connection con, String dniEntrenador) 
			throws SQLException {
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
	 * Devuelve una lista con los eventos que ocupan la instalacion seleccionada
	 * en la fecha seleccionada
	 */
	public static List<EventoOcupacion> obtenerEventosOcupacion(Connection con, 
			int idEquipo, int idInstalacion, LocalDate fecha) throws SQLException {
		
		List<EventoOcupacion> eventos = new ArrayList<>();
		// 1. Partidos (No sobreescribible)
		try (PreparedStatement pst = con.prepareStatement(QUERY_PARTIDOS_INSTALACION)) {
			pst.setInt(1, idInstalacion);
			pst.setString(2, fecha.toString());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					eventos.add(new EventoOcupacion(parseHora(rs.getString("hora_inicio")), parseHora(rs.getString("hora_fin")),
							"Partido", rs.getString("local") + " vs " + rs.getString("visitante"), false));
				}
			}
		}
		
		// 2. Entrenamientos en la instalación (No sobreescribible)
		try (PreparedStatement pst = con.prepareStatement(QUERY_ENTRENAMIENTOS_INSTALACION)) {
			pst.setInt(1, idInstalacion);
			pst.setString(2, fecha.toString());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					String eqNombre = rs.getString("equipo");
					eventos.add(new EventoOcupacion(parseHora(rs.getString("hora_inicio")), parseHora(rs.getString("hora_fin")),
							"Entrenamiento", "Equipo: " + eqNombre, false));
				}
			}
		}
		
		// 3. Entrevistas de jugadores del equipo (No sobreescribible)
		if (idEquipo > 0) {
			try (PreparedStatement pst = con.prepareStatement(QUERY_TEAM_PLAYER_INTERVIEWS)) {
				pst.setInt(1, idEquipo);
				pst.setString(2, fecha.toString());
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String nom = rs.getString("nombre") + " " + rs.getString("apellido") + " (" + rs.getString("nombre_medio") + ")";
						eventos.add(new EventoOcupacion(parseHora(rs.getString("hora_inicio")), parseHora(rs.getString("hora_fin")),
								"Entrevista", nom, false));
					}
				}
			}
		}
		
		// 4. Reservas externas (Sobreescribible)
		try (PreparedStatement pst = con.prepareStatement(QUERY_RESERVAS_INSTALACION)) {
			pst.setInt(1, idInstalacion);
			pst.setString(2, fecha.toString());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					eventos.add(new EventoOcupacion(parseHora(rs.getString("hora_inicio")), parseHora(rs.getString("hora_fin")),
							"Reserva externa", "Socio: " + rs.getString("nombre"), true));
				}
			}
		}
		
		// 5. Jardinería (Sobreescribible)
		try (PreparedStatement pst = con.prepareStatement(QUERY_JARDINERIA_ESPECIFICOS)) {
			pst.setString(1, fecha.toString());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					String det = rs.getString("nombre") + " " + rs.getString("apellido") + " (Específico)";
					eventos.add(new EventoOcupacion(parseHora(rs.getString("hora_inicio")), parseHora(rs.getString("hora_fin")),
							"Jardinería", det, true));
				}
			}
		}
		try (PreparedStatement pst = con.prepareStatement(QUERY_JARDINERIA_PERIODICOS)) {
			pst.setInt(1, fecha.getDayOfWeek().getValue());
			pst.setString(2, fecha.toString());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					String det = rs.getString("nombre") + " " + rs.getString("apellido") + " (Periódico)";
					eventos.add(new EventoOcupacion(parseHora(rs.getString("hora_inicio")), parseHora(rs.getString("hora_fin")),
							"Jardinería", det, true));
				}
			}
		}
		
		eventos.sort(Comparator.comparing(EventoOcupacion::getHoraInicio));
		return eventos;
	}
	
	/**
	 * Comprueba las colisiones del entrenamiento en la fecha, hora e instalacion
	 * seleccionada con otros eventos de ocupacion
	 * @param con
	 * @param fecha
	 * @param horaInicio
	 * @param horaFin
	 * @param idEquipo
	 * @param idInstalacion
	 * @throws SQLException
	 * @throws PersistenceException
	 */
	public static void comprobarColisiones(Connection con, LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin, int idEquipo, int idInstalacion) throws SQLException, PersistenceException {
		List<EventoOcupacion> eventos = obtenerEventosOcupacion(con, idEquipo, idInstalacion, fecha);
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
		for (EventoOcupacion e : eventos) {
			if (!e.isSobreescribible()) {
				if (horaInicio.isBefore(e.getHoraFin()) && horaFin.isAfter(e.getHoraInicio())) {
					throw new PersistenceException("Existe una colisión con " + e.getTipo().toLowerCase() + " '"
							+ e.getDetalle() + "' (" + e.getHoraInicio().format(fmt) + " - " + e.getHoraFin().format(fmt)
							+ ").\nPor favor, escoja otro horario, ya que hay colisión.");
				}
			}
		}
	}
	
	/**
	 * Guarda el entrenamiento creado en la BBDD
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
	
	private static LocalTime parseHora(String hStr) {
		return LocalTime.parse(hStr, DateTimeFormatter.ofPattern(hStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
	}
}
