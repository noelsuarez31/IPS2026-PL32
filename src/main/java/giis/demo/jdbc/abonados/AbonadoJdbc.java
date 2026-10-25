package giis.demo.jdbc.abonados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AbonadoJdbc {

	public final static String QUERY_TIENE_ABONO = "SELECT * FROM Abono ab, Abonado a WHERE ab.id_abonado = a.id_abonado"
			+ " AND a.dni = ? AND fecha_inicio = ?";

	public final static String QUERY_GET_ID_ABONADO = "SELECT id_abonado FROM Abonado WHERE dni = ?";
	
	public final static String QUERY_INSERT_ABONADO =
			"INSERT INTO Abonado (dni, nombre, apellidos, fecha_nacimiento, email) VALUES (?,?,?,?,?)";
	
	/**
	 * Consulta a la BBDD si un cliente tiene ya un abono de temporada
	 * @param con
	 * @param dni
	 * @param fechaDeInicio
	 * @return true si ya lo tiene false en caso contrario
	 * @throws SQLException
	 */
	public static boolean tieneAbonoTemporada (Connection con, String dni, LocalDate fechaDeInicio) throws SQLException {
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_TIENE_ABONO)){
			
			pst.setString(1, dni);
			pst.setString(2, fechaDeInicio.toString());
			
			try(ResultSet rs  = pst.executeQuery()){
				
				return rs.next();
			}
			
		}
		
	}
}
