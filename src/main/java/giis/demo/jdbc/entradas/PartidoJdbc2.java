package giis.demo.jdbc.entradas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.entradas.Partido2;

public class PartidoJdbc2 {

	// CHECK PARTIDO2

	private static final String QUERY_MATCH = "select id_partido, fecha, hora_inicio, hora_fin,"
			+ "id_local, id_visitante, eq1.nombre as nombre_local, eq2.nombre as nombre_visitante from partido2 p, Equipo eq1,\r\n"
			+ "Equipo eq2 WHERE p.id_local = eq1.id_equipo AND p.id_visitante = eq2.id_equipo "
			+ "AND p.fecha >= ? ORDER BY p.fecha";

	/**
	 * Obtiene todos los partidos disponibles
	 * 
	 * @return los partido disponibles
	 * @throws SQLException
	 */
	public static List<Partido2> getPartidos(Connection con) throws SQLException {

		List<Partido2> partidos = new ArrayList<Partido2>();

		try (PreparedStatement ps = con.prepareStatement(QUERY_MATCH)) {

			ps.setString(1, LocalDate.now().toString());// Para mostrar partidos que
			// Aun no se haya jugado

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Partido2 partido = new Partido2(

							rs.getInt("id_partido"), LocalDate.parse(rs.getString("fecha")),
							LocalTime.parse(rs.getString("hora_inicio")), LocalTime.parse(rs.getString("hora_fin")),
							rs.getInt("id_local"), rs.getInt("id_visitante"), rs.getString("nombre_local"),
							rs.getString("nombre_visitante")

					);

					partidos.add(partido);

				}

			}

			return partidos;

		}

	}

}
