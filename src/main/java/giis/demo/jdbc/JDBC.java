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
	
	//public static final String DRIVER_GROUP2 = "org.sqlite.JDBC";
	public static final String URL_GROUP2 = "jdbc:sqlite:DemoDB.db";
	
	private Connection con;
	
	/**
	 * Constructor de la clase, inicializa la conexion
	 */
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
	public List<Butaca> getFreeButacas(int idPartido, TipoTribuna tribuna, TipoSeccion seccion)throws SQLException {

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
	
	/**
	 * Obtiene todas las butacas en una tribuna y sección
	 * @param idPartido
	 * @param tribuna
	 * @param seccion
	 * @return butacas de una tribuna y seccion
	 * @throws SQLException
	 */
	public List<Butaca> getAllButacas(int idPartido, TipoTribuna tribuna, TipoSeccion seccion) throws SQLException {

		return ButacaJdbc.getAllButacas(con, idPartido, tribuna, seccion);

	}
	
	/**
	 * Cierra la conexion
	 * @throws SQLException
	 */
	public void close() throws SQLException{
		
		if(con!=null && !con.isClosed()) {
			
			con.close();
		}
		
	}

}
