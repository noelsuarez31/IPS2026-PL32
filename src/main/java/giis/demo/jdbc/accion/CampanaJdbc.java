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

    // Campana activa
    private static final String QUERY_GET_CAMPANA_ACTIVA = 
            "SELECT id_campana, nombre, fase, precio_emision, num_acciones, acciones_vendidas, estado "
            + "FROM Campana WHERE estado = 'ACTIVA'";

    // Nueva campana en fase 1; el nombre se genera: "Campaña N"
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
}