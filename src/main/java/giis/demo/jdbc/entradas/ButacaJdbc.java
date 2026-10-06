package giis.demo.jdbc.entradas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.entradas.Butaca;
import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;


public class ButacaJdbc {

	
	private static final String QUERY_FREE_BUTACAS = "SELECT id_butaca, fila, asiento FROM butaca "
			+ "WHERE tribuna = ? AND seccion = ? "
			+ "AND id_butaca NOT IN (SELECT id_butaca FROM entrada WHERE id_partido = ?) " + "ORDER BY fila, asiento";

	
	/**
	 * Obtiene todas las butacas disponibles
	 * @param idPartido
	 * @param tribuna
	 * @param seccion
	 * @return butacas disponibles
	 * @throws SQLException
	 */
	public static List<Butaca> getFreeButacas(Connection con, int idPartido, TipoTribuna tribuna, TipoSeccion seccion) throws SQLException {

		List<Butaca> butacasLibres = new ArrayList<Butaca>();

		try (PreparedStatement ps = con.prepareStatement(QUERY_FREE_BUTACAS)) {

			ps.setString(1, String.valueOf(tribuna));
			ps.setString(2, String.valueOf(seccion));
			ps.setInt(3, idPartido);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					Butaca butaca = new Butaca(rs.getInt("id_butaca"), tribuna, seccion, rs.getInt("fila"),
							rs.getInt("asiento"));

					butacasLibres.add(butaca);
				}
			}

			return butacasLibres;

		}

	}

}
