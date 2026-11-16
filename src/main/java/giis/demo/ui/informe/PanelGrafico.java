package giis.demo.ui.informe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import giis.demo.model.historial.TipoIngreso;
import giis.demo.model.informe.InformeFinanciero;

/**
 * Dibuja los graficos del informe financiero (barras y tarta) con Java2D.
 */
public class PanelGrafico extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum TipoGrafico {
        TOTALES_BARRAS("Ingresos, gastos y balance (barras)"), INGRESOS_TARTA("Ingresos por tipo de venta (tarta)"),
        INGRESOS_BARRAS("Ingresos por tipo de venta (barras)");

        private final String nombre;

        private TipoGrafico(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
     * Un valor del grafico: una barra o un sector de la tarta
     */
    private record Dato(String nombre, double valor, Color color) {
    }

    // Textos
    private static final String FORMATO_EUROS = "%.2f €";
    private static final String MSG_SIN_CONSULTA = "Pulse Consultar para ver el gráfico.";
    private static final String MSG_SIN_INGRESOS = "No hay ingresos en el periodo: no se puede mostrar la comparativa por tipo.";

    // Colores
    private static final Color COLOR_INGRESOS = new Color(70, 130, 180);
    private static final Color COLOR_GASTOS = new Color(230, 145, 56);
    private static final Color COLOR_BALANCE = new Color(84, 160, 90);
    private static final Color COLOR_NEGATIVO = new Color(198, 40, 40);
    private static final Color COLOR_MENSAJE = new Color(160, 0, 0);
    private static final Color COLOR_REJILLA = new Color(225, 225, 225);
    private static final Color[] COLORES_TIPO = { new Color(70, 130, 180), new Color(230, 145, 56),
            new Color(84, 160, 90), new Color(150, 110, 190), new Color(200, 90, 110) };

    // Medidas comunes en píxeles
    private static final int TAMANO_MINIMO_GRAFICO = 60;
    private static final BasicStroke TRAZO_FINO = new BasicStroke(1f);

    // Medidas del grafico de barras
    private static final int MARGEN_IZQ = 80;
    private static final int MARGEN_DER = 25;
    private static final int MARGEN_SUP = 45;
    private static final int MARGEN_INF = 80;
    private static final int MARCAS_EJE = 5;
    private static final double PROPORCION_BARRA = 0.55;
    private static final int ANCHO_MAX_BARRA = 90;
    private static final int MAX_LINEAS_ETIQUETA = 3;
    private static final BasicStroke TRAZO_EJES = new BasicStroke(1.5f);

    // Medidas del grafico de tarta
    private static final int X_TARTA = 30;
    private static final int ALTO_TITULO = 40;
    private static final int MARGEN_VERTICAL_TARTA = 80;
    private static final int ANGULO_INICIAL = 90;
    private static final int SEPARACION_LEYENDA = 40;
    private static final int LADO_MUESTRA = 14;
    private static final BasicStroke TRAZO_SECTORES = new BasicStroke(2f);

    private InformeFinanciero informe;
    private TipoGrafico tipoGrafico = TipoGrafico.TOTALES_BARRAS;

    public PanelGrafico() {
        setBackground(Color.WHITE);
    }

    public void setInforme(InformeFinanciero informe) {
        this.informe = informe;
        repaint();
    }

    public void setTipoGrafico(TipoGrafico tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
        repaint();
    }

    // Que se pinta

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont().deriveFont(Font.PLAIN));

            if (informe == null) {
                dibujarMensaje(g2, MSG_SIN_CONSULTA, Color.DARK_GRAY);
            } else {
                dibujarTitulo(g2);
                dibujarGrafico(g2);
            }
        } finally {
            g2.dispose();
        }
    }

    private void dibujarGrafico(Graphics2D g2) {
        if (tipoGrafico == TipoGrafico.TOTALES_BARRAS) {
            dibujarBarras(g2, datosTotales());
        } else if (!informe.tieneIngresos()) {
            dibujarMensaje(g2, MSG_SIN_INGRESOS, COLOR_MENSAJE);
        } else if (tipoGrafico == TipoGrafico.INGRESOS_TARTA) {
            dibujarTarta(g2, soloConImporte(datosIngresosPorTipo()));
        } else {
            dibujarBarras(g2, datosIngresosPorTipo());
        }
    }

    // Datos de cada grafico

    private List<Dato> datosTotales() {
        double balance = informe.getBalance();
        return List.of(new Dato("Ingresos", informe.getTotalIngresos(), COLOR_INGRESOS),
                new Dato("Gastos", informe.getGastos(), COLOR_GASTOS),
                new Dato("Balance", balance, balance < 0 ? COLOR_NEGATIVO : COLOR_BALANCE));
    }

    /**
     * Un dato por cada tipo de ingreso, tambien los que no han tenido ventas
     */
    private List<Dato> datosIngresosPorTipo() {
        List<Dato> datos = new ArrayList<>();
        for (TipoIngreso tipo : TipoIngreso.values()) {
            Color color = COLORES_TIPO[tipo.ordinal() % COLORES_TIPO.length];
            datos.add(new Dato(tipo.getConcepto(), informe.getIngreso(tipo), color));
        }
        return datos;
    }

    private static List<Dato> soloConImporte(List<Dato> datos) {
        List<Dato> conImporte = new ArrayList<>();
        for (Dato dato : datos) {
            if (dato.valor() > 0) {
                conImporte.add(dato);
            }
        }
        return conImporte;
    }

    // Grafico de barras

    private void dibujarBarras(Graphics2D g2, List<Dato> datos) {
        Rectangle area = new Rectangle(MARGEN_IZQ, MARGEN_SUP, getWidth() - MARGEN_IZQ - MARGEN_DER,
                getHeight() - MARGEN_SUP - MARGEN_INF);
        if (area.width < TAMANO_MINIMO_GRAFICO || area.height < TAMANO_MINIMO_GRAFICO) {
            return;
        }
        EjeVertical eje = new EjeVertical(datos, area);

        dibujarRejilla(g2, area, eje);
        dibujarEjes(g2, area, eje);

        double anchoHueco = (double) area.width / datos.size();
        for (int i = 0; i < datos.size(); i++) {
            double xHueco = area.x + i * anchoHueco;
            dibujarBarra(g2, datos.get(i), xHueco, anchoHueco, eje);
            dibujarEtiquetaBarra(g2, datos.get(i).nombre(), xHueco, anchoHueco, area);
        }
    }

    /**
     * Lineas horizontales de referencia con su importe a la izquierda
     */
    private void dibujarRejilla(Graphics2D g2, Rectangle area, EjeVertical eje) {
        FontMetrics fm = g2.getFontMetrics();
        for (double valor : eje.marcas()) {
            int y = eje.y(valor);
            g2.setColor(COLOR_REJILLA);
            g2.drawLine(area.x, y, area.x + area.width, y);
            g2.setColor(Color.DARK_GRAY);
            String marca = String.format("%,.0f", valor);
            g2.drawString(marca, area.x - 8 - fm.stringWidth(marca), y + fm.getAscent() / 2 - 1);
        }
    }

    /**
     * Eje vertical y linea del cero
     */
    private void dibujarEjes(Graphics2D g2, Rectangle area, EjeVertical eje) {
        int yCero = eje.y(0);
        g2.setColor(Color.BLACK);
        g2.setStroke(TRAZO_EJES);
        g2.drawLine(area.x, area.y, area.x, area.y + area.height);
        g2.drawLine(area.x, yCero, area.x + area.width, yCero);
        g2.setStroke(TRAZO_FINO);
    }

    /**
     * Barra centrada en su hueco, con el importe encima (o debajo si es negativo)
     */
    private void dibujarBarra(Graphics2D g2, Dato dato, double xHueco, double anchoHueco, EjeVertical eje) {
        FontMetrics fm = g2.getFontMetrics();
        int ancho = (int) Math.min(anchoHueco * PROPORCION_BARRA, ANCHO_MAX_BARRA);
        int x = (int) (xHueco + (anchoHueco - ancho) / 2);
        int yValor = eje.y(dato.valor());
        int yCero = eje.y(0);
        int yArriba = Math.min(yValor, yCero);
        int alto = Math.max(1, Math.abs(yValor - yCero));

        g2.setColor(dato.color());
        g2.fillRect(x, yArriba, ancho, alto);
        g2.setColor(dato.color().darker());
        g2.drawRect(x, yArriba, ancho, alto);

        g2.setColor(Color.BLACK);
        String importe = String.format(FORMATO_EUROS, dato.valor());
        int xImporte = x + ancho / 2 - fm.stringWidth(importe) / 2;
        int yImporte = dato.valor() >= 0 ? yArriba - 5 : yArriba + alto + fm.getAscent() + 3;
        g2.drawString(importe, xImporte, yImporte);
    }

    /**
     * Nombre de la barra bajo el eje, centrado y partido en varias lineas si no cabe
     */
    private void dibujarEtiquetaBarra(Graphics2D g2, String nombre, double xHueco, double anchoHueco, Rectangle area) {
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        int y = area.y + area.height + 8 + fm.getAscent();
        for (String linea : partirEnLineas(nombre, fm, (int) anchoHueco - 8)) {
            g2.drawString(linea, (int) (xHueco + anchoHueco / 2 - fm.stringWidth(linea) / 2.0), y);
            y += fm.getHeight();
        }
    }

    /**
     * Escala del eje vertical: convierte un importe en su coordenada y dentro del area del grafico. Los limites se
     * ajustan a marcas redondas (1, 2 o 5 por una potencia de diez) para que el eje sea legible.
     */
    private static final class EjeVertical {
        private final double paso;
        private final double suelo;
        private final double techo;
        private final Rectangle area;

        EjeVertical(List<Dato> datos, Rectangle area) {
            this.area = area;
            double max = 0;
            double min = 0;
            for (Dato dato : datos) {
                max = Math.max(max, dato.valor());
                min = Math.min(min, dato.valor());
            }
            this.paso = pasoRedondo(Math.max(max - min, MARCAS_EJE) / MARCAS_EJE);
            this.suelo = Math.floor(min / paso) * paso;
            double techoAjustado = Math.ceil(max / paso) * paso;
            this.techo = techoAjustado == suelo ? suelo + paso : techoAjustado;
        }

        int y(double valor) {
            return (int) Math.round(area.y + (techo - valor) / (techo - suelo) * area.height);
        }

        List<Double> marcas() {
            List<Double> marcas = new ArrayList<>();
            for (double valor = suelo; valor <= techo + paso / 2; valor += paso) {
                marcas.add(valor);
            }
            return marcas;
        }

        /**
         * Redondea a 1, 2, 5 o 10 por una potencia de diez
         */
        private static double pasoRedondo(double aproximado) {
            double potencia = Math.pow(10, Math.floor(Math.log10(aproximado)));
            double factor = aproximado / potencia;
            if (factor <= 1) {
                return potencia;
            } else if (factor <= 2) {
                return 2 * potencia;
            } else if (factor <= 5) {
                return 5 * potencia;
            }
            return 10 * potencia;
        }
    }

    // Grafico de tarta

    /**
     * Tarta a la izquierda y leyenda a su derecha. Solo recibe datos con importe positivo.
     */
    private void dibujarTarta(Graphics2D g2, List<Dato> datos) {
        int diametro = Math.min(getHeight() - MARGEN_VERTICAL_TARTA, (int) (getWidth() * 0.5));
        if (diametro < TAMANO_MINIMO_GRAFICO) {
            return;
        }
        int yTarta = ALTO_TITULO + (getHeight() - ALTO_TITULO - diametro) / 2;
        double total = informe.getTotalIngresos();

        dibujarSectores(g2, datos, total, yTarta, diametro);
        dibujarLeyenda(g2, datos, total, X_TARTA + diametro + SEPARACION_LEYENDA, yTarta, diametro);
    }

    /**
     * Sectores en el sentido de las agujas del reloj empezando por arriba, separados por una linea blanca
     */
    private void dibujarSectores(Graphics2D g2, List<Dato> datos, double total, int yTarta, int diametro) {
        g2.setStroke(TRAZO_SECTORES);
        double inicio = ANGULO_INICIAL;
        for (Dato dato : datos) {
            double extension = -dato.valor() / total * 360.0;
            Arc2D sector = new Arc2D.Double(X_TARTA, yTarta, diametro, diametro, inicio, extension, Arc2D.PIE);
            g2.setColor(dato.color());
            g2.fill(sector);
            g2.setColor(Color.WHITE);
            g2.draw(sector);
            inicio += extension;
        }
        g2.setStroke(TRAZO_FINO);
    }

    /**
     * Una entrada por dato, centradas en vertical respecto a la tarta: muestra de color, nombre y debajo importe y
     * porcentaje
     */
    private void dibujarLeyenda(Graphics2D g2, List<Dato> datos, double total, int x, int yTarta, int diametro) {
        FontMetrics fm = g2.getFontMetrics();
        int altoEntrada = 2 * fm.getHeight() + 10;
        int xTexto = x + LADO_MUESTRA + 8;
        int y = yTarta + (diametro - datos.size() * altoEntrada) / 2;
        for (Dato dato : datos) {
            g2.setColor(dato.color());
            g2.fillRect(x, y + 2, LADO_MUESTRA, LADO_MUESTRA);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y + 2, LADO_MUESTRA, LADO_MUESTRA);

            g2.setColor(Color.BLACK);
            g2.drawString(dato.nombre(), xTexto, y + fm.getAscent());
            g2.setColor(Color.DARK_GRAY);
            String importe = String.format(FORMATO_EUROS + " (%.1f %%)", dato.valor(), dato.valor() / total * 100.0);
            g2.drawString(importe, xTexto, y + fm.getAscent() + fm.getHeight());
            y += altoEntrada;
        }
    }

    // Textos

    private void dibujarTitulo(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        Font normal = g2.getFont();
        g2.setFont(normal.deriveFont(Font.BOLD, normal.getSize2D() + 1f));
        g2.drawString(tipoGrafico + " - " + informe.getPeriodo().getEtiqueta(), 12, 22);
        g2.setFont(normal);
    }

    /**
     * Texto centrado en el panel, para cuando no hay grafico que mostrar
     */
    private void dibujarMensaje(Graphics2D g2, String texto, Color color) {
        g2.setColor(color);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto, Math.max(10, (getWidth() - fm.stringWidth(texto)) / 2), getHeight() / 2);
    }

    /**
     * Divide un texto en como mucho MAX_LINEAS_ETIQUETA lineas que quepan en el ancho dado. Lo que no quepa se recorta
     * con puntos suspensivos.
     */
    private static List<String> partirEnLineas(String texto, FontMetrics fm, int ancho) {
        List<String> lineas = new ArrayList<>();
        for (String linea : limitarLineas(ajustarAlAncho(texto, fm, ancho))) {
            lineas.add(recortar(linea, fm, ancho));
        }
        return lineas;
    }

    /**
     * Reparte las palabras en lineas
     */
    private static List<String> ajustarAlAncho(String texto, FontMetrics fm, int ancho) {
        List<String> lineas = new ArrayList<>();
        String actual = "";
        for (String palabra : texto.split(" ")) {
            String ampliada = actual.isEmpty() ? palabra : actual + " " + palabra;
            if (actual.isEmpty() || fm.stringWidth(ampliada) <= ancho) {
                actual = ampliada;
            } else {
                lineas.add(actual);
                actual = palabra;
            }
        }
        lineas.add(actual);
        return lineas;
    }

    /**
     * Si sobran lineas, se juntan todas las que sobran en la ultima permitida
     */
    private static List<String> limitarLineas(List<String> lineas) {
        if (lineas.size() <= MAX_LINEAS_ETIQUETA) {
            return lineas;
        }
        int ultima = MAX_LINEAS_ETIQUETA - 1;
        List<String> limitadas = new ArrayList<>(lineas.subList(0, ultima));
        limitadas.add(String.join(" ", lineas.subList(ultima, lineas.size())));
        return limitadas;
    }

    /**
     * Si la linea no cabe, se recorta hasta que quepa y le pone puntos suspensivos
     */
    private static String recortar(String linea, FontMetrics fm, int ancho) {
        if (linea.length() <= 1 || fm.stringWidth(linea) <= ancho) {
            return linea;
        }
        String recortada = linea;
        while (recortada.length() > 1 && fm.stringWidth(recortada) > ancho) {
            recortada = recortada.substring(0, recortada.length() - 1);
        }
        return recortada.trim() + "…";
    }
}
