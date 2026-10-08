package giis.demo.jdbc.horarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.horarios.HorarioPeriodico;

public class HorarioPeriodicoJdbc{

	private static final String QUERY_INSERT_HORARIO = 
			"INSERT INTO horario_periodico (id_empleado, dia_semana, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
			
	private static final String QUERY_GET_HORARIOS = 
			"SELECT id_horario, id_empleado, dia_semana, hora_inicio, hora_fin FROM horario_periodico WHERE id_empleado = ?";

	public static void añadirHorario(Connection con, HorarioPeriodico horario) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_HORARIO)) {

			ps.setInt(1, horario.getId_empleado());
			ps.setInt(2, horario.getDia_semana());
			ps.setString(3, horario.getHora_inicio());
			ps.setString(4, horario.getHora_fin());
			ps.executeUpdate();
		}
	}

	public static List<HorarioPeriodico> getHorariosEmpleado(Connection con, int idEmpleado) throws SQLException {
		List<HorarioPeriodico> horarios = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(QUERY_GET_HORARIOS)) {

			ps.setInt(1, idEmpleado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					HorarioPeriodico h = new HorarioPeriodico(
							rs.getInt("id_horario"), rs.getInt("id_empleado"), rs.getInt("dia_semana"),
							rs.getString("hora_inicio"), rs.getString("hora_fin"));
					horarios.add(h);
				}
			}
		}
		return horarios;
	}
}