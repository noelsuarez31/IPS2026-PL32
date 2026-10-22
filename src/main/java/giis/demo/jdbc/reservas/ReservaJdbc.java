package giis.demo.jdbc.reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import giis.demo.model.reserva.Reserva;

public class ReservaJdbc {

	private static String QUERY_INSERT_BOOK = "INSERT INTO Reserva(fecha, hora_inicio, hora_fin, importe, fecha_registro,"
			+ "id_usuario, id_instalacion) VALUES (?,?,?,?,?,?,?)";

	private static final DateTimeFormatter FORMATO_REGISTRO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

			ps.setString(1, reserva.getFechaReserva().toString());
			ps.setString(2, reserva.getHoraInicio().toString());
			ps.setString(3, reserva.getHoraFin().toString());

			ps.setDouble(4, reserva.getImporte());
			ps.setString(5, reserva.getFechaRegistro().format(FORMATO_REGISTRO));
			ps.setInt(6, reserva.getUserId());
			ps.setInt(7, reserva.getInstalacionId());

			return ps.executeUpdate();

		}

	}
	
	/**
	 * Registra la reserva
	 * 
	 * @param con
	 * @param reserva
	 * @return el numero de las filas insertadas
	 * @throws SQLException
	 */
	public static int registrarReserva(Connection con, Reserva reserva) throws SQLException {

		con.setAutoCommit(false);

		try {

			int numFilasCreadas = crearReserva(con, reserva);
			con.commit();

			return numFilasCreadas;

		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
		}

	}

}
