package giis.demo.model.equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.exceptions.EmployeeException;
import giis.demo.exceptions.TeamException;
import giis.demo.exceptions.UnauthorizedException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Entrenador;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.equipo.CategoriaEquipo;


public class AñadirEquipoService {
	private List<Equipo> equipos = new ArrayList<Equipo>();
	private BaseEmpleado empleadoActivo;
	private JDBC jdbc = new JDBC();
	private int ultimoIdEquipo = 5;
	
	public void seleccionarEmpleadoActivo(BaseEmpleado empleado) {
		if(empleado == null) {
			throw new EmployeeException("El empleado seleccionado no puede ser null");
		}
		this.empleadoActivo = empleado;
	}
	
	public void añadirEquipo(List<EmpleadoDeportivo> jugadores, List<Entrenador> entrenadores,
			String tipo, CategoriaEquipo categoria, String nombreEquipo){
		pedirPermiso(Posicion.GERENTE);
		
		Equipo equipo = crearEquipo(jugadores, entrenadores, tipo, categoria, nombreEquipo);
		
		equipos.add(equipo);
	}
	
	public Equipo crearEquipo(List<EmpleadoDeportivo> jugadores, List<Entrenador> entrenadores,
			String tipo, CategoriaEquipo categoria, String nombre) throws TeamException{
		if(jugadores == null || entrenadores == null || tipo == null || categoria == null || nombre == null) {
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
		
		if(entrenadores.size() < Equipo.NUM_MIN_ENTRENADORES || !cuerpoTecnicoCompleto(entrenadores)) {
			throw new TeamException("Requisitos de cuerpo tecnico no cumplidos: "
					+ "Debe de haber un primer y un segundo entrenador");
		}
		
		if(!tipo.equals("Profesional") && !tipo.equals("En formacion")) {
			throw new TeamException("Tipo de equipo invalido: Solo se admite profesional/en formacion");
		}
		
		return new Equipo(siguienteIdEquipo(), nombre, tipo, categoria);
	}
	
	private int siguienteIdEquipo() {
		return ultimoIdEquipo +1;
	}

	private void pedirPermiso(Posicion posicionRequerida) throws UnauthorizedException{
		if(posicionRequerida == null) {
			throw new UnauthorizedException("Posicion requerida no puede ser null");
		}
		
		if(empleadoActivo.getPosicion() != posicionRequerida) {
			throw new UnauthorizedException("Posicion del empleado no es la requerida");
		}
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

	private boolean cuerpoTecnicoCompleto(List<Entrenador> entrenadores) {
		boolean hayPrimerEntrenador = false;
		boolean haySegundoEntrenador = false;
		
		for(Entrenador entrenador : entrenadores) {
			if(entrenador.esPrimerEntrenador()) {
				hayPrimerEntrenador = true;
			} else if(entrenador.esSegundoEntrenador()) {
				haySegundoEntrenador = true;
			}
		}
		
		return hayPrimerEntrenador && haySegundoEntrenador;
	}

	public List<Entrenador> obtenerEntrenadoresDisponibles() {
		String consultaEntrenadores = "SELECT * FROM Entrenador";
		String consultaBuscarEntrenadores = "SELECT nombre, apellido, salario, fechaNacimiento, numeroDeTelefono FROM"
				+ " EmpleadoDeportivo ed, BaseEmpleado bd WHERE ed.dni=bd.dni and ed.dni=?";
		List<Entrenador> cuerpoTecnico = new ArrayList<Entrenador>();
		
		try(Connection con = JDBC.abrirConexion();
				PreparedStatement pst = con.prepareStatement(consultaEntrenadores)){
			ResultSet rsEntrenador = pst.executeQuery();
			
			while(rsEntrenador.next()) {
				String dniEntrenador = rsEntrenador.getString(1);
				try(PreparedStatement pst2 = con.prepareStatement(consultaBuscarEntrenadores);){
					pst2.setString(1, dniEntrenador);
					ResultSet rsEntrenadorCompleto = pst2.executeQuery();
					while(rsEntrenadorCompleto.next()) {
						Entrenador entrenador = new Entrenador(dniEntrenador, rsEntrenadorCompleto.getString(1), rsEntrenadorCompleto.getString(2),
								rsEntrenadorCompleto.getBigDecimal(3), LocalDate.parse(rsEntrenadorCompleto.getString(4)) ,rsEntrenadorCompleto.getString(5));
						cuerpoTecnico.add(entrenador);
					}
				}
			}
		} catch(SQLException sqle) {
			throw new TeamException("Error al obtener cuerpo tecnico disponible");
		}
		
		return cuerpoTecnico;
	}

	public List<EmpleadoDeportivo> obtenerJugadoresDisponibles(CategoriaEquipo categoria) {
		if(categoria == null) {
			throw new TeamException("Categoria nula");
		}
		
		String consultaJugadores = "SELECT * FROM EmpleadoDeportivo WHERE posicion = ?";
		String consultaJugadorCompleto = "SELECT * FROM BaseEmpleado be, EmpleadoDeportivo ed "
				+ "WHERE be.dni=ed.dni AND ed.dni=?";
		List<EmpleadoDeportivo> jugadores = new ArrayList<EmpleadoDeportivo>();
		
		try(Connection con = jdbc.abrirConexion();
				PreparedStatement pst = con.prepareStatement(consultaJugadores)){
			pst.setString(1, Posicion.JUGADOR.name());
			ResultSet rsJugadores = pst.executeQuery();
			
			while(rsJugadores.next()) {
				String dni = rsJugadores.getString(1);
	
				try(PreparedStatement pst2 = con.prepareStatement(consultaJugadorCompleto);){
					ResultSet rsJugadorCompleto = pst2.executeQuery();
					
					while(rsJugadorCompleto.next()) {
						jugadores.add(new EmpleadoDeportivo(dni, rsJugadorCompleto.getString(1), rsJugadorCompleto.getString(2),
								rsJugadorCompleto.getBigDecimal(3), LocalDate.parse(rsJugadorCompleto.getString(4)), rsJugadorCompleto.getString(5), Posicion.JUGADOR));
					}
				}
			}
		} catch(SQLException sqle) {
			throw new TeamException(sqle.getMessage());
			//throw new TeamException("Error al obtener categorias por tipo");
		}
		
		if(categoria.getNombre().equals("Primer Equipo") || categoria.getNombre().equals("Filial")) {
			return jugadores;
		}
		
		List<EmpleadoDeportivo> jugadoresFiltradosPorCategoria = new ArrayList<EmpleadoDeportivo>();
		for(EmpleadoDeportivo jugador : jugadores) {
			if(categoria.esEdadValida(jugador.calcularEdad())) {
				jugadoresFiltradosPorCategoria.add(jugador);
			}
		}
		
		return jugadoresFiltradosPorCategoria;
	}
	
	public List<CategoriaEquipo> obtenerCategoriasPorTipo(String tipo) {
		String consultaSQL = "SELECT * FROM CategoriaEquipo WHERE tipoEquipo = ?";
		List<CategoriaEquipo> categoriasPorTipo = new ArrayList<CategoriaEquipo>();
		
		try(Connection con = jdbc.abrirConexion();
				PreparedStatement pst = con.prepareStatement(consultaSQL)){
			pst.setString(1,tipo);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				categoriasPorTipo.add(new CategoriaEquipo(rs.getString(1), rs.getInt(2),
						rs.getInt(3), rs.getInt(4), rs.getString(5)));
			}
		} catch(SQLException sqle) {
			throw new TeamException("Error al obtener categorias por tipo");
		}
	    
		return categoriasPorTipo;
	}

	public List<EmpleadoDeportivo> obtenerRestoTecnicos() {
		String consultaEntrenadores = "SELECT * FROM EmpleadoDeportivo WHERE posicion=?";
		String consultaBuscarEntrenadores = "SELECT nombre, apellido, salario, fechaNacimiento, numeroDeTelefono FROM"
				+ " EmpleadoDeportivo ed, BaseEmpleado bd WHERE ed.dni=bd.dni and ed.dni=?";
		List<EmpleadoDeportivo> tecnicosAdicionales = new ArrayList<EmpleadoDeportivo>();
		
		try(Connection con = jdbc.abrirConexion();
				PreparedStatement pst = con.prepareStatement(consultaEntrenadores)){
			pst.setString(1, Posicion.TECNICO_ADICIONAL.name());
			ResultSet rsTecnicoAdicional = pst.executeQuery();
			
			while(rsTecnicoAdicional.next()) {
				String dniTecnicoAdicional = rsTecnicoAdicional.getString(1);
				Posicion posicion = Posicion.valueOf(rsTecnicoAdicional.getString(2));
				try(PreparedStatement pst2 = con.prepareStatement(consultaBuscarEntrenadores);){
					pst2.setString(1, dniTecnicoAdicional);
					ResultSet rsTecnicoAdicionalCompleto = pst2.executeQuery();
					while(rsTecnicoAdicionalCompleto.next()) {
						EmpleadoDeportivo tecnicoAdicional = new EmpleadoDeportivo(dniTecnicoAdicional, rsTecnicoAdicionalCompleto.getString(1), rsTecnicoAdicionalCompleto.getString(2),
								rsTecnicoAdicionalCompleto.getBigDecimal(3), LocalDate.parse(rsTecnicoAdicionalCompleto.getString(4)) ,rsTecnicoAdicionalCompleto.getString(5), posicion);
						tecnicosAdicionales.add(tecnicoAdicional);
					}
				}
			}
		} catch(SQLException sqle) {
			throw new TeamException("Error al obtener resto de tecnicos disponibles");
		}
		
		return tecnicosAdicionales;
	}

	public CategoriaEquipo obtenerObjetoCategoria(String nombreCategoriaSeleccionada) {
		String queryCategoria = "SELECT * FROM CategoriaEquipo WHERE nombre=?";
		
		try(Connection con = jdbc.abrirConexion();
				PreparedStatement pst = con.prepareStatement(queryCategoria);){
			pst.setString(1, nombreCategoriaSeleccionada);
			
			ResultSet rsCategoria = pst.executeQuery();
			while(rsCategoria.next()) {
				return new CategoriaEquipo(nombreCategoriaSeleccionada, rsCategoria.getInt(2), 
						rsCategoria.getInt(3), rsCategoria.getInt(4), rsCategoria.getString(5));
			}
		}
	}
}
