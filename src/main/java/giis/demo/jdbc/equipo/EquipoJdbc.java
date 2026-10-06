package giis.demo.jdbc.equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Entrenador;
import giis.demo.model.equipo.Equipo;

public class EquipoJdbc {
	public final static String QUERY_GET_CATEGORY_ID = "SELECT id_categoria FROM CategoriaEquipo WHERE nombre=?";
	
	public final static String QUERY_CREATE_TEAM = "INSERT INTO Equipo2 VALUES "
			+ "(?,?,?,?,?)";
	
	public final static String QUERY_SAVE_TEAM_MEMBERS = 
			"UPDATE EmpleadoDeportivo SET id_equipo=? WHERE dni=?";
	
	public static void almacenarEquipo(Connection con, Equipo equipo, List<EmpleadoDeportivo> jugadores, List<Entrenador> entrenadores, List<EmpleadoDeportivo> tecnicosAdicionales) throws SQLException {

		int idCategoria = 0;
		try(PreparedStatement pstGetCategoryId = con.prepareStatement(QUERY_GET_CATEGORY_ID)){
			pstGetCategoryId.setString(1, equipo.getCategoriaEquipo().getNombre());
			
			ResultSet rsCategoria = pstGetCategoryId.executeQuery();
			while(rsCategoria.next()) {
				idCategoria = rsCategoria.getInt(1);
			}
		}
		
		try(PreparedStatement pstCreateTeam = con.prepareStatement(QUERY_CREATE_TEAM)){
			pstCreateTeam.setInt(1, equipo.getIdEquipo());
			pstCreateTeam.setString(2, equipo.getNombre());
			pstCreateTeam.setInt(3, equipo.getIsPropio()? 1 : 0);
			pstCreateTeam.setString(4, equipo.getTipoEquipo());
			pstCreateTeam.setInt(5, idCategoria);
			
			int updatedRows = pstCreateTeam.executeUpdate();
			if(updatedRows != 1) {
				throw new RuntimeException("numero de filas actualizadas incorrecto");
			}
		}
		
		for(EmpleadoDeportivo ed : jugadores) {
			try(PreparedStatement pstSaveJugadores = con.prepareStatement(QUERY_SAVE_TEAM_MEMBERS)){
				pstSaveJugadores.setInt(1, equipo.getIdEquipo());
				pstSaveJugadores.setString(2, ed.getDni());
				
				pstSaveJugadores.executeUpdate();
			}
		}
		
		for(Entrenador entr : entrenadores) {
			try(PreparedStatement pstSaveEntrenadores = con.prepareStatement(QUERY_SAVE_TEAM_MEMBERS)){
				pstSaveEntrenadores.setInt(1, equipo.getIdEquipo());
				pstSaveEntrenadores.setString(2, entr.getDni());
				
				pstSaveEntrenadores.executeUpdate();
			}
		}
		
		for(EmpleadoDeportivo ed : tecnicosAdicionales) {
			try(PreparedStatement pstSaveTecAdicionales = con.prepareStatement(QUERY_SAVE_TEAM_MEMBERS)){
				pstSaveTecAdicionales.setInt(1, equipo.getIdEquipo());
				pstSaveTecAdicionales.setString(2, ed.getDni());
				
				pstSaveTecAdicionales.executeUpdate();
			}
		}
	}

}
