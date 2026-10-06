package giis.demo.model.entradas;

import java.util.Date;

public class Partido {
	
	//Atributos de la BBDD
	
	private int id_partido;
	private Date fecha;
	private int id_local;
	private int id_visitante;
	
	//Atributos extras para facilitar la comprensión
	
	private String nameLocal;
	private String nameVisitante;	
	
	/**
	 * Constructor de la clase con parametros
	 * @param id_partido
	 * @param fecha
	 * @param id_local
	 * @param id_visitante
	 * @param nameLocal
	 * @param nameVisitante
	 */
	public Partido(int id_partido, Date fecha, int id_local, int id_visitante, String nameLocal, String nameVisitante) {
		
		this.id_partido = id_partido;
		this.fecha = fecha;
		this.id_local = id_local;
		this.id_visitante = id_visitante;
		this.nameLocal = nameLocal;
		this.nameVisitante = nameVisitante;
	}
	
	/**
	 * Devuelve el id del partido
	 * @return el id del partido
	 */
	public int getId_partido() {
		return id_partido;
	}
	
	/**
	 * Devuelve la fecha
	 * @return fecha partido
	 */
	public Date getFecha() {
		return fecha;
	}
	
	/**
	 * Devuelve el id del equipo local
	 * @return id del equipo local
	 */
	public int getId_local() {
		return id_local;
	}
	
	/**
	 * Devuelve el id del equipo visitante
	 * @return id del equipo visitante
	 */
	public int getId_visitante() {
		return id_visitante;
	}

	/**
	 * Devuelve el nombre del equipo local
	 * @return nombre del equipo local
	 */
	public String getNameLocal() {
		return nameLocal;
	}

	/**
	 * Devuelve el nombre del equipo visitante
	 * @return nombre del equipo visitante
	 */
	public String getNameVisitante() {
		return nameVisitante;
	}

	/**
	 * toString de la clase
	 */
	@Override
	public String toString() {
		
		return "" + fecha + " - [" + nameLocal + " - " + nameVisitante + "]";
	}

	
}
