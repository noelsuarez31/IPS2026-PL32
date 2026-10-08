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

	private static final String QUERY_INSERT_HORARIO = 
			"INSERT INTO horario_periodico (dni_empleado, dia_semana, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
			
	private static final String QUERY_GET_HORARIOS = 
			"SELECT id_horario, dni_empleado, dia_semana, hora_inicio, hora_fin FROM horario_periodico WHERE dni_empleado = ?";
	
	private static final String GET_NO_DEPORTIVOS = "SELECT be.dni, be.nombre, be.apellido, be.salario, be.fecha_nacimiento, be.numero_de_telefono, end.posicion " +
												    "FROM EmpleadoNoDeportivo end " +
												    "JOIN BaseEmpleado be ON end.dni = be.dni";

	public static void añadirHorario(Connection con, HorarioPeriodico horario) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_HORARIO)) {

			ps.setString(1, horario.getDni_empleado());
			ps.setInt(2, horario.getDia_semana());
			ps.setString(3, horario.getHora_inicio());
			ps.setString(4, horario.getHora_fin());
			ps.executeUpdate();
		}
	}

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
	
	public static List<EmpleadoNoDeportivo> getEmpleadosNoDeportivos(Connection con) throws SQLException {
        List<EmpleadoNoDeportivo> empleados = new ArrayList<>();
        
                     
        try (PreparedStatement pst = con.prepareStatement(GET_NO_DEPORTIVOS);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                EmpleadoNoDeportivo emp = new EmpleadoNoDeportivo(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    new BigDecimal(rs.getString("salario")),
                    LocalDate.parse(rs.getString("fecha_nacimiento")),
                    rs.getString("numero_de_telefono"),
                    Posicion.valueOf(rs.getString("posicion"))
                );
                empleados.add(emp);
            }
        }
        return empleados;
    }
}