package giis.demo.jdbc.accion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import giis.demo.model.accion.Campana;

public class CampanaJdbc {

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Campaña activa
    private static final String QUERY_GET_CAMPANA_ACTIVA = 
            "SELECT id_campana, nombre, fase, precio_emision, num_acciones, acciones_vendidas, estado "
            + "FROM Campana WHERE estado = 'ACTIVA'";

    // Nueva campaña en fase 1; el nombre se genera: "Campaña N"
    private static final String INSERT_CAMPANA = 
            "INSERT INTO Campana (nombre, fase, precio_emision, num_acciones, acciones_vendidas, estado, fecha_lanzamiento) "
            + "VALUES ('Campaña ' || (SELECT COUNT(*) + 1 FROM Campana), 1, ?, ?, 0, 'ACTIVA', ?)";

    private static final String QUERY_ULTIMO_ID = "SELECT last_insert_rowid()";

    // Foto de las acciones que posee cada accionista en el momento del lanzamiento
    private static final String INSERT_ACCIONES_INICIALES = 
            "INSERT INTO CampanaAccionista (id_campana, dni, acciones_iniciales, acciones_compradas) "
            + "SELECT ?, ac.dni, COUNT(a.id_accion), 0 "
            + "FROM Accionista ac LEFT JOIN Accion a ON a.dni_propietario = ac.dni "
            + "GROUP BY ac.dni";
    
    // Avanzar a la siguiente fase: solo si la campaña sigue activa, esta en la fase que se
    // esperaba, no es la ultima y quedan acciones por vender
    private static final String UPDATE_AVANZAR_FASE = 
            "UPDATE Campana SET fase = fase + 1 "
            + "WHERE id_campana = ? AND estado = 'ACTIVA' AND fase = ? AND fase < " + Campana.ULTIMA_FASE
            + " AND acciones_vendidas < num_acciones";

    // Cerrar la campaña: solo si sigue activa y esta en la ultima fase
    private static final String UPDATE_CERRAR_CAMPANA = 
            "UPDATE Campana SET estado = 'CERRADA' "
            + "WHERE id_campana = ? AND estado = 'ACTIVA' AND fase = " + Campana.ULTIMA_FASE;

    /**
     * Devuelve la campaña activa o null si no hay ninguna.
     */
    public static Campana getCampanaActiva(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(QUERY_GET_CAMPANA_ACTIVA);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Campana(rs.getInt("id_campana"), rs.getString("nombre"), rs.getInt("fase"),
                        rs.getDouble("precio_emision"), rs.getInt("num_acciones"), rs.getInt("acciones_vendidas"),
                        rs.getString("estado"));
            }
        }
        return null;
    }

    /**
     * Inserta una campaña nueva en fase 1 y devuelve su id.
     */
    public static int insertarCampana(Connection con, double precio, int numAcciones, LocalDateTime fechaLanzamiento)
            throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_CAMPANA)) {
            ps.setDouble(1, precio);
            ps.setInt(2, numAcciones);
            ps.setString(3, fechaLanzamiento.format(FORMATO_FECHA_HORA));
            ps.executeUpdate();
        }
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(QUERY_ULTIMO_ID)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    /**
     * Guarda, para la campaña indicada, cuantas acciones posee cada accionista en
     * este momento (incluidas las que tiene puestas a la venta).
     */
    public static void insertarAccionesIniciales(Connection con, int idCampana) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_ACCIONES_INICIALES)) {
            ps.setInt(1, idCampana);
            ps.executeUpdate();
        }
    }
    
    /**
     * Pasa la campaña a la fase siguiente.
     * 
     * @param faseActual fase en la que se cree que esta la campaña; evita avanzar
     *                   dos veces si alguien la ha cambiado mientras tanto
     * @return true si se ha avanzado de fase, false si la campaña ya no cumplia las
     *         condiciones (ha cambiado, esta cerrada o no se puede avanzar)
     */
    public static boolean avanzarFase(Connection con, int idCampana, int faseActual) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_AVANZAR_FASE)) {
            ps.setInt(1, idCampana);
            ps.setInt(2, faseActual);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Cierra la campaña. Las acciones no vendidas no se crean: simplemente dejan de
     * ofrecerse.
     * 
     * @return true si se ha cerrado, false si ya no estaba activa o no estaba en la
     *         ultima fase
     */
    public static boolean cerrarCampana(Connection con, int idCampana) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_CERRAR_CAMPANA)) {
            ps.setInt(1, idCampana);
            return ps.executeUpdate() == 1;
        }
    }
}