package giis.demo.jdbc.entrevistas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.entrevistas.FranjaHoraria;

public class FranjaJdbc {
	private final static String QUERY_GET_PLAYERS_PROFESIONAL_TEAM = 
			"SELECT * FROM EmpleadoDeportivo ed, BaseEmpleado bd WHERE id_equipo=? AND "
			+ "bd.dni=ed.dni";
	private final static String QUERY_GET_DNIS_FOR_TRAINERS_PROFESIONAL_TEAMS = 
			"SELECT dni FROM Entrenador en, EmpleadoDeportivo ed, Equipo e WHERE "
			+ "ed.id_equipo=e.id_equipo AND ed.dni=en.dni AND e.tipo_equipo='Profesional'";
	private final static String QUERY_SAVE_SLOT = 
			"INSERT INTO FranjaHoraria (dni_jugador, fecha, hora_inicio, hora_fin) VALUES "
			+ "(?,?,?,?)";
	private final static String QUERY_GET_INTERVIEW_ASSOCIATED_TO_PLAYER = 
			"SELECT id_entrevista FROM Entrevista e, FranjaHoraria f WHERE "
			+ "e.id_franja=f.id_franja AND f.fecha=? AND f.dni_jugador=?";
	private final static String QUERY_GET_MATCHES_OF_TEAM_FOR_DATE = "SELECT * FROM Partido2 "
			+ "WHERE (id_local=? OR id_visitante=?) AND fecha=?";
	private final static String QUERY_GET_TRAINING_SESSIONS_OF_TEAM_FOR_DATE = "SELECT * FROM Entrenamiento "
			+ "WHERE id_equipo=? AND fecha=?";
	private final static String QUERY_GET_TEAM_ID = 
			"SELECT id_equipo FROM Entrenador WHERE dni=?";
	
	
	public static List<EmpleadoDeportivo> cargarJugadoresEquipoProfesional(Connection con, int idEquipoProfesional) 
			throws SQLException {
		List<EmpleadoDeportivo> jugadores = new ArrayList<EmpleadoDeportivo>();
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_PLAYERS_PROFESIONAL_TEAM);){
			pst.setInt(1, idEquipoProfesional);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String dni = rs.getString("dni");	
				String nombre = rs.getString("nombre");
				String apellido = rs.getString("apellido");
				BigDecimal salario = rs.getBigDecimal("salario");
				LocalDate fechaDeNacimiento = LocalDate.parse(rs.getString("fecha_nacimiento"));
				String numeroDeTelefono = rs.getString("numero_de_telefono");
				Posicion posicion = Posicion.valueOf(rs.getString("posicion"));
				EmpleadoDeportivo jugador = new EmpleadoDeportivo(dni, nombre, apellido,
						salario, fechaDeNacimiento, numeroDeTelefono, posicion);
				jugador.setIdEquipo(idEquipoProfesional);
				jugadores.add(jugador);
			}
		}
		
		return jugadores;
	}

	public static List<String> obtenerDnisEntrenadoresDeEquiposProfesionales(Connection con) 
			throws SQLException {
		List<String> dnis = new ArrayList<String>();
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_DNIS_FOR_TRAINERS_PROFESIONAL_TEAMS);){
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String dni = rs.getString("dni");
				dnis.add(dni);
			}
		}
		
		return dnis;
	}

	public static void almacenarFranja(Connection con, FranjaHoraria nuevaFranja) throws SQLException {
		try(PreparedStatement pst = con.prepareStatement(QUERY_SAVE_SLOT);){
			String dniJugador = nuevaFranja.getDniJugador();
			LocalDate fechaFranja = nuevaFranja.getFecha();
			LocalTime horaInicio = nuevaFranja.getHoraInicio();
			LocalTime horaFin = nuevaFranja.getHoraFin();
			
			pst.setString(1, dniJugador);
			pst.setObject(2, fechaFranja);
			pst.setObject(3, horaInicio);
			pst.setObject(4, horaFin);
			
			int filasActualizadas = pst.executeUpdate();
			if(filasActualizadas != 1) {
				throw new SQLException("SQLException: Numero de filas actualizadas incorrecto. Fallo al insertar");
			}
		}
	}

	public static boolean jugadorTieneAsignadaEntrevista(Connection con, EmpleadoDeportivo jugador,
			LocalDate fechaFranja) throws SQLException {
		boolean tieneEntrevista = false;
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_INTERVIEW_ASSOCIATED_TO_PLAYER);){
			pst.setObject(1, fechaFranja);
			pst.setString(2, jugador.getDni());
			
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				tieneEntrevista = true;
			}
		}
		
		return tieneEntrevista;
	}

	public static int getIdEquipo(Connection con, String dniEntrenador) throws SQLException {
		int id_equipo = 0;
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_TEAM_ID)){
			pst.setString(1, dniEntrenador);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				id_equipo = rs.getInt(1);
			}
		}
		
		return id_equipo;
	}

	public static boolean equipoTienePartidoOEntrenamiento(Connection con, EmpleadoDeportivo jugador,
			LocalDate fechaFranja, LocalTime horaInicio, LocalTime horaFin) throws SQLException {
		boolean tienePartido = false;
		boolean tieneEntrenamiento = false;
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_MATCHES_OF_TEAM_FOR_DATE);){
			pst.setInt(1, jugador.getIdEquipo());
			pst.setInt(2, jugador.getIdEquipo());
			pst.setObject(2, fechaFranja);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				LocalTime horaInicioPartido = rs.getObject("hora_inicio", LocalTime.class);
				LocalTime horaFinPartido = rs.getObject("hora_fin", LocalTime.class);
				
				if (horaInicio.isBefore(horaFinPartido) && horaFin.isAfter(horaInicioPartido)) {
				    tienePartido = true;
				}
			}
		}
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_TRAINING_SESSIONS_OF_TEAM_FOR_DATE);){
			pst.setInt(1, jugador.getIdEquipo());
			pst.setObject(2, fechaFranja);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				LocalTime horaInicioEntrenamiento = rs.getObject("hora_inicio", LocalTime.class);
				LocalTime horaFinEntrenamiento = rs.getObject("hora_fin", LocalTime.class);
				
				if (horaInicio.isBefore(horaFinEntrenamiento) && horaFin.isAfter(horaInicioEntrenamiento)) {
				    tieneEntrenamiento = true;
				}
			}
		}
		
		return (tienePartido | tieneEntrenamiento);
	}
	
}
