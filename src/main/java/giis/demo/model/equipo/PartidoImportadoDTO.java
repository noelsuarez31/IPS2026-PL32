package giis.demo.model.equipo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PartidoImportadoDTO {

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

	private String equipoLocal;
	private String equipoVisitante;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private String instalacion;

	public PartidoImportadoDTO() {
	}

	public PartidoImportadoDTO(String equipoLocal, String equipoVisitante, LocalDate fecha, 
			LocalTime horaInicio, LocalTime horaFin, String instalacion) {
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.instalacion = instalacion;
	}

	public String getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(String equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public String getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(String equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public String getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(String instalacion) {
		this.instalacion = instalacion;
	}

	public String getFechaFormateada() {
		return fecha != null ? fecha.format(FORMATO_FECHA) : "";
	}

	public String getHoraInicioFormateada() {
		return horaInicio != null ? horaInicio.format(FORMATO_HORA) : "";
	}

	public String getHoraFinFormateada() {
		return horaFin != null ? horaFin.format(FORMATO_HORA) : "";
	}
}