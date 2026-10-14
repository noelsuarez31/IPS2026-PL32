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

public class EntrevistaJdbc {
	
	private final static String QUERY_GET_SLOTS = "SELECT * FROM FranjaHoraria f " +
            "WHERE f.dni_jugador = ? AND f.fecha = ? " +
            "AND NOT EXISTS (SELECT 1 FROM Entrevista e WHERE e.id_franja = f.id_franja)";
	private final static String QUERY_GET_PLAYERS_WITH_SLOTS = 
		    "SELECT ed.*, bd.* FROM EmpleadoDeportivo ed " +
		    "JOIN BaseEmpleado bd ON ed.dni = bd.dni " +
		    "WHERE ed.posicion = ? AND EXISTS (SELECT 1 FROM FranjaHoraria fh WHERE fh.dni_jugador = ed.dni)";
	
	// strftime lo que hace es extraer los 4 digitos del año en el primer caso y los dos
	// del mes en el segundo
	private final static String QUERY_GET_DAYS_WITH_SLOTS_BY_MONTH = 
		    "SELECT DISTINCT fecha FROM FranjaHoraria " +
		    "WHERE dni_jugador = ? AND strftime('%Y', fecha) = ? AND strftime('%m', fecha) = ?";
	
	private final static String QUERY_CREATE_INTERVIEW = 
			"INSERT INTO Entrevista (id_franja, nombre_medio) VALUES (?,?)";
	
	private final static String QUERY_DELETE_SAME_DAY_SLOTS = 
			"DELETE FROM FranjaHoraria WHERE dni_jugador = ? AND fecha = ? AND id_franja <> ?";
	
	/**
	 * Crea la entrevista y elimina las franjas que tenga ese jugador para ese mismo dia
	 * @param con
	 * @param jugadorSeleccionado
	 * @param franjaSeleccionada
	 * @param nombreMedioCom
	 * @throws SQLException
	 */
	public static void crearEntrevistaYEliminarFranjasCoincidentes(Connection con, EmpleadoDeportivo jugadorSeleccionado,
			FranjaHoraria franjaSeleccionada, String nombreMedioCom) throws SQLException {
		boolean originalAutoCommit = con.getAutoCommit();
		
		try {
			con.setAutoCommit(false);
			
			try(PreparedStatement pst = con.prepareStatement(QUERY_CREATE_INTERVIEW)){
				pst.setInt(1, franjaSeleccionada.getIdFranja());
				pst.setString(2, nombreMedioCom);
				
				int rowsUpdated = pst.executeUpdate();
				if(rowsUpdated != 1) {
					throw new SQLException("Fallo al crear la entrevista");
				}
			}
			
			try(PreparedStatement pst = con.prepareStatement(QUERY_DELETE_SAME_DAY_SLOTS)){
				pst.setString(1,jugadorSeleccionado.getDni());
				pst.setObject(2, franjaSeleccionada.getFecha());
				pst.setInt(3, franjaSeleccionada.getIdFranja());
				
				pst.executeUpdate();
			}
			
			con.commit();
		} catch(SQLException sqle) {
			con.rollback();
			throw sqle;
		} finally {
			con.setAutoCommit(originalAutoCommit);
		}
		
	}

	/**
	 * Devuelve una lista con las franjas del jugador pasado por parametro
	 * para la fecha pasada por parametro
	 * @param con
	 * @param jugadorSeleccionado
	 * @param fechaSeleccionada
	 * @return
	 * @throws SQLException
	 */
	public static List<FranjaHoraria> cargarFranjas(Connection con, EmpleadoDeportivo jugadorSeleccionado,
			LocalDate fechaSeleccionada) throws SQLException {
		List<FranjaHoraria> franjasSinEntrevistasAsignadas = new ArrayList<FranjaHoraria>();
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_SLOTS)){
			pst.setString(1, jugadorSeleccionado.getDni());
			pst.setObject(2, fechaSeleccionada);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String horaInicioStr = rs.getString("hora_inicio");
				String horaFinStr = rs.getString("hora_fin");
				
				if (horaInicioStr != null && horaFinStr != null) {
					LocalTime horaInicio = LocalTime.parse(horaInicioStr, DateTimeFormatter.ofPattern(horaInicioStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					LocalTime horaFin = LocalTime.parse(horaFinStr, DateTimeFormatter.ofPattern(horaFinStr.length() == 5 ? "HH:mm" : "HH:mm:ss"));
					FranjaHoraria franjaLibre = new FranjaHoraria(jugadorSeleccionado.getDni(), fechaSeleccionada, 
							horaInicio, horaFin);
					franjaLibre.setIdFranja(rs.getInt("id_franja"));
					
					franjasSinEntrevistasAsignadas.add(franjaLibre);
				}
			}
		}
		
		return franjasSinEntrevistasAsignadas;
	}

	/**
	 * Devuelve una lista de los jugadores que tienen franjas creadas
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static List<EmpleadoDeportivo> getJugadoresConFranjas(Connection con) throws SQLException {
		List<EmpleadoDeportivo> jugadores = new ArrayList<EmpleadoDeportivo>();
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_PLAYERS_WITH_SLOTS)){
			pst.setString(1, Posicion.JUGADOR.name());
			
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
				
				jugadores.add(jugador);
			}
		}
		
		return jugadores;
	}

	/**
	 * Devuelve los dias que el jugador tiene franjas creadas para un mes y año 
	 * pasado por parametro
	 * @param con
	 * @param jugadorSeleccionado
	 * @param mes
	 * @param año
	 * @return
	 * @throws SQLException
	 */
	public static List<LocalDate> getDiasConFranja(Connection con, EmpleadoDeportivo jugadorSeleccionado, int mes,
			int año) throws SQLException {
		List<LocalDate> dias = new ArrayList<>();
	    
	    // Formateamos el mes a dos dígitos
	    String mesStr = String.format("%02d", mes);
	    String añoStr = String.valueOf(año);
	    
	    try (PreparedStatement pst = con.prepareStatement(QUERY_GET_DAYS_WITH_SLOTS_BY_MONTH)) {
	        pst.setString(1, jugadorSeleccionado.getDni());
	        pst.setString(2, añoStr);
	        pst.setString(3, mesStr);
	        
	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                String fechaStr = rs.getString("fecha");
	                if (fechaStr != null) {
	                    LocalDate fecha = LocalDate.parse(fechaStr);
	                    dias.add(fecha);
	                }
	            }
	        }
	    }
	    
	    return dias;
	}

}
