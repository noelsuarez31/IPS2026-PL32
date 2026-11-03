package giis.demo.ui.accion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import giis.demo.model.accion.Campana;
import giis.demo.model.accion.GestionCampanas;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.ApplicationException;

/**
 * Ventana del gerente para lanzar campañas de venta de acciones y gestionar sus fases. De momento solo contiene la
 * interfaz: la funcionalidad se añade despues.
 */
public class VentanaCampanasAcciones extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARTA_SIN_CAMPANA = "sinCampana";
    private static final String CARTA_ACTIVA = "activa";
    private static final int NUM_FASES = 3;
    private static final Locale ES = Locale.forLanguageTag("es-ES");
    private static final Color COLOR_ACTUAL = new Color(198, 40, 40);

    private VentanaPrincipal vp;
    private GestionCampanas gestion;
    private Campana campanaMostrada; // campaña activa que se esta viendo (null si no hay)

    // Zona de lanzamiento
    private JPanel pnLanzar;
    private TitledBorder bordeLanzar;
    private JTextField txNumAcciones;
    private JTextField txPrecio;
    private JButton btnLanzar;

    // Zona de campana activa
    private JPanel pnCartas;
    private CardLayout clCartas;
    private IndicadorFase indicadorFase;
    private JLabel lbFase;
    private JLabel lbDescripcionFase;
    private JTextField txPrecioCampana;
    private JTextField txAccionesVenta;
    private JTextField txVendidas;
    private JTextField txDisponibles;
    private JTextField txRecaudado;
    private JButton btnAccionFase;

    public VentanaCampanasAcciones(VentanaPrincipal vp) {
        this.vp = vp;
        this.gestion = new GestionCampanas();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Campañas de acciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 460, 560);
        setMinimumSize(new Dimension(440, 540));

        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
        setContentPane(contentPane);

        contentPane.add(crearPanelLanzar(), BorderLayout.NORTH);
        contentPane.add(crearPanelCampana(), BorderLayout.CENTER);

        setLocationRelativeTo(null);

        // Al cerrar la ventana se vuelve a mostrar la ventana principal
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (vp != null)
                    vp.setVisible(true);
            }
        });

        configurarEventos();
        cargarEstado();
    }
    
    // Eventos y acceso a la logica

    private void configurarEventos() {
        btnLanzar.addActionListener(e -> lanzarCampana());
        btnAccionFase.addActionListener(e -> accionSobreFase());
    }
    
    /** El boton cambia segun la fase: avanza de fase o, en la ultima, cierra la campaña. */
    private void accionSobreFase() {
        if (campanaMostrada == null)
            return;
        if (campanaMostrada.getFase() < Campana.ULTIMA_FASE)
            avanzarFase();
        else
            cerrarCampana();
    }

    private void avanzarFase() {
        int siguiente = campanaMostrada.getFase() + 1;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Avanzar a la fase " + siguiente + "?\nNo se podrá volver a la fase anterior.", "Avanzar de fase",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION)
            return;
        try {
            gestion.avanzarFase(campanaMostrada);
            cargarEstado();
        } catch (ApplicationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            cargarEstado();
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
        }
    }

    private void cerrarCampana() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Cerrar la campaña?\nQuedan " + campanaMostrada.getAccionesDisponibles()
                        + " acciones sin vender\ny no se crearán.",
                "Cerrar campaña", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION)
            return;
        try {
            gestion.cerrarCampana(campanaMostrada);
            cargarEstado();
        } catch (ApplicationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            cargarEstado();
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
        }
    }

    /** Lee la campaña activa de la base de datos y muestra su estado. */
    private void cargarEstado() {
        try {
            Campana campana = gestion.obtenerCampanaActiva();
            campanaMostrada = campana;
            if (campana == null)
                mostrarSinCampana();
            else
                mostrarCampanaActiva(campana.getFase(), campana.getPrecio(), campana.getNumAcciones(),
                        campana.getAccionesVendidas());
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
        }
    }

    private void lanzarCampana() {
        try {
            gestion.lanzarCampana(txNumAcciones.getText(), txPrecio.getText());
            txNumAcciones.setText("");
            txPrecio.setText("");
            cargarEstado();
        } catch (ApplicationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            cargarEstado();
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
        }
    }

    private void mostrarErrorBaseDatos(SQLException e) {
        JOptionPane.showMessageDialog(this, "No se pudo acceder a la base de datos.\n" + e.getMessage(),
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    private JPanel crearPanelLanzar() {
        pnLanzar = new JPanel(new GridBagLayout());
        bordeLanzar = BorderFactory.createTitledBorder("Lanzar nueva campaña");
        pnLanzar.setBorder(BorderFactory.createCompoundBorder(bordeLanzar, new EmptyBorder(5, 10, 8, 10)));

        txNumAcciones = new JTextField(12);
        txPrecio = new JTextField(12);
        anadirFila(pnLanzar, 0, "Nº de acciones:", txNumAcciones);
        anadirFila(pnLanzar, 1, "Precio por acción (€):", txPrecio);

        btnLanzar = new JButton("Lanzar campaña");
        btnLanzar.setFont(btnLanzar.getFont().deriveFont(Font.BOLD));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(8, 0, 0, 0);
        pnLanzar.add(btnLanzar, gbc);
        return pnLanzar;
    }

    private JPanel crearPanelCampana() {
        JPanel pnCampana = new JPanel(new BorderLayout());
        pnCampana.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Campaña activa"),
                new EmptyBorder(5, 10, 10, 10)));

        clCartas = new CardLayout();
        pnCartas = new JPanel(clCartas);
        pnCartas.add(crearCartaSinCampana(), CARTA_SIN_CAMPANA);
        pnCartas.add(crearCartaActiva(), CARTA_ACTIVA);
        pnCampana.add(pnCartas, BorderLayout.CENTER);
        return pnCampana;
    }

    private JPanel crearCartaSinCampana() {
        JPanel carta = new JPanel(new BorderLayout());
        JLabel lbSinCampana = new JLabel("No hay ninguna campaña activa.", SwingConstants.CENTER);
        lbSinCampana.setFont(lbSinCampana.getFont().deriveFont(Font.PLAIN, 14f));
        lbSinCampana.setForeground(Color.GRAY);
        carta.add(lbSinCampana, BorderLayout.CENTER);
        return carta;
    }

    private JPanel crearCartaActiva() {
        JPanel carta = new JPanel(new BorderLayout(0, 10));

        // Norte: indicador de fase y descripcion
        JPanel pnFase = new JPanel();
        pnFase.setLayout(new BoxLayout(pnFase, BoxLayout.Y_AXIS));
        indicadorFase = new IndicadorFase();
        indicadorFase.setAlignmentX(CENTER_ALIGNMENT);
        lbFase = new JLabel("Fase actual: -", SwingConstants.CENTER);
        lbFase.setFont(lbFase.getFont().deriveFont(Font.BOLD, 15f));
        lbFase.setForeground(COLOR_ACTUAL);
        lbFase.setAlignmentX(CENTER_ALIGNMENT);
        lbDescripcionFase = new JLabel(" ", SwingConstants.CENTER);
        lbDescripcionFase.setAlignmentX(CENTER_ALIGNMENT);
        pnFase.add(indicadorFase);
        pnFase.add(lbFase);
        pnFase.add(lbDescripcionFase);
        carta.add(pnFase, BorderLayout.NORTH);

        // Centro: datos
        JPanel pnDatos = new JPanel(new GridBagLayout());
        txPrecioCampana = crearCampoLectura();
        txAccionesVenta = crearCampoLectura();
        txVendidas = crearCampoLectura();
        txDisponibles = crearCampoLectura();
        txRecaudado = crearCampoLectura();
        anadirFila(pnDatos, 0, "Precio por acción:", txPrecioCampana);
        anadirFila(pnDatos, 1, "Acciones a la venta:", txAccionesVenta);
        anadirFila(pnDatos, 2, "Acciones vendidas:", txVendidas);
        anadirFila(pnDatos, 3, "Acciones disponibles:", txDisponibles);
        anadirFila(pnDatos, 4, "Importe recaudado:", txRecaudado);
        JPanel pnCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnCentro.add(pnDatos);
        carta.add(pnCentro, BorderLayout.CENTER);

        // Sur: boton de accion sobre la fase
        btnAccionFase = new JButton("Avanzar a la fase 2");
        btnAccionFase.setFont(btnAccionFase.getFont().deriveFont(Font.BOLD));
        JPanel pnBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnBoton.add(btnAccionFase);
        carta.add(pnBoton, BorderLayout.SOUTH);
        return carta;
    }

    private JTextField crearCampoLectura() {
        JTextField tx = new JTextField(12);
        tx.setEditable(false);
        tx.setHorizontalAlignment(SwingConstants.RIGHT);
        return tx;
    }

    private void anadirFila(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = fila;
        gbc.insets = new Insets(4, 0, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(4, 0, 4, 0);
        panel.add(campo, gbc);
    }

    // Cambios de estado de la vista

    /** No hay campaña activa: se puede lanzar una nueva. */
    public void mostrarSinCampana() {
        habilitarLanzamiento(true);
        clCartas.show(pnCartas, CARTA_SIN_CAMPANA);
    }

    /** Hay una campaña activa: se muestran sus datos y se bloquea el lanzamiento. */
    public void mostrarCampanaActiva(int fase, double precio, int totalAcciones, int vendidas) {
        habilitarLanzamiento(false);

        indicadorFase.setFase(fase);
        lbFase.setText("Fase actual: " + fase + " de " + NUM_FASES);
        lbDescripcionFase.setText(descripcionFase(fase));

        txPrecioCampana.setText(formatearEuros(precio));
        txAccionesVenta.setText(String.valueOf(totalAcciones));
        txVendidas.setText(String.valueOf(vendidas));
        txDisponibles.setText(String.valueOf(totalAcciones - vendidas));
        txRecaudado.setText(formatearEuros(precio * vendidas));

        btnAccionFase.setText(fase < NUM_FASES ? "Avanzar a la fase " + (fase + 1) : "Cerrar campaña");
        clCartas.show(pnCartas, CARTA_ACTIVA);
    }

    private void habilitarLanzamiento(boolean habilitado) {
        txNumAcciones.setEnabled(habilitado);
        txPrecio.setEnabled(habilitado);
        btnLanzar.setEnabled(habilitado);
        bordeLanzar.setTitle(habilitado ? "Lanzar nueva campaña" : "Lanzar nueva campaña (deshabilitado)");
        pnLanzar.repaint();
    }

    private String descripcionFase(int fase) {
        switch (fase) {
        case 1:
            return "<html><div style='text-align:center'>Solo accionistas actuales, cada uno hasta<br>"
                    + "las acciones que poseía al lanzarse la campaña</div></html>";
        case 2:
            return "<html><div style='text-align:center'>Solo accionistas actuales,<br>sin límite de compra</div></html>";
        default:
            return "<html><div style='text-align:center'>Cualquier persona,<br>incluidos nuevos accionistas</div></html>";
        }
    }

    private String formatearEuros(double importe) {
        return String.format(ES, "%,.2f €", importe);
    }

    // Indicador para las tres fases

    private static class IndicadorFase extends JPanel {
        private static final long serialVersionUID = 1L;
        private int fase = 1;

        IndicadorFase() {
            setPreferredSize(new Dimension(320, 50));
            setMaximumSize(new Dimension(320, 50));
        }

        void setFase(int fase) {
            this.fase = fase;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int d = 30;
            int cy = getHeight() / 2;
            int[] cx = { getWidth() / 6, getWidth() / 2, getWidth() * 5 / 6 };
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(cx[0] + d / 2, cy, cx[1] - d / 2, cy);
            g2.drawLine(cx[1] + d / 2, cy, cx[2] - d / 2, cy);
            FontMetrics fm = g2.getFontMetrics(g2.getFont().deriveFont(Font.BOLD, 14f));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
            for (int i = 0; i < NUM_FASES; i++) {
                boolean actual = (i + 1) == fase;
                g2.setColor(actual ? new Color(251, 227, 227) : getBackground());
                g2.fillOval(cx[i] - d / 2, cy - d / 2, d, d);
                g2.setColor(actual ? COLOR_ACTUAL : Color.DARK_GRAY);
                g2.drawOval(cx[i] - d / 2, cy - d / 2, d, d);
                String n = String.valueOf(i + 1);
                g2.drawString(n, cx[i] - fm.stringWidth(n) / 2, cy + (fm.getAscent() - fm.getDescent()) / 2);
            }
            g2.dispose();
        }
    }
}