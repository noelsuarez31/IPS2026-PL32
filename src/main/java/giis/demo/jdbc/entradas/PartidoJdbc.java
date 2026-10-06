package giis.demo.jdbc.entradas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.entradas.Partido;

public class PartidoJdbc implements ServiceJdbc {

	private static final String QUERY_MATCH = "select id_partido, fecha, id_local, id_visitante, eq1.name, eq2.name from partido p, Equipo eq1,\r\n"
			+ "Equipo eq2 WHERE p.id_local = eq1.id_equipo AND p.id_visitante = eq2.id_equipo AND p.fecha >= ? ORDER BY p.fecha";

	/**
	 * Obtiene todos los partidos disponibles
	 * @return los partido disponibles
	 * @throws SQLException
	 */
	public static List<Partido> getPartidos(Connection con) throws SQLException {

		List<Partido> partidos = new ArrayList<Partido>();

			
		
			try(PreparedStatement ps = con.prepareStatement(QUERY_MATCH)) {

			ps.setString(1, LocalDate.now().toString());//Para mostrar partidos que
			//Aun no se haya jugado
			
			try(ResultSet rs = ps.executeQuery()){
				
				while (rs.next()) {

					Partido partido = new Partido(rs.getInt(1), Date.valueOf(rs.getString(2)), rs.getInt(3), rs.getInt(4),
							rs.getString(5), rs.getString(6));

					partidos.add(partido);
				
			}

			

			}

			return partidos;

		}

	}

}
