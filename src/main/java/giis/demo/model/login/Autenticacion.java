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
	 * Autentica a un miembro y obtiene su sesión con los permisos
	 * correspondientes al tipo de cuenta utilizado.
	 *
	 * @param usuario nombre de usuario
	 * @param contrasena contraseña en claro
	 * @return el usuario autenticado
	 * @throws ApplicationException si los campos están vacíos o las
	 *                              credenciales no son válidas.
	 */
	public UsuarioAutenticado autenticar(String usuario, String contrasena) {

		if (usuario == null || usuario.isBlank()
				|| contrasena == null || contrasena.isEmpty()) {
			throw new ApplicationException("Usuario y contraseña son obligatorios");
		}

		String[] credenciales;

		try {
			credenciales = jdbc.getCredenciales(usuario);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}

		if (credenciales == null
				|| !PasswordHasher.verificar(contrasena, credenciales[1])) {
			throw new ApplicationException("Usuario o contraseña incorrectos");
		}

		String dni = credenciales[0];
		String tipo = credenciales[2];

		try {

			Set<Posicion> posiciones = new HashSet<>();

			if ("EMPLEADO".equalsIgnoreCase(tipo)) {

				List<BaseEmpleado> empleados = jdbc.getEmpleados();

				for (BaseEmpleado empleado : empleados) {
					if (empleado.getDni().equalsIgnoreCase(dni)) {
						posiciones.add(empleado.getPosicion());
					}
				}

				if (posiciones.isEmpty()) {
					throw new ApplicationException(
							"La cuenta de empleado no tiene un perfil asociado");
				}

				return new UsuarioAutenticado(dni, posiciones, false);

			} else if ("ACCIONISTA".equalsIgnoreCase(tipo)) {

				Accionista accionista = jdbc.getAccionistaByDni(dni);

				if (accionista == null) {
					throw new ApplicationException(
							"La cuenta de accionista no tiene un perfil asociado");
				}

				return new UsuarioAutenticado(dni, posiciones, true);

			} else {
				throw new ApplicationException("Tipo de cuenta desconocido");
			}

		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
	}
}
