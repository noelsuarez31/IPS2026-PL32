package giis.demo.model.noticias;
import java.sql.SQLException;
import java.util.List;

import giis.demo.jdbc.JDBC;

public class VisualizarNoticiasService {
	private JDBC jdbc = new JDBC();
	
	/**
	 * Devuelve las noticias almacenadas en la BBDD ordenadas por fecha
	 * @return
	 */
	public List<Noticia> obtenerNoticiasOrdenadasPorFecha() {
		try {
			return jdbc.getNoticias();
		} catch (SQLException e) {
			throw new RuntimeException("Error al consultar las noticias del club: " + e.getMessage(), e);
		}
	}
}