package giis.demo.model.historial;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import giis.demo.exceptions.HistorialException;
import giis.demo.jdbc.JDBC;

public class HistorialVentas {

	private JDBC jdbc = new JDBC();

	/**
	 * Devuelve los ingresos producidos entre dos fechas (ambas incluidas),
	 * ordenados cronologicamente
	 * @throws HistorialException si el rango es invalido o falla la BBDD
	 */
	public List<Ingreso> obtenerIngresos(LocalDate desde, LocalDate hasta) {
		if (desde == null || hasta == null) {
			throw new HistorialException("Debe indicar la fecha de inicio y la fecha de fin");
		}
		if (desde.isAfter(hasta)) {
			throw new HistorialException("La fecha de inicio no puede ser posterior a la fecha de fin");
		}
		try {
			return jdbc.getIngresos(desde, hasta);
		} catch (SQLException e) {
			throw new HistorialException("Error al consultar el historial de ventas: " + e.getMessage());
		}
	}

	/**
	 * Suma la cuantia de todos los ingresos
	 */
	public double calcularTotal(List<Ingreso> ingresos) {
		double total = 0.0;
		for (Ingreso ingreso : ingresos) {
			total += ingreso.getImporte();
		}
		return total;
	}

	/**
	 * Devuelve los productos, unidades y precios de una venta de merchandising.
	 * El resto de conceptos no tienen detalle adicional
	 * @throws HistorialException si el ingreso no es de merchandising o falla la BBDD
	 */
	public List<LineaDetalle> obtenerDetalleMerchandising(Ingreso ingreso) {
		if (ingreso == null || !ingreso.esMerchandising()) {
			throw new HistorialException("Solo las ventas de merchandising tienen detalle");
		}
		try {
			return jdbc.getDetalleVentaMerchandising(ingreso.getIdOperacion());
		} catch (SQLException e) {
			throw new HistorialException("Error al consultar el detalle de la venta: " + e.getMessage());
		}
	}

	public void close() throws SQLException {
		jdbc.close();
	}
}
