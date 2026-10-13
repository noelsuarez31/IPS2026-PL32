package giis.demo.jdbc.empleado;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.Entrenador;
import giis.demo.model.empleado.TipoEntrenador;

public class EntrenadorJdbc {
	private static final String QUERY_INSERT_MANAGER = "INSERT INTO Entrenador (dni, tipo_entrenador) VALUES (?, ?)";
	private final static String QUERY_GET_COMPLETE_MANAGERS = "SELECT b.dni, b.nombre, b.apellido, b.salario, b.fecha_nacimiento, b.numero_de_telefono, e.tipo_entrenador "
			+ "FROM Entrenador e JOIN EmpleadoDeportivo ed ON e.dni = ed.dni JOIN BaseEmpleado b ON e.dni = b.dni "
			+ "WHERE ed.id_equipo IS NULL";
	
	/**
	 * Devuelve una lista de los entrenadores que no tienen asignado ningun equipo
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static List<Entrenador> obtenerEntrenadoresDisponibles(Connection con) throws SQLException {
	    List<Entrenador> cuerpoTecnico = new ArrayList<Entrenador>();
	    
	    try(PreparedStatement pst = con.prepareStatement(QUERY_GET_COMPLETE_MANAGERS)){
	        ResultSet rsEntrenador = pst.executeQuery();
	        
	        while (rsEntrenador.next()) {
	            
	        	java.sql.Date fechaSql = rsEntrenador.getDate("fecha_nacimiento");
	            LocalDate fechaNac = (fechaSql != null) ? fechaSql.toLocalDate() : null;
	            BigDecimal salario = rsEntrenador.getBigDecimal("salario");

	            // Leer tipo 
	            String tipoBD = rsEntrenador.getString("tipo_entrenador");
	            TipoEntrenador tipo = null;
	            
	            if (tipoBD != null) {
	                tipo = TipoEntrenador.valueOf(tipoBD.toUpperCase());
	            }

	            // Instanciar entrenador
	            Entrenador entrenador = new Entrenador(
	                    rsEntrenador.getString("dni"),
	                    rsEntrenador.getString("nombre"),
	                    rsEntrenador.getString("apellido"),
	                    salario,
	                    fechaNac,
	                    rsEntrenador.getString("numero_de_telefono"),
	                    tipo);

	            cuerpoTecnico.add(entrenador);
	        }
	    }
	    
	    return cuerpoTecnico;
	}
	
	public void insertarEntrenador(Entrenador entrenador, Connection con) throws SQLException {
	    try (PreparedStatement ps = con.prepareStatement(QUERY_INSERT_MANAGER)) {
	        ps.setString(1, entrenador.getDni());
	        ps.setString(2, entrenador.getTipoEntrenador().name());
	        ps.executeUpdate();
	    }
	}
	
}
