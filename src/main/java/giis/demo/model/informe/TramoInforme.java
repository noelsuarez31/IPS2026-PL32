package giis.demo.model.informe;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Parte de un periodo del informe que se muestra como un grupo de barras en los graficos: un mes del año, una semana
 * del mes o la semana entera.
 */
public class TramoInforme {
    private final LocalDate desde;
    private final LocalDate hasta;
    private final String etiqueta;
    private final String descripcion;

    /**
     * @param etiqueta    texto corto bajo el grupo de barras (1/11 - 08/11)
     * @param descripcion texto completo para el detalle al pasar el raton (Semana del 1/11 al 8/11)
     */
    public TramoInforme(LocalDate desde, LocalDate hasta, String etiqueta, String descripcion) {
        this.desde = desde;
        this.hasta = hasta;
        this.etiqueta = etiqueta;
        this.descripcion = descripcion;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getDias() {
        return ChronoUnit.DAYS.between(desde, hasta) + 1;
    }

    public boolean contiene(LocalDate fecha) {
        return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }
}