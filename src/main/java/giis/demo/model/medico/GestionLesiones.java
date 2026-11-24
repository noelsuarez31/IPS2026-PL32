package giis.demo.model.medico;

import java.sql.SQLException;
import java.util.List;

import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.equipo.Equipo;
import giis.demo.util.ApplicationException;

public class GestionLesiones {

	private JDBC jdbc;

	public GestionLesiones() {
		jdbc = new JDBC();
	}

	public EmpleadoDeportivo[] getJugadores() {
		try {
			return jdbc.getJugadoresConEquipo().toArray(new EmpleadoDeportivo[0]);
		} catch (SQLException e) {
			throw new ApplicationException("Error al obtener los jugadores: " + e.getMessage());
		}
	}

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

	public Equipo[] getEquipos() {

		try {
			return jdbc.getEquiposLocales();
		} catch (SQLException e) {

			throw new ApplicationException("Error al obtener los equipos");
		}
	}

}
