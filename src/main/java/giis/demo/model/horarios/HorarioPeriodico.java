package giis.demo.model.horarios;

public class HorarioPeriodico {
	
	private int id_horario;
	private String dni_empleado;
	private int dia_semana; // 1 = Lunes, 7 = Domingo
	private String hora_inicio; // Formato "HH:mm"
	private String hora_fin; // Formato "HH:mm"
	
	/**
	 * Constructor de la clase HorarioPeriodico
	 */
	public HorarioPeriodico(int id_horario, String dni_empleado, int dia_semana, String hora_inicio, String hora_fin) {
		this.id_horario = id_horario;
		this.dni_empleado = dni_empleado;
		this.dia_semana = dia_semana;
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

	public int getDia_semana() {
		return dia_semana;
	}

	public String getHora_inicio() {
		return hora_inicio;
	}

	public String getHora_fin() {
		return hora_fin;
	}
}
