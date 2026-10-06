package giis.demo.exceptions;

public class SinDisponibilidadException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Excepción creada para ser lanzada únicamente en el caso de que
	 * no haya el número de asientos disponibles para una tribuna y sección dadas.
	 * 
	 * @param string
	 */
	public SinDisponibilidadException(String string) {
		
		super(string);

	}

}
