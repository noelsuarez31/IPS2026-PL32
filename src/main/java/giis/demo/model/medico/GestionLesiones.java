package giis.demo.model.medico;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.equipo.Equipo;
import giis.demo.util.ApplicationException;

public class GestionLesiones {

	private JDBC jdbc;
	public static final String[] VALORACIONES = { "Leve", "Moderada", "Grave" };

	public GestionLesiones() {
		jdbc = new JDBC();
	}

	/**
	 * Obbtiene los jugadores
	 * @return
	 */
	public EmpleadoDeportivo[] getJugadores() {
		try {
			return jdbc.getJugadoresConEquipo().toArray(new EmpleadoDeportivo[0]);
		} catch (SQLException e) {
			throw new ApplicationException("Error al obtener los jugadores: " + e.getMessage());
		}
	}

	/**
	 * Obtiene las lesiones de un jugador
	 * @param jugador
	 * @return lesiones de jugador
	 */
	public List<Lesion> getLesiones(EmpleadoDeportivo jugador) {

		if (jugador == null) {
			throw new ApplicationException("El jugador no puede ser nulo");
		}

		try {

			return jdbc.getLesionesJugador(jugador.getDni());

		} catch (SQLException e) {

			throw new ApplicationException("Error al obtener las lesiones");

		}
	}

	/**
	 * Obtiene las evoluciones de una lesión
	 * @param lesion
	 * @return lista con las evoluciones
	 */
	public List<EvolucionLesion> getEvolucion(Lesion lesion) {

		if (lesion == null) {
			throw new ApplicationException("La lesión no puede ser nula");
		}

		try {
			return jdbc.getEvolucionLesion(lesion.getId());
		} catch (SQLException e) {

			throw new ApplicationException("Error al obtener la evolución de la lesión");

		}

	}

	/**
	 * Obtiene todos los equipos
	 * @return equipos locales
	 */
	public Equipo[] getEquipos() {

		try {
			return jdbc.getEquiposLocales();
		} catch (SQLException e) {

			throw new ApplicationException("Error al obtener los equipos");
		}
	}
	
	/**
	 * Partidos o entrenamientos del equipo del jugador en esa fecha, ya formateados
	 */
	public List<String> getEventosEquipo(EmpleadoDeportivo jugador, OrigenLesion origen, LocalDate fecha) {

		if (jugador == null) {
			throw new ApplicationException("Debe seleccionar un jugador");
		}
		if (fecha == null) {
			throw new ApplicationException("Debe seleccionar una fecha");
		}
		if (origen == null || !origen.requiereEvento()) {
			throw new ApplicationException("Este origen no tiene eventos asociados");
		}

		try {
			int idEquipo = jdbc.getIdEquipoJugador(jugador.getDni());
			if (idEquipo <= 0) {
				throw new ApplicationException("El jugador no pertenece a ningún equipo");
			}
			return origen == OrigenLesion.PARTIDO ? jdbc.getPartidosEquipoDia(idEquipo, fecha)
					: jdbc.getEntrenamientosEquipoDia(idEquipo, fecha);
		} catch (SQLException e) {
			throw new ApplicationException("Error al obtener los eventos del equipo");
		}
	}

	/**
	 * Registra una nueva lesión
	 * 
	 * @param jugador
	 * @param origen
	 * @param evento
	 * @param fecha
	 * @param tipo
	 * @param valoracion
	 * @param descripcion
	 */
	public void registrarLesion(EmpleadoDeportivo jugador, OrigenLesion origen, String evento, LocalDate fecha,
			String tipo, String valoracion, String descripcion) {

		if (jugador == null) {
			throw new ApplicationException("Debe seleccionar un jugador");
		}
		if (fecha == null) {
			throw new ApplicationException("Debe indicar la fecha de la lesión");
		}
		if (fecha.isAfter(LocalDate.now())) {
			throw new ApplicationException("La fecha de la lesión no puede ser futura");
		}
		if (origen == null) {
			throw new ApplicationException("Debe indicar el origen de la lesión");
		}
		if (origen.requiereEvento() && (evento == null || evento.isBlank())) {
			throw new ApplicationException(
					"Debe seleccionar el " + origen.getTexto().toLowerCase() + " donde se originó la lesión");
		}
		if (tipo == null || tipo.isBlank()) {
			throw new ApplicationException("Debe indicar la lesión");
		}
		if (!esValoracionValida(valoracion)) {
			throw new ApplicationException("Debe seleccionar una valoración");
		}
		if (descripcion == null || descripcion.isBlank()) {
			throw new ApplicationException("Debe introducir la descripción inicial");
		}

		String tipoLimpio = tipo.trim();
		String textoOrigen = origen.requiereEvento() ? origen.getTexto() + ": " + evento : origen.getTexto();

		try {
			for (Lesion l : jdbc.getLesionesJugador(jugador.getDni())) {
				if (l.getFecha().equals(fecha) && l.getTipo().equalsIgnoreCase(tipoLimpio)) {
					throw new ApplicationException("Ya hay registrada una lesión igual para ese jugador en esa fecha");
				}
			}
			jdbc.registrarLesion(jugador.getDni(), fecha, tipoLimpio, textoOrigen, valoracion, descripcion.trim());
		} catch (SQLException e) {
			throw new ApplicationException("Error al registrar la lesión: " + e.getMessage());
		}
	}

	/**
	 * Registra una nueva evolucion
	 * 
	 * @param lesion
	 * @param fecha
	 * @param valoracion
	 * @param actualizacion
	 * @param nuevoEstado
	 * @param fechaAlta
	 */
	public void registrarEvolucion(Lesion lesion, LocalDate fecha, String valoracion, String actualizacion,
			String nuevoEstado, LocalDate fechaAlta) {

		if (lesion == null) {
			throw new ApplicationException("Debe seleccionar una lesión");
		}
		if (lesion.estaRecuperada()) {
			throw new ApplicationException("Esta lesión ya tiene el alta médica");
		}
		if (fecha == null) {
			throw new ApplicationException("Debe indicar la fecha de la evolución");
		}
		if (fecha.isAfter(LocalDate.now())) {
			throw new ApplicationException("La fecha de la evolución no puede ser futura");
		}
		if (fecha.isBefore(lesion.getFecha())) {
			throw new ApplicationException("La evolución no puede ser anterior a la fecha de la lesión");
		}
		if (!esValoracionValida(valoracion)) {
			throw new ApplicationException("Debe seleccionar una valoración");
		}
		if (actualizacion == null || actualizacion.isBlank()) {
			throw new ApplicationException("Debe introducir la actualización");
		}

		boolean alta = Lesion.RECUPERADO.equals(nuevoEstado);
		
		if (!alta && !esEstadoActivo(nuevoEstado)) {
			throw new ApplicationException("Debe seleccionar un estado válido");
		}
		if (alta) {
			
			if (fechaAlta == null) {
				throw new ApplicationException("Debe indicar la fecha del alta");
			}
			if (fechaAlta.isAfter(LocalDate.now())) {
				throw new ApplicationException("La fecha del alta no puede ser futura");
			}
			
			if (fechaAlta.isBefore(lesion.getFecha())) {
		        throw new ApplicationException("La fecha del alta no puede ser anterior a la fecha de la lesión");
		    }
		}

		try {
			if (alta) {
				LocalDate ultima = fecha;
				for (EvolucionLesion ev : jdbc.getEvolucionLesion(lesion.getId())) {
					if (ev.getFecha().isAfter(ultima)) {
						ultima = ev.getFecha();
					}
				}
				if (fechaAlta.isBefore(ultima)) {
					throw new ApplicationException("La fecha del alta no puede ser anterior a la última evolución");
				}
			}
			jdbc.registrarEvolucion(lesion, new EvolucionLesion(fecha, valoracion, actualizacion.trim()),
					nuevoEstado, alta ? fechaAlta : null);
		} catch (SQLException e) {
			throw new ApplicationException("Error al registrar la evolución: " + e.getMessage());
		}
	}

	/**
	 * Comprueba si una valoración es válida
	 * @param v
	 * @return true si es valida, false en caso contrario
	 */
	private boolean esValoracionValida(String valoracion) {
		for (String s : VALORACIONES) {
			if (s.equals(valoracion)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Comprueba si una lesión esta activa
	 * @param e
	 * @return true si esta activa, false en caso contrario
	 */
	private boolean esEstadoActivo(String estado) {
		
		for (String s : Lesion.ESTADOS_ACTIVOS) {
			
			if (s.equals(estado)) {
				return true;
			}
		}
		return false;
	}

}
