package giis.demo.model.entradas;

public class Equipo {
	
	private int id_equipo;
	private String name;
	private boolean isPropio;
	
	/**
	 * Constructor de la clase
	 * @param id_equipo
	 * @param name
	 * @param isPropio
	 */
	public Equipo(int id_equipo, String name, boolean isPropio) {
		
		this.id_equipo = id_equipo;
		this.name = name;
		this.isPropio = isPropio;
	}

	/**
	 * Devuelve el id el equipo
	 * @return el id del equipo
	 */
	public int getId_equipo() {
		return id_equipo;
	}

	/**
	 * Devuelve el nombre el equipo
	 * @return el nombre del equipo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Confirma si un equipo es Propio
	 * @return true si el equipo es local, false en caso contrario
	 */
	public boolean isPropio() {
		return isPropio;
	}
	
	
}
