package giis.demo.ui.reserva;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.reserva.Instalacion;
import giis.demo.model.reserva.Intervalo;
import giis.demo.model.reserva.RealizarReserva;
import giis.demo.ui.VentanaPrincipal;

import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;

public class VentanaReserva extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelFecha;
	private JPanel panelSeleccion;
	private JSpinner spFecha;
	private JPanel panelSeleccionFecha;
	private JPanel panelBtConfirmacion;
	private JButton btConsulta;
	private JLabel lbFecha;
	private JPanel panelHoraInicio;
	private JLabel lbHoraInicio;
	private JSpinner spInicio;
	private JPanel panelHoraFin;
	private JLabel lbHoraFin;
	private JSpinner spFin;
	private JPanel panelBotones;
	private JButton btSiguiente;
	private JPanel panelHorario;
	private JScrollPane spHorario;
	private JButton btVolver;

	private VentanaPrincipal vp;
	private RealizarReserva rr;
	private JLabel lbInstalacion;
	private JComboBox<Instalacion> cbInstalacion;
	private JButton btCancelar;

	private CerrarVentana cv;

	private JTable tablaHorarios;
	private DefaultTableModel modeloTablaHorarios;

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */
	public VentanaReserva(VentanaPrincipal vp) throws SQLException {

		this.vp = vp;
		this.rr = new RealizarReserva();
		this.cv = new CerrarVentana();

		setTitle("Reserva de Instalaciones");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1044, 432);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelFecha(), BorderLayout.NORTH);
		contentPane.add(getPanelSeleccion(), BorderLayout.SOUTH);
		contentPane.add(getPanelHorario(), BorderLayout.CENTER);
		setLocationRelativeTo(null);
		addWindowListener(cv);
	}

	/**
	 * Devuelve la instancia de RealizarReserva que se utiliza
	 * 
	 * @return instancia de RealizarReserva que se utiliza
	 */
	public RealizarReserva getRealizarReserva() {

		return rr;

	}

	private JPanel getPanelFecha() throws SQLException {
		if (panelFecha == null) {
			panelFecha = new JPanel();
			panelFecha.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelFecha.setLayout(new BorderLayout(0, 0));
			panelFecha.add(getPanelSeleccionFecha(), BorderLayout.CENTER);
			panelFecha.add(getPanelBtConfirmacion(), BorderLayout.EAST);
		}
		return panelFecha;
	}

	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			panelSeleccion = new JPanel();
			panelSeleccion.setBorder(new LineBorder(new Color(0, 0, 0)));
			panelSeleccion.setLayout(new BorderLayout(0, 0));
			panelSeleccion.add(getPanelHoraInicio(), BorderLayout.WEST);
			panelSeleccion.add(getPanelHoraFin(), BorderLayout.CENTER);
			panelSeleccion.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panelSeleccion;
	}

	JSpinner getSpFecha() {

		if (spFecha == null) {

			Calendar calendario = Calendar.getInstance();

			calendario.set(Calendar.HOUR_OF_DAY, 0);
			calendario.set(Calendar.MINUTE, 0);
			calendario.set(Calendar.SECOND, 0);
			calendario.set(Calendar.MILLISECOND, 0);

			Date fechaMin = calendario.getTime();

			SpinnerDateModel modeloFecha = new SpinnerDateModel(new Date(), fechaMin, null, Calendar.DAY_OF_MONTH);

			spFecha = new JSpinner(modeloFecha);
			spFecha.setBounds(645, 5, 163, 26);

			JSpinner.DateEditor editor = new JSpinner.DateEditor(spFecha, "dd/MM/yyyy");
			spFecha.setEditor(editor);
		}
		return spFecha;
	}

	private JPanel getPanelSeleccionFecha() throws SQLException {
		if (panelSeleccionFecha == null) {
			panelSeleccionFecha = new JPanel();
			panelSeleccionFecha.setLayout(null);
			panelSeleccionFecha.add(getLbFecha());
			panelSeleccionFecha.add(getSpFecha());
			panelSeleccionFecha.add(getCbInstalacion());
			panelSeleccionFecha.add(getLbInstalacion());
		}
		return panelSeleccionFecha;
	}

	private JPanel getPanelBtConfirmacion() {
		if (panelBtConfirmacion == null) {
			panelBtConfirmacion = new JPanel();
			panelBtConfirmacion.add(getBtVolver());
			panelBtConfirmacion.add(getBtConsulta());
		}
		return panelBtConfirmacion;
	}

	private JButton getBtConsulta() {
		if (btConsulta == null) {
			btConsulta = new JButton("Consultar");
			btConsulta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					consultarHorarios();
				}
			});
			btConsulta.setBackground(new Color(128, 255, 128));
			btConsulta.setToolTipText("Consultar los horarios disponibles");
			btConsulta.setMnemonic('C');
		}
		return btConsulta;
	}

	private JLabel getLbFecha() {
		if (lbFecha == null) {
			lbFecha = new JLabel("Seleccione la fecha: ");
			lbFecha.setBounds(483, 8, 143, 20);
			lbFecha.setDisplayedMnemonic('C');
			lbFecha.setLabelFor(getSpFecha());
		}
		return lbFecha;
	}

	private JPanel getPanelHoraInicio() {
		if (panelHoraInicio == null) {
			panelHoraInicio = new JPanel();
			panelHoraInicio.add(getLbHoraInicio());
			panelHoraInicio.add(getSpInicio());
		}
		return panelHoraInicio;
	}

	private JLabel getLbHoraInicio() {
		if (lbHoraInicio == null) {
			lbHoraInicio = new JLabel("Hora de inicio:");
			lbHoraInicio.setLabelFor(getSpInicio());
			lbHoraInicio.setDisplayedMnemonic('R');
			lbHoraInicio.setToolTipText("Seleccione la hora de inicio");
		}
		return lbHoraInicio;
	}

	JSpinner getSpInicio() {

		if (spInicio == null) {

			spInicio = new JSpinner(crearModeloHora(8, 0));
			spInicio.setEnabled(false);
			spInicio.setEditor(new JSpinner.DateEditor(spInicio, "HH:mm"));

		}
		return spInicio;
	}

	private JPanel getPanelHoraFin() {
		if (panelHoraFin == null) {
			panelHoraFin = new JPanel();
			panelHoraFin.add(getLbHoraFin());
			panelHoraFin.add(getSpFin());
		}
		return panelHoraFin;
	}

	private JLabel getLbHoraFin() {
		if (lbHoraFin == null) {
			lbHoraFin = new JLabel("Hora de fin: ");
			lbHoraFin.setLabelFor(getSpFin());
			lbHoraFin.setToolTipText("Seleccione la hora de fin");
			lbHoraFin.setDisplayedMnemonic('F');
		}
		return lbHoraFin;
	}

	JSpinner getSpFin() {
		if (spFin == null) {

			spFin = new JSpinner(crearModeloHora(9, 0));
			spFin.setEnabled(false);
			spFin.setEditor(new JSpinner.DateEditor(spFin, "HH:mm"));
		}
		return spFin;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtCancelar());
			panelBotones.add(getBtSiguiente());
		}
		return panelBotones;
	}

	private JButton getBtSiguiente() {
		if (btSiguiente == null) {
			btSiguiente = new JButton("Siguiente");
			btSiguiente.setEnabled(false);
			btSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarDatosDeUsuario();
				}
			});
			btSiguiente.setMnemonic('S');
			btSiguiente.setToolTipText("Pulsa para continuar con la reserva");
			btSiguiente.setBackground(new Color(128, 255, 128));
		}
		return btSiguiente;
	}

	private JPanel getPanelHorario() {
		if (panelHorario == null) {
			panelHorario = new JPanel();
			panelHorario.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
					"Horarios de la instalaci\u00F3n: ", TitledBorder.LEADING, TitledBorder.TOP, null,
					new Color(0, 0, 0)));
			panelHorario.setLayout(new BorderLayout(0, 0));
			panelHorario.add(getSpHorario());
		}
		return panelHorario;
	}

	private JScrollPane getSpHorario() {

		if (spHorario == null) {

			spHorario = new JScrollPane(getTablaHorarios());
		}
		return spHorario;
	}

	public JTable getTablaHorarios() {

		if (tablaHorarios == null) {

			String[] columnas = { "Hora de inicio", "Hora de fin", "Estado", "Motivo" };
			modeloTablaHorarios = new DefaultTableModel(columnas, 0) {

				@Override
				public boolean isCellEditable(int fila, int columna) {
					return false;
				}

			};

			tablaHorarios = new JTable(modeloTablaHorarios);
			tablaHorarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			tablaHorarios.setRowHeight(25);
			tablaHorarios.getTableHeader().setReorderingAllowed(false);

			tablaHorarios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
						boolean tieneFoco, int fila, int columna) {

					Component componente = super.getTableCellRendererComponent(tabla, valor, seleccionado, tieneFoco,
							fila, columna);

					String estado = String.valueOf(tabla.getValueAt(fila, 2));

					String motivo = String.valueOf(tabla.getValueAt(fila, 3));

					if (!seleccionado) {

						if ("Disponible".equals(estado)) {
							componente.setBackground(new Color(200, 255, 200));

						} else if (motivo.toLowerCase().contains("margen")) {
							componente.setBackground(new Color(255, 220, 160));

						} else {
							componente.setBackground(new Color(255, 200, 200));
						}

					} else {
						componente.setBackground(tabla.getSelectionBackground());
					}

					return componente;
				}
			});

			modeloTablaHorarios
					.addRow(new Object[] { "--:--", "--:--", "Pendiente", "Pulse Consultar para ver los horarios" });

		}

		return tablaHorarios;

	}

	public void reiniciar() throws SQLException {

		if (getCbInstalacion().getItemCount() > 0) {

			this.getCbInstalacion().setSelectedIndex(0);

		}

		this.getSpFecha().setValue(new Date());
		this.getSpInicio().setValue(hora(8, 0));
		this.getSpFin().setValue(hora(9, 0));

		getCbInstalacion().setEnabled(true);
		getSpFecha().setEnabled(true);

		btConsulta.setEnabled(getCbInstalacion().getItemCount() > 0);
		btCancelar.setEnabled(false);
		btSiguiente.setEnabled(false);

		modeloTablaHorarios.setRowCount(0);
		modeloTablaHorarios.addRow(new Object[] {
			    "--:--",
			    "--:--",
			    "Pendiente",
			    "Pulse Consultar para ver los horarios"
			});

		getSpInicio().setEnabled(false);
		getSpFin().setEnabled(false);

	}

	private Date hora(int h, int m) {

		Calendar c = Calendar.getInstance();

		c.clear();
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);

		return c.getTime();
	}

	private SpinnerDateModel crearModeloHora(int h, int m) {
		return new SpinnerDateModel(hora(h, m), hora(8, 0), hora(22, 0), Calendar.MINUTE);
	}

	private void consultarHorarios() {

		Instalacion instalacion = (Instalacion) cbInstalacion.getSelectedItem();

		if (instalacion == null) {

			JOptionPane.showMessageDialog(this, "Seleccione una instalación.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;

		}

		try {

			int idInstalacion = instalacion.getInstalacionId();
			LocalDate fecha = ((Date) spFecha.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			List<Intervalo> ocupados = rr.obtenerOcupaciones(idInstalacion, fecha);
			List<Intervalo> disponibles = rr.obtenerIntervalosDisponibles(idInstalacion, fecha);

			List<Intervalo> todos = new ArrayList<Intervalo>();

			todos.addAll(ocupados);
			todos.addAll(disponibles);

			todos.sort(Comparator.comparing(Intervalo::getHoraInicio));

			modeloTablaHorarios.setRowCount(0);

			DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");

			for (Intervalo intervalo : todos) {

				String motivo = intervalo.getMotivo();
				boolean isDisponible = "Disponible".equalsIgnoreCase(motivo);

				modeloTablaHorarios.addRow(new Object[] { intervalo.getHoraInicio().format(formato),
						intervalo.getHoraFin().format(formato), isDisponible ? "Disponible" : "Ocupado",
						isDisponible ? "-" : motivo });
			}

			if (todos.isEmpty()) {

				modeloTablaHorarios
						.addRow(new Object[] { "-", "-", "Sin franjas", "No hay intervalos disponibles para mostrar" });
			}

			this.cbInstalacion.setEnabled(false);
			this.spFecha.setEnabled(false);

			this.btConsulta.setEnabled(false);
			this.btCancelar.setEnabled(true);
			this.btSiguiente.setEnabled(true);

			this.spInicio.setEnabled(true);
			this.spFin.setEnabled(true);

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(this, "No se han podido consultar los horarios de la instalación.",
					"Error de base de datos", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();

		}
	}

	private int aMinutos(Date d) {
		LocalTime t = d.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		return t.getHour() * 60 + t.getMinute();
	}

	private boolean checkHoras() {
		return aMinutos((Date) spInicio.getValue()) < aMinutos((Date) spFin.getValue());
	}

	private boolean checkDuration() {

		int duracion = aMinutos((Date) spFin.getValue()) - aMinutos((Date) spInicio.getValue());

		return duracion >= 60 && duracion % 60 == 0;
	}

	private void registrarDatosDeUsuario() {

		if (!checkHoras()) {
			JOptionPane.showMessageDialog(this, "La hora de inicio no puede ser mayor o igual a la hora final.",
					"Duración no válida", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!checkDuration()) {
			JOptionPane.showMessageDialog(this,
					"La reserva debe durar al menos una hora.\n"
							+ "Solo se puede reservar horas enteras, no se admiten reservas de horas y x minutos",
					"Duración no válida", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// check is available to book

		Instalacion instalacion = (Instalacion) cbInstalacion.getSelectedItem();

		if (instalacion == null) {
			JOptionPane.showMessageDialog(this, "Seleccione una instalación.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		LocalDate fecha = ((Date) spFecha.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalTime inicio = ((Date) spInicio.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		LocalTime fin = ((Date) spFin.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

		try {
			
			boolean disponible = rr.comprobarDisponibilidad(instalacion.getInstalacionId(), fecha, inicio, fin);

			if (!disponible) {
				JOptionPane.showMessageDialog(this,
						"El intervalo seleccionado no está disponible. Seleccione otro horario.",
						"Horario no disponible", JOptionPane.WARNING_MESSAGE);
				return;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "No se ha podido comprobar la disponibilidad.",
												"Error de base de datos", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}

		VentaRegistroUsuario vru = new VentaRegistroUsuario(this);
		vru.setVisible(true);

	}

	private void volverAlMenu() throws SQLException {

		this.vp.setVisible(true);
		cerrar();
		this.dispose();
	}

	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver");
			btVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						volverAlMenu();
					} catch (SQLException e1) {

						JOptionPane.showMessageDialog(null, "No se ha podido cerrar la conexion con la base de datos",
								"Error", JOptionPane.ERROR_MESSAGE);
						System.err.println("Error al cerrar la BBDD");
						e1.printStackTrace();

					}
				}
			});
			btVolver.setToolTipText("Volver al menú");
			btVolver.setMnemonic('V');
			btVolver.setBackground(new Color(255, 128, 128));
		}
		return btVolver;
	}

	private JLabel getLbInstalacion() {
		if (lbInstalacion == null) {
			lbInstalacion = new JLabel("Seleccione la instalación:");
			lbInstalacion.setBounds(0, 8, 175, 20);
			lbInstalacion.setDisplayedMnemonic('T');
		}
		return lbInstalacion;
	}

	JComboBox<Instalacion> getCbInstalacion() throws SQLException {
		
		if (cbInstalacion == null) {
			
			cbInstalacion = new JComboBox<Instalacion>();
			cbInstalacion.setModel(new DefaultComboBoxModel<Instalacion>(rr.obtenerInstalacionesDisponibles()));
			cbInstalacion.setBounds(196, 4, 272, 28);
		}
		return cbInstalacion;
	}

	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.setEnabled(false);
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						reiniciar();
					} catch (SQLException e1) {

						JOptionPane.showMessageDialog(contentPane,
								"Se ha producido el error al cargar las instalaciones disponibles.", null,
								JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();

					}
				}
			});
			btCancelar.setToolTipText("Pulsa para cancelar la reserva");
			btCancelar.setMnemonic('N');
			btCancelar.setBackground(new Color(255, 128, 128));
		}
		return btCancelar;
	}

	private void cerrar() throws SQLException {

		this.rr.close();
	}

	private class CerrarVentana extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent ew) {

			try {
				rr.close();
			} catch (SQLException e) {

				JOptionPane.showMessageDialog(null, "No se ha podido cerrar la conexion con la base de datos", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("Error al cerrar la BBDD");
				e.printStackTrace();
			}

			dispose();

		}

	}
}
