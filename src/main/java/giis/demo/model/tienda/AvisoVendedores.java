package giis.demo.model.tienda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resultado de comprobar los vendedores de la tienda en un momento: cuantos trabajan ahora y que franjas de lo que
 * queda de semana no tienen el minimo
 */
public class AvisoVendedores {
    private final LocalDateTime momento;
    private final FranjaTienda franjaActual;
    private final int vendedoresAhora;
    private final List<FranjaSinCubrir> franjasSinCubrir;

    /**
     * @param franjaActual franja de apertura en la que cae el momento, o null si esta fuera del horario
     */
    public AvisoVendedores(LocalDateTime momento, FranjaTienda franjaActual, int vendedoresAhora,
            List<FranjaSinCubrir> franjasSinCubrir) {
        this.momento = momento;
        this.franjaActual = franjaActual;
        this.vendedoresAhora = vendedoresAhora;
        this.franjasSinCubrir = List.copyOf(franjasSinCubrir);
    }

    /**
     * Hay que avisar si faltan vendedores ahora (dentro del horario) o en alguna franja de lo que queda de semana
     */
    public boolean hayAviso() {
        return faltanAhora() > 0 || !franjasSinCubrir.isEmpty();
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public boolean isEnHorario() {
        return franjaActual != null;
    }

    public FranjaTienda getFranjaActual() {
        return franjaActual;
    }

    public int getVendedoresAhora() {
        return vendedoresAhora;
    }

    /**
     * Vendedores que faltan ahora mismo; 0 fuera del horario, donde no se exige minimo
     */
    public int faltanAhora() {
        return isEnHorario() ? Math.max(0, HorarioTienda.MIN_VENDEDORES - vendedoresAhora) : 0;
    }

    public List<FranjaSinCubrir> getFranjasSinCubrir() {
        return franjasSinCubrir;
    }

    public LocalDate getLunes() {
        return GestionVendedores.lunesDe(momento.toLocalDate());
    }

    public LocalDate getSabado() {
        return getLunes().plusDays(5);
    }
}