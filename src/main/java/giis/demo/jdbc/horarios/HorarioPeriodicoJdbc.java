package giis.demo.jdbc.horarios;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.horarios.HorarioPeriodico;

public class HorarioPeriodicoJdbc{

	//inserta el nuevo horario periodico
	private static final String QUERY_INSERT_HORARIO = 
			"INSERT INTO HorarioPeriodico (dni_empleado, dia_semana, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
	
	//consulta los horarios de un empleado no deportivo en especifico
	private static final String QUERY_GET_HORARIOS = 
			"SELECT id_horario, dni_empleado, dia_semana, hora_inicio, hora_fin FROM HorarioPeriodico WHERE dni_empleado = ?";
	
	

	/*
	 * inserta un nuevo horario periodico a la bd
	 */
	public static void añadirHorario(Connection con, HorarioPeriodico horario) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_HORARIO)) {

			ps.setString(1, horario.getDni_empleado());
			ps.setInt(2, horario.getDia_semana());
			ps.setString(3, horario.getHora_inicio());
			ps.setString(4, horario.getHora_fin());
			ps.executeUpdate();
		}
	}

	/*
	 * devuelve una lista de horariosperiodicos asignados al empleado
	 */
	public static List<HorarioPeriodico> getHorariosEmpleado(Connection con, String idEmpleado) throws SQLException {
		List<HorarioPeriodico> horarios = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_HORARIOS)) {

			ps.setString(1, idEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					HorarioPeriodico h = new HorarioPeriodico(
							rs.getInt("id_horario"), rs.getString("dni_empleado"), rs.getInt("dia_semana"),
							rs.getString("hora_inicio"), rs.getString("hora_fin"));
					horarios.add(h);
				}
			}
		}
		return horarios;
	}
	
	
}