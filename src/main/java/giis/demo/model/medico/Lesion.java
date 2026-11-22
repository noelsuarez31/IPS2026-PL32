package giis.demo.model.medico;

import java.time.LocalDate;

public class Lesion {

	private int id;
	private String dniJugador;
	private LocalDate fecha;
	private String tipo;
	private String origen;
	private String estado;

	public Lesion(int id, String dniJugador, LocalDate fecha, String tipo, String origen, String estado) {
		this.id = id;
		this.dniJugador = dniJugador;
		this.fecha = fecha;
		this.tipo = tipo;
		this.origen = origen;
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public String getDniJugador() {
		return dniJugador;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public String getOrigen() {
		return origen;
	}

	public String getEstado() {
		return estado;
	}

	@Override
	public String toString() {
		return String.format("%s - %s - %s - %s",
				fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), tipo, origen, estado);
	}

}
