package giis.demo.ui.informe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import giis.demo.model.historial.TipoIngreso;
import giis.demo.model.informe.InformeFinanciero;

/**
 * Dibuja los graficos del informe financiero (barras y tarta) con Java2D
 */
public class PanelGrafico extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum TipoGrafico {
        TOTALES_BARRAS("Ingresos, gastos y balance (barras)"),
        INGRESOS_TARTA("Ingresos por tipo de venta (tarta)"),
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

    private static final String FORMATO_EUROS = "%.2f €";
    private static final String MSG_SIN_CONSULTA = "Pulse Consultar para ver el gráfico.";
    private static final String MSG_SIN_INGRESOS = "No hay ingresos en el periodo: no se puede mostrar la comparativa por tipo.";

    private static final Color COLOR_INGRESOS = new Color(70, 130, 180);
    private static final Color COLOR_GASTOS = new Color(230, 145, 56);
    private static final Color COLOR_BALANCE = new Color(84, 160, 90);
    private static final Color COLOR_NEGATIVO = new Color(198, 40, 40);
    private static final Color COLOR_MENSAJE = new Color(160, 0, 0);
    private static final Color COLOR_REJILLA = new Color(225, 225, 225);
    private static final Color[] COLORES_TIPO = { new Color(70, 130, 180), new Color(230, 145, 56),
            new Color(84, 160, 90), new Color(150, 110, 190), new Color(200, 90, 110) };

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(getFont().deriveFont(Font.PLAIN));
        try {
            if (informe == null) {
                mensaje(g2, MSG_SIN_CONSULTA, Color.DARK_GRAY);
                return;
            }
            dibujarTitulo(g2);
            switch (tipoGrafico) {
            case TOTALES_BARRAS:
                barras(g2, new String[] { "Ingresos", "Gastos", "Balance" },
                        new double[] { informe.getTotalIngresos(), informe.getGastos(), informe.getBalance() },
                        new Color[] { COLOR_INGRESOS, COLOR_GASTOS,
                                informe.getBalance() < 0 ? COLOR_NEGATIVO : COLOR_BALANCE });
                break;
            case INGRESOS_TARTA:
                if (informe.tieneIngresos()) {
                    tarta(g2);
                } else {
                    mensaje(g2, MSG_SIN_INGRESOS, COLOR_MENSAJE);
                }
                break;
            default:
                if (informe.tieneIngresos()) {
                    TipoIngreso[] tipos = TipoIngreso.values();
                    String[] nombres = new String[tipos.length];
                    double[] valores = new double[tipos.length];
                    Color[] colores = new Color[tipos.length];
                    for (int i = 0; i < tipos.length; i++) {
                        nombres[i] = tipos[i].getConcepto();
                        valores[i] = informe.getIngreso(tipos[i]);
                        colores[i] = colorDe(tipos[i]);
                    }
                    barras(g2, nombres, valores, colores);
                } else {
                    mensaje(g2, MSG_SIN_INGRESOS, COLOR_MENSAJE);
                }
                break;
            }
        } finally {
            g2.dispose();
        }
    }

    private static Color colorDe(TipoIngreso tipo) {
        return COLORES_TIPO[tipo.ordinal() % COLORES_TIPO.length];
    }

    private void dibujarTitulo(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        Font normal = g2.getFont();
        g2.setFont(normal.deriveFont(Font.BOLD, normal.getSize2D() + 1f));
        g2.drawString(tipoGrafico + " - " + informe.getPeriodo().getEtiqueta(), 12, 22);
        g2.setFont(normal);
    }

    private void mensaje(Graphics2D g2, String texto, Color color) {
        g2.setColor(color);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto, Math.max(10, (getWidth() - fm.stringWidth(texto)) / 2), getHeight() / 2);
    }

    // Barras

    private void barras(Graphics2D g2, String[] nombres, double[] valores, Color[] colores) {
        FontMetrics fm = g2.getFontMetrics();
        int izq = 80, der = 25, sup = 45, inf = 80;
        int ancho = getWidth() - izq - der;
        int alto = getHeight() - sup - inf;
        if (ancho < 60 || alto < 60) {
            return;
        }

        double max = 0, min = 0;
        for (double v : valores) {
            max = Math.max(max, v);
            min = Math.min(min, v);
        }
        double paso = pasoBonito(Math.max(max - min, 5.0) / 5.0);
        double techo = Math.ceil(max / paso) * paso;
        double suelo = Math.floor(min / paso) * paso;
        if (techo == suelo) {
            techo = suelo + paso;
        }

        // rejilla y marcas del eje vertical
        for (double v = suelo; v <= techo + paso / 2; v += paso) {
            int y = escalaY(v, suelo, techo, sup, alto);
            g2.setColor(COLOR_REJILLA);
            g2.drawLine(izq, y, izq + ancho, y);
            g2.setColor(Color.DARK_GRAY);
            String marca = String.format("%,.0f", v);
            g2.drawString(marca, izq - 8 - fm.stringWidth(marca), y + fm.getAscent() / 2 - 1);
        }
        int yCero = escalaY(0, suelo, techo, sup, alto);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(izq, sup, izq, sup + alto);
        g2.drawLine(izq, yCero, izq + ancho, yCero);
        g2.setStroke(new BasicStroke(1f));

        double hueco = (double) ancho / nombres.length;
        int anchoBarra = (int) Math.min(hueco * 0.55, 90);
        for (int i = 0; i < nombres.length; i++) {
            int x = (int) (izq + i * hueco + (hueco - anchoBarra) / 2);
            int yValor = escalaY(valores[i], suelo, techo, sup, alto);
            int yArriba = Math.min(yValor, yCero);
            int altoBarra = Math.max(1, Math.abs(yValor - yCero));
            g2.setColor(colores[i]);
            g2.fillRect(x, yArriba, anchoBarra, altoBarra);
            g2.setColor(colores[i].darker());
            g2.drawRect(x, yArriba, anchoBarra, altoBarra);

            g2.setColor(Color.BLACK);
            String importe = String.format(FORMATO_EUROS, valores[i]);
            int xTexto = x + anchoBarra / 2 - fm.stringWidth(importe) / 2;
            if (valores[i] >= 0) {
                g2.drawString(importe, xTexto, yArriba - 5);
            } else {
                g2.drawString(importe, xTexto, yArriba + altoBarra + fm.getAscent() + 3);
            }

            int yEtiqueta = sup + alto + 8 + fm.getAscent();
            for (String linea : partir(nombres[i], fm, (int) hueco - 8)) {
                g2.drawString(linea, (int) (izq + i * hueco + hueco / 2 - fm.stringWidth(linea) / 2.0), yEtiqueta);
                yEtiqueta += fm.getHeight();
            }
        }
    }

    private static int escalaY(double valor, double suelo, double techo, int sup, int alto) {
        return (int) Math.round(sup + (techo - valor) / (techo - suelo) * alto);
    }

    /**
     * Redondea a 1, 2, 5 o 10 por una potencia de diez, para marcas del eje legibles
     */
    private static double pasoBonito(double aproximado) {
        double potencia = Math.pow(10, Math.floor(Math.log10(aproximado)));
        double f = aproximado / potencia;
        double bonito = f <= 1 ? 1 : f <= 2 ? 2 : f <= 5 ? 5 : 10;
        return bonito * potencia;
    }

    /**
     * Divide un texto en como mucho tres lineas que quepan en el ancho dado
     */
    private static List<String> partir(String texto, FontMetrics fm, int ancho) {
        List<String> lineas = new ArrayList<>();
        String actual = "";
        for (String palabra : texto.split(" ")) {
            String prueba = actual.isEmpty() ? palabra : actual + " " + palabra;
            if (fm.stringWidth(prueba) <= ancho || actual.isEmpty()) {
                actual = prueba;
            } else {
                lineas.add(actual);
                actual = palabra;
            }
        }
        lineas.add(actual);
        if (lineas.size() > 3) {
            String resto = String.join(" ", lineas.subList(2, lineas.size()));
            lineas = new ArrayList<>(List.of(lineas.get(0), lineas.get(1), resto));
        }
        for (int i = 0; i < lineas.size(); i++) {
            String l = lineas.get(i);
            while (l.length() > 1 && fm.stringWidth(l) > ancho) {
                l = l.substring(0, l.length() - 1);
                lineas.set(i, l.trim() + "…");
            }
        }
        return lineas;
    }

    // Tarta

    private void tarta(Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics();
        int diametro = Math.min(getHeight() - 80, (int) (getWidth() * 0.5));
        if (diametro < 60) {
            return;
        }
        int xTarta = 30;
        int yTarta = 40 + (getHeight() - 40 - diametro) / 2;
        double total = informe.getTotalIngresos();

        double inicio = 90;
        for (TipoIngreso tipo : TipoIngreso.values()) {
            double importe = informe.getIngreso(tipo);
            if (importe <= 0) {
                continue;
            }
            double extension = -importe / total * 360.0;
            g2.setColor(colorDe(tipo));
            g2.fill(new Arc2D.Double(xTarta, yTarta, diametro, diametro, inicio, extension, Arc2D.PIE));
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new Arc2D.Double(xTarta, yTarta, diametro, diametro, inicio, extension, Arc2D.PIE));
            inicio += extension;
        }
        g2.setStroke(new BasicStroke(1f));

        // leyenda: nombre del tipo y debajo importe y porcentaje
        int xLeyenda = xTarta + diametro + 40;
        int alturaEntrada = 2 * fm.getHeight() + 10;
        int entradas = 0;
        for (TipoIngreso tipo : TipoIngreso.values()) {
            if (informe.getIngreso(tipo) > 0) {
                entradas++;
            }
        }
        int y = yTarta + (diametro - entradas * alturaEntrada) / 2;
        for (TipoIngreso tipo : TipoIngreso.values()) {
            double importe = informe.getIngreso(tipo);
            if (importe <= 0) {
                continue;
            }
            g2.setColor(colorDe(tipo));
            g2.fillRect(xLeyenda, y + 2, 14, 14);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(xLeyenda, y + 2, 14, 14);
            g2.setColor(Color.BLACK);
            g2.drawString(tipo.getConcepto(), xLeyenda + 22, y + fm.getAscent());
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.format(FORMATO_EUROS + " (%.1f %%)", importe, importe / total * 100.0),
                    xLeyenda + 22, y + fm.getAscent() + fm.getHeight());
            y += alturaEntrada;
        }
    }
}
