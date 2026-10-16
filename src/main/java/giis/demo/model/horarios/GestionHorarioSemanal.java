package giis.demo.model.horarios;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.util.ApplicationException;

public class GestionHorarioSemanal {

	private JDBC jdbc = new JDBC();

	/**
	 * Devuelve la lista de empleados no deportivos registrados en la BBDD.
	 */
	public List<EmpleadoNoDeportivo> obtenerEmpleadosNoDeportivos() throws SQLException {
		return jdbc.getEmpleadosNoDeportivos();
	}

	/**
	 * Obtiene el lunes de la semana correspondiente a la fecha pasada.
	 */
	public LocalDate obtenerLunesDeSemana(String fechaStr) {
		if (fechaStr == null || fechaStr.isBlank() || fechaStr.equals("Ninguno")) {
			throw new ApplicationException("Debe seleccionar una fecha válida del calendario.");
		}
		LocalDate fecha = LocalDate.parse(fechaStr);
		int diaSemanaNum = fecha.getDayOfWeek().getValue(); // 1 = Lunes, 7 = Domingo
		return fecha.minusDays(diaSemanaNum - 1);
	}

	/**
	 * Obtiene el domingo de la semana correspondiente a la fecha pasada.
	 */
	public LocalDate obtenerDomingoDeSemana(String fechaStr) {
		if (fechaStr == null || fechaStr.isBlank() || fechaStr.equals("Ninguno")) {
			throw new ApplicationException("Debe seleccionar una fecha válida del calendario.");
		}
		LocalDate fecha = LocalDate.parse(fechaStr);
		int diaSemanaNum = fecha.getDayOfWeek().getValue();
		return fecha.plusDays(7 - diaSemanaNum);
	}

	/**
	 * Obtiene la lista ordenada de turnos de un empleado para la semana que contiene a fechaReferenciaStr.
	 * Aplica la regla híbrida: los específicos sustituyen íntegramente a los periódicos de ese día concreto.
	 */
	public List<HorarioSemanal> obtenerHorariosDeSemana(String dniEmpleado, String fechaReferenciaStr)
			throws SQLException {
		if (dniEmpleado == null || dniEmpleado.isBlank()) {
			throw new ApplicationException("Debe seleccionar un empleado para consultar sus horarios.");
		}

		LocalDate lunesSemana = obtenerLunesDeSemana(fechaReferenciaStr);
		LocalDate domingoSemana = obtenerDomingoDeSemana(fechaReferenciaStr);

		List<HorarioPeriodico> periodicos = jdbc.getHorariosPeriodicosEmpleado(dniEmpleado);
		List<HorarioEspecifico> especificosSemana = jdbc.getHorariosEspecificosRango(
				dniEmpleado, lunesSemana.toString(), domingoSemana.toString());

		List<HorarioSemanal> turnosResultado = new ArrayList<>();

		for (int i = 0; i < 7; i++) {
			LocalDate diaIterado = lunesSemana.plusDays(i);
			int numDiaSemana = i + 1;

			// Filtramos específicos de este día concreto
			List<HorarioEspecifico> especificosDelDia = new ArrayList<>();
			for (HorarioEspecifico he : especificosSemana) {
				if (he.getFecha().equals(diaIterado.toString())) {
					especificosDelDia.add(he);
				}
			}

			if (!especificosDelDia.isEmpty()) {
				// Prioridad a horarios específicos (anulan el periódico de este día)
				for (HorarioEspecifico he : especificosDelDia) {
					long mins = calcularMinutos(he.getHora_inicio(), he.getHora_fin());
					turnosResultado.add(new HorarioSemanal(
							numDiaSemana,
							obtenerNombreDia(numDiaSemana),
							diaIterado.toString(),
							he.getHora_inicio(),
							he.getHora_fin(),
							"Específico",
							mins
					));
				}
			} else {
				// Sin específicos para este día -> se toman los periódicos correspondientes
				for (HorarioPeriodico hp : periodicos) {
					if (hp.getDia_semana() == numDiaSemana) {
						long mins = calcularMinutos(hp.getHora_inicio(), hp.getHora_fin());
						turnosResultado.add(new HorarioSemanal(
								numDiaSemana,
								obtenerNombreDia(numDiaSemana),
								diaIterado.toString(),
								hp.getHora_inicio(),
								hp.getHora_fin(),
								"Periódico",
								mins
						));
					}
				}
			}
		}

		// Ordenación cronológica: por fecha y luego por hora de inicio
		Collections.sort(turnosResultado, new Comparator<HorarioSemanal>() {
			@Override
			public int compare(HorarioSemanal h1, HorarioSemanal h2) {
				int cmpFecha = h1.getFecha().compareTo(h2.getFecha());
				if (cmpFecha != 0) {
					return cmpFecha;
				}
				LocalTime t1 = LocalTime.parse(h1.getHoraInicio());
				LocalTime t2 = LocalTime.parse(h2.getHoraInicio());
				return t1.compareTo(t2);
			}
		});

		return turnosResultado;
	}

	/**
	 * Calcula el sumatorio de minutos trabajados en los turnos de la semana.
	 */
	public long calcularMinutosTotalesSemana(List<HorarioSemanal> horariosSemana) {
		long total = 0;
		if (horariosSemana != null) {
			for (HorarioSemanal h : horariosSemana) {
				total += h.getDuracionMinutos();
			}
		}
		return total;
	}

	private long calcularMinutos(String horaInicio, String horaFin) {
		LocalTime inicio = LocalTime.parse(horaInicio);
		LocalTime fin = LocalTime.parse(horaFin);
		if (fin.isBefore(inicio)) {
			throw new ApplicationException(
					"La hora de fin (" + horaFin + ") no puede ser anterior a la de inicio (" + horaInicio + ").");
		}
		return Duration.between(inicio, fin).toMinutes();
	}

	private String obtenerNombreDia(int diaSemana) {
		String[] dias = { "", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" };
		if (diaSemana >= 1 && diaSemana <= 7) {
			return dias[diaSemana];
		}
		return "Desconocido";
	}
}