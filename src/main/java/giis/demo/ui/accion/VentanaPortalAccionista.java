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
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import giis.demo.model.log.GestionLogSesion;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.SwingUtil;
import giis.demo.util.UnexpectedException;

/**
 * Ventana del Portal del Accionista adaptada al usuario autenticado en la sesión.
 */
public class VentanaPortalAccionista extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vp;
	private GestionAcciones gestionAcciones;
	private Accionista accionistaActual;

	// Zona Norte: Datos del accionista autenticado
	private JLabel lblDniAccionistaTexto;
	private JLabel lblDniAccionista;
	private JLabel lblTitularTexto;
	private JLabel lblTitular;

	// Zona Centro: Listado tabular de acciones
	private JLabel lblAccionesPropiedad;
	private JTable tablaAcciones;
	private DefaultTableModel modeloAcciones;
	private JScrollPane scrollPaneAcciones;

	// Zona Sur: Métricas de participación y botones de acción
	private JLabel lblTotalAccionesTexto;
	private JLabel lblTotalAcciones;
	private JLabel lblTotalClubTexto;
	private JLabel lblTotalClub;
	private JLabel lblParticipacionTexto;
	private JLabel lblParticipacion;
	private JButton btnVender;
	private JButton btnAtras;

	/**
	 * Constructor para integración con la VentanaPrincipal (usuario logueado).
	 */
	public VentanaPortalAccionista(VentanaPrincipal vp) {
		this.vp = vp;
		this.gestionAcciones = new GestionAcciones();
		inicializarComponentes();
	}

	/**
	 * Constructor por defecto para pruebas o WindowBuilder.
	 */
	public VentanaPortalAccionista() {
		this.gestionAcciones = new GestionAcciones();
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Portal del accionista");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 820, 500);
		setMinimumSize(new Dimension(750, 420));

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
		setContentPane(contentPane);

		// 1. ZONA NORTE: Identificación fija del accionista autenticado
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		panelNorte.add(getLblDniAccionistaTexto());
		panelNorte.add(getLblDniAccionista());
		panelNorte.add(getLblTitularTexto());
		panelNorte.add(getLblTitular());
		contentPane.add(panelNorte, BorderLayout.NORTH);

		// 2. ZONA CENTRO: Tabla con las acciones en propiedad
		JPanel panelCentro = new JPanel(new BorderLayout(0, 5));
		panelCentro.add(getLblAccionesPropiedad(), BorderLayout.NORTH);
		panelCentro.add(getScrollPaneAcciones(), BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);

		// 3. ZONA SUR: Métricas de participación (izquierda) y botones Vender / Atrás (derecha)
		JPanel panelSur = new JPanel(new BorderLayout());

		JPanel panelMetricas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		panelMetricas.add(getLblTotalAccionesTexto());
		panelMetricas.add(getLblTotalAcciones());
		panelMetricas.add(getLblTotalClubTexto());
		panelMetricas.add(getLblTotalClub());
		panelMetricas.add(getLblParticipacionTexto());
		panelMetricas.add(getLblParticipacion());

		JPanel panelBotonesSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		panelBotonesSur.add(getBtnVender());
		panelBotonesSur.add(getBtnAtras());

		panelSur.add(panelMetricas, BorderLayout.WEST);
		panelSur.add(panelBotonesSur, BorderLayout.EAST);
		contentPane.add(panelSur, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		configurarEventosYCargaInicial();
	}

	private void configurarEventosYCargaInicial() {
		// Modelo no editable para la tabla de acciones
		modeloAcciones = new DefaultTableModel(
				new Object[] { "ID acción", "Fecha adquisición", "Origen", "Precio pagado", "En venta", "Precio venta" },
				0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaAcciones().setModel(modeloAcciones);

		// Evento de selección en la tabla de acciones
		getTablaAcciones().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				actualizarEstadoBotonVender();
			}
		});

		// Botón Vender
		getBtnVender().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarVentaAccion()));

		// Botón Atrás
		getBtnAtras().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrarVentana();
			}
		});

		// Restaurar VentanaPrincipal al cerrar la ventana con la X
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarVentana();
			}
		});

		// Cargar automáticamente los datos del accionista logueado
		cargarAccionistaAutenticado();
	}

	/**
	 * Carga automáticamente la información del accionista autenticado en la sesión.
	 */
	private void cargarAccionistaAutenticado() {
		if (vp != null && vp.getUsuario() != null) {
			String dni = vp.getUsuario().getDni();
			try {
				Accionista accionista = gestionAcciones.validarYObtenerAccionista(dni);
				this.accionistaActual = accionista;
				getLblDniAccionista().setText(accionista.getDni());
				cargarDatosAccionista(accionista);
				actualizarEstadoBotonVender();
				GestionLogSesion.registrarAccion("Acceso portal accionista (dni=" + accionista.getDni() + ")");
			} catch (SQLException ex) {
				throw new UnexpectedException(ex);
			}
		}
	}

	/**
	 * Carga las métricas y la tabla de acciones del accionista indicado.
	 */
	private void cargarDatosAccionista(Accionista accionista) throws SQLException {
		// Actualizar titular
		getLblTitular().setText(accionista.getNombreCompleto());

		// 1. Cargar métricas de participación
		ResumenParticipacion resumen = gestionAcciones.obtenerResumenParticipacion(accionista.getDni());
		getLblTotalAcciones().setText(String.valueOf(resumen.getTotalAccionesPropias()));
		getLblTotalClub().setText(String.valueOf(resumen.getTotalAccionesClub()));
		getLblParticipacion().setText(String.format(Locale.GERMAN, "%.2f %%", resumen.getPorcentajeParticipacion()));

		// 2. Cargar tabla de acciones en propiedad ordenadas por fecha de adquisición
		List<Accion> acciones = gestionAcciones.obtenerAccionesAccionista(accionista.getDni());
		modeloAcciones.setRowCount(0);
		for (Accion a : acciones) {
			String strPrecioPagado = String.format(Locale.GERMAN, "%.2f €", a.getPrecioPagado());
			String strEnVenta = a.isEnVenta() ? "Sí" : "No";
			String strPrecioVenta = (a.getPrecioVenta() != null)
					? String.format(Locale.GERMAN, "%.2f €", a.getPrecioVenta())
					: "-";

			modeloAcciones.addRow(new Object[] {
					String.format("%03d", a.getIdAccion()),
					a.getFechaAdquisicion(),
					a.getOrigen(),
					strPrecioPagado,
					strEnVenta,
					strPrecioVenta
			});
		}
	}

	private void actualizarEstadoBotonVender() {
		int fila = getTablaAcciones().getSelectedRow();
		if (fila == -1) {
			getBtnVender().setEnabled(false);
			return;
		}
		Object enVenta = modeloAcciones.getValueAt(fila, 4);
		getBtnVender().setEnabled("No".equalsIgnoreCase(String.valueOf(enVenta)));
	}

	private void ejecutarVentaAccion() {
		int fila = getTablaAcciones().getSelectedRow();
		if (fila == -1) {
			return;
		}
		Object enVenta = modeloAcciones.getValueAt(fila, 4);
		if (!"No".equalsIgnoreCase(String.valueOf(enVenta))) {
			return;
		}

		String strIdAccion = String.valueOf(modeloAcciones.getValueAt(fila, 0));
		int idAccion = Integer.parseInt(strIdAccion);

		DialogoVentaAccion dialogo = new DialogoVentaAccion(this);
		dialogo.setVisible(true);

		if (dialogo.isConfirmado()) {
			try {
				gestionAcciones.ponerAccionEnVenta(idAccion, dialogo.getPrecioVenta());
			} catch (SQLException ex) {
				throw new UnexpectedException(ex);
			}
			recargarDatosAccionista(idAccion);
		}
	}

	private void recargarDatosAccionista(int idAccionSeleccionar) {
		if (accionistaActual == null) {
			return;
		}
		try {
			cargarDatosAccionista(accionistaActual);
			for (int i = 0; i < modeloAcciones.getRowCount(); i++) {
				if (Integer.parseInt(String.valueOf(modeloAcciones.getValueAt(i, 0))) == idAccionSeleccionar) {
					getTablaAcciones().setRowSelectionInterval(i, i);
					break;
				}
			}
			actualizarEstadoBotonVender();
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private void cerrarVentana() {
		dispose();
		if (vp != null) {
			vp.setVisible(true);
		}
	}

	// ==================== COMPONENTES VISUALES ====================

	private JLabel getLblDniAccionistaTexto() {
		if (lblDniAccionistaTexto == null) {
			lblDniAccionistaTexto = new JLabel("DNI :");
			lblDniAccionistaTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblDniAccionistaTexto;
	}

	private JLabel getLblDniAccionista() {
		if (lblDniAccionista == null) {
			lblDniAccionista = new JLabel("-");
			lblDniAccionista.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblDniAccionista;
	}

	private JLabel getLblTitularTexto() {
		if (lblTitularTexto == null) {
			lblTitularTexto = new JLabel("Titular :");
			lblTitularTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblTitularTexto;
	}

	private JLabel getLblTitular() {
		if (lblTitular == null) {
			lblTitular = new JLabel("-");
			lblTitular.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblTitular.setForeground(new Color(0, 102, 204));
		}
		return lblTitular;
	}

	private JLabel getLblAccionesPropiedad() {
		if (lblAccionesPropiedad == null) {
			lblAccionesPropiedad = new JLabel("Acciones en propiedad :");
			lblAccionesPropiedad.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblAccionesPropiedad;
	}

	private JTable getTablaAcciones() {
		if (tablaAcciones == null) {
			tablaAcciones = new JTable();
			tablaAcciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
			lblTotalClubTexto = new JLabel("Total club :");
			lblTotalClubTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblTotalClubTexto;
	}

	private JLabel getLblTotalClub() {
		if (lblTotalClub == null) {
			lblTotalClub = new JLabel("0");
			lblTotalClub.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblTotalClub;
	}

	private JLabel getLblParticipacionTexto() {
		if (lblParticipacionTexto == null) {
			lblParticipacionTexto = new JLabel("Participación :");
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

	private JButton getBtnVender() {
		if (btnVender == null) {
			btnVender = new JButton("Vender");
			btnVender.setPreferredSize(new Dimension(100, 30));
			btnVender.setEnabled(false);
		}
		return btnVender;
	}

	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.setPreferredSize(new Dimension(110, 30));
		}
		return btnAtras;
	}
}