package giis.demo.model.tienda;

import java.util.Objects;

public class Producto {
	private int id_producto;
	private String nombre;
	private double precio;
	private String tipo;

	public Producto(int id_producto, String nombre, double precio, String tipo) {
		this.id_producto = id_producto;
		this.nombre = nombre;
		this.precio = precio;
		this.tipo = tipo;
	}

	public int getId_producto() { return id_producto; }
	public String getNombre() { return nombre; }
	public double getPrecio() { return precio; }
	public String getTipo() { return tipo; }

	@Override
	public int hashCode() {
		return Objects.hash(id_producto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Producto other = (Producto) obj;
		return id_producto == other.id_producto;
	}
}
