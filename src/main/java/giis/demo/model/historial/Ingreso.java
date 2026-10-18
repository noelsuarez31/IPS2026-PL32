package giis.demo.model.historial;

import java.time.LocalDateTime;

/**
 * Operacion en la que se ha producido un ingreso
 */
public class Ingreso {
	private final int idOperacion; // id_venta o id_reserva, segun el tipo
	private final TipoIngreso tipo;
	private final LocalDateTime fecha;
	private final double importe;

	public Ingreso(int idOperacion, TipoIngreso tipo, LocalDateTime fecha, double importe) {
		this.idOperacion = idOperacion;
		this.tipo = tipo;
		this.fecha = fecha;
		this.importe = importe;
	}

	public int getIdOperacion() {
		return idOperacion;
	}

	public TipoIngreso getTipo() {
		return tipo;
	}

	public String getConcepto() {
		return tipo.getConcepto();
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public double getImporte() {
		return importe;
	}

	public boolean esMerchandising() {
		return tipo == TipoIngreso.MERCHANDISING;
	}
}
