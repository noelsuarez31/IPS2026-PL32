package giis.demo.model.abonos;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;
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
	
	/**
	 * Calcula la categoria del abonado en funcion de su fecha de nacimiento
	 * @param fechaNacimiento
	 * @return
	 */
	public Categoria calcularCategoria(LocalDate fechaNacimiento) {
		
		LocalDate fechaActual = LocalDate.now();
		
		if(fechaNacimiento == null || fechaNacimiento.isAfter(fechaActual)) {
			throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula ni posterior a la fecha actual");
		}
		
		int edad = fechaActual.getYear() - fechaNacimiento.getYear();
		
		return getCategoria(edad);
		
	}
	
	/**
	 * Metodo privado que devuelve la categoria del abonado en funcion de su edad
	 * @param edad
	 * @return
	 */
	private Categoria getCategoria(int edad) {
		
		if(edad <= EDAD_MAX_INFANTIL) {
			return Categoria.INFANTIL;
		}else if(edad <= EDAD_MAX_SUB26) {
			return Categoria.SUB_26;
		}else if(edad >= EDAD_MIN_JUBILADO) {
			return Categoria.JUBILADO;
		}else {
			return Categoria.ADULTO;
		}
		
	}
	
	public List<int[]> getAsientosOcupados(Equipo equipo, TipoTribuna tribuna, TipoSeccion tipoSeccion) throws SQLException {
		
		return jdbc.getAsientosOcupados(equipo.getIdEquipo(), tribuna.name(), tipoSeccion.name(), LocalDate.now());
		
	}
	
	public Equipo[] getEquiposLocales() throws SQLException {

		return jdbc.getEquiposLocales();
		
	}
	
	public void close() throws SQLException {
		
		jdbc.close();
		
	}
	
	
}
