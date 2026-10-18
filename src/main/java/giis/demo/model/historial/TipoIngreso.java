package giis.demo.model.historial;

/**
 * Conceptos por los que la organizacion puede obtener ingresos
 */
public enum TipoIngreso {
	MERCHANDISING("Merchandising"),
	ENTRADAS("Entradas"),
	ABONOS("Abonos de temporada"),
	RESERVA_INSTALACIONES("Reserva de instalaciones deportivas"),
	CAMPANAS_ACCIONISTAS("Campañas de accionistas");

	private final String concepto;

	private TipoIngreso(String concepto) {
		this.concepto = concepto;
	}

	public String getConcepto() {
		return concepto;
	}

	@Override
	public String toString() {
		return concepto;
	}
}
