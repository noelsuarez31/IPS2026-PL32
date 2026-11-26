package giis.demo.ui.informe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import giis.demo.model.historial.TipoIngreso;
import giis.demo.model.informe.GrupoInforme;
import giis.demo.model.informe.InformeFinanciero;

/**
 * Dibuja los graficos del informe financiero (barras y tarta) con Java2D.
 * <p>
 * Las barras se agrupan por tramos del periodo (meses del año, semanas del mes o la semana entera) y el importe de cada
 * barra se ve al pasar el raton por encima. La tarta muestra el total del periodo.
 * <p>
 * La clase esta ordenada de arriba abajo: que se puede pintar ({@link TipoGrafico}), que datos lleva cada grafico, y
 * despues como se dibuja cada uno (barras, tarta) y los textos.
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

    /**
     * Las barras de un tramo del periodo, con el texto que va debajo y el que sale al pasar el raton
     */
    private record Grupo(String etiqueta, String descripcion, List<Dato> datos) {
    }

    /**
     * Una entrada de la leyenda
     */
    private record Serie(String nombre, Color color) {
    }

    /**
     * Zona de una barra y el texto que se muestra al pasar el raton por ella
     */
    private record Zona(Rectangle area, String texto) {
    }

    // Textos
    private static final String FORMATO_EUROS = "%.2f €";
    private static final String FORMATO_EUROS_DETALLE = "%,.2f €";
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
    private static final List<Serie> LEYENDA_TOTALES = List.of(new Serie("Ingresos", COLOR_INGRESOS),
            new Serie("Gastos", COLOR_GASTOS), new Serie("Balance positivo", COLOR_BALANCE),
            new Serie("Balance negativo", COLOR_NEGATIVO));

    // Medidas comunes (en pixeles)
    private static final int TAMANO_MINIMO_GRAFICO = 60;
    private static final int LADO_MUESTRA = 14;
    private static final BasicStroke TRAZO_FINO = new BasicStroke(1f);

    // Medidas del grafico de barras
    private static final int MARGEN_IZQ = 80;
    private static final int MARGEN_DER = 25;
    private static final int MARGEN_SUP = 45;
    private static final int MARGEN_INF = 8;
    private static final int SEPARACION_ETIQUETAS = 8;
    private static final int SEPARACION_LEYENDA = 12;
    private static final int MARCAS_EJE = 8;
    private static final double PROPORCION_GRUPO = 0.8;
    private static final int ANCHO_MAX_BARRA = 40;
    private static final int ANCHO_MIN_BARRA = 2;
    private static final int ALTO_MIN_ZONA = 6;
    private static final int MAX_LINEAS_ETIQUETA = 2;
    private static final int HUECO_LEYENDA = 22;
    private static final BasicStroke TRAZO_EJES = new BasicStroke(1.5f);

    // Medidas del grafico de tarta
    private static final int X_TARTA = 30;
    private static final int ALTO_TITULO = 40;
    private static final int MARGEN_VERTICAL_TARTA = 80;
    private static final int ANGULO_INICIAL = 90;
    private static final int SEPARACION_LEYENDA_TARTA = 40;
    private static final BasicStroke TRAZO_SECTORES = new BasicStroke(2f);

    private InformeFinanciero informe;
    private TipoGrafico tipoGrafico = TipoGrafico.TOTALES_BARRAS;
    private final transient List<Zona> zonas = new ArrayList<>();

    public PanelGrafico() {
        setBackground(Color.WHITE);
        // registra el panel en el gestor de tooltips; el texto depende de la barra
        setToolTipText("");
    }

    public void setInforme(InformeFinanciero informe) {
        this.informe = informe;
        repaint();
    }

    public void setTipoGrafico(TipoGrafico tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
        repaint();
    }

    /**
     * Importe de la barra que hay bajo el raton, o nada si no hay ninguna
     */
    @Override
    public String getToolTipText(MouseEvent e) {
        for (Zona zona : zonas) {
            if (zona.area().contains(e.getPoint())) {
                return zona.texto();
            }
        }
        return null;
    }

    // Que se pinta

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        zonas.clear();
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
            dibujarBarras(g2, gruposTotales(), LEYENDA_TOTALES);
        } else if (!informe.tieneIngresos()) {
            dibujarMensaje(g2, MSG_SIN_INGRESOS, COLOR_MENSAJE);
        } else if (tipoGrafico == TipoGrafico.INGRESOS_TARTA) {
            dibujarTarta(g2, soloConImporte(datosIngresosDelPeriodo()));
        } else {
            dibujarBarras(g2, gruposPorTipo(), leyendaTipos());
        }
    }

    // Datos de cada grafico

    /**
     * Por cada tramo: ingresos, gastos y balance (verde si es positivo, rojo si es negativo)
     */
    private List<Grupo> gruposTotales() {
        List<Grupo> grupos = new ArrayList<>();
        for (GrupoInforme g : informe.getGrupos()) {
            double balance = g.getBalance();
            grupos.add(new Grupo(g.getTramo().getEtiqueta(), g.getTramo().getDescripcion(),
                    List.of(new Dato("Ingresos", g.getTotalIngresos(), COLOR_INGRESOS),
                            new Dato("Gastos", g.getGastos(), COLOR_GASTOS),
                            new Dato("Balance", balance, balance < 0 ? COLOR_NEGATIVO : COLOR_BALANCE))));
        }
        return grupos;
    }

    /**
     * Por cada tramo: una barra por tipo de ingreso
     */
    private List<Grupo> gruposPorTipo() {
        List<Grupo> grupos = new ArrayList<>();
        for (GrupoInforme g : informe.getGrupos()) {
            List<Dato> datos = new ArrayList<>();
            for (TipoIngreso tipo : TipoIngreso.values()) {
                datos.add(new Dato(tipo.getConcepto(), g.getIngreso(tipo), colorDe(tipo)));
            }
            grupos.add(new Grupo(g.getTramo().getEtiqueta(), g.getTramo().getDescripcion(), datos));
        }
        return grupos;
    }

    private static List<Serie> leyendaTipos() {
        List<Serie> leyenda = new ArrayList<>();
        for (TipoIngreso tipo : TipoIngreso.values()) {
            leyenda.add(new Serie(tipo.getConcepto(), colorDe(tipo)));
        }
        return leyenda;
    }

    /**
     * Un dato por cada tipo de ingreso con el total del periodo, tambien los que no han tenido ventas
     */
    private List<Dato> datosIngresosDelPeriodo() {
        List<Dato> datos = new ArrayList<>();
        for (TipoIngreso tipo : TipoIngreso.values()) {
            datos.add(new Dato(tipo.getConcepto(), informe.getIngreso(tipo), colorDe(tipo)));
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

    private static Color colorDe(TipoIngreso tipo) {
        return COLORES_TIPO[tipo.ordinal() % COLORES_TIPO.length];
    }

    // Grafico de barras

    /**
     * Rejilla y ejes arriba, un grupo de barras por tramo, su etiqueta debajo y la leyenda al final
     */
    private void dibujarBarras(Graphics2D g2, List<Grupo> grupos, List<Serie> leyenda) {
        FontMetrics fm = g2.getFontMetrics();
        int anchoGrafico = getWidth() - MARGEN_IZQ - MARGEN_DER;
        int altoEtiquetas = MAX_LINEAS_ETIQUETA * fm.getHeight();
        int altoLeyenda = altoLeyenda(fm, leyenda, anchoGrafico);
        int altoGrafico = getHeight() - MARGEN_SUP - SEPARACION_ETIQUETAS - altoEtiquetas - SEPARACION_LEYENDA
                - altoLeyenda - MARGEN_INF;
        if (anchoGrafico < TAMANO_MINIMO_GRAFICO || altoGrafico < TAMANO_MINIMO_GRAFICO || grupos.isEmpty()) {
            return;
        }
        Rectangle area = new Rectangle(MARGEN_IZQ, MARGEN_SUP, anchoGrafico, altoGrafico);
        EjeVertical eje = new EjeVertical(grupos, area);

        dibujarRejilla(g2, area, eje);
        dibujarEjes(g2, area, eje);

        double anchoHueco = (double) area.width / grupos.size();
        for (int i = 0; i < grupos.size(); i++) {
            double xHueco = area.x + i * anchoHueco;
            dibujarGrupo(g2, grupos.get(i), xHueco, anchoHueco, eje);
            dibujarEtiquetaGrupo(g2, grupos.get(i).etiqueta(), xHueco, anchoHueco, area);
        }

        int yLeyenda = area.y + area.height + SEPARACION_ETIQUETAS + altoEtiquetas + SEPARACION_LEYENDA;
        dibujarLeyenda(g2, leyenda, area.x, yLeyenda, area.width);
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
     * Eje vertical y linea del cero (que no esta abajo del todo si hay valores negativos)
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
     * Barras del tramo una al lado de otra, centradas en su hueco. Un valor 0 deja su sitio vacio para que las barras
     * no se descoloquen entre grupos.
     */
    private void dibujarGrupo(Graphics2D g2, Grupo grupo, double xHueco, double anchoHueco, EjeVertical eje) {
        int numBarras = grupo.datos().size();
        double anchoBarra = Math.min(anchoHueco * PROPORCION_GRUPO / numBarras, ANCHO_MAX_BARRA);
        double inicio = xHueco + (anchoHueco - anchoBarra * numBarras) / 2;
        int yCero = eje.y(0);
        for (int i = 0; i < numBarras; i++) {
            Dato dato = grupo.datos().get(i);
            if (dato.valor() == 0) {
                continue;
            }
            int x = (int) Math.round(inicio + i * anchoBarra);
            int ancho = Math.max(ANCHO_MIN_BARRA, (int) Math.round(anchoBarra) - 1);
            int yValor = eje.y(dato.valor());
            Rectangle barra = new Rectangle(x, Math.min(yValor, yCero), ancho, Math.max(1, Math.abs(yValor - yCero)));

            g2.setColor(dato.color());
            g2.fill(barra);
            g2.setColor(dato.color().darker());
            g2.draw(barra);
            registrarZona(barra, grupo.descripcion() + " · " + dato.nombre() + ": "
                    + String.format(FORMATO_EUROS_DETALLE, dato.valor()));
        }
    }

    /**
     * Guarda la barra para el detalle al pasar el raton. Las barras muy bajitas se agrandan un poco para poder
     * apuntarlas.
     */
    private void registrarZona(Rectangle barra, String texto) {
        Rectangle area = new Rectangle(barra);
        if (area.height < ALTO_MIN_ZONA) {
            area.grow(0, (ALTO_MIN_ZONA - area.height) / 2 + 1);
        }
        zonas.add(new Zona(area, texto));
    }

    /**
     * Texto del tramo bajo el eje, centrado y partido en dos lineas si no cabe
     */
    private void dibujarEtiquetaGrupo(Graphics2D g2, String etiqueta, double xHueco, double anchoHueco,
            Rectangle area) {
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        int y = area.y + area.height + SEPARACION_ETIQUETAS + fm.getAscent();
        for (String linea : partirEnLineas(etiqueta, fm, (int) anchoHueco - 4)) {
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

        EjeVertical(List<Grupo> grupos, Rectangle area) {
            this.area = area;
            double max = 0;
            double min = 0;
            for (Grupo grupo : grupos) {
                for (Dato dato : grupo.datos()) {
                    max = Math.max(max, dato.valor());
                    min = Math.min(min, dato.valor());
                }
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

    // Leyenda de las barras

    /**
     * En una fila centrada si cabe en el ancho del grafico; si no, en columna
     */
    private void dibujarLeyenda(Graphics2D g2, List<Serie> leyenda, int x, int y, int ancho) {
        FontMetrics fm = g2.getFontMetrics();
        boolean enFila = anchoEnFila(fm, leyenda) <= ancho;
        int xEntrada = enFila ? x + (ancho - anchoEnFila(fm, leyenda)) / 2 : x;
        int yEntrada = y;
        for (Serie serie : leyenda) {
            dibujarMuestra(g2, serie.color(), xEntrada, yEntrada + (fm.getHeight() - LADO_MUESTRA) / 2);
            g2.setColor(Color.BLACK);
            g2.drawString(serie.nombre(), xEntrada + LADO_MUESTRA + 6, yEntrada + fm.getAscent());
            if (enFila) {
                xEntrada += anchoEntrada(fm, serie) + HUECO_LEYENDA;
            } else {
                yEntrada += fm.getHeight() + 2;
            }
        }
    }

    private static int altoLeyenda(FontMetrics fm, List<Serie> leyenda, int ancho) {
        if (anchoEnFila(fm, leyenda) <= ancho) {
            return fm.getHeight();
        }
        return leyenda.size() * (fm.getHeight() + 2);
    }

    private static int anchoEnFila(FontMetrics fm, List<Serie> leyenda) {
        int ancho = 0;
        for (Serie serie : leyenda) {
            ancho += anchoEntrada(fm, serie);
        }
        return ancho + HUECO_LEYENDA * (leyenda.size() - 1);
    }

    private static int anchoEntrada(FontMetrics fm, Serie serie) {
        return LADO_MUESTRA + 6 + fm.stringWidth(serie.nombre());
    }

    private static void dibujarMuestra(Graphics2D g2, Color color, int x, int y) {
        g2.setColor(color);
        g2.fillRect(x, y, LADO_MUESTRA, LADO_MUESTRA);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(x, y, LADO_MUESTRA, LADO_MUESTRA);
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
        dibujarLeyendaTarta(g2, datos, total, X_TARTA + diametro + SEPARACION_LEYENDA_TARTA, yTarta, diametro);
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
    private void dibujarLeyendaTarta(Graphics2D g2, List<Dato> datos, double total, int x, int yTarta, int diametro) {
        FontMetrics fm = g2.getFontMetrics();
        int altoEntrada = 2 * fm.getHeight() + 10;
        int xTexto = x + LADO_MUESTRA + 8;
        int y = yTarta + (diametro - datos.size() * altoEntrada) / 2;
        for (Dato dato : datos) {
            dibujarMuestra(g2, dato.color(), x, y + 2);
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
     * Divide un texto en como mucho {@value #MAX_LINEAS_ETIQUETA} lineas que quepan en el ancho dado. Lo que no quepa
     * se recorta con puntos suspensivos.
     */
    private static List<String> partirEnLineas(String texto, FontMetrics fm, int ancho) {
        List<String> lineas = new ArrayList<>();
        for (String linea : limitarLineas(ajustarAlAncho(texto, fm, ancho))) {
            lineas.add(recortar(linea, fm, ancho));
        }
        return lineas;
    }

    /**
     * Reparte las palabras en lineas, pasando a la siguiente cuando una palabra ya no cabe
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
     * Si sobran lineas, junta todas las que sobran en la ultima permitida
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
     * Si la linea no cabe, le quita letras del final hasta que quepa con los puntos suspensivos
     */
    private static String recortar(String linea, FontMetrics fm, int ancho) {
        if (linea.length() <= 1 || fm.stringWidth(linea) <= ancho) {
            return linea;
        }
        String recortada = linea;
        while (recortada.length() > 1 && fm.stringWidth(recortada.trim() + "…") > ancho) {
            recortada = recortada.substring(0, recortada.length() - 1);
        }
        return recortada.trim() + "…";
    }
}
