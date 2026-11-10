package giis.demo.model.accion;

import java.util.List;

/**
 * Resultado de una compra de acciones de campaña ya confirmada.
 */
public class ResultadoCompra {

    private final List<Integer> idsAcciones;
    private final double precioUnitario;
    private final double total;
    private final boolean campanaCerrada;

    public ResultadoCompra(List<Integer> idsAcciones, double precioUnitario, double total, boolean campanaCerrada) {
        this.idsAcciones = idsAcciones;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.campanaCerrada = campanaCerrada;
    }

    public List<Integer> getIdsAcciones() {
        return idsAcciones;
    }

    public int getUnidades() {
        return idsAcciones.size();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getTotal() {
        return total;
    }

    /** true si esta compra ha agotado las acciones y la campaña se ha cerrado sola */
    public boolean isCampanaCerrada() {
        return campanaCerrada;
    }

    /** Identificadores para mostrar al usuario: "21", "21 y 22", "21, 22 y 23" o "21 a 520" si son muchas */
    public String getIdsTexto() {
        int n = idsAcciones.size();
        if (n == 1)
            return String.valueOf(idsAcciones.get(0));
        if (n > 6)
            return idsAcciones.get(0) + " a " + idsAcciones.get(n - 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(idsAcciones.get(i));
        }
        return sb.append(" y ").append(idsAcciones.get(n - 1)).toString();
    }
}
