package giis.demo.jdbc.tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.tienda.Producto;

public class ProductoJdbc {

	private static final String QUERY_PRODUCTOS = "SELECT id_producto, nombre, tipo, precio FROM Producto";

	public static List<Producto> getProductos(Connection con) throws SQLException {
		List<Producto> productos = new ArrayList<>();
		
		try (PreparedStatement ps = con.prepareStatement(QUERY_PRODUCTOS);
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				Producto p = new Producto(
					rs.getInt("id_producto"), 
					rs.getString("nombre"), 
					rs.getDouble("precio"), 
					rs.getString("tipo")
				);
				productos.add(p);
			}
		}
		return productos;
	}
}
