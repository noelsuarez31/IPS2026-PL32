package giis.demo.model.informe;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * Periodo (semana, mes o año) que cubre un informe: el que contiene la fecha de referencia. La semana va de lunes a
 * domingo y el mes y el año son naturales.
 */
public class PeriodoInforme {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale ESPANOL = Locale.forLanguageTag("es-ES");

    private final TipoPeriodo tipo;
    private final LocalDate desde;
    private final LocalDate hasta;

    private PeriodoInforme(TipoPeriodo tipo, LocalDate desde, LocalDate hasta) {
        this.tipo = tipo;
        this.desde = desde;
        this.hasta = hasta;
    }

    public static PeriodoInforme de(TipoPeriodo tipo, LocalDate referencia) {
        switch (tipo) {
        case SEMANA:
            LocalDate lunes = referencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            return new PeriodoInforme(tipo, lunes, lunes.plusDays(6));
        case MES:
            return new PeriodoInforme(tipo, referencia.with(TemporalAdjusters.firstDayOfMonth()),
                    referencia.with(TemporalAdjusters.lastDayOfMonth()));
        default:
            return new PeriodoInforme(tipo, referencia.with(TemporalAdjusters.firstDayOfYear()),
                    referencia.with(TemporalAdjusters.lastDayOfYear()));
        }
    }

    public TipoPeriodo getTipo() {
        return tipo;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    /**
     * Texto que se muestra al usuario, por ejemplo "Semana del 09/11/2026 al 15/11/2026", "Noviembre de 2026" o "Año
     * 2026"
     */
    public String getEtiqueta() {
        switch (tipo) {
        case SEMANA:
            return "Semana del " + desde.format(FORMATO_FECHA) + " al " + hasta.format(FORMATO_FECHA);
        case MES:
            String mes = desde.getMonth().getDisplayName(TextStyle.FULL, ESPANOL);
            return Character.toUpperCase(mes.charAt(0)) + mes.substring(1) + " de " + desde.getYear();
        default:
            return "Año " + desde.getYear();
        }
    }
}
