package giis.demo.model.reserva;

public class Usuario {
	
	private int usuarioId;
	private String name;
	private String tarjetaBancaria;
	
	
	/**
	 * Constructor de la clase por defecto
	 * @param usuarioId
	 * @param name
	 * @param tarjetaBancaria
	 */
	public Usuario(String name, String tarjetaBancaria) {
		
		this.name = name;
		this.tarjetaBancaria = tarjetaBancaria;
	}
	
	/**
	 * Obtiene el id del usuario
	 * @return id
	 */
	public int getUsuarioId() {
		return usuarioId;
	}
	
	/**
	 * Devuelve el nombre del usuario
	 * @return nombre
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Devuelve la tarjeta del usuario
	 * @return tarjeta
	 */
	public String getTarjetaBancaria() {
		return tarjetaBancaria;
	}
	
	/**
	 * Permite cambir el id del usuario
	 * @param id
	 */
	public void setUsuarioId(int id) {
		this.usuarioId = id;
	}

}
