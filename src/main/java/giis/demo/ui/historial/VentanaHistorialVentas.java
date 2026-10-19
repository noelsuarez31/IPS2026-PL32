package giis.demo.ui.historial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import giis.demo.exceptions.HistorialException;
import giis.demo.model.historial.HistorialVentas;
import giis.demo.model.historial.Ingreso;
import giis.demo.model.historial.LineaDetalle;
import giis.demo.ui.VentanaPrincipal;

public class VentanaHistorialVentas extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private static final String FORMATO_EUROS = "%.2f €";

	private HistorialVentas historial;

	private JDateChooser dcDesde;
	private JDateChooser dcHasta;
	private JTable tablaIngresos;
	private DefaultTableModel modeloIngresos;
	private JTable tablaDetalle;
	private DefaultTableModel modeloDetalle;
	private JLabel lbTotalIngresos;
	private JLabel lbDetalle;
	private JLabel lbTotalDetalle;

	// Ingreso de cada fila de la tabla (mismo orden)
	private List<Ingreso> ingresosMostrados = new ArrayList<>();

	public VentanaHistorialVentas(VentanaPrincipal ventanaPrincipal, HistorialVentas historial) {
		this.historial = historial;

		setTitle("Historial de ventas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 600);
		setLocationRelativeTo(ventanaPrincipal);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				cerrar(ventanaPrincipal);
			}
		});

		JPanel contentPane = new JPanel(new BorderLayout(5, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.add(crearPanelFiltro(), BorderLayout.NORTH);
		contentPane.add(crearPanelCentral(), BorderLayout.CENTER);

		setLocationRelativeTo(null);

		inicializarRangoPorDefecto();
	}

	private JPanel crearPanelFiltro() {
		JPanel pnFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		pnFiltro.setBorder(BorderFactory.createTitledBorder("Rango de fechas"));

		dcDesde = new JDateChooser();
		dcDesde.setDateFormatString("dd/MM/yyyy");
		dcDesde.setPreferredSize(new java.awt.Dimension(120, 25));
		dcHasta = new JDateChooser();
		dcHasta.setDateFormatString("dd/MM/yyyy");
		dcHasta.setPreferredSize(new java.awt.Dimension(120, 25));

		JButton btConsultar = new JButton("Consultar");
		btConsultar.addActionListener(e -> consultar());

		pnFiltro.add(new JLabel("Desde:"));
		pnFiltro.add(dcDesde);
		pnFiltro.add(new JLabel("Hasta:"));
		pnFiltro.add(dcHasta);
		pnFiltro.add(btConsultar);
		return pnFiltro;
	}

	private JSplitPane crearPanelCentral() {
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, crearPanelIngresos(), crearPanelDetalle());
		split.setResizeWeight(0.6);
		return split;
	}

	private JPanel crearPanelIngresos() {
		JPanel pn = new JPanel(new BorderLayout(5, 5));
		pn.setBorder(BorderFactory.createTitledBorder("Ingresos"));

		modeloIngresos = new DefaultTableModel(new String[] { "Concepto", "Fecha y hora", "Cuantía" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaIngresos = new JTable(modeloIngresos);
		tablaIngresos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaIngresos.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				mostrarDetalleSeleccionado();
			}
		});
		pn.add(new JScrollPane(tablaIngresos), BorderLayout.CENTER);

		lbTotalIngresos = new JLabel("Total de ingresos: 0.00 €");
		lbTotalIngresos.setFont(new Font("Arial", Font.BOLD, 16));
		lbTotalIngresos.setHorizontalAlignment(JLabel.RIGHT);
		pn.add(lbTotalIngresos, BorderLayout.SOUTH);
		return pn;
	}

	private JPanel crearPanelDetalle() {
		JPanel pn = new JPanel(new BorderLayout(5, 5));
		pn.setBorder(BorderFactory.createTitledBorder("Detalle de la venta de merchandising"));

		lbDetalle = new JLabel(" ");
		lbDetalle.setForeground(Color.DARK_GRAY);
		pn.add(lbDetalle, BorderLayout.NORTH);

		modeloDetalle = new DefaultTableModel(new String[] { "Producto", "Unidades", "Precio unitario", "Precio total" },
				0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaDetalle = new JTable(modeloDetalle);
		pn.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

		lbTotalDetalle = new JLabel(" ");
		lbTotalDetalle.setFont(new Font("Arial", Font.BOLD, 14));
		lbTotalDetalle.setHorizontalAlignment(JLabel.RIGHT);
		pn.add(lbTotalDetalle, BorderLayout.SOUTH);
		return pn;
	}

	/**
	 * Por defecto se propone el mes actual
	 */
	private void inicializarRangoPorDefecto() {
		LocalDate hoy = LocalDate.now();
		dcDesde.setDate(aDate(hoy.withDayOfMonth(1)));
		dcHasta.setDate(aDate(hoy.withDayOfMonth(hoy.lengthOfMonth())));
		consultar();
	}

	private void consultar() {
		try {
			List<Ingreso> ingresos = historial.obtenerIngresos(aLocalDate(dcDesde.getDate()),
					aLocalDate(dcHasta.getDate()));
			cargarIngresos(ingresos);
			lbTotalIngresos.setText(String.format("Total de ingresos: " + FORMATO_EUROS, historial.calcularTotal(ingresos)));
		} catch (HistorialException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarIngresos(List<Ingreso> ingresos) {
		modeloIngresos.setRowCount(0);
		ingresosMostrados = new ArrayList<>(ingresos);
		for (Ingreso ingreso : ingresos) {
			modeloIngresos.addRow(new Object[] { ingreso.getConcepto(), ingreso.getFecha().format(FORMATO_FECHA_HORA),
					String.format(FORMATO_EUROS, ingreso.getImporte()) });
		}
		limpiarDetalle();
	}

	private void mostrarDetalleSeleccionado() {
		int fila = tablaIngresos.getSelectedRow();
		limpiarDetalle();
		if (fila == -1) {
			return;
		}

		Ingreso ingreso = ingresosMostrados.get(fila);
		if (!ingreso.esMerchandising()) {
			lbDetalle.setText("Este concepto no tiene detalle adicional");
			return;
		}

		try {
			double total = 0.0;
			for (LineaDetalle linea : historial.obtenerDetalleMerchandising(ingreso)) {
				modeloDetalle.addRow(new Object[] { linea.getNombreProducto(), linea.getUnidades(),
						String.format(FORMATO_EUROS, linea.getPrecioUnitario()),
						String.format(FORMATO_EUROS, linea.getSubtotal()) });
				total += linea.getSubtotal();
			}
			lbTotalDetalle.setText(String.format("Total de la venta: " + FORMATO_EUROS, total));
		} catch (HistorialException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void limpiarDetalle() {
		modeloDetalle.setRowCount(0);
		lbDetalle.setText(" ");
		lbTotalDetalle.setText(" ");
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
			historial.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ventanaPrincipal.setVisible(true);
	}
}
