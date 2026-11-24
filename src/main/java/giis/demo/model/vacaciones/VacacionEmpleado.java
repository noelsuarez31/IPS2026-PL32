package giis.demo.model.vacaciones;

public class VacacionEmpleado {

	private int idVacacion;
	private String dniEmpleado;
	private String fecha;
	private String diaSemana;
	private String estado;

	public VacacionEmpleado(int idVacacion, String dniEmpleado, String fecha, String diaSemana, String estado) {
		this.idVacacion = idVacacion;
		this.dniEmpleado = dniEmpleado;
		this.fecha = fecha;
		this.diaSemana = diaSemana;
		this.estado = estado;
	}

	public int getIdVacacion() {
		return idVacacion;
	}

	public void setIdVacacion(int idVacacion) {
		this.idVacacion = idVacacion;
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

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
