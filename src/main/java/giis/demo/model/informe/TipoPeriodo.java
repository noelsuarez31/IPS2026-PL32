package giis.demo.model.informe;


/**
 * Tipos de periodo por los que se puede filtrar el informe financiero.
 */
public enum TipoPeriodo {
    SEMANA("Semana", 52),
    MES("Mes", 12),
    ANIO("Año", 1);

    private final String nombre;
    private final int periodosPorAnio;

    private TipoPeriodo(String nombre, int periodosPorAnio) {
        this.nombre = nombre;
        this.periodosPorAnio = periodosPorAnio;
    }

    public int getPeriodosPorAnio() {
        return periodosPorAnio;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
