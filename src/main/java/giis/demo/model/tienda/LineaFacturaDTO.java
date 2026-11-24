package giis.demo.model.tienda;

public class LineaFacturaDTO {

	private final String producto;
	private final int unidades;
	private final double precioUnitario;
	private final double subtotal;

	public LineaFacturaDTO(String producto, int unidades, double precioUnitario, double subtotal) {
		this.producto = producto;
		this.unidades = unidades;
		this.precioUnitario = precioUnitario;
		this.subtotal = subtotal;
	}

	public LineaFacturaDTO(String producto, int unidades, double precioUnitario) {
		this(producto, unidades, precioUnitario, unidades * precioUnitario);
	}

	public String getProducto() {
		return producto;
	}

	public int getUnidades() {
		return unidades;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public double getSubtotal() {
		return subtotal;
	}
}
