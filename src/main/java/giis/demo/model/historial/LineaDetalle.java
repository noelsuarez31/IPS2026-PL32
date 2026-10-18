package giis.demo.model.historial;

/**
 * Linea de una venta de merchandising: producto, unidades y precio unitario
 */
public class LineaDetalle {
	private final String nombreProducto;
	private final int unidades;
	private final double precioUnitario;

	public LineaDetalle(String nombreProducto, int unidades, double precioUnitario) {
		this.nombreProducto = nombreProducto;
		this.unidades = unidades;
		this.precioUnitario = precioUnitario;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public int getUnidades() {
		return unidades;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public double getSubtotal() {
		return unidades * precioUnitario;
	}
}
