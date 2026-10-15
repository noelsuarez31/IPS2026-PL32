package giis.demo.model.horarios;


public class HorarioEspecifico {
	private int id_horario;
	private String dni_empleado;
	private String fecha; // YYYY-MM-DD
	private String hora_inicio; // Formato "HH:mm"
	private String hora_fin; // Formato "HH:mm"
	
	/**
	 * Constructor de la clase HorarioEspecifico
	 */
	public HorarioEspecifico(int id_horario, String dni_empleado, String fecha, String hora_inicio, String hora_fin) {
		this.id_horario = id_horario;
		this.dni_empleado = dni_empleado;
		this.fecha = fecha;
		this.hora_inicio = hora_inicio;
		this.hora_fin = hora_fin;
	}

	// Getters
	public int getId_horario() {
		return id_horario;
	}

	public String getDni_empleado() {
		return dni_empleado;
	}

	public String getFecha() {
		return fecha;
	}

	public String getHora_inicio() {
		return hora_inicio;
	}

	public String getHora_fin() {
		return hora_fin;
	}
}


