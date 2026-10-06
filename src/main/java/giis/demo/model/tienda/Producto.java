package giis.demo.model.tienda;

public class Producto {
	
	private String id;
    private String nombre;
    private double precio;
    private Categoria categoria;
    
    public enum Categoria{
    	ParteArriba, ParteAbajo, Merchandising;
    }

    public Producto(String id, String nombre, double precio, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public Categoria getCategoria() { return categoria; }

    @Override
    public String toString() {
        return String.format("%s - %.2f€", nombre, precio);
    }

}
