package giis.demo.model.accion;

/**
 * Campaña de venta de acciones del club. Una campaña esta activa hasta que se
 * cierra (manualmente en la fase 3 o automaticamente al agotarse las acciones).
 */
public class Campana {

    public static final String ACTIVA = "ACTIVA";
    public static final String CERRADA = "CERRADA";
    public static final int PRIMERA_FASE = 1;
    public static final int ULTIMA_FASE = 3;

    private int id;
    private String nombre;
    private int fase;
    private double precio;
    private int numAcciones;
    private int accionesVendidas;
    private String estado;

    public Campana(int id, String nombre, int fase, double precio, int numAcciones, int accionesVendidas,
            String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fase = fase;
        this.precio = precio;
        this.numAcciones = numAcciones;
        this.accionesVendidas = accionesVendidas;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFase() {
        return fase;
    }

    public double getPrecio() {
        return precio;
    }

    public int getNumAcciones() {
        return numAcciones;
    }

    public int getAccionesVendidas() {
        return accionesVendidas;
    }

    public int getAccionesDisponibles() {
        return numAcciones - accionesVendidas;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isActiva() {
        return ACTIVA.equals(estado);
    }
}