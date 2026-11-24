package giis.demo.ui.tienda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.tienda.GenerarFacturaService;
import giis.demo.model.tienda.LineaFacturaDTO;
import giis.demo.ui.login.VentanaPrincipal;

/**
 * HU-36813: Ventana para la generación de factura de venta en PDF.
 */
public class VentanaFactura extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Color COLOR_FONDO = new Color(240, 240, 240);
	private static final Color COLOR_SOLO_LECTURA = new Color(228, 228, 228);

	private VentanaPrincipal vPrincipal;
	private GenerarFacturaService service;

	// Contenedores principales
	private JPanel contentPane;
	private JPanel pnCentral;
	private JPanel pnSur;

	// Bloque 1: Datos de la Factura y Vendedor
	private JPanel pnDatosFactura;
	private JLabel lbNumFactura;
	private JTextField txNumFactura;
	private JLabel lbFecha;
	private JTextField txFecha;
	private JLabel lbHora;
	private JTextField txHora;
	private JLabel lbEmisor;
	private JTextField txEmisor;
	private JLabel lbNifVendedor;
	private JTextField txNifVendedor;
	private JLabel lbDomicilioVendedor;
	private JTextField txDomicilioVendedor;

	// Bloque 2: Datos del Comprador
	private JPanel pnDatosComprador;
	private JLabel lbDniComprador;
	private JTextField txDniComprador;
	private JLabel lbNombreComprador;
	private JTextField txNombreComprador;
	private JLabel lbUsuarioComprador;
	private JTextField txUsuarioComprador;
	private JLabel lbDomicilioComprador;
	private JTextField txDomicilioComprador;

	// Bloque 3: Líneas de la Venta
	private JPanel pnLineasVenta;
	private JLabel lbUnidadesCesta;
	private JScrollPane spTabla;
	private JTable tablaLineas;
	private DefaultTableModel modeloTabla;
	private JPanel pnPieLineas;
	private JPanel pnDestinoPdf;
	private JLabel lbPrefijoDestino;
	private JLabel lbDestinoPdf;
	private JPanel pnTotales;
	private JLabel lbTxtBase;
	private JLabel lbPrecioBase;
	private JLabel lbTxtIva;
	private JLabel lbIva;
	private JLabel lbTxtTotal;
	private JLabel lbTotalFactura;

	// Botones de acción
	private JButton btnCancelar;
	private JButton btnGenerar;

	public VentanaFactura(VentanaPrincipal vPrincipal) {
		super(vPrincipal, "Generar Factura de Venta", true);
		this.vPrincipal = vPrincipal;
		this.service = vPrincipal.getGenerarFacturaService();

		setSize(850, 790);
		setMinimumSize(new Dimension(780, 730));
		setLocationRelativeTo(vPrincipal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(getContentPane());

		cargarDatosDesdeServicio();
	}

	@Override
	public JPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout(12, 14));
			contentPane.setBorder(new EmptyBorder(16, 18, 16, 18));
			contentPane.setBackground(COLOR_FONDO);
			contentPane.add(getPnCentral(), BorderLayout.CENTER);
			contentPane.add(getPnSur(), BorderLayout.SOUTH);
		}
		return contentPane;
	}

	private JPanel getPnCentral() {
		if (pnCentral == null) {
			pnCentral = new JPanel();
			pnCentral.setLayout(new BoxLayout(pnCentral, BoxLayout.Y_AXIS));
			pnCentral.setBackground(COLOR_FONDO);
			pnCentral.add(getPnDatosFactura());
			pnCentral.add(Box.createVerticalStrut(16));
			pnCentral.add(getPnDatosComprador());
			pnCentral.add(Box.createVerticalStrut(16));
			pnCentral.add(getPnLineasVenta());
		}
		return pnCentral;
	}

	// Datos de la factura y vendedor
	private JPanel getPnDatosFactura() {
		if (pnDatosFactura == null) {
			pnDatosFactura = new JPanel(new GridBagLayout());
			pnDatosFactura.setBackground(COLOR_FONDO);
			pnDatosFactura.setBorder(crearBordeSeccion("DATOS DE LA FACTURA Y VENDEDOR"));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(7, 8, 7, 8);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			// Fila 0: Nº factura, Fecha, Hora
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
			pnDatosFactura.add(getLbNumFactura(), gbc);

			gbc.gridx = 1; gbc.weightx = 0.45;
			pnDatosFactura.add(getTxNumFactura(), gbc);

			gbc.gridx = 2; gbc.weightx = 0.0;
			pnDatosFactura.add(getLbFecha(), gbc);

			gbc.gridx = 3; gbc.weightx = 0.25;
			pnDatosFactura.add(getTxFecha(), gbc);

			gbc.gridx = 4; gbc.weightx = 0.0;
			pnDatosFactura.add(getLbHora(), gbc);

			gbc.gridx = 5; gbc.weightx = 0.25;
			pnDatosFactura.add(getTxHora(), gbc);

			// Fila 1: Emisor / vendedor, NIF del vendedor
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
			pnDatosFactura.add(getLbEmisor(), gbc);

			gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.6;
			pnDatosFactura.add(getTxEmisor(), gbc);

			gbc.gridx = 3; gbc.gridwidth = 1; gbc.weightx = 0.0;
			pnDatosFactura.add(getLbNifVendedor(), gbc);

			gbc.gridx = 4; gbc.gridwidth = 2; gbc.weightx = 0.4;
			pnDatosFactura.add(getTxNifVendedor(), gbc);

			// Fila 2: Domicilio fiscal
			gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
			pnDatosFactura.add(getLbDomicilioVendedor(), gbc);

			gbc.gridx = 1; gbc.gridwidth = 5; gbc.weightx = 1.0;
			pnDatosFactura.add(getTxDomicilioVendedor(), gbc);
		}
		return pnDatosFactura;
	}

	private JLabel getLbNumFactura() {
		if (lbNumFactura == null) {
			lbNumFactura = crearEtiquetaNegrita("Nº factura:");
		}
		return lbNumFactura;
	}

	private JTextField getTxNumFactura() {
		if (txNumFactura == null) {
			txNumFactura = crearCampoSoloLectura();
			txNumFactura.setFont(new Font("Monospaced", Font.BOLD, 12));
		}
		return txNumFactura;
	}

	private JLabel getLbFecha() {
		if (lbFecha == null) {
			lbFecha = crearEtiquetaNegrita("Fecha:");
		}
		return lbFecha;
	}

	private JTextField getTxFecha() {
		if (txFecha == null) {
			txFecha = crearCampoSoloLectura();
			txFecha.setHorizontalAlignment(JTextField.CENTER);
		}
		return txFecha;
	}

	private JLabel getLbHora() {
		if (lbHora == null) {
			lbHora = crearEtiquetaNegrita("Hora:");
		}
		return lbHora;
	}

	private JTextField getTxHora() {
		if (txHora == null) {
			txHora = crearCampoSoloLectura();
			txHora.setHorizontalAlignment(JTextField.CENTER);
		}
		return txHora;
	}

	private JLabel getLbEmisor() {
		if (lbEmisor == null) {
			lbEmisor = crearEtiquetaNegrita("Emisor / vendedor:");
		}
		return lbEmisor;
	}

	private JTextField getTxEmisor() {
		if (txEmisor == null) {
			txEmisor = crearCampoSoloLectura();
		}
		return txEmisor;
	}

	private JLabel getLbNifVendedor() {
		if (lbNifVendedor == null) {
			lbNifVendedor = crearEtiquetaNegrita("NIF del vendedor:");
		}
		return lbNifVendedor;
	}

	private JTextField getTxNifVendedor() {
		if (txNifVendedor == null) {
			txNifVendedor = crearCampoSoloLectura();
			txNifVendedor.setHorizontalAlignment(JTextField.CENTER);
		}
		return txNifVendedor;
	}

	private JLabel getLbDomicilioVendedor() {
		if (lbDomicilioVendedor == null) {
			lbDomicilioVendedor = crearEtiquetaNegrita("Domicilio fiscal:");
		}
		return lbDomicilioVendedor;
	}

	private JTextField getTxDomicilioVendedor() {
		if (txDomicilioVendedor == null) {
			txDomicilioVendedor = crearCampoSoloLectura();
		}
		return txDomicilioVendedor;
	}

	// Datos del comprador
	private JPanel getPnDatosComprador() {
		if (pnDatosComprador == null) {
			pnDatosComprador = new JPanel(new GridBagLayout());
			pnDatosComprador.setBackground(COLOR_FONDO);
			pnDatosComprador.setBorder(crearBordeSeccion("DATOS DEL COMPRADOR"));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(7, 8, 7, 8);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			// Fila 0: DNI/NIF, Nombre y Apellidos
			gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
			pnDatosComprador.add(getLbDniComprador(), gbc);

			gbc.gridx = 1; gbc.weightx = 0.45;
			pnDatosComprador.add(getTxDniComprador(), gbc);

			gbc.gridx = 2; gbc.weightx = 0.0;
			pnDatosComprador.add(getLbNombreComprador(), gbc);

			gbc.gridx = 3; gbc.weightx = 0.55;
			pnDatosComprador.add(getTxNombreComprador(), gbc);

			// Fila 1: Nombre de usuario, Domicilio Fiscal
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
			pnDatosComprador.add(getLbUsuarioComprador(), gbc);

			gbc.gridx = 1; gbc.weightx = 0.45;
			pnDatosComprador.add(getTxUsuarioComprador(), gbc);

			gbc.gridx = 2; gbc.weightx = 0.0;
			pnDatosComprador.add(getLbDomicilioComprador(), gbc);

			gbc.gridx = 3; gbc.weightx = 0.55;
			pnDatosComprador.add(getTxDomicilioComprador(), gbc);
		}
		return pnDatosComprador;
	}

	private JLabel getLbDniComprador() {
		if (lbDniComprador == null) {
			lbDniComprador = crearEtiquetaNegrita("DNI/NIF:");
		}
		return lbDniComprador;
	}

	private JTextField getTxDniComprador() {
		if (txDniComprador == null) {
			txDniComprador = crearCampoEditable();
		}
		return txDniComprador;
	}

	private JLabel getLbNombreComprador() {
		if (lbNombreComprador == null) {
			lbNombreComprador = crearEtiquetaNegrita("Nombre y Apellidos:");
		}
		return lbNombreComprador;
	}

	private JTextField getTxNombreComprador() {
		if (txNombreComprador == null) {
			txNombreComprador = crearCampoEditable();
		}
		return txNombreComprador;
	}

	private JLabel getLbUsuarioComprador() {
		if (lbUsuarioComprador == null) {
			lbUsuarioComprador = crearEtiquetaNegrita("Nombre de usuario:");
		}
		return lbUsuarioComprador;
	}

	private JTextField getTxUsuarioComprador() {
		if (txUsuarioComprador == null) {
			txUsuarioComprador = crearCampoEditable();
		}
		return txUsuarioComprador;
	}

	private JLabel getLbDomicilioComprador() {
		if (lbDomicilioComprador == null) {
			lbDomicilioComprador = crearEtiquetaNegrita("Domicilio Fiscal:");
		}
		return lbDomicilioComprador;
	}

	private JTextField getTxDomicilioComprador() {
		if (txDomicilioComprador == null) {
			txDomicilioComprador = crearCampoEditable();
		}
		return txDomicilioComprador;
	}

	// Lineas de la venta
	private JPanel getPnLineasVenta() {
		if (pnLineasVenta == null) {
			pnLineasVenta = new JPanel(new BorderLayout(8, 10));
			pnLineasVenta.setBackground(COLOR_FONDO);
			pnLineasVenta.setBorder(crearBordeSeccion("LINEAS DE LA VENTA"));
			pnLineasVenta.add(getLbUnidadesCesta(), BorderLayout.NORTH);
			pnLineasVenta.add(getSpTabla(), BorderLayout.CENTER);
			pnLineasVenta.add(getPnPieLineas(), BorderLayout.SOUTH);
		}
		return pnLineasVenta;
	}

	private JLabel getLbUnidadesCesta() {
		if (lbUnidadesCesta == null) {
			lbUnidadesCesta = new JLabel("0 unidades en cesta");
			lbUnidadesCesta.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbUnidadesCesta.setBorder(new EmptyBorder(2, 4, 4, 4));
		}
		return lbUnidadesCesta;
	}

	private JScrollPane getSpTabla() {
		if (spTabla == null) {
			spTabla = new JScrollPane(getTablaLineas());
			spTabla.setPreferredSize(new Dimension(spTabla.getPreferredSize().width, 165));
		}
		return spTabla;
	}

	private JTable getTablaLineas() {
		if (tablaLineas == null) {
			tablaLineas = new JTable(getModeloTabla());
			tablaLineas.setRowHeight(26);
			tablaLineas.setFont(new Font("Tahoma", Font.PLAIN, 12));
			tablaLineas.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
			tablaLineas.getTableHeader().setReorderingAllowed(false);

			DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
			centro.setHorizontalAlignment(SwingConstants.CENTER);
			DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
			derecha.setHorizontalAlignment(SwingConstants.RIGHT);

			tablaLineas.getColumnModel().getColumn(0).setPreferredWidth(320);
			tablaLineas.getColumnModel().getColumn(1).setPreferredWidth(60);
			tablaLineas.getColumnModel().getColumn(1).setCellRenderer(centro);
			tablaLineas.getColumnModel().getColumn(2).setPreferredWidth(110);
			tablaLineas.getColumnModel().getColumn(2).setCellRenderer(derecha);
			tablaLineas.getColumnModel().getColumn(3).setPreferredWidth(110);
			tablaLineas.getColumnModel().getColumn(3).setCellRenderer(derecha);
		}
		return tablaLineas;
	}

	private DefaultTableModel getModeloTabla() {
		if (modeloTabla == null) {
			String[] columnas = { "Producto", "Uds", "Precio Unitario", "Subtotal" };
			modeloTabla = new DefaultTableModel(columnas, 0) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		return modeloTabla;
	}

	private JPanel getPnPieLineas() {
		if (pnPieLineas == null) {
			pnPieLineas = new JPanel(new BorderLayout(12, 8));
			pnPieLineas.setBackground(COLOR_FONDO);
			pnPieLineas.setBorder(new EmptyBorder(10, 4, 4, 4));
			pnPieLineas.add(getPnDestinoPdf(), BorderLayout.WEST);
			pnPieLineas.add(getPnTotales(), BorderLayout.EAST);
		}
		return pnPieLineas;
	}

	private JPanel getPnDestinoPdf() {
		if (pnDestinoPdf == null) {
			pnDestinoPdf = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 14));
			pnDestinoPdf.setBackground(COLOR_FONDO);
			pnDestinoPdf.add(getLbPrefijoDestino());
			pnDestinoPdf.add(getLbDestinoPdf());
		}
		return pnDestinoPdf;
	}

	private JLabel getLbPrefijoDestino() {
		if (lbPrefijoDestino == null) {
			lbPrefijoDestino = new JLabel("Destino PDF: ");
			lbPrefijoDestino.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lbPrefijoDestino;
	}

	private JLabel getLbDestinoPdf() {
		if (lbDestinoPdf == null) {
			lbDestinoPdf = new JLabel();
			lbDestinoPdf.setFont(new Font("Monospaced", Font.PLAIN, 12));
		}
		return lbDestinoPdf;
	}

	private JPanel getPnTotales() {
		if (pnTotales == null) {
			pnTotales = new JPanel(new GridLayout(3, 2, 20, 8));
			pnTotales.setBackground(new Color(232, 232, 232));
			pnTotales.setBorder(new CompoundBorder(
					BorderFactory.createEtchedBorder(),
					new EmptyBorder(10, 16, 10, 16)
			));
			pnTotales.add(getLbTxtBase());
			pnTotales.add(getLbPrecioBase());
			pnTotales.add(getLbTxtIva());
			pnTotales.add(getLbIva());
			pnTotales.add(getLbTxtTotal());
			pnTotales.add(getLbTotalFactura());
		}
		return pnTotales;
	}

	private JLabel getLbTxtBase() {
		if (lbTxtBase == null) {
			lbTxtBase = new JLabel("Precio Base:");
			lbTxtBase.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbTxtBase;
	}

	private JLabel getLbPrecioBase() {
		if (lbPrecioBase == null) {
			lbPrecioBase = new JLabel("0.00 €", SwingConstants.RIGHT);
			lbPrecioBase.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbPrecioBase;
	}

	private JLabel getLbTxtIva() {
		if (lbTxtIva == null) {
			lbTxtIva = new JLabel("IVA (21%):");
			lbTxtIva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbTxtIva;
	}

	private JLabel getLbIva() {
		if (lbIva == null) {
			lbIva = new JLabel("0.00 €", SwingConstants.RIGHT);
			lbIva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbIva;
	}

	private JLabel getLbTxtTotal() {
		if (lbTxtTotal == null) {
			lbTxtTotal = new JLabel("Total factura:");
			lbTxtTotal.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		return lbTxtTotal;
	}

	private JLabel getLbTotalFactura() {
		if (lbTotalFactura == null) {
			lbTotalFactura = new JLabel("0.00 €", SwingConstants.RIGHT);
			lbTotalFactura.setFont(new Font("Tahoma", Font.BOLD, 14));
		}
		return lbTotalFactura;
	}

	// Panel sur
	private JPanel getPnSur() {
		if (pnSur == null) {
			pnSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
			pnSur.setBackground(COLOR_FONDO);
			pnSur.add(getBtnCancelar());
			pnSur.add(getBtnGenerar());
		}
		return pnSur;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnCancelar.setPreferredSize(new Dimension(120, 32));
			btnCancelar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private JButton getBtnGenerar() {
		if (btnGenerar == null) {
			btnGenerar = new JButton("Generar");
			btnGenerar.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnGenerar.setPreferredSize(new Dimension(120, 32));
			btnGenerar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					pulsarGenerar();
				}
			});
		}
		return btnGenerar;
	}

	// Metodos adicionales
	private void cargarDatosDesdeServicio() {
		getTxNumFactura().setText(service.getNumeroFactura());
		getTxFecha().setText(service.getFechaFormateada());
		getTxHora().setText(service.getHoraFormateada());
		getTxEmisor().setText(service.getEmisor());
		getTxNifVendedor().setText(service.getNifVendedor());
		getTxDomicilioVendedor().setText(service.getDomicilioVendedor());

		getLbUnidadesCesta().setText(service.getUnidadesTotales() + " unidades en cesta");
		getModeloTabla().setRowCount(0);
		for (LineaFacturaDTO linea : service.getLineasVenta()) {
			getModeloTabla().addRow(new Object[] {
					linea.getProducto(),
					linea.getUnidades(),
					String.format("%.2f €", linea.getPrecioUnitario()),
					String.format("%.2f €", linea.getSubtotal())
			});
		}

		getLbDestinoPdf().setText(service.getRutaPdfDestino());
		getLbPrecioBase().setText(String.format("%.2f €", service.getPrecioBase()));
		getLbIva().setText(String.format("%.2f €", service.getIva()));
		getLbTotalFactura().setText(String.format("%.2f €", service.getTotalFactura()));
	}

	private void pulsarGenerar() {
		String dni = getTxDniComprador().getText().trim();
		String nombre = getTxNombreComprador().getText().trim();
		String usuario = getTxUsuarioComprador().getText().trim();
		String domicilio = getTxDomicilioComprador().getText().trim();

		try {
			String rutaGenerada = service.generarFactura(dni, nombre, usuario, domicilio);

			String mensaje = "¡Venta registrada correctamente!\n"
					+ "Puedes consultar la factura en:\n"
					+ rutaGenerada;

			JOptionPane.showMessageDialog(this, mensaje, "Factura Generada", JOptionPane.INFORMATION_MESSAGE);
			dispose();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"No se pudo generar la factura:\n" + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField crearCampoSoloLectura() {
		JTextField tf = new JTextField();
		tf.setEditable(false);
		tf.setBackground(COLOR_SOLO_LECTURA);
		tf.setPreferredSize(new Dimension(0, 26));
		return tf;
	}

	private JTextField crearCampoEditable() {
		JTextField tf = new JTextField();
		tf.setPreferredSize(new Dimension(0, 26));
		return tf;
	}

	private CompoundBorder crearBordeSeccion(String titulo) {
		TitledBorder titled = BorderFactory.createTitledBorder(
				null,
				titulo,
				TitledBorder.LEADING,
				TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 12),
				null
		);
		return new CompoundBorder(titled, new EmptyBorder(12, 14, 12, 14));
	}

	private JLabel crearEtiquetaNegrita(String texto) {
		JLabel label = new JLabel(texto);
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		return label;
	}
}