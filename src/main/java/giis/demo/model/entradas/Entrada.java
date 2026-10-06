package giis.demo.model.entradas;

public class Entrada {
	
	private int id_entrada;
	private int id_partido;
	private int id_venta;
	private int id_butaca;
	private double precio;
	
	/**
	 * Constructor de la clase
	 * @param id_entrada
	 * @param id_partido
	 * @param id_venta
	 * @param id_butaca
	 * @param precio
	 */
	public Entrada(int id_entrada, int id_partido, int id_venta, int id_butaca, double precio) {

		this.id_entrada = id_entrada;
		this.id_partido = id_partido;
		this.id_venta = id_venta;
		this.id_butaca = id_butaca;
		this.precio = precio;
	}

	/**
	 * Devuelve la id de la entrada
	 * @return id de la entrada
	 */
	public int getId_entrada() {
		return id_entrada;
	}

	/**
	 * Devuelve el id del partido
	 * @return el id del partido
	 */
	public int getId_partido() {
		return id_partido;
	}

	/**
	 * Devuelve el id de la venta
	 * @return id de la venta
	 */
	public int getId_venta() {
		return id_venta;
	}

	/**
	 * Devuelve el id de la butaca
	 * @return id de la butaca
	 */
	public int getId_butaca() {
		return id_butaca;
	}
	
	/**
	 * Devuelve el precio de la entrada
	 * @return precio de la entrada
	 */
	public double getPrecio() {
		return precio;
	}
	
	

}
