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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import giis.demo.util.UnexpectedException;

/**
 * Servicio de gestión del archivo de log por sesión.
 * Registra las acciones de cada usuario y las modificaciones realizadas en la BBDD
 * creando un archivo independiente por sesión de usuario dentro del directorio {@code logs/}.
 */
public class GestionLogSesion {

	public static final String DIRECTORIO_LOGS = "logs";
	public static final String OPCION_TODOS_ARCHIVOS = "TODOS";
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
		return iniciarNuevaSesion(null);
	}

	/**
	 * Inicia una nueva sesión para un usuario concreto creando un archivo de log independiente.
	 * Si el archivo de sesión anterior estaba vacío (0 bytes, por ejemplo antes del primer login),
	 * lo elimina para evitar archivos huérfanos vacíos.
	 */
	public static Path iniciarNuevaSesion(String usuario) {
		synchronized (LOCK) {
			try {
				eliminarSiEstaVacio(archivoSesionActual);

				Path dirLogs = Paths.get(DIRECTORIO_LOGS);
				Files.createDirectories(dirLogs);

				String marcaTiempo = LocalDateTime.now().format(FORMATO_NOMBRE_ARCHIVO);
				String sufijoUsuario = sanitizarParaNombreArchivo(usuario);
				String base = sufijoUsuario.isEmpty()
						? "sesion_" + marcaTiempo
						: "sesion_" + marcaTiempo + "_" + sufijoUsuario;

				Path candidato = dirLogs.resolve(base + ".log");
				int sufijo = 2;
				while (Files.exists(candidato)) {
					candidato = dirLogs.resolve(base + "_" + sufijo + ".log");
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

	private static void eliminarSiEstaVacio(Path archivo) {
		if (archivo == null) {
			return;
		}
		try {
			if (Files.exists(archivo) && Files.size(archivo) == 0L) {
				Files.deleteIfExists(archivo);
			}
		} catch (IOException e) {
			// Ignorar si no se puede eliminar un archivo temporal vacío
		}
	}

	private static String sanitizarParaNombreArchivo(String valor) {
		if (valor == null || valor.isBlank()) {
			return "";
		}
		return valor.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
	}

	/**
	 * Garantiza que existe un archivo de log inicializado para la sesión actual.
	 */
	private static Path asegurarSesionIniciada() {
		synchronized (LOCK) {
			if (archivoSesionActual == null || !Files.exists(archivoSesionActual)) {
				String usuarioParaArchivo = USUARIO_POR_DEFECTO.equals(usuarioActivo) ? null : usuarioActivo;
				return iniciarNuevaSesion(usuarioParaArchivo);
			}
			return archivoSesionActual;
		}
	}

	/**
	 * Establece los datos del usuario autenticado e inicia un archivo de log propio
	 * para su sesión, de modo que cada inicio de sesión disponga de su propio fichero.
	 */
	public static void setUsuarioActivo(String usuario, String dni, String rol) {
		synchronized (LOCK) {
			usuarioActivo = (usuario == null || usuario.isBlank()) ? USUARIO_POR_DEFECTO : usuario.trim();
			dniActivo = (dni == null || dni.isBlank()) ? null : dni.trim();
			rolActivo = (rol == null || rol.isBlank()) ? ROL_POR_DEFECTO : rol.trim();
			iniciarNuevaSesion(usuarioActivo);
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
			return normalizarRuta(archivo);
		}
	}

	private static String normalizarRuta(Path archivo) {
		return archivo.toString().replace('\\', '/');
	}

	/**
	 * Devuelve las rutas normalizadas de todos los archivos .log existentes en {@code logs/},
	 * ordenados del más reciente al más antiguo. Incluye siempre el archivo de la sesión actual
	 * y excluye otros archivos vacíos (0 bytes).
	 */
	public List<String> listarArchivosSesion() {
		synchronized (LOCK) {
			Path actual = asegurarSesionIniciada();
			String rutaActual = normalizarRuta(actual);
			Path dirLogs = Paths.get(DIRECTORIO_LOGS);
			List<Path> encontrados = new ArrayList<>();

			if (Files.isDirectory(dirLogs)) {
				try (Stream<Path> stream = Files.list(dirLogs)) {
					stream.filter(Files::isRegularFile)
							.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".log"))
							.forEach(p -> {
								try {
									if (normalizarRuta(p).equals(rutaActual) || Files.size(p) > 0L) {
										encontrados.add(p);
									}
								} catch (IOException e) {
									// Ignorar archivos inaccesibles
								}
							});
				} catch (IOException e) {
					throw new UnexpectedException(e);
				}
			}

			encontrados.sort(Comparator.comparing((Path p) -> p.getFileName().toString()).reversed());

			List<String> rutas = new ArrayList<>();
			for (Path p : encontrados) {
				rutas.add(normalizarRuta(p));
			}
			if (!rutas.contains(rutaActual)) {
				rutas.add(0, rutaActual);
			}
			return rutas;
		}
	}

	/**
	 * Lee desde disco todos los eventos registrados en el archivo de la sesión actual.
	 */
	public List<EventoLog> leerEventosSesionActual() {
		return leerEventosArchivo(getRutaArchivoSesionActual());
	}

	/**
	 * Lee desde disco todos los eventos de un archivo .log concreto.
	 */
	public List<EventoLog> leerEventosArchivo(String rutaArchivo) {
		synchronized (LOCK) {
			List<EventoLog> eventos = new ArrayList<>();
			if (rutaArchivo == null || rutaArchivo.isBlank()) {
				return eventos;
			}
			Path archivo = Paths.get(rutaArchivo);
			if (!Files.exists(archivo)) {
				return eventos;
			}
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
	 * Lee y combina los eventos de todos los archivos .log existentes en {@code logs/}
	 * en orden cronológico (de sesiones más antiguas a más recientes).
	 */
	public List<EventoLog> leerEventosTodosLosArchivos() {
		synchronized (LOCK) {
			List<String> archivos = new ArrayList<>(listarArchivosSesion());
			Collections.reverse(archivos);
			List<EventoLog> todos = new ArrayList<>();
			for (String ruta : archivos) {
				todos.addAll(leerEventosArchivo(ruta));
			}
			return todos;
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
	 * Devuelve la lista de usuarios distintos que han intervenido en los eventos dados,
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
