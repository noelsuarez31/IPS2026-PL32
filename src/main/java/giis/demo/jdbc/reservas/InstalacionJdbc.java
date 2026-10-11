package giis.demo.jdbc.reservas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.reserva.Instalacion;

public class InstalacionJdbc {
	
	private static final String QUERY_AVAILABLE_INSTALATIONS = "SELECT id_instalacion, nombre FROM instalacion ";
	
	public static List<Instalacion> getAvailableInstalations(Connection con) throws SQLException {

		List<Instalacion> instalacionesDisponibles = new ArrayList<Instalacion>();

		try (Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(QUERY_AVAILABLE_INSTALATIONS)) {

				while(rs.next()) {
					
					Instalacion instalacion = new Instalacion(rs.getInt("id_instalacion"), rs.getString("nombre"));
					instalacionesDisponibles.add(instalacion);
					
				}
				
				return instalacionesDisponibles;
		}

	}

}
