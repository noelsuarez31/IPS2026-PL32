package giis.demo.ui.vacaciones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.horarios.HorarioEspecifico;
import giis.demo.model.horarios.HorarioPeriodico;
import giis.demo.model.log.GestionLogSesion;
import giis.demo.model.mantenimiento.TareaMantenimiento;
import giis.demo.model.rol.UsuarioAutenticado;
import giis.demo.model.vacaciones.ElementoInvalidadoVacacion;
import giis.demo.model.vacaciones.GestionVacaciones;
import giis.demo.model.vacaciones.VacacionEmpleado;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;
import giis.demo.util.UnexpectedException;

public class VentanaSeleccionarVacaciones extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String SIN_FECHA = "Ninguno";

	private VentanaPrincipal vp;
	private GestionVacaciones gestionVacaciones;
	private String dniEmpleadoInicial;
	private EmpleadoNoDeportivo empleadoActual;

	// Zona Norte: Empleado (solo lectura), Día inicio, Fecha inicio, Día fin, Fecha fin y Añadir
	private JLabel lblEmpleado;
	private JTextField txtEmpleado;
	private JButton btnDiaInicio;
	private JLabel lblFechaInicio;
	private JTextField txtFechaInicio;
	private JButton btnDiaFin;
	private JLabel lblFechaFin;
	private JTextField txtFechaFin;
	private JButton btnAnadir;

	// Zona Centro Izquierda: Días seleccionados + Quitar / Limpiar
	private JLabel lblDiasSeleccionados;
	private JTable tablaDiasSeleccionados;
	private DefaultTableModel modeloDiasSeleccionados;
	private JScrollPane scrollPaneDiasSeleccionados;
	private JButton btnQuitar;
	private JButton btnLimpiar;

	// Zona Centro Derecha: Mis vacaciones y Turnos/tareas invalidados
	private JLabel lblMisVacaciones;
	private JTable tablaMisVacaciones;
	private DefaultTableModel modeloMisVacaciones;
	private JScrollPane scrollPaneMisVacaciones;

	private JLabel lblInvalidados;
	private JTable tablaInvalidados;
	private DefaultTableModel modeloInvalidados;
	private JScrollPane scrollPaneInvalidados;

	// Zona Sur: Contadores Días disponibles y En selección + botones Atrás y Confirmar Vacaciones
	private JLabel lblDiasDisponiblesTexto;
	private JLabel lblDiasDisponiblesValor;
	private JLabel lblEnSeleccionTexto;
	private JLabel lblEnSeleccionValor;
	private JButton btnAtras;
	private JButton btnConfirmarVacaciones;

	private List<VacacionEmpleado> diasEnSeleccion = new ArrayList<>();
	private int anioVisualizado = LocalDate.now().getYear();

	public VentanaSeleccionarVacaciones(VentanaPrincipal vp) {
		this.vp = vp;
		this.gestionVacaciones = (vp != null && vp.getGestionVacaciones() != null)
				? vp.getGestionVacaciones()
				: new GestionVacaciones();
		if (vp != null && vp.getUsuarioAutenticado() != null) {
			UsuarioAutenticado ua = vp.getUsuarioAutenticado();
			this.dniEmpleadoInicial = ua.getDni();
		}
		inicializarComponentes();
	}

	public VentanaSeleccionarVacaciones(String dniEmpleado) {
		this.gestionVacaciones = new GestionVacaciones();
		this.dniEmpleadoInicial = dniEmpleado;
		inicializarComponentes();
	}

	public VentanaSeleccionarVacaciones() {
		this.gestionVacaciones = new GestionVacaciones();
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Seleccionar Vacaciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1040, 540);
		setMinimumSize(new Dimension(880, 440));

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
		setContentPane(contentPane);

		// 1. ZONA NORTE: Formulario en una misma línea horizontal
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
		panelNorte.add(getLblEmpleado());
		panelNorte.add(getTxtEmpleado());
		panelNorte.add(getBtnDiaInicio());
		panelNorte.add(getLblFechaInicio());
		panelNorte.add(getTxtFechaInicio());
		panelNorte.add(getBtnDiaFin());
		panelNorte.add(getLblFechaFin());
		panelNorte.add(getTxtFechaFin());
		panelNorte.add(getBtnAnadir());
		contentPane.add(panelNorte, BorderLayout.NORTH);

		// 2. ZONA CENTRO: 38% izquierda días seleccionados, 62% derecha vacaciones e invalidados
		JPanel panelCentro = new JPanel(new GridBagLayout());

		// Columna izquierda: Días seleccionados + botones Quitar y Limpiar
		JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 5));
		panelIzquierdo.add(getLblDiasSeleccionados(), BorderLayout.NORTH);
		panelIzquierdo.add(getScrollPaneDiasSeleccionados(), BorderLayout.CENTER);

		JPanel panelBotonesSeleccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
		panelBotonesSeleccion.add(getBtnQuitar());
		panelBotonesSeleccion.add(getBtnLimpiar());
		panelIzquierdo.add(panelBotonesSeleccion, BorderLayout.SOUTH);

		GridBagConstraints gbcIzq = new GridBagConstraints();
		gbcIzq.gridx = 0;
		gbcIzq.gridy = 0;
		gbcIzq.weightx = 0.38;
		gbcIzq.weighty = 1.0;
		gbcIzq.fill = GridBagConstraints.BOTH;
		gbcIzq.insets = new Insets(0, 0, 0, 15);
		panelCentro.add(panelIzquierdo, gbcIzq);

		// Columna derecha: Mis vacaciones (arriba) y Turnos/tareas invalidados (abajo)
		JPanel panelDerecho = new JPanel(new GridLayout(2, 1, 0, 8));

		JPanel subPanelVacaciones = new JPanel(new BorderLayout(0, 3));
		subPanelVacaciones.add(getLblMisVacaciones(), BorderLayout.NORTH);
		subPanelVacaciones.add(getScrollPaneMisVacaciones(), BorderLayout.CENTER);
		panelDerecho.add(subPanelVacaciones);

		JPanel subPanelInvalidados = new JPanel(new BorderLayout(0, 3));
		subPanelInvalidados.add(getLblInvalidados(), BorderLayout.NORTH);
		subPanelInvalidados.add(getScrollPaneInvalidados(), BorderLayout.CENTER);
		panelDerecho.add(subPanelInvalidados);

		GridBagConstraints gbcDer = new GridBagConstraints();
		gbcDer.gridx = 1;
		gbcDer.gridy = 0;
		gbcDer.weightx = 0.62;
		gbcDer.weighty = 1.0;
		gbcDer.fill = GridBagConstraints.BOTH;
		panelCentro.add(panelDerecho, gbcDer);

		contentPane.add(panelCentro, BorderLayout.CENTER);

		// 3. ZONA SUR: Contadores a la izquierda y botones Atrás / Confirmar Vacaciones a la derecha
		JPanel panelSur = new JPanel(new BorderLayout());

		JPanel panelContadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		panelContadores.add(getLblDiasDisponiblesTexto());
		panelContadores.add(getLblDiasDisponiblesValor());
		panelContadores.add(getLblEnSeleccionTexto());
		panelContadores.add(getLblEnSeleccionValor());

		JPanel panelBotonesSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		panelBotonesSur.add(getBtnAtras());
		panelBotonesSur.add(getBtnConfirmarVacaciones());

		panelSur.add(panelContadores, BorderLayout.WEST);
		panelSur.add(panelBotonesSur, BorderLayout.EAST);
		contentPane.add(panelSur, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		inicializarTablasYEventos();
	}

	private JLabel getLblEmpleado() {
		if (lblEmpleado == null) {
			lblEmpleado = new JLabel("Empleado:");
		}
		return lblEmpleado;
	}

	public JTextField getTxtEmpleado() {
		if (txtEmpleado == null) {
			txtEmpleado = new JTextField();
			txtEmpleado.setEditable(false);
			txtEmpleado.setPreferredSize(new Dimension(200, 26));
			txtEmpleado.setColumns(15);
		}
		return txtEmpleado;
	}

	public JButton getBtnDiaInicio() {
		if (btnDiaInicio == null) {
			btnDiaInicio = new JButton("Día inicio");
			btnDiaInicio.setPreferredSize(new Dimension(100, 28));
		}
		return btnDiaInicio;
	}

	private JLabel getLblFechaInicio() {
		if (lblFechaInicio == null) {
			lblFechaInicio = new JLabel("Fecha inicio:");
		}
		return lblFechaInicio;
	}

	public JTextField getTxtFechaInicio() {
		if (txtFechaInicio == null) {
			txtFechaInicio = new JTextField(SIN_FECHA);
			txtFechaInicio.setEditable(false);
			txtFechaInicio.setPreferredSize(new Dimension(85, 26));
			txtFechaInicio.setColumns(8);
		}
		return txtFechaInicio;
	}

	public JButton getBtnDiaFin() {
		if (btnDiaFin == null) {
			btnDiaFin = new JButton("Día fin");
			btnDiaFin.setPreferredSize(new Dimension(90, 28));
		}
		return btnDiaFin;
	}

	private JLabel getLblFechaFin() {
		if (lblFechaFin == null) {
			lblFechaFin = new JLabel("Fecha fin:");
		}
		return lblFechaFin;
	}

	public JTextField getTxtFechaFin() {
		if (txtFechaFin == null) {
			txtFechaFin = new JTextField(SIN_FECHA);
			txtFechaFin.setEditable(false);
			txtFechaFin.setPreferredSize(new Dimension(85, 26));
			txtFechaFin.setColumns(8);
		}
		return txtFechaFin;
	}

	public JButton getBtnAnadir() {
		if (btnAnadir == null) {
			btnAnadir = new JButton("Añadir");
			btnAnadir.setPreferredSize(new Dimension(85, 28));
		}
		return btnAnadir;
	}

	private JLabel getLblDiasSeleccionados() {
		if (lblDiasSeleccionados == null) {
			lblDiasSeleccionados = new JLabel("Días seleccionados:");
			lblDiasSeleccionados.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblDiasSeleccionados;
	}

	public JTable getTablaDiasSeleccionados() {
		if (tablaDiasSeleccionados == null) {
			tablaDiasSeleccionados = new JTable();
			tablaDiasSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tablaDiasSeleccionados;
	}

	private JScrollPane getScrollPaneDiasSeleccionados() {
		if (scrollPaneDiasSeleccionados == null) {
			scrollPaneDiasSeleccionados = new JScrollPane();
			scrollPaneDiasSeleccionados.setViewportView(getTablaDiasSeleccionados());
		}
		return scrollPaneDiasSeleccionados;
	}

	public JButton getBtnQuitar() {
		if (btnQuitar == null) {
			btnQuitar = new JButton("Quitar");
			btnQuitar.setPreferredSize(new Dimension(90, 28));
		}
		return btnQuitar;
	}

	public JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar");
			btnLimpiar.setPreferredSize(new Dimension(90, 28));
		}
		return btnLimpiar;
	}

	private JLabel getLblMisVacaciones() {
		if (lblMisVacaciones == null) {
			lblMisVacaciones = new JLabel("Mis vacaciones:");
			lblMisVacaciones.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblMisVacaciones;
	}

	public JTable getTablaMisVacaciones() {
		if (tablaMisVacaciones == null) {
			tablaMisVacaciones = new JTable();
			tablaMisVacaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tablaMisVacaciones;
	}

	private JScrollPane getScrollPaneMisVacaciones() {
		if (scrollPaneMisVacaciones == null) {
			scrollPaneMisVacaciones = new JScrollPane();
			scrollPaneMisVacaciones.setViewportView(getTablaMisVacaciones());
		}
		return scrollPaneMisVacaciones;
	}

	private JLabel getLblInvalidados() {
		if (lblInvalidados == null) {
			lblInvalidados = new JLabel("Turnos/tareas invalidados:");
			lblInvalidados.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblInvalidados;
	}

	public JTable getTablaInvalidados() {
		if (tablaInvalidados == null) {
			tablaInvalidados = new JTable();
			tablaInvalidados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tablaInvalidados;
	}

	private JScrollPane getScrollPaneInvalidados() {
		if (scrollPaneInvalidados == null) {
			scrollPaneInvalidados = new JScrollPane();
			scrollPaneInvalidados.setViewportView(getTablaInvalidados());
		}
		return scrollPaneInvalidados;
	}

	public JLabel getLblDiasDisponiblesTexto() {
		if (lblDiasDisponiblesTexto == null) {
			lblDiasDisponiblesTexto = new JLabel("Días disponibles (" + anioVisualizado + "):");
			lblDiasDisponiblesTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblDiasDisponiblesTexto;
	}

	public int getAnioVisualizado() {
		return anioVisualizado;
	}

	public void setAnioVisualizado(int anio) {
		this.anioVisualizado = anio;
		refrescarVistaEmpleado();
	}

	public JLabel getLblDiasDisponiblesValor() {
		if (lblDiasDisponiblesValor == null) {
			lblDiasDisponiblesValor = new JLabel("22 / 22");
			lblDiasDisponiblesValor.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblDiasDisponiblesValor.setForeground(new Color(0, 102, 204));
		}
		return lblDiasDisponiblesValor;
	}

	private JLabel getLblEnSeleccionTexto() {
		if (lblEnSeleccionTexto == null) {
			lblEnSeleccionTexto = new JLabel("En selección:");
			lblEnSeleccionTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblEnSeleccionTexto;
	}

	public JLabel getLblEnSeleccionValor() {
		if (lblEnSeleccionValor == null) {
			lblEnSeleccionValor = new JLabel("0");
			lblEnSeleccionValor.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblEnSeleccionValor.setForeground(new Color(0, 102, 204));
		}
		return lblEnSeleccionValor;
	}

	public JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.setPreferredSize(new Dimension(100, 30));
			btnAtras.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cerrarYVolver();
				}
			});
		}
		return btnAtras;
	}

	public JButton getBtnConfirmarVacaciones() {
		if (btnConfirmarVacaciones == null) {
			btnConfirmarVacaciones = new JButton("Confirmar Vacaciones");
			btnConfirmarVacaciones.setPreferredSize(new Dimension(170, 30));
		}
		return btnConfirmarVacaciones;
	}

	private void inicializarTablasYEventos() {
		modeloDiasSeleccionados = new DefaultTableModel(new Object[] { "Fecha", "Día semana" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaDiasSeleccionados().setModel(modeloDiasSeleccionados);

		modeloMisVacaciones = new DefaultTableModel(new Object[] { "Fecha", "Día", "Estado" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaMisVacaciones().setModel(modeloMisVacaciones);
		configurarColumnasCompactas(getTablaMisVacaciones(), 90, 95);

		modeloInvalidados = new DefaultTableModel(new Object[] { "Fecha", "Tipo", "Detalle" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaInvalidados().setModel(modeloInvalidados);
		configurarColumnasCompactas(getTablaInvalidados(), 90, 65);
		getTablaInvalidados().getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());

		getScrollPaneInvalidados().getViewport().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ajustarAlturasTablaInvalidados();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (vp != null) {
					vp.setVisible(true);
				}
			}
		});

		getBtnDiaInicio().addActionListener(e -> SwingUtil.exceptionWrapper(() -> abrirDialogoCalendario(true)));
		getBtnDiaFin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> abrirDialogoCalendario(false)));
		getBtnAnadir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> anadirRangoSeleccionado()));
		getBtnQuitar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> quitarDiaSeleccionado()));
		getBtnLimpiar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> limpiarDiasSeleccionados()));
		getBtnConfirmarVacaciones().addActionListener(e -> SwingUtil.exceptionWrapper(() -> confirmarVacaciones()));

		cargarEmpleadoActual();
	}

	private void configurarColumnasCompactas(JTable tabla, int anchoCol0, int anchoCol1) {
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn col0 = tabla.getColumnModel().getColumn(0);
		col0.setMinWidth(85);
		col0.setPreferredWidth(anchoCol0);
		col0.setMaxWidth(110);

		TableColumn col1 = tabla.getColumnModel().getColumn(1);
		col1.setMinWidth(55);
		col1.setPreferredWidth(anchoCol1);
		col1.setMaxWidth(110);
	}

	private void cargarEmpleadoActual() {
		this.empleadoActual = null;
		try {
			if (dniEmpleadoInicial != null && !dniEmpleadoInicial.isBlank()) {
				List<EmpleadoNoDeportivo> empleados = gestionVacaciones.obtenerEmpleadosNoDeportivos();
				for (EmpleadoNoDeportivo emp : empleados) {
					if (dniEmpleadoInicial.equalsIgnoreCase(emp.getDni())) {
						this.empleadoActual = emp;
						break;
					}
				}
			}

			if (empleadoActual != null) {
				getTxtEmpleado().setText(empleadoActual.getNombre() + " " + empleadoActual.getApellido());
			} else if (vp != null && vp.getUsuarioAutenticado() != null) {
				getTxtEmpleado().setText(vp.getUsuarioAutenticado().getName());
			} else {
				getTxtEmpleado().setText("");
			}

			refrescarVistaEmpleado();
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private void abrirDialogoCalendario(boolean esInicio) {
		if (empleadoActual == null) {
			throw new ApplicationException("No se ha podido identificar al empleado no deportivo autenticado.");
		}

		try {
			String dni = empleadoActual.getDni();
			List<HorarioPeriodico> periodicos = gestionVacaciones.obtenerHorariosPeriodicos(dni);
			List<HorarioEspecifico> especificos = gestionVacaciones.obtenerHorariosEspecificos(dni);
			List<TareaMantenimiento> tareas = gestionVacaciones.obtenerTareasEmpleado(dni);
			List<VacacionEmpleado> confirmadas = gestionVacaciones.obtenerVacacionesConfirmadas(dni);

			Set<String> fechasNoDisponibles = new HashSet<>();
			Map<Integer, Integer> diasUsadosPorAnio = new HashMap<>();
			for (VacacionEmpleado v : confirmadas) {
				fechasNoDisponibles.add(v.getFecha());
				int anio = LocalDate.parse(v.getFecha()).getYear();
				diasUsadosPorAnio.put(anio, diasUsadosPorAnio.getOrDefault(anio, 0) + 1);
			}
			for (VacacionEmpleado v : diasEnSeleccion) {
				fechasNoDisponibles.add(v.getFecha());
				int anio = LocalDate.parse(v.getFecha()).getYear();
				diasUsadosPorAnio.put(anio, diasUsadosPorAnio.getOrDefault(anio, 0) + 1);
			}

			final int anioInicioFijado = (!esInicio && !SIN_FECHA.equals(getTxtFechaInicio().getText()))
					? LocalDate.parse(getTxtFechaInicio().getText()).getYear()
					: -1;

			JCalendar calendario = new JCalendar();
			if (!esInicio && !SIN_FECHA.equals(getTxtFechaInicio().getText())) {
				LocalDate fIni = LocalDate.parse(getTxtFechaInicio().getText());
				Date dIni = Date.from(fIni.atStartOfDay(ZoneId.systemDefault()).toInstant());
				calendario.setDate(dIni);
			} else if (esInicio && !SIN_FECHA.equals(getTxtFechaInicio().getText())) {
				LocalDate fIni = LocalDate.parse(getTxtFechaInicio().getText());
				Date dIni = Date.from(fIni.atStartOfDay(ZoneId.systemDefault()).toInstant());
				calendario.setDate(dIni);
			}

			IDateEvaluator evaluador = new IDateEvaluator() {
				@Override
				public boolean isSpecial(Date date) {
					return !isInvalid(date);
				}

				@Override
				public Color getSpecialForegroundColor() {
					return new Color(0, 102, 51);
				}

				@Override
				public Color getSpecialBackroundColor() {
					return new Color(225, 245, 230);
				}

				@Override
				public String getSpecialTooltip() {
					return "Día disponible con turnos o tareas asignados";
				}

				@Override
				public boolean isInvalid(Date date) {
					if (date == null) {
						return true;
					}
					LocalDate f = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if (f.isBefore(LocalDate.now())) {
						return true;
					}
					if (fechasNoDisponibles.contains(f.toString())) {
						return true;
					}
					if (diasUsadosPorAnio.getOrDefault(f.getYear(), 0) >= GestionVacaciones.MAX_DIAS_VACACIONES) {
						return true;
					}
					if (!esInicio && anioInicioFijado != -1 && f.getYear() != anioInicioFijado) {
						return true;
					}
					return !gestionVacaciones.tieneTurnosOTareasEnFecha(f, periodicos, especificos, tareas);
				}

				@Override
				public Color getInvalidForegroundColor() {
					return Color.LIGHT_GRAY;
				}

				@Override
				public Color getInvalidBackroundColor() {
					return null;
				}

				@Override
				public String getInvalidTooltip() {
					return "Día no disponible (sin turnos/tareas, cupo anual completo o distinto año)";
				}
			};

			calendario.getDayChooser().addDateEvaluator(evaluador);
			calendario.setCalendar(calendario.getCalendar());

			String titulo = esInicio
					? "Selecciona el día de inicio (días con turnos o tareas)"
					: "Selecciona el día de fin (mismo año que inicio)";
			int opcion = JOptionPane.showConfirmDialog(
					this,
					calendario,
					titulo,
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);

			if (opcion == JOptionPane.OK_OPTION) {
				Date fecha = calendario.getDate();
				LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (!esInicio && anioInicioFijado != -1 && fechaLocal.getYear() != anioInicioFijado) {
					throw new ApplicationException(
							"La fecha de fin debe pertenecer al mismo año que la fecha de inicio (" + anioInicioFijado + ").");
				}
				if (evaluador.isInvalid(fecha)) {
					if (diasUsadosPorAnio.getOrDefault(fechaLocal.getYear(), 0) >= GestionVacaciones.MAX_DIAS_VACACIONES) {
						throw new ApplicationException(
								"Ya has completado el máximo de 22 días laborables de vacaciones para el año "
										+ fechaLocal.getYear() + ".");
					}
					throw new ApplicationException(
							"El día seleccionado no está disponible: solo pueden escogerse días futuros en los que el empleado tenga turnos o tareas asignados.");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String fechaFormateada = sdf.format(fecha);
				if (esInicio) {
					getTxtFechaInicio().setText(fechaFormateada);
					String finActualStr = getTxtFechaFin().getText();
					if (SIN_FECHA.equals(finActualStr)) {
						getTxtFechaFin().setText(fechaFormateada);
					} else {
						LocalDate finActual = LocalDate.parse(finActualStr);
						if (finActual.getYear() != fechaLocal.getYear() || finActual.isBefore(fechaLocal)) {
							getTxtFechaFin().setText(fechaFormateada);
						}
					}
				} else {
					getTxtFechaFin().setText(fechaFormateada);
				}
				this.anioVisualizado = fechaLocal.getYear();
				refrescarVistaEmpleado();
			}
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	public void anadirRangoSeleccionado() {
		if (empleadoActual == null) {
			throw new ApplicationException("No se ha podido identificar al empleado no deportivo autenticado.");
		}

		String fechaInicio = getTxtFechaInicio().getText();
		String fechaFin = getTxtFechaFin().getText();

		try {
			List<VacacionEmpleado> nuevos = gestionVacaciones.calcularDiasSeleccionablesEnRango(
					empleadoActual.getDni(), fechaInicio, fechaFin, diasEnSeleccion);

			diasEnSeleccion.addAll(nuevos);
			Collections.sort(diasEnSeleccion, new Comparator<VacacionEmpleado>() {
				@Override
				public int compare(VacacionEmpleado v1, VacacionEmpleado v2) {
					return v1.getFecha().compareTo(v2.getFecha());
				}
			});

			this.anioVisualizado = LocalDate.parse(fechaInicio).getYear();
			refrescarVistaEmpleado();
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	public void quitarDiaSeleccionado() {
		int fila = getTablaDiasSeleccionados().getSelectedRow();
		if (fila == -1) {
			throw new ApplicationException("Debe seleccionar un día de la tabla 'Días seleccionados' para quitarlo.");
		}
		diasEnSeleccion.remove(fila);
		refrescarVistaEmpleado();
	}

	public void limpiarDiasSeleccionados() {
		diasEnSeleccion.clear();
		refrescarVistaEmpleado();
	}

	public void confirmarVacaciones() {
		if (empleadoActual == null) {
			throw new ApplicationException("No se ha podido identificar al empleado no deportivo autenticado.");
		}
		if (diasEnSeleccion.isEmpty()) {
			throw new ApplicationException("Debe añadir al menos un día a 'Días seleccionados' antes de confirmar.");
		}

		try {
			int numConfirmados = diasEnSeleccion.size();
			gestionVacaciones.confirmarVacaciones(empleadoActual, new ArrayList<>(diasEnSeleccion));
			GestionLogSesion.registrarAccion(
					"Confirmación de vacaciones (empleado=" + empleadoActual.getDni() + ", dias=" + numConfirmados + ")");

			diasEnSeleccion.clear();
			getTxtFechaInicio().setText(SIN_FECHA);
			getTxtFechaFin().setText(SIN_FECHA);
			refrescarVistaEmpleado();

			JOptionPane.showMessageDialog(
					this,
					"Vacaciones confirmadas con éxito. Los turnos y tareas afectados han quedado invalidados.",
					"Éxito",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	public void refrescarVistaEmpleado() {
		modeloDiasSeleccionados.setRowCount(0);
		modeloMisVacaciones.setRowCount(0);
		modeloInvalidados.setRowCount(0);

		for (VacacionEmpleado v : diasEnSeleccion) {
			modeloDiasSeleccionados.addRow(new Object[] { v.getFecha(), v.getDiaSemana() });
		}
		getLblEnSeleccionValor().setText(String.valueOf(diasEnSeleccion.size()));

		getLblDiasDisponiblesTexto().setText("Días disponibles (" + anioVisualizado + "):");

		if (empleadoActual == null) {
			getLblDiasDisponiblesValor().setText(
					GestionVacaciones.MAX_DIAS_VACACIONES + " / " + GestionVacaciones.MAX_DIAS_VACACIONES);
			return;
		}

		try {
			String dni = empleadoActual.getDni();
			List<VacacionEmpleado> confirmadas = gestionVacaciones.obtenerVacacionesConfirmadas(dni);
			for (VacacionEmpleado v : confirmadas) {
				modeloMisVacaciones.addRow(new Object[] { v.getFecha(), v.getDiaSemana(), v.getEstado() });
			}

			int confirmadasAnio = gestionVacaciones.contarVacacionesEnAnio(confirmadas, anioVisualizado);
			int disponibles = Math.max(0, GestionVacaciones.MAX_DIAS_VACACIONES - confirmadasAnio);
			getLblDiasDisponiblesValor().setText(disponibles + " / " + GestionVacaciones.MAX_DIAS_VACACIONES);

			List<ElementoInvalidadoVacacion> invalidados = gestionVacaciones.obtenerVistaPreviaInvalidados(
					dni, diasEnSeleccion);
			for (ElementoInvalidadoVacacion inv : invalidados) {
				modeloInvalidados.addRow(new Object[] { inv.getFecha(), inv.getTipo(), inv.getDetalle() });
			}

			SwingUtilities.invokeLater(this::ajustarAlturasTablaInvalidados);
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private void ajustarAlturasTablaInvalidados() {
		JTable tabla = getTablaInvalidados();
		int filas = tabla.getRowCount();
		if (filas == 0) {
			return;
		}
		TableColumn colDetalle = tabla.getColumnModel().getColumn(2);
		int anchoDetalle = colDetalle.getWidth();
		if (anchoDetalle <= 0) {
			anchoDetalle = 420;
		}

		TableCellRenderer renderer = colDetalle.getCellRenderer();
		if (renderer == null) {
			return;
		}

		for (int fila = 0; fila < filas; fila++) {
			Component comp = tabla.prepareRenderer(renderer, fila, 2);
			comp.setSize(anchoDetalle, Short.MAX_VALUE);
			int alto = Math.max(22, comp.getPreferredSize().height);
			if (tabla.getRowHeight(fila) != alto) {
				tabla.setRowHeight(fila, alto);
			}
		}
	}

	private void cerrarYVolver() {
		dispose();
		if (vp != null) {
			vp.setVisible(true);
		}
	}

	/**
	 * Renderizador multilínea para que el detalle completo de los turnos/tareas invalidados
	 * se muestre sin recortes ("...") y conserve los colores alternos y de selección.
	 */
	private static class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public MultiLineCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
			setBorder(new EmptyBorder(3, 5, 3, 5));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			String texto = value == null ? "" : value.toString();
			setText(texto);
			setToolTipText(texto);
			setFont(table.getFont());

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				Color alt = UIManager.getColor("Table.alternateRowColor");
				if (alt != null && row % 2 == 1) {
					setBackground(alt);
				} else {
					setBackground(table.getBackground());
				}
			}

			int colWidth = table.getColumnModel().getColumn(column).getWidth();
			if (colWidth <= 0) {
				colWidth = 420;
			}
			setSize(colWidth, Short.MAX_VALUE);
			return this;
		}
	}
}
