package giis.demo.model.login;

import java.sql.SQLException;

import giis.demo.jdbc.JDBC;
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
		} finally {
			try {
				jdbc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
 
		if (credenciales == null || !PasswordHasher.verificar(contrasena, credenciales[2], credenciales[1])) {
			throw new ApplicationException("Usuario o contraseña incorrectos");
		}
 
		return credenciales[0];
	}
}
