package giis.demo.model.tienda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Una franja de apertura de la tienda en un dia concreto
 */
public class FranjaTienda {
    private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM");
    private static final Locale ESPANOL = Locale.forLanguageTag("es-ES");

    private final LocalDate dia;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;

    public FranjaTienda(LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public LocalDate getDia() {
        return dia;
    }

    public LocalDateTime getInicio() {
        return dia.atTime(horaInicio);
    }

    public LocalDateTime getFin() {
        return dia.atTime(horaFin);
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * El inicio cuenta como dentro y el fin como fuera:
     */
    public boolean contiene(LocalDateTime momento) {
        return !momento.isBefore(getInicio()) && momento.isBefore(getFin());
    }

    /**
     * Por ejemplo "Jueves 19/11"
     */
    public String getNombreDia() {
        String nombre = dia.getDayOfWeek().getDisplayName(TextStyle.FULL, ESPANOL);
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1) + " " + dia.format(FORMATO_DIA);
    }

    /**
     * Por ejemplo "10:00 – 14:00"
     */
    public String getHoras() {
        return HorarioTienda.hora(horaInicio) + " – " + HorarioTienda.hora(horaFin);
    }

    @Override
    public String toString() {
        return getNombreDia() + ", " + getHoras();
    }
}