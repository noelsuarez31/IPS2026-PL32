package giis.demo.model.entrevistas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import giis.demo.exceptions.NoAvailableSlotException;
import giis.demo.exceptions.SlotException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoDeportivo;

public class CrearFranjaService {
	private EmpleadoDeportivo jugadorSeleccionado;
	private JDBC jdbc = new JDBC();
	
	
	public void setJugadorSeleccionado(EmpleadoDeportivo jugador) {
		this.jugadorSeleccionado = jugador;
	}
	
	public EmpleadoDeportivo getJugadorSeleccionado() {
		return this.jugadorSeleccionado;
	}
	
	public List<EmpleadoDeportivo> obtenerJugadoresEquipoProfesional(int idEquipo){
		try {
			return jdbc.cargarJugadoresEquipoProfesional(idEquipo);
		} catch (SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}
	
	public List<String> obtenerDnisEntrenadoresDeEquiposProfesionales(){
		try {
			return jdbc.obtenerDnisEntrenadoresDeEquiposProfesionales();
		} catch (SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}
	
	public void crearFranjaHoraria(LocalDate fechaFranja, LocalTime horaInicio,
			LocalTime horaFin) throws NoAvailableSlotException {
		validarCampos(jugadorSeleccionado, fechaFranja, horaInicio, horaFin);
		
		FranjaHoraria nuevaFranja = new FranjaHoraria(jugadorSeleccionado.getDni(), fechaFranja, horaInicio,
				horaFin);
		
		try {
			jdbc.almacenarFranja(nuevaFranja);
		} catch (SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}

	private void validarCampos(EmpleadoDeportivo jugador, LocalDate fechaFranja, 
			LocalTime horaInicio, LocalTime horaFin) throws NoAvailableSlotException{
		if(jugador == null || fechaFranja == null || horaInicio == null || horaFin == null) {
			throw new NoAvailableSlotException("No se admiten valores nulos");
		}
		
		if(yaHayEsaFranjaParaEseJugador(jugador, fechaFranja, horaInicio, horaFin)) {
			throw new NoAvailableSlotException("Ya has añadido esa franja a este jugador");
		}
		
		String motivoConflicto = equipoTienePartidoOEntrenamiento(jugador, fechaFranja, horaInicio, horaFin);
		if(!motivoConflicto.isEmpty()) {
			throw new NoAvailableSlotException(motivoConflicto);
		}
		
		if(jugadorTieneAsignadaEntrevista(jugador, fechaFranja)) {
			DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			throw new NoAvailableSlotException(String.format("El jugador ya tiene asignada una entrevista el "
					+ "dia (%s). Por tanto, no se pueden crear franjas ese dia. Escoja otro."
					, fechaFranja.format(formatoDeseado)));
		}
		
		if(fechaFranja.isBefore(LocalDate.now())) {
			throw new NoAvailableSlotException("La fecha introducida es anterior a ahora");
		}
		
		if(!horaFin.isAfter(horaInicio)) {
			throw new NoAvailableSlotException("La hora de fin es anterior a la de inicio");
		}
		
		if(fechaFranja.isEqual(LocalDate.now()) && horaInicio.isBefore(LocalTime.now())) {
			throw new NoAvailableSlotException("La hora de inicio es anterior a ahora");
		}
		
	}

	private boolean yaHayEsaFranjaParaEseJugador(EmpleadoDeportivo jugador, LocalDate fechaFranja, LocalTime horaInicio,
			LocalTime horaFin) {
		try {
			return jdbc.yaHayEsaFranjaParaEseJugador(jugador, fechaFranja, horaInicio, horaFin);
		} catch(SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}

	private String equipoTienePartidoOEntrenamiento(EmpleadoDeportivo jugador, LocalDate fechaFranja,
			LocalTime horaInicio, LocalTime horaFin) {
		try {
			return jdbc.equipoTienePartidoOEntrenamiento(jugador, fechaFranja, horaInicio, horaFin);
		} catch (SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}

	private boolean jugadorTieneAsignadaEntrevista(EmpleadoDeportivo jugador, LocalDate fechaFranja) {
		try {
			return jdbc.jugadorTieneAsignadaEntrevista(jugador, fechaFranja);
		} catch (SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}

	public int getIdEquipo(String nifEntrenador) {
		try {
			return jdbc.getIdEquipo(nifEntrenador);
		} catch (SQLException sqle) {
			throw new SlotException(sqle.getMessage());
		}
	}
}
