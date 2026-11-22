package giis.demo.jdbc.medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.medico.EvolucionLesion;
import giis.demo.model.medico.Lesion;

public class LesionJdbc {
	
	private static final String QUERY_GET_LESIONES =
			"SELECT id, dni_jugador, fecha, tipo, origen, estado FROM Lesion "
			+ "WHERE dni_jugador = ? ORDER BY fecha DESC";

	private static final String QUERY_GET_EVOLUCION =
			"SELECT fecha, valoracion, actualizacion FROM EvolucionLesion "
			+ "WHERE id_lesion = ? ORDER BY fecha ASC";
	
	public static List<Lesion> getLesionesJugador(Connection con, String dniJugador) throws SQLException {
		List<Lesion> lesiones = new ArrayList<>();
		try (PreparedStatement pst = con.prepareStatement(QUERY_GET_LESIONES)) {
			pst.setString(1, dniJugador);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					lesiones.add(new Lesion(rs.getInt("id"), rs.getString("dni_jugador"),
							LocalDate.parse(rs.getString("fecha")), rs.getString("tipo"),
							rs.getString("origen"), rs.getString("estado")));
				}
			}
		}
		return lesiones;
	}

	
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

}
