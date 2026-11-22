package giis.demo.model.informe;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Periodo (semana, mes o año) que cubre un informe: el que contiene la fecha de referencia. La semana va de lunes a
 * domingo y el mes y el año son naturales.
 */
public class PeriodoInforme {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DIA_MES = DateTimeFormatter.ofPattern("d/M");
    private static final Locale ESPANOL = Locale.forLanguageTag("es-ES");
    /**
     * Un trozo de semana mas corto que esto, al principio o al final del mes, se suma a la semana de al lado
     */
    private static final int MIN_DIAS_SEMANA = 4;

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

    public long getDias() {
        return ChronoUnit.DAYS.between(desde, hasta) + 1;
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
            return nombreMes(desde) + " de " + desde.getYear();
        default:
            return "Año " + desde.getYear();
        }
    }

    /**
     * Tramos en los que se agrupan las barras de los graficos
     */
    public List<TramoInforme> dividir() {
        switch (tipo) {
        case ANIO:
            return meses();
        case MES:
            return semanasDelMes();
        default:
            return List.of(new TramoInforme(desde, hasta, rango(desde, hasta), getEtiqueta()));
        }
    }

    private List<TramoInforme> meses() {
        List<TramoInforme> meses = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            LocalDate inicio = desde.withMonth(mes);
            String nombre = nombreMes(inicio);
            meses.add(new TramoInforme(inicio, inicio.with(TemporalAdjusters.lastDayOfMonth()), nombre.substring(0, 3),
                    nombre));
        }
        return meses;
    }

    /**
     * Semanas de lunes a domingo recortadas al mes. Si la primera o la ultima se quedan con menos de MIN_DIAS_SEMANA,
     * se suman a la semana de al lado.
     */
    private List<TramoInforme> semanasDelMes() {
        List<LocalDate[]> semanas = new ArrayList<>();
        LocalDate inicio = desde;
        while (!inicio.isAfter(hasta)) {
            LocalDate domingo = inicio.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            LocalDate fin = domingo.isAfter(hasta) ? hasta : domingo;
            semanas.add(new LocalDate[] { inicio, fin });
            inicio = fin.plusDays(1);
        }
        if (semanas.size() > 1 && dias(semanas.get(0)) < MIN_DIAS_SEMANA) {
            semanas.get(1)[0] = semanas.remove(0)[0];
        }
        int ultima = semanas.size() - 1;
        if (ultima > 0 && dias(semanas.get(ultima)) < MIN_DIAS_SEMANA) {
            semanas.get(ultima - 1)[1] = semanas.remove(ultima)[1];
        }

        List<TramoInforme> tramos = new ArrayList<>();
        for (LocalDate[] semana : semanas) {
            tramos.add(new TramoInforme(semana[0], semana[1], rango(semana[0], semana[1]),
                    "Semana del " + semana[0].format(FORMATO_DIA_MES) + " al " + semana[1].format(FORMATO_DIA_MES)));
        }
        return tramos;
    }

    private static long dias(LocalDate[] semana) {
        return ChronoUnit.DAYS.between(semana[0], semana[1]) + 1;
    }

    private static String rango(LocalDate inicio, LocalDate fin) {
        return inicio.format(FORMATO_DIA_MES) + " – " + fin.format(FORMATO_DIA_MES);
    }

    private static String nombreMes(LocalDate fecha) {
        String mes = fecha.getMonth().getDisplayName(TextStyle.FULL, ESPANOL);
        return Character.toUpperCase(mes.charAt(0)) + mes.substring(1);
    }
}