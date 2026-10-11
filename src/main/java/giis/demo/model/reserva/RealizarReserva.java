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

	public boolean guardarUsuario(String name, String cardNumber) throws SQLException {
		
		return jdbc.guardarUsuario(name, cardNumber);
		
	}

}
