package giis.demo.model.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import giis.demo.util.UnexpectedException;

/**
 * Servicio de gestión del archivo de log por sesión.
 * Registra todas las acciones de cada usuario y las modificaciones realizadas en la BBDD
 * en un archivo único por sesión dentro del directorio {@code logs/}.
 */
public class GestionLogSesion {

	public static final String DIRECTORIO_LOGS = "logs";
	public static final String FILTRO_TODOS = "Todos";
	public static final String FILTRO_SOLO_BBDD = "Solo modificaciones BD";
	public static final String FILTRO_SOLO_ACCIONES = "Solo acciones del usuario";

	private static final String USUARIO_POR_DEFECTO = "anonimo";
	private static final String ROL_POR_DEFECTO = "SIN_ROL";
	private static final String TIPO_ACCION = "acción";

	private static final DateTimeFormatter FORMATO_NOMBRE_ARCHIVO = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
	private static final DateTimeFormatter FORMATO_HORA_EVENTO = DateTimeFormatter.ofPattern("HH:mm:ss");

	private static final Object LOCK = new Object();
	private static Path archivoSesionActual;
	private static String usuarioActivo = USUARIO_POR_DEFECTO;
	private static String dniActivo = null;
	private static String rolActivo = ROL_POR_DEFECTO;

	/**
	 * Inicia una nueva sesión creando un archivo de log nuevo en disco.
	 * Si ya existe otro archivo creado en el mismo minuto, añade un sufijo incremental
	 * para no sobrescribir ni mezclar sesiones anteriores.
	 */
	public static Path iniciarNuevaSesion() {
		synchronized (LOCK) {
			try {
				Path dirLogs = Paths.get(DIRECTORIO_LOGS);
				Files.createDirectories(dirLogs);

				String marcaTiempo = LocalDateTime.now().format(FORMATO_NOMBRE_ARCHIVO);
				Path candidato = dirLogs.resolve("sesion_" + marcaTiempo + ".log");
				int sufijo = 2;
				while (Files.exists(candidato)) {
					candidato = dirLogs.resolve("sesion_" + marcaTiempo + "_" + sufijo + ".log");
					sufijo++;
				}

				Files.createFile(candidato);
				archivoSesionActual = candidato;
				return archivoSesionActual;
			} catch (IOException e) {
				throw new UnexpectedException(e);
			}
		}
	}

	/**
	 * Garantiza que existe un archivo de log inicializado para la sesión actual.
	 */
	private static Path asegurarSesionIniciada() {
		synchronized (LOCK) {
			if (archivoSesionActual == null || !Files.exists(archivoSesionActual)) {
				return iniciarNuevaSesion();
			}
			return archivoSesionActual;
		}
	}

	/**
	 * Establece los datos del usuario autenticado en la sesión actual.
	 */
	public static void setUsuarioActivo(String usuario, String dni, String rol) {
		synchronized (LOCK) {
			usuarioActivo = (usuario == null || usuario.isBlank()) ? USUARIO_POR_DEFECTO : usuario.trim();
			dniActivo = (dni == null || dni.isBlank()) ? null : dni.trim();
			rolActivo = (rol == null || rol.isBlank()) ? ROL_POR_DEFECTO : rol.trim();
		}
	}

	/**
	 * Restablece el usuario activo al estado sin autenticar.
	 */
	public static void limpiarUsuarioActivo() {
		synchronized (LOCK) {
			usuarioActivo = USUARIO_POR_DEFECTO;
			dniActivo = null;
			rolActivo = ROL_POR_DEFECTO;
		}
	}

	public static String getUsuarioActivo() {
		synchronized (LOCK) {
			return usuarioActivo;
		}
	}

	public static String getDniActivo() {
		synchronized (LOCK) {
			return dniActivo;
		}
	}

	public static String getRolActivo() {
		synchronized (LOCK) {
			return rolActivo;
		}
	}

	/**
	 * Registra una acción realizada por el usuario activo en la sesión actual.
	 */
	public static void registrarAccion(String detalle) {
		escribirEvento(TIPO_ACCION, detalle);
	}

	/**
	 * Registra con especial relevancia una modificación sobre la base de datos
	 * (insert, update o delete).
	 */
	public static void registrarModificacionBBDD(String operacion, String detalle) {
		String op = (operacion == null || operacion.isBlank()) ? "modificacion" : operacion.trim().toLowerCase();
		escribirEvento("BBDD-" + op, detalle);
	}

	private static void escribirEvento(String tipo, String detalle) {
		synchronized (LOCK) {
			Path archivo = asegurarSesionIniciada();
			String hora = LocalTime.now().format(FORMATO_HORA_EVENTO);
			String usuarioLimpio = sanitizarCampo(usuarioActivo, USUARIO_POR_DEFECTO);
			String rolLimpio = sanitizarCampo(rolActivo, ROL_POR_DEFECTO);
			String tipoLimpio = sanitizarCampo(tipo, TIPO_ACCION);
			String detalleLimpio = sanitizarCampo(detalle, "-");

			String linea = hora + " | " + usuarioLimpio + " | " + rolLimpio + " | " + tipoLimpio + " | " + detalleLimpio
					+ System.lineSeparator();
			try {
				Files.writeString(archivo, linea, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				throw new UnexpectedException(e);
			}
		}
	}

	private static String sanitizarCampo(String valor, String porDefecto) {
		if (valor == null || valor.isBlank()) {
			return porDefecto;
		}
		return valor.replace('\r', ' ').replace('\n', ' ').replace('|', '/').trim();
	}

	/**
	 * Devuelve la ruta normalizada con '/' del archivo de log de la sesión actual.
	 */
	public String getRutaArchivoSesionActual() {
		synchronized (LOCK) {
			Path archivo = asegurarSesionIniciada();
			return archivo.toString().replace('\\', '/');
		}
	}

	/**
	 * Lee desde disco todos los eventos registrados en el archivo de la sesión actual.
	 */
	public List<EventoLog> leerEventosSesionActual() {
		synchronized (LOCK) {
			Path archivo = asegurarSesionIniciada();
			List<EventoLog> eventos = new ArrayList<>();
			try {
				List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
				for (String linea : lineas) {
					if (linea == null || linea.isBlank()) {
						continue;
					}
					String[] partes = linea.split("\\s*\\|\\s*", 5);
					if (partes.length == 5) {
						eventos.add(new EventoLog(partes[0], partes[1], partes[2], partes[3], partes[4]));
					}
				}
			} catch (IOException e) {
				throw new UnexpectedException(e);
			}
			return eventos;
		}
	}

	/**
	 * Filtra la lista de eventos por tipo y por usuario.
	 */
	public List<EventoLog> filtrarEventos(List<EventoLog> eventos, String filtroTipo, String filtroUsuario) {
		List<EventoLog> resultado = new ArrayList<>();
		if (eventos == null) {
			return resultado;
		}

		for (EventoLog evento : eventos) {
			if (!cumpleFiltroTipo(evento, filtroTipo)) {
				continue;
			}
			if (!cumpleFiltroUsuario(evento, filtroUsuario)) {
				continue;
			}
			resultado.add(evento);
		}
		return resultado;
	}

	private boolean cumpleFiltroTipo(EventoLog evento, String filtroTipo) {
		if (filtroTipo == null || filtroTipo.isBlank() || FILTRO_TODOS.equals(filtroTipo)) {
			return true;
		}
		if (FILTRO_SOLO_BBDD.equals(filtroTipo)) {
			return evento.esModificacionBBDD();
		}
		if (FILTRO_SOLO_ACCIONES.equals(filtroTipo)) {
			return evento.esAccionUsuario();
		}
		return true;
	}

	private boolean cumpleFiltroUsuario(EventoLog evento, String filtroUsuario) {
		if (filtroUsuario == null || filtroUsuario.isBlank() || FILTRO_TODOS.equals(filtroUsuario)) {
			return true;
		}
		return evento.getUsuario() != null && evento.getUsuario().equalsIgnoreCase(filtroUsuario.trim());
	}

	/**
	 * Devuelve la lista de usuarios distintos que han intervenido en la sesión actual,
	 * manteniendo su orden de aparición.
	 */
	public List<String> obtenerUsuariosSesion(List<EventoLog> eventos) {
		Set<String> usuarios = new LinkedHashSet<>();
		if (eventos != null) {
			for (EventoLog evento : eventos) {
				if (evento.getUsuario() != null && !evento.getUsuario().isBlank()) {
					usuarios.add(evento.getUsuario());
				}
			}
		}
		return new ArrayList<>(usuarios);
	}

	/**
	 * Cuenta cuántos eventos de la lista corresponden a modificaciones en la base de datos.
	 */
	public int contarModificacionesBBDD(List<EventoLog> eventos) {
		int contador = 0;
		if (eventos != null) {
			for (EventoLog evento : eventos) {
				if (evento.esModificacionBBDD()) {
					contador++;
				}
			}
		}
		return contador;
	}
}
