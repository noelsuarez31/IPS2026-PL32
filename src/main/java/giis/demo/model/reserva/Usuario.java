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
	
	public int getUsuarioId() {
		return usuarioId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTarjetaBancaria() {
		return tarjetaBancaria;
	}
	
	public void setUsuarioId(int id) {
		this.usuarioId = id;
	}

}
