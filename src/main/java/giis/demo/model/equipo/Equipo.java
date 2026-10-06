package giis.demo.model.equipo;

import java.util.List;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Entrenador;



public class Equipo {
	public final static int NUM_MIN_JUGADORES = 7;
	public final static int NUM_MIN_ENTRENADORES = 2;

	private int idEquipo;
	private List<EmpleadoDeportivo> jugadores;
	private List<Entrenador> entrenadores;
	private String tipoEquipo;
	private CategoriaEquipo categoriaEquipo;
	
	public Equipo(int id, List<EmpleadoDeportivo> jugadores, List<Entrenador> entrenadores,
			String tipo, CategoriaEquipo categoria) {
		this.idEquipo = id;
		this.jugadores = List.copyOf(jugadores);
		this.entrenadores = List.copyOf(entrenadores);
		this.tipoEquipo = tipo;
		this.categoriaEquipo = categoria;
	}
}
