package giis.demo.model.tienda;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cesta {
    private Map<Producto, Integer> lineas = new LinkedHashMap<>();

    public void añadirProducto(Producto p) {
        lineas.put(p, lineas.getOrDefault(p, 0) + 1);
    }

    public void eliminarProducto(Producto p) {
        lineas.remove(p);
    }

    public void vaciar() {
        lineas.clear();
    }

    public double getTotal() {
        double total = 0.0;
        for (Map.Entry<Producto, Integer> entry : lineas.entrySet()) {
            total += entry.getKey().getPrecio() * entry.getValue();
        }
        return total;
    }

    public int getNumProductosDistintos() {
        return lineas.size();
    }

    public int getTotalUnidades() {
        int total = 0;
        for (int cantidad : lineas.values()) {
            total += cantidad;
        }
        return total;
    }

    public Map<Producto, Integer> getLineas() {
        return Collections.unmodifiableMap(lineas);
    }

    public boolean isVacia() {
        return lineas.isEmpty();
    }
}
