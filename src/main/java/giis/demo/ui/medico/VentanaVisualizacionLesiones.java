package giis.demo.ui.medico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.medico.GestionLesiones;
import giis.demo.model.medico.Lesion;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.ApplicationException;

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
	private List<Lesion> lesiones;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private GestionLesiones gl;
	private VentanaPrincipal vp;
	private CerrarVentana cv;
	
	private EmpleadoDeportivo[] todosJugadores;
	/**
	 * Create the frame.
	 */
	public VentanaVisualizacionLesiones(VentanaPrincipal vprin) {
		
		gl = new GestionLesiones();
		vp = vprin;
		cv = new CerrarVentana();
		
		setTitle("Portal Médico : Ver Lesiones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 841, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelUser(), BorderLayout.NORTH);
		contentPane.add(getPanel_1(), BorderLayout.CENTER);
		contentPane.add(getPanel_1_1(), BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		this.addWindowListener(cv);
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
			todosJugadores = gl.getJugadores();

			cbJugador = new JComboBox<EmpleadoDeportivo>(new DefaultComboBoxModel<EmpleadoDeportivo>(todosJugadores));
			cbJugador.setEditable(true);
			cbJugador.setToolTipText("Seleccione un jugador o escriba para buscar");
			cbJugador.setBounds(15, 52, 507, 28);

			// Cómo se ve cada jugador en la lista desplegable
			cbJugador.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					if (value instanceof EmpleadoDeportivo) {
						value = nombreCompleto((EmpleadoDeportivo) value);
					}
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});

			// Cómo se ve el jugador elegido en la caja de texto
			cbJugador.setEditor(new BasicComboBoxEditor() {
				@Override
				public void setItem(Object item) {
					if (item instanceof EmpleadoDeportivo) {
						item = nombreCompleto((EmpleadoDeportivo) item);
					}
					super.setItem(item);
				}
			});

			// Filtrar mientras se escribe
			JTextField campo = (JTextField) cbJugador.getEditor().getEditorComponent();
			campo.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					int k = e.getKeyCode();
					if (k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN || k == KeyEvent.VK_ENTER
							|| k == KeyEvent.VK_ESCAPE || k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) {
						return;
					}
					filtrarJugadores(campo);
				}
			});

			cbJugador.setSelectedItem(null); // empieza vacío
		}
		return cbJugador;
	}
	
	private String nombreCompleto(EmpleadoDeportivo j) {
		return j.getNombre() + " " + j.getApellido();
	}

	private String normalizar(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
	}

	private void filtrarJugadores(JTextField campo) {
		String texto = campo.getText();
		int cursor = campo.getCaretPosition();
		String buscado = normalizar(texto.trim());

		DefaultComboBoxModel<EmpleadoDeportivo> nuevo = new DefaultComboBoxModel<>();
		for (EmpleadoDeportivo j : todosJugadores) {
			if (normalizar(nombreCompleto(j)).contains(buscado)) {
				nuevo.addElement(j);
			}
		}
		nuevo.setSelectedItem(null);

		cbJugador.setModel(nuevo);
		campo.setText(texto);
		campo.setCaretPosition(Math.min(cursor, texto.length()));

		if (nuevo.getSize() > 0) {
			cbJugador.showPopup();
		} else {
			cbJugador.hidePopup();
		}
	}

	private EmpleadoDeportivo getJugadorSeleccionado() {
		
		Object sel = cbJugador.getSelectedItem();
		if (sel instanceof EmpleadoDeportivo) {
			return (EmpleadoDeportivo) sel;
		}
		if (sel instanceof String) {
			String texto = normalizar(((String) sel).trim());
			for (EmpleadoDeportivo j : todosJugadores) {
				if (normalizar(nombreCompleto(j)).equals(texto)) {
					return j;
				}
			}
		}
		return null;
	}
	
	private JButton getBtConsultar() {
		
		if (btConsultar == null) {
			btConsultar = new JButton("Consultar");
			btConsultar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					consultar();
				}
			});
			btConsultar.setMnemonic('N');
			btConsultar.setBackground(new Color(128, 255, 128));
			btConsultar.setBounds(685, 52, 115, 29);
		}
		return btConsultar;
	}
	private JPanel getPanel_1() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
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
			btVer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					verEvolucion();
				}
			});
			btVer.setEnabled(false);
			btVer.setBackground(new Color(128, 255, 128));
			btVer.setMnemonic('V');
		}
		return btVer;
	}
	private JButton getBtCerrar() {
		if (btCerrar == null) {
			btCerrar = new JButton("Cerrar");
			btCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					volverPrincipal();
				}
			});
			btCerrar.setBackground(new Color(255, 128, 128));
			btCerrar.setMnemonic('C');
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
	
	private void volverPrincipal() {
		
		VentanaVisualizacionLesiones.this.vp.setVisible(true);
		
		this.dispose();
	}
	
	private void consultar() {
		
		
		modelo.setRowCount(0);
		btVer.setEnabled(true);
		
		EmpleadoDeportivo jugador = getJugadorSeleccionado();
		
		if(jugador == null) {
			
			getLbLesionesJugador().setText("");
			
			JOptionPane.showMessageDialog(null, "Debes seleccionar un jugador primero", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		getLbLesionesJugador().setText("Lesiones de " + jugador.getNombre() + jugador.getApellido() + ":");
		
		try {
			
			lesiones = gl.getLesiones(jugador);
			
		} catch (ApplicationException ape){
			
			JOptionPane.showMessageDialog(null, "Se ha producido un error al buscar las lesiones del jugador",
					"Error", JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		for(Lesion lesion: lesiones) {
			modelo.addRow(new Object[] {
					
					lesion.getFecha().format(formatter),
					lesion.getTipo(),
					lesion.getOrigen(),
					lesion.getEstado()
			});
		}
	}
	
	private void verEvolucion() {
		
		int fila = tableLesiones.getSelectedRow();
		
		if(fila<0) {
			
			JOptionPane.showMessageDialog(null, "Debes seleccionar una lesion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		VentanaEvolucionLesiones ventana = new VentanaEvolucionLesiones(this, lesiones.get(fila));
		
		this.setVisible(false);
		ventana.setVisible(true);
	}
	
	GestionLesiones getGestionLesiones() {
		return gl;
	}
	
	private class CerrarVentana extends WindowAdapter{
		
		@Override
		public void windowClosed(WindowEvent we) {
			volverPrincipal();
		}
	}
	
	public DateTimeFormatter getFormatter() {
		return formatter;
	}

}
