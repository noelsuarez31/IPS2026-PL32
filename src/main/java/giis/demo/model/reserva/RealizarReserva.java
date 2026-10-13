package giis.demo.model.reserva;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import giis.demo.jdbc.JDBC;

public class RealizarReserva {
	
	private JDBC jdbc;
	
	public RealizarReserva() {
		
		jdbc = new JDBC();
		
	}
	
	public Instalacion[] obtenerInstalacionesDisponibles() throws SQLException{
		
		Instalacion[] instalaciones = jdbc.getAvailableInstalations().toArray(new Instalacion[0]);
		return instalaciones;
	}
	
	/**
	 * Devuelve el id del usuario que bien ya esta en la BBDD o que ha sido creado
	 * @param name
	 * @param cardNumber
	 * @return el id del usuario
	 * @throws SQLException
	 */
	public void guardarUsuario(Usuario user) throws SQLException {
		
		jdbc.obtenerOCrearUsuario(user);
		
	}
	
	/**
	 * Añade la reserva a la BBDD
	 * @param reserva
	 * @return true si se ha podido realizar la reseva, false en caso contrario
	 * @throws SQLException
	 */
	public boolean registrarNuevaReserva(Reserva reserva) throws SQLException {
		
		return jdbc.registrarReserva(reserva);
		
	}
	
	/**
	 * Obtiene las franjas ocupadas de una instalacion en una fecha
	 * @param id_Instalacion
	 * @param fecha
	 * @return las franajs ocupadas y el motivo
	 * @throws SQLException
	 */
	public List<Intervalo> obtenerOcupaciones(int id_Instalacion, LocalDate fecha) throws SQLException{
		
		return jdbc.obtenerOcupaciones(id_Instalacion, fecha);
		
	}
	
	/**
	 * Comprueba si el horario elegido por el usuario está disponible
	 * @param idInstalacion
	 * @param fecha
	 * @param horaInicio
	 * @param horaFin
	 * @return true si es válido, false en caso contrario
	 * @throws SQLException
	 */
	public boolean comprobarDisponibilidad(int idInstalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) throws SQLException {
		
		return jdbc.comprobarDisponibilidad(idInstalacion, fecha, horaInicio, horaFin);
		
	}
	
	
	public void close() throws SQLException {
		
		jdbc.close();
		
	}
	

}
