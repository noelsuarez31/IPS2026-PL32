package giis.demo.jdbc.accion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.accion.Accion;
import giis.demo.model.accion.Accionista;
import giis.demo.util.ApplicationException;

public class AccionJdbc {

	// 1. Obtener todos los accionistas ordenados por apellidos y nombre
	private static final String QUERY_GET_ACCIONISTAS =
	        "SELECT a.dni, a.nombre, a.apellidos, a.email, "
	        + "m.nombre_usuario, m.contrasena "
	        + "FROM Accionista a "
	        + "JOIN Miembro m ON a.dni = m.dni "
	        + "ORDER BY a.apellidos, a.nombre";
	
	// 2. Comprobar si existe un accionista por DNI y obtener sus datos
	private static final String QUERY_GET_ACCIONISTA_BY_DNI =
	        "SELECT a.dni, a.nombre, a.apellidos, a.email, "
	        + "m.nombre_usuario, m.contrasena "
	        + "FROM Accionista a "
	        + "JOIN Miembro m ON a.dni = m.dni "
	        + "WHERE UPPER(a.dni) = UPPER(?)";
	
	// 3. Obtener el número total de acciones emitidas por el club
	private static final String QUERY_GET_TOTAL_ACCIONES_CLUB = 
			"SELECT COUNT(*) FROM Accion";

	// 4. Obtener las acciones de un propietario ordenadas por fecha de adquisición
	private static final String QUERY_GET_ACCIONES_PROPIETARIO = 
			"SELECT a.id_accion, a.fecha_adquisicion, a.precio_adquisicion, a.en_venta, a.precio_venta, "
			+ "a.origen_tipo, c.nombre AS nombre_campana, c.fase AS fase_campana, a.vendedor_previo "
			+ "FROM Accion a "
			+ "LEFT JOIN Campana c ON a.id_campana = c.id_campana "
			+ "WHERE UPPER(a.dni_propietario) = UPPER(?) "
			+ "ORDER BY a.fecha_adquisicion ASC, a.id_accion ASC";

	// 5. Poner una acción a la venta fijando su precio de venta
	private static final String QUERY_PONER_ACCION_EN_VENTA = 
			"UPDATE Accion SET en_venta = 1, precio_venta = ? WHERE id_accion = ? AND en_venta = 0";

	// 6. Retirar una acción de la venta
	private static final String QUERY_RETIRAR_ACCION_DE_VENTA = 
			"UPDATE Accion SET en_venta = 0, precio_venta = NULL WHERE id_accion = ? AND en_venta = 1";

	// 7. Obtener todas las acciones actualmente puestas a la venta, excluyendo las del comprador
	private static final String QUERY_GET_ACCIONES_EN_VENTA = 
			"SELECT a.id_accion, a.fecha_adquisicion, a.precio_adquisicion, a.en_venta, a.precio_venta, "
			+ "a.origen_tipo, c.nombre AS nombre_campana, a.vendedor_previo, "
			+ "a.dni_propietario AS dni_vendedor, (ac.nombre || ' ' || ac.apellidos) AS nombre_vendedor "
			+ "FROM Accion a "
			+ "JOIN Accionista ac ON a.dni_propietario = ac.dni "
			+ "LEFT JOIN Campana c ON a.id_campana = c.id_campana "
			+ "WHERE a.en_venta = 1 AND a.precio_venta IS NOT NULL AND a.precio_venta > 0 "
			+ "AND (? IS NULL OR UPPER(a.dni_propietario) != UPPER(?)) "
			+ "ORDER BY a.id_accion ASC";

	// 8. Comprobar si una acción sigue estando a la venta y obtener sus datos para la transacción
	private static final String QUERY_CHECK_ACCION_EN_VENTA = 
			"SELECT a.id_accion, a.dni_propietario, a.en_venta, a.precio_venta, "
			+ "(ac.nombre || ' ' || ac.apellidos) AS nombre_vendedor "
			+ "FROM Accion a "
			+ "JOIN Accionista ac ON a.dni_propietario = ac.dni "
			+ "WHERE a.id_accion = ?";

	// 9. Transferir la propiedad de una acción al comprador y retirarla de la venta
	private static final String QUERY_COMPRAR_ACCION = 
			"UPDATE Accion "
			+ "SET dni_propietario = ?, fecha_adquisicion = ?, precio_adquisicion = precio_venta, "
			+ "en_venta = 0, precio_venta = NULL, origen_tipo = 'COMPRA_ACCIONISTA', "
			+ "id_campana = NULL, vendedor_previo = ? "
			+ "WHERE id_accion = ? AND en_venta = 1";

	/**
	 * Devuelve la lista de todos los accionistas registrados en el club.
	 */
	public static List<Accionista> getAccionistas(Connection con) throws SQLException {
		List<Accionista> accionistas = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ACCIONISTAS);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Accionista a = new Accionista(
						rs.getString("dni"),
						rs.getString("nombre"),
						rs.getString("apellidos"),
						rs.getString("email"),
						rs.getString("nombre_usuario"),
				        rs.getString("contrasena"));
				accionistas.add(a);
			}
		}
		return accionistas;
	}

	/**
	 * Busca un accionista por su DNI.
	 */
	public static Accionista getAccionistaByDni(Connection con, String dni) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ACCIONISTA_BY_DNI)) {
			ps.setString(1, dni);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Accionista(
					        rs.getString("dni"),
					        rs.getString("nombre"),
					        rs.getString("apellidos"),
					        rs.getString("email"),
					        rs.getString("nombre_usuario"),
					        rs.getString("contrasena")
					);
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve el número total de acciones emitidas por el club.
	 */
	public static int getTotalAccionesClub(Connection con) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_TOTAL_ACCIONES_CLUB);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	/**
	 * Devuelve las acciones en propiedad de un accionista ordenadas por fecha de adquisición.
	 */
	public static List<Accion> getAccionesPropietario(Connection con, String dni) throws SQLException {
		List<Accion> acciones = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ACCIONES_PROPIETARIO)) {
			ps.setString(1, dni);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int idAccion = rs.getInt("id_accion");
					String fechaAdquisicion = rs.getString("fecha_adquisicion");
					BigDecimal precioPagado = BigDecimal.valueOf(rs.getDouble("precio_adquisicion"));
					boolean enVenta = rs.getInt("en_venta") == 1;

					BigDecimal precioVenta = null;
					double precioVentaVal = rs.getDouble("precio_venta");
					if (!rs.wasNull()) {
						precioVenta = BigDecimal.valueOf(precioVentaVal);
					}

					// Construcción del texto descriptivo del origen
					String origenTipo = rs.getString("origen_tipo");
					String origenDesc;
					if ("CAMPANA".equalsIgnoreCase(origenTipo)) {
						String nombreCampana = rs.getString("nombre_campana");
						if (nombreCampana != null && !nombreCampana.isBlank()) {
							origenDesc = nombreCampana;
						} else {
							origenDesc = "Campaña";
						}
					} else {
						origenDesc = "Otro accionista";
					}

					Accion dto = new Accion(idAccion, fechaAdquisicion, origenDesc, precioPagado, enVenta, precioVenta);
					acciones.add(dto);
				}
			}
		}
		return acciones;
	}

	/**
	 * Pone una acción a la venta fijando su precio de venta.
	 */
	public static void ponerAccionEnVenta(Connection con, int idAccion, BigDecimal precioVenta) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_PONER_ACCION_EN_VENTA)) {
			ps.setDouble(1, precioVenta.doubleValue());
			ps.setInt(2, idAccion);
			ps.executeUpdate();
		}
	}

	/**
	 * Retira una acción de la venta restableciendo su estado y precio.
	 */
	public static void retirarAccionDeVenta(Connection con, int idAccion) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_RETIRAR_ACCION_DE_VENTA)) {
			ps.setInt(1, idAccion);
			ps.executeUpdate();
		}
	}

	/**
	 * Devuelve las acciones puestas a la venta por otros accionistas (excluyendo las del comprador).
	 */
	public static List<Accion> getAccionesEnVenta(Connection con, String dniCompradorExcluir) throws SQLException {
		List<Accion> acciones = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ACCIONES_EN_VENTA)) {
			ps.setString(1, dniCompradorExcluir);
			ps.setString(2, dniCompradorExcluir);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int idAccion = rs.getInt("id_accion");
					String fechaAdquisicion = rs.getString("fecha_adquisicion");
					BigDecimal precioPagado = BigDecimal.valueOf(rs.getDouble("precio_adquisicion"));
					boolean enVenta = rs.getInt("en_venta") == 1;

					BigDecimal precioVenta = null;
					double precioVentaVal = rs.getDouble("precio_venta");
					if (!rs.wasNull()) {
						precioVenta = BigDecimal.valueOf(precioVentaVal);
					}

					String origenTipo = rs.getString("origen_tipo");
					String origenDesc;
					if ("CAMPANA".equalsIgnoreCase(origenTipo)) {
						String nombreCampana = rs.getString("nombre_campana");
						if (nombreCampana != null && !nombreCampana.isBlank()) {
							origenDesc = nombreCampana;
						} else {
							origenDesc = "Campaña";
						}
					} else {
						origenDesc = "Otro accionista";
					}

					String dniVendedor = rs.getString("dni_vendedor");
					String nombreVendedor = rs.getString("nombre_vendedor");

					Accion dto = new Accion(idAccion, fechaAdquisicion, origenDesc, precioPagado, enVenta,
							precioVenta, dniVendedor, nombreVendedor);
					acciones.add(dto);
				}
			}
		}
		return acciones;
	}

	/**
	 * Ejecuta atómicamente la compra de una o varias acciones por parte de un accionista comprador.
	 */
	public static void comprarAcciones(Connection con, String dniComprador, List<Integer> idsAcciones) throws SQLException {
		if (idsAcciones == null || idsAcciones.isEmpty()) {
			throw new ApplicationException("Debe seleccionar al menos una acción para comprar.");
		}
		if (dniComprador == null || dniComprador.isBlank()) {
			throw new ApplicationException("Debe identificarse un accionista comprador.");
		}

		boolean prevAutoCommit = con.getAutoCommit();
		try {
			con.setAutoCommit(false);

			String fechaActual = LocalDate.now().toString();

			for (int idAccion : idsAcciones) {
				String dniVendedor = null;
				String nombreVendedor = null;
				double precioVenta = 0.0;
				boolean enVenta = false;

				try (PreparedStatement psCheck = con.prepareStatement(QUERY_CHECK_ACCION_EN_VENTA)) {
					psCheck.setInt(1, idAccion);
					try (ResultSet rs = psCheck.executeQuery()) {
						if (rs.next()) {
							dniVendedor = rs.getString("dni_propietario");
							nombreVendedor = rs.getString("nombre_vendedor");
							precioVenta = rs.getDouble("precio_venta");
							enVenta = rs.getInt("en_venta") == 1 && !rs.wasNull() && precioVenta > 0;
						}
					}
				}

				if (!enVenta) {
					throw new ApplicationException(
							"Una o varias de las acciones seleccionadas ya han sido vendidas o retiradas de la venta. No se ha realizado la compra.");
				}

				if (dniComprador.equalsIgnoreCase(dniVendedor)) {
					throw new ApplicationException("No puede comprar sus propias acciones.");
				}

				// Transferir propiedad al comprador
				try (PreparedStatement psUpdate = con.prepareStatement(QUERY_COMPRAR_ACCION)) {
					psUpdate.setString(1, dniComprador);
					psUpdate.setString(2, fechaActual);
					psUpdate.setString(3, (nombreVendedor != null) ? nombreVendedor : dniVendedor);
					psUpdate.setInt(4, idAccion);
					int updated = psUpdate.executeUpdate();
					if (updated == 0) {
						throw new ApplicationException(
								"Una o varias de las acciones seleccionadas ya han sido vendidas o retiradas de la venta. No se ha realizado la compra.");
					}
				}

			}

			con.commit();
		} catch (SQLException | ApplicationException ex) {
			try {
				con.rollback();
			} catch (SQLException rollbackEx) {
				// ignorar
			}
			throw ex;
		} finally {
			con.setAutoCommit(prevAutoCommit);
		}
	}
}
