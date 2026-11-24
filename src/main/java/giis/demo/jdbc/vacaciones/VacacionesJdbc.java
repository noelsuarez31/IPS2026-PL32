package giis.demo.jdbc.vacaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.mantenimiento.TareaMantenimiento;
import giis.demo.model.vacaciones.AlertaGerente;
import giis.demo.model.vacaciones.ElementoInvalidadoVacacion;
import giis.demo.model.vacaciones.VacacionEmpleado;

public class VacacionesJdbc {

	private static final String QUERY_GET_VACACIONES_EMPLEADO =
			"SELECT id_vacacion, dni_empleado, fecha, dia_semana, estado "
			+ "FROM VacacionEmpleado WHERE dni_empleado = ? ORDER BY fecha";

	private static final String QUERY_TIENE_VACACION_FECHA =
			"SELECT COUNT(*) FROM VacacionEmpleado WHERE dni_empleado = ? AND fecha = ?";

	private static final String QUERY_GET_INVALIDADOS_EMPLEADO =
			"SELECT id_invalidado, dni_empleado, fecha, tipo, detalle "
			+ "FROM ElementoInvalidadoVacacion WHERE dni_empleado = ? ORDER BY fecha, id_invalidado";

	private static final String QUERY_GET_TAREAS_EMPLEADO_FECHA =
			"SELECT tm.id_tarea, tm.id_instalacion, i.nombre AS nombre_instalacion, tm.fecha, tm.hora_inicio, tm.hora_fin, tm.descripcion "
			+ "FROM TareaMantenimiento tm "
			+ "JOIN TareaMantenimientoJardinero tmj ON tm.id_tarea = tmj.id_tarea "
			+ "JOIN Instalacion i ON tm.id_instalacion = i.id_instalacion "
			+ "WHERE tmj.dni_jardinero = ? AND tm.fecha = ? "
			+ "ORDER BY tm.hora_inicio";

	private static final String QUERY_GET_TAREAS_EMPLEADO =
			"SELECT tm.id_tarea, tm.id_instalacion, i.nombre AS nombre_instalacion, tm.fecha, tm.hora_inicio, tm.hora_fin, tm.descripcion "
			+ "FROM TareaMantenimiento tm "
			+ "JOIN TareaMantenimientoJardinero tmj ON tm.id_tarea = tmj.id_tarea "
			+ "JOIN Instalacion i ON tm.id_instalacion = i.id_instalacion "
			+ "WHERE tmj.dni_jardinero = ? "
			+ "ORDER BY tm.fecha, tm.hora_inicio";

	private static final String QUERY_INSERT_VACACION =
			"INSERT INTO VacacionEmpleado (dni_empleado, fecha, dia_semana, estado) VALUES (?, ?, ?, ?)";

	private static final String QUERY_INSERT_INVALIDADO =
			"INSERT INTO ElementoInvalidadoVacacion (dni_empleado, fecha, tipo, detalle) VALUES (?, ?, ?, ?)";

	private static final String QUERY_DELETE_HORARIO_ESPECIFICO_FECHA =
			"DELETE FROM HorarioEspecifico WHERE dni_empleado = ? AND fecha = ?";

	private static final String QUERY_DELETE_TAREA_JARDINERO_FECHA =
			"DELETE FROM TareaMantenimientoJardinero WHERE dni_jardinero = ? "
			+ "AND id_tarea IN (SELECT id_tarea FROM TareaMantenimiento WHERE fecha = ?)";

	private static final String QUERY_DELETE_TAREAS_SIN_JARDINERO_FECHA =
			"DELETE FROM TareaMantenimiento WHERE fecha = ? "
			+ "AND id_tarea NOT IN (SELECT id_tarea FROM TareaMantenimientoJardinero)";

	private static final String QUERY_INSERT_ALERTA_GERENTE =
			"INSERT INTO AlertaGerente (dni_empleado, nombre_empleado, fecha_creacion, mensaje, leida) "
			+ "VALUES (?, ?, ?, ?, 0)";

	private static final String QUERY_GET_ALERTAS_PENDIENTES =
			"SELECT id_alerta, dni_empleado, nombre_empleado, fecha_creacion, mensaje, leida "
			+ "FROM AlertaGerente WHERE leida = 0 ORDER BY id_alerta";

	private static final String QUERY_MARCAR_ALERTAS_LEIDAS =
			"UPDATE AlertaGerente SET leida = 1 WHERE leida = 0";

	/**
	 * Devuelve las vacaciones confirmadas de un empleado ordenadas por fecha.
	 */
	public static List<VacacionEmpleado> getVacacionesEmpleado(Connection con, String dniEmpleado) throws SQLException {
		List<VacacionEmpleado> lista = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_VACACIONES_EMPLEADO)) {
			ps.setString(1, dniEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(new VacacionEmpleado(
							rs.getInt("id_vacacion"),
							rs.getString("dni_empleado"),
							rs.getString("fecha"),
							rs.getString("dia_semana"),
							rs.getString("estado")));
				}
			}
		}
		return lista;
	}

	/**
	 * Comprueba si el empleado tiene vacación confirmada en una fecha concreta.
	 */
	public static boolean tieneVacacionEnFecha(Connection con, String dniEmpleado, String fecha) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_TIENE_VACACION_FECHA)) {
			ps.setString(1, dniEmpleado);
			ps.setString(2, fecha);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	/**
	 * Devuelve los turnos y tareas que fueron invalidados al confirmar vacaciones del empleado.
	 */
	public static List<ElementoInvalidadoVacacion> getElementosInvalidadosEmpleado(Connection con, String dniEmpleado)
			throws SQLException {
		List<ElementoInvalidadoVacacion> lista = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_INVALIDADOS_EMPLEADO)) {
			ps.setString(1, dniEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(new ElementoInvalidadoVacacion(
							rs.getInt("id_invalidado"),
							rs.getString("dni_empleado"),
							rs.getString("fecha"),
							rs.getString("tipo"),
							rs.getString("detalle")));
				}
			}
		}
		return lista;
	}

	/**
	 * Devuelve las tareas de mantenimiento asignadas al empleado en una fecha concreta.
	 */
	public static List<TareaMantenimiento> getTareasEmpleadoEnFecha(Connection con, String dniEmpleado, String fecha)
			throws SQLException {
		List<TareaMantenimiento> tareas = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_TAREAS_EMPLEADO_FECHA)) {
			ps.setString(1, dniEmpleado);
			ps.setString(2, fecha);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					TareaMantenimiento t = new TareaMantenimiento(
							rs.getInt("id_tarea"),
							rs.getInt("id_instalacion"),
							rs.getString("fecha"),
							rs.getString("hora_inicio"),
							rs.getString("hora_fin"),
							rs.getString("descripcion"));
					t.setNombreInstalacion(rs.getString("nombre_instalacion"));
					tareas.add(t);
				}
			}
		}
		return tareas;
	}

	/**
	 * Devuelve todas las tareas de mantenimiento asignadas al empleado.
	 */
	public static List<TareaMantenimiento> getTareasEmpleado(Connection con, String dniEmpleado) throws SQLException {
		List<TareaMantenimiento> tareas = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_TAREAS_EMPLEADO)) {
			ps.setString(1, dniEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					TareaMantenimiento t = new TareaMantenimiento(
							rs.getInt("id_tarea"),
							rs.getInt("id_instalacion"),
							rs.getString("fecha"),
							rs.getString("hora_inicio"),
							rs.getString("hora_fin"),
							rs.getString("descripcion"));
					t.setNombreInstalacion(rs.getString("nombre_instalacion"));
					tareas.add(t);
				}
			}
		}
		return tareas;
	}

	/**
	 * Registra de forma transaccional los días de vacaciones confirmados, almacena los elementos
	 * invalidados, elimina los horarios específicos y asignaciones de tareas afectadas,
	 * y crea la alerta para el gerente si procede.
	 */
	public static void confirmarVacaciones(Connection con, String dniEmpleado, List<VacacionEmpleado> vacaciones,
			List<ElementoInvalidadoVacacion> invalidados, AlertaGerente alerta) throws SQLException {
		boolean autoCommitOriginal = con.getAutoCommit();
		try {
			con.setAutoCommit(false);

			try (PreparedStatement psVac = con.prepareStatement(QUERY_INSERT_VACACION)) {
				for (VacacionEmpleado v : vacaciones) {
					psVac.setString(1, dniEmpleado);
					psVac.setString(2, v.getFecha());
					psVac.setString(3, v.getDiaSemana());
					psVac.setString(4, "Confirmada");
					psVac.addBatch();
				}
				psVac.executeBatch();
			}

			if (invalidados != null && !invalidados.isEmpty()) {
				try (PreparedStatement psInv = con.prepareStatement(QUERY_INSERT_INVALIDADO)) {
					for (ElementoInvalidadoVacacion inv : invalidados) {
						psInv.setString(1, dniEmpleado);
						psInv.setString(2, inv.getFecha());
						psInv.setString(3, inv.getTipo());
						psInv.setString(4, inv.getDetalle());
						psInv.addBatch();
					}
					psInv.executeBatch();
				}
			}

			try (PreparedStatement psDelHe = con.prepareStatement(QUERY_DELETE_HORARIO_ESPECIFICO_FECHA);
					PreparedStatement psDelTmj = con.prepareStatement(QUERY_DELETE_TAREA_JARDINERO_FECHA);
					PreparedStatement psDelTm = con.prepareStatement(QUERY_DELETE_TAREAS_SIN_JARDINERO_FECHA)) {
				for (VacacionEmpleado v : vacaciones) {
					psDelHe.setString(1, dniEmpleado);
					psDelHe.setString(2, v.getFecha());
					psDelHe.executeUpdate();

					psDelTmj.setString(1, dniEmpleado);
					psDelTmj.setString(2, v.getFecha());
					psDelTmj.executeUpdate();

					psDelTm.setString(1, v.getFecha());
					psDelTm.executeUpdate();
				}
			}

			if (alerta != null) {
				try (PreparedStatement psAlerta = con.prepareStatement(QUERY_INSERT_ALERTA_GERENTE)) {
					psAlerta.setString(1, alerta.getDniEmpleado());
					psAlerta.setString(2, alerta.getNombreEmpleado());
					psAlerta.setString(3, alerta.getFechaCreacion());
					psAlerta.setString(4, alerta.getMensaje());
					psAlerta.executeUpdate();
				}
			}

			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(autoCommitOriginal);
		}
	}

	/**
	 * Devuelve las alertas pendientes de leer para el gerente.
	 */
	public static List<AlertaGerente> getAlertasGerentePendientes(Connection con) throws SQLException {
		List<AlertaGerente> alertas = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_ALERTAS_PENDIENTES);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				alertas.add(new AlertaGerente(
						rs.getInt("id_alerta"),
						rs.getString("dni_empleado"),
						rs.getString("nombre_empleado"),
						rs.getString("fecha_creacion"),
						rs.getString("mensaje"),
						rs.getInt("leida") == 1));
			}
		}
		return alertas;
	}

	/**
	 * Marca todas las alertas pendientes del gerente como leídas.
	 */
	public static void marcarAlertasGerenteComoLeidas(Connection con) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_MARCAR_ALERTAS_LEIDAS)) {
			ps.executeUpdate();
		}
	}
}
