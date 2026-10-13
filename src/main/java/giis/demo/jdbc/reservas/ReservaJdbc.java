package giis.demo.jdbc.reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import giis.demo.model.reserva.Reserva;

public class ReservaJdbc {

	private static String QUERY_INSERT_BOOK = "INSERT INTO (Id_reserva, fecha, hora_inicio, hora_fin, importe, fecha_registro,"
			+ "id_usuario, id_instalacion) VALUES (?,?,?,?,?,?,?,?)";

	/**
	 * Añade la reserva a la BBDD
	 * 
	 * @param con
	 * @param reserva
	 * @return 1 si se realiza la insercción, 0 en caso contrario
	 * @throws SQLException
	 */

	private static int crearReserva(Connection con, Reserva reserva) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_BOOK)) {

			ps.setInt(1, reserva.getInstalacionId());

			// ps.setString(2, reserva.getFechaReserva());
			// ps.setString(3, reserva.getHoraInicio());
			// ps.setString(4, reserva.getHoraFin());

			ps.setDouble(5, reserva.getImporte());
			ps.setInt(6, reserva.getUserId());
			ps.setInt(0, 0);

			return ps.executeUpdate();

		}

	}

	public static int registrarVenta(Connection con, Reserva reserva) throws SQLException {

		con.setAutoCommit(false);

		try {

			int numFilasCreadas = crearReserva(con, reserva);
			con.commit();
			
			return numFilasCreadas;
			
		} catch (SQLException e) {
			con.rollback();
			throw e;
		}

	}

}
