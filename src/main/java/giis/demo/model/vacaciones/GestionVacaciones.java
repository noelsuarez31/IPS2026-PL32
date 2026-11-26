package giis.demo.model.vacaciones;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.horarios.HorarioEspecifico;
import giis.demo.model.horarios.HorarioPeriodico;
import giis.demo.model.mantenimiento.TareaMantenimiento;
import giis.demo.util.ApplicationException;

public class GestionVacaciones {

	public static final int MAX_DIAS_VACACIONES = 22;

	private JDBC jdbc = new JDBC();

	/**
	 * Devuelve la lista de empleados no deportivos registrados en la BBDD.
	 */
	public List<EmpleadoNoDeportivo> obtenerEmpleadosNoDeportivos() throws SQLException {
		return jdbc.getEmpleadosNoDeportivos();
	}

	/**
	 * Devuelve las vacaciones ya confirmadas de un empleado.
	 */
	public List<VacacionEmpleado> obtenerVacacionesConfirmadas(String dniEmpleado) throws SQLException {
		if (dniEmpleado == null || dniEmpleado.isBlank()) {
			return new ArrayList<>();
		}
		return jdbc.getVacacionesEmpleado(dniEmpleado);
	}

	/**
	 * Cuenta cuántos días de vacaciones de la lista dada pertenecen al año indicado.
	 */
	public int contarVacacionesEnAnio(List<VacacionEmpleado> lista, int anio) {
		int contador = 0;
		if (lista != null) {
			for (VacacionEmpleado v : lista) {
				if (v.getFecha() != null && !v.getFecha().isBlank()) {
					if (LocalDate.parse(v.getFecha()).getYear() == anio) {
						contador++;
					}
				}
			}
		}
		return contador;
	}

	/**
	 * Devuelve los días de vacaciones disponibles de un empleado para un año concreto.
	 */
	public int obtenerDiasDisponiblesEnAnio(String dniEmpleado, int anio) throws SQLException {
		List<VacacionEmpleado> confirmadas = obtenerVacacionesConfirmadas(dniEmpleado);
		return Math.max(0, MAX_DIAS_VACACIONES - contarVacacionesEnAnio(confirmadas, anio));
	}

	/**
	 * Devuelve los elementos (turnos y tareas) ya invalidados por vacaciones confirmadas del empleado.
	 */
	public List<ElementoInvalidadoVacacion> obtenerElementosInvalidadosConfirmados(String dniEmpleado)
			throws SQLException {
		if (dniEmpleado == null || dniEmpleado.isBlank()) {
			return new ArrayList<>();
		}
		return jdbc.getElementosInvalidadosVacacion(dniEmpleado);
	}

	public List<HorarioPeriodico> obtenerHorariosPeriodicos(String dniEmpleado) throws SQLException {
		return jdbc.getHorariosPeriodicosEmpleado(dniEmpleado);
	}

	public List<HorarioEspecifico> obtenerHorariosEspecificos(String dniEmpleado) throws SQLException {
		return jdbc.getHorariosEspecificosEmpleado(dniEmpleado);
	}

	public List<TareaMantenimiento> obtenerTareasEmpleado(String dniEmpleado) throws SQLException {
		return jdbc.getTareasEmpleado(dniEmpleado);
	}

	/**
	 * Comprueba en memoria si en una fecha concreta el empleado tiene asignado algún turno
	 * (aplicando la regla híbrida específico/periódico) o alguna tarea de mantenimiento.
	 */
	public boolean tieneTurnosOTareasEnFecha(LocalDate fecha, List<HorarioPeriodico> periodicos,
			List<HorarioEspecifico> especificos, List<TareaMantenimiento> tareas) {
		if (fecha == null) {
			return false;
		}
		String fechaStr = fecha.toString();

		if (especificos != null) {
			for (HorarioEspecifico he : especificos) {
				if (fechaStr.equals(he.getFecha())) {
					return true;
				}
			}
		}

		int diaSemana = fecha.getDayOfWeek().getValue();
		if (periodicos != null) {
			for (HorarioPeriodico hp : periodicos) {
				if (hp.getDia_semana() == diaSemana) {
					return true;
				}
			}
		}

		if (tareas != null) {
			for (TareaMantenimiento t : tareas) {
				if (fechaStr.equals(t.getFecha())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Obtiene los turnos y tareas que quedarían invalidados para un empleado en una fecha concreta.
	 */
	public List<ElementoInvalidadoVacacion> obtenerElementosAInvalidarEnFecha(String dniEmpleado, String fechaStr)
			throws SQLException {
		List<ElementoInvalidadoVacacion> resultado = new ArrayList<>();
		if (dniEmpleado == null || dniEmpleado.isBlank() || fechaStr == null || fechaStr.isBlank()) {
			return resultado;
		}

		if (jdbc.tieneVacacionEnFecha(dniEmpleado, fechaStr)) {
			return resultado;
		}

		LocalDate fecha = LocalDate.parse(fechaStr);
		int numDiaSemana = fecha.getDayOfWeek().getValue();

		List<HorarioEspecifico> especificos = jdbc.getHorariosEspecificosFecha(dniEmpleado, fechaStr);
		if (!especificos.isEmpty()) {
			for (HorarioEspecifico he : especificos) {
				resultado.add(new ElementoInvalidadoVacacion(
						0,
						dniEmpleado,
						fechaStr,
						"Turno",
						he.getHora_inicio() + " - " + he.getHora_fin()));
			}
		} else {
			List<HorarioPeriodico> periodicos = jdbc.getHorariosPeriodicosEmpleado(dniEmpleado);
			for (HorarioPeriodico hp : periodicos) {
				if (hp.getDia_semana() == numDiaSemana) {
					resultado.add(new ElementoInvalidadoVacacion(
							0,
							dniEmpleado,
							fechaStr,
							"Turno",
							hp.getHora_inicio() + " - " + hp.getHora_fin()));
				}
			}
		}

		List<TareaMantenimiento> tareas = jdbc.getTareasEmpleadoEnFecha(dniEmpleado, fechaStr);
		for (TareaMantenimiento t : tareas) {
			String desc = (t.getDescripcion() != null && !t.getDescripcion().isBlank())
					? t.getDescripcion()
					: "Mantenimiento";
			resultado.add(new ElementoInvalidadoVacacion(
					0,
					dniEmpleado,
					fechaStr,
					"Tarea",
					t.getHoraInicio() + " - " + t.getHoraFin() + " (" + desc + ")"));
		}

		return resultado;
	}

	/**
	 * Devuelve la lista combinada de turnos/tareas ya invalidados en BBDD más los que se
	 * invalidarían con los días actualmente en selección.
	 */
	public List<ElementoInvalidadoVacacion> obtenerVistaPreviaInvalidados(String dniEmpleado,
			List<VacacionEmpleado> diasSeleccionados) throws SQLException {
		List<ElementoInvalidadoVacacion> total = new ArrayList<>(obtenerElementosInvalidadosConfirmados(dniEmpleado));
		if (diasSeleccionados != null) {
			for (VacacionEmpleado v : diasSeleccionados) {
				total.addAll(obtenerElementosAInvalidarEnFecha(dniEmpleado, v.getFecha()));
			}
		}
		Collections.sort(total, new Comparator<ElementoInvalidadoVacacion>() {
			@Override
			public int compare(ElementoInvalidadoVacacion e1, ElementoInvalidadoVacacion e2) {
				int cmp = e1.getFecha().compareTo(e2.getFecha());
				if (cmp != 0) {
					return cmp;
				}
				return e1.getTipo().compareTo(e2.getTipo());
			}
		});
		return total;
	}

	/**
	 * Calcula y valida los días laborables (con turnos o tareas asignados) a añadir entre
	 * fechaInicioStr y fechaFinStr (ambos incluidos y pertenecientes al mismo año).
	 * Si entre inicio y fin hay días en los que el empleado no trabajaba, no cuentan.
	 */
	public List<VacacionEmpleado> calcularDiasSeleccionablesEnRango(String dniEmpleado, String fechaInicioStr,
			String fechaFinStr, List<VacacionEmpleado> seleccionActual) throws SQLException {
		if (dniEmpleado == null || dniEmpleado.isBlank()) {
			throw new ApplicationException("Debe seleccionar un empleado no deportivo.");
		}
		if (fechaInicioStr == null || fechaInicioStr.isBlank() || "Ninguno".equals(fechaInicioStr)
				|| fechaFinStr == null || fechaFinStr.isBlank() || "Ninguno".equals(fechaFinStr)) {
			throw new ApplicationException("Debe seleccionar tanto el día de inicio como el día de fin.");
		}

		LocalDate inicio = LocalDate.parse(fechaInicioStr);
		LocalDate fin = LocalDate.parse(fechaFinStr);

		if (inicio.isBefore(LocalDate.now())) {
			throw new ApplicationException("No se pueden seleccionar días de vacaciones anteriores al día actual.");
		}
		if (inicio.isAfter(fin)) {
			throw new ApplicationException(
					"La fecha de inicio (" + fechaInicioStr + ") no puede ser posterior a la fecha de fin (" + fechaFinStr + ").");
		}
		if (inicio.getYear() != fin.getYear()) {
			throw new ApplicationException(
					"La fecha de inicio y la fecha de fin deben pertenecer al mismo año (" + inicio.getYear() + ").");
		}

		List<VacacionEmpleado> confirmadas = obtenerVacacionesConfirmadas(dniEmpleado);
		Set<String> fechasYaConfirmadas = new HashSet<>();
		for (VacacionEmpleado v : confirmadas) {
			fechasYaConfirmadas.add(v.getFecha());
		}

		Set<String> fechasEnSeleccion = new HashSet<>();
		if (seleccionActual != null) {
			for (VacacionEmpleado v : seleccionActual) {
				fechasEnSeleccion.add(v.getFecha());
			}
		}

		if (inicio.equals(fin)) {
			if (fechasYaConfirmadas.contains(fechaInicioStr)) {
				throw new ApplicationException("El día " + fechaInicioStr + " ya está confirmado en sus vacaciones.");
			}
			if (fechasEnSeleccion.contains(fechaInicioStr)) {
				throw new ApplicationException("El día " + fechaInicioStr + " ya se encuentra en 'Días seleccionados'.");
			}
		}

		List<HorarioPeriodico> periodicos = obtenerHorariosPeriodicos(dniEmpleado);
		List<HorarioEspecifico> especificos = obtenerHorariosEspecificos(dniEmpleado);
		List<TareaMantenimiento> tareas = obtenerTareasEmpleado(dniEmpleado);

		List<VacacionEmpleado> nuevosDias = new ArrayList<>();
		LocalDate cursor = inicio;
		while (!cursor.isAfter(fin)) {
			String fStr = cursor.toString();
			if (!fechasYaConfirmadas.contains(fStr) && !fechasEnSeleccion.contains(fStr)) {
				if (tieneTurnosOTareasEnFecha(cursor, periodicos, especificos, tareas)) {
					nuevosDias.add(new VacacionEmpleado(
							0,
							dniEmpleado,
							fStr,
							obtenerNombreDia(cursor.getDayOfWeek().getValue()),
							"En selección"));
				}
			}
			cursor = cursor.plusDays(1);
		}

		if (nuevosDias.isEmpty()) {
			throw new ApplicationException(
					"En el rango seleccionado no hay días disponibles en los que tenga turnos o tareas asignados.");
		}

		int anio = inicio.getYear();
		int confirmadasAnio = contarVacacionesEnAnio(confirmadas, anio);
		int seleccionadasAnio = contarVacacionesEnAnio(seleccionActual, anio);
		int disponiblesRestantes = MAX_DIAS_VACACIONES - confirmadasAnio - seleccionadasAnio;
		if (nuevosDias.size() > disponiblesRestantes) {
			throw new ApplicationException("No se pueden añadir " + nuevosDias.size()
					+ " día(s) en " + anio + ": supera el máximo anual de " + MAX_DIAS_VACACIONES
					+ " días laborables (disponibles restantes en " + anio + ": "
					+ Math.max(0, disponiblesRestantes) + ").");
		}

		return nuevosDias;
	}

	/**
	 * Confirma los días de vacaciones seleccionados, invalida los turnos y tareas correspondientes
	 * y registra una alerta para el gerente cuando se producen invalidaciones (salvo si el propio
	 * empleado es el gerente).
	 */
	public void confirmarVacaciones(EmpleadoNoDeportivo empleado, List<VacacionEmpleado> diasSeleccionados)
			throws SQLException {
		if (empleado == null || empleado.getDni() == null || empleado.getDni().isBlank()) {
			throw new ApplicationException("Debe seleccionar un empleado no deportivo.");
		}
		if (diasSeleccionados == null || diasSeleccionados.isEmpty()) {
			throw new ApplicationException("Debe añadir al menos un día a 'Días seleccionados' antes de confirmar.");
		}

		String dni = empleado.getDni();
		List<VacacionEmpleado> confirmadas = obtenerVacacionesConfirmadas(dni);

		Map<Integer, Integer> diasPorAnio = new HashMap<>();
		for (VacacionEmpleado v : diasSeleccionados) {
			int anio = LocalDate.parse(v.getFecha()).getYear();
			diasPorAnio.put(anio, diasPorAnio.getOrDefault(anio, 0) + 1);
		}
		for (Map.Entry<Integer, Integer> entry : diasPorAnio.entrySet()) {
			int anio = entry.getKey();
			int cantidadAnio = entry.getValue();
			int disponiblesAnio = MAX_DIAS_VACACIONES - contarVacacionesEnAnio(confirmadas, anio);
			if (cantidadAnio > disponiblesAnio) {
				throw new ApplicationException("El número de días seleccionados en " + anio + " (" + cantidadAnio
						+ ") supera los días disponibles para ese año (" + Math.max(0, disponiblesAnio) + ").");
			}
		}

		List<ElementoInvalidadoVacacion> invalidadosNuevos = new ArrayList<>();
		for (VacacionEmpleado v : diasSeleccionados) {
			invalidadosNuevos.addAll(obtenerElementosAInvalidarEnFecha(dni, v.getFecha()));
		}

		String nombreCompleto = empleado.getNombre() + " " + empleado.getApellido();
		AlertaGerente alerta = null;
		if (!invalidadosNuevos.isEmpty() && empleado.getPosicion() != Posicion.GERENTE) {
			StringBuilder sb = new StringBuilder();
			sb.append("El empleado ").append(nombreCompleto).append(" (").append(dni)
					.append(") ha confirmado ").append(diasSeleccionados.size())
					.append(" día(s) de vacaciones, quedando invalidados los siguientes turnos/tareas:\n");
			for (ElementoInvalidadoVacacion inv : invalidadosNuevos) {
				sb.append(" • ").append(inv.getFecha()).append(" | ").append(inv.getTipo())
						.append(": ").append(inv.getDetalle()).append("\n");
			}
			String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			alerta = new AlertaGerente(0, dni, nombreCompleto, ahora, sb.toString().trim(), false);
		}

		jdbc.confirmarVacacionesEmpleado(dni, diasSeleccionados, invalidadosNuevos, alerta);
	}

	/**
	 * Devuelve las alertas pendientes para mostrar al gerente al autenticarse.
	 */
	public List<AlertaGerente> obtenerAlertasPendientesGerente() throws SQLException {
		return jdbc.getAlertasGerentePendientes();
	}

	/**
	 * Marca las alertas pendientes del gerente como leídas tras mostrárselas.
	 */
	public void marcarAlertasGerenteComoLeidas() throws SQLException {
		jdbc.marcarAlertasGerenteComoLeidas();
	}

	public String obtenerNombreDia(int diaSemana) {
		String[] dias = { "", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" };
		if (diaSemana >= 1 && diaSemana <= 7) {
			return dias[diaSemana];
		}
		return "Desconocido";
	}
}
