package giis.demo.ui.horarios;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import giis.demo.model.horarios.GestionHorarioSemanal;
import giis.demo.model.horarios.HorarioSemanal;
import giis.demo.model.log.GestionLogSesion;
import giis.demo.ui.login.VentanaPrincipal;
import giis.demo.util.SwingUtil;
import giis.demo.util.UnexpectedException;

public class VentanaHorarioSemanal extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vp;
	private GestionHorarioSemanal ghs;
	private String dniEmpleadoActual;

	private JButton btnSeleccionarDia;
	private JLabel lblFecha;
	private JTextField txtFecha;
	private JLabel lblRangoSemana;

	private JLabel lblHorariosSemana;
	private JTable tablaHorariosSemanales;
	private DefaultTableModel modeloHorariosSemanales;

	private JLabel lblHorasSemanaTexto;
	private JLabel lblHorasSemana;
	private JButton btnAtras;
	private JScrollPane scrollPaneHorariosEmpleado;

	public VentanaHorarioSemanal(VentanaPrincipal vp) {
		this.vp = vp;
		this.ghs = (vp != null && vp.getGestionHorarioSemanal() != null)
				? vp.getGestionHorarioSemanal()
				: new GestionHorarioSemanal();
		this.dniEmpleadoActual = (vp != null && vp.getUsuarioAutenticado() != null)
				? vp.getUsuarioAutenticado().getDni()
				: null;
		inicializarComponentes();
	}

	public VentanaHorarioSemanal(String dniEmpleado) {
		this.ghs = new GestionHorarioSemanal();
		this.dniEmpleadoActual = dniEmpleado;
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Consultar Horarios Semanales - Empleados No Deportivos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 480);
		setMinimumSize(new Dimension(650, 400));

		// Panel principal con BorderLayout y márgenes
		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 15, 10, 15));
		setContentPane(contentPane);

		// 1. ZONA NORTE: Barra de selección de fecha y rango de la semana
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		panelNorte.add(getBtnSeleccionarDia());
		panelNorte.add(getLblFecha());
		panelNorte.add(getTxtFecha());
		panelNorte.add(getLblRangoSemana());
		contentPane.add(panelNorte, BorderLayout.NORTH);

		// 2. ZONA CENTRO: Tabla única ocupando todo el ancho disponible
		JPanel panelCentro = new JPanel(new BorderLayout(0, 5));
		panelCentro.add(getLblHorariosSemana(), BorderLayout.NORTH);
		panelCentro.add(getScrollPaneHorariosEmpleado(), BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);

		// 3. ZONA SUR: Resumen de horas a la izquierda y botón Atrás a la derecha
		JPanel panelSur = new JPanel(new BorderLayout());
		JPanel panelHoras = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelHoras.add(getLblHorasSemanaTexto());
		panelHoras.add(getLblHorasSemana());

		panelSur.add(panelHoras, BorderLayout.WEST);
		panelSur.add(getBtnAtras(), BorderLayout.EAST);
		contentPane.add(panelSur, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		inicializarTablasYEventos();
	}

	private JButton getBtnSeleccionarDia() {
		if (btnSeleccionarDia == null) {
			btnSeleccionarDia = new JButton("Seleccionar Día");
			btnSeleccionarDia.setPreferredSize(new Dimension(130, 28));
		}
		return btnSeleccionarDia;
	}

	private JLabel getLblFecha() {
		if (lblFecha == null) {
			lblFecha = new JLabel("Fecha:");
		}
		return lblFecha;
	}

	public JTextField getTxtFecha() {
		if (txtFecha == null) {
			txtFecha = new JTextField(LocalDate.now().toString());
			txtFecha.setEditable(false);
			txtFecha.setPreferredSize(new Dimension(90, 26));
			txtFecha.setColumns(10);
		}
		return txtFecha;
	}

	private JLabel getLblRangoSemana() {
		if (lblRangoSemana == null) {
			lblRangoSemana = new JLabel("Semana: -");
			lblRangoSemana.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblRangoSemana;
	}

	private JLabel getLblHorariosSemana() {
		if (lblHorariosSemana == null) {
			lblHorariosSemana = new JLabel("Horarios de la semana seleccionada:");
			lblHorariosSemana.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblHorariosSemana;
	}

	public JTable getTablaHorariosSemanales() {
		if (tablaHorariosSemanales == null) {
			tablaHorariosSemanales = new JTable();
		}
		return tablaHorariosSemanales;
	}

	private JLabel getLblHorasSemanaTexto() {
		if (lblHorasSemanaTexto == null) {
			lblHorasSemanaTexto = new JLabel("Horas semana:");
			lblHorasSemanaTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblHorasSemanaTexto;
	}

	public JLabel getLblHorasSemana() {
		if (lblHorasSemana == null) {
			lblHorasSemana = new JLabel("0 h 00 min (máx. 40 h)");
			lblHorasSemana.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblHorasSemana.setForeground(new Color(0, 102, 204));
		}
		return lblHorasSemana;
	}

	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.setPreferredSize(new Dimension(110, 30));
			btnAtras.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					if (vp != null) {
						vp.setVisible(true);
					}
				}
			});
		}
		return btnAtras;
	}

	private void inicializarTablasYEventos() {
		modeloHorariosSemanales = new DefaultTableModel(
				new Object[] { "Día", "Fecha", "Hora Inicio", "Hora Fin", "Duración" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaHorariosSemanales().setModel(modeloHorariosSemanales);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (vp != null) {
					vp.setVisible(true);
				}
			}
		});

		getBtnSeleccionarDia().addActionListener(e -> SwingUtil.exceptionWrapper(() -> abrirDialogoCalendario()));

		actualizarTextoRangoSemana();
		refrescarTablaSemana();
	}

	private void abrirDialogoCalendario() {
		JCalendar calendario = new JCalendar();
		int opcion = JOptionPane.showConfirmDialog(
				this,
				calendario,
				"Selecciona una fecha de la semana a consultar",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (opcion == JOptionPane.OK_OPTION) {
			Date fecha = calendario.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			getTxtFecha().setText(sdf.format(fecha));
			actualizarTextoRangoSemana();
			refrescarTablaSemana();
		}
	}

	private void actualizarTextoRangoSemana() {
		String fechaStr = getTxtFecha().getText();
		try {
			LocalDate lunes = ghs.obtenerLunesDeSemana(fechaStr);
			LocalDate domingo = ghs.obtenerDomingoDeSemana(fechaStr);
			getLblRangoSemana().setText("Semana: " + lunes.toString() + " al " + domingo.toString());
		} catch (Exception e) {
			getLblRangoSemana().setText("Semana: -");
		}
	}

	public void refrescarTablaSemana() {
		modeloHorariosSemanales.setRowCount(0);

		if (dniEmpleadoActual == null || dniEmpleadoActual.isBlank()) {
			getLblHorasSemana().setText("0 h 00 min (máx. 40 h)");
			return;
		}

		String fechaStr = getTxtFecha().getText();

		try {
			List<HorarioSemanal> turnos = ghs.obtenerHorariosDeSemana(dniEmpleadoActual, fechaStr);
			for (HorarioSemanal h : turnos) {
				modeloHorariosSemanales.addRow(new Object[] {
						h.getNombreDia(),
						h.getFecha(),
						h.getHoraInicio(),
						h.getHoraFin(),
						h.getDuracionFormateada()
				});
			}

			long totalMinutos = ghs.calcularMinutosTotalesSemana(turnos);
			long horas = totalMinutos / 60;
			long mins = totalMinutos % 60;
			getLblHorasSemana().setText(String.format("%d h %02d min (máx. 40 h)", horas, mins));
			GestionLogSesion.registrarAccion(
					"Consulta horario semanal (empleado=" + dniEmpleadoActual + ", fecha=" + fechaStr + ")");

		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private JScrollPane getScrollPaneHorariosEmpleado() {
		if (scrollPaneHorariosEmpleado == null) {
			scrollPaneHorariosEmpleado = new JScrollPane();
			scrollPaneHorariosEmpleado.setViewportView(getTablaHorariosSemanales());
		}
		return scrollPaneHorariosEmpleado;
	}
}
