package giis.demo.jdbc.medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.medico.EvolucionLesion;
import giis.demo.model.medico.Lesion;

public class LesionJdbc {

	private static final String QUERY_GET_LESIONES = "SELECT id, dni_jugador, fecha, tipo, origen, estado, fecha_alta FROM Lesion "
			+ "WHERE dni_jugador = ? ORDER BY fecha DESC";

	private static final String QUERY_GET_EVOLUCION = "SELECT fecha, valoracion, actualizacion FROM EvolucionLesion "
			+ "WHERE id_lesion = ? ORDER BY fecha ASC";

	private static final String QUERY_INSERT_LESION = "INSERT INTO Lesion (dni_jugador, fecha, tipo, origen, estado) VALUES (?, ?, ?, ?, ?)";

	private static final String QUERY_INSERT_EVOLUCION = "INSERT INTO EvolucionLesion (id_lesion, fecha, valoracion, actualizacion) VALUES (?, ?, ?, ?)";

	private static final String QUERY_ACTUALIZAR_ESTADO = "UPDATE Lesion SET estado = ?, fecha_alta = ? WHERE id = ?";

	private static final String QUERY_ID_EQUIPO = "SELECT id_equipo FROM EmpleadoDeportivo WHERE dni = ?";

	private static final String QUERY_PARTIDOS_EQUIPO_DIA = "SELECT p.hora_inicio, p.hora_fin, el.nombre, ev.nombre "
			+ "FROM Partido p " + "JOIN Equipo el ON p.id_local = el.id_equipo "
			+ "JOIN Equipo ev ON p.id_visitante = ev.id_equipo "
			+ "WHERE p.fecha = ? AND (p.id_local = ? OR p.id_visitante = ?) " + "ORDER BY p.hora_inicio";

	private static final String QUERY_ENTRENAMIENTOS_EQUIPO_DIA = "SELECT e.hora_inicio, e.hora_fin, i.nombre "
			+ "FROM Entrenamiento e " + "JOIN Instalacion i ON e.id_instalacion = i.id_instalacion "
			+ "WHERE e.fecha = ? AND e.id_equipo = ? " + "ORDER BY e.hora_inicio";

	/**
	 * Obtiene las lesiones de un jugador dado su dni
	 * 
	 * @param con
	 * @param dniJugador
	 * @return una lista de las lesiones
	 * @throws SQLException
	 */
	public static List<Lesion> getLesionesJugador(Connection con, String dniJugador) throws SQLException {

		List<Lesion> lesiones = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(QUERY_GET_LESIONES)) {

			pst.setString(1, dniJugador);

			try (ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {

					String strFecha = rs.getString("fecha");
					LocalDate fecha = (strFecha != null && !strFecha.isBlank()) ? LocalDate.parse(strFecha) : null;

					String strAlta = rs.getString("fecha_alta");
					LocalDate fechaAlta = (strAlta != null && !strAlta.isBlank()) ? LocalDate.parse(strAlta) : null;
					lesiones.add(new Lesion(rs.getInt("id"), rs.getString("dni_jugador"), fecha, rs.getString("tipo"),
							rs.getString("origen"), rs.getString("estado"), fechaAlta));
				}
			}
		}
		return lesiones;
	}

	/**
	 * Obtiene las evoluciones de una lesion dado su id
	 * 
	 * @param con
	 * @param idLesion
	 * @return las evoluciones
	 * @throws SQLException
	 */
	public static List<EvolucionLesion> getEvolucionLesion(Connection con, int idLesion) throws SQLException {

		List<EvolucionLesion> evolucion = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(QUERY_GET_EVOLUCION)) {
			pst.setInt(1, idLesion);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					evolucion.add(new EvolucionLesion(LocalDate.parse(rs.getString("fecha")),
							rs.getString("valoracion"), rs.getString("actualizacion")));
				}
			}
		}
		return evolucion;
	}

	/**
	 * Ontiene el id del equipo de un jugador dado su dni
	 * 
	 * @param con
	 * @param dni
	 * @return id_equipo
	 * @throws SQLException
	 */
	public static int getIdEquipoJugador(Connection con, String dni) throws SQLException {

		try (PreparedStatement pst = con.prepareStatement(QUERY_ID_EQUIPO)) {
			pst.setString(1, dni);

			try (ResultSet rs = pst.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	/**
	 * Obtiene los partidos que un equipo juega un dia concreto
	 * 
	 * @param con
	 * @param idEquipo
	 * @param fecha
	 * @return lista con los partido
	 * @throws SQLException
	 */
	public static List<String> getPartidosEquipoDia(Connection con, int idEquipo, LocalDate fecha) throws SQLException {
		List<String> eventos = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(QUERY_PARTIDOS_EQUIPO_DIA)) {
			pst.setString(1, fecha.toString());
			pst.setInt(2, idEquipo);
			pst.setInt(3, idEquipo);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					eventos.add(rs.getString(3) + " - " + rs.getString(4) + " (" + hhmm(rs.getString(1)) + "-"
							+ hhmm(rs.getString(2)) + ")");
				}
			}
		}
		return eventos;
	}

	/**
	 * Devuelve una lista con los entrenamiento de un equipo un dia concreto
	 * 
	 * @param con
	 * @param idEquipo
	 * @param fecha
	 * @return entrenamientos de un equipo
	 * @throws SQLException
	 */
	public static List<String> getEntrenamientosEquipoDia(Connection con, int idEquipo, LocalDate fecha)
			throws SQLException {
		List<String> eventos = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(QUERY_ENTRENAMIENTOS_EQUIPO_DIA)) {
			pst.setString(1, fecha.toString());
			pst.setInt(2, idEquipo);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					eventos.add(rs.getString(3) + " (" + hhmm(rs.getString(1)) + "-" + hhmm(rs.getString(2)) + ")");
				}
			}
		}
		return eventos;
	}

	/**
	 * Inserta una lesión
	 * 
	 * @param con
	 * @param dniJugador
	 * @param fecha
	 * @param tipo
	 * @param origen
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public static int insertarLesion(Connection con, String dniJugador, LocalDate fecha, String tipo, String origen,
			String estado) throws SQLException {

		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_LESION, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, dniJugador);
			pst.setString(2, fecha.toString());
			pst.setString(3, tipo);
			pst.setString(4, origen);
			pst.setString(5, estado);
			pst.executeUpdate();
			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		throw new SQLException("No se ha podido obtener el id de la lesión insertada");
	}

	/**
	 * Inserta una evolución de una lesión dado su id
	 * 
	 * @param con
	 * @param idLesion
	 * @param ev
	 * @throws SQLException
	 */
	public static void insertarEvolucion(Connection con, int idLesion, EvolucionLesion ev) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_EVOLUCION)) {
			pst.setInt(1, idLesion);
			pst.setString(2, ev.getFecha().toString());
			pst.setString(3, ev.getValoracion());
			pst.setString(4, ev.getActualizacion());
			pst.executeUpdate();
		}
	}

	/**
	 * Actualiza el estado de una lesión
	 * 
	 * @param con
	 * @param idLesion
	 * @param estado
	 * @param fechaAlta
	 * @throws SQLException
	 */
	public static void actualizarEstado(Connection con, int idLesion, String estado, LocalDate fechaAlta)
			throws SQLException {

		try (PreparedStatement pst = con.prepareStatement(QUERY_ACTUALIZAR_ESTADO)) {
			pst.setString(1, estado);
			if (fechaAlta == null) {
				pst.setNull(2, Types.VARCHAR);
			} else {
				pst.setString(2, fechaAlta.toString());
			}
			pst.setInt(3, idLesion);
			pst.executeUpdate();
		}
	}

	/**
	 * Formatea la hora para que todas tengan el mismo formato
	 * 
	 * @param hora
	 * @return la hora formateada
	 */
	private static String hhmm(String hora) {
		return hora != null && hora.length() > 5 ? hora.substring(0, 5) : hora;
	}

}
