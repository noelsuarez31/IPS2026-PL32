package giis.demo.model.email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResumenCompra {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String MENSAJE_IMPORTANTE = "Conserve este correo como justificante de su compra.";

    // Colores del correo
    private static final String COLOR_PRINCIPAL = "#FF0000"; // cabecera de la tabla y titulo
    private static final String COLOR_FILA_PAR = "#f1f8e9";
    private static final String COLOR_TOTAL = "#FDFF6B";
    private static final String COLOR_AVISO_FONDO = "#A3B7FF";
    private static final String COLOR_AVISO_BORDE = "#005AFF";

    private static class Linea {
        final String descripcion;
        final int unidades;
        final double precioUnitario;

        Linea(String descripcion, int unidades, double precioUnitario) {
            this.descripcion = descripcion;
            this.unidades = unidades;
            this.precioUnitario = precioUnitario;
        }

        double subtotal() {
            return unidades * precioUnitario;
        }
    }

    private final String tipoVenta;
    private final String concepto;
    private final LocalDateTime fechaHora;
    private final List<Linea> lineas = new ArrayList<>();
    private final double total;

    public ResumenCompra(String tipoVenta, String concepto, LocalDateTime fechaHora, double total) {
        this.tipoVenta = tipoVenta;
        this.concepto = concepto;
        this.fechaHora = fechaHora;
        this.total = total;
    }

    /** Añade una linea de la compra */
    public void addLinea(String descripcion, int unidades, double precioUnitario) {
        lineas.add(new Linea(descripcion, unidades, precioUnitario));
    }

    public static String precio(double importe) {
        return String.format(Locale.forLanguageTag("es-ES"), "%.2f €", importe);
    }

    public String getAsunto() {
        return "Resumen de su compra - " + tipoVenta;
    }

    /** Version en texto plano */
    public String getCuerpo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gracias por su compra. Este es el resumen:\n\n");
        sb.append("Concepto: ").append(concepto).append("\n");
        sb.append("Fecha y hora: ").append(fechaHora.format(FORMATO_FECHA)).append("\n\n");
        sb.append("Detalle:\n");
        for (Linea l : lineas)
            sb.append(String.format("- %s: %d x %s = %s%n", l.descripcion, l.unidades, precio(l.precioUnitario),
                    precio(l.subtotal())));
        sb.append("\nTotal: ").append(precio(total)).append("\n\n");
        sb.append(MENSAJE_IMPORTANTE).append("\n\n");
        sb.append("Un saludo,\nEl club");
        return sb.toString();
    }

    /** Version HTML: detalle en tabla, total y aviso importante en negrita, con color */
    public String getCuerpoHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='margin:0;padding:0;background:#f4f4f4;'>");
        sb.append("<table width='100%' cellpadding='0' cellspacing='0' style='background:#f4f4f4;'><tr>");
        sb.append("<td align='center' style='padding:20px;'>");
        sb.append("<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;");
        sb.append("border:1px solid #dddddd;font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#333333;'>");

        // Cabecera
        sb.append("<tr><td style='background:").append(COLOR_PRINCIPAL).append(";color:#ffffff;padding:18px 24px;");
        sb.append("font-size:20px;font-weight:bold;'>Resumen de su compra</td></tr>");

        sb.append("<tr><td style='padding:24px;'>");
        sb.append("<p style='margin:0 0 16px 0;'>Gracias por su compra. Este es el resumen:</p>");

        // Concepto y fecha
        sb.append("<p style='margin:0 0 4px 0;'><b>Concepto:</b> ").append(escapar(concepto)).append("</p>");
        sb.append("<p style='margin:0 0 20px 0;'><b>Fecha y hora:</b> ").append(fechaHora.format(FORMATO_FECHA))
                .append("</p>");

        // Tabla de detalle
        sb.append("<table width='100%' cellpadding='8' cellspacing='0' style='border-collapse:collapse;'>");
        sb.append("<tr style='background:").append(COLOR_PRINCIPAL).append(";color:#ffffff;'>");
        sb.append("<th align='left'>Producto</th><th align='right'>Unidades</th>");
        sb.append("<th align='right'>Precio unitario</th><th align='right'>Subtotal</th></tr>");
        int i = 0;
        for (Linea l : lineas) {
            String fondo = (i++ % 2 == 1) ? COLOR_FILA_PAR : "#ffffff";
            sb.append("<tr style='background:").append(fondo).append(";border-bottom:1px solid #e0e0e0;'>");
            sb.append("<td>").append(escapar(l.descripcion)).append("</td>");
            sb.append("<td align='right'>").append(l.unidades).append("</td>");
            sb.append("<td align='right'>").append(precio(l.precioUnitario)).append("</td>");
            sb.append("<td align='right'>").append(precio(l.subtotal())).append("</td></tr>");
        }
        sb.append("<tr style='background:").append(COLOR_TOTAL).append(";font-size:16px;'>");
        sb.append("<td colspan='3' align='right'><b>Total</b></td>");
        sb.append("<td align='right'><b>").append(precio(total)).append("</b></td></tr>");
        sb.append("</table>");

        // Mensaje importante
        sb.append("<p style='margin:24px 0 0 0;padding:12px 16px;background:").append(COLOR_AVISO_FONDO);
        sb.append(";border-left:4px solid ").append(COLOR_AVISO_BORDE).append(";'><b>");
        sb.append(MENSAJE_IMPORTANTE).append("</b></p>");

        sb.append("<p style='margin:24px 0 0 0;'>Un saludo,<br>El club</p>");
        sb.append("</td></tr></table></td></tr></table></body></html>");
        return sb.toString();
    }

    /** Evita que un nombre con &, < o > rompa el HTML */
    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
