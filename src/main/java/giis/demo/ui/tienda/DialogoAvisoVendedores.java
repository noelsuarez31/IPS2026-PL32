package giis.demo.ui.tienda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.tienda.AvisoVendedores;
import giis.demo.model.tienda.FranjaSinCubrir;
import giis.demo.model.tienda.HorarioTienda;

/**
 * Aviso al gerente cuando faltan vendedores en la tienda, ahora mismo o en lo que queda de semana. Desde
 * aqui puede ir a asignar horarios; al volver se comprueba otra vez.
 */
public class DialogoAvisoVendedores extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Color COLOR_FALTAN = new Color(163, 0, 0);
    private static final Color COLOR_CUBIERTO = new Color(0, 110, 0);
    private static final Color COLOR_PROXIMA = new Color(255, 242, 194);
    private static final DateTimeFormatter FORMATO_MOMENTO = DateTimeFormatter
            .ofPattern("EEEE dd/MM/yyyy, HH:mm", Locale.forLanguageTag("es-ES"));
    private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM");

    /**
     * @param asignarPeriodico se ejecuta al pulsar "Asignar horario periódico", despues de cerrar el aviso
     * @param asignarEspecifico se ejecuta al pulsar "Asignar horario específico", despues de cerrar el aviso
     */
    public DialogoAvisoVendedores(Frame propietario, AvisoVendedores aviso, Runnable asignarPeriodico,
            Runnable asignarEspecifico) {
        super(propietario, "Aviso: faltan vendedores en la tienda", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));
        contenido.add(alinear(crearCabecera()));
        contenido.add(espacio());
        contenido.add(alinear(crearPanelAhora(aviso)));
        contenido.add(espacio());
        if (!aviso.getFranjasSinCubrir().isEmpty()) {
            contenido.add(alinear(crearPanelSemana(aviso)));
            contenido.add(espacio());
        }
        contenido.add(alinear(new JLabel("El aviso dejará de aparecer cuando todas las franjas tengan "
                + HorarioTienda.MIN_VENDEDORES + " vendedores.")));
        contenido.add(espacio());
        contenido.add(alinear(crearBotones(asignarPeriodico, asignarEspecifico)));
        setContentPane(contenido);

        pack();
        setMinimumSize(new Dimension(620, getHeight()));
        setLocationRelativeTo(propietario);
    }

    private static JPanel crearCabecera() {
        JPanel pn = new JPanel(new BorderLayout(12, 0));
        pn.add(new JLabel(UIManager.getIcon("OptionPane.warningIcon")), BorderLayout.WEST);
        JLabel titulo = new JLabel("La tienda no tiene los vendedores mínimos en parte de su horario");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, titulo.getFont().getSize2D() + 1f));
        JLabel detalle = new JLabel("<html><body style='width: 500px'>Debe haber al menos " + HorarioTienda.MIN_VENDEDORES
                + " vendedores de tienda trabajando mientras la tienda esté abierta (" + HorarioTienda.DESCRIPCION
                + ").</html>");
        JPanel textos = new JPanel(new BorderLayout(0, 4));
        textos.add(titulo, BorderLayout.NORTH);
        textos.add(detalle, BorderLayout.CENTER);
        pn.add(textos, BorderLayout.CENTER);
        return pn;
    }

    /**
     * Lo mas urgente: cuantos vendedores hay ahora mismo
     */
    private static JPanel crearPanelAhora(AvisoVendedores aviso) {
        JPanel pn = new JPanel();
        pn.setLayout(new BoxLayout(pn, BoxLayout.Y_AXIS));
        pn.setBackground(Color.WHITE);
        pn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JLabel titulo = new JLabel("Ahora mismo");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD));
        String momento = capitalizar(aviso.getMomento().format(FORMATO_MOMENTO));
        JLabel cuando = new JLabel(momento + " · " + (aviso.isEnHorario() ? "dentro" : "fuera")
                + " del horario de apertura");
        JLabel estado = new JLabel(textoAhora(aviso));
        estado.setFont(estado.getFont().deriveFont(Font.BOLD));
        estado.setForeground(aviso.faltanAhora() > 0 ? COLOR_FALTAN : COLOR_CUBIERTO);
        if (!aviso.isEnHorario()) {
            estado.setForeground(Color.DARK_GRAY);
        }

        pn.add(titulo);
        pn.add(cuando);
        pn.add(estado);
        return pn;
    }

    private static String textoAhora(AvisoVendedores aviso) {
        if (!aviso.isEnHorario()) {
            return "Fuera del horario de apertura: no se exige un mínimo de vendedores.";
        }
        int hay = aviso.getVendedoresAhora();
        String texto = "Hay " + hay + (hay == 1 ? " vendedor trabajando" : " vendedores trabajando");
        int faltan = aviso.faltanAhora();
        return faltan == 0 ? texto + "." : texto + ": " + (faltan == 1 ? "falta 1." : "faltan " + faltan + ".");
    }

    /**
     * Franjas sin cubrir desde hoy hasta el sabado; la mas proxima va resaltada
     */
    private static JPanel crearPanelSemana(AvisoVendedores aviso) {
        JPanel pn = new JPanel(new BorderLayout(0, 6));
        JLabel titulo = new JLabel("Semana actual (lunes " + aviso.getLunes().format(FORMATO_DIA) + " a sábado "
                + aviso.getSabado().format(FORMATO_DIA) + "): franjas sin cubrir a partir de hoy");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD));
        pn.add(titulo, BorderLayout.NORTH);

        DefaultTableModel modelo = new DefaultTableModel(
                new String[] { "Día", "Franja de apertura", "Empleados", "Faltan" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        for (FranjaSinCubrir f : aviso.getFranjasSinCubrir()) {
            String dia = f.getFranja().getNombreDia() + (f.isEnCurso() ? " (en curso)" : "");
            modelo.addRow(new Object[] { dia, f.getFranja().getHoras(), f.getVendedores(), f.getFaltan() });
        }
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.setFocusable(false);
        tabla.setRowSelectionAllowed(false);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setDefaultRenderer(Object.class, new RenderizadorFranjas());
        tabla.getColumnModel().getColumn(0).setPreferredWidth(170);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.setPreferredScrollableViewportSize(
                new Dimension(560, tabla.getRowHeight() * Math.min(modelo.getRowCount(), 8)));
        pn.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return pn;
    }

    private JPanel crearBotones(Runnable asignarPeriodico, Runnable asignarEspecifico) {
        JPanel pn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pn.add(crearBoton("Asignar horario periódico", asignarPeriodico));
        pn.add(Box.createHorizontalStrut(8));
        pn.add(crearBoton("Asignar horario específico", asignarEspecifico));
        return pn;
    }

    private JButton crearBoton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> {
            dispose();
            accion.run();
        });
        return boton;
    }

    /**
     * Resalta la primera fila (la franja mas proxima) y pone en rojo lo que falta
     */
    private static class RenderizadorFranjas extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            setBackground(fila == 0 ? COLOR_PROXIMA : Color.WHITE);
            setForeground(columna == 3 ? COLOR_FALTAN : Color.BLACK);
            setFont(getFont().deriveFont(fila == 0 || columna == 3 ? Font.BOLD : Font.PLAIN));
            setHorizontalAlignment(columna >= 2 ? CENTER : LEFT);
            return this;
        }
    }

    private static Component alinear(JPanel panel) {
        panel.setAlignmentX(LEFT_ALIGNMENT);
        return panel;
    }

    private static Component alinear(JLabel etiqueta) {
        etiqueta.setAlignmentX(LEFT_ALIGNMENT);
        return etiqueta;
    }

    private static Component espacio() {
        return Box.createVerticalStrut(12);
    }

    private static String capitalizar(String texto) {
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}