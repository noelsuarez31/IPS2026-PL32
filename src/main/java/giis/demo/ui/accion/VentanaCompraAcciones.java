package giis.demo.ui.accion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.accion.Accion;
import giis.demo.model.accion.Accionista;
import giis.demo.model.accion.GestionAcciones;
import giis.demo.model.accion.ResumenParticipacion;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.SwingUtil;
import giis.demo.util.UnexpectedException;

/**
 * Ventana para la compra de acciones puestas a la venta por otros accionistas.
 */
public class VentanaCompraAcciones extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vp;
	private GestionAcciones gestionAcciones;
	private Accionista accionistaActual;
	private List<Accion> listaAccionesEnVenta;

	// Zona Norte: Identificación del comprador
	private JLabel lblDniComprador;
	private JComboBox<String> cbDniComprador;
	private JLabel lblCompradorTexto;
	private JLabel lblNombreComprador;

	// Zona Centro: Tabla de acciones a la venta
	private JLabel lblAccionesEnVenta;
	private JTable tablaAcciones;
	private DefaultTableModel modeloAcciones;
	private JScrollPane scrollPaneAcciones;

	// Zona Sur: Métricas del comprador y botones de navegación / acción
	private JLabel lblTotalAccionesTexto;
	private JLabel lblTotalAcciones;
	private JLabel lblTotalClubTexto;
	private JLabel lblTotalClub;
	private JLabel lblParticipacionTexto;
	private JLabel lblParticipacion;
	private JButton btnAtras;
	private JButton btnComprar;

	public VentanaCompraAcciones(VentanaPrincipal vp) {
		this.vp = vp;
		this.gestionAcciones = new GestionAcciones();
		this.listaAccionesEnVenta = new ArrayList<>();
		inicializarComponentes();
	}

	public VentanaCompraAcciones() {
		this.gestionAcciones = new GestionAcciones();
		this.listaAccionesEnVenta = new ArrayList<>();
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Comprar acciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 820, 500);
		setMinimumSize(new Dimension(750, 420));

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
		setContentPane(contentPane);

		// 1. ZONA NORTE: Barra de identificación del comprador
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
		panelNorte.add(getLblDniComprador());
		panelNorte.add(getCbDniComprador());
		panelNorte.add(getLblCompradorTexto());
		panelNorte.add(getLblNombreComprador());
		contentPane.add(panelNorte, BorderLayout.NORTH);

		// 2. ZONA CENTRO: Listado de acciones en venta de otros accionistas
		JPanel panelCentro = new JPanel(new BorderLayout(0, 5));
		panelCentro.add(getLblAccionesEnVenta(), BorderLayout.NORTH);
		panelCentro.add(getScrollPaneAcciones(), BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);

		// 3. ZONA SUR: Métricas de participación y botones Atrás / Comprar
		JPanel panelSur = new JPanel(new BorderLayout());

		JPanel panelMetricas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		panelMetricas.add(getLblTotalAccionesTexto());
		panelMetricas.add(getLblTotalAcciones());
		panelMetricas.add(getLblTotalClubTexto());
		panelMetricas.add(getLblTotalClub());
		panelMetricas.add(getLblParticipacionTexto());
		panelMetricas.add(getLblParticipacion());

		JPanel panelBotonesSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		panelBotonesSur.add(getBtnAtras());
		panelBotonesSur.add(getBtnComprar());

		panelSur.add(panelMetricas, BorderLayout.WEST);
		panelSur.add(panelBotonesSur, BorderLayout.EAST);
		contentPane.add(panelSur, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		configurarEventosYCargaInicial();
	}

	private void configurarEventosYCargaInicial() {
		// Modelo no editable para la tabla de acciones en venta
		modeloAcciones = new DefaultTableModel(
				new Object[] { "Id acción", "Vendedor", "Fecha adq", "Origen", "Precio venta" },
				0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaAcciones().setModel(modeloAcciones);

		// Permitir selección múltiple de filas (para comprar varias acciones)
		getTablaAcciones().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Listener para habilitar/deshabilitar el botón Comprar al seleccionar acciones
		getTablaAcciones().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				actualizarEstadoBotonComprar();
			}
		});

		// Cargar lista inicial de DNIs de accionistas
		cargarDnisAccionistas();

		// Evento al seleccionar un DNI del ComboBox
		getCbDniComprador().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarSeleccionComprador()));
		getCbDniComprador().getEditor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarSeleccionComprador()));

		// Botón Comprar
		getBtnComprar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarCompraAcciones()));

		// Botón Atrás
		getBtnAtras().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrarVentana();
			}
		});

		// Al cerrar la ventana restaurar la VentanaPrincipal
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarVentana();
			}
		});
	}

	/**
	 * Puebla el desplegable con los DNIs de todos los accionistas.
	 */
	private void cargarDnisAccionistas() {
		try {
			List<Accionista> lista = gestionAcciones.obtenerAccionistas();
			getCbDniComprador().removeAllItems();
			for (Accionista a : lista) {
				getCbDniComprador().addItem(a.getDni());
			}
			getCbDniComprador().setSelectedIndex(-1);
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	/**
	 * Identifica al accionista comprador y carga sus métricas y las acciones disponibles a la venta.
	 */
	private void ejecutarSeleccionComprador() {
		Object item = getCbDniComprador().getEditor().getItem();
		String dni = (item != null) ? item.toString().trim() : "";
		if (dni.isEmpty()) {
			limpiarDatos();
			return;
		}

		try {
			Accionista accionista = gestionAcciones.validarYObtenerAccionista(dni);
			this.accionistaActual = accionista;
			getLblNombreComprador().setText(accionista.getNombreCompleto());
			recargarDatos();
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	/**
	 * Recarga las métricas del comprador y el listado de acciones en venta en el mercado.
	 */
	private void recargarDatos() {
		if (accionistaActual == null) {
			limpiarDatos();
			return;
		}

		try {
			// 1. Cargar métricas de participación del comprador
			ResumenParticipacion resumen = gestionAcciones.obtenerResumenParticipacion(accionistaActual.getDni());
			getLblTotalAcciones().setText(String.valueOf(resumen.getTotalAccionesPropias()));
			getLblTotalClub().setText(String.valueOf(resumen.getTotalAccionesClub()));
			getLblParticipacion().setText(String.format(Locale.GERMAN, "%.2f %%", resumen.getPorcentajeParticipacion()));

			// 2. Cargar tabla de acciones puestas a la venta por otros accionistas
			this.listaAccionesEnVenta = gestionAcciones.obtenerAccionesEnVenta(accionistaActual.getDni());
			modeloAcciones.setRowCount(0);

			for (Accion a : listaAccionesEnVenta) {
				String strPrecioVenta = (a.getPrecioVenta() != null)
						? String.format(Locale.GERMAN, "%.2f €", a.getPrecioVenta())
						: "-";

				modeloAcciones.addRow(new Object[] {
						String.format("%03d", a.getIdAccion()),
						a.getNombreVendedor(),
						a.getFechaAdquisicion(),
						a.getOrigen(),
						strPrecioVenta
				});
			}

			actualizarEstadoBotonComprar();
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private void limpiarDatos() {
		this.accionistaActual = null;
		this.listaAccionesEnVenta.clear();
		getLblNombreComprador().setText("-");
		getLblTotalAcciones().setText("0");
		getLblTotalClub().setText("0");
		getLblParticipacion().setText("0.00 %");
		modeloAcciones.setRowCount(0);
		actualizarEstadoBotonComprar();
	}

	/**
	 * El botón Comprar solo se activa si hay al menos una acción seleccionada en la tabla.
	 */
	private void actualizarEstadoBotonComprar() {
		int seleccionadas = getTablaAcciones().getSelectedRowCount();
		getBtnComprar().setEnabled(seleccionadas > 0 && accionistaActual != null);
	}

	/**
	 * Prepara el resumen de la compra, abre el diálogo modal de confirmación y procesa la adquisición.
	 */
	private void ejecutarCompraAcciones() {
		int[] filasSeleccionadas = getTablaAcciones().getSelectedRows();
		if (filasSeleccionadas == null || filasSeleccionadas.length == 0 || accionistaActual == null) {
			return;
		}

		List<Integer> idsAcciones = new ArrayList<>();
		BigDecimal importeTotal = BigDecimal.ZERO;

		for (int fila : filasSeleccionadas) {
			int idAccion = Integer.parseInt(String.valueOf(modeloAcciones.getValueAt(fila, 0)));
			idsAcciones.add(idAccion);


			// Localizar precio exacto de la acción en la lista
			for (Accion a : listaAccionesEnVenta) {
				if (a.getIdAccion() == idAccion && a.getPrecioVenta() != null) {
					importeTotal = importeTotal.add(a.getPrecioVenta());
					break;
				}
			}
		}


		// Abrir diálogo modal de confirmación
		DialogoConfirmarCompra dialogo = new DialogoConfirmarCompra(
				this, idsAcciones.size(), importeTotal);
		dialogo.setVisible(true);

		if (dialogo.isConfirmado()) {
			try {
				gestionAcciones.comprarAcciones(accionistaActual.getDni(), idsAcciones);
				JOptionPane.showMessageDialog(this, "Compra realizada con éxito.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException ex) {
				throw new UnexpectedException(ex);
			} finally {
				recargarDatos();
			}
		}
	}

	private void cerrarVentana() {
		dispose();
		if (vp != null) {
			vp.setVisible(true);
		}
	}

	// ==================== GETTERS COMPONENTES VISUALES ====================

	private JLabel getLblDniComprador() {
		if (lblDniComprador == null) {
			lblDniComprador = new JLabel("Dni comprador :");
			lblDniComprador.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblDniComprador;
	}

	private JComboBox<String> getCbDniComprador() {
		if (cbDniComprador == null) {
			cbDniComprador = new JComboBox<>();
			cbDniComprador.setEditable(true);
			cbDniComprador.setPreferredSize(new Dimension(130, 26));
		}
		return cbDniComprador;
	}

	private JLabel getLblCompradorTexto() {
		if (lblCompradorTexto == null) {
			lblCompradorTexto = new JLabel("Comprador :");
			lblCompradorTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblCompradorTexto;
	}

	private JLabel getLblNombreComprador() {
		if (lblNombreComprador == null) {
			lblNombreComprador = new JLabel("-");
			lblNombreComprador.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblNombreComprador.setForeground(new Color(0, 102, 204));
		}
		return lblNombreComprador;
	}

	private JLabel getLblAccionesEnVenta() {
		if (lblAccionesEnVenta == null) {
			lblAccionesEnVenta = new JLabel("Acciones en venta :");
			lblAccionesEnVenta.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblAccionesEnVenta;
	}

	private JTable getTablaAcciones() {
		if (tablaAcciones == null) {
			tablaAcciones = new JTable();
		}
		return tablaAcciones;
	}

	private JScrollPane getScrollPaneAcciones() {
		if (scrollPaneAcciones == null) {
			scrollPaneAcciones = new JScrollPane(getTablaAcciones());
		}
		return scrollPaneAcciones;
	}

	private JLabel getLblTotalAccionesTexto() {
		if (lblTotalAccionesTexto == null) {
			lblTotalAccionesTexto = new JLabel("Total acciones :");
			lblTotalAccionesTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblTotalAccionesTexto;
	}

	private JLabel getLblTotalAcciones() {
		if (lblTotalAcciones == null) {
			lblTotalAcciones = new JLabel("0");
			lblTotalAcciones.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblTotalAcciones.setForeground(new Color(0, 102, 204));
		}
		return lblTotalAcciones;
	}

	private JLabel getLblTotalClubTexto() {
		if (lblTotalClubTexto == null) {
			lblTotalClubTexto = new JLabel("Tot club :");
			lblTotalClubTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblTotalClubTexto;
	}

	private JLabel getLblTotalClub() {
		if (lblTotalClub == null) {
			lblTotalClub = new JLabel("0");
			lblTotalClub.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblTotalClub.setForeground(new Color(0, 102, 204));
		}
		return lblTotalClub;
	}

	private JLabel getLblParticipacionTexto() {
		if (lblParticipacionTexto == null) {
			lblParticipacionTexto = new JLabel("Part :");
			lblParticipacionTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblParticipacionTexto;
	}

	private JLabel getLblParticipacion() {
		if (lblParticipacion == null) {
			lblParticipacion = new JLabel("0.00 %");
			lblParticipacion.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblParticipacion.setForeground(new Color(0, 102, 204));
		}
		return lblParticipacion;
	}

	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.setPreferredSize(new Dimension(110, 30));
		}
		return btnAtras;
	}

	private JButton getBtnComprar() {
		if (btnComprar == null) {
			btnComprar = new JButton("Comprar");
			btnComprar.setPreferredSize(new Dimension(110, 30));
			btnComprar.setEnabled(false);
		}
		return btnComprar;
	}
}
