package giis.demo.model.informe;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
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
     * Suma los ingresos por tipo y los salarios anuales de los empleados. No accede a la BBDD
     */
    public static InformeFinanciero calcular(PeriodoInforme periodo, List<Ingreso> ingresos,
            List<BaseEmpleado> empleados) {
        Map<TipoIngreso, Double> porTipo = new EnumMap<>(TipoIngreso.class);
        for (Ingreso ingreso : ingresos) {
            porTipo.merge(ingreso.getTipo(), ingreso.getImporte(), Double::sum);
        }
        BigDecimal salarios = BigDecimal.ZERO;
        for (BaseEmpleado empleado : empleados) {
            salarios = salarios.add(empleado.getSalario());
        }
        return new InformeFinanciero(periodo, porTipo, empleados.size(), salarios.doubleValue());
    }

    public void close() throws SQLException {
        jdbc.close();
    }
}
