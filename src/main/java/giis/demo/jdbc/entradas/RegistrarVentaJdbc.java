package giis.demo.jdbc.entradas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import giis.demo.model.entradas.Butaca;
import giis.demo.model.entradas.VentaDeEntradas;

public class RegistrarVentaJdbc implements ServiceJdbc{
	
	
	private static final String QUERY_INSERTVENTAS = "INSERT INTO Venta(fecha, concepto, tipo, total) VALUES (?,?,?,?) ";
	private static final String QUERY_INSERTENTRADA = "INSERT INTO Entrada(id_partido, id_venta, id_butaca, precio) VALUES (?,?,?,?) ";

	
	/**
	 * Registra las ventas y entrads en la base de datos
	 * @param idPartido
	 * @param butacas
	 * @throws SQLException
	 */
	public static void registrar(int idPartido, List<Butaca> butacas) throws SQLException {

		try (Connection con = DriverManager.getConnection(URL_GROUP2)) {
			
			con.setAutoCommit(false);
			
			try {
				
				int idVenta = insertarVenta(con, idPartido, butacas.size()*VentaDeEntradas.PRICE_ENTRADAS);
				insertarEntradas(con, idPartido, idVenta, butacas, VentaDeEntradas.PRICE_ENTRADAS);
				con.commit();
				
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		}

	}
	
	/**
	 * Inserta la venta en la BBDD
	 * @param con
	 * @param idPartido
	 * @param total
	 * @return la clave primaria de la venta recién creada
	 * @throws SQLException
	 */
	private static int insertarVenta(Connection con, int idPartido, int total) throws SQLException {
		
		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERTVENTAS, Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setString(1, LocalDate.now().toString());
			ps.setString(2, "ENTRADAS PARA EL PARTIDO " + idPartido);
			ps.setString(3, "ENTRADAS");
			ps.setDouble(4, total);
			
			ps.executeUpdate();
			
			try(ResultSet rs = ps.getGeneratedKeys()){ //Para obtener la PK generada
				
				rs.next();
				return rs.getInt(1);
			}
		}

	}
	
	/**
	 * Inserta las entradas en la BBDD
	 * @param con
	 * @param idPartido
	 * @param idVenta
	 * @param butacas
	 * @param precio
	 * @throws SQLException
	 */
	private static void insertarEntradas(Connection con, int idPartido, int idVenta, List<Butaca> butacas, double precio) throws SQLException {

		try (PreparedStatement ps = con.prepareStatement(QUERY_INSERTENTRADA)){
			
			for(Butaca butaca : butacas) {
				
				ps.setInt(1, idPartido);
				ps.setInt(2, idVenta);
				ps.setInt(3, butaca.getId_butaca());
				ps.setDouble(4, precio);
				
				ps.executeUpdate();
				
			}
			
		}
		
	}

}
