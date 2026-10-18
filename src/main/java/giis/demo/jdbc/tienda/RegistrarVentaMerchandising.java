package giis.demo.jdbc.tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import giis.demo.model.tienda.Producto;

public class RegistrarVentaMerchandising {

	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final String CONCEPTO_VENTA = "Venta Merchandising";
	private static final String TIPO_VENTA = "MERCHANDISING";

	private static final String QUERY_INSERT_VENTA = "INSERT INTO Venta(fecha, concepto, tipo, total) VALUES (?,?,?,?)";
	private static final String QUERY_INSERT_LINEA = "INSERT INTO LineaVenta(id_venta, id_producto, unidades, precio_unitario) VALUES (?,?,?,?)";

	public static void registrar(Connection con, double total, Map<Producto, Integer> lineasCesta) throws SQLException {
		con.setAutoCommit(false);
		
		try {
			int idVenta = insertarVenta(con, total);
			insertarLineas(con, idVenta, lineasCesta);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
		}
	}

	private static int insertarVenta(Connection con, double total) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_VENTA, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, LocalDateTime.now().format(FORMATO_FECHA_HORA));
			ps.setString(2, CONCEPTO_VENTA);
			ps.setString(3, TIPO_VENTA);
			ps.setDouble(4, total);
			
			ps.executeUpdate();
			
			try (ResultSet rs = ps.getGeneratedKeys()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	private static void insertarLineas(Connection con, int idVenta, Map<Producto, Integer> lineasCesta) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_LINEA)) {
			for (Map.Entry<Producto, Integer> entry : lineasCesta.entrySet()) {
				ps.setInt(1, idVenta);
				ps.setInt(2, entry.getKey().getId_producto());
				ps.setInt(3, entry.getValue());
				ps.setDouble(4, entry.getKey().getPrecio());
				ps.executeUpdate();
			}
		}
	}
}
