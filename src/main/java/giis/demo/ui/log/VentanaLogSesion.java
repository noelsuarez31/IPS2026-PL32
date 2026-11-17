package giis.demo.ui.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.log.EventoLog;
import giis.demo.model.log.GestionLogSesion;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.SwingUtil;

/**
 * Ventana para que el gerente consulte el archivo de log de la sesión actual,
 * permitiendo filtrar por tipo de evento (modificaciones BBDD o acciones de usuario)
 * y por usuario, así como recargar el contenido desde disco.
 */
public class VentanaLogSesion extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vp;
	private GestionLogSesion gestionLogSesion;

	// Zona Norte: Archivo de sesión y desplegables de filtro
	private JLabel lblArchivoSesion;
	private JTextField txtArchivoSesion;
	private JLabel lblFiltrar;
	private JComboBox<String> cbFiltrar;
	private JLabel lblUsuario;
	private JComboBox<String> cbUsuario;

	// Zona Centro: Tabla de eventos de la sesión actual
	private JLabel lblRegistroEventos;
	private JTable tablaEventos;
	private DefaultTableModel modeloEventos;
	private JScrollPane scrollPaneEventos;

	// Zona Sur: Contadores resumen y botones Actualizar / Atrás
	private JLabel lblEventosTexto;
	private JLabel lblEventosValor;
	private JLabel lblModificacionesTexto;
	private JLabel lblModificacionesValor;
	private JButton btnActualizar;
	private JButton btnAtras;

	private List<EventoLog> eventosCargados = new ArrayList<>();
	private boolean actualizandoCombos = false;

	/**
	 * Constructor para integración con VentanaPrincipal.
	 */
	public VentanaLogSesion(VentanaPrincipal vp) {
		this.vp = vp;
		this.gestionLogSesion = new GestionLogSesion();
		inicializarComponentes();
	}

	/**
	 * Constructor por defecto para pruebas o WindowBuilder.
	 */
	public VentanaLogSesion() {
		this.gestionLogSesion = new GestionLogSesion();
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Consultar log de la sesión");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 940, 500);
		setMinimumSize(new Dimension(780, 420));

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
		setContentPane(contentPane);

		// 1. ZONA NORTE: Nombre del archivo de sesión y filtros a la derecha
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panelNorte.add(getLblArchivoSesion());
		panelNorte.add(getTxtArchivoSesion());
		panelNorte.add(getLblFiltrar());
		panelNorte.add(getCbFiltrar());
		panelNorte.add(getLblUsuario());
		panelNorte.add(getCbUsuario());
		contentPane.add(panelNorte, BorderLayout.NORTH);

		// 2. ZONA CENTRO: Título de la tabla y listado de eventos
		JPanel panelCentro = new JPanel(new BorderLayout(0, 5));
		panelCentro.add(getLblRegistroEventos(), BorderLayout.NORTH);
		panelCentro.add(getScrollPaneEventos(), BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);

		// 3. ZONA SUR: Contadores a la izquierda y botones Actualizar / Atrás a la derecha
		JPanel panelSur = new JPanel(new BorderLayout());

		JPanel panelContadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		panelContadores.add(getLblEventosTexto());
		panelContadores.add(getLblEventosValor());
		panelContadores.add(getLblModificacionesTexto());
		panelContadores.add(getLblModificacionesValor());

		JPanel panelBotonesSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		panelBotonesSur.add(getBtnActualizar());
		panelBotonesSur.add(getBtnAtras());

		panelSur.add(panelContadores, BorderLayout.WEST);
		panelSur.add(panelBotonesSur, BorderLayout.EAST);
		contentPane.add(panelSur, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		inicializarTablaYEventos();
	}

	private void inicializarTablaYEventos() {
		modeloEventos = new DefaultTableModel(
				new Object[] { "Hora", "Usuario", "Rol", "Tipo", "Detalle" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaEventos().setModel(modeloEventos);
		configurarAnchosColumnas();

		getCbFiltrar().addActionListener(e -> {
			if (!actualizandoCombos) {
				SwingUtil.exceptionWrapper(() -> aplicarFiltrosYActualizarVista());
			}
		});

		getCbUsuario().addActionListener(e -> {
			if (!actualizandoCombos) {
				SwingUtil.exceptionWrapper(() -> aplicarFiltrosYActualizarVista());
			}
		});

		getBtnActualizar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> actualizarDesdeArchivo()));

		getBtnAtras().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrarVentana();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarVentana();
			}
		});

		actualizarDesdeArchivo();
	}

	private void configurarAnchosColumnas() {
		if (getTablaEventos().getColumnModel().getColumnCount() == 5) {
			getTablaEventos().getColumnModel().getColumn(0).setPreferredWidth(85);
			getTablaEventos().getColumnModel().getColumn(1).setPreferredWidth(130);
			getTablaEventos().getColumnModel().getColumn(2).setPreferredWidth(160);
			getTablaEventos().getColumnModel().getColumn(3).setPreferredWidth(110);
			getTablaEventos().getColumnModel().getColumn(4).setPreferredWidth(390);
		}
	}

	/**
	 * Vuelve a leer el archivo .log de la sesión actual desde disco, actualiza el selector
	 * de usuarios manteniendo la selección previa si sigue existiendo y recarga la tabla.
	 */
	public void actualizarDesdeArchivo() {
		getTxtArchivoSesion().setText(gestionLogSesion.getRutaArchivoSesionActual());
		eventosCargados = gestionLogSesion.leerEventosSesionActual();
		actualizarComboUsuarios();
		aplicarFiltrosYActualizarVista();
	}

	private void actualizarComboUsuarios() {
		actualizandoCombos = true;
		try {
			Object seleccionPrevia = getCbUsuario().getSelectedItem();
			String usuarioPrevio = (seleccionPrevia != null) ? seleccionPrevia.toString() : GestionLogSesion.FILTRO_TODOS;

			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
			model.addElement(GestionLogSesion.FILTRO_TODOS);

			List<String> usuarios = gestionLogSesion.obtenerUsuariosSesion(eventosCargados);
			for (String u : usuarios) {
				model.addElement(u);
			}

			getCbUsuario().setModel(model);
			if (usuarios.contains(usuarioPrevio) || GestionLogSesion.FILTRO_TODOS.equals(usuarioPrevio)) {
				getCbUsuario().setSelectedItem(usuarioPrevio);
			} else {
				getCbUsuario().setSelectedItem(GestionLogSesion.FILTRO_TODOS);
			}
		} finally {
			actualizandoCombos = false;
		}
	}

	private void aplicarFiltrosYActualizarVista() {
		String filtroTipo = String.valueOf(getCbFiltrar().getSelectedItem());
		String filtroUsuario = String.valueOf(getCbUsuario().getSelectedItem());

		List<EventoLog> filtrados = gestionLogSesion.filtrarEventos(eventosCargados, filtroTipo, filtroUsuario);

		modeloEventos.setRowCount(0);
		for (EventoLog evento : filtrados) {
			modeloEventos.addRow(new Object[] {
					evento.getHora(),
					evento.getUsuario(),
					evento.getRol(),
					evento.getTipo(),
					evento.getDetalle()
			});
		}

		getLblEventosValor().setText(String.valueOf(filtrados.size()));
		getLblModificacionesValor().setText(String.valueOf(gestionLogSesion.contarModificacionesBBDD(filtrados)));
	}

	private void cerrarVentana() {
		dispose();
		if (vp != null) {
			vp.setVisible(true);
		}
	}

	// ==================== GETTERS COMPONENTES VISUALES ====================

	private JLabel getLblArchivoSesion() {
		if (lblArchivoSesion == null) {
			lblArchivoSesion = new JLabel("Archivo sesión:");
			lblArchivoSesion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblArchivoSesion;
	}

	private JTextField getTxtArchivoSesion() {
		if (txtArchivoSesion == null) {
			txtArchivoSesion = new JTextField();
			txtArchivoSesion.setEditable(false);
			txtArchivoSesion.setPreferredSize(new Dimension(240, 26));
		}
		return txtArchivoSesion;
	}

	private JLabel getLblFiltrar() {
		if (lblFiltrar == null) {
			lblFiltrar = new JLabel("Filtrar:");
			lblFiltrar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblFiltrar;
	}

	public JComboBox<String> getCbFiltrar() {
		if (cbFiltrar == null) {
			cbFiltrar = new JComboBox<>();
			cbFiltrar.setModel(new DefaultComboBoxModel<>(new String[] {
					GestionLogSesion.FILTRO_TODOS,
					GestionLogSesion.FILTRO_SOLO_BBDD,
					GestionLogSesion.FILTRO_SOLO_ACCIONES
			}));
			cbFiltrar.setPreferredSize(new Dimension(180, 26));
		}
		return cbFiltrar;
	}

	private JLabel getLblUsuario() {
		if (lblUsuario == null) {
			lblUsuario = new JLabel("Usuario:");
			lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblUsuario;
	}

	public JComboBox<String> getCbUsuario() {
		if (cbUsuario == null) {
			cbUsuario = new JComboBox<>();
			cbUsuario.setModel(new DefaultComboBoxModel<>(new String[] { GestionLogSesion.FILTRO_TODOS }));
			cbUsuario.setPreferredSize(new Dimension(145, 26));
		}
		return cbUsuario;
	}

	private JLabel getLblRegistroEventos() {
		if (lblRegistroEventos == null) {
			lblRegistroEventos = new JLabel("Registro de eventos sesión actual:");
			lblRegistroEventos.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblRegistroEventos;
	}

	public JTable getTablaEventos() {
		if (tablaEventos == null) {
			tablaEventos = new JTable();
			tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tablaEventos;
	}

	private JScrollPane getScrollPaneEventos() {
		if (scrollPaneEventos == null) {
			scrollPaneEventos = new JScrollPane(getTablaEventos());
		}
		return scrollPaneEventos;
	}

	private JLabel getLblEventosTexto() {
		if (lblEventosTexto == null) {
			lblEventosTexto = new JLabel("Eventos:");
			lblEventosTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblEventosTexto;
	}

	public JLabel getLblEventosValor() {
		if (lblEventosValor == null) {
			lblEventosValor = new JLabel("0");
			lblEventosValor.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblEventosValor.setForeground(new Color(0, 102, 204));
		}
		return lblEventosValor;
	}

	private JLabel getLblModificacionesTexto() {
		if (lblModificacionesTexto == null) {
			lblModificacionesTexto = new JLabel("Modificaciones BBDD:");
			lblModificacionesTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblModificacionesTexto;
	}

	public JLabel getLblModificacionesValor() {
		if (lblModificacionesValor == null) {
			lblModificacionesValor = new JLabel("0");
			lblModificacionesValor.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblModificacionesValor.setForeground(new Color(0, 102, 204));
		}
		return lblModificacionesValor;
	}

	public JButton getBtnActualizar() {
		if (btnActualizar == null) {
			btnActualizar = new JButton("Actualizar");
			btnActualizar.setPreferredSize(new Dimension(110, 30));
		}
		return btnActualizar;
	}

	public JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.setPreferredSize(new Dimension(110, 30));
		}
		return btnAtras;
	}
}
