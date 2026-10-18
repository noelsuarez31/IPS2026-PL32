package giis.demo.model.horarios;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.util.ApplicationException;

public class GestionHorariosEspecificos {
	private JDBC jdbc = new JDBC();
	
	/**
	 * Valida que el nuevo horario específico cumpla todas las reglas de negocio
	 * @param nuevoHorario objeto HorarioEspecifico con los datos del turno a validar e insertar.
	 * @throws SQLException si ocurre un fallo en el acceso a la base de datos.
	 * @throws ApplicationException si se incumple alguna regla de negocio.
	 */
	public void validarYAnadirHorario(HorarioEspecifico nuevoHorario) throws SQLException {
		
		
		
		// para añadir un horario especifico, el empleado debe tener al menos un horario periodico asignado
		if (!jdbc.tieneHorariosPeriodicos(nuevoHorario.getDni_empleado())) {
			throw new ApplicationException("No se puede asignar un horario de día específico: "
					+ "el empleado no tiene ningún horario periódico asignado previamente.");
		}
		
		// Calculamos la duración en minutos del nuevo turno que se pretende añadir
		long minutosNuevoTurno = calcularMinutos(nuevoHorario.getHora_inicio(), nuevoHorario.getHora_fin());
		
		if (minutosNuevoTurno == 0) {
			throw new ApplicationException("Error: Los turnos deben durar al menos 1 minuto");
		}
		
		// validacion de turnos especificos de ese dia
		List<HorarioEspecifico> turnosMismoDia = jdbc.getHorariosEspecificosFecha(
				nuevoHorario.getDni_empleado(), nuevoHorario.getFecha());
		
		LocalTime inicioNuevo = LocalTime.parse(nuevoHorario.getHora_inicio());
		LocalTime finNuevo = LocalTime.parse(nuevoHorario.getHora_fin());
		
		// Comenzamos sumando los minutos del nuevo turno
		long minutosTotalesDia = minutosNuevoTurno;
		
		for (HorarioEspecifico turnoExistente : turnosMismoDia) {
			// Comprobamos solapamiento entre turnos del mismo día
			LocalTime inicioExistente = LocalTime.parse(turnoExistente.getHora_inicio());
			LocalTime finExistente = LocalTime.parse(turnoExistente.getHora_fin());
			
			if (inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
				throw new ApplicationException("Error: El turno se solapa con otro turno existente el mismo día de "
						+ turnoExistente.getHora_inicio() + " a " + turnoExistente.getHora_fin() + ".");
			}
			
			// Acumulamos los minutos de los turnos que ya tenía en esa fecha
			minutosTotalesDia += calcularMinutos(turnoExistente.getHora_inicio(), turnoExistente.getHora_fin());
		}
		
		// Límite diario: 8 horas = 480 minutos
		if (minutosTotalesDia > 480) {
			throw new ApplicationException("Error: El empleado superaría el límite de 8 horas diarias para la fecha " 
					+ nuevoHorario.getFecha() + ".");
		}
		
		// calculamos ahora las horas semanales
		LocalDate fechaTurno = LocalDate.parse(nuevoHorario.getFecha());
		int diaSemanaNum = fechaTurno.getDayOfWeek().getValue(); // 1 = Lunes 7 = Domingo
		
		LocalDate lunesSemana = fechaTurno.minusDays(diaSemanaNum - 1);	//lunes de la semana del turno especifico
		LocalDate domingoSemana = fechaTurno.plusDays(7 - diaSemanaNum);
		
		// Obtenemos los turnos periódicos y los específicos existentes en esa semana completa
		List<HorarioPeriodico> horariosPeriodicos = jdbc.getHorariosPeriodicosEmpleado(nuevoHorario.getDni_empleado());
		List<HorarioEspecifico> horariosEspecificosSemana = jdbc.getHorariosEspecificosRango(
				nuevoHorario.getDni_empleado(), lunesSemana.toString(), domingoSemana.toString());
		
		long minutosTotalesSemana = 0;
		
		for (int i = 0; i < 7; i++) {
			LocalDate diaIterado = lunesSemana.plusDays(i);
			int numDiaSemana = i + 1; 
			
			if (diaIterado.equals(fechaTurno)) {//si es el dia del horario que queremos añadir
				minutosTotalesSemana += minutosTotalesDia;
			} else {
				List<HorarioEspecifico> especificosDeEsteDia = new ArrayList<>();
				for (HorarioEspecifico he : horariosEspecificosSemana) {
					if (he.getFecha().equals(diaIterado.toString())) {
						especificosDeEsteDia.add(he);	//añañdimos los horarios especificos de ese dia
					}
				}
				
				if (!especificosDeEsteDia.isEmpty()) {//si hay especificos solo usamos esos
					for (HorarioEspecifico he : especificosDeEsteDia) {
						minutosTotalesSemana += calcularMinutos(he.getHora_inicio(), he.getHora_fin());
					}
				} else {
					// si no hay especificos se añade el periodico que tenga asignado ese dia (si tiene)
					for (HorarioPeriodico hp : horariosPeriodicos) {
						if (hp.getDia_semana() == numDiaSemana) {
							minutosTotalesSemana += calcularMinutos(hp.getHora_inicio(), hp.getHora_fin());
						}
					}
				}
			}
		}
		
		// Límite semanal: 40 horas = 2400 minutos
		if (minutosTotalesSemana > 2400) {
			throw new ApplicationException("Error: El empleado superaría el límite de 40 horas semanales en la semana del " 
					+ lunesSemana + " al " + domingoSemana + ".");
		}
		
		jdbc.añadirHorarioEspecifico(nuevoHorario);
	}
	
	/**
	 * Método auxiliar que transforma los textos de hora en tiempo real y calcula la diferencia en minutos.
	 * @param horaInicio la hora de inicio del turno en formato "HH:mm"
	 * @param horaFin la hora de fin del turno en formato "HH:mm"
	 * @return la duración del turno en minutos
	 * @throws ApplicationException si la hora de fin es anterior a la hora de inicio
	 */
	private long calcularMinutos(String horaInicio, String horaFin) {
		LocalTime inicio = LocalTime.parse(horaInicio);
		LocalTime fin = LocalTime.parse(horaFin);
		
		if (fin.isBefore(inicio)) {
			throw new ApplicationException("La hora de fin (" + horaFin + ") no puede ser anterior a la de inicio (" + horaInicio + ").");
		}
		
		return Duration.between(inicio, fin).toMinutes();
	}
	
	public List<EmpleadoNoDeportivo> obtenerEmpleadosNoDeportivos() throws SQLException {
		return jdbc.getEmpleadosNoDeportivos();
	}

	public List<HorarioEspecifico> obtenerHorariosEspecificosDeEmpleado(String dni, String fecha) throws SQLException {
		return jdbc.getHorariosEspecificosFecha(dni, fecha);
	}
	
	public List<HorarioPeriodico> obtenerHorariosPeriodicosDeEmpleado(String dni) throws SQLException {
		return jdbc.getHorariosPeriodicosEmpleado(dni);
	}

	public List<HorarioEspecifico> obtenerTodosLosHorariosEspecificosDeEmpleado(String dni) throws SQLException {
		return jdbc.getHorariosEspecificosEmpleado(dni);
	}
}
