package giis.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import giis.demo.jdbc.empleado.EmpleadoDeportivoJdbc;
import giis.demo.jdbc.empleado.EntrenadorJdbc;
import giis.demo.jdbc.entradas.ButacaJdbc;
import giis.demo.jdbc.entradas.PartidoJdbc;
import giis.demo.jdbc.entradas.RegistrarVentaJdbc;
import giis.demo.jdbc.equipo.CategoriaEquipoJdbc;
import giis.demo.jdbc.equipo.EquipoJdbc;
import giis.demo.jdbc.horarios.HorarioPeriodicoJdbc;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Entrenador;
import giis.demo.model.entradas.Butaca;
import giis.demo.model.entradas.Partido;
import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;
import giis.demo.model.equipo.CategoriaEquipo;
import giis.demo.model.equipo.Equipo;
import giis.demo.model.horarios.HorarioPeriodico;

public class JDBC {

	//public static final String DRIVER_GROUP2 = "org.sqlite.JDBC";
	public static final String URL_GROUP2 = "jdbc:sqlite:DemoDB.db";
	
	private Connection con;
	
	/**
	 * Constructor de la clase, inicializa la conexion
	 */
	public JDBC() {
		
		try {
			
			con = DriverManager.getConnection(URL_GROUP2);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 * Obtiene las butacas libres disponibles
	 * @param idPartido
	 * @param tribuna
	 * @param seccion
	 * @return las butcas libres
	 * @throws SQLException
	 */
	public List<Butaca> getFreeButacas(int idPartido, TipoTribuna tribuna, TipoSeccion seccion)
			throws SQLException {

		return ButacaJdbc.getFreeButacas(con, idPartido, tribuna, seccion);

	}
	
	/**
	 * Obtienes los distintos partidos disponibles
	 * @return los partidos disponibles
	 * @throws SQLException
	 */
	public List<Partido> getPartidos() throws SQLException {

		return PartidoJdbc.getPartidos(con);

	}
	
	/**
	 * Almacena las entradas y la venta
	 * @param idPartido
	 * @param butacasSeleccionadas
	 * @throws SQLException
	 */
	public void almacenar(int idPartido, List<Butaca> butacasSeleccionadas) throws SQLException {
		
		RegistrarVentaJdbc.registrar(con,idPartido, butacasSeleccionadas);
		
	}
	
	/**
	 * Cierra la conexion
	 * @throws SQLException
	 */
	public void close() throws SQLException{
		
		if(con!=null && !con.isClosed()) {
			
			con.close();
		}
		
	}

	/**
	 * Devuelve el objeto categoria con todos los atributos a partir del nombre
	 * de la categoria
	 * @param nombreCategoriaSeleccionada
	 * @return
	 * @throws SQLException
	 */
	public CategoriaEquipo obtenerObjetoCategoria(String nombreCategoriaSeleccionada) throws SQLException {
		return CategoriaEquipoJdbc.obtenerObjetoCategoria(con, nombreCategoriaSeleccionada);
	}

	/**
	 * Devuelve una lista con empleados deportivos cuya posicion es TECNICO_ADICIONAL
	 * @return
	 * @throws SQLException
	 */
	public List<EmpleadoDeportivo> obtenerRestoTecnicos() throws SQLException {
		return EmpleadoDeportivoJdbc.obtenerRestoTecnicos(con);
	}

	/**
	 * Devuelve una lista con el nombre de las categorias para un tipo
	 * de equipo correspondiente
	 * @param tipo
	 * @return
	 * @throws SQLException
	 */
	public List<String> obtenerNombreDeCategoriasPorTipo(String tipo) throws SQLException {
		return CategoriaEquipoJdbc.obtenerNombreDeCategoriasPorTipo(con, tipo);
	}

	/**
	 * Devuelve una lista con los empleados deportivos cuya posicion es JUGADOR
	 * y no tienen equipo asignado
	 * @param categoria
	 * @return
	 * @throws SQLException
	 */
	public List<EmpleadoDeportivo> obtenerJugadoresDisponibles(CategoriaEquipo categoria) throws SQLException {
		return EmpleadoDeportivoJdbc.obtenerJugadoresDisponibles(con, categoria);
	}

	/**
	 * Devuelve una lista de entrenadores que no tienen equipo asignado
	 * @return
	 * @throws SQLException
	 */
	public List<Entrenador> obtenerEntrenadoresDisponibles() throws SQLException {
		return EntrenadorJdbc.obtenerEntrenadoresDisponibles(con);
	}

	/**
	 * Almacena el equipo creado en la BBDD. Validando que los jugadores, entrenadores
	 * y tecnicos adicionales no tengan un equipo asignado ya. Les asigna el id del 
	 * nuevo equipo.
	 * @param equipo
	 * @param jugadores
	 * @param entrenadores
	 * @param tecnicosAdicionales
	 * @throws SQLException
	 */
	public void almacenarEquipo(Equipo equipo, List<EmpleadoDeportivo> jugadores, 
			List<Entrenador> entrenadores, List<EmpleadoDeportivo> tecnicosAdicionales) throws SQLException {
		EquipoJdbc.almacenarEquipo(con, equipo, jugadores, entrenadores, tecnicosAdicionales);
	}
	
	/**
	 * Inserta el horario en la tabla
	 * @param horario
	 * @throws SQLException
	 */
	public void añadirHorario(HorarioPeriodico horario) throws SQLException{
		HorarioPeriodicoJdbc.añadirHorario(con, horario);
	}
	
	/**
	 * Devuelve los horarios de un empleado
	 * @param idEmpleado
	 * @throws SQLException
	 */
	public List<HorarioPeriodico> getHorariosEmpleado(String dniEmpleado)throws SQLException{
		return HorarioPeriodicoJdbc.getHorariosEmpleado(con, dniEmpleado);
	}
	
	public List<EmpleadoNoDeportivo> getEmpleadosNoDeportivos() throws SQLException {
	    return HorarioPeriodicoJdbc.getEmpleadosNoDeportivos(con);
	}

	public Connection getCon() {
		return con;
	}

}
