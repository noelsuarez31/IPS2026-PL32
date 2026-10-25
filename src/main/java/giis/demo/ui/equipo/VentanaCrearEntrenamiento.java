package giis.demo.ui.equipo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JCalendar;

import giis.demo.model.equipo.CrearEntrenamientoService;
import giis.demo.model.reserva.Instalacion;
import giis.demo.ui.VentanaPrincipal;

public class VentanaCrearEntrenamiento extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vPrincipal;
	private CrearEntrenamientoService service;
	
	private JPanel contentPane;
	private JPanel pnSuperior;
	private JPanel pnCentro;
	private JPanel pnInferior;
	private JLabel lbDniEntrenador;
	private JComboBox<String> cbDniEntrenador;
	private JLabel lbEquipo;
	private JTextField txEquipo;
	private JPanel pnIzquierda;
	private JPanel pnInstalaciones;
	private JScrollPane scrInstalaciones;
	private JList<Instalacion> listInstalaciones;
	private DefaultListModel<Instalacion> modeloInstalaciones;
	private JPanel pnFecha;
	private JCalendar calendarioFecha;
	private JPanel pnDerecha;
	private JPanel pnOcupacion;
	private JTextArea txOcupacion;
	private JPanel pnFormularioHoras;
	private JPanel pnHoraInicio;
	private JLabel lbHoraInicio;
	private JTextField txHoraInicio;
	private JPanel pnHoraFin;
	private JLabel lbHoraFin;
	private JTextField txHoraFin;
	private JPanel pnBotonCrear;
	private JButton btnCrearEntrenamiento;
	private JButton btnAtras;

	public VentanaCrearEntrenamiento(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.service = this.vPrincipal.getCrearEntrenamientoService();

		setTitle("Planificar Entrenamiento");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 940, 620);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);

		contentPane.add(getPnSuperior(), BorderLayout.NORTH);
		contentPane.add(getPnCentro(), BorderLayout.CENTER);
		contentPane.add(getPnInferior(), BorderLayout.SOUTH);

		cargarDatosIniciales();
		actualizarEventosYOcupacion();
	}

	
	private JPanel getPnSuperior() {
		if (pnSuperior == null) {
			pnSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
			pnSuperior.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"Datos del Entrenador", TitledBorder.LEADING, TitledBorder.TOP,
					new Font("Tahoma", Font.BOLD, 13), new Color(0, 0, 0)));
			pnSuperior.add(getLbDniEntrenador());
			pnSuperior.add(getCbDniEntrenador());
			pnSuperior.add(getLbEquipo());
			pnSuperior.add(getTxEquipo());
		}
		return pnSuperior;
	}

	private JLabel getLbDniEntrenador() {
		if (lbDniEntrenador == null) {
			lbDniEntrenador = new JLabel("DNI Entrenador:");
			lbDniEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbDniEntrenador;
	}

	private JComboBox<String> getCbDniEntrenador() {
		if (cbDniEntrenador == null) {
			cbDniEntrenador = new JComboBox<String>();
			cbDniEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 14));
			cbDniEntrenador.setPreferredSize(new Dimension(140, 26));
			cbDniEntrenador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					actualizarEquipoEntrenador();
					actualizarEventosYOcupacion();
				}
			});
		}
		return cbDniEntrenador;
	}

	private JLabel getLbEquipo() {
		if (lbEquipo == null) {
			lbEquipo = new JLabel("Equipo asignado:");
			lbEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbEquipo;
	}

	private JTextField getTxEquipo() {
		if (txEquipo == null) {
			txEquipo = new JTextField();
			txEquipo.setFont(new Font("Tahoma", Font.BOLD, 14));
			txEquipo.setEditable(false);
			txEquipo.setColumns(18);
			txEquipo.setBackground(new Color(245, 245, 245));
		}
		return txEquipo;
	}

	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setLayout(new GridLayout(1, 2, 12, 0));
			pnCentro.add(getPnIzquierda());
			pnCentro.add(getPnDerecha());
		}
		return pnCentro;
	}

	private JPanel getPnIzquierda() {
		if (pnIzquierda == null) {
			pnIzquierda = new JPanel(new BorderLayout(0, 10));
			pnIzquierda.add(getPnInstalaciones(), BorderLayout.NORTH);
			pnIzquierda.add(getPnFecha(), BorderLayout.CENTER);
		}
		return pnIzquierda;
	}

	private JPanel getPnInstalaciones() {
		if (pnInstalaciones == null) {
			pnInstalaciones = new JPanel(new BorderLayout(0, 5));
			pnInstalaciones.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"1. Seleccione la Instalaci\u00F3n (Selecci\u00F3n \u00FAnica)",
					TitledBorder.LEADING, TitledBorder.TOP,
					new Font("Tahoma", Font.BOLD, 13), null));
			pnInstalaciones.setPreferredSize(new Dimension(0, 135));
			pnInstalaciones.add(getScrInstalaciones(), BorderLayout.CENTER);
		}
		return pnInstalaciones;
	}

	private JScrollPane getScrInstalaciones() {
		if (scrInstalaciones == null) {
			scrInstalaciones = new JScrollPane(getListInstalaciones());
		}
		return scrInstalaciones;
	}

	private JList<Instalacion> getListInstalaciones() {
		if (listInstalaciones == null) {
			modeloInstalaciones = new DefaultListModel<Instalacion>();
			listInstalaciones = new JList<Instalacion>(modeloInstalaciones);
			listInstalaciones.setFont(new Font("Tahoma", Font.PLAIN, 13));
			listInstalaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listInstalaciones.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						actualizarEventosYOcupacion();
					}
				}
			});
		}
		return listInstalaciones;
	}

	private JPanel getPnFecha() {
		if (pnFecha == null) {
			pnFecha = new JPanel(new BorderLayout(0, 5));
			pnFecha.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"2. Fecha del Entrenamiento", TitledBorder.LEADING, TitledBorder.TOP,
					new Font("Tahoma", Font.BOLD, 13), null));
			pnFecha.add(getCalendarioFecha(), BorderLayout.CENTER);
		}
		return pnFecha;
	}

	private JCalendar getCalendarioFecha() {
		if (calendarioFecha == null) {
			calendarioFecha = new JCalendar();

			PropertyChangeListener listenerFecha = new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					actualizarEventosYOcupacion();
				}
			};

			calendarioFecha.getDayChooser().addPropertyChangeListener("day", listenerFecha);
			calendarioFecha.getDayChooser().addPropertyChangeListener("month", listenerFecha);
			calendarioFecha.getDayChooser().addPropertyChangeListener("year", listenerFecha);
		}
		return calendarioFecha;
	}

	private JPanel getPnDerecha() {
		if (pnDerecha == null) {
			pnDerecha = new JPanel(new BorderLayout(0, 10));
			pnDerecha.add(getPnOcupacion(), BorderLayout.CENTER);
			pnDerecha.add(getPnFormularioHoras(), BorderLayout.SOUTH);
		}
		return pnDerecha;
	}

	private JPanel getPnOcupacion() {
		if (pnOcupacion == null) {
			pnOcupacion = new JPanel(new BorderLayout(0, 5));
			pnOcupacion.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"3. Ocupaci\u00F3n y Eventos (D\u00EDa e Instalaci\u00F3n)",
					TitledBorder.LEADING, TitledBorder.TOP,
					new Font("Tahoma", Font.BOLD, 13), null));
			pnOcupacion.add(getTxOcupacion(), BorderLayout.CENTER);
		}
		return pnOcupacion;
	}

	private JTextArea getTxOcupacion() {
		if (txOcupacion == null) {
			txOcupacion = new JTextArea();
			txOcupacion.setEditable(false);
			txOcupacion.setLineWrap(true);           // Salto de línea automático
			txOcupacion.setWrapStyleWord(true);       // No corta palabras al saltar
			txOcupacion.setFont(new Font("Tahoma", Font.PLAIN, 13));
			txOcupacion.setBackground(new Color(250, 250, 250));
			txOcupacion.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
		return txOcupacion;
	}

	private JPanel getPnFormularioHoras() {
		if (pnFormularioHoras == null) {
			pnFormularioHoras = new JPanel(new GridLayout(3, 1, 0, 5));
			pnFormularioHoras.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"4. Horario del Entrenamiento", TitledBorder.LEADING, TitledBorder.TOP,
					new Font("Tahoma", Font.BOLD, 13), null));
			pnFormularioHoras.add(getPnHoraInicio());
			pnFormularioHoras.add(getPnHoraFin());
			pnFormularioHoras.add(getPnBotonCrear());
		}
		return pnFormularioHoras;
	}

	private JPanel getPnHoraInicio() {
		if (pnHoraInicio == null) {
			pnHoraInicio = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
			pnHoraInicio.add(getLbHoraInicio());
			pnHoraInicio.add(getTxHoraInicio());
		}
		return pnHoraInicio;
	}

	private JLabel getLbHoraInicio() {
		if (lbHoraInicio == null) {
			lbHoraInicio = new JLabel("Hora inicio (ej:HH:mm):");
			lbHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbHoraInicio;
	}

	private JTextField getTxHoraInicio() {
		if (txHoraInicio == null) {
			txHoraInicio = new JTextField();
			txHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
			txHoraInicio.setColumns(8);
		}
		return txHoraInicio;
	}

	private JPanel getPnHoraFin() {
		if (pnHoraFin == null) {
			pnHoraFin = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
			pnHoraFin.add(getLbHoraFin());
			pnHoraFin.add(getTxHoraFin());
		}
		return pnHoraFin;
	}

	private JLabel getLbHoraFin() {
		if (lbHoraFin == null) {
			lbHoraFin = new JLabel("Hora fin (ej:HH:mm):   ");
			lbHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbHoraFin;
	}

	private JTextField getTxHoraFin() {
		if (txHoraFin == null) {
			txHoraFin = new JTextField();
			txHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 14));
			txHoraFin.setColumns(8);
		}
		return txHoraFin;
	}

	private JPanel getPnBotonCrear() {
		if (pnBotonCrear == null) {
			pnBotonCrear = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
			pnBotonCrear.add(getBtnCrearEntrenamiento());
		}
		return pnBotonCrear;
	}

	private JButton getBtnCrearEntrenamiento() {
		if (btnCrearEntrenamiento == null) {
			btnCrearEntrenamiento = new JButton("Crear Entrenamiento");
			btnCrearEntrenamiento.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnCrearEntrenamiento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accionCrearEntrenamiento();
				}
			});
		}
		return btnCrearEntrenamiento;
	}

	private JPanel getPnInferior() {
		if (pnInferior == null) {
			pnInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
			pnInferior.add(getBtnAtras());
		}
		return pnInferior;
	}

	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atr\u00E1s");
			btnAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					if (vPrincipal != null) {
						vPrincipal.setVisible(true);
					}
				}
			});
		}
		return btnAtras;
	}

	// Metodos auxiliares
	private void cargarDatosIniciales() {
		List<String> dnis = service.obtenerDnisEntrenadores();
		getCbDniEntrenador().setModel(new DefaultComboBoxModel<>(dnis.toArray(new String[0])));
		actualizarEquipoEntrenador();

		modeloInstalaciones.clear();
		for (Instalacion inst : service.obtenerInstalacionesDisponibles()) {
			modeloInstalaciones.addElement(inst);
		}
		if (modeloInstalaciones.getSize() > 0) {
			getListInstalaciones().setSelectedIndex(0);
		}
	}

	private void actualizarEquipoEntrenador() {
		String dni = (String) getCbDniEntrenador().getSelectedItem();
		String nombreEquipo = service.obtenerNombreEquipoDeEntrenador(dni);
		getTxEquipo().setText(nombreEquipo);
	}

	private void actualizarEventosYOcupacion() {
		Date fechaUtil = getCalendarioFecha().getDate();
		if (fechaUtil == null) {
			return;
		}
		LocalDate fecha = fechaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Instalacion instalacionSel = getListInstalaciones().getSelectedValue();
		String nombreInstalacion = (instalacionSel != null) ? instalacionSel.getName() : "Instalación";
		String dni = (String) getCbDniEntrenador().getSelectedItem();
		int idEquipo = service.obtenerIdEquipoDeEntrenador(dni);

		getPnOcupacion().setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"3. Ocupación (" + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + nombreInstalacion + ")",
				TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 13), null));

		StringBuilder sb = new StringBuilder();
		List<String> entrevistas = service.obtenerEntrevistasJugadores(idEquipo, fecha);

		if (entrevistas.isEmpty()) {
			sb.append("• [LIBRE] Ningún jugador del equipo tiene entrevistas asignadas este día.\n");
		} else {
			for (String ent : entrevistas) {
				sb.append(ent).append("\n\n");
			}
		}

		getTxOcupacion().setText(sb.toString());
		getTxOcupacion().setCaretPosition(0);
	}

	private void accionCrearEntrenamiento() {
		try {
			String hIniStr = getTxHoraInicio().getText().trim();
			String hFinStr = getTxHoraFin().getText().trim();
			DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

			if (hIniStr.isEmpty() || hFinStr.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Debe introducir las horas en formato HH:mm.", "Validación", JOptionPane.WARNING_MESSAGE);
				return;
			}

			LocalTime hIni = LocalTime.parse(hIniStr, formatoHora);
			LocalTime hFin = LocalTime.parse(hFinStr, formatoHora);

			Date fechaUtil = getCalendarioFecha().getDate();
			LocalDate fecha = fechaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Instalacion inst = getListInstalaciones().getSelectedValue();
			if (inst == null) {
				JOptionPane.showMessageDialog(this, "Debe seleccionar una instalación.", "Validación", JOptionPane.WARNING_MESSAGE);
				return;
			}

			String dni = (String) getCbDniEntrenador().getSelectedItem();
			int idEquipo = service.obtenerIdEquipoDeEntrenador(dni);

			service.crearEntrenamiento(fecha, hIni, hFin, idEquipo, inst.getInstalacionId());

			JOptionPane.showMessageDialog(this, "¡Entrenamiento planificado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			getTxHoraInicio().setText("");
			getTxHoraFin().setText("");

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}