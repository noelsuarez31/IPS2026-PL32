package giis.demo.model.entrevistas;

import java.time.LocalDate;
import java.time.LocalTime;

public class FranjaHoraria {
	private int idFranja; 
	private String dniJugador;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	
	public FranjaHoraria(String dniJugador, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
		this.dniJugador = dniJugador;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}
	
	public String getDniJugador() {
		return this.dniJugador;
	}
	
	public LocalDate getFecha() {
		return this.fecha;
	}
	
	public LocalTime getHoraInicio() {
		return this.horaInicio;
	}
	
	public LocalTime getHoraFin() {
		return this.horaFin;
	}
	
	public void setIdFranja(int idFranja) {
		this.idFranja = idFranja;
	}
	
	public int getIdFranja() {
		return this.idFranja;
	}
	
	@Override
	public String toString() {
		return String.format("Franja: Fecha=%s ; Hora de inicio=%s ; Hora de fin=%s", 
				getFecha(), getHoraInicio(), getHoraFin());
	}
}
