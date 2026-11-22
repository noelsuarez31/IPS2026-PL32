package giis.demo.ui.medico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.medico.GestionLesiones;
import giis.demo.model.medico.Lesion;
import giis.demo.ui.login.VentanaPrincipal;

public class VentanaVisualizacionLesiones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelUser;
	private JLabel lbJugador;
	private JComboBox<EmpleadoDeportivo> cbJugador;
	private JButton btConsultar;
	private JPanel panel;
	private JPanel panelOpciones;
	private JPanel panelBotones;
	private JButton btVer;
	private JButton btCerrar;
	private JTable tableLesiones;
	private JLabel lbLesionesJugador;
	
	private DefaultTableModel modelo;
	private VentanaPrincipal vp;
	private List<Lesion> lesiones;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private GestionLesiones gl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaVisualizacionLesiones frame = new VentanaVisualizacionLesiones();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaVisualizacionLesiones() {
		
		gl = new GestionLesiones();
		
		setTitle("Portal Médico : Ver Lesiones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 841, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelUser(), BorderLayout.NORTH);
		contentPane.add(getPanel_1(), BorderLayout.CENTER);
		contentPane.add(getPanel_1_1(), BorderLayout.SOUTH);

	}

	private JPanel getPanelUser() {
		if (panelUser == null) {
			panelUser = new JPanel();
			panelUser.setPreferredSize(new Dimension(0, 120));
			panelUser.setLayout(null);
			panelUser.add(getLbJugador());
			panelUser.add(getCbJugador());
			panelUser.add(getBtConsultar());
		}
		return panelUser;
	}
	private JLabel getLbJugador() {
		if (lbJugador == null) {
			lbJugador = new JLabel("Jugador:");
			lbJugador.setBounds(15, 16, 69, 20);
		}
		return lbJugador;
	}
	private JComboBox<EmpleadoDeportivo> getCbJugador() {
		if (cbJugador == null) {
			cbJugador = new JComboBox<EmpleadoDeportivo>();
			cbJugador.setModel(new DefaultComboBoxModel<EmpleadoDeportivo>(gl.getJugadores()));
			cbJugador.setToolTipText("Seleccione un jugador");
			cbJugador.setBounds(15, 52, 507, 28);
		}
		return cbJugador;
	}
	private JButton getBtConsultar() {
		if (btConsultar == null) {
			btConsultar = new JButton("Consultar");
			btConsultar.setBackground(new Color(128, 255, 128));
			btConsultar.setBounds(685, 52, 115, 29);
		}
		return btConsultar;
	}
	private JPanel getPanel_1() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getTableLesiones(), BorderLayout.CENTER);
			panel.add(getLbLesionesJugador(), BorderLayout.NORTH);
			panel.add(new JScrollPane(getTableLesiones()), BorderLayout.CENTER);
		}
		return panel;
	}
	private JPanel getPanel_1_1() {
		if (panelOpciones == null) {
			panelOpciones = new JPanel();
			panelOpciones.setLayout(new BorderLayout(0, 0));
			panelOpciones.add(getPanel_1_2(), BorderLayout.EAST);
		}
		return panelOpciones;
	}
	private JPanel getPanel_1_2() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new GridLayout(1, 2, 0, 0));
			panelBotones.add(getBtVer());
			panelBotones.add(getBtCerrar());
		}
		return panelBotones;
	}
	private JButton getBtVer() {
		if (btVer == null) {
			btVer = new JButton("Ver");
			btVer.setBackground(new Color(128, 255, 128));
			btVer.setMnemonic('V');
		}
		return btVer;
	}
	private JButton getBtCerrar() {
		if (btCerrar == null) {
			btCerrar = new JButton("Cerrar");
			btCerrar.setBackground(new Color(255, 128, 128));
			btCerrar.setMnemonic('R');
		}
		return btCerrar;
	}
	
	private JTable getTableLesiones() {
				
		if (tableLesiones == null) {
			modelo = new DefaultTableModel(new String[] { "Fecha", "Lesión", "Origen", "Estado" }, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			};
			tableLesiones = new JTable(modelo);

			tableLesiones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableLesiones.setRowSelectionAllowed(true);
			tableLesiones.setColumnSelectionAllowed(false);
			tableLesiones.getTableHeader().setReorderingAllowed(false);

			tableLesiones.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					getBtVer().setEnabled(tableLesiones.getSelectedRow() >= 0);
				}
			});
		}
		return tableLesiones;
	}
	
	private JLabel getLbLesionesJugador() {
		if (lbLesionesJugador == null) {
			lbLesionesJugador = new JLabel("Lesiones de");
		}
		return lbLesionesJugador;
	}
}
