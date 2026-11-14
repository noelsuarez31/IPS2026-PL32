package giis.demo.jdbc.accion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import giis.demo.jdbc.login.MiembroJdbc;
import giis.demo.model.accion.Accionista;
import giis.demo.model.accion.Campana;
import giis.demo.model.accion.ResultadoCompra;
import giis.demo.util.ApplicationException;

/**
 * Acceso a datos de la compra de acciones de la campaña activa.
 */
public class CompraCampanaJdbc {

	public static final String MSG_CAMPANA_CAMBIADA = "La campaña ha cambiado desde que se mostró la información.\n"
			+ "No se ha registrado la compra. Se ha actualizado la información mostrada.";

	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	// Acciones que aun puede comprar un accionista en la fase 1 (sin fila = 0)
	private static final String QUERY_CUPO_FASE1 = "SELECT acciones_iniciales - acciones_compradas FROM CampanaAccionista WHERE id_campana = ? AND dni = ?";

	// Reserva las acciones en la campaña: solo si sigue activa, en la fase que se
	// vio y quedan suficientes
	private static final String UPDATE_RESERVAR_ACCIONES = "UPDATE Campana SET acciones_vendidas = acciones_vendidas + ? "
			+ "WHERE id_campana = ? AND estado = 'ACTIVA' AND fase = ? AND acciones_vendidas + ? <= num_acciones";

	// Descuenta del cupo de la fase 1: solo si le queda cupo suficiente
	private static final String UPDATE_DESCONTAR_CUPO = "UPDATE CampanaAccionista SET acciones_compradas = acciones_compradas + ? "
			+ "WHERE id_campana = ? AND dni = ? AND acciones_iniciales - acciones_compradas >= ?";

	private static final String QUERY_EXISTE_ACCIONISTA = "SELECT 1 FROM Accionista WHERE dni = ?";

	private static final String INSERT_ACCIONISTA = "INSERT INTO Accionista (dni, nombre, apellidos, email) VALUES (?, ?, ?, ?)";

	// Guarda el email solo si el accionista no tenia ninguno
	private static final String UPDATE_EMAIL_SI_VACIO = "UPDATE Accionista SET email = ? WHERE dni = ? AND (email IS NULL OR TRIM(email) = '')";

	private static final String INSERT_ACCION = "INSERT INTO Accion (dni_propietario, fecha_adquisicion, precio_adquisicion, en_venta, precio_venta, "
			+ "origen_tipo, id_campana, vendedor_previo) VALUES (?, ?, ?, 0, NULL, 'CAMPANA', ?, NULL)";

	private static final String INSERT_VENTA = "INSERT INTO Venta (fecha, concepto, tipo, total) VALUES (?, ?, 'CAMPANAS_ACCIONISTAS', ?)";

	// Si ya se han vendido todas las acciones, la campaña se cierra sola
	private static final String UPDATE_CERRAR_SI_AGOTADA = "UPDATE Campana SET estado = 'CERRADA' "
			+ "WHERE id_campana = ? AND estado = 'ACTIVA' AND acciones_vendidas >= num_acciones";

	/**
	 * Acciones que todavia puede comprar el accionista en la fase 1 de la campaña
	 * (las que tenia al lanzarse menos las que ya ha comprado). 0 si no consta.
	 */
	public static int getCupoFase1(Connection con, int idCampana, String dni) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_CUPO_FASE1)) {
			ps.setInt(1, idCampana);
			ps.setString(2, dni);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Math.max(0, rs.getInt(1)) : 0;
			}
		}
	}

	/**
	 * Registra en una unica transaccion la compra de acciones de la campaña:
	 * reserva las acciones, registra al comprador si es nuevo, crea cada acción,
	 * registra el ingreso y cierra la campaña si se ha agotado.
	 * 
	 * @param campana   la campaña tal y como la vio el comprador (id, fase y
	 *                  precio)
	 * @param comprador datos del comprador
	 * @param esNuevo   true si hay que registrarlo como accionista
	 * @throws ApplicationException si la campaña ha cambiado (fase, cierre,
	 *                              acciones agotadas), si el comprador supera su
	 *                              cupo de la fase 1 o si el DNI ya estaba
	 *                              registrado
	 */
	public static ResultadoCompra comprar(Connection con, Campana campana, Accionista comprador, boolean esNuevo,
			int unidades, String usuario, String hashContrasena) throws SQLException {
		
		boolean autoCommitOriginal = con.getAutoCommit();
		try {
			con.setAutoCommit(false);

			if (!reservarAcciones(con, campana, unidades))
				throw new ApplicationException(MSG_CAMPANA_CAMBIADA);

			if (campana.getFase() == Campana.PRIMERA_FASE
					&& !descontarCupo(con, campana.getId(), comprador.getDni(), unidades))
				throw new ApplicationException(MSG_CAMPANA_CAMBIADA);

			if (esNuevo) {
				if (existeAccionista(con, comprador.getDni()))
					throw new ApplicationException("El DNI " + comprador.getDni()
							+ " ya está registrado como accionista.\nVuelva a identificarlo.");
				
				// Si todavía no tiene cuenta, creamos sus credenciales.
			    if (!MiembroJdbc.existeMiembro(con, comprador.getDni())) {
			        MiembroJdbc.insertarMiembro(
			                con,
			                comprador.getDni(),
			                usuario,
			                hashContrasena
			        );
			    }
				
				insertarAccionista(con, comprador);
			
			} else {
				guardarEmailSiVacio(con, comprador);
			}

			List<Integer> ids = insertarAcciones(con, campana, comprador.getDni(), unidades);
			double total = Math.round(unidades * campana.getPrecio() * 100.0) / 100.0;
			insertarVenta(con, campana, total);
			boolean cerrada = cerrarSiAgotada(con, campana.getId());

			con.commit();
			return new ResultadoCompra(ids, campana.getPrecio(), total, cerrada);
		} catch (SQLException | RuntimeException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(autoCommitOriginal);
		}
	}

	private static boolean reservarAcciones(Connection con, Campana campana, int unidades) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(UPDATE_RESERVAR_ACCIONES)) {
			ps.setInt(1, unidades);
			ps.setInt(2, campana.getId());
			ps.setInt(3, campana.getFase());
			ps.setInt(4, unidades);
			return ps.executeUpdate() == 1;
		}
	}

	private static boolean descontarCupo(Connection con, int idCampana, String dni, int unidades) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(UPDATE_DESCONTAR_CUPO)) {
			ps.setInt(1, unidades);
			ps.setInt(2, idCampana);
			ps.setString(3, dni);
			ps.setInt(4, unidades);
			return ps.executeUpdate() == 1;
		}
	}

	private static boolean existeAccionista(Connection con, String dni) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_EXISTE_ACCIONISTA)) {
			ps.setString(1, dni);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private static void insertarAccionista(Connection con, Accionista a) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(INSERT_ACCIONISTA)) {
			ps.setString(1, a.getDni());
			ps.setString(2, a.getNombre());
			ps.setString(3, a.getApellidos());
			ps.setString(4, a.getEmail());
			ps.executeUpdate();
		}
	}

	private static void guardarEmailSiVacio(Connection con, Accionista a) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(UPDATE_EMAIL_SI_VACIO)) {
			ps.setString(1, a.getEmail());
			ps.setString(2, a.getDni());
			ps.executeUpdate();
		}
	}

	private static List<Integer> insertarAcciones(Connection con, Campana campana, String dni, int unidades)
			throws SQLException {
		List<Integer> ids = new ArrayList<>();
		String fecha = LocalDate.now().toString(); // yyyy-MM-dd, igual que el resto de acciones
		try (PreparedStatement ps = con.prepareStatement(INSERT_ACCION, Statement.RETURN_GENERATED_KEYS)) {
			for (int i = 0; i < unidades; i++) {
				ps.setString(1, dni);
				ps.setString(2, fecha);
				ps.setDouble(3, campana.getPrecio());
				ps.setInt(4, campana.getId());
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					rs.next();
					ids.add(rs.getInt(1));
				}
			}
		}
		return ids;
	}

	private static void insertarVenta(Connection con, Campana campana, double total) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(INSERT_VENTA)) {
			ps.setString(1, LocalDateTime.now().format(FORMATO_FECHA_HORA));
			ps.setString(2, "Compra de acciones - " + campana.getNombre());
			ps.setDouble(3, total);
			ps.executeUpdate();
		}
	}

	private static boolean cerrarSiAgotada(Connection con, int idCampana) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(UPDATE_CERRAR_SI_AGOTADA)) {
			ps.setInt(1, idCampana);
			return ps.executeUpdate() == 1;
		}
	}
}