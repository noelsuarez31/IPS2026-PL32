package giis.demo.model.reserva;

public class Instalacion {
	
	private int instalacionId;
	private String name;
	
	/**
	 * Constructor por defecto de la clase
	 * @param instalacionId
	 * @param name
	 */
	public Instalacion(int instalacionId, String name) {
		
		this.instalacionId = instalacionId;
		this.name = name;
	}
	
	/**
	 * Devuelve el id de la instalcion
	 * @return id de la instalación
	 */
	public int getInstalacionId() {
		return instalacionId;
	}
	
	/**
	 * Devuelve el nombre de la instalacion
	 * @return nombre de la instalación
	 */
	public String getName() {
		return name;
	}

	/**
	 * Redefine el toString
	 */
	@Override
	public String toString() {
		return name;
	}
	
	

}
