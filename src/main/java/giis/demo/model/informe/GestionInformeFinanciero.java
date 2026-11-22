package giis.demo.model.informe;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import giis.demo.exceptions.InformeFinancieroException;
import giis.demo.jdbc.JDBC;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.historial.Ingreso;
import giis.demo.model.historial.TipoIngreso;

public class GestionInformeFinanciero {

    public static final String MSG_FECHA_VACIA = "Seleccione una fecha de referencia para el periodo.";
    public static final String MSG_ERROR_DATOS = "No se han podido obtener los datos del informe. Inténtelo de nuevo.";

    private JDBC jdbc = new JDBC();

    /**
     * Genera el informe del periodo (semana, mes o año) que contiene la fecha de referencia. Los ingresos son los
     * mismos que muestra el historial de ventas.
     * 
     * @throws InformeFinancieroException si falta la fecha o falla la BBDD
     */
    public InformeFinanciero generarInforme(TipoPeriodo tipo, LocalDate referencia) {
        if (tipo == null || referencia == null) {
            throw new InformeFinancieroException(MSG_FECHA_VACIA);
        }
        PeriodoInforme periodo = PeriodoInforme.de(tipo, referencia);
        try {
            List<Ingreso> ingresos = jdbc.getIngresos(periodo.getDesde(), periodo.getHasta());
            List<BaseEmpleado> empleados = jdbc.getEmpleados();
            return calcular(periodo, ingresos, empleados);
        } catch (SQLException e) {
            throw new InformeFinancieroException(MSG_ERROR_DATOS);
        }
    }

    /**
     * Suma los ingresos por tipo y los salarios anuales de los empleados, en total y por cada tramo del periodo. No
     * accede a la BBDD
     */
    public static InformeFinanciero calcular(PeriodoInforme periodo, List<Ingreso> ingresos,
            List<BaseEmpleado> empleados) {
        double salarios = sumarSalarios(empleados);
        List<GrupoInforme> grupos = new ArrayList<>();
        for (TramoInforme tramo : periodo.dividir()) {
            grupos.add(new GrupoInforme(tramo, sumarPorTipo(ingresos, tramo), gastosDelTramo(periodo, tramo, salarios)));
        }
        return new InformeFinanciero(periodo, sumarPorTipo(ingresos, null), empleados.size(), salarios, grupos);
    }

    /**
     * Parte de los salarios que le toca a un tramo. Los meses del año cuentan todos igual (la doceava parte); dentro
     * de un mes o de una semana se reparte por dias, para que la suma de los tramos sea el gasto del periodo.
     */
    static double gastosDelTramo(PeriodoInforme periodo, TramoInforme tramo, double salariosAnual) {
        double gastosPeriodo = salariosAnual / periodo.getTipo().getPeriodosPorAnio();
        if (periodo.getTipo() == TipoPeriodo.ANIO) {
            return gastosPeriodo / 12;
        }
        return gastosPeriodo * tramo.getDias() / periodo.getDias();
    }

    /**
     * @param tramo solo se suman los ingresos de sus dias; null para sumarlos todos
     */
    private static Map<TipoIngreso, Double> sumarPorTipo(List<Ingreso> ingresos, TramoInforme tramo) {
        Map<TipoIngreso, Double> porTipo = new EnumMap<>(TipoIngreso.class);
        for (Ingreso ingreso : ingresos) {
            if (tramo == null || tramo.contiene(ingreso.getFecha().toLocalDate())) {
                porTipo.merge(ingreso.getTipo(), ingreso.getImporte(), Double::sum);
            }
        }
        return porTipo;
    }

    private static double sumarSalarios(List<BaseEmpleado> empleados) {
        BigDecimal salarios = BigDecimal.ZERO;
        for (BaseEmpleado empleado : empleados) {
            salarios = salarios.add(empleado.getSalario());
        }
        return salarios.doubleValue();
    }

    public void close() throws SQLException {
        jdbc.close();
    }
}