package giis.demo.model.entradas;

import java.util.Date;

import giis.demo.model.enumerados.TipoVenta;

public class Compra {
	
	private int id_venta;
	private Date fecha;
	private String concepto;
	private TipoVenta tipo;
	private double total;

	/**
	 * Constructor de la clase por defecto
	 * @param id_Compra
	 * @param fecha
	 * @param concepto
	 * @param tipo
	 * @param total
	 */
	public Compra(int id_Compra, Date fecha, String concepto, TipoVenta tipo, double total) {
		
		this.id_venta = id_Compra;
		this.fecha = fecha;
		this.concepto = concepto;
		this.tipo = tipo;
		this.total = total;
	}

	/**
	 * Devuelve el id de la compra
	 * @return el id de la compra
	 */
	public int getId_Compra() {
		return id_venta;
	}

	/**
	 * Devuelve la fecha de de la venta
	 * @return
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Devuelve el concepto de la venta
	 * @return el concepto de la venta
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * Devuelve el tipo de Venta de la compra
	 * @return el tipo de Venta de la compra
	 */
	public TipoVenta getTipo() {
		return tipo;
	}

	/**
	 * Devuelve el total
	 * @return el total
	 */
	public double getTotal() {
		return total;
	}
	
	
	

}
