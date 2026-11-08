package giis.demo.model.accion;

import java.math.BigDecimal;

/**
 * Objeto de transferencia de datos (DTO) para la visualización tabular de las
 * acciones de un accionista en el portal y de las acciones en venta en el mercado.
 */
public class Accion {

	private int idAccion;
	private String fechaAdquisicion;
	private String origen;
	private BigDecimal precioPagado;
	private boolean enVenta;
	private BigDecimal precioVenta;
	private String dniVendedor;
	private String nombreVendedor;

	public Accion() {
	}

	public Accion(int idAccion, String fechaAdquisicion, String origen, BigDecimal precioPagado, boolean enVenta,
			BigDecimal precioVenta) {
		this(idAccion, fechaAdquisicion, origen, precioPagado, enVenta, precioVenta, null, null);
	}

	public Accion(int idAccion, String fechaAdquisicion, String origen, BigDecimal precioPagado, boolean enVenta,
			BigDecimal precioVenta, String dniVendedor, String nombreVendedor) {
		this.idAccion = idAccion;
		this.fechaAdquisicion = fechaAdquisicion;
		this.origen = origen;
		this.precioPagado = precioPagado;
		this.enVenta = enVenta;
		this.precioVenta = precioVenta;
		this.dniVendedor = dniVendedor;
		this.nombreVendedor = nombreVendedor;
	}

	public int getIdAccion() {
		return idAccion;
	}

	public void setIdAccion(int idAccion) {
		this.idAccion = idAccion;
	}

	public String getFechaAdquisicion() {
		return fechaAdquisicion;
	}

	public void setFechaAdquisicion(String fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public BigDecimal getPrecioPagado() {
		return precioPagado;
	}

	public void setPrecioPagado(BigDecimal precioPagado) {
		this.precioPagado = precioPagado;
	}

	public boolean isEnVenta() {
		return enVenta;
	}

	public void setEnVenta(boolean enVenta) {
		this.enVenta = enVenta;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public String getDniVendedor() {
		return dniVendedor;
	}

	public void setDniVendedor(String dniVendedor) {
		this.dniVendedor = dniVendedor;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}
}
