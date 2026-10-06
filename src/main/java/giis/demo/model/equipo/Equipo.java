package giis.demo.model.equipo;

public class Equipo {
	public final static int NUM_MIN_JUGADORES = 7;
	public final static int NUM_MIN_ENTRENADORES = 2;

	private int idEquipo;
	private String nombre;
//	private List<EmpleadoDeportivo> jugadores;
//	private List<Entrenador> entrenadores;
	private String tipoEquipo;
	private CategoriaEquipo categoriaEquipo;
	private boolean isPropio;
	
	
	public Equipo(int id, String nombre, String tipo, CategoriaEquipo categoria) {
		this.idEquipo = id;
		this.nombre = nombre;
		this.tipoEquipo = tipo;
		this.categoriaEquipo = categoria;
	}
	
	public void setIsPropio(boolean isPropio) {
		this.isPropio = isPropio;
	}
}
