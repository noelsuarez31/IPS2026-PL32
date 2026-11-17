package giis.demo.model.log;

/**
 * Representa un evento registrado en el archivo de log de la sesión actual.
 */
public class EventoLog {

	private final String hora;
	private final String usuario;
	private final String rol;
	private final String tipo;
	private final String detalle;

	public EventoLog(String hora, String usuario, String rol, String tipo, String detalle) {
		this.hora = hora;
		this.usuario = usuario;
		this.rol = rol;
		this.tipo = tipo;
		this.detalle = detalle;
	}

	public String getHora() {
		return hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getRol() {
		return rol;
	}

	public String getTipo() {
		return tipo;
	}

	public String getDetalle() {
		return detalle;
	}

	/**
	 * Indica si el evento corresponde a una modificación sobre la base de datos.
	 */
	public boolean esModificacionBBDD() {
		return tipo != null && tipo.toUpperCase().startsWith("BBDD");
	}

	/**
	 * Indica si el evento corresponde a una acción de usuario.
	 */
	public boolean esAccionUsuario() {
		return !esModificacionBBDD();
	}
}
