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
}
