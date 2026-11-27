package giis.demo.model.medico;

import java.time.LocalDate;

public class Lesion {

	public static final String ESTADO_INICIAL = "En tratamiento";
	public static final String RECUPERADO = "Recuperado";
	public static final String[] ESTADOS_ACTIVOS = { "En tratamiento", "En rehabilitación", "Pendiente de pruebas" };
	
	private int id;
	private String dniJugador;
	private LocalDate fecha;
	private String tipo;
	private String origen;
	private String estado;
	private LocalDate fechaAlta;
	/**
	 * Constructor por defecto de la clase
	 * 
	 * @param id
	 * @param dniJugador
	 * @param fecha
	 * @param tipo
	 * @param origen
	 * @param estado
	 */
	public Lesion(int id, String dniJugador, LocalDate fecha, String tipo, String origen, String estado, LocalDate fechaAlta) {
		this.id = id;
		this.dniJugador = dniJugador;
		this.fecha = fecha;
		this.tipo = tipo;
		this.origen = origen;
		this.estado = estado;
		this.fechaAlta = fechaAlta;
	}
	
	/**
	 * Constructor si la fecha de alta
	 * 
	 * @param id
	 * @param dniJugador
	 * @param fecha
	 * @param tipo
	 * @param origen
	 * @param estado
	 */
	public Lesion(int id, String dniJugador, LocalDate fecha, String tipo, String origen, String estado) {
		this(id, dniJugador, fecha, tipo, origen, estado, null);
	}
	
	/**
	 * Devuelve el id
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Dni del jugador
	 * @return dni del jugador
	 */
	public String getDniJugador() {
		return dniJugador;
	}

	/**
	 * Devuelve la fecha
	 * @return fecha
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * Devuelve el tipo de la lesión
	 * @return tipo de la lesión
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Devuelve el origen de la lesión
	 * @return origen de la lesión
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * Devuelve el estado de la lesión
	 * @return estado de la lesión
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * Devuelve la fecha del alta
	 * @return fecha del alta
	 */
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	
	/**
	 * Comprueba si la lesión ya esta recuperada
	 * @return
	 */
	public boolean estaRecuperada() {
		return RECUPERADO.equalsIgnoreCase(this.estado);
	}

	/**
	 * Redefinicion del toString
	 */
	@Override
	public String toString() {
		return String.format("%s - %s - %s - %s",
				fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), tipo, origen, estado);
	}

}
