package giis.demo.model.entrevistas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import giis.demo.exceptions.InterviewException;
import giis.demo.exceptions.RuntimeInterviewException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoDeportivo;

public class CrearEntrevistaService {
	private JDBC jdbc = new JDBC();
	
	/**
	 * Valida los campos y crea la entrevista si todos son correctos.
	 * Si se crea la entrevista, elimina las demas franjas del jugador
	 * para ese dia.
	 * En caso contrario, lanza una InterviewException
	 * @param jugadorSeleccionado
	 * @param fechaSeleccionada
	 * @param franjaSeleccionada
	 * @param nombreMedioCom
	 * @throws InterviewException
	 * @throws SQLException 
	 */
	public void validarYCrearEntrevista(EmpleadoDeportivo jugadorSeleccionado, 
			LocalDate fechaSeleccionada, FranjaHoraria franjaSeleccionada, 
			String nombreMedioCom) throws InterviewException {
		if(jugadorSeleccionado == null || fechaSeleccionada == null || franjaSeleccionada == null 
				|| nombreMedioCom == null) {
			throw new InterviewException("Ninguno de los campos puede ser null");
		}
		
		if(nombreMedioCom.isBlank()) {
			throw new InterviewException("El nombre del medio de comunicacion no puede ser vacío");
		}
		
		try {
			jdbc.crearEntrevistaYEliminarFranjasCoincidentes(jugadorSeleccionado, 
					franjaSeleccionada, nombreMedioCom);
		} catch (SQLException e) {
			throw new RuntimeInterviewException("Error al validar y crear la entrevista");
		}
	}

	/**
	 * Devuelve una lista de FranjasHorarias para el jugador seleccionado y 
	 * la fecha seleccionada, ambos pasados por parametro
	 * @param jugadorSeleccionado
	 * @param fechaSeleccionada
	 * @return
	 * @throws InterviewException 
	 */
	public List<FranjaHoraria> cargarFranjas(EmpleadoDeportivo jugadorSeleccionado, LocalDate fechaSeleccionada) throws InterviewException {
		if(jugadorSeleccionado == null || fechaSeleccionada == null) {
			throw new InterviewException("Jugador o fecha seleccionada null");
		}
		
		try {
			return jdbc.cargarFranjas(jugadorSeleccionado, fechaSeleccionada);
		} catch (SQLException e) {
			throw new RuntimeInterviewException("Error al cargar las franjas del jugador");
		}
	}

	/**
	 * Devuelve una lista de jugadores con franjas creadas
	 * y sin entrevistas asociadas a estas
	 * @return
	 * @throws SQLException 
	 */
	public List<EmpleadoDeportivo> getJugadoresConFranjas()  {
		try {
			return jdbc.getJugadoresConFranjas();
		} catch (SQLException e) {
			throw new RuntimeInterviewException("Error al cargar los jugadores con franjas");
		}
	}

	/**
	 * Devuelve una lista de fechas que son los dias que el jugador tiene franjas creadas
	 * @param jugadorSeleccionado
	 * @param mes
	 * @param año
	 * @return
	 * @throws InterviewException 
	 * @throws SQLException 
	 */
	public List<LocalDate> getDiasConFranja(EmpleadoDeportivo jugadorSeleccionado, int mes, int año) throws InterviewException{
		if(jugadorSeleccionado == null) {
			throw new InterviewException("Jugador seleccionado null");
		}
		
		try {
			return jdbc.getDiasConFranjaYSinEntrevista(jugadorSeleccionado, mes, año);
		} catch (SQLException e) {
			throw new RuntimeInterviewException("Error al obtener los dias que el jugador tiene "
					+ "franjas asociadas");
		}
	}
	
}
