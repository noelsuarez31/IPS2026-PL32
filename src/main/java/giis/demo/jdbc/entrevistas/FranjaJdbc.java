package giis.demo.jdbc.entrevistas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.entrevistas.FranjaHoraria;

public class FranjaJdbc {
	private final static String QUERY_GET_PLAYERS_PROFESIONAL_TEAM = 
			"SELECT * FROM EmpleadoDeportivo ed, BaseEmpleado bd WHERE id_equipo=? AND "
			+ "bd.nif=ed.nif";
	private final static String QUERY_GET_DNIS_FOR_TRAINERS_PROFESIONAL_TEAMS = 
			"SELECT dni FROM Entrenador en, Equipo e WHERE en.id_equipo=? AND "
			+ "en.id_equipo=e.id_equipo AND e.tipo_equipo='Profesional'";
	private final static String QUERY_SAVE_SLOT = 
			"";
	private final static String QUERY_GET_INTERVIEW_ASSOCIATED_TO_PLAYER = 
			"";
	private final static String QUERY_GET_TEAM_ID = 
			"SELECT id_equipo FROM Entrenador WHERE nif=?";
	
	

	public static List<EmpleadoDeportivo> cargarJugadoresEquipoProfesional(Connection con, int idEquipoProfesional) throws SQLException {
		List<EmpleadoDeportivo> jugadores = new ArrayList<EmpleadoDeportivo>();
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_PLAYERS_PROFESIONAL_TEAM)){
			pst.setInt(1, idEquipoProfesional);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				EmpleadoDeportivo jugador = new EmpleadoDeportivo();
				jugador.setDni(rs.getString("dni"));
				jugador.setNombre(rs.getString("dni"));
				jugador.setApellido(rs.getString("dni"));
				jugador.setSalario(rs.getBigDecimal("salario"));
				jugador.setFechaDeNacimiento(LocalDate.parse(rs.getString("fecha_de_nacimiento")));
				jugador.setNumeroDeTelefono(rs.getString("numero_de_telefono"));
				jugador.setPosicion(Posicion.valueOf(rs.getString("posicion")));
				jugador.setIdEquipo(idEquipoProfesional);
				jugadores.add(jugador);
			}
		}
		
		return jugadores;
	}

	public static List<String> obtenerDnisEntrenadoresDeEquiposProfesionales(Connection con) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void almacenarFranja(Connection con, FranjaHoraria nuevaFranja) {
		// TODO Auto-generated method stub
		
	}

	public static boolean jugadorTieneAsignadaEntrevista(Connection con, EmpleadoDeportivo jugador,
			LocalDate fechaFranja) {
		// TODO Auto-generated method stub
		return false;
	}

	public static int getIdEquipo(Connection con, String nifEntrenador) throws SQLException {
		int id_equipo = 0;
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_TEAM_ID)){
			pst.setString(1, nifEntrenador);
			ResultSet rs = pst.executeQuery();
			id_equipo = rs.getInt(1);
		}
		
		return id_equipo;
	}
	
}
