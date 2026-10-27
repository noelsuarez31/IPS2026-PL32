package giis.demo.ui.equipo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import giis.demo.exceptions.TrainingException;
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
	private JScrollPane scrOcupacion;
	private JTable tablaOcupacion;
	private DefaultTableModel modeloTablaOcupacion;
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
		setBounds(100, 100, 960, 620);
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
			pnOcupacion.add(getScrOcupacion(), BorderLayout.CENTER);
		}
		return pnOcupacion;
	}

	private JScrollPane getScrOcupacion() {
		if (scrOcupacion == null) {
			scrOcupacion = new JScrollPane();
			scrOcupacion.setViewportView(getTablaOcupacion());
		}
		return scrOcupacion;
	}

	private JTable getTablaOcupacion() {
		if (tablaOcupacion == null) {
			String[] columnas = { "Inicio", "Fin", "Evento", "Detalle", "¿Sobreescribible?" };
			modeloTablaOcupacion = new DefaultTableModel(columnas, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			tablaOcupacion = new JTable(modeloTablaOcupacion);
			tablaOcupacion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaOcupacion.setRowHeight(24);
			tablaOcupacion.getTableHeader().setReorderingAllowed(false);
			tablaOcupacion.setFont(new Font("Tahoma", Font.PLAIN, 12));
			tablaOcupacion.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));

			// Ajuste de anchos relativos de columna
			tablaOcupacion.getColumnModel().getColumn(0).setPreferredWidth(55);
			tablaOcupacion.getColumnModel().getColumn(1).setPreferredWidth(55);
			tablaOcupacion.getColumnModel().getColumn(2).setPreferredWidth(100);
			tablaOcupacion.getColumnModel().getColumn(3).setPreferredWidth(160);
			tablaOcupacion.getColumnModel().getColumn(4).setPreferredWidth(130);

			// Renderizador con colores para distinguir sobreescribibles de colisiones
			tablaOcupacion.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (!isSelected) {
						String sobre = String.valueOf(table.getValueAt(row, 4));
						if (sobre.startsWith("Sí")) {
							c.setBackground(new Color(230, 248, 230)); // verde suave para sobreescribibles
						} else if (sobre.startsWith("No")) {
							c.setBackground(new Color(255, 232, 232)); // rojo suave para colisiones no sobreescribibles
						} else {
							c.setBackground(Color.WHITE); // blanco neutro para la fila informativa "Libre"
						}
					}
					return c;
				}
			});
		}
		return tablaOcupacion;
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
		int idInstalacion = (instalacionSel != null) ? instalacionSel.getInstalacionId() : 0;
		String nombreInstalacion = (instalacionSel != null) ? instalacionSel.getName() : "Instalación";
		String dni = (String) getCbDniEntrenador().getSelectedItem();
		int idEquipo = service.obtenerIdEquipoDeEntrenador(dni);

		getPnOcupacion().setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"3. Ocupación (" + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + nombreInstalacion + ")",
				TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 13), null));

		DefaultTableModel modelo = (DefaultTableModel) getTablaOcupacion().getModel();
		modelo.setRowCount(0);
		List<Object[]> filas = service.obtenerFilasOcupacion(idEquipo, idInstalacion, fecha);

		if (filas.isEmpty()) {
			modelo.addRow(new Object[] { "--:--", "--:--", "Libre", "Instalación libre", "-" });
		} else {
			for (Object[] fila : filas) {
				modelo.addRow(fila);
			}
		}
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
			actualizarEventosYOcupacion();

		} catch (TrainingException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Colisión detectada", JOptionPane.WARNING_MESSAGE);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}