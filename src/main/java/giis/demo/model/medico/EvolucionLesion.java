package giis.demo.model.medico;

import java.time.LocalDate;

public class EvolucionLesion {

	private LocalDate fecha;
	private String valoracion;
	private String actualizacion;

	public EvolucionLesion(LocalDate fecha, String valoracion, String actualizacion) {
		this.fecha = fecha;
		this.valoracion = valoracion;
		this.actualizacion = actualizacion;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public String getValoracion() {
		return valoracion;
	}

	public String getActualizacion() {
		return actualizacion;
	}

}
