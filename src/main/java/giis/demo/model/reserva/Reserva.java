package giis.demo.model.reserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
	
	private final static int PRICE_PER_HOUR=50;
	
	private int reserva_id;
	private LocalDate fechaReserva;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private double importe;
	private LocalDate fechaRegistro;
	private int userId;
	private int instalacionId;
	
	/**
	 * Constructor de la clase por defecto
	 * @param reserva_id
	 * @param fechaReserva
	 * @param horaInicio
	 * @param horaFin
	 * @param importe
	 * @param fechaRegistro
	 * @param userId
	 * @param instalacionId
	 */
	public Reserva(int reserva_id, LocalDate fechaReserva, LocalTime horaInicio, LocalTime horaFin, double importe,
			LocalDate fechaRegistro, int userId, int instalacionId) {

		this.reserva_id = reserva_id;
		this.fechaReserva = fechaReserva;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.importe = importe;
		this.fechaRegistro = fechaRegistro;
		this.userId = userId;
		this.instalacionId = instalacionId;
		
		calcularPrecio();
	}
	
	/**
	 * Devuelve el id de la reserva
	 * @return id de la reserva
	 */
	public int getReserva_id() {
		return reserva_id;
	}
	
	/**
	 * Devuelve la fecha de la reserva
	 * @return fecha de la reserva
	 */
	public LocalDate getFechaReserva() {
		return fechaReserva;
	}
	
	/**
	 * Devuelve la hora de inicio de la reserva
	 * @return hora inicio reserva
	 */
	public LocalTime getHoraInicio() {
		return horaInicio;
	}
	
	/**
	 * Devuelve la hora final de la reserva
	 * @return hora final de la reserva
	 */
	public LocalTime getHoraFin() {
		return horaFin;
	}
	
	/**
	 * Devuelve el importe de la reserva
	 * @return importe total
	 */
	public double getImporte() {
		return importe;
	}
	
	/**
	 * Devuelve la fecha en la que se realizó la reserva
	 * @return fecha en la que se realizó la reserva
	 */
	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}
	
	/**
	 * Devuelve el id del usuario que realiza la reserva
	 * @return id del cliente
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Dwevuelve el id de la instalación que ha sido reservada
	 * @return id de la instalación
	 */
	public int getInstalacionId() {
		return instalacionId;
	}
	
	private void calcularPrecio() {
		
		int horas = horaFin.getHour()-horaInicio.getHour();
		importe = horas*PRICE_PER_HOUR;
		
	}

}
