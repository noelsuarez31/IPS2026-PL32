package giis.demo.ui.horarios;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.horarios.GestionHorarioSemanal;
import giis.demo.model.horarios.HorarioSemanal;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.SwingUtil;
import giis.demo.util.UnexpectedException;

public class VentanaHorarioSemanal extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vp;
	private GestionHorarioSemanal ghs;

	private JLabel lblEmpleados;
	private JTable tablaEmpleadosNoDeportivos;
	private DefaultTableModel modeloEmpleados;

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
	private JScrollPane scrollPaneEmpleadosNoDeportivos;
	private JScrollPane scrollPaneHorariosEmpleado;

	public VentanaHorarioSemanal(VentanaPrincipal vp) {
		this.vp = vp;
		this.ghs = vp.getGestionHorarioSemanal();

		setTitle("Consultar Horarios Semanales - Empleados No Deportivos");
		setBounds(100, 100, 720, 460);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		getContentPane().add(getLblEmpleados());
		getContentPane().add(getScrollPaneEmpleadosNoDeportivos());
		getContentPane().add(getBtnSeleccionarDia());
		getContentPane().add(getLblFecha());
		getContentPane().add(getTxtFecha());
		getContentPane().add(getLblRangoSemana());
		getContentPane().add(getLblHorariosSemana());
		getContentPane().add(getScrollPaneHorariosEmpleado());
		getContentPane().add(getLblHorasSemanaTexto());
		getContentPane().add(getLblHorasSemana());
		getContentPane().add(getBtnAtras());

		inicializarTablasYEventos();
	}

	private JLabel getLblEmpleados() {
		if (lblEmpleados == null) {
			lblEmpleados = new JLabel("Empleados:");
			lblEmpleados.setBounds(30, 20, 120, 14);
		}
		return lblEmpleados;
	}

	private JTable getTablaEmpleadosNoDeportivos() {
		if (tablaEmpleadosNoDeportivos == null) {
			tablaEmpleadosNoDeportivos = new JTable();
		}
		return tablaEmpleadosNoDeportivos;
	}

	

	private JButton getBtnSeleccionarDia() {
		if (btnSeleccionarDia == null) {
			btnSeleccionarDia = new JButton("Seleccionar Día");
			btnSeleccionarDia.setBounds(310, 20, 130, 28);
		}
		return btnSeleccionarDia;
	}

	private JLabel getLblFecha() {
		if (lblFecha == null) {
			lblFecha = new JLabel("Fecha:");
			lblFecha.setBounds(455, 25, 45, 14);
		}
		return lblFecha;
	}

	private JTextField getTxtFecha() {
		if (txtFecha == null) {
			txtFecha = new JTextField();
			txtFecha.setText(LocalDate.now().toString());
			txtFecha.setEditable(false);
			txtFecha.setBounds(505, 20, 85, 28);
			txtFecha.setColumns(10);
		}
		return txtFecha;
	}

	private JLabel getLblRangoSemana() {
		if (lblRangoSemana == null) {
			lblRangoSemana = new JLabel("Semana: -");
			lblRangoSemana.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblRangoSemana.setBounds(310, 56, 375, 18);
		}
		return lblRangoSemana;
	}

	private JLabel getLblHorariosSemana() {
		if (lblHorariosSemana == null) {
			lblHorariosSemana = new JLabel("Horarios de la semana seleccionada:");
			lblHorariosSemana.setBounds(310, 82, 250, 14);
		}
		return lblHorariosSemana;
	}

	private JTable getTablaHorariosSemanales() {
		if (tablaHorariosSemanales == null) {
			tablaHorariosSemanales = new JTable();
		}
		return tablaHorariosSemanales;
	}


	private JLabel getLblHorasSemanaTexto() {
		if (lblHorasSemanaTexto == null) {
			lblHorasSemanaTexto = new JLabel("Horas semana:");
			lblHorasSemanaTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblHorasSemanaTexto.setBounds(30, 360, 100, 20);
		}
		return lblHorasSemanaTexto;
	}

	private JLabel getLblHorasSemana() {
		if (lblHorasSemana == null) {
			lblHorasSemana = new JLabel("0 h 00 min (máx. 40 h)");
			lblHorasSemana.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblHorasSemana.setForeground(new Color(0, 0, 0));
			lblHorasSemana.setBounds(135, 360, 220, 20);
		}
		return lblHorasSemana;
	}

	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.setBounds(565, 355, 120, 32);
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					vp.setVisible(true);
				}
			});
		}
		return btnAtras;
	}

	private void inicializarTablasYEventos() {
		// Modelo de la tabla de empleados (no editable)
		modeloEmpleados = new DefaultTableModel(
				new Object[] { "DNI", "Nombre", "Apellidos",}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaEmpleadosNoDeportivos().setModel(modeloEmpleados);

		// Modelo de la tabla de horarios de la semana (no editable)
		modeloHorariosSemanales = new DefaultTableModel(
				new Object[] { "Día", "Fecha", "Hora Inicio", "Hora Fin", "Duración",}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		getTablaHorariosSemanales().setModel(modeloHorariosSemanales);

		// Evento al seleccionar un empleado
		getTablaEmpleadosNoDeportivos().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				refrescarTablaSemana();
			}
		});

		// Evento para seleccionar fecha con JCalendar
		getBtnSeleccionarDia().addActionListener(e -> SwingUtil.exceptionWrapper(() -> abrirDialogoCalendario()));

		cargarEmpleados();
		actualizarTextoRangoSemana();
	}

	private void abrirDialogoCalendario() {
		JCalendar calendario = new JCalendar();
		int opcion = JOptionPane.showConfirmDialog(
				this,
				calendario,
				"Selecciona una fecha de la semana a consultar",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (opcion == JOptionPane.OK_OPTION) {
			Date fecha = calendario.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			getTxtFecha().setText(sdf.format(fecha));
			actualizarTextoRangoSemana();
			refrescarTablaSemana();
		}
	}

	private void cargarEmpleados() {
		modeloEmpleados.setRowCount(0);
		try {
			List<EmpleadoNoDeportivo> empleados = ghs.obtenerEmpleadosNoDeportivos();
			for (EmpleadoNoDeportivo emp : empleados) {
				modeloEmpleados.addRow(new Object[] {
					emp.getDni(), emp.getNombre(), emp.getApellido()
				});
			}
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
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

	private void refrescarTablaSemana() {
		modeloHorariosSemanales.setRowCount(0);
		int fila = getTablaEmpleadosNoDeportivos().getSelectedRow();

		if (fila == -1) {
			getLblHorasSemana().setText("0 h 00 min (máx. 40 h)");
			return;
		}

		String dni = (String) getTablaEmpleadosNoDeportivos().getValueAt(fila, 0);
		String fechaStr = getTxtFecha().getText();

		try {
			List<HorarioSemanal> turnos = ghs.obtenerHorariosDeSemana(dni, fechaStr);
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

		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}
	private JScrollPane getScrollPaneEmpleadosNoDeportivos() {
		if (scrollPaneEmpleadosNoDeportivos == null) {
			scrollPaneEmpleadosNoDeportivos = new JScrollPane();
			scrollPaneEmpleadosNoDeportivos.setBounds(30, 42, 260, 290);
			scrollPaneEmpleadosNoDeportivos.setViewportView(getTablaEmpleadosNoDeportivos());
		}
		return scrollPaneEmpleadosNoDeportivos;
	}
	private JScrollPane getScrollPaneHorariosEmpleado() {
		if (scrollPaneHorariosEmpleado == null) {
			scrollPaneHorariosEmpleado = new JScrollPane();
			scrollPaneHorariosEmpleado.setBounds(310, 102, 375, 230);
			scrollPaneHorariosEmpleado.setViewportView(getTablaHorariosSemanales());
		}
		return scrollPaneHorariosEmpleado;
	}
}