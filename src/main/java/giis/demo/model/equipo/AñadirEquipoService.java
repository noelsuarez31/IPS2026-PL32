package giis.demo.model.equipo;

import java.sql.SQLException;
import java.util.List;

import giis.demo.exceptions.TeamException;
import giis.demo.exceptions.UnauthorizedException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Entrenador;
import giis.demo.model.empleado.Posicion;


public class AñadirEquipoService {
	private BaseEmpleado empleadoActivo;
	private JDBC jdbc = new JDBC();
	
	/**
	 * Llama al metodo de jdbc que almacena el equipo en la BBDD
	 * @param jugadores
	 * @param entrenadores
	 * @param tecnicosAdicionales
	 * @param tipo
	 * @param categoria
	 * @param nombreEquipo
	 */
	public void añadirEquipo(List<EmpleadoDeportivo> jugadores, List<Entrenador> entrenadores, List<EmpleadoDeportivo> tecnicosAdicionales,
			String tipo, CategoriaEquipo categoria, String nombreEquipo) {
		// pedirPermiso(Posicion.GERENTE);
		
		Equipo equipo = crearEquipo(jugadores, entrenadores, tecnicosAdicionales, tipo, categoria, nombreEquipo);
		
		try {
			jdbc.almacenarEquipo(equipo, jugadores, entrenadores, tecnicosAdicionales);
		} catch (SQLException sqle) {
			throw new TeamException(sqle.getMessage());
			// throw new TeamException("SQLException: Error al almacenar el equipo en la BBDD");
		}
	}
	
	/**
	 * Crea el equipo a partir de los jugadores, entrenadores y tecnicos adicionales
	 * y el tipo, categoria y nombre del equipo, haciendo validaciones en los campos
	 * correspondientes
	 * @param jugadores
	 * @param entrenadores
	 * @param tecnicosAdicionales
	 * @param tipo
	 * @param categoria
	 * @param nombre
	 * @return
	 * @throws TeamException
	 */
	private Equipo crearEquipo(List<EmpleadoDeportivo> jugadores, List<Entrenador> entrenadores, List<EmpleadoDeportivo> tecnicosAdicionales,
			String tipo, CategoriaEquipo categoria, String nombre) throws TeamException{
		if(jugadores == null || entrenadores == null || tecnicosAdicionales == null || tipo == null || categoria == null || nombre == null) {
			throw new TeamException("No se admiten valores nulos");
		}
		
		if(!sonJugadores(jugadores)) {
			throw new TeamException("Los empleados recibidos no son jugadores");
		}
		
		if(jugadores.size() < Equipo.NUM_MIN_JUGADORES) {
			throw new TeamException("Numero de jugadores insuficiente para crear el equipo");
		}
		
		if(tipo.equals("En formacion") && !jugadoresEdadValida(jugadores, categoria)) {
			throw new TeamException("Edad invalida de algunos jugadores para esta categoria");
		}
		
		if(entrenadores.size() < Equipo.NUM_MIN_ENTRENADORES) {
			throw new TeamException("Requisitos de cuerpo tecnico no cumplidos: "
					+ "Debe de haber un primer y un segundo entrenador");
		}
		
		if(!tipo.equals("Profesional") && !tipo.equals("En formacion")) {
			throw new TeamException("Tipo de equipo invalido: Solo se admite profesional/en formacion");
		}
		
		return new Equipo(nombre, tipo, categoria);
	}
	
	private boolean jugadoresEdadValida(List<EmpleadoDeportivo> jugadores, CategoriaEquipo categoria) {
		for(EmpleadoDeportivo jugador : jugadores) {
			if(!categoria.esEdadValida(jugador.calcularEdad())) {
				return false;
			}
		}
		return true;
	}

	private boolean sonJugadores(List<EmpleadoDeportivo> jugadores) {
		for(EmpleadoDeportivo end : jugadores) {
			if(end.getPosicion() != Posicion.JUGADOR) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Llama al metodo de jdbc que obtiene los entrenadores sin equipo
	 * @return
	 */
	public List<Entrenador> obtenerEntrenadoresDisponibles() {
		try {
			return jdbc.obtenerEntrenadoresDisponibles();
		} catch (SQLException e) {
			throw new TeamException("SQLException: Error al obtener los entrenadores disponibles");
		}
	}

	public List<EmpleadoDeportivo> obtenerJugadoresDisponibles(CategoriaEquipo categoria) {
		if(categoria == null) {
			throw new TeamException("Categoria nula");
		}
		
		try {
			return jdbc.obtenerJugadoresDisponibles(categoria);
		} catch (SQLException e) {
			throw new TeamException("SQLException: Error al obtener los jugadores disponibles");
		}
	}
	
	/**
	 * Llama al metodo de jdbc que obtiene el nombre de las categorias
	 * segun el tipo de equipo
	 * @param tipo
	 * @return
	 */
	public List<String> obtenerNombreDeCategoriasPorTipo(String tipo) {
		if(tipo == null) {
			throw new TeamException("Tipo es nulo");
		}
		
		try {
			return jdbc.obtenerNombreDeCategoriasPorTipo(tipo);
		} catch (SQLException e) {
			throw new TeamException("SQLException: Error al obtener las categorias por tipo");
		}
	}

	/**
	 * Llama al metodo de jdbc que obtiene los tecnicos adicionales sin equipo
	 * @return
	 */
	public List<EmpleadoDeportivo> obtenerRestoTecnicos() {
		try {
			return jdbc.obtenerRestoTecnicos();
		} catch (SQLException e) {
			throw new TeamException("SQLException: Error al obtener el resto de tecnicos");
		}
	}

	public CategoriaEquipo obtenerObjetoCategoria(String nombreCategoriaSeleccionada) {
		try {
			return jdbc.obtenerObjetoCategoria(nombreCategoriaSeleccionada);
		} catch (SQLException e) {
			throw new TeamException("SQLException: Error al obtener la categoria");
		}
	}
	
	// Gestion de roles
	public void seleccionarEmpleadoActivo(BaseEmpleado empleado) {
		if (empleado == null) {
			throw new TeamException("El empleado seleccionado no puede ser null");
		}
		this.empleadoActivo = empleado;
	}

	private void pedirPermiso(Posicion posicionRequerida) throws UnauthorizedException {
		if (posicionRequerida == null) {
			throw new UnauthorizedException("Posicion requerida no puede ser null");
		}

		if (empleadoActivo.getPosicion() != posicionRequerida) {
			throw new UnauthorizedException("Posicion del empleado no es la requerida");
		}
	}
}
