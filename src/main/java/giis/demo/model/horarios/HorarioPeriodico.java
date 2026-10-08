package giis.demo.model.horarios;

public class HorarioPeriodico {
	
	private int id_horario;
	private int id_empleado;
	private int dia_semana; // 1 = Lunes, 7 = Domingo
	private String hora_inicio; // Formato "HH:mm"
	private String hora_fin; // Formato "HH:mm"
	
	/**
	 * Constructor de la clase HorarioPeriodico
	 */
	public HorarioPeriodico(int id_horario, int id_empleado, int dia_semana, String hora_inicio, String hora_fin) {
		this.id_horario = id_horario;
		this.id_empleado = id_empleado;
		this.dia_semana = dia_semana;
		this.hora_inicio = hora_inicio;
		this.hora_fin = hora_fin;
	}

	// Getters
	public int getId_horario() {
		return id_horario;
	}

	public int getId_empleado() {
		return id_empleado;
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
