package giis.demo.model.tienda;

import java.util.ArrayList;
import java.util.List;

public class Cesta {
	
	private List<Producto> productos = new ArrayList<>();

    public void añadirProducto(Producto p) {
        productos.add(p);
    }

    public void eliminarProducto(Producto p) {
        productos.remove(p);
    }

    public void vaciar() {
        productos.clear();
    }

    public double getTotal() {
        double total = 0.0;
        for (Producto p : productos) {
            total += p.getPrecio();
        }
        return total;
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

}
