package giis.demo.jdbc.empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.model.empleado.Entrenador;

public class EntrenadorJdbc {
	public final static String QUERY_GET_MANAGERS = "SELECT * FROM EmpleadoDeportivo WHERE posicion = 'ENTRENADOR' AND id_equipo IS NULL";
    public final static String QUERY_GET_COMPLETE_MANAGERS = "SELECT nombre, apellido, salario, fecha_nacimiento, numero_de_telefono FROM"
            + " BaseEmpleado be, EmpleadoDeportivo ed WHERE ed.dni=be.dni and ed.dni=?";
    
    public static List<Entrenador> obtenerEntrenadoresDisponibles(Connection con) throws SQLException {
        List<Entrenador> cuerpoTecnico = new ArrayList<Entrenador>();
        
        try(PreparedStatement pst = con.prepareStatement(QUERY_GET_MANAGERS)){
            ResultSet rsEntrenador = pst.executeQuery();
            
            while(rsEntrenador.next()) {
                String dniEntrenador = rsEntrenador.getString(1); // dni
                
                try(PreparedStatement pst2 = con.prepareStatement(QUERY_GET_COMPLETE_MANAGERS)){
                    pst2.setString(1, dniEntrenador);
                    ResultSet rsEntrenadorCompleto = pst2.executeQuery();
                    
                    while(rsEntrenadorCompleto.next()) {
                        Entrenador entrenador = new Entrenador(
                                dniEntrenador, 
                                rsEntrenadorCompleto.getString(1), 
                                rsEntrenadorCompleto.getString(2),
                                rsEntrenadorCompleto.getBigDecimal(3), 
                                LocalDate.parse(rsEntrenadorCompleto.getString(4)),
                                rsEntrenadorCompleto.getString(5));
                        cuerpoTecnico.add(entrenador);
                    }
                }
            }
        }
        return cuerpoTecnico;
    }
}
