package giis.demo.model.reserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrenamiento {
	
	private int id_entrenamiento;
	private LocalDate fecha;
	private LocalTime hora_inicio;
	private LocalTime hora_fin;
	private int id_equipo;
	private int id_instalacion;
	
	/**
	 * Constructor de la clase por defecto
	 * @param id_entrenamiento
	 * @param fecha
	 * @param hora_inicio
	 * @param hora_fin
	 * @param id_equipo
	 * @param id_instalacion
	 */
	public Entrenamiento(int id_entrenamiento, LocalDate fecha, LocalTime hora_inicio, LocalTime hora_fin,
			int id_equipo, int id_instalacion) {
		
		this.id_entrenamiento = id_entrenamiento;
		this.fecha = fecha;
		this.hora_inicio = hora_inicio;
		this.hora_fin = hora_fin;
		this.id_equipo = id_equipo;
		this.id_instalacion = id_instalacion;
	}
	
	/**
	 * Devuelve el id del entrenamiento
	 * @return id entrenamiento
	 */
	public int getId_entrenamiento() {
		return id_entrenamiento;
	}
	
	/**
	 * Devuelve la fecha de entrenamiento
	 * @return fecha entrenamiento
	 */
	public LocalDate getFecha() {
		return fecha;
	}
	
	/**
	 * Devuelve hora de inicio
	 * @return hora inicio entrenamiento
	 */
	public LocalTime getHora_inicio() {
		return hora_inicio;
	}
	
	/**
	 * Devuelve la hora de finalización
	 * @return hora de finalizacion
	 */
	public LocalTime getHora_fin() {
		return hora_fin;
	}
	
	/**
	 * Devuelve el id del equipo
	 * @return id del equipo
	 */
	public int getId_equipo() {
		return id_equipo;
	}
	
	/**
	 * Devuelve el id de la instalacion
	 * @return id de la instalacion
	 */
	public int getId_instalacion() {
		return id_instalacion;
	}
	
	

}
