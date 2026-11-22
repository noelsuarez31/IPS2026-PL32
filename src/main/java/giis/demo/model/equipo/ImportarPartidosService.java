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
		if (nombre.endsWith(".csv")) {
			return parsearCsv(archivo);
		} else if (nombre.endsWith(".json")) {
			return parsearJson(archivo);
		} else {
			throw new IllegalArgumentException("Formato no soportado. Debe ser un archivo .csv o .json.");
		}
	}

	/**
	 * Parsea un archivo CSV (soporta separador por coma o punto y coma).
	 */
	private List<PartidoImportadoDTO> parsearCsv(File archivo) throws IOException {
		List<PartidoImportadoDTO> partidos = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea = br.readLine();
			if (linea == null) {
				return partidos; // Archivo vacío
			}

			// Detectar separador (, o ;)
			String separador = linea.contains(";") ? ";" : ",";

			// Si la primera línea es cabecera, la saltamos
			if (linea.toLowerCase().contains("local") || linea.toLowerCase().contains("equipo")) {
				linea = br.readLine();
			}

			while (linea != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] tokens = linea.split(separador);
					if (tokens.length >= 6) {
						String local = tokens[0].trim().replace("\"", "");
						String visitante = tokens[1].trim().replace("\"", "");
						LocalDate fecha = parsearFecha(tokens[2].trim().replace("\"", ""));
						LocalTime hIni = parsearHora(tokens[3].trim().replace("\"", ""));
						LocalTime hFin = parsearHora(tokens[4].trim().replace("\"", ""));
						String instalacion = tokens[5].trim().replace("\"", "");

						partidos.add(new PartidoImportadoDTO(local, visitante, fecha, hIni, hFin, instalacion));
					}
				}
				linea = br.readLine();
			}
		}

		return partidos;
	}

	/**
	 * Parsea un archivo JSON usando Jackson. Acepta array raíz o objeto con propiedad "partidos".
	 */
	private List<PartidoImportadoDTO> parsearJson(File archivo) throws IOException {
		List<PartidoImportadoDTO> partidos = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(archivo);

		JsonNode arrayPartidos = root.isArray() ? root : root.get("partidos");
		if (arrayPartidos != null && arrayPartidos.isArray()) {
			for (JsonNode node : arrayPartidos) {
				String local = obtenerTextoNodo(node, "equipoLocal", "equipo_local", "local");
				String visitante = obtenerTextoNodo(node, "equipoVisitante", "equipo_visitante", "visitante");
				String fechaStr = obtenerTextoNodo(node, "fecha");
				String hIniStr = obtenerTextoNodo(node, "horaInicio", "hora_inicio");
				String hFinStr = obtenerTextoNodo(node, "horaFin", "hora_fin");
				String instalacion = obtenerTextoNodo(node, "instalacion");

				if (!local.isEmpty() && !visitante.isEmpty()) {
					LocalDate fecha = parsearFecha(fechaStr);
					LocalTime hIni = parsearHora(hIniStr);
					LocalTime hFin = parsearHora(hFinStr);
					partidos.add(new PartidoImportadoDTO(local, visitante, fecha, hIni, hFin, instalacion));
				}
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
			throw new IllegalArgumentException("Formato de fecha inválido ('" + valor + "'). Use yyyy-MM-dd o dd/MM/yyyy.");
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
			throw new IllegalArgumentException("Formato de hora inválido ('" + valor + "'). Use HH:mm.");
		}
	}

	/**
	 * Valida los partidos y los almacena en la tabla Partido de la base de datos.
	 * @return Número de partidos importados correctamente.
	 */
	public int importarPartidos(List<PartidoImportadoDTO> partidos) throws SQLException {
		if (partidos == null || partidos.isEmpty()) {
			throw new IllegalArgumentException("No hay partidos para importar.");
		}

		// Cargar mapas de equipos e instalaciones para resolver IDs
		Map<String, Integer> mapaEquipos = cargarMapaEquipos();
		Map<String, Integer> mapaInstalaciones = cargarMapaInstalaciones();

		int importados = 0;

		for (PartidoImportadoDTO dto : partidos) {
			// Validaciones básicas
			if (dto.getEquipoLocal().equalsIgnoreCase(dto.getEquipoVisitante())) {
				throw new IllegalArgumentException("El equipo local y visitante no pueden ser iguales: " + dto.getEquipoLocal());
			}
			if (!dto.getHoraFin().isAfter(dto.getHoraInicio())) {
				throw new IllegalArgumentException("La hora de fin debe ser posterior a la de inicio en el partido " 
						+ dto.getEquipoLocal() + " vs " + dto.getEquipoVisitante());
			}

			// Resolver IDs
			Integer idLocal = mapaEquipos.get(dto.getEquipoLocal().toLowerCase());
			if (idLocal == null) {
				throw new IllegalArgumentException("El equipo local no existe en el sistema: " + dto.getEquipoLocal());
			}

			Integer idVisitante = mapaEquipos.get(dto.getEquipoVisitante().toLowerCase());
			if (idVisitante == null) {
				throw new IllegalArgumentException("El equipo visitante no existe en el sistema: " + dto.getEquipoVisitante());
			}
			
			if (idLocal.equals(idVisitante)) {
				throw new IllegalArgumentException("El equipo local y el visitante no pueden ser el mismo: "
						+ dto.getEquipoLocal() + " (ID: " + idLocal + ")");
			}

			Integer idInstalacion = mapaInstalaciones.get(dto.getInstalacion().toLowerCase());
			if (idInstalacion == null) {
				throw new IllegalArgumentException("La instalación no existe en el sistema: " + dto.getInstalacion());
			}

			// Inserción en BBDD
			jdbc.insertarPartidoImportado(idLocal, idVisitante, idInstalacion, 
					dto.getFecha(), dto.getHoraInicio(), dto.getHoraFin());
			importados++;
		}

		return importados;
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

	private Map<String, Integer> cargarMapaInstalaciones() throws SQLException {
		Map<String, Integer> mapa = new HashMap<>();
		for (Instalacion inst : jdbc.getAvailableInstalations()) {
			mapa.put(inst.getName().toLowerCase(), inst.getInstalacionId());
		}
		return mapa;
	}
}
