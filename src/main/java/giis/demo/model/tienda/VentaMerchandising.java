package giis.demo.model.tienda;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.jdbc.JDBC;

public class VentaMerchandising {
	private Cesta cesta;
	private List<Producto> catalogo;
	private JDBC jdbc;

	public VentaMerchandising() {
		this.cesta = new Cesta();
		this.catalogo = new ArrayList<>();
		this.jdbc = new JDBC();
	}

	public void cargarCatalogo() throws SQLException {
		this.catalogo = jdbc.getProductosTienda();
	}

	public void almacenarVenta() throws SQLException {
		jdbc.almacenarVentaMerchandising(cesta.getTotal(), cesta.getLineas());
	}
	
	public void close() throws SQLException {
		jdbc.close();
	}

	public List<Producto> getCatalogo() {
		return catalogo;
	}

	public Cesta getCesta() {
		return cesta;
	}
	
	public Producto getProductoPorId(int id) {
		for (Producto p : catalogo) {
			if (p.getId_producto() == id) return p;
		}
		return null;
	}
	
	public Producto getProductoPorNombre(String nombreProducto) {
		for (Producto p : catalogo) {
			if (p.getNombre().equals(nombreProducto)) return p;
		}
		return null;
	}
}
