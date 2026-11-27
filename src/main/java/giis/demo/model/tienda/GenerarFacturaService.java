package giis.demo.model.tienda;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import giis.demo.model.log.GestionLogSesion;

public class GenerarFacturaService {

	private static final String DIRECTORIO_FACTURAS = "facturas";
	private static final String NIF_CLUB = "A-33004589";
	private static final String DOMICILIO_CLUB = "Calle Alejandro Casona s/n, 33013 Oviedo (Asturias)";
	private static final double TIPO_IVA = 0.21;

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

	private String numeroFactura;
	private LocalDate fecha;
	private LocalTime hora;
	private String emisor;
	private String nifVendedor;
	private String domicilioVendedor;
	private String rutaPdfDestino;

	private List<LineaFacturaDTO> lineasVenta;
	private int unidadesTotales;
	private double totalFactura;
	private double precioBase;
	private double iva;
	
	public GenerarFacturaService() {
		this.nifVendedor = NIF_CLUB;
		this.domicilioVendedor = DOMICILIO_CLUB;
	}

	public GenerarFacturaService(Map<Producto, Integer> productosVendidos) {
		this();
		prepararFactura(productosVendidos);
	}

	public GenerarFacturaService(Map<Producto, Integer> productosVendidos, String nombreVendedor) {
		this();
		prepararFactura(convertirProductosALineas(productosVendidos), nombreVendedor);
	}

	public GenerarFacturaService(List<LineaFacturaDTO> lineasVendidas, String nombreVendedor) {
		this();
		prepararFactura(lineasVendidas, nombreVendedor);
	}

	/**
	 * Carga en el servicio los productos de la venta recién realizada y genera
	 * un nuevo número de factura, fecha, hora y totales.
	 */
	public void prepararFactura(Map<Producto, Integer> productosVendidos) {
		prepararFactura(convertirProductosALineas(productosVendidos), obtenerEmisorPorDefecto());
	}
	
	public void prepararFactura(List<LineaFacturaDTO> lineasVendidas) {
		prepararFactura(lineasVendidas, obtenerEmisorPorDefecto());
	}
	
	public void prepararFactura(List<LineaFacturaDTO> lineasVendidas, String nombreVendedor) {
		this.fecha = LocalDate.now();
		this.hora = LocalTime.now();
		this.numeroFactura = generarSiguienteNumeroFactura();
		this.rutaPdfDestino = DIRECTORIO_FACTURAS + "/" + numeroFactura + ".pdf";
		
		this.emisor = (nombreVendedor != null && !nombreVendedor.isBlank())
				? nombreVendedor
				: obtenerEmisorPorDefecto();
		this.nifVendedor = NIF_CLUB;
		this.domicilioVendedor = DOMICILIO_CLUB;
		
		this.lineasVenta = (lineasVendidas != null) ? new ArrayList<>(lineasVendidas) : new ArrayList<>();
		int uds = 0;
		double total = 0.0;
		for (LineaFacturaDTO linea : this.lineasVenta) {
			uds += linea.getUnidades();
			total += linea.getSubtotal();
		}
		
		this.unidadesTotales = uds;
		this.totalFactura = total;
		this.precioBase = totalFactura / (1.0 + TIPO_IVA);
		this.iva = totalFactura - this.precioBase;
	}
	
	private static List<LineaFacturaDTO> convertirProductosALineas(Map<Producto, Integer> productosVendidos) {
		List<LineaFacturaDTO> lineas = new ArrayList<>();
		if (productosVendidos != null) {
			for (Map.Entry<Producto, Integer> entry : productosVendidos.entrySet()) {
				Producto p = entry.getKey();
				int cantidad = entry.getValue();
				lineas.add(new LineaFacturaDTO(p.getNombre(), cantidad, p.getPrecio()));
			}
		}
		return lineas;
	}

	private static String obtenerEmisorPorDefecto() {
		String usuarioActivo = GestionLogSesion.getUsuarioActivo();
		if (usuarioActivo != null && !usuarioActivo.isBlank() && !"anonimo".equalsIgnoreCase(usuarioActivo)) {
			return usuarioActivo + " (Club Deportivo)";
		}
		return "Club Deportivo - Tienda Oficial";
	}

	/**
	 * Genera un código secuencial con formato FAC-AAAA-XXXXX revisando las facturas ya existentes.
	 */
	private String generarSiguienteNumeroFactura() {
		int anio = fecha.getYear();
		String prefijo = "FAC-" + anio + "-";
		int maxNumero = 0;

		File dir = new File(DIRECTORIO_FACTURAS);
		if (dir.exists() && dir.isDirectory()) {
			File[] archivos = dir.listFiles();
			if (archivos != null) {
				for (File f : archivos) {
					String nombre = f.getName();
					if (nombre.startsWith(prefijo) && nombre.endsWith(".pdf")) {
						String parteNum = nombre.substring(prefijo.length(), nombre.length() - 4);
						try {
							int n = Integer.parseInt(parteNum);
							if (n > maxNumero) {
								maxNumero = n;
							}
						} catch (NumberFormatException ignored) {
						}
					}
				}
			}
		}
		return String.format("%s%05d", prefijo, maxNumero + 1);
	}

	/**
	 * Valida los datos fiscales del comprador y genera el PDF de la factura en disco.
	 * @return Ruta relativa donde se ha guardado el PDF generado.
	 */
	public String generarFactura(String dniComprador, String nombreComprador,
			String usuarioComprador, String domicilioComprador) throws IOException {

		if (lineasVenta.isEmpty()) {
			throw new IllegalArgumentException("No hay líneas de venta registradas para facturar.");
		}
		if (dniComprador == null || dniComprador.trim().isEmpty()) {
			throw new IllegalArgumentException("Por favor, introduzca el DNI/NIF del comprador.");
		}
		if (nombreComprador == null || nombreComprador.trim().isEmpty()) {
			throw new IllegalArgumentException("Por favor, introduzca el Nombre y Apellidos del comprador.");
		}

		File dirFacturas = new File(DIRECTORIO_FACTURAS);
		if (!dirFacturas.exists()) {
			dirFacturas.mkdirs();
		}

		File archivoPdf = new File(rutaPdfDestino);
		escribirPdf(archivoPdf, dniComprador.trim(), nombreComprador.trim(),
				usuarioComprador != null ? usuarioComprador.trim() : "",
				domicilioComprador != null ? domicilioComprador.trim() : "");

		GestionLogSesion.registrarAccion("Factura generada: " + rutaPdfDestino);
		return rutaPdfDestino;
	}

	/**
	 * Construye y escribe un documento PDF estándar 1.4 sin dependencias externas.
	 */
	private void escribirPdf(File archivo, String dniComprador, String nombreComprador,
			String usuarioComprador, String domicilioComprador) throws IOException {

		StringBuilder stream = new StringBuilder();
		stream.append("BT\n");
		stream.append("/F1 18 Tf\n");
		stream.append("50 785 Td\n");
		stream.append("(FACTURA DE VENTA) Tj\n");
		stream.append("0 -28 Td\n");
		stream.append("/F1 11 Tf\n");
		stream.append("(")
			.append(escaparPdf("N. Factura: " + numeroFactura + "    Fecha: " + getFechaFormateada() + "    Hora: " + getHoraFormateada()))
			.append(") Tj\n");

		// Emisor / Vendedor
		stream.append("0 -34 Td\n");
		stream.append("/F1 12 Tf\n");
		stream.append("(DATOS DE LA FACTURA Y VENDEDOR:) Tj\n");
		stream.append("0 -20 Td\n");
		stream.append("/F1 10 Tf\n");
		stream.append("(").append(escaparPdf("Emisor / Vendedor: " + emisor + "    NIF: " + nifVendedor)).append(") Tj\n");
		stream.append("0 -18 Td\n");
		stream.append("(").append(escaparPdf("Domicilio fiscal: " + domicilioVendedor)).append(") Tj\n");

		// Comprador
		stream.append("0 -34 Td\n");
		stream.append("/F1 12 Tf\n");
		stream.append("(DATOS DEL COMPRADOR:) Tj\n");
		stream.append("0 -20 Td\n");
		stream.append("/F1 10 Tf\n");
		stream.append("(").append(escaparPdf("DNI/NIF: " + dniComprador + "    Nombre y Apellidos: " + nombreComprador)).append(") Tj\n");
		if (!usuarioComprador.isEmpty()) {
			stream.append("0 -18 Td\n");
			stream.append("(").append(escaparPdf("Nombre de usuario: " + usuarioComprador)).append(") Tj\n");
		}
		if (!domicilioComprador.isEmpty()) {
			stream.append("0 -18 Td\n");
			stream.append("(").append(escaparPdf("Domicilio fiscal: " + domicilioComprador)).append(") Tj\n");
		}

		// Líneas de la venta
		stream.append("0 -36 Td\n");
		stream.append("/F1 12 Tf\n");
		stream.append("(LINEAS DE LA VENTA:) Tj\n");
		stream.append("0 -20 Td\n");
		stream.append("/F1 10 Tf\n");
		stream.append("(--------------------------------------------------------------------------------) Tj\n");
		stream.append("0 -16 Td\n");
		stream.append("(Producto                                    Uds.    Precio Unit.        Subtotal) Tj\n");
		stream.append("0 -14 Td\n");
		stream.append("(--------------------------------------------------------------------------------) Tj\n");

		for (LineaFacturaDTO linea : lineasVenta) {
			List<String> partes = envolverTexto(linea.getProducto(), 42);
			stream.append("0 -18 Td\n");
			String primeraFila = String.format("%-42s  %4d  %10.2f EUR  %10.2f EUR",
					partes.get(0),
					linea.getUnidades(),
					linea.getPrecioUnitario(),
					linea.getSubtotal());
			stream.append("(").append(escaparPdf(primeraFila)).append(") Tj\n");
			for (int i = 1; i < partes.size(); i++) {
				stream.append("0 -14 Td\n");
				stream.append("(").append(escaparPdf(partes.get(i))).append(") Tj\n");
			}
		}

		stream.append("0 -16 Td\n");
		stream.append("(--------------------------------------------------------------------------------) Tj\n");
		stream.append("0 -28 Td\n");
		stream.append("/F1 11 Tf\n");
		stream.append("(").append(escaparPdf(String.format("Precio Base: %.2f EUR", precioBase))).append(") Tj\n");
		stream.append("0 -18 Td\n");
		stream.append("(").append(escaparPdf(String.format("IVA (21%%):   %.2f EUR", iva))).append(") Tj\n");
		stream.append("0 -24 Td\n");
		stream.append("/F1 13 Tf\n");
		stream.append("(").append(escaparPdf(String.format("TOTAL FACTURA: %.2f EUR", totalFactura))).append(") Tj\n");
		stream.append("ET\n");

		byte[] streamBytes = stream.toString().getBytes(StandardCharsets.ISO_8859_1);

		StringBuilder pdf = new StringBuilder();
		pdf.append("%PDF-1.4\n");
		pdf.append("%\u00E2\u00E3\u00CF\u00D3\n");

		List<Long> offsets = new ArrayList<>();
		offsets.add((long) pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length);
		pdf.append("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

		offsets.add((long) pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length);
		pdf.append("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

		offsets.add((long) pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length);
		pdf.append("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n");

		offsets.add((long) pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length);
		pdf.append("4 0 obj\n<< /Length ").append(streamBytes.length).append(" >>\nstream\n");
		pdf.append(stream.toString());
		pdf.append("endstream\nendobj\n");

		offsets.add((long) pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length);
		pdf.append("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>\nendobj\n");

		long startXref = pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length;
		pdf.append("xref\n0 6\n0000000000 65535 f \n");
		for (Long off : offsets) {
			pdf.append(String.format("%010d 00000 n \n", off));
		}
		pdf.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n");
		pdf.append(startXref).append("\n%%EOF\n");

		try (FileOutputStream fos = new FileOutputStream(archivo)) {
			fos.write(pdf.toString().getBytes(StandardCharsets.ISO_8859_1));
		}
	}

	private String escaparPdf(String texto) {
		if (texto == null) {
			return "";
		}
		return texto.replace("\\", "\\\\")
				.replace("(", "\\(")
				.replace(")", "\\)")
				.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
				.replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
				.replace("ñ", "n").replace("Ñ", "N").replace("º", "o");
	}

	/**
	 * Divide un texto largo en múltiples líneas de máximo {@code maxAncho} caracteres
	 * respetando palabras cuando es posible para mostrar todo el concepto sin recortes.
	 */
	private List<String> envolverTexto(String texto, int maxAncho) {
		List<String> lineas = new ArrayList<>();
		if (texto == null || texto.isBlank()) {
			lineas.add("");
			return lineas;
		}
		String restante = texto.trim();
		while (restante.length() > maxAncho) {
			int corte = restante.lastIndexOf(' ', maxAncho);
			if (corte <= 0) {
				corte = maxAncho;
			}
			lineas.add(restante.substring(0, corte).trim());
			restante = restante.substring(corte).trim();
		}
		if (!restante.isEmpty()) {
			lineas.add(restante);
		}
		return lineas;
	}

	// Getters para la vista
	public String getNumeroFactura() {
		return numeroFactura;
	}

	public String getFechaFormateada() {
		return fecha.format(FORMATO_FECHA);
	}

	public String getHoraFormateada() {
		return hora.format(FORMATO_HORA);
	}

	public String getEmisor() {
		return emisor;
	}

	public String getNifVendedor() {
		return nifVendedor;
	}

	public String getDomicilioVendedor() {
		return domicilioVendedor;
	}

	public String getRutaPdfDestino() {
		return rutaPdfDestino;
	}

	public List<LineaFacturaDTO> getLineasVenta() {
		return Collections.unmodifiableList(lineasVenta);
	}

	public int getUnidadesTotales() {
		return unidadesTotales;
	}

	public double getPrecioBase() {
		return precioBase;
	}

	public double getIva() {
		return iva;
	}

	public double getTotalFactura() {
		return totalFactura;
	}
}