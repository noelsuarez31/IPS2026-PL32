package giis.demo.jdbc.abonados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import giis.demo.exceptions.AbonoNoPermitidoException;
import giis.demo.model.entradas.Partido;

public class EntradaAbonadoJdbc {

	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private static final String QUERY_BUTACA_ABONO = "SELECT a.id_butaca FROM Abono a "
			+ "JOIN Abonado ab ON ab.id_abonado = a.id_abonado "
			+ "WHERE ab.dni = ? AND a.id_equipo = ? AND a.fecha_inicio <= ? AND a.fecha_fin >= ?";

	private static final String QUERY_ENTRADA_EXISTE = "SELECT 1 FROM Entrada WHERE id_partido = ? AND id_butaca = ?";

	private static final String QUERY_INSERT_VENTA = "INSERT INTO Venta (fecha, concepto, tipo, total) "
			+ "VALUES (?, ?, 'ENTRADAS', ?)";

	private static final String QUERY_INSERT_ENTRADA = "INSERT INTO Entrada (id_partido, id_venta, id_butaca, precio) "
			+ "VALUES (?, ?, ?, ?)";

	/**
	 * Registra la entrada del día del club para un abonado, en su propio asiento
	 *
	 * @param con
	 * @param dni
	 * @param partido
	 * @return el id de la venta
	 * @throws SQLException
	 * @throws AbonoNoPermitidoException si no es abonado de ese equipo y temporada,
	 *                                   o ya tiene la entrada
	 */
	public static int registrar(Connection con, String dni, Partido partido)
			throws SQLException, AbonoNoPermitidoException {

		boolean autoCommitOriginal = con.getAutoCommit();

		try {
			con.setAutoCommit(false);

			int idButaca = getButacaAbono(con, dni, partido.getId_local(), partido.getFecha());

			if (entradaExiste(con, partido.getId_partido(), idButaca)) {
				throw new AbonoNoPermitidoException("Ya existe una entrada para este abonado en este partido.");
			}

			double precio = partido.getPrecioAbonado();

			// 3. Venta + Entrada
			int idVenta = insertarVenta(con, "Entrada día del club - " + partido, precio);

			try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_ENTRADA)) {
				pst.setInt(1, partido.getId_partido());
				pst.setInt(2, idVenta);
				pst.setInt(3, idButaca);
				pst.setDouble(4, precio);
				pst.executeUpdate();
			}

			con.commit();
			return idVenta;

		} catch (SQLException | AbonoNoPermitidoException e) {
			try {
				con.rollback();
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			throw e;
		} finally {
			try {
				con.setAutoCommit(autoCommitOriginal);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Devuelve el id de la butaca del abono vigente del abonado para ese equipo y fecha
	 */
	private static int getButacaAbono(Connection con, String dni, int idLocal, LocalDate fecha)
			throws SQLException, AbonoNoPermitidoException {

		try (PreparedStatement pst = con.prepareStatement(QUERY_BUTACA_ABONO)) {
			pst.setString(1, dni);
			pst.setInt(2, idLocal);
			pst.setString(3, fecha.toString());
			pst.setString(4, fecha.toString());

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		throw new AbonoNoPermitidoException("El cliente no es abonado de este equipo en esta temporada.");
	}

	/**
	 * Comprueba si ya hay una entrada vendida para esa butaca en ese partido
	 */
	private static boolean entradaExiste(Connection con, int idPartido, int idButaca) throws SQLException {

		try (PreparedStatement pst = con.prepareStatement(QUERY_ENTRADA_EXISTE)) {
			pst.setInt(1, idPartido);
			pst.setInt(2, idButaca);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Inserta la venta y devuelve su id
	 */
	private static int insertarVenta(Connection con, String concepto, double total) throws SQLException {

		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_VENTA, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, LocalDateTime.now().format(FORMATO));
			pst.setString(2, concepto);
			pst.setDouble(3, total);
			pst.executeUpdate();

			try (ResultSet keys = pst.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
			}
		}
		throw new SQLException("No se pudo obtener el id de la venta");
	}
	
}
