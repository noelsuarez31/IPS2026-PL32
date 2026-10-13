package giis.demo.jdbc.reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ComprobarDisponibilidadJdbc {

	private static final int MARGEN_MINUTOS = 90;

	private static final String QUERY_ENTRENAMIENTOS ="SELECT hora_inicio, hora_fin, id_equipo "
			  + "FROM Entrenamiento2 "
			  + "WHERE id_instalacion = ? AND fecha = ?";

	private static final String QUERY_PARTIDOS = "SELECT hora_inicio, hora_fin, id_local, id_visitante  "
			+ "FROM Partido2 WHERE id_instalacion = ? " + "AND fecha = ?";

	private static final String QUERY_RESERVAS = "SELECT fh.hora_inicio, fh.hora_fin " + "FROM Reserva r "
			+ "JOIN FranjaHoraria fh ON r.id_franja = fh.id_franja " + "WHERE r.id_instalacion = ? "
			+ "AND r.fecha = ?";

	private static final String QUERY_CHECK_TEAM_IS_LOCAL = "SELECT es_propio FROM Equipo WHERE id_equipo = ?";

	/**
	 * Comprueba si para una fecha y horas dadas la instalación está disponible
	 * @param con
	 * @param idInstalacion
	 * @param fecha
	 * @param inicio
	 * @param fin
	 * @return true si está disponible, false en caso contrario
	 * @throws SQLException
	 */
	public static boolean estaDisponible(Connection con, int idInstalacion, LocalDate fecha, LocalTime inicio,
			LocalTime fin) throws SQLException {

		return !haySolapamiento(con, QUERY_ENTRENAMIENTOS,idInstalacion, fecha, inicio, fin, true, false)
        && !haySolapamiento(con, QUERY_PARTIDOS,idInstalacion, fecha, inicio, fin, false, true) && 
        !haySolapamiento(con, QUERY_RESERVAS,idInstalacion, fecha, inicio, fin, false, false);
	}

	/**
	 * Comprueba si para hay solapamiento para una instalacion, fecha y horas de
	 * inicio dadas y otra actividad
	 * 
	 * @param con
	 * @param query
	 * @param idInstalacion
	 * @param fecha
	 * @param inicio
	 * @param fin
	 * @return true si hay solapamientos, false en caso contrario
	 * @throws SQLException
	 */
	private static boolean haySolapamiento(Connection con, String query, int idInstalacion, LocalDate fecha,
			LocalTime inicio, LocalTime fin, boolean esEntrenamiento, boolean esPartido) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, idInstalacion);
			ps.setString(2, fecha.toString());

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					LocalTime inicioExistente = LocalTime.parse(rs.getString("hora_inicio"));

					LocalTime finExistente = LocalTime.parse(rs.getString("hora_fin"));

					boolean equipoPropio = false;

					if (esEntrenamiento) {
						int idEquipo = rs.getInt("id_equipo");
						equipoPropio = isPropio(con, idEquipo);

					} else if (esPartido) {
						int idLocal = rs.getInt("id_local");
						int idVisitante = rs.getInt("id_visitante");

						equipoPropio = isPropio(con, idLocal) || isPropio(con, idVisitante);
					}

					if (equipoPropio) {
						finExistente = finExistente.plusMinutes(MARGEN_MINUTOS);
					}

					boolean solapamiento = inicio.isBefore(finExistente) && fin.isAfter(inicioExistente);

					if (solapamiento) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Metodo auxiliar para comprobar si un equipo es propio
	 * 
	 * @param con
	 * @param idEquipo
	 * @return true si es propio, false en caso contrario
	 * @throws SQLException
	 */
	private static boolean isPropio(Connection con, int idEquipo) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_CHECK_TEAM_IS_LOCAL)) {

			ps.setInt(1, idEquipo);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt("es_propio") == 1;
			}
		}
	}
}
