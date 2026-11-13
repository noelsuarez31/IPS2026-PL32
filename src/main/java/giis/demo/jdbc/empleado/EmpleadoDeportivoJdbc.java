package giis.demo.jdbc.empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.equipo.CategoriaEquipo;

public class EmpleadoDeportivoJdbc {
	private static final String QUERY_GET_ADITIONAL_MANAGERS =
	        "SELECT * FROM EmpleadoDeportivo WHERE posicion=? AND id_equipo IS NULL";
	
	private static final String QUERY_GET_COMPLETE_ADITIONAL_MANAGERS =
		        "SELECT be.nombre, be.apellido, be.salario, be.fecha_nacimiento, "
		        + "be.numero_de_telefono, m.nombre_usuario, m.contrasena "
		        + "FROM EmpleadoDeportivo ed "
		        + "JOIN BaseEmpleado be ON ed.dni = be.dni "
		        + "JOIN Miembro m ON be.dni = m.dni "
		        + "WHERE ed.dni=?";
	
	private static final String QUERY_GET_PLAYERS =
		        "SELECT * FROM EmpleadoDeportivo WHERE posicion = ? AND id_equipo IS NULL";

	private static final String QUERY_GET_COMPLETE_PLAYERS =
	        "SELECT be.dni, be.nombre, be.apellido, be.salario, "
	        + "be.fecha_nacimiento, be.numero_de_telefono, "
	        + "m.nombre_usuario, m.contrasena, ed.posicion "
	        + "FROM BaseEmpleado be "
	        + "JOIN EmpleadoDeportivo ed ON be.dni = ed.dni "
	        + "JOIN Miembro m ON be.dni = m.dni "
	        + "WHERE ed.dni=?";
	
	private static final String QUERY_INSERT_DEPORTIVO =
	        "INSERT INTO EmpleadoDeportivo (dni, posicion, id_equipo) VALUES (?, ?, NULL)";

	private static final String QUERY_GET_MANAGERS =
	        "SELECT * FROM EmpleadoDeportivo WHERE posicion=? AND id_equipo IS NULL";
	
	private static final String QUERY_GET_COMPLETE_MANAGERS =
	        "SELECT be.dni, be.nombre, be.apellido, be.salario, "
	        + "be.fecha_nacimiento, be.numero_de_telefono, "
	        + "m.nombre_usuario, m.contrasena, ed.posicion "
	        + "FROM BaseEmpleado be "
	        + "JOIN EmpleadoDeportivo ed ON be.dni = ed.dni "
	        + "JOIN Miembro m ON be.dni = m.dni "
	        + "WHERE ed.dni=?";
	
	/**
	 * Devuelve una lista de los empleados deportivos que no tienen asignado 
	 * ningun equipo cuya posicion es TECNICO_ADICIONAL
	 * @param con
	 * @return List<EmpleadoDeportivo>
	 * @throws SQLException
	 */
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
						EmpleadoDeportivo tecnicoAdicional =  new EmpleadoDeportivo(dniTecnicoAdicional,
								rsTecnicoAdicionalCompleto.getString(1), rsTecnicoAdicionalCompleto.getString(2),
								rsTecnicoAdicionalCompleto.getBigDecimal(3), LocalDate.parse(rsTecnicoAdicionalCompleto.getString(4)),
								rsTecnicoAdicionalCompleto.getString(5), posicion,
								rsTecnicoAdicionalCompleto.getString("nombre_usuario"),
								rsTecnicoAdicionalCompleto.getString("contrasena"));
						
						tecnicosAdicionales.add(tecnicoAdicional);
					}
				}
			}
		} 
		
		return tecnicosAdicionales;
	}


	/**
	 * Devuelve una lista de empleados deportivos cuya posicion es JUGADOR
	 * @param con
	 * @param categoria
	 * @return List<EmpleadoDeportivo>
	 * @throws SQLException
	 */
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
						jugadores.add(new EmpleadoDeportivo(
						        dni,
						        rsJugadorCompleto.getString("nombre"),
						        rsJugadorCompleto.getString("apellido"),
						        rsJugadorCompleto.getBigDecimal("salario"),
						        LocalDate.parse(rsJugadorCompleto.getString("fecha_nacimiento")),
						        rsJugadorCompleto.getString("numero_de_telefono"),
						        Posicion.valueOf(rsJugadorCompleto.getString("posicion")),
						        rsJugadorCompleto.getString("nombre_usuario"),
						        rsJugadorCompleto.getString("contrasena")
						));
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
	
	/**
	 * Devuelve una lista de empleados deportivos con posicion ENTRENADOR
	 * que no tienen asignado ningun equipo
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static List<EmpleadoDeportivo> obtenerEntrenadoresDisponibles(Connection con) throws SQLException {
		List<EmpleadoDeportivo> cuerpoTecnico = new ArrayList<>();

	    try (PreparedStatement pst = con.prepareStatement(QUERY_GET_MANAGERS)) {
	        pst.setString(1, Posicion.ENTRENADOR.name());

	        try (ResultSet rsEntrenador = pst.executeQuery()) {
	            while (rsEntrenador.next()) {
	                String dniEntrenador = rsEntrenador.getString("dni");

	                try (PreparedStatement pst2 = con.prepareStatement(QUERY_GET_COMPLETE_MANAGERS)) {
	                    pst2.setString(1, dniEntrenador);

	                    try (ResultSet rsEntrenadorCompleto = pst2.executeQuery()) {
	                        if (rsEntrenadorCompleto.next()) {
	                            EmpleadoDeportivo entrenador = new EmpleadoDeportivo(
	                                    dniEntrenador,
	                                    rsEntrenadorCompleto.getString("nombre"),
	                                    rsEntrenadorCompleto.getString("apellido"),
	                                    rsEntrenadorCompleto.getBigDecimal("salario"),
	                                    LocalDate.parse(rsEntrenadorCompleto.getString("fecha_nacimiento")),
	                                    rsEntrenadorCompleto.getString("numero_de_telefono"),
	                                    Posicion.valueOf(rsEntrenadorCompleto.getString("posicion")),
	                                    rsEntrenadorCompleto.getString("nombre_usuario"),
	                                    rsEntrenadorCompleto.getString("contrasena")
	                            );

	                            cuerpoTecnico.add(entrenador);
	                        }
	                    }
	                }
	            }
	        }
	    }

	    return cuerpoTecnico;
	}

	/**
	 * Inserta un empleado deportivo recien dado de alta (sin equipo asignado).
	 * Los datos comunes se insertan antes con BaseEmpleadoJdbc
	 * @param con
	 * @param emp
	 * @throws SQLException
	 */
	public static void insertarEmpleadoDeportivo(Connection con, EmpleadoDeportivo emp) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement(QUERY_INSERT_DEPORTIVO)) {
			pst.setString(1, emp.getDni());
			pst.setString(2, emp.getPosicion().name());
			pst.executeUpdate();
		}
	}

}
