package giis.demo.model.equipo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import giis.demo.jdbc.JDBC;
import giis.demo.model.reserva.Instalacion;

public class ImportarPartidosService {

	private JDBC jdbc = new JDBC();

	/**
	 * Carga y parsea los partidos desde un archivo .csv o .json.
	 */
	public List<PartidoImportadoDTO> cargarPartidosDesdeArchivo(File archivo) throws IOException {
		if (archivo == null || !archivo.exists()) {
			throw new IllegalArgumentException("El archivo especificado no existe.");
		}

		String nombre = archivo.getName().toLowerCase();
		List<PartidoImportadoDTO> partidos;
		if (nombre.endsWith(".csv")) {
			partidos = parsearCsv(archivo);
		} else if (nombre.endsWith(".json")) {
			partidos = parsearJson(archivo);
		} else {
			throw new IllegalArgumentException("Formato no soportado. Debe ser un archivo .csv o .json.");
		}

		// Validar automáticamente los partidos cargados contra reglas de negocio y colisiones
		validarPartidos(partidos);

		return partidos;
	}

	/**
	 * Parsea un archivo CSV (soporta separador por coma o punto y coma).
	 */
	private List<PartidoImportadoDTO> parsearCsv(File archivo) throws IOException {
		List<PartidoImportadoDTO> partidos = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String primeraLinea = br.readLine();
			if (primeraLinea == null) {
				return partidos; // Archivo vacío
			}

			// Validar separador: debe usar ',' o ';'
			String separador;
			if (primeraLinea.contains(";")) {
				separador = ";";
			} else if (primeraLinea.contains(",")) {
				separador = ",";
			} else {
				throw new IllegalArgumentException("Error en la importación: separador incorrecto");
			}

			String linea = primeraLinea;
			// Si la primera línea es cabecera, la saltamos
			if (linea.toLowerCase().contains("local") || linea.toLowerCase().contains("equipo")) {
				linea = br.readLine();
			}

			while (linea != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] tokens = linea.split(separador);
					if (tokens.length < 6) {
						if (linea.contains("|") || linea.contains("\t") || linea.contains(":")) {
							throw new IllegalArgumentException("Error en la importación: separador incorrecto");
						}
						throw new IllegalArgumentException("Error en la importación: formato inválido");
					}

					String local = tokens[0].trim().replace("\"", "");
					String visitante = tokens[1].trim().replace("\"", "");
					LocalDate fecha = parsearFecha(tokens[2].trim().replace("\"", ""));
					LocalTime hIni = parsearHora(tokens[3].trim().replace("\"", ""));
					LocalTime hFin = parsearHora(tokens[4].trim().replace("\"", ""));
					String instalacion = tokens[5].trim().replace("\"", "");

					if (local.isEmpty() || visitante.isEmpty() || instalacion.isEmpty()) {
						throw new IllegalArgumentException("Error en la importación: formato inválido");
					}

					partidos.add(new PartidoImportadoDTO(local, visitante, fecha, hIni, hFin, instalacion));
				}
				linea = br.readLine();
			}
		}

		if (partidos.isEmpty()) {
			throw new IllegalArgumentException("Error en la importación: formato inválido");
		}

		return partidos;
	}

	/**
	 * Parsea un archivo JSON usando Jackson. Acepta array raíz o objeto con propiedad "partidos".
	 */
	private List<PartidoImportadoDTO> parsearJson(File archivo) throws IOException {
		List<PartidoImportadoDTO> partidos = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(archivo);
		} catch (com.fasterxml.jackson.core.JsonParseException e) {
			String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
			if (msg.contains("code 59") || msg.contains("';'") || msg.contains("unexpected character (';'")
					|| msg.contains("was expecting comma to separate")) {
				throw new IllegalArgumentException("Error en la importación: separador incorrecto");
			} else {
				throw new IllegalArgumentException("Error en la importación: formato inválido");
			}
		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
			throw new IllegalArgumentException("Error en la importación: formato inválido");
		}

		if (root == null) {
			throw new IllegalArgumentException("Error en la importación: formato inválido");
		}

		JsonNode arrayPartidos = root.isArray() ? root : root.get("partidos");
		if (arrayPartidos == null || !arrayPartidos.isArray() || arrayPartidos.isEmpty()) {
			throw new IllegalArgumentException("Error en la importación: formato inválido");
		}

		for (JsonNode node : arrayPartidos) {
			String local = obtenerTextoNodo(node, "equipoLocal", "equipo_local", "local");
			String visitante = obtenerTextoNodo(node, "equipoVisitante", "equipo_visitante", "visitante");
			String fechaStr = obtenerTextoNodo(node, "fecha");
			String hIniStr = obtenerTextoNodo(node, "horaInicio", "hora_inicio");
			String hFinStr = obtenerTextoNodo(node, "horaFin", "hora_fin");
			String instalacion = obtenerTextoNodo(node, "instalacion");

			if (local.isEmpty() || visitante.isEmpty() || fechaStr.isEmpty() || hIniStr.isEmpty() || hFinStr.isEmpty() || instalacion.isEmpty()) {
				throw new IllegalArgumentException("Error en la importación: formato inválido");
			}

			try {
				LocalDate fecha = parsearFecha(fechaStr);
				LocalTime hIni = parsearHora(hIniStr);
				LocalTime hFin = parsearHora(hFinStr);
				partidos.add(new PartidoImportadoDTO(local, visitante, fecha, hIni, hFin, instalacion));
			} catch (Exception ex) {
				throw new IllegalArgumentException("Error en la importación: formato inválido");
			}
		}

		return partidos;
	}

	private String obtenerTextoNodo(JsonNode node, String... claves) {
		for (String clave : claves) {
			if (node.has(clave)) {
				return node.get(clave).asText().trim();
			}
		}
		return "";
	}

	private LocalDate parsearFecha(String valor) {
		try {
			if (valor.contains("-")) {
				return LocalDate.parse(valor, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} else {
				return LocalDate.parse(valor, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Error en la importación: formato inválido");
		}
	}

	private LocalTime parsearHora(String valor) {
		try {
			if (valor.length() == 5) {
				return LocalTime.parse(valor, DateTimeFormatter.ofPattern("HH:mm"));
			} else {
				return LocalTime.parse(valor, DateTimeFormatter.ofPattern("HH:mm:ss"));
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Error en la importación: formato inválido");
		}
	}

	/**
	 * Valida cada partido según las reglas de negocio y comprueba si genera colisiones
	 * tanto internas (con otros partidos del archivo) como externas (con eventos existentes en BBDD).
	 * Actualiza el estado esValido y el motivo en cada PartidoImportadoDTO.
	 */
	public void validarPartidos(List<PartidoImportadoDTO> partidos) {
		if (partidos == null || partidos.isEmpty()) {
			return;
		}

		Map<String, Integer> mapaEquipos;
		Map<String, Integer> mapaInstalaciones;
		Map<String, Integer> mapaEquiposPropios;
		try {
			mapaEquipos = cargarMapaEquipos();
			mapaEquiposPropios = cargarMapaEquiposPropios();
			mapaInstalaciones = cargarMapaInstalaciones();
		} catch (SQLException e) {
			for (PartidoImportadoDTO dto : partidos) {
				dto.setValido(false);
				dto.setMotivo("Error al acceder a la base de datos");
			}
			return;
		}

		DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");

		for (int i = 0; i < partidos.size(); i++) {
			PartidoImportadoDTO dto = partidos.get(i);

			// 1. Validar horas
			if (dto.getHoraInicio() == null || dto.getHoraFin() == null) {
				dto.setValido(false);
				dto.setMotivo("Horario no definido");
				continue;
			}
			if (!dto.getHoraFin().isAfter(dto.getHoraInicio())) {
				dto.setValido(false);
				dto.setMotivo("Hora fin debe ser posterior a inicio");
				continue;
			}

			// 2. Validar que local y visitante no sean iguales
			if (dto.getEquipoLocal() == null || dto.getEquipoVisitante() == null
					|| dto.getEquipoLocal().equalsIgnoreCase(dto.getEquipoVisitante())) {
				dto.setValido(false);
				dto.setMotivo("Equipo local y visitante iguales");
				continue;
			}

			// 3. Validar existencia del equipo local (debe ser un equipo propio de la organización)
			Integer idLocal = mapaEquiposPropios.get(dto.getEquipoLocal().toLowerCase());
			if (idLocal == null) {
				dto.setValido(false);
				dto.setMotivo("Equipo local no registrado: " + dto.getEquipoLocal());
				continue;
			}

			// 4. Si el visitante ya está registrado en BBDD, validar que no coincida con el local
			Integer idVisitante = mapaEquipos.get(dto.getEquipoVisitante().toLowerCase());
			if (idVisitante != null && idLocal.equals(idVisitante)) {
				dto.setValido(false);
				dto.setMotivo("Equipo local y visitante son el mismo");
				continue;
			}

			// 6. Validar existencia de la instalación
			Integer idInstalacion = mapaInstalaciones.get(dto.getInstalacion().toLowerCase());
			if (idInstalacion == null) {
				dto.setValido(false);
				dto.setMotivo("Instalación no registrada: " + dto.getInstalacion());
				continue;
			}

			// 7. Colisión interna entre partidos del archivo
			boolean colisionInterna = false;
			for (int j = 0; j < i; j++) {
				PartidoImportadoDTO anterior = partidos.get(j);
				if (anterior.isValido() && anterior.getInstalacion().equalsIgnoreCase(dto.getInstalacion())
						&& anterior.getFecha().equals(dto.getFecha())) {
					if (dto.getHoraInicio().isBefore(anterior.getHoraFin()) && dto.getHoraFin().isAfter(anterior.getHoraInicio())) {
						dto.setValido(false);
						dto.setMotivo("Colisión interna con partido anterior (" + anterior.getHoraInicioFormateada() + " - " + anterior.getHoraFinFormateada() + ")");
						colisionInterna = true;
						break;
					}
				}
			}
			if (colisionInterna) {
				continue;
			}

			// 8. Colisión con eventos de ocupación en la base de datos (Partido, Entrenamiento, Reserva, Jardinería)
			try {
				List<EventoOcupacion> eventos = jdbc.obtenerEventosOcupacionPartido(idLocal, idInstalacion, dto.getFecha());
				boolean colisionBBDD = false;
				for (EventoOcupacion ev : eventos) {
					if (dto.getHoraInicio().isBefore(ev.getHoraFin()) && dto.getHoraFin().isAfter(ev.getHoraInicio())) {
						dto.setValido(false);
						dto.setMotivo("Colisión con " + ev.getTipo().toLowerCase() + " (" + ev.getHoraInicio().format(fmtHora) + " - " + ev.getHoraFin().format(fmtHora) + ")");
						colisionBBDD = true;
						break;
					}
				}
				if (colisionBBDD) {
					continue;
				}
			} catch (SQLException e) {
				dto.setValido(false);
				dto.setMotivo("Error al consultar disponibilidad en BBDD");
				continue;
			}

			// Pasa todas las validaciones
			dto.setValido(true);
			dto.setMotivo("Correcto");
		}
	}

	/**
	 * Importa únicamente los partidos que han sido evaluados como válidos.
	 * @return Lista de los partidos que fueron importados con éxito en la base de datos.
	 */
	public List<PartidoImportadoDTO> importarPartidosValidos(List<PartidoImportadoDTO> partidos) throws SQLException {
		if (partidos == null || partidos.isEmpty()) {
			throw new IllegalArgumentException("No hay partidos para importar.");
		}

		// Revalidar para garantizar coherencia con el estado actual de la BBDD
		validarPartidos(partidos);

		Map<String, Integer> mapaEquipos = cargarMapaEquipos();
		Map<String, Integer> mapaInstalaciones = cargarMapaInstalaciones();
		Map<String, Integer> mapaEquiposPropios = cargarMapaEquiposPropios();

		List<PartidoImportadoDTO> importados = new ArrayList<>();

		for (PartidoImportadoDTO dto : partidos) {
			if (dto.isValido()) {
				Integer idLocal = mapaEquiposPropios.get(dto.getEquipoLocal().toLowerCase());
				Integer idVisitante = mapaEquipos.get(dto.getEquipoVisitante().toLowerCase());
				// Si el equipo visitante no estaba registrado, se da de alta automáticamente con su nombre
				if (idVisitante == null) {
					idVisitante = jdbc.obtenerOCrearEquipoVisitante(dto.getEquipoVisitante(), idLocal);
					mapaEquipos.put(dto.getEquipoVisitante().toLowerCase(), idVisitante);
				}
				Integer idInstalacion = mapaInstalaciones.get(dto.getInstalacion().toLowerCase());
				jdbc.insertarPartidoImportado(idLocal, idVisitante, idInstalacion, 
						dto.getFecha(), dto.getHoraInicio(), dto.getHoraFin());
				importados.add(dto);
			}
		}

		return importados;
	}

	/**
	 * Importa los partidos válidos y devuelve la cantidad importada.
	 */
	public int importarPartidos(List<PartidoImportadoDTO> partidos) throws SQLException {
		return importarPartidosValidos(partidos).size();
	}

	private Map<String, Integer> cargarMapaEquipos() throws SQLException {
		Map<String, Integer> mapa = new HashMap<>();
		for (Equipo e : jdbc.getEquiposPropios()) {
			mapa.put(e.getNombre().toLowerCase(), e.getIdEquipo());
		}
		for (Equipo e : jdbc.getEquiposVisitantes()) {
			mapa.put(e.getNombre().toLowerCase(), e.getIdEquipo());
		}
		return mapa;
	}
	
	private Map<String, Integer> cargarMapaEquiposPropios() throws SQLException {
		Map<String, Integer> mapa = new HashMap<>();
		for (Equipo e : jdbc.getEquiposPropios()) {
			mapa.put(e.getNombre().toLowerCase(), e.getIdEquipo());
		}
		return mapa;
	}

	private Map<String, Integer> cargarMapaInstalaciones() throws SQLException {
		Map<String, Integer> mapa = new HashMap<>();
		for (Instalacion inst : jdbc.getAvailableInstalations()) {
			mapa.put(inst.getName().toLowerCase(), inst.getInstalacionId());
		}
		return mapa;
	}
}
