package giis.demo.ui.medico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.equipo.Equipo;
import giis.demo.model.medico.GestionLesiones;
import giis.demo.model.medico.OrigenLesion;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.ApplicationException;

public class VentanaRegistrarLesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private EmpleadoDeportivo[] todosJugadores;
	private EmpleadoDeportivo[] jugadoresDelEquipo;
	private DefaultListModel<EmpleadoDeportivo> modeloListaJugadores;
	private DefaultListModel<String> modeloEventos;

	private JPanel panelNorte;
	private JPanel panelBuscar;
	private JLabel lbEquipo;
	private JComboBox<Equipo> cbEquipo;
	private JLabel lbJugador;
	private JTextField txBuscarJugador;
	private JScrollPane scrollListaJugadores;
	private JList<EmpleadoDeportivo> listaJugadores;

	private JPanel panelOrigenFecha;
	private JLabel lbOrigen;
	private JRadioButton rbPartido;
	private JRadioButton rbEntrenamiento;
	private JRadioButton rbOtro;
	private final ButtonGroup bgOrigen = new ButtonGroup();
	private JLabel lbFecha;
	private JDateChooser dcFecha;
	private JButton btConsultarEventos;

	private JPanel panelEventos;
	private JScrollPane scrollEventos;
	private JList<String> listaEventos;

	private JPanel panelSur;
	private JPanel panelDatosLesion;
	private JLabel lbTipoLesion;
	private JTextField txTipoLesion;
	private JLabel lbValoracion;
	private JComboBox<String> cbValoracion;
	private JLabel lbActualizacion;
	private JTextField txActualizacion;
	private JPanel panelBotones;
	private JButton btGuardar;
	private JButton btCancelar;
	private GestionLesiones gl;
	private VentanaPrincipal vp;

	/**
	 * Constructor principal.
	 */
	public VentanaRegistrarLesion(VentanaPrincipal vPrin) {
		this.vp = vPrin;
		this.gl = new GestionLesiones();
		setTitle("Portal Médico : Registrar Lesión");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 860, 680);
		setMinimumSize(new Dimension(800, 620));
		setLocationRelativeTo(null);
		contentPane = new JPanel(new BorderLayout(5, 5));
		contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
		setContentPane(contentPane);
		contentPane.add(getPanelNorte(), BorderLayout.NORTH);
		contentPane.add(getPanelEventos(), BorderLayout.CENTER);
		contentPane.add(getPanelSur(), BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				volverPrincipal();
			}
		});
		cargarDatosIniciales();
	}

	private JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel(new BorderLayout(0, 5));
			panelNorte.add(getPanelBuscar(), BorderLayout.CENTER);
			panelNorte.add(getPanelOrigenFecha(), BorderLayout.SOUTH);
		}
		return panelNorte;
	}

	private JPanel getPanelBuscar() {
		if (panelBuscar == null) {
			panelBuscar = new JPanel();
			panelBuscar.setLayout(null);
			panelBuscar.setPreferredSize(new Dimension(0, 175));
			panelBuscar.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Buscar Jugador:",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelBuscar.add(getLbEquipo());
			panelBuscar.add(getCbEquipo());
			panelBuscar.add(getLbJugador());
			panelBuscar.add(getTxBuscarJugador());
			panelBuscar.add(getScrollListaJugadores());
		}
		return panelBuscar;
	}

	private JLabel getLbEquipo() {
		if (lbEquipo == null) {
			lbEquipo = new JLabel("Equipo:");
			lbEquipo.setBounds(15, 20, 60, 20);
		}
		return lbEquipo;
	}

	private JComboBox<Equipo> getCbEquipo() {
		if (cbEquipo == null) {
			DefaultComboBoxModel<Equipo> modelEquipo = new DefaultComboBoxModel<>();
			Equipo opcionTodos = new Equipo("-- Todos los equipos --", null, null);
			opcionTodos.setIdEquipo(-1);
			modelEquipo.addElement(opcionTodos);
			try {
				Equipo[] equipos = gl.getEquipos();
				if (equipos != null) {
					for (Equipo eq : equipos) {
						modelEquipo.addElement(eq);
					}
				}
			} catch (Exception e) {
				// TODO
			}
			cbEquipo = new JComboBox<>(modelEquipo);
			cbEquipo.setBounds(85, 18, 420, 26);
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

	private JLabel getLbJugador() {
		if (lbJugador == null) {
			lbJugador = new JLabel("Jugador:");
			lbJugador.setBounds(15, 54, 60, 20);
		}
		return lbJugador;
	}

	private JTextField getTxBuscarJugador() {
		if (txBuscarJugador == null) {
			txBuscarJugador = new JTextField();
			txBuscarJugador.setToolTipText("Escriba para filtrar por nombre o apellidos");
			txBuscarJugador.setBounds(85, 52, 420, 24);
			txBuscarJugador.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					filtrarListaPorTexto(txBuscarJugador.getText());
				}
			});
		}
		return txBuscarJugador;
	}

	private JScrollPane getScrollListaJugadores() {
		if (scrollListaJugadores == null) {
			scrollListaJugadores = new JScrollPane();
			scrollListaJugadores.setBounds(85, 80, 420, 80);
			scrollListaJugadores.setViewportView(getListaJugadores());
		}
		return scrollListaJugadores;
	}

	private JList<EmpleadoDeportivo> getListaJugadores() {

		if (listaJugadores == null) {

			modeloListaJugadores = new DefaultListModel<>();
			listaJugadores = new JList<>(modeloListaJugadores);

			listaJugadores.addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting() && modeloEventos != null) {
					modeloEventos.clear();
				}
			});

			listaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listaJugadores.setCellRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					if (value instanceof EmpleadoDeportivo) {
						EmpleadoDeportivo ed = (EmpleadoDeportivo) value;
						value = ed.getNombre() + " " + ed.getApellido();
					}
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
		}
		return listaJugadores;
	}

	private JPanel getPanelOrigenFecha() {
		if (panelOrigenFecha == null) {
			panelOrigenFecha = new JPanel();
			panelOrigenFecha.setLayout(null);
			panelOrigenFecha.setPreferredSize(new Dimension(0, 80));
			panelOrigenFecha.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			panelOrigenFecha.add(getLbOrigen());
			panelOrigenFecha.add(getRbPartido());
			panelOrigenFecha.add(getRbEntrenamiento());
			panelOrigenFecha.add(getRbOtro());
			panelOrigenFecha.add(getLbFecha());
			panelOrigenFecha.add(getDcFecha());
			panelOrigenFecha.add(getBtConsultarEventos());
		}
		return panelOrigenFecha;
	}

	private JLabel getLbOrigen() {
		if (lbOrigen == null) {
			lbOrigen = new JLabel("Origen:");
			lbOrigen.setFont(new Font("Tahoma", Font.BOLD, 11));
			lbOrigen.setBounds(15, 14, 50, 20);
		}
		return lbOrigen;
	}

	private JRadioButton getRbPartido() {
		if (rbPartido == null) {
			rbPartido = new JRadioButton("Partido");
			rbPartido.setSelected(true);
			bgOrigen.add(rbPartido);
			rbPartido.setBounds(70, 13, 75, 23);
			rbPartido.addActionListener(e -> alternarVisibilidadEventos(true));
		}
		return rbPartido;
	}

	private JRadioButton getRbEntrenamiento() {
		if (rbEntrenamiento == null) {
			rbEntrenamiento = new JRadioButton("Entrenamiento");
			bgOrigen.add(rbEntrenamiento);
			rbEntrenamiento.setBounds(150, 13, 110, 23);
			rbEntrenamiento.addActionListener(e -> alternarVisibilidadEventos(true));
		}
		return rbEntrenamiento;
	}

	private JRadioButton getRbOtro() {
		if (rbOtro == null) {
			rbOtro = new JRadioButton("Otro");
			bgOrigen.add(rbOtro);
			rbOtro.setBounds(265, 13, 60, 23);
			rbOtro.addActionListener(e -> alternarVisibilidadEventos(false));
		}
		return rbOtro;
	}

	private JLabel getLbFecha() {
		if (lbFecha == null) {
			lbFecha = new JLabel("Fecha:");
			lbFecha.setFont(new Font("Tahoma", Font.BOLD, 11));
			lbFecha.setBounds(15, 46, 45, 20);
		}
		return lbFecha;
	}

	private JDateChooser getDcFecha() {

		if (dcFecha == null) {

			dcFecha = new JDateChooser();
			dcFecha.setBounds(65, 45, 135, 24);
			dcFecha.setDate(new Date());

			dcFecha.addPropertyChangeListener("date", e -> {
				if (modeloEventos != null) {
					modeloEventos.clear();
				}
			});
		}
		return dcFecha;
	}

	private JButton getBtConsultarEventos() {
		if (btConsultarEventos == null) {
			btConsultarEventos = new JButton("Consultar");
			btConsultarEventos.setMnemonic('C');
			btConsultarEventos.setBounds(545, 12, 100, 26);
			btConsultarEventos.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					consultarEventosDelDia();
					
				}
			});
		}
		return btConsultarEventos;
	}

	private JPanel getPanelEventos() {
		if (panelEventos == null) {
			panelEventos = new JPanel(new BorderLayout());
			panelEventos.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
					"Partidos / Entrenamientos del equipo del día (donde se originó la lesión):", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panelEventos.add(getScrollEventos(), BorderLayout.CENTER);
		}
		return panelEventos;
	}

	private JScrollPane getScrollEventos() {
		if (scrollEventos == null) {
			scrollEventos = new JScrollPane();
			scrollEventos.setViewportView(getListaEventos());
		}
		return scrollEventos;
	}

	private JList<String> getListaEventos() {
		if (listaEventos == null) {
			modeloEventos = new DefaultListModel<>();
			listaEventos = new JList<>(modeloEventos);
			listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return listaEventos;
	}

	private JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel(new BorderLayout(0, 5));
			panelSur.add(getPanelDatosLesion(), BorderLayout.CENTER);
			panelSur.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panelSur;
	}

	private JPanel getPanelDatosLesion() {
		if (panelDatosLesion == null) {
			panelDatosLesion = new JPanel();
			panelDatosLesion.setLayout(null);
			panelDatosLesion.setPreferredSize(new Dimension(520, 105));
			panelDatosLesion.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Datos de la Lesión:",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatosLesion.add(getLbTipoLesion());
			panelDatosLesion.add(getTxTipoLesion());
			panelDatosLesion.add(getLbValoracion());
			panelDatosLesion.add(getCbValoracion());
			panelDatosLesion.add(getLbActualizacion());
			panelDatosLesion.add(getTxActualizacion());
		}
		return panelDatosLesion;
	}

	private JLabel getLbTipoLesion() {
		if (lbTipoLesion == null) {
			lbTipoLesion = new JLabel("Lesión:");
			lbTipoLesion.setBounds(15, 20, 80, 20);
		}
		return lbTipoLesion;
	}

	private JTextField getTxTipoLesion() {
		if (txTipoLesion == null) {
			txTipoLesion = new JTextField();
			txTipoLesion.setBounds(105, 18, 380, 22);
		}
		return txTipoLesion;
	}

	private JLabel getLbValoracion() {
		if (lbValoracion == null) {
			lbValoracion = new JLabel("Valoración:");
			lbValoracion.setBounds(15, 48, 80, 20);
		}
		return lbValoracion;
	}

	private JComboBox<String> getCbValoracion() {
		if (cbValoracion == null) {
			cbValoracion = new JComboBox<>();
			cbValoracion.setModel(new DefaultComboBoxModel<>(new String[] { "Leve", "Moderada", "Grave" }));
			cbValoracion.setBounds(105, 46, 140, 22);
		}
		return cbValoracion;
	}

	private JLabel getLbActualizacion() {
		if (lbActualizacion == null) {
			lbActualizacion = new JLabel("Actualización:");
			lbActualizacion.setBounds(15, 76, 85, 20);
		}
		return lbActualizacion;
	}

	private JTextField getTxActualizacion() {
		if (txActualizacion == null) {
			txActualizacion = new JTextField();
			txActualizacion.setBounds(105, 74, 380, 22);
		}
		return txActualizacion;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 30));
			panelBotones.add(getBtGuardar());
			panelBotones.add(getBtCancelar());
		}
		return panelBotones;
	}

	private JButton getBtGuardar() {
		if (btGuardar == null) {
			btGuardar = new JButton("Guardar");
			btGuardar.setBackground(new Color(128, 255, 128));
			btGuardar.setMnemonic('G');
			btGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guardarLesion();
				}
			});
		}
		return btGuardar;
	}

	private void consultarEventosDelDia() {
		
		modeloEventos.clear();
		EmpleadoDeportivo jugador = getListaJugadores().getSelectedValue();
		
		if (jugador == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un jugador de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (getDcFecha().getDate() == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		LocalDate fecha = aLocalDate(getDcFecha().getDate());
		OrigenLesion origen = getOrigenSeleccionado();

		try {
			List<String> eventos = gl.getEventosEquipo(jugador, origen, fecha);
			
			if (eventos.isEmpty()) {
				
				JOptionPane.showMessageDialog(this,
						"El equipo del jugador no tiene " + (origen == OrigenLesion.PARTIDO ? "partidos" : "entrenamientos") + " ese día.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			for (String ev : eventos) {
				modeloEventos.addElement(ev);
			}
		} catch (ApplicationException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void guardarLesion() {
		
		try {
			
			gl.registrarLesion(getListaJugadores().getSelectedValue(), getOrigenSeleccionado(),
					getListaEventos().getSelectedValue(), aLocalDate(getDcFecha().getDate()),
					getTxTipoLesion().getText(), (String) getCbValoracion().getSelectedItem(),
					getTxActualizacion().getText());
		
		} catch (ApplicationException ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this, "Lesión registrada correctamente.", "Información",
				JOptionPane.INFORMATION_MESSAGE);
		
		getTxTipoLesion().setText("");
		getTxActualizacion().setText("");
		modeloEventos.clear();
		getListaJugadores().clearSelection();
	}

	private OrigenLesion getOrigenSeleccionado() {
		if (getRbEntrenamiento().isSelected()) {
			return OrigenLesion.ENTRENAMIENTO;
		}
		if (getRbOtro().isSelected()) {
			return OrigenLesion.OTRO;
		}
		return OrigenLesion.PARTIDO;
	}

	private LocalDate aLocalDate(Date d) {
		return d == null ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.setBackground(new Color(255, 128, 128));
			btCancelar.setMnemonic('C');
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					volverPrincipal();
				}
			});
		}
		return btCancelar;
	}

	private void cargarDatosIniciales() {
		try {
			todosJugadores = gl.getJugadores();
			jugadoresDelEquipo = todosJugadores;
			filtrarPorEquipo();
		} catch (Exception e) {
			// TODO
		}
	}

	private void filtrarPorEquipo() {
		Equipo equipoSel = (Equipo) getCbEquipo().getSelectedItem();
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
		filtrarListaPorTexto(getTxBuscarJugador().getText());
	}

	private void filtrarListaPorTexto(String texto) {
		String buscado = normalizar(texto == null ? "" : texto.trim());
		modeloListaJugadores.clear();
		EmpleadoDeportivo[] base = (jugadoresDelEquipo != null) ? jugadoresDelEquipo : todosJugadores;
		if (base != null) {
			for (EmpleadoDeportivo j : base) {
				if (normalizar(j.getNombre() + " " + j.getApellido()).contains(buscado)) {
					modeloListaJugadores.addElement(j);
				}
			}
		}
	}

	private void alternarVisibilidadEventos(boolean visible) {

		modeloEventos.clear();

		getListaEventos().setEnabled(visible);
		getBtConsultarEventos().setEnabled(visible);
		if (!visible) {
			modeloEventos.clear();
		}
	}

	private String normalizar(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
	}

	private void volverPrincipal() {
		if (vp != null) {
			vp.setVisible(true);
		}
		this.dispose();
	}
}