package giis.demo.model.abonos;

import java.sql.SQLException;

import giis.demo.jdbc.JDBC;
import giis.demo.model.equipo.Equipo;

public class VentaDeAbonos extends Venta{

	public enum Categoria { INFANTIL, SUB_26, ADULTO, JUBILADO }
	
	private static final int EDAD_MAX_INFANTIL = 17;
	private static final int EDAD_MAX_SUB26 = 25;
	private static final int EDAD_MIN_JUBILADO = 65;

	private JDBC jdbc;

	public VentaDeAbonos(){
		
		jdbc = new JDBC();

		
	}
	
	public double getPrecio(Categoria categoria) {
		
		switch (categoria) {
			
		case INFANTIL:
			return 100;
		case SUB_26:
			return 200;
		case JUBILADO:
			return 250;
		default:
			return 400;
		}
		
	}

	public Equipo[] getEquiposLocales() throws SQLException {

		return jdbc.getEquiposLocales();
		
	}
	
	public void close() throws SQLException {
		
		jdbc.close();
		
	}
	
	
}
