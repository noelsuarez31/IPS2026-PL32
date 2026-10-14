package giis.demo.model.reserva;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Intervalo {
	
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private String motivo;
	
	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm");
	
	/**
	 * Constructor de la clase por defecto
	 * @param horaInicio
	 * @param horaFin
	 */
	
	public Intervalo(LocalTime horaInicio, LocalTime horaFin) {
		
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		motivo = "Disponible";
	}
	
	
	/**
	 * Constructor de la clase a la que se le puede asignar un motivo del por qué
	 * ese intervalo está ocupado
	 * @param horaInicio
	 * @param horaFin
	 * @param motivo
	 */
	public Intervalo(LocalTime horaInicio, LocalTime horaFin, String motivo) {
		
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.motivo = motivo;
	}


	/**
	 * Devuelve la hora de inicio
	 * @return hora de inicio
	 */
	public LocalTime getHoraInicio() {
		return horaInicio;
	}
	
	/**
	 * Devuelve la hora de finalización
	 * @return hora de finalización del intervalo 
	 */
	public LocalTime getHoraFin() {
		return horaFin;
	}

	/**
	 * Devuelve el motivo
	 * @return el motivo del intervalo de estar ocupado o libre
	 */
	public String getMotivo() {
		return motivo;
	}
	
	/**
	 * Redefinición del toString
	 */
	 @Override
	    public String toString() {
	        return horaInicio.format(FORMATO) + " - "
	                + horaFin.format(FORMATO) + "    " + motivo;
	    }
}
