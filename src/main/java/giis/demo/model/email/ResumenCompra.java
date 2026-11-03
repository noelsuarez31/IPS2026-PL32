package giis.demo.model.email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResumenCompra {
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String tipoVenta;
    private final String concepto;
    private final LocalDateTime fechaHora;
    private final List<String> detalle = new ArrayList<>();
    private final double total;

    public ResumenCompra(String tipoVenta, String concepto, LocalDateTime fechaHora, double total) {
        this.tipoVenta = tipoVenta;
        this.concepto = concepto;
        this.fechaHora = fechaHora;
        this.total = total;
    }

    public void addDetalle(String linea) {
        detalle.add(linea);
    }

    public static String precio(double importe) {
        return String.format(Locale.forLanguageTag("es-ES"), "%.2f €", importe);
    }

    public String getAsunto() {
        return "Resumen de su compra - " + tipoVenta;
    }

    public String getCuerpo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gracias por su compra. Este es el resumen:\n\n");
        sb.append("Concepto: ").append(concepto).append("\n");
        sb.append("Fecha y hora: ").append(fechaHora.format(FORMATO_FECHA)).append("\n\n");
        sb.append("Detalle:\n");
        for (String linea : detalle)
            sb.append(linea).append("\n");
        sb.append("\nTotal: ").append(precio(total)).append("\n\n");
        sb.append("Conserve este correo como justificante de su compra.\n\n");
        sb.append("Un saludo,\nEl club");
        return sb.toString();
    }
}
