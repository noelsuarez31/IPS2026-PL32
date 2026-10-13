package giis.demo.model.reserva;

import java.sql.SQLException;

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
	
	public void close() throws SQLException {
		
		jdbc.close();
		
	}
	

}
