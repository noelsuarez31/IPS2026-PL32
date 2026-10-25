package giis.demo.jdbc.abonados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.abonos.Abono;

public class AbonoJdbc {
	
	public final static String QUERY_ASIENTOS_OCUPADOS =  "SELECT b.fila, b.asiento "
	          + "FROM Abono a JOIN Butaca b ON b.id_butaca = a.id_butaca "
	          + "WHERE a.id_equipo = ? AND b.tribuna = ? AND b.seccion = ? "
	          + "AND ? BETWEEN a.fecha_inicio AND a.fecha_fin "
	          + "UNION "
	          + "SELECT b.fila, b.asiento "
	          + "FROM Entrada e JOIN Partido p ON p.id_partido = e.id_partido "
	          + "JOIN Butaca b ON b.id_butaca = e.id_butaca "
	          + "WHERE p.id_local = ? AND b.tribuna = ? AND b.seccion = ? AND p.fecha >= ?";
	
	public final static String QUERY_INFO_ABONO =
			"SELECT e.nombre, b.tribuna, b.seccion, b.fila, b.asiento, "
		  + "a.fecha_inicio, a.fecha_fin, a.fecha_registro, a.categoria, a.precio "
		  + "FROM Abono a "
		  + "JOIN Abonado ab ON ab.id_abonado = a.id_abonado "
		  + "JOIN Butaca b ON b.id_butaca = a.id_butaca "
		  + "JOIN Equipo e ON e.id_equipo = a.id_equipo "
		  + "WHERE ab.dni = ? AND a.fecha_inicio = ?";
	
	public static final DateTimeFormatter FORMATO_FECHA_HORA =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public static List<int[]> getAsientosOcupados(Connection con, int idEquipo, String tribuna, String seccion, LocalDate hoy) throws SQLException {
		
		List<int[]> asientosOcupados = new ArrayList<>();
		
		String fecha = hoy.toString();
		
		try (PreparedStatement pst = con.prepareStatement(QUERY_ASIENTOS_OCUPADOS)) {
			
			pst.setInt(1, idEquipo);
			pst.setString(2, tribuna);
			pst.setString(3, seccion);
			pst.setString(4, fecha);
			
			pst.setInt(5, idEquipo);
			pst.setString(6, tribuna);
			pst.setString(7, seccion);
			pst.setString(8, fecha);
			
			try (ResultSet rs = pst.executeQuery()) {
				
				while (rs.next()) {
					
					int fila = rs.getInt("fila");
					int asiento = rs.getInt("asiento");
					
					asientosOcupados.add(new int[] { fila, asiento });
				}
			}
		}
		
		return asientosOcupados;
	}
	
public static Abono getInfoAbono(Connection con, String dni, LocalDate fechaInicio) throws SQLException {
		
				
		try (PreparedStatement pst = con.prepareStatement(QUERY_INFO_ABONO)) {
			
			pst.setString(1, dni);
			pst.setString(2, fechaInicio.toString());
		
			
			try (ResultSet rs = pst.executeQuery()) {
				
				if(rs.next()) {
					
					return new Abono(rs.getString("nombre"), rs.getString("tribuna"), rs.getString("seccion"),
							rs.getInt("fila"), rs.getInt("asiento"), LocalDate.parse(rs.getString("fecha_inicio")),
							LocalDate.parse(rs.getString("fecha_fin")), LocalDateTime.parse(rs.getString("fecha_registro"), FORMATO_FECHA_HORA),
							rs.getString("categoria"), rs.getDouble("precio"));
							
				}
			}
		}
		
		return null;
		
	}

}
