package giis.demo.jdbc.historial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.historial.Ingreso;
import giis.demo.model.historial.LineaDetalle;
import giis.demo.model.historial.TipoIngreso;

public class HistorialJdbc {

	// Ventas (entradas, merchandising, abonos, campañas) y reservas de instalaciones.
	// date() permite filtrar por dia tanto si la fecha lleva hora como si no
	private static final String QUERY_INGRESOS = "SELECT id_venta AS id, fecha AS momento, tipo, total AS importe "
			+ "FROM Venta WHERE date(fecha) BETWEEN ? AND ? "
			+ "UNION ALL "
			+ "SELECT id_reserva, fecha_registro, 'RESERVA_INSTALACIONES', importe "
			+ "FROM Reserva WHERE date(fecha_registro) BETWEEN ? AND ? "
			+ "ORDER BY momento";

	private static final String QUERY_DETALLE_MERCHANDISING = "SELECT p.nombre, lv.unidades, lv.precio_unitario "
			+ "FROM LineaVenta lv JOIN Producto p ON p.id_producto = lv.id_producto "
			+ "WHERE lv.id_venta = ? ORDER BY p.nombre";

	public static List<Ingreso> getIngresos(Connection con, LocalDate desde, LocalDate hasta) throws SQLException {
		List<Ingreso> ingresos = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(QUERY_INGRESOS)) {
			pst.setString(1, desde.toString());
			pst.setString(2, hasta.toString());
			pst.setString(3, desde.toString());
			pst.setString(4, hasta.toString());

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					ingresos.add(new Ingreso(rs.getInt("id"), TipoIngreso.valueOf(rs.getString("tipo")),
							parsearFecha(rs.getString("momento")), rs.getDouble("importe")));
				}
			}
		}
		return ingresos;
	}

	public static List<LineaDetalle> getDetalleVentaMerchandising(Connection con, int idVenta) throws SQLException {
		List<LineaDetalle> lineas = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(QUERY_DETALLE_MERCHANDISING)) {
			pst.setInt(1, idVenta);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					lineas.add(new LineaDetalle(rs.getString("nombre"), rs.getInt("unidades"),
							rs.getDouble("precio_unitario")));
				}
			}
		}
		return lineas;
	}

	/**
	 * Admite "yyyy-MM-dd HH:mm" y, para operaciones antiguas guardadas sin hora, "yyyy-MM-dd" (00:00)
	 */
	private static LocalDateTime parsearFecha(String fecha) {
		if (fecha.length() == 10) {
			return LocalDate.parse(fecha).atStartOfDay();
		}
		return LocalDateTime.parse(fecha.replace(' ', 'T'));
	}
}
