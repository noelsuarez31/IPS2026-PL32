package giis.demo.model.login;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import giis.demo.jdbc.JDBC;
import giis.demo.model.accion.Accionista;
import giis.demo.model.empleado.BaseEmpleado;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.rol.UsuarioAutenticado;
import giis.demo.util.ApplicationException;
import giis.demo.util.UnexpectedException;

public class Autenticacion {

	private JDBC jdbc;

	public Autenticacion() {

		jdbc = new JDBC();

	}

	/**
	 * Autentica a un empleado
	 * 
	 * @param usuario    nombre de usuario
	 * @param contrasena contraseña en claro
	 * @return el dni del empleado autenticado
	 * @throws ApplicationException si los campos estan vacios o las credenciales no
	 *                              son validas (mismo mensaje si falla el usuario o
	 *                              la contraseña)
	 */
	public String autenticar(String usuario, String contrasena) {
		if (usuario == null || usuario.isBlank() || contrasena == null || contrasena.isEmpty()) {
			throw new ApplicationException("Usuario y contraseña son obligatorios");
		}
		String[] credenciales;
		try {
			credenciales = jdbc.getCredenciales(usuario);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
		
		if (credenciales == null || !PasswordHasher.verificar(contrasena, credenciales[1])) {
			throw new ApplicationException("Usuario o contraseña incorrectos");
		}
		return credenciales[0];

	}
	
	public UsuarioAutenticado obtenerUsuarioAutenticado(String dni) {

		try {

	        Set<Posicion> posiciones = new HashSet<>();
	        List<BaseEmpleado> empleados = jdbc.getEmpleados();

	        for (BaseEmpleado empleado : empleados) {

	            if (empleado.getDni().equals(dni)) {
	                posiciones.add(empleado.getPosicion());
	            }

	        }

	        Accionista accionista = jdbc.getAccionistaByDni(dni);

	        boolean esAccionista = accionista != null;

	        return new UsuarioAutenticado(dni, posiciones, esAccionista);

	    } catch (SQLException e) {
	        throw new UnexpectedException(e);
	    }

	}
}
