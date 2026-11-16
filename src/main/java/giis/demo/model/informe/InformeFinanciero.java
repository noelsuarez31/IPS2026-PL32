package giis.demo.model.informe;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import giis.demo.model.historial.TipoIngreso;

/**
 * Ingresos, gastos y balance del club en un periodo. Los gastos son los salarios de los empleados y no se desglosan en
 * nada más.
 */
public class InformeFinanciero {
    private final PeriodoInforme periodo;
    private final Map<TipoIngreso, Double> ingresosPorTipo = new EnumMap<>(TipoIngreso.class);
    private final double totalIngresos;
    private final int numEmpleados;
    private final double gastos;

    /**
     * @param ingresos      importe de cada tipo de ingreso; los tipos que falten cuentan como 0
     * @param salariosAnual suma de los salarios anuales brutos de todos los empleados
     */
    public InformeFinanciero(PeriodoInforme periodo, Map<TipoIngreso, Double> ingresos, int numEmpleados, double salariosAnual) {
        this.periodo = periodo;
        double total = 0.0;
        for (TipoIngreso tipo : TipoIngreso.values()) {
            double importe = redondear(ingresos.getOrDefault(tipo, 0.0));
            ingresosPorTipo.put(tipo, importe);
            total += importe;
        }
        this.totalIngresos = redondear(total);
        this.numEmpleados = numEmpleados;
        this.gastos = redondear(salariosAnual / periodo.getTipo().getPeriodosPorAnio());
    }

    public PeriodoInforme getPeriodo() {
        return periodo;
    }

    public Map<TipoIngreso, Double> getIngresosPorTipo() {
        return Collections.unmodifiableMap(ingresosPorTipo);
    }

    public double getIngreso(TipoIngreso tipo) {
        return ingresosPorTipo.get(tipo);
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public int getNumEmpleados() {
        return numEmpleados;
    }

    public double getGastos() {
        return gastos;
    }

    public double getBalance() {
        return redondear(totalIngresos - gastos);
    }

    public boolean tieneIngresos() {
        return totalIngresos > 0;
    }

    private static double redondear(double importe) {
        return Math.round(importe * 100.0) / 100.0;
    }
}
