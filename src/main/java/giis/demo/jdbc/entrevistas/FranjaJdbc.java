package giis.demo.jdbc.entrevistas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.entrevistas.FranjaHoraria;

public class FranjaJdbc {
	private final static String QUERY_GET_PLAYERS_PROFESIONAL_TEAM = 
			"SELECT * FROM EmpleadoDeportivo ed, BaseEmpleado bd WHERE id_equipo=? AND "
			+ "bd.dni=ed.dni AND posicion='JUGADOR'";
	private final static String QUERY_GET_DNIS_FOR_TRAINERS_PROFESIONAL_TEAMS = 
			"SELECT ed.dni FROM EmpleadoDeportivo ed, Equipo e WHERE "
			+ "ed.id_equipo=e.id_equipo AND ed.posicion='ENTRENADOR' AND e.tipo_equipo='Profesional'";
	private final static String QUERY_SAVE_SLOT = 
			"INSERT INTO FranjaHoraria (dni_jugador, fecha, hora_inicio, hora_fin) VALUES "
			+ "(?,?,?,?)";
	private final static String QUERY_GET_INTERVIEW_ASSOCIATED_TO_PLAYER = 
			"SELECT e.id_entrevista FROM Entrevista e, FranjaHoraria f WHERE "
			+ "e.id_franja=f.id_franja AND f.fecha=? AND f.dni_jugador=?";
	private final static String QUERY_GET_MATCHES_OF_TEAM_FOR_DATE = "SELECT * FROM Partido "
			+ "WHERE (id_local=? OR id_visitante=?) AND fecha=?";
	private final static String QUERY_GET_TRAINING_SESSIONS_OF_TEAM_FOR_DATE = "SELECT * FROM Entrenamiento "
			+ "WHERE id_equipo=? AND fecha=?";
	private final static String QUERY_GET_TEAM_ID = 
			"SELECT ed.id_equipo FROM EmpleadoDeportivo ed WHERE ed.dni=?";
	private final static String QUERY_SLOT_ALREADY_EXISTS = 
			"SELECT * FROM FranjaHoraria WHERE dni_jugador=? AND fecha=?";
	
	/**
	 * Devuelve una lista de EmpleadoDeportivo con los jugadores que pertenecen al equipo 
	 * profesional cuyo id se pasa por parametro
	 * @param con
	 * @param idEquipoProfesional
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Devuelve una lista de dnis que pertenecen a los entrenadores que tienen asignado
	 * un equipo profesional
	 * @param con
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Almacena la franja cuyos datos se pasan por parametro
	 * @param con
	 * @param nuevaFranja
	 * @throws SQLException
	 */
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

	/**
	 * Devuelve true si el jugador pasado por parametro tiene asignada alguna 
	 * entrevista en la fecha pasada por parametro
	 * @param con
	 * @param jugador
	 * @param fechaFranja
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Devuelve el id del equipo que tiene como entrenador el cual tiene 
	 * de dni el pasado por parametro
	 * @param con
	 * @param dniEntrenador
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Devuelve true si el equipo al que pertenece el jugador pasado por parametro
	 * tiene algun partido/entrenamiento que coincida con esa fecha y horas
	 * @param con
	 * @param jugador
	 * @param fechaFranja
	 * @param horaInicio
	 * @param horaFin
	 * @return
	 * @throws SQLException
	 */
	public static String equipoTienePartidoOEntrenamiento(Connection con, EmpleadoDeportivo jugador,
			LocalDate fechaFranja, LocalTime horaInicio, LocalTime horaFin) throws SQLException {
		String motivoConflicto = "";
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_MATCHES_OF_TEAM_FOR_DATE);){
			pst.setInt(1, jugador.getIdEquipo());
			pst.setInt(2, jugador.getIdEquipo());
			pst.setObject(3, fechaFranja);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String horaInicioStr = rs.getString("hora_inicio");
				String horaFinStr = rs.getString("hora_fin");
				
				if (horaInicioStr != null && horaFinStr != null) {
					// Parseamos la cadena de texto de la base de datos a LocalTime de forma segura
					LocalTime horaInicioPartido = LocalTime.parse(horaInicioStr, DateTimeFormatter.ofPattern(horaInicioStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					LocalTime horaFinPartido = LocalTime.parse(horaFinStr, DateTimeFormatter.ofPattern(horaFinStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					
					if (horaInicio.isBefore(horaFinPartido) && horaFin.isAfter(horaInicioPartido)) {
					    motivoConflicto = String.format("El jugador tiene un partido con su equipo de %s a %s",
					    		horaInicioPartido.format(formatoHora), horaFinPartido.format(formatoHora));
					}
				}
			}
		}
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_TRAINING_SESSIONS_OF_TEAM_FOR_DATE);){
			pst.setInt(1, jugador.getIdEquipo());
			pst.setObject(2, fechaFranja);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String horaInicioStr = rs.getString("hora_inicio");
				String horaFinStr = rs.getString("hora_fin");
				
				if (horaInicioStr != null && horaFinStr != null) {
					LocalTime horaInicioEntrenamiento = LocalTime.parse(horaInicioStr, DateTimeFormatter.ofPattern(horaInicioStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					LocalTime horaFinEntrenamiento = LocalTime.parse(horaFinStr, DateTimeFormatter.ofPattern(horaFinStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					
					if (horaInicio.isBefore(horaFinEntrenamiento) && horaFin.isAfter(horaInicioEntrenamiento)) {
					    motivoConflicto = String.format("El jugador tiene un entrenamiento con su equipo de %s a %s",
					    		horaInicioEntrenamiento.format(formatoHora), horaFinEntrenamiento.format(formatoHora));
					}
				}
			}
		}
		
		return motivoConflicto;
	}

	/**
	 * Devuelve true si el jugador que se pasa por parametro ya tiene esa franja 
	 * asignada o tiene una con la que hay conflicto
	 * @param con
	 * @param jugador
	 * @param fechaFranja
	 * @param horaInicio
	 * @param horaFin
	 * @return
	 * @throws SQLException
	 */
	public static boolean yaHayEsaFranjaParaEseJugador(Connection con, EmpleadoDeportivo jugador, LocalDate fechaFranja,
			LocalTime horaInicio, LocalTime horaFin) throws SQLException {
		try(PreparedStatement pst = con.prepareStatement(QUERY_SLOT_ALREADY_EXISTS);){
			pst.setString(1, jugador.getDni());
			pst.setObject(2, fechaFranja);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String horaInicioStr = rs.getString("hora_inicio");
				String horaFinStr = rs.getString("hora_fin");
				
				if (horaInicioStr != null && horaFinStr != null) {
					LocalTime horaInicioExistente = LocalTime.parse(horaInicioStr, DateTimeFormatter.ofPattern(horaInicioStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					LocalTime horaFinExistente = LocalTime.parse(horaFinStr, DateTimeFormatter.ofPattern(horaFinStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					
					if (horaInicio.isBefore(horaFinExistente) && horaFin.isAfter(horaInicioExistente)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
