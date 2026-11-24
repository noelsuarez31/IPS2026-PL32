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
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
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
import javax.swing.table.DefaultTableModel;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.equipo.Equipo;
import giis.demo.model.medico.GestionLesiones;
import giis.demo.model.medico.Lesion;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.ApplicationException;

public class VentanaVisualizacionLesiones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelUser;

	// Filtro de Equipo
	private JLabel lbEquipo;
	private JComboBox<Equipo> cbEquipo;

	// Filtro y Lista de Jugadores
	private JLabel lbJugador;
	private JTextField txFiltroJugador;
	private JList<EmpleadoDeportivo> listJugadores;
	private DefaultListModel<EmpleadoDeportivo> modeloListaJugadores;
	private JScrollPane scrollListaJugadores;
	private JButton btConsultar;

	// Zona Centro: Tabla
	private JPanel panel;
	private JLabel lbLesionesJugador;
	private JTable tableLesiones;
	private DefaultTableModel modelo;

	// Zona Sur: Botones
	private JPanel panelOpciones;
	private JPanel panelBotones;
	private JButton btVer;
	private JButton btCerrar;

	private List<Lesion> lesiones;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private GestionLesiones gl;
	private VentanaPrincipal vp;
	private CerrarVentana cv;

	private EmpleadoDeportivo[] todosJugadores;
	private EmpleadoDeportivo[] jugadoresDelEquipo;
	private JButton btActualizar;

	/**
	 * Create the frame.
	 */
	public VentanaVisualizacionLesiones(VentanaPrincipal vprin) {
		gl = new GestionLesiones();
		vp = vprin;
		cv = new CerrarVentana();

		setTitle("Portal Médico : Ver Lesiones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 841, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelUser(), BorderLayout.NORTH);
		contentPane.add(getPanel_1(), BorderLayout.CENTER);
		contentPane.add(getPanel_1_1(), BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		this.addWindowListener(cv);

		cargarDatosIniciales();
	}

	private JPanel getPanelUser() {
		if (panelUser == null) {
			panelUser = new JPanel();
			panelUser.setPreferredSize(new Dimension(0, 150));
			panelUser.setLayout(null);

			panelUser.add(getLbEquipo());
			panelUser.add(getCbEquipo());

			panelUser.add(getLbJugador());
			panelUser.add(getTxFiltroJugador());
			panelUser.add(getScrollListaJugadores());

			panelUser.add(getBtConsultar());
		}
		return panelUser;
	}

	// ==================== ZONA EQUIPO ====================

	private JLabel getLbEquipo() {
		if (lbEquipo == null) {
			lbEquipo = new JLabel("Equipo:");
			lbEquipo.setBounds(15, 16, 69, 20);
		}
		return lbEquipo;
	}

	private JComboBox<Equipo> getCbEquipo() {
		
		if (cbEquipo == null) {

			DefaultComboBoxModel<Equipo> modelEquipo = new DefaultComboBoxModel<>();
			Equipo opcionTodos = new Equipo("-- Todos los equipos --", null, null);
			opcionTodos.setIdEquipo(-1);
			modelEquipo.addElement(opcionTodos);
			Equipo[] equipos = gl.getEquipos();
			if (equipos != null) {
				for (Equipo eq : equipos) {
					modelEquipo.addElement(eq);
				}
			}

			cbEquipo = new JComboBox<>(modelEquipo);
			cbEquipo.setBounds(85, 12, 450, 28);

			cbEquipo.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					if (value instanceof Equipo) {
						value = ((Equipo) value).getNombre();
					}
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});

			cbEquipo.addItemListener(e -> {
				if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
					filtrarPorEquipo();
				}
			});
		}
		return cbEquipo;
	}
	// ==================== ZONA JUGADOR (LISTA FILTRABLE) ====================

	private JLabel getLbJugador() {
		if (lbJugador == null) {
			lbJugador = new JLabel("Jugador:");
			lbJugador.setBounds(15, 52, 69, 20);
		}
		return lbJugador;
	}

	private JTextField getTxFiltroJugador() {
		if (txFiltroJugador == null) {
			txFiltroJugador = new JTextField();
			txFiltroJugador.setToolTipText("Escriba para filtrar jugadores");
			txFiltroJugador.setBounds(85, 48, 450, 26);

			// Filtra la lista en tiempo real mientras escribe
			txFiltroJugador.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					filtrarListaPorTexto(txFiltroJugador.getText());
				}
			});
		}
		return txFiltroJugador;
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
			btConsultar.setBounds(685, 48, 115, 29);
		}
		return btConsultar;
	}

	// ==================== LÓGICA DE FILTRADO ====================

	private void cargarDatosIniciales() {
		
		todosJugadores = gl.getJugadores();
		jugadoresDelEquipo = todosJugadores;

		filtrarPorEquipo();
	}

	private void filtrarPorEquipo() {
		
		Equipo equipoSel = (Equipo)getCbEquipo().getSelectedItem();
		
		if (equipoSel == null) {
			return;
		}

		if (equipoSel.getIdEquipo() < 0) {
			
			jugadoresDelEquipo = todosJugadores;
			
		} else {
			
			List<EmpleadoDeportivo> filtrados = new ArrayList<>();
			
			if (todosJugadores != null) {
				for (EmpleadoDeportivo j : todosJugadores) {

					if (j.getIdEquipo() == equipoSel.getIdEquipo()) {
						filtrados.add(j);
					}
				}
			}
			jugadoresDelEquipo = filtrados.toArray(new EmpleadoDeportivo[0]);
		}

		getTxFiltroJugador().setText("");
		filtrarListaPorTexto("");

		if (modelo != null) {
			modelo.setRowCount(0);
		}
		getLbLesionesJugador().setText("Lesiones de");
		getBtVer().setEnabled(false);
		getBtActualizar().setEnabled(false);
	}

	
	private void filtrarListaPorTexto(String texto) {
		
		String buscado = normalizar(texto == null ? "" : texto.trim());
		
		DefaultListModel<EmpleadoDeportivo> nuevoModelo = new DefaultListModel<>();
		
		EmpleadoDeportivo[] base = (jugadoresDelEquipo != null) ? jugadoresDelEquipo : todosJugadores;
		
		if (base != null) {
			
			for (EmpleadoDeportivo j : base) {
				
				if (normalizar(nombreCompleto(j)).contains(buscado)) {
					nuevoModelo.addElement(j);
				}
			}
		}
		
		getListJugadores().setModel(nuevoModelo);
		
		getListJugadores().revalidate();
		getListJugadores().repaint();
	}

	private DefaultListModel<EmpleadoDeportivo> getModeloListaJugadores() {
		if (modeloListaJugadores == null) {
			modeloListaJugadores = new DefaultListModel<>();
		}
		return modeloListaJugadores;
	}

	private JList<EmpleadoDeportivo> getListJugadores() {
		if (listJugadores == null) {
			listJugadores = new JList<>(getModeloListaJugadores());
			listJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listJugadores.setCellRenderer(new DefaultListCellRenderer() {
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
		}
		return listJugadores;
	}

	private JScrollPane getScrollListaJugadores() {
		if (scrollListaJugadores == null) {
			scrollListaJugadores = new JScrollPane(getListJugadores());
			scrollListaJugadores.setBounds(85, 76, 450, 65);
		}
		return scrollListaJugadores;
	}

	private String nombreCompleto(EmpleadoDeportivo j) {
		return j.getNombre() + " " + j.getApellido();
	}

	private String normalizar(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
	}

	private EmpleadoDeportivo getJugadorSeleccionado() {
		return listJugadores.getSelectedValue();
	}

	// ==================== ZONA CENTRO Y SUR ====================

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
			panelBotones.setLayout(new GridLayout(0, 3, 4, 0));
			panelBotones.add(getBtActualizar());
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
					getBtActualizar().setEnabled(tableLesiones.getSelectedRow() >= 0);
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
		btVer.setEnabled(false);

		EmpleadoDeportivo jugador = getJugadorSeleccionado();

		if (jugador == null) {
			getLbLesionesJugador().setText("");
			JOptionPane.showMessageDialog(null, "Debes seleccionar un jugador de la lista primero", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		getLbLesionesJugador().setText("Lesiones de " + jugador.getNombre() + " " + jugador.getApellido() + ":");

		try {
			lesiones = gl.getLesiones(jugador);
		} catch (ApplicationException ape) {
			JOptionPane.showMessageDialog(null, "Se ha producido un error al buscar las lesiones del jugador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (lesiones != null) {
			for (Lesion lesion : lesiones) {
				modelo.addRow(new Object[] { lesion.getFecha().format(formatter), lesion.getTipo(), lesion.getOrigen(),
						lesion.getEstado() });
			}
		}
	}

	private void verEvolucion() {
		int fila = tableLesiones.getSelectedRow();
		if (fila < 0 || lesiones == null || fila >= lesiones.size()) {
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

	private class CerrarVentana extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent we) {
			volverPrincipal();
		}
	}

	public DateTimeFormatter getFormatter() {
		return formatter;
	}
	private JButton getBtActualizar() {
		if (btActualizar == null) {
			btActualizar = new JButton("Actualizar");
			btActualizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaregistrarEvolucion();
				}
			});
			btActualizar.setMnemonic('A');
			btActualizar.setEnabled(false);
			btActualizar.setBackground(new Color(128, 255, 128));
		}
		return btActualizar;
	}
	
	private void abrirVentanaregistrarEvolucion() {
		
		int fila = tableLesiones.getSelectedRow();
		if (fila < 0 || lesiones == null || fila >= lesiones.size()) {
			JOptionPane.showMessageDialog(null, "Debes seleccionar una lesion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Lesion lesion = lesiones.get(fila);
		
		VentanaRegistrarEvolucion ventanaRegistro = new VentanaRegistrarEvolucion(this,lesion);
		ventanaRegistro.setVisible(true);
		
	}
}