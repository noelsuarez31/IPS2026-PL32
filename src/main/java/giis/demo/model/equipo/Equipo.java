package giis.demo.model.equipo;

public class Equipo {
	public final static int NUM_MIN_JUGADORES = 7;
	public final static int NUM_MIN_ENTRENADORES = 2;

	private int idEquipo;
	private String nombre;
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
	
	public int getIdEquipo() {
		return this.idEquipo;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getTipoEquipo() {
		return this.tipoEquipo;
	}
	
	public boolean getIsPropio() {
		return this.isPropio;
	}
	
	public CategoriaEquipo getCategoriaEquipo() {
		return this.categoriaEquipo;
	}
}
