package giis.demo.model.equipo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.reserva.Instalacion;

public class CrearEntrenamientoService {
	private JDBC jdbc = new JDBC();

	/**
	 * Devuelve una lista de dnis de los entrenadores que entrenan a algun equipo
	 * @return
	 */
	public List<String> obtenerDnisEntrenadores() {
		try {
			return jdbc.obtenerDnisEntrenadoresConEquipo();
		} catch (SQLException e) {
			throw new RuntimeException("Error al cargar los entrenadores: " + e.getMessage());
		}
	}

	/**
	 * Devuelve el nombre del equipo segun el dni de su entrenador
	 * @param dniEntrenador
	 * @return
	 */
	public String obtenerNombreEquipoDeEntrenador(String dniEntrenador) {
		if (dniEntrenador == null || dniEntrenador.isBlank()) {
			return "Sin equipo";
		}
		try {
			return jdbc.obtenerNombreEquipoPorDniEntrenador(dniEntrenador);
		} catch (SQLException e) {
			throw new RuntimeException("Error al obtener el equipo: " + e.getMessage());
		}
	}

	/**
	 * Devuelve el id del equipo segun el nif del entrenador
	 * @param dniEntrenador
	 * @return
	 */
	public int obtenerIdEquipoDeEntrenador(String dniEntrenador) {
		if (dniEntrenador == null || dniEntrenador.isBlank()) {
			return 0;
		}
		try {
			return jdbc.obtenerIdEquipoPorDniEntrenador(dniEntrenador);
		} catch (SQLException e) {
			throw new RuntimeException("Error al obtener el id del equipo: " + e.getMessage());
		}
	}

	/**
	 * Devuelve una lista de instalaciones disponibles
	 * @return
	 */
	public List<Instalacion> obtenerInstalacionesDisponibles() {
		try {
			return jdbc.getAvailableInstalations();
		} catch (SQLException e) {
			throw new RuntimeException("Error al cargar las instalaciones: " + e.getMessage());
		}
	}

	/**
	 * Devuelve una lista con las entrevistas que tienen asignadas los
	 * jugadores del equipo
	 * @param idEquipo
	 * @param fecha
	 * @return
	 */
	public List<String> obtenerEntrevistasJugadores(int idEquipo, LocalDate fecha) {
		if (idEquipo <= 0 || fecha == null) {
			return List.of();
		}
		try {
			return jdbc.obtenerEntrevistasJugadoresEquipo(idEquipo, fecha);
		} catch (SQLException e) {
			throw new RuntimeException("Error al consultar las entrevistas del equipo: " + e.getMessage());
		}
	}

	/**
	 * Crea el entrenamiento y lo guarda en la BBDD
	 * @param fecha
	 * @param horaInicio
	 * @param horaFin
	 * @param idEquipo
	 * @param idInstalacion
	 */
	public void crearEntrenamiento(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int idEquipo, int idInstalacion) {
		validarDatosEntrenamiento(fecha, horaInicio, horaFin, idEquipo, idInstalacion);
		try {
			jdbc.guardarEntrenamiento(fecha, horaInicio, horaFin, idEquipo, idInstalacion);
		} catch (SQLException e) {
			throw new RuntimeException("Error al almacenar el entrenamiento: " + e.getMessage());
		}
	}

	private void validarDatosEntrenamiento(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int idEquipo, int idInstalacion) {
		if (idEquipo <= 0) {
			throw new IllegalArgumentException("El entrenador seleccionado debe tener un equipo asignado.");
		}
		if (idInstalacion <= 0) {
			throw new IllegalArgumentException("Debe seleccionar una instalación válida.");
		}
		if (fecha == null) {
			throw new IllegalArgumentException("Debe seleccionar una fecha en el calendario.");
		}
		if (fecha.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("La fecha del entrenamiento no puede ser anterior a hoy.");
		}
		if (horaInicio == null || horaFin == null) {
			throw new IllegalArgumentException("Las horas de inicio y fin no pueden estar vacías.");
		}
		if (!horaFin.isAfter(horaInicio)) {
			throw new IllegalArgumentException("La hora de fin debe ser posterior a la de inicio.");
		}
	}
}