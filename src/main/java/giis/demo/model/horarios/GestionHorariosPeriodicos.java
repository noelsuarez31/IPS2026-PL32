package giis.demo.model.horarios;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.util.ApplicationException;

public class GestionHorariosPeriodicos {
	private JDBC jdbc = new JDBC();
	/**
	 * Valida que el nuevo horario cumpla con las restricciones de negocio (máximo 8 horas diarias, 
	 * máximo 40 horas semanales y sin solapamientos) y lo añade a la base de datos si es correcto.
	 * @param nuevoHorario el objeto HorarioPeriodico con los datos del turno a insertar
	 * @throws SQLException si ocurre un error en el acceso o inserción en la base de datos
	 * @throws ApplicationException si se incumple alguna regla de negocio (límites de horas o solapamiento)
	 */
	public void validarYAnadirHorario(HorarioPeriodico nuevoHorario) throws SQLException {
		
		// Pedimos a la BD todos los turnos que ya tiene el empleado
		List<HorarioPeriodico> horariosExistentes = jdbc.getHorariosEmpleado(nuevoHorario.getDni_empleado());
		
		// Calculamos los minutos exactos del turno que se intenta crear
		long minutosNuevos = calcularMinutos(nuevoHorario.getHora_inicio(), nuevoHorario.getHora_fin());
		
		long minutosTotalesDia = minutosNuevos;
		long minutosTotalesSemana = minutosNuevos;
		
		// Parseamos las horas del nuevo turno para comprobar solapamientos
		LocalTime inicioNuevo = LocalTime.parse(nuevoHorario.getHora_inicio());
		LocalTime finNuevo = LocalTime.parse(nuevoHorario.getHora_fin());
		
		// Sumamos los minutos que ya tenía asignados y comprobamos choques
		for (HorarioPeriodico h : horariosExistentes) {
			long minutosTurno = calcularMinutos(h.getHora_inicio(), h.getHora_fin());
			
			minutosTotalesSemana += minutosTurno;
			
			// Si el turno que estamos comprobando es del mismo día de la semana
			if (h.getDia_semana() == nuevoHorario.getDia_semana()) {
				minutosTotalesDia += minutosTurno;
				
				// COMPROBACIÓN DE SOLAPAMIENTO
				LocalTime inicioExistente = LocalTime.parse(h.getHora_inicio());
				LocalTime finExistente = LocalTime.parse(h.getHora_fin());
				
				if (inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
					throw new ApplicationException("Error: El turno se solapa con un horario existente de " 
							+ h.getHora_inicio() + " a " + h.getHora_fin() + ".");
				}
			}
		}
		
		// Validamos los límites en minutos (8h * 60 = 480 min) y (40h * 60 = 2400 min)
		if (minutosTotalesDia > 480) {
			throw new ApplicationException("Error: El empleado superaría el límite de 8 horas en este día.");
		}
		
		if (minutosTotalesSemana > 2400) {
			throw new ApplicationException("Error: El empleado superaría el límite de 40 horas semanales.");
		}
		
		// Si nada lanza excepción, el horario es perfecto: a la Base de Datos.
		jdbc.añadirHorario(nuevoHorario);
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

	public List<HorarioPeriodico> obtenerHorariosDeEmpleado(String dni) throws SQLException {
		return jdbc.getHorariosEmpleado(dni);
	}
}