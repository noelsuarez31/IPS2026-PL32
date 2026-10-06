package giis.demo.jdbc.equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.equipo.CategoriaEquipo;

public class CategoriaEquipoJdbc {
	public final static String QUERY_GET_CATEGORY_NAME = "SELECT * FROM CategoriaEquipo WHERE nombre=?";
	
	public final static String 	QUERY_CATEGORIES_FOR_TIPE = "SELECT * FROM CategoriaEquipo WHERE tipo_equipo=?";
	
	public static CategoriaEquipo obtenerObjetoCategoria(Connection con, String nombreCategoriaSeleccionada) throws SQLException {
		CategoriaEquipo categoria = null;
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_GET_CATEGORY_NAME);){
			pst.setString(1, nombreCategoriaSeleccionada);
			
			ResultSet rsCategoria = pst.executeQuery();
			while(rsCategoria.next()) {
				categoria = new CategoriaEquipo(nombreCategoriaSeleccionada, rsCategoria.getInt(2), 
						rsCategoria.getInt(3), rsCategoria.getInt(4), rsCategoria.getString(5));
				return categoria;
			}
		}
		
		return categoria;
	}

	public static List<String> obtenerNombreDeCategoriasPorTipo(Connection con, String tipo) throws SQLException {
		
		List<String> categoriasPorTipo = new ArrayList<String>();
		
		try(PreparedStatement pst = con.prepareStatement(QUERY_CATEGORIES_FOR_TIPE)){
			pst.setString(1,tipo);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				categoriasPorTipo.add(rs.getString(1));
			}
		}
	    
		return categoriasPorTipo;
	}
	
}
