package giis.demo.model.accion;

import java.sql.SQLException;

import giis.demo.jdbc.JDBC;
import giis.demo.model.email.ValidadorEmail;
import giis.demo.model.login.PasswordHasher;
import giis.demo.util.ApplicationException;

/**
 * Logica de negocio de la compra de acciones de la campaña activa.
 * 
 * Reglas: fase 1, solo accionistas y hasta las acciones que tenian al lanzarse
 * la campaña; fase 2, solo accionistas, sin limite; fase 3, cualquiera (los
 * nuevos se registran), sin limite. Siempre hasta las acciones disponibles.
 */
public class GestionCompraCampana {

	public static final String MSG_SIN_CAMPANA = "No hay ninguna campaña de acciones activa.\nNo es posible comprar acciones.";
	public static final String MSG_DNI_VACIO = "Introduzca el DNI del comprador.";
	public static final String MSG_DNI_INVALIDO = "El DNI debe tener 8 números y una letra.";
	public static final String MSG_CANTIDAD_INVALIDA = "Indique un número de acciones mayor que cero.";
	public static final String MSG_DATOS_REGISTRO = "Nombre, apellidos y email son obligatorios.";
	public static final String MSG_EMAIL_INVALIDO = "Introduzca un email válido.\nEjemplo: nombre@uniovi.es";

	public static final String MSG_CREDENCIALES_OBLIGATORIAS = "El nombre de usuario y la contraseña son obligatorios.";

	public static final String MSG_USUARIO_DUPLICADO = "Ese nombre de usuario ya está registrado. Elija otro.";

	private JDBC jdbc = new JDBC();

	/**
	 * Devuelve la campaña activa o null si no hay ninguna.
	 */
	public Campana obtenerCampanaActiva() throws SQLException {
		return jdbc.getCampanaActiva();
	}

	/**
	 * Identifica al comprador por su DNI y calcula lo que puede comprar en la
	 * campaña activa.
	 * 
	 * @throws ApplicationException si el DNI no es válido, no hay campaña activa, o
	 *                              no es accionista y la campaña no está en la
	 *                              última fase
	 */
	public InfoCompra identificar(String dniTexto) throws SQLException {
		String dni = dniTexto == null ? "" : dniTexto.trim().toUpperCase();
		if (dni.isEmpty())
			throw new ApplicationException(MSG_DNI_VACIO);
		if (!dni.matches("\\d{8}[A-Z]"))
			throw new ApplicationException(MSG_DNI_INVALIDO);

		Campana campana = jdbc.getCampanaActiva();
		if (campana == null)
			throw new ApplicationException(MSG_SIN_CAMPANA);

		Accionista accionista = jdbc.getAccionistaByDni(dni);
		if (accionista == null && campana.getFase() != Campana.ULTIMA_FASE)
			throw new ApplicationException("El DNI " + dni + " no corresponde a ningún accionista.\n"
					+ "Los nuevos accionistas solo pueden comprar en la fase " + Campana.ULTIMA_FASE
					+ " (la campaña está en la fase " + campana.getFase() + ").");

		int cupoFase1 = (accionista != null && campana.getFase() == Campana.PRIMERA_FASE)
				? jdbc.getCupoFase1Campana(campana.getId(), dni)
				: 0;
		int maximo = calcularMaximo(campana.getFase(), campana.getAccionesDisponibles(), cupoFase1);

		boolean miembroExistente = jdbc.existeMiembro(dni);

		return new InfoCompra(campana, dni, accionista, maximo, miembroExistente);
	}

	/**
	 * Máximo de acciones que se pueden comprar: en la fase 1 el menor entre las
	 * disponibles y lo que le queda al accionista de las que tenía al lanzarse; en
	 * el resto, las disponibles.
	 */
	public static int calcularMaximo(int fase, int disponibles, int cupoFase1) {
		if (fase == Campana.PRIMERA_FASE)
			return Math.min(disponibles, cupoFase1);
		return disponibles;
	}

	/**
	 * Comprueba la cantidad escrita y la devuelve como número.
	 * 
	 * @throws ApplicationException indicando el motivo si no es un número mayor que
	 *                              cero o supera las disponibles o el máximo
	 *                              permitido
	 */
	public static int validarCantidad(InfoCompra info, String texto) {
		int cantidad;
		try {
			cantidad = Integer.parseInt(texto.trim());
		} catch (NumberFormatException | NullPointerException e) {
			throw new ApplicationException(MSG_CANTIDAD_INVALIDA);
		}
		if (cantidad <= 0)
			throw new ApplicationException(MSG_CANTIDAD_INVALIDA);

		int disponibles = info.getCampana().getAccionesDisponibles();
		if (cantidad > disponibles)
			throw new ApplicationException(
					"No se pueden comprar " + cantidad + " acciones: solo quedan " + disponibles + " disponibles.");
		if (info.getMaximo() == 0)
			throw new ApplicationException(
					"Ya ha comprado todas las acciones que le corresponden en la fase " + Campana.PRIMERA_FASE + ".");
		if (cantidad > info.getMaximo())
			throw new ApplicationException("No se pueden comprar " + cantidad + " acciones: su máximo en la fase "
					+ info.getCampana().getFase() + " es de " + info.getMaximo() + " acciones.");
		return cantidad;
	}

	/**
	 * Email al que se enviará el resumen: el guardado del accionista si es válido
	 * y, si no, el introducido.
	 */
	public static String emailDestino(InfoCompra info, String emailIntroducido) {
		String guardado = info.getEmailGuardado();
		if (guardado != null)
			return guardado;
		return emailIntroducido == null ? "" : emailIntroducido.trim();
	}

	/**
	 * Comprueba los datos del comprador: si es nuevo, nombre y apellidos; en todos
	 * los casos, un email válido (el guardado o el introducido).
	 * 
	 * @throws SQLException
	 * 
	 * @throws ApplicationException indicando el motivo si falta algún dato o el
	 *                              email no es válido
	 */
	public void validarDatosComprador(InfoCompra info, String nombre, String apellidos, String email, String usuario,
			String contrasena) throws SQLException {
		String emailFinal = emailDestino(info, email);
		if (info.esNuevo() && (esVacio(nombre) || esVacio(apellidos) || emailFinal.isEmpty()))
			throw new ApplicationException(MSG_DATOS_REGISTRO);
		if (!ValidadorEmail.esValido(emailFinal))
			throw new ApplicationException(MSG_EMAIL_INVALIDO);

		if (!info.tieneMiembro()) {
			if (esVacio(usuario) || esVacio(contrasena))
				throw new ApplicationException(MSG_CREDENCIALES_OBLIGATORIAS);

			if (jdbc.existeUsuarioEmpleado(usuario.trim()))
				throw new ApplicationException(MSG_USUARIO_DUPLICADO);
		}

	}

	/**
	 * Registra la compra. Si el comprador es nuevo, lo registra como accionista con
	 * los datos indicados.
	 * 
	 * @param unidades cantidad ya validada con {@link #validarCantidad}
	 * @throws ApplicationException si faltan datos del comprador, el email no es
	 *                              válido, o la campaña ha cambiado desde que se
	 *                              mostró
	 */
	public ResultadoCompra comprar(InfoCompra info, int unidades, String nombre, String apellidos, String email,
			String usuario, String contrasena) throws SQLException {

		validarDatosComprador(info, nombre, apellidos, email, usuario, contrasena);

		String emailFinal = emailDestino(info, email);

		String usuarioFinal;
		String hashContrasena;

		if (info.tieneMiembro()) {

			// Reutilizamos las credenciales que ya existen.
			String[] credenciales = jdbc.getCredencialesPorDni(info.getDni());

			if (credenciales == null) {
				throw new ApplicationException("No se han encontrado las credenciales del miembro.");
			}

			usuarioFinal = credenciales[0];
			hashContrasena = credenciales[1];

		} else {

			// Creamos las credenciales para un miembro nuevo.
			usuarioFinal = usuario.trim();
			hashContrasena = PasswordHasher.hash(contrasena);
		}

		Accionista comprador;

		if (info.esNuevo()) {
			comprador = new Accionista(info.getDni(), nombre.trim(), apellidos.trim(), emailFinal, usuarioFinal,
					hashContrasena);

		} else {

			Accionista a = info.getAccionista();
			comprador = new Accionista(info.getDni(), a.getNombre(), a.getApellidos(), emailFinal, usuarioFinal,
					hashContrasena);
		}

		return jdbc.comprarAccionesCampana(info.getCampana(), comprador, info.esNuevo(), unidades, usuarioFinal,
				info.tieneMiembro() ? null : hashContrasena);
	}

	private static boolean esVacio(String texto) {
		return texto == null || texto.trim().isEmpty();
	}
}