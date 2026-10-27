package giis.demo.model.equipo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import giis.demo.exceptions.PersistenceException;
import giis.demo.exceptions.TrainingException;
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
	 * Devuelve la lista de filas listas para añadir directamente al DefaultTableModel de la JTable.
	 */
	public List<Object[]> obtenerFilasOcupacion(int idEquipo, int idInstalacion, LocalDate fecha) {
		if (idInstalacion <= 0 || fecha == null) {
			return List.of();
		}
		try {
			List<EventoOcupacion> eventos = jdbc.obtenerEventosOcupacion(idEquipo, idInstalacion, fecha);
			List<Object[]> filas = new ArrayList<>();
			for (EventoOcupacion e : eventos) {
				filas.add(e.toRow());
			}
			return filas;
		} catch (SQLException e) {
			throw new RuntimeException("Error al consultar la ocupación de la instalación: " + e.getMessage());
		}
	}
	
	/**
	 * Crea el entrenamiento y cancela las tareas o reservas externas coincidentes en la base de datos.
	 */
	public void crearEntrenamiento(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int idEquipo,
			int idInstalacion, List<EventoOcupacion> cancelables) throws TrainingException {
		validarDatosEntrenamiento(fecha, horaInicio, horaFin, idEquipo, idInstalacion);
		try {
			jdbc.comprobarColisionesEntrenamiento(fecha, horaInicio, horaFin, idEquipo, idInstalacion);
			jdbc.guardarEntrenamientoCancelandoCoincidentes(fecha, horaInicio, horaFin, idEquipo, idInstalacion, cancelables);
		} catch (PersistenceException | SQLException e) {
			throw new TrainingException(e.getMessage());
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
		if (horaInicio.isBefore(LocalTime.of(8, 0)) || horaFin.isAfter(LocalTime.of(22, 0))) {
			throw new IllegalArgumentException("El horario del club es de 08:00 a 22:00. Las horas deben estar dentro de este rango.");
		}
	}
	
	/**
	 * Devuelve los eventos sobreescribibles (reservas, jardinería) que coinciden con el horario propuesto.
	 * Lanza TrainingException si colisiona con un evento no sobreescribible (partidos, entrevistas, etc.).
	 */
	public List<EventoOcupacion> obtenerSobreescribiblesCoincidentes(LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin, int idEquipo, int idInstalacion) throws TrainingException {
		validarDatosEntrenamiento(fecha, horaInicio, horaFin, idEquipo, idInstalacion);
		try {
			jdbc.comprobarColisionesEntrenamiento(fecha, horaInicio, horaFin, idEquipo, idInstalacion);
			return jdbc.obtenerEventosSobreescribiblesSolapados(idEquipo, idInstalacion, fecha, horaInicio, horaFin);
		} catch (PersistenceException | SQLException e) {
			throw new TrainingException(e.getMessage());
		}
	}
}