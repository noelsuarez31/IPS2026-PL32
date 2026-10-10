package giis.demo.model.entrevistas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import giis.demo.exceptions.NoAvailableSlotException;
import giis.demo.exceptions.SlotException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoDeportivo;

public class CrearFranjaService {
	private EmpleadoDeportivo jugadorSeleccionado;
	private JDBC jdbc;
	
	
	public void setJugadorSeleccionado(EmpleadoDeportivo jugador) {
		this.jugadorSeleccionado = jugador;
	}
	
	public List<EmpleadoDeportivo> obtenerJugadoresEquipoProfesional(int idEquipo){
		return jdbc.cargarJugadoresEquipoProfesional(idEquipo);
	}
	
	public List<String> obtenerDnisEntrenadoresDeEquiposProfesionales(){
		return jdbc.obtenerDnisEntrenadoresDeEquiposProfesionales();
	}
	
	public void crearFranjaHoraria(LocalDate fechaFranja, LocalTime horaInicio,
			LocalTime horaFin) throws NoAvailableSlotException {
		validarCampos(jugadorSeleccionado, fechaFranja, horaInicio, horaFin);
		
		FranjaHoraria nuevaFranja = new FranjaHoraria(jugadorSeleccionado.getDni(), fechaFranja, horaInicio,
				horaFin);
		
		jdbc.almacenarFranja(nuevaFranja);
	}

	private void validarCampos(EmpleadoDeportivo jugador, LocalDate fechaFranja, 
			LocalTime horaInicio, LocalTime horaFin) throws NoAvailableSlotException{
		if(jugador == null || fechaFranja == null || horaInicio == null || horaFin == null) {
			throw new NoAvailableSlotException("No se admiten valores nulos");
		}
		
		if(jugadorTieneAsignadaEntrevista(jugador, fechaFranja)) {
			throw new NoAvailableSlotException("El jugador ya tiene asignada una franja");
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

	private boolean jugadorTieneAsignadaEntrevista(EmpleadoDeportivo jugador, LocalDate fechaFranja) {
		return jdbc.jugadorTieneAsignadaEntrevista(jugador, fechaFranja);
	}

	public int getIdEquipo(String nifEntrenador) {
		try {
			return jdbc.getIdEquipo(nifEntrenador);
		} catch (SQLException e) {
			throw new SlotException(e.getMessage());
		}
	}
}
