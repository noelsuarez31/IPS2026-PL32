package giis.demo.model.informe;

import java.util.EnumMap;
import java.util.Map;

import giis.demo.model.historial.TipoIngreso;

/**
 * Ingresos por tipo y gastos de un tramo del periodo, lo que se dibuja en cada grupo de barras
 */
public class GrupoInforme {
    private final TramoInforme tramo;
    private final Map<TipoIngreso, Double> ingresosPorTipo = new EnumMap<>(TipoIngreso.class);
    private final double totalIngresos;
    private final double gastos;

    /**
     * @param ingresos importe de cada tipo de ingreso en el tramo; los tipos que falten cuentan como 0
     * @param gastos   parte de los salarios que le corresponde al tramo
     */
    public GrupoInforme(TramoInforme tramo, Map<TipoIngreso, Double> ingresos, double gastos) {
        this.tramo = tramo;
        double total = 0.0;
        for (TipoIngreso tipo : TipoIngreso.values()) {
            double importe = InformeFinanciero.redondear(ingresos.getOrDefault(tipo, 0.0));
            ingresosPorTipo.put(tipo, importe);
            total += importe;
        }
        this.totalIngresos = InformeFinanciero.redondear(total);
        this.gastos = InformeFinanciero.redondear(gastos);
    }

    public TramoInforme getTramo() {
        return tramo;
    }

    public double getIngreso(TipoIngreso tipo) {
        return ingresosPorTipo.get(tipo);
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public double getGastos() {
        return gastos;
    }

    public double getBalance() {
        return InformeFinanciero.redondear(totalIngresos - gastos);
    }
}