package giis.demo.model.tienda;

import java.util.ArrayList;
import java.util.List;

import giis.demo.model.tienda.Producto.Categoria;

public class Venta {
	
	private Cesta cesta;
    private List<Producto> catalogo;

    public Venta() {
        this.cesta = new Cesta();
        this.catalogo = new ArrayList<>();
        cargarCatalogoSimulado(); // Cargar base de datos
    }

    private void cargarCatalogoSimulado() {
        catalogo.add(new Producto("P1", "Camiseta", 15.99, Categoria.ParteArriba));
        catalogo.add(new Producto("P2", "Pantalón", 29.99, Categoria.ParteAbajo));
        catalogo.add(new Producto("P3", "Taza", 9.50, Categoria.Merchandising));
        catalogo.add(new Producto("P4", "Gorra", 12.00, Categoria.Merchandising));
    }

    public List<Producto> getCatalogo() {
        return catalogo;
    }

    public Cesta getCesta() {
        return cesta;
    }

    public Producto getProductoPorPosicion(int index) {
        if (index >= 0 && index < catalogo.size()) {
            return catalogo.get(index);
        }
        return null;
    }

    public boolean cumpleFiltro(int posicionCatalogo, String categoriaFiltro) {
        if (categoriaFiltro.equals("Todos")) return true;
        return catalogo.get(posicionCatalogo).getCategoria().equals(categoriaFiltro);
    }

}
