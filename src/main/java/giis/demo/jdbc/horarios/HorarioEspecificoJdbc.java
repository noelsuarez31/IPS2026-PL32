package giis.demo.jdbc.horarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.horarios.HorarioEspecifico;

public class HorarioEspecificoJdbc {

	// insertar un nuevo turno específico
	private static final String QUERY_INSERT_HORARIO_ESPECIFICO = 
			"INSERT INTO HorarioEspecifico (dni_empleado, fecha, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

	// comprobar si el empleado tiene horarios periódicos asignados (cuenta si existe al menos 1)
	private static final String QUERY_TIENE_HORARIOS_PERIODICOS = 
			"SELECT COUNT(*) FROM HorarioPeriodico WHERE dni_empleado = ?";

	// consultar horarios específicos de un empleado en una fecha concreta
	private static final String QUERY_GET_HORARIOS_FECHA = 
			"SELECT id_horario, dni_empleado, fecha, hora_inicio, hora_fin FROM HorarioEspecifico WHERE dni_empleado = ? AND fecha = ?";

	// consultar horarios específicos de un empleado en un rango de fechas 
	private static final String QUERY_GET_HORARIOS_RANGO = 
			"SELECT id_horario, dni_empleado, fecha, hora_inicio, hora_fin FROM HorarioEspecifico WHERE dni_empleado = ? AND fecha BETWEEN ? AND ?";
	
	// consultar todos los horarios específicos de un empleado (ordenados por fecha y hora)
	private static final String QUERY_GET_HORARIOS_EMPLEADO = 
			"SELECT id_horario, dni_empleado, fecha, hora_inicio, hora_fin FROM HorarioEspecifico WHERE dni_empleado = ? ORDER BY fecha, hora_inicio";

	/**
	 * Inserta un nuevo horario específico en la base de datos.
	 */
	public static void añadirHorarioEspecifico(Connection con, HorarioEspecifico horario) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_HORARIO_ESPECIFICO)) {
			ps.setString(1, horario.getDni_empleado());
			ps.setString(2, horario.getFecha());
			ps.setString(3, horario.getHora_inicio());
			ps.setString(4, horario.getHora_fin());
			ps.executeUpdate();
		}
	}

	/**
	 * Comprueba si el empleado tiene al menos un horario periódico asignado.
	 */
	public static boolean tieneHorariosPeriodicos(Connection con, String dniEmpleado) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_TIENE_HORARIOS_PERIODICOS)) {
			ps.setString(1, dniEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	/**
	 * Devuelve los turnos específicos de un empleado para una fecha concreta (YYYY-MM-DD).
	 */
	public static List<HorarioEspecifico> getHorariosFecha(Connection con, String dniEmpleado, String fecha) throws SQLException {
		List<HorarioEspecifico> horarios = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_HORARIOS_FECHA)) {
			ps.setString(1, dniEmpleado);
			ps.setString(2, fecha);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					HorarioEspecifico h = new HorarioEspecifico(
							rs.getInt("id_horario"), 
							rs.getString("dni_empleado"), 
							rs.getString("fecha"),
							rs.getString("hora_inicio"), 
							rs.getString("hora_fin"));
					horarios.add(h);
				}
			}
		}
		return horarios;
	}

	/**
	 * Devuelve los turnos específicos de un empleado comprendidos entre dos fechas (inclusive).
	 */
	public static List<HorarioEspecifico> getHorariosRango(Connection con, String dniEmpleado, String fechaInicio, String fechaFin) throws SQLException {
		List<HorarioEspecifico> horarios = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_HORARIOS_RANGO)) {
			ps.setString(1, dniEmpleado);
			ps.setString(2, fechaInicio);
			ps.setString(3, fechaFin);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					HorarioEspecifico h = new HorarioEspecifico(
							rs.getInt("id_horario"), 
							rs.getString("dni_empleado"), 
							rs.getString("fecha"),
							rs.getString("hora_inicio"), 
							rs.getString("hora_fin"));
					horarios.add(h);
				}
			}
		}
		return horarios;
	}
	
	/**
	 * Devuelve todos los turnos específicos de un empleado, ordenados por fecha y hora de inicio.
	 */
	public static List<HorarioEspecifico> getHorariosEmpleado(Connection con, String dniEmpleado) throws SQLException {
		List<HorarioEspecifico> horarios = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_HORARIOS_EMPLEADO)) {
			ps.setString(1, dniEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					HorarioEspecifico h = new HorarioEspecifico(
							rs.getInt("id_horario"), 
							rs.getString("dni_empleado"), 
							rs.getString("fecha"),
							rs.getString("hora_inicio"), 
							rs.getString("hora_fin"));
					horarios.add(h);
				}
			}
		}
		return horarios;
	}
}