package giis.demo.model.vacaciones;

public class AlertaGerente {

	private int idAlerta;
	private String dniEmpleado;
	private String nombreEmpleado;
	private String fechaCreacion;
	private String mensaje;
	private boolean leida;

	public AlertaGerente(int idAlerta, String dniEmpleado, String nombreEmpleado, String fechaCreacion, String mensaje,
			boolean leida) {
		this.idAlerta = idAlerta;
		this.dniEmpleado = dniEmpleado;
		this.nombreEmpleado = nombreEmpleado;
		this.fechaCreacion = fechaCreacion;
		this.mensaje = mensaje;
		this.leida = leida;
	}

	public int getIdAlerta() {
		return idAlerta;
	}

	public void setIdAlerta(int idAlerta) {
		this.idAlerta = idAlerta;
	}

	public String getDniEmpleado() {
		return dniEmpleado;
	}

	public void setDniEmpleado(String dniEmpleado) {
		this.dniEmpleado = dniEmpleado;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isLeida() {
		return leida;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}
}
