package giis.demo.model.vacaciones;

public class ElementoInvalidadoVacacion {

	private int idInvalidado;
	private String dniEmpleado;
	private String fecha;
	private String tipo;
	private String detalle;

	public ElementoInvalidadoVacacion(int idInvalidado, String dniEmpleado, String fecha, String tipo, String detalle) {
		this.idInvalidado = idInvalidado;
		this.dniEmpleado = dniEmpleado;
		this.fecha = fecha;
		this.tipo = tipo;
		this.detalle = detalle;
	}

	public int getIdInvalidado() {
		return idInvalidado;
	}

	public void setIdInvalidado(int idInvalidado) {
		this.idInvalidado = idInvalidado;
	}

	public String getDniEmpleado() {
		return dniEmpleado;
	}

	public void setDniEmpleado(String dniEmpleado) {
		this.dniEmpleado = dniEmpleado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
}
