package giis.demo.model.tienda;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.horarios.GestionHorarioSemanal;
import giis.demo.model.horarios.HorarioSemanal;

/**
 * Comprueba si hay suficientes vendedores de tienda (empleados de tienda) dentro del horario de apertura, segun los
 * horarios asignados. Usa los turnos de GestionHorarioSemanal, donde un horario especifico sustituye al periodico de
 * ese dia.
 */
public class GestionVendedores {

    private final GestionHorarioSemanal horarios;

    public GestionVendedores(GestionHorarioSemanal horarios) {
        this.horarios = horarios;
    }

    /**
     * Comprueba los vendedores que hay en el momento dado y en las franjas que quedan de esa semana
     */
    public AvisoVendedores comprobar(LocalDateTime ahora) throws SQLException {
        List<List<Turno>> turnosPorVendedor = new ArrayList<>();
        for (EmpleadoNoDeportivo empleado : horarios.obtenerEmpleadosNoDeportivos()) {
            if (empleado.getPosicion() == Posicion.EMPLEADO_DE_TIENDA) {
                turnosPorVendedor.add(
                        Turno.de(horarios.obtenerHorariosDeSemana(empleado.getDni(), ahora.toLocalDate().toString())));
            }
        }
        return calcular(ahora, turnosPorVendedor);
    }

    /**
     * Calcula el aviso a partir de los turnos de cada vendedor. No accede a la BBDD
     */
    public static AvisoVendedores calcular(LocalDateTime ahora, Collection<List<Turno>> turnosPorVendedor) {
        List<FranjaSinCubrir> sinCubrir = new ArrayList<>();
        LocalDate sabado = lunesDe(ahora.toLocalDate()).plusDays(5);
        for (LocalDate dia = ahora.toLocalDate(); !dia.isAfter(sabado); dia = dia.plusDays(1)) {
            for (FranjaTienda franja : HorarioTienda.franjasDe(dia)) {
                if (franja.getFin().isAfter(ahora)) {
                    // de la franja en curso solo cuenta lo que queda
                    LocalDateTime desde = franja.getInicio().isBefore(ahora) ? ahora : franja.getInicio();
                    int vendedores = minimoVendedores(desde, franja.getFin(), turnosPorVendedor);
                    if (vendedores < HorarioTienda.MIN_VENDEDORES) {
                        sinCubrir.add(new FranjaSinCubrir(franja, vendedores, franja.contiene(ahora)));
                    }
                }
            }
        }
        return new AvisoVendedores(ahora, HorarioTienda.franjaEn(ahora), vendedoresEn(ahora, turnosPorVendedor),
                sinCubrir);
    }

    /**
     * Menor numero de vendedores trabajando a la vez entre dos momentos. Se mira cada tramo entre los inicios y finales
     * de turno, porque dentro de cada tramo no cambia quien trabaja.
     */
    static int minimoVendedores(LocalDateTime desde, LocalDateTime hasta, Collection<List<Turno>> turnosPorVendedor) {
        TreeSet<LocalDateTime> cortes = new TreeSet<>(List.of(desde, hasta));
        for (List<Turno> turnos : turnosPorVendedor) {
            for (Turno turno : turnos) {
                if (turno.inicio().isAfter(desde) && turno.inicio().isBefore(hasta)) {
                    cortes.add(turno.inicio());
                }
                if (turno.fin().isAfter(desde) && turno.fin().isBefore(hasta)) {
                    cortes.add(turno.fin());
                }
            }
        }
        int minimo = Integer.MAX_VALUE;
        LocalDateTime inicioTramo = null;
        for (LocalDateTime corte : cortes) {
            if (inicioTramo != null) {
                minimo = Math.min(minimo, vendedoresEnTramo(inicioTramo, corte, turnosPorVendedor));
            }
            inicioTramo = corte;
        }
        return minimo == Integer.MAX_VALUE ? 0 : minimo;
    }

    private static int vendedoresEnTramo(LocalDateTime desde, LocalDateTime hasta,
            Collection<List<Turno>> turnosPorVendedor) {
        int vendedores = 0;
        for (List<Turno> turnos : turnosPorVendedor) {
            for (Turno turno : turnos) {
                if (!turno.inicio().isAfter(desde) && !turno.fin().isBefore(hasta)) {
                    vendedores++;
                    break;
                }
            }
        }
        return vendedores;
    }

    private static int vendedoresEn(LocalDateTime momento, Collection<List<Turno>> turnosPorVendedor) {
        int vendedores = 0;
        for (List<Turno> turnos : turnosPorVendedor) {
            for (Turno turno : turnos) {
                if (!momento.isBefore(turno.inicio()) && momento.isBefore(turno.fin())) {
                    vendedores++;
                    break;
                }
            }
        }
        return vendedores;
    }

    static LocalDate lunesDe(LocalDate dia) {
        return dia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Un turno de trabajo de un vendedor en un dia concreto
     */
    public record Turno(LocalDateTime inicio, LocalDateTime fin) {

        static List<Turno> de(List<HorarioSemanal> horarios) {
            List<Turno> turnos = new ArrayList<>();
            for (HorarioSemanal h : horarios) {
                LocalDate dia = LocalDate.parse(h.getFecha());
                turnos.add(new Turno(dia.atTime(LocalTime.parse(h.getHoraInicio())),
                        dia.atTime(LocalTime.parse(h.getHoraFin()))));
            }
            return turnos;
        }
    }
}