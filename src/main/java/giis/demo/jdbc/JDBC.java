package giis.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import giis.demo.jdbc.entradas.ButacaJdbc;
import giis.demo.jdbc.entradas.PartidoJdbc;
import giis.demo.jdbc.entradas.RegistrarVentaJdbc;
import giis.demo.model.entradas.Butaca;
import giis.demo.model.entradas.Partido;
import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;

public class JDBC {
	
	public static final String URL_GROUP2 = "jdbc:sqlite:C:\\Users\\Gaby\\iCloudDrive\\Documents\\0 - ESTUDIOS\\6- UNIVERSIDAD\\3- TERCERO DE CARRERA\\1- PRIMER SEMESTRE\\INGENIERÍA DEL PROCESO SOFTWARE\\PRÁCTICA\\BBDD\\BBDD-IPS.db";
	
	private static Connection con;
	
	public JDBC() {
		
		try {
			
			con = DriverManager.getConnection(URL_GROUP2);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 * Obtiene las butacas libres disponibles
	 * @param idPartido
	 * @param tribuna
	 * @param seccion
	 * @return las butcas libres
	 * @throws SQLException
	 */
	public List<Butaca> getFreeButacas(int idPartido, TipoTribuna tribuna, TipoSeccion seccion)
			throws SQLException {

		return ButacaJdbc.getFreeButacas(con, idPartido, tribuna, seccion);

	}
	
	/**
	 * Obtienes los distintos partidos disponibles
	 * @return los partidos disponibles
	 * @throws SQLException
	 */
	public List<Partido> getPartidos() throws SQLException {

		return PartidoJdbc.getPartidos(con);

	}
	
	/**
	 * Almacena las entradas y la venta
	 * @param idPartido
	 * @param butacasSeleccionadas
	 * @throws SQLException
	 */
	public void almacenar(int idPartido, List<Butaca> butacasSeleccionadas) throws SQLException {
		
		RegistrarVentaJdbc.registrar(con,idPartido, butacasSeleccionadas);
		
	}

}
