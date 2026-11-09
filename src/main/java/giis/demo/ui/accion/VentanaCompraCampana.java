package giis.demo.ui.accion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import giis.demo.exceptions.EmailException;
import giis.demo.model.accion.Campana;
import giis.demo.model.accion.GestionCompraCampana;
import giis.demo.model.accion.InfoCompra;
import giis.demo.model.accion.ResultadoCompra;
import giis.demo.model.email.ResumenCompra;
import giis.demo.model.email.ServicioEmail;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.ApplicationException;

/**
 * Ventana para comprar acciones de la campaña activa. El comprador se identifica con su DNI; si no es
 * accionista y la campaña está en la última fase, se registra con sus datos.
 */
public class VentanaCompraCampana extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARTA_MENSAJE = "mensaje";
    private static final String CARTA_INFO = "info";
    private static final Color COLOR_DESTACADO = new Color(198, 40, 40);

    private VentanaPrincipal vp;
    private GestionCompraCampana gestion;
    private InfoCompra infoActual; // comprador identificado (null si todavia no hay)
    private boolean ajustandoDni; // true mientras el DNI se escribe por codigo, no el usuario

    // Comprador
    private JTextField txDni;
    private JButton btnIdentificar;
    private JLabel lbAvisoNuevo;
    private JLabel lbNombre;
    private JTextField txNombre;
    private JLabel lbApellidos;
    private JTextField txApellidos;
    private JLabel lbEmail;
    private JTextField txEmail;

    // Campaña
    private JPanel pnCartas;
    private CardLayout clCartas;
    private JLabel lbMensaje;
    private JTextField txCampana;
    private JTextField txFase;
    private JTextField txPrecio;
    private JTextField txDisponibles;
    private JTextField txMaximo;
    private JLabel lbDescripcionFase;

    // Compra
    private JTextField txCantidad;
    private JButton btnComprar;

    public VentanaCompraCampana(VentanaPrincipal vp) {
        this.vp = vp;
        this.gestion = new GestionCompraCampana();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Comprar acciones de la campaña");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
        setContentPane(contentPane);

        contentPane.add(crearPanelComprador(), BorderLayout.NORTH);
        contentPane.add(crearPanelCampana(), BorderLayout.CENTER);
        contentPane.add(crearPanelCompra(), BorderLayout.SOUTH);

        // El tamaño se calcula con el caso mas alto (comprador nuevo, con todos sus campos) para que nunca se corte
        // nada, sea cual sea el tamaño de letra de la pantalla
        mostrarDatosComprador(true, true);
        pack();
        setMinimumSize(getSize());

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
        cargarEstadoInicial();
    }

    // Eventos y acceso a la logica

    private void configurarEventos() {
        btnIdentificar.addActionListener(e -> cargarComprador(txDni.getText()));
        txDni.addActionListener(e -> cargarComprador(txDni.getText()));
        btnComprar.addActionListener(e -> comprar());

        // Si se cambia el DNI despues de identificar, hay que volver a identificar
        txDni.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                dniModificado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                dniModificado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                dniModificado();
            }
        });
    }

    private void dniModificado() {
        if (!ajustandoDni && infoActual != null)
            reiniciarComprador();
    }

    /** Al abrir: si no hay campaña activa se avisa; si la hay, se espera el DNI. */
    private void cargarEstadoInicial() {
        try {
            if (gestion.obtenerCampanaActiva() == null)
                mostrarSinCampana();
            else
                reiniciarComprador();
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
            mostrarSinCampana();
        }
    }

    /** Identifica al comprador por su DNI y muestra la campaña y su maximo. */
    private void cargarComprador(String dni) {
        try {
            mostrarInfo(gestion.identificar(dni));
        } catch (ApplicationException e) {
            reiniciarComprador();
            if (GestionCompraCampana.MSG_SIN_CAMPANA.equals(e.getMessage()))
                mostrarSinCampana();
            else
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
        }
    }

    private void comprar() {
        if (infoActual == null)
            return;
        InfoCompra info = infoActual;

        // Comprobar lo escrito antes de pedir confirmacion
        int unidades;
        try {
            unidades = GestionCompraCampana.validarCantidad(info, txCantidad.getText());
            gestion.validarDatosComprador(info, txNombre.getText(), txApellidos.getText(), txEmail.getText());
        } catch (ApplicationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Resumen y confirmacion
        double precio = info.getCampana().getPrecio();
        double total = Math.round(unidades * precio * 100.0) / 100.0;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "Resumen de la compra:\n\nAcciones: " + unidades + "\nPrecio unitario: " + ResumenCompra.precio(precio)
                        + "\nImporte total: " + ResumenCompra.precio(total) + "\n\n¿Confirmar la compra?",
                "Confirmar compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION)
            return;

        // Registrar la compra
        String emailDestino = GestionCompraCampana.emailDestino(info, txEmail.getText());
        try {
            ResultadoCompra resultado = gestion.comprar(info, unidades, txNombre.getText(), txApellidos.getText(),
                    txEmail.getText());
            compraRealizada(info, resultado, emailDestino);
        } catch (ApplicationException e) {
            // La campaña ha cambiado o el DNI ya estaba registrado: se avisa y se actualiza la informacion
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            cargarComprador(info.getDni());
        } catch (SQLException e) {
            mostrarErrorBaseDatos(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Se ha producido un error inesperado.\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** La compra ya esta guardada: se envia el email, se informa y se limpia la ventana. */
    private void compraRealizada(InfoCompra info, ResultadoCompra resultado, String emailDestino) {
        boolean enviado = enviarEmail(emailDestino, info.getCampana(), resultado);

        String creadas = resultado.getUnidades() == 1 ? "Se ha creado 1 acción" : "Se han creado " + resultado.getUnidades() + " acciones";
        if (enviado) {
            JOptionPane.showMessageDialog(this,
                    "Compra realizada.\n" + creadas + ": nº " + resultado.getIdsTexto() + ".\n\nResumen enviado a "
                            + emailDestino,
                    "Compra realizada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "La compra se ha guardado correctamente.\n" + creadas + ": nº " + resultado.getIdsTexto()
                            + ".\n\nNo se ha podido enviar el email a " + emailDestino + ".\nInforme al comprador.",
                    "Email no enviado", JOptionPane.WARNING_MESSAGE);
        }
        if (resultado.isCampanaCerrada())
            JOptionPane.showMessageDialog(this,
                    "Con esta compra se han vendido todas las acciones:\nla campaña se ha cerrado automáticamente.",
                    "Campaña cerrada", JOptionPane.INFORMATION_MESSAGE);

        ajustandoDni = true;
        txDni.setText("");
        ajustandoDni = false;
        if (resultado.isCampanaCerrada())
            mostrarSinCampana();
        else
            reiniciarComprador();
    }

    private boolean enviarEmail(String destinatario, Campana campana, ResultadoCompra resultado) {
        try {
            ResumenCompra resumen = new ResumenCompra("Acciones", "Compra de acciones - " + campana.getNombre(),
                    LocalDateTime.now(), resultado.getTotal());
            resumen.addLinea("Acciones nº " + resultado.getIdsTexto() + " (" + campana.getNombre() + ")",
                    resultado.getUnidades(), resultado.getPrecioUnitario());
            new ServicioEmail().enviar(destinatario, resumen.getAsunto(), resumen.getCuerpo(), resumen.getCuerpoHtml());
            return true;
        } catch (EmailException | RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarErrorBaseDatos(SQLException e) {
        JOptionPane.showMessageDialog(this, "No se pudo acceder a la base de datos.\n" + e.getMessage(),
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    // Construccion de la interfaz

    private JPanel crearPanelComprador() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Comprador"),
                new EmptyBorder(5, 10, 8, 10)));

        txDni = new JTextField(12);
        btnIdentificar = new JButton("Identificar");
        JPanel pnDni = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnDni.add(txDni);
        pnDni.add(new JLabel("  "));
        pnDni.add(btnIdentificar);
        anadirFila(panel, 0, new JLabel("DNI:"), pnDni);

        lbAvisoNuevo = new JLabel("No es accionista: complete sus datos");
        lbAvisoNuevo.setForeground(COLOR_DESTACADO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 0, 2, 0);
        panel.add(lbAvisoNuevo, gbc);

        lbNombre = new JLabel("Nombre:");
        txNombre = new JTextField(20);
        lbApellidos = new JLabel("Apellidos:");
        txApellidos = new JTextField(20);
        lbEmail = new JLabel("Email:");
        txEmail = new JTextField(20);
        anadirFila(panel, 2, lbNombre, txNombre);
        anadirFila(panel, 3, lbApellidos, txApellidos);
        anadirFila(panel, 4, lbEmail, txEmail);
        return panel;
    }

    private JPanel crearPanelCampana() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Campaña activa"),
                new EmptyBorder(5, 10, 10, 10)));

        clCartas = new CardLayout();
        pnCartas = new JPanel(clCartas);
        pnCartas.add(crearCartaMensaje(), CARTA_MENSAJE);
        pnCartas.add(crearCartaInfo(), CARTA_INFO);
        panel.add(pnCartas, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearCartaMensaje() {
        JPanel carta = new JPanel(new BorderLayout());
        lbMensaje = new JLabel(" ", SwingConstants.CENTER);
        lbMensaje.setFont(lbMensaje.getFont().deriveFont(Font.PLAIN, 14f));
        carta.add(lbMensaje, BorderLayout.CENTER);
        return carta;
    }

    private JPanel crearCartaInfo() {
        JPanel carta = new JPanel(new BorderLayout(0, 10));

        JPanel pnDatos = new JPanel(new GridBagLayout());
        txCampana = crearCampoLectura();
        txFase = crearCampoLectura();
        txPrecio = crearCampoLectura();
        txDisponibles = crearCampoLectura();
        txMaximo = crearCampoLectura();
        txMaximo.setFont(txMaximo.getFont().deriveFont(Font.BOLD));
        txMaximo.setForeground(COLOR_DESTACADO);
        JLabel lbMaximo = new JLabel("Máximo que puede comprar:");
        lbMaximo.setFont(lbMaximo.getFont().deriveFont(Font.BOLD));
        lbMaximo.setForeground(COLOR_DESTACADO);
        anadirFila(pnDatos, 0, new JLabel("Campaña:"), txCampana);
        anadirFila(pnDatos, 1, new JLabel("Fase actual:"), txFase);
        anadirFila(pnDatos, 2, new JLabel("Precio por acción:"), txPrecio);
        anadirFila(pnDatos, 3, new JLabel("Acciones disponibles:"), txDisponibles);
        anadirFila(pnDatos, 4, lbMaximo, txMaximo);
        JPanel pnCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnCentro.add(pnDatos);
        carta.add(pnCentro, BorderLayout.CENTER);

        // texto de dos lineas desde el principio para que el tamaño de la ventana lo tenga en cuenta
        lbDescripcionFase = new JLabel(descripcionFase(Campana.PRIMERA_FASE), SwingConstants.CENTER);
        lbDescripcionFase.setForeground(COLOR_DESTACADO);
        carta.add(lbDescripcionFase, BorderLayout.SOUTH);
        return carta;
    }

    private JPanel crearPanelCompra() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Compra"),
                new EmptyBorder(5, 10, 8, 10)));

        txCantidad = new JTextField(12);
        anadirFila(panel, 0, new JLabel("Nº de acciones:"), txCantidad);

        btnComprar = new JButton("Comprar");
        btnComprar.setFont(btnComprar.getFont().deriveFont(Font.BOLD));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(8, 0, 0, 0);
        panel.add(btnComprar, gbc);
        return panel;
    }

    private JTextField crearCampoLectura() {
        JTextField tx = new JTextField(12);
        tx.setEditable(false);
        tx.setHorizontalAlignment(SwingConstants.RIGHT);
        return tx;
    }

    private void anadirFila(JPanel panel, int fila, JComponent etiqueta, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = fila;
        gbc.insets = new Insets(4, 0, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        panel.add(etiqueta, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(4, 0, 4, 0);
        panel.add(campo, gbc);
    }

    // Cambios de estado de la vista

    /** Espera el DNI: no se muestran datos del comprador ni de la campaña y no se puede comprar. */
    private void reiniciarComprador() {
        infoActual = null;
        txDni.setEnabled(true);
        btnIdentificar.setEnabled(true);
        mostrarDatosComprador(false, false);
        mostrarMensaje("Introduzca el DNI del comprador\ny pulse Identificar.", Color.GRAY);
        txCantidad.setText("");
        txCantidad.setEnabled(false);
        btnComprar.setEnabled(false);
    }

    /** No hay campaña activa: no es posible comprar. */
    private void mostrarSinCampana() {
        reiniciarComprador();
        txDni.setEnabled(false);
        btnIdentificar.setEnabled(false);
        mostrarMensaje("No hay ninguna campaña activa.\nNo es posible comprar acciones.", COLOR_DESTACADO);
    }

    /** Comprador identificado: se muestran sus datos, la campaña y su maximo. */
    private void mostrarInfo(InfoCompra info) {
        infoActual = info;
        ajustandoDni = true;
        txDni.setText(info.getDni());
        ajustandoDni = false;

        boolean nuevo = info.esNuevo();
        mostrarDatosComprador(true, nuevo);
        if (nuevo) {
            txNombre.setText("");
            txApellidos.setText("");
            txEmail.setText("");
        } else {
            txNombre.setText(info.getAccionista().getNombreCompleto());
            String guardado = info.getEmailGuardado();
            txEmail.setText(guardado != null ? guardado : "");
            txEmail.setEditable(guardado == null);
        }

        Campana c = info.getCampana();
        txCampana.setText(c.getNombre());
        txFase.setText(c.getFase() + " de " + Campana.ULTIMA_FASE);
        txPrecio.setText(ResumenCompra.precio(c.getPrecio()));
        txDisponibles.setText(String.valueOf(c.getAccionesDisponibles()));
        txMaximo.setText(String.valueOf(info.getMaximo()));
        lbDescripcionFase.setText(descripcionFase(c.getFase()));
        clCartas.show(pnCartas, CARTA_INFO);

        txCantidad.setText("");
        txCantidad.setEnabled(true);
        btnComprar.setEnabled(true);
        txCantidad.requestFocus();
    }

    /**
     * Muestra u oculta los datos del comprador. Si es nuevo se piden nombre, apellidos y email; si ya es accionista
     * se muestra su nombre y su email (solo se pide si no tiene uno guardado).
     */
    private void mostrarDatosComprador(boolean visibles, boolean nuevo) {
        lbAvisoNuevo.setVisible(visibles && nuevo);
        lbNombre.setVisible(visibles);
        txNombre.setVisible(visibles);
        lbNombre.setText(nuevo ? "Nombre:" : "Accionista:");
        txNombre.setEditable(nuevo);
        lbApellidos.setVisible(visibles && nuevo);
        txApellidos.setVisible(visibles && nuevo);
        lbEmail.setVisible(visibles);
        txEmail.setVisible(visibles);
        txEmail.setEditable(true);
        if (!visibles) {
            txNombre.setText("");
            txApellidos.setText("");
            txEmail.setText("");
        }
        revalidate();
        repaint();
    }

    private void mostrarMensaje(String texto, Color color) {
        lbMensaje.setText("<html><div style='text-align:center'>" + texto.replace("\n", "<br>") + "</div></html>");
        lbMensaje.setForeground(color);
        clCartas.show(pnCartas, CARTA_MENSAJE);
    }

    private String descripcionFase(int fase) {
        switch (fase) {
        case 1:
            return "<html><div style='text-align:center'>Fase 1: hasta las acciones que tenía<br>al lanzarse la campaña</div></html>";
        case 2:
            return "<html><div style='text-align:center'>Fase 2: solo accionistas,<br>hasta las acciones disponibles</div></html>";
        default:
            return "<html><div style='text-align:center'>Fase 3: cualquier persona,<br>hasta las acciones disponibles</div></html>";
        }
    }
}