package giis.demo.model.equipo;

public class CategoriaEquipo {
	private String nombre;
	private int edadMinima;
	private int edadMaxima;
	private String tipo;
	
	private int idCategoria;
	
	public CategoriaEquipo(String nombre, int edadMinima, int edadMaxima, String tipo) {
		this.nombre = nombre;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
		this.tipo = tipo;
	}
	
	public boolean esEdadValida(int edad) {
		return edad >= edadMinima && edad <= edadMaxima;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}

	public String getTipo() {
		return tipo;
	}
	
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;;
	}
	
	public int getIdCategoria() {
		return this.idCategoria;
	}
}
