package giis.demo.ui.informe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import giis.demo.exceptions.InformeFinancieroException;
import giis.demo.model.historial.TipoIngreso;
import giis.demo.model.informe.GestionInformeFinanciero;
import giis.demo.model.informe.InformeFinanciero;
import giis.demo.model.informe.TipoPeriodo;
import giis.demo.ui.informe.PanelGrafico.TipoGrafico;
import giis.demo.ui.login.VentanaPrincipal;

public class VentanaInformeFinanciero extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String FORMATO_EUROS = "%.2f €";
    private static final Color COLOR_POSITIVO = new Color(0, 110, 0);
    private static final Color COLOR_NEGATIVO = new Color(198, 40, 40);

    private GestionInformeFinanciero gestion;

    private JComboBox<TipoPeriodo> cbTipoPeriodo;
    private JDateChooser dcFecha;
    private JLabel lbPeriodo;
    private JLabel lbIngresos;
    private JLabel lbGastos;
    private JLabel lbBalance;
    private DefaultTableModel modeloIngresos;
    private DefaultTableModel modeloGastos;
    private JLabel lbTotalIngresos;
    private JLabel lbTotalGastos;
    private JLabel lbNotaGastos;
    private JComboBox<TipoGrafico> cbGrafico;
    private PanelGrafico panelGrafico;

    public VentanaInformeFinanciero(VentanaPrincipal ventanaPrincipal, GestionInformeFinanciero gestion) {
        this.gestion = gestion;

        setTitle("Informe financiero");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 720);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                cerrar(ventanaPrincipal);
            }
        });

        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel pnNorte = new JPanel(new BorderLayout(5, 5));
        pnNorte.add(crearPanelPeriodo(), BorderLayout.NORTH);
        pnNorte.add(crearPanelBalance(), BorderLayout.CENTER);
        contentPane.add(pnNorte, BorderLayout.NORTH);
        contentPane.add(crearPestanas(), BorderLayout.CENTER);

        setMinimumSize(new Dimension(760, 600));
        setLocationRelativeTo(null);

        inicializarPeriodoPorDefecto();
    }

    private JPanel crearPanelPeriodo() {
        JPanel pn = new JPanel(new BorderLayout());
        pn.setBorder(BorderFactory.createTitledBorder("Periodo"));

        cbTipoPeriodo = new JComboBox<>(TipoPeriodo.values());
        dcFecha = new JDateChooser();
        dcFecha.setDateFormatString("dd/MM/yyyy");
        dcFecha.setPreferredSize(new Dimension(120, 25));

        JButton btConsultar = new JButton("Consultar");
        btConsultar.addActionListener(e -> consultar());

        JPanel pnFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnFiltro.add(new JLabel("Tipo de periodo:"));
        pnFiltro.add(cbTipoPeriodo);
        pnFiltro.add(new JLabel("Fecha de referencia:"));
        pnFiltro.add(dcFecha);
        pnFiltro.add(btConsultar);
        pn.add(pnFiltro, BorderLayout.CENTER);

        lbPeriodo = new JLabel(" ");
        lbPeriodo.setFont(lbPeriodo.getFont().deriveFont(Font.BOLD));
        lbPeriodo.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));
        pn.add(lbPeriodo, BorderLayout.SOUTH);
        return pn;
    }

    private JPanel crearPanelBalance() {
        JPanel pn = new JPanel(new GridLayout(1, 3, 10, 0));
        pn.setBorder(BorderFactory.createTitledBorder("Balance del periodo"));
        lbIngresos = crearValor();
        lbGastos = crearValor();
        lbBalance = crearValor();
        pn.add(crearCaja("Ingresos", lbIngresos));
        pn.add(crearCaja("Gastos", lbGastos));
        pn.add(crearCaja("Balance", lbBalance));
        return pn;
    }

    private static JLabel crearValor() {
        JLabel lb = new JLabel("0,00 €", SwingConstants.CENTER);
        lb.setFont(new Font("Arial", Font.BOLD, 20));
        return lb;
    }

    private static JPanel crearCaja(String titulo, JLabel valor) {
        JPanel pn = new JPanel(new BorderLayout(0, 4));
        pn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        pn.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
        pn.add(valor, BorderLayout.CENTER);
        return pn;
    }

    private JTabbedPane crearPestanas() {
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Resumen", crearPanelResumen());
        pestanas.addTab("Gráficos", crearPanelGraficos());
        return pestanas;
    }

    private JPanel crearPanelResumen() {
        JPanel pn = new JPanel(new GridLayout(1, 2, 10, 0));
        pn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ingresos por tipo de venta
        JPanel pnIngresos = new JPanel(new BorderLayout(5, 5));
        pnIngresos.setBorder(BorderFactory.createTitledBorder("Ingresos por tipo de venta"));
        modeloIngresos = crearModeloTabla();
        pnIngresos.add(new JScrollPane(crearTabla(modeloIngresos)), BorderLayout.CENTER);
        lbTotalIngresos = crearTotal();
        pnIngresos.add(lbTotalIngresos, BorderLayout.SOUTH);
        pn.add(pnIngresos);

        // gastos: solo salarios
        JPanel pnGastos = new JPanel(new BorderLayout(5, 5));
        pnGastos.setBorder(BorderFactory.createTitledBorder("Gastos"));
        modeloGastos = crearModeloTabla();
        pnGastos.add(new JScrollPane(crearTabla(modeloGastos)), BorderLayout.CENTER);
        JPanel pnTotalGastos = new JPanel(new GridLayout(0, 1));
        lbTotalGastos = crearTotal();
        lbNotaGastos = new JLabel(" ");
        lbNotaGastos.setFont(lbNotaGastos.getFont().deriveFont(Font.ITALIC, 11f));
        lbNotaGastos.setForeground(Color.DARK_GRAY);
        pnTotalGastos.add(lbTotalGastos);
        pnTotalGastos.add(lbNotaGastos);
        pnGastos.add(pnTotalGastos, BorderLayout.SOUTH);
        pn.add(pnGastos);
        return pn;
    }

    private static DefaultTableModel crearModeloTabla() {
        return new DefaultTableModel(new String[] { "Concepto", "Importe" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tabla.getColumnModel().getColumn(1).setCellRenderer(derecha);
        tabla.getColumnModel().getColumn(1).setMinWidth(130);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(1).setMaxWidth(170);
        return tabla;
    }

    private static JLabel crearTotal() {
        JLabel lb = new JLabel(" ", SwingConstants.RIGHT);
        lb.setFont(new Font("Arial", Font.BOLD, 14));
        return lb;
    }

    private JPanel crearPanelGraficos() {
        JPanel pn = new JPanel(new BorderLayout(5, 5));
        pn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cbGrafico = new JComboBox<>(TipoGrafico.values());
        cbGrafico.addActionListener(e -> panelGrafico.setTipoGrafico((TipoGrafico) cbGrafico.getSelectedItem()));
        JPanel pnSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnSelector.add(new JLabel("Gráfico:"));
        pnSelector.add(cbGrafico);
        pn.add(pnSelector, BorderLayout.NORTH);

        panelGrafico = new PanelGrafico();
        panelGrafico.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pn.add(panelGrafico, BorderLayout.CENTER);
        return pn;
    }

    // Acciones

    /**
     * Por defecto se propone el mes actual
     */
    private void inicializarPeriodoPorDefecto() {
        cbTipoPeriodo.setSelectedItem(TipoPeriodo.MES);
        dcFecha.setDate(aDate(LocalDate.now()));
        consultar();
    }

    private void consultar() {
        try {
            InformeFinanciero informe = gestion.generarInforme((TipoPeriodo) cbTipoPeriodo.getSelectedItem(),
                    aLocalDate(dcFecha.getDate()));
            mostrar(informe);
        } catch (InformeFinancieroException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar(InformeFinanciero informe) {
        lbPeriodo.setText("Periodo mostrado: " + informe.getPeriodo().getEtiqueta());

        lbIngresos.setText(euros(informe.getTotalIngresos()));
        lbGastos.setText(euros(informe.getGastos()));
        lbBalance.setText(euros(informe.getBalance()));
        lbBalance.setForeground(informe.getBalance() < 0 ? COLOR_NEGATIVO : COLOR_POSITIVO);

        modeloIngresos.setRowCount(0);
        for (TipoIngreso tipo : TipoIngreso.values()) {
            modeloIngresos.addRow(new Object[] { tipo.getConcepto(), euros(informe.getIngreso(tipo)) });
        }
        lbTotalIngresos.setText("Total ingresos: " + euros(informe.getTotalIngresos()));

        modeloGastos.setRowCount(0);
        modeloGastos.addRow(new Object[] { "Salarios de los empleados", euros(informe.getGastos()) });
        lbTotalGastos.setText("Total gastos: " + euros(informe.getGastos()));
        lbNotaGastos.setText(notaGastos(informe));

        panelGrafico.setInforme(informe);
    }

    private static String notaGastos(InformeFinanciero informe) {
        int n = informe.getNumEmpleados();
        String base = "Salario anual bruto de " + n + (n == 1 ? " empleado" : " empleados");
        switch (informe.getPeriodo().getTipo()) {
        case SEMANA:
            return base + " entre 52 (semana)";
        case MES:
            return base + " entre 12 (mes)";
        default:
            return base + " (año completo)";
        }
    }

    private static String euros(double importe) {
        return String.format(FORMATO_EUROS, importe);
    }

    private static LocalDate aLocalDate(Date fecha) {
        return fecha == null ? null : fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static Date aDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Libera la conexion y devuelve el control a la ventana principal
     */
    private void cerrar(VentanaPrincipal ventanaPrincipal) {
        try {
            gestion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ventanaPrincipal.setVisible(true);
    }
}