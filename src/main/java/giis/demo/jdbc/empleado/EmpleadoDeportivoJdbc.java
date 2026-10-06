package giis.demo.jdbc.empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.exceptions.TeamException;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.equipo.CategoriaEquipo;

public class EmpleadoDeportivoJdbc {
	public final static String QUERY_GET_ADITIONAL_MANAGERS = "SELECT * FROM EmpleadoDeportivo WHERE posicion=?";
	public final static String QUERY_GET_COMPLETE_ADITIONAL_MANAGERS = "SELECT nombre, apellido, salario, fecha_nacimiento, numero_de_telefono FROM"
			+ " EmpleadoDeportivo ed, BaseEmpleado bd WHERE ed.dni=bd.dni and ed.dni=?";
	
	public final static String QUERY_GET_PLAYERS = "SELECT * FROM EmpleadoDeportivo WHERE posicion = ?";
	public final static String QUERY_GET_COMPLETE_PLAYERS = "SELECT * FROM BaseEmpleado be, EmpleadoDeportivo ed "
			+ "WHERE be.dni=ed.dni AND ed.dni=?";
	
	public static List<EmpleadoDeportivo> obtenerRestoTecnicos(Connection con) throws SQLException {
		List<EmpleadoDeportivo> tecnicosAdicionales = new ArrayList<EmpleadoDeportivo>();
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_ADITIONAL_MANAGERS)){
			pst.setString(1, Posicion.TECNICO_ADICIONAL.name());
			ResultSet rsTecnicoAdicional = pst.executeQuery();
			
			while(rsTecnicoAdicional.next()) {
				String dniTecnicoAdicional = rsTecnicoAdicional.getString(1);
				Posicion posicion = Posicion.valueOf(rsTecnicoAdicional.getString(2));
				try(PreparedStatement pst2 = con.prepareStatement(QUERY_GET_COMPLETE_ADITIONAL_MANAGERS);){
					pst2.setString(1, dniTecnicoAdicional);
					ResultSet rsTecnicoAdicionalCompleto = pst2.executeQuery();
					while(rsTecnicoAdicionalCompleto.next()) {
						EmpleadoDeportivo tecnicoAdicional = new EmpleadoDeportivo(dniTecnicoAdicional, rsTecnicoAdicionalCompleto.getString(1), rsTecnicoAdicionalCompleto.getString(2),
								rsTecnicoAdicionalCompleto.getBigDecimal(3), LocalDate.parse(rsTecnicoAdicionalCompleto.getString(4)) ,rsTecnicoAdicionalCompleto.getString(5), posicion);
						tecnicosAdicionales.add(tecnicoAdicional);
					}
				}
			}
		} 
		
		return tecnicosAdicionales;
	}


	public static List<EmpleadoDeportivo> obtenerJugadoresDisponibles(Connection con, CategoriaEquipo categoria) throws SQLException {
		List<EmpleadoDeportivo> jugadores = new ArrayList<EmpleadoDeportivo>();
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_PLAYERS)){
			pst.setString(1, Posicion.JUGADOR.name());
			ResultSet rsJugadores = pst.executeQuery();
			
			while(rsJugadores.next()) {
				String dni = rsJugadores.getString(1);
	
				try(PreparedStatement pst2 = con.prepareStatement(QUERY_GET_COMPLETE_PLAYERS);){
					pst2.setString(1,dni);
					ResultSet rsJugadorCompleto = pst2.executeQuery();
					
					while(rsJugadorCompleto.next()) {
						jugadores.add(new EmpleadoDeportivo(dni, rsJugadorCompleto.getString(2), rsJugadorCompleto.getString(3),
								rsJugadorCompleto.getBigDecimal(4), LocalDate.parse(rsJugadorCompleto.getString(5)), rsJugadorCompleto.getString(6), Posicion.JUGADOR));
					}
				}
			}
		}
		
		if(categoria.getNombre().equals("Primer equipo") || categoria.getNombre().equals("Filial")) {
			return jugadores;
		}
		
		List<EmpleadoDeportivo> jugadoresFiltradosPorCategoria = new ArrayList<EmpleadoDeportivo>();
		for(EmpleadoDeportivo jugador : jugadores) {
			if(categoria.esEdadValida(jugador.calcularEdad())) {
				jugadoresFiltradosPorCategoria.add(jugador);
			}
		}
		
		return jugadoresFiltradosPorCategoria;
	}

}
