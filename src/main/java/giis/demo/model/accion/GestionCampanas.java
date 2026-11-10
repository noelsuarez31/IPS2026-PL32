package giis.demo.model.accion;

import java.sql.SQLException;

import giis.demo.jdbc.JDBC;
import giis.demo.util.ApplicationException;

/**
 * Logica de negocio de las campañas de venta de acciones (gerente).
 */
public class GestionCampanas {

    public static final String MSG_DATOS_INVALIDOS = "El número de acciones y el precio por acción deben ser mayores que cero.";
    public static final String MSG_CAMPANA_CAMBIADA = "La campaña ha cambiado desde que se abrió esta ventana.\nSe ha actualizado la información mostrada.";

    private JDBC jdbc = new JDBC();

    /**
     * Devuelve la campaña activa o null si no hay ninguna.
     */
    public Campana obtenerCampanaActiva() throws SQLException {
        return jdbc.getCampanaActiva();
    }

    /**
     * Lanza una campaña nueva a partir de los textos introducidos por el gerente.
     * 
     * @throws ApplicationException si el numero de acciones o el precio no son numeros mayores que cero, o si ya hay
     *                              una campaña activa
     */
    public void lanzarCampana(String numAccionesTexto, String precioTexto) throws SQLException {
        int numAcciones = parsearNumAcciones(numAccionesTexto);
        double precio = parsearPrecio(precioTexto);
        jdbc.lanzarCampana(precio, numAcciones);
    }
    
    /**
     * Avanza la campaña mostrada a la fase siguiente (sin posibilidad de retroceder).
     * 
     * @param campana la campaña tal y como la ve el gerente en pantalla
     * @throws ApplicationException si ya esta en la ultima fase, o si la campaña ha
     *                              cambiado desde que se mostro
     */
    public void avanzarFase(Campana campana) throws SQLException {
        if (campana.getFase() >= Campana.ULTIMA_FASE)
            throw new ApplicationException("La campaña ya está en la última fase.");
        if (!jdbc.avanzarFaseCampana(campana.getId(), campana.getFase()))
            throw new ApplicationException(MSG_CAMPANA_CAMBIADA);
    }

    /**
     * Cierra la campaña mostrada. Solo se puede en la ultima fase; las acciones no
     * vendidas no se crean.
     * 
     * @param campana la campaña tal y como la ve el gerente en pantalla
     * @throws ApplicationException si no esta en la ultima fase, o si la campaña ha
     *                              cambiado desde que se mostro
     */
    public void cerrarCampana(Campana campana) throws SQLException {
        if (campana.getFase() != Campana.ULTIMA_FASE)
            throw new ApplicationException("Solo se puede cerrar la campaña en la última fase (fase "
                    + Campana.ULTIMA_FASE + ").");
        if (!jdbc.cerrarCampana(campana.getId()))
            throw new ApplicationException(MSG_CAMPANA_CAMBIADA);
    }

    /** Entero mayor que cero; si no, ApplicationException. */
    public static int parsearNumAcciones(String texto) {
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor > 0)
                return valor;
        } catch (NumberFormatException | NullPointerException e) {
        }
        throw new ApplicationException(MSG_DATOS_INVALIDOS);
    }

    /** Numero mayor que cero (admite coma o punto decimal); si no, ApplicationException. */
    public static double parsearPrecio(String texto) {
        try {
            double valor = Double.parseDouble(texto.trim().replace(',', '.'));
            if (valor > 0 && Double.isFinite(valor))
                return valor;
        } catch (NumberFormatException | NullPointerException e) {
        }
        throw new ApplicationException(MSG_DATOS_INVALIDOS);
    }
}