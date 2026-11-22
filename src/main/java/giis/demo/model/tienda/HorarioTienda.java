package giis.demo.model.tienda;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Horario de apertura de la tienda. Es horario teorico, la tienda puede vender fuera de el, pero dentro de este se
 * exige un minimo de vendedores.
 */
public final class HorarioTienda {

    public static final int MIN_VENDEDORES = 2;
    public static final String DESCRIPCION = "lunes a sábado, de 10:00 a 14:00 y de 17:00 a 20:00";

    private static final LocalTime[][] FRANJAS = { { LocalTime.of(10, 0), LocalTime.of(14, 0) },
            { LocalTime.of(17, 0), LocalTime.of(20, 0) } };
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final Locale ESPANOL = Locale.forLanguageTag("es-ES");

    private HorarioTienda() {
    }

    public static boolean abre(LocalDate dia) {
        return dia.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    /**
     * Franjas de apertura del dia, en orden; ninguna si es domingo
     */
    public static List<FranjaTienda> franjasDe(LocalDate dia) {
        List<FranjaTienda> franjas = new ArrayList<>();
        if (abre(dia)) {
            for (LocalTime[] franja : FRANJAS) {
                franjas.add(new FranjaTienda(dia, franja[0], franja[1]));
            }
        }
        return franjas;
    }

    /**
     * @return la franja de apertura que contiene el momento, o null si la tienda esta fuera de su horario
     */
    public static FranjaTienda franjaEn(LocalDateTime momento) {
        for (FranjaTienda franja : franjasDe(momento.toLocalDate())) {
            if (franja.contiene(momento)) {
                return franja;
            }
        }
        return null;
    }

    public static boolean estaAbierta(LocalDateTime momento) {
        return franjaEn(momento) != null;
    }

    /**
     * Inicio de la siguiente franja de apertura a partir del momento
     */
    public static LocalDateTime siguienteApertura(LocalDateTime momento) {
        for (int i = 0; i <= 7; i++) {
            for (FranjaTienda franja : franjasDe(momento.toLocalDate().plusDays(i))) {
                if (franja.getInicio().isAfter(momento)) {
                    return franja.getInicio();
                }
            }
        }
        throw new IllegalStateException("La tienda no abre ningún día");
    }

    /**
     * Cuando ocurre un momento visto desde otro
     */
    public static String cuando(LocalDateTime desde, LocalDateTime momento) {
        String hora = " a las " + momento.format(FORMATO_HORA);
        LocalDate dia = momento.toLocalDate();
        if (dia.equals(desde.toLocalDate())) {
            return "hoy" + hora;
        } else if (dia.equals(desde.toLocalDate().plusDays(1))) {
            return "mañana" + hora;
        }
        return "el " + dia.getDayOfWeek().getDisplayName(TextStyle.FULL, ESPANOL) + hora;
    }

    static String hora(LocalTime hora) {
        return hora.format(FORMATO_HORA);
    }
}