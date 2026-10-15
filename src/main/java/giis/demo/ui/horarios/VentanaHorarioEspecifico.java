package giis.demo.ui.horarios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.horarios.GestionHorariosEspecificos;
import giis.demo.model.horarios.HorarioEspecifico;
import giis.demo.model.horarios.HorarioPeriodico;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.SwingUtil;
import giis.demo.util.UnexpectedException;
import javax.swing.JTextField;

public class VentanaHorarioEspecifico extends JFrame {
	
	private VentanaPrincipal vp;
	private GestionHorariosEspecificos ghe;

	private static final long serialVersionUID = 1L;
	private static final String SIN_FECHA = "Ninguno";
	private JSpinner spHoraInicio;
	private JSpinner spMinInicio;
	private JSpinner spHoraFin;
	private JSpinner spMinFin;
	private JButton btnGuardar;
	private JLabel lblEmpleados;
	private JLabel lblDiaEspecifico;
	private JLabel lblHoraInicio;
	private JLabel lblMinutoInicio;
	private JLabel lblHoraFinal;
	private JLabel lblMinutoFinal;
	
	
	private JTable tablaEmpleadosNoDeportivos;
	private JTable tablaHorariosPeriodicos;
	private DefaultTableModel modeloEmpleados;
	private DefaultTableModel modeloHorariosPeriodicos;
	private DefaultTableModel modeloHorariosEspecificos;
	private JButton btnAtras;
	private JLabel lblHorariosDeEmpleado;
	private JScrollPane scrollPaneEmpleadosNoDep;
	private JScrollPane scrollPaneHorarioPeriodico;
	private JButton btnSeleccionarDia;
	private JTextField TxtFecha;
	private JTable tablaHorariosEspecificos;
	private JScrollPane scrollPaneHorarioEspecifico;
	private JLabel lblHorariosEspecificos;

	/**
	 * Create the panel.
	 */
	public VentanaHorarioEspecifico(VentanaPrincipal vp) {
		this.vp = vp;
		this.ghe = vp.getGestionHorariosEspecificos();
		
		setTitle("Añadir Horario de Día Específico");
		setBounds(100, 100, 700, 450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getSpHoraInicio());
		getContentPane().add(getSpMinInicio());
		getContentPane().add(getSpHoraFin());
		getContentPane().add(getSpMinFin());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getLblEmpleados());
		getContentPane().add(getLblDiaEspecifico());
		getContentPane().add(getLblHoraInicio());
		getContentPane().add(getLblMinutoInicio());
		getContentPane().add(getLblHoraFinal());
		getContentPane().add(getLblMinutoFinal());
		getContentPane().add(getScrollPaneEmpleadosNoDep());
		getContentPane().add(getScrollPaneHorarioPeriodico());
		getContentPane().add(getBtnAtras());
		getContentPane().add(getLblHorariosDeEmpleado());
		getContentPane().add(getBtnSeleccionarDia());
		getContentPane().add(getTxtFecha());
		getContentPane().add(getLblHorariosEspecificos());
		getContentPane().add(getScrollPaneHorarioEspecifico());
		
		inicializarTablasYEventos();
	}
	public JSpinner getSpHoraInicio() {
		if (spHoraInicio == null) {
			spHoraInicio = new JSpinner();
			spHoraInicio.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spHoraInicio.setBounds(446, 38, 46, 32);
		}
		return spHoraInicio;
	}
	public JSpinner getSpMinInicio() {
		if (spMinInicio == null) {
			spMinInicio = new JSpinner();
			spMinInicio.setModel(new SpinnerNumberModel(0, 0, 59, 1));
			spMinInicio.setBounds(574, 38, 46, 32);
		}
		return spMinInicio;
	}
	public JSpinner getSpHoraFin() {
		if (spHoraFin == null) {
			spHoraFin = new JSpinner();
			spHoraFin.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spHoraFin.setBounds(446, 76, 46, 32);
		}
		return spHoraFin;
	}
	public JSpinner getSpMinFin() {
		if (spMinFin == null) {
			spMinFin = new JSpinner();
			spMinFin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
			spMinFin.setBounds(574, 76, 46, 32);
		}
		return spMinFin;
	}
	public JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setBounds(549, 365, 127, 38);
		}
		return btnGuardar;
	}
	
	private void guardarDatos() {
		int filaSeleccionada = getTablaEmpleadosNoDeportivos().getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return; 
		}
		
		String fecha = getTxtFecha().getText();
		if (fecha.equals(SIN_FECHA)) {
			JOptionPane.showMessageDialog(this, "Por favor, selecciona un día con el calendario.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String dniEmpleado = (String) getTablaEmpleadosNoDeportivos().getValueAt(filaSeleccionada, 0);
		
		int hIni = (Integer) getSpHoraInicio().getValue();
		int mIni = (Integer) getSpMinInicio().getValue();
		String horaInicio = String.format("%02d:%02d", hIni, mIni);
		
		int hFin = (Integer) getSpHoraFin().getValue();
		int mFin = (Integer) getSpMinFin().getValue();
		String horaFin = String.format("%02d:%02d", hFin, mFin);
		
		HorarioEspecifico nuevoHorario = new HorarioEspecifico(0, dniEmpleado, fecha, horaInicio, horaFin);
		
		try {
			ghe.validarYAnadirHorario(nuevoHorario);
			JOptionPane.showMessageDialog(this, "Horario específico guardado con éxito.", "Operación completada", JOptionPane.INFORMATION_MESSAGE);
			
			cargarHorarios(dniEmpleado);
			
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
	}
	
	private JLabel getLblEmpleados() {
		if (lblEmpleados == null) {
			lblEmpleados = new JLabel("Empleados:");
			lblEmpleados.setBounds(48, 111, 77, 12);
		}
		return lblEmpleados;
	}
	private JLabel getLblDiaEspecifico() {
		if (lblDiaEspecifico == null) {
			lblDiaEspecifico = new JLabel("Dia seleccionado:");
			lblDiaEspecifico.setBounds(178, 69, 101, 12);
		}
		return lblDiaEspecifico;
	}
	private JLabel getLblHoraInicio() {
		if (lblHoraInicio == null) {
			lblHoraInicio = new JLabel("Hora inicio:");
			lblHoraInicio.setBounds(380, 47, 83, 12);
		}
		return lblHoraInicio;
	}
	private JLabel getLblMinutoInicio() {
		if (lblMinutoInicio == null) {
			lblMinutoInicio = new JLabel("Minuto inicio:");
			lblMinutoInicio.setBounds(502, 47, 76, 12);
		}
		return lblMinutoInicio;
	}
	private JLabel getLblHoraFinal() {
		if (lblHoraFinal == null) {
			lblHoraFinal = new JLabel("Hora final:");
			lblHoraFinal.setBounds(380, 85, 60, 12);
		}
		return lblHoraFinal;
	}
	private JLabel getLblMinutoFinal() {
		if (lblMinutoFinal == null) {
			lblMinutoFinal = new JLabel("Minuto final:");
			lblMinutoFinal.setBounds(502, 88, 76, 12);
		}
		return lblMinutoFinal;
	}
	
	
	public VentanaPrincipal getVp() {
		return this.vp;
	}
	private JTable getTablaEmpleadosNoDeportivos() {
		if (tablaEmpleadosNoDeportivos == null) {
			tablaEmpleadosNoDeportivos = new JTable();
		}
		return tablaEmpleadosNoDeportivos;
	}
	private JTable getTablaHorariosPeriodicos() {
		if (tablaHorariosPeriodicos == null) {
			tablaHorariosPeriodicos = new JTable();
		}
		return tablaHorariosPeriodicos;
	}
	
	private void inicializarTablasYEventos() {
		// modelo de la tabla de Empleados
		modeloEmpleados = new DefaultTableModel(new Object[]{"DNI", "Nombre", "Apellidos", "Salario", "Posición"}, 0);
		getTablaEmpleadosNoDeportivos().setModel(modeloEmpleados);
		
		// modelo de la tabla de horarios periódicos del empleado seleccionado
		modeloHorariosPeriodicos = new DefaultTableModel(new Object[]{"Día", "Hora Inicio", "Hora Fin"}, 0);
		getTablaHorariosPeriodicos().setModel(modeloHorariosPeriodicos);
		
		// modelo de la tabla de horarios específicos del empleado seleccionado
		modeloHorariosEspecificos = new DefaultTableModel(new Object[]{"Fecha", "Hora Inicio", "Hora Fin"}, 0);
		getTablaHorariosEspecificos().setModel(modeloHorariosEspecificos);
		
		cargarEmpleados();
		
		// al seleccionar un empleado se cargan sus horarios periódicos y específicos
		getTablaEmpleadosNoDeportivos().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				refrescarHorariosDelEmpleado();
			}
		});

		// action listener usando el wrapper
		getBtnGuardar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> guardarDatos()));
		
		getBtnSeleccionarDia().addActionListener(e -> SwingUtil.exceptionWrapper(() -> abrirDialogoCalendario()));
	}

	private void abrirDialogoCalendario() {
		JCalendar calendario = new JCalendar();
		
		// Mostramos el calendario dentro de un JOptionPane (ventana emergente modal)
		int opcion = JOptionPane.showConfirmDialog(
				this, 
				calendario, 
				"Selecciona una fecha", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE
		);
		
		if (opcion == JOptionPane.OK_OPTION) {
			// Date -> "yyyy-MM-dd"
			Date fechaSeleccionada = calendario.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			getTxtFecha().setText(sdf.format(fechaSeleccionada));
		}
	}
	
	private void cargarEmpleados() {
		modeloEmpleados.setRowCount(0); //limpia la tabla
		try {
			List<EmpleadoNoDeportivo> empleados = ghe.obtenerEmpleadosNoDeportivos();
			for (EmpleadoNoDeportivo emp : empleados) {
				modeloEmpleados.addRow(new Object[]{
					emp.getDni(), emp.getNombre(), emp.getApellido(), emp.getSalario(), emp.getPosicion().name()
				});
			}
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	/**
	 * Recarga las dos tablas de horarios con los del empleado seleccionado.
	 * Si no hay ningún empleado seleccionado, las deja vacías.
	 */
	private void refrescarHorariosDelEmpleado() {
		int fila = getTablaEmpleadosNoDeportivos().getSelectedRow();
		
		if (fila == -1) {
			modeloHorariosPeriodicos.setRowCount(0);
			modeloHorariosEspecificos.setRowCount(0);
			return;
		}
		String dni = (String) getTablaEmpleadosNoDeportivos().getValueAt(fila, 0);
		cargarHorarios(dni);
	}

	private void cargarHorarios(String dni) {
		modeloHorariosPeriodicos.setRowCount(0); // Limpiar tablas
		modeloHorariosEspecificos.setRowCount(0);
		try {
			List<HorarioPeriodico> periodicos = ghe.obtenerHorariosPeriodicosDeEmpleado(dni);
			for (HorarioPeriodico h : periodicos) {
				modeloHorariosPeriodicos.addRow(new Object[]{
					obtenerNombreDia(h.getDia_semana()), h.getHora_inicio(), h.getHora_fin()
				});
			}
			
			List<HorarioEspecifico> especificos = ghe.obtenerTodosLosHorariosEspecificosDeEmpleado(dni);
			for (HorarioEspecifico h : especificos) {
				modeloHorariosEspecificos.addRow(new Object[]{
					h.getFecha(), h.getHora_inicio(), h.getHora_fin()
				});
			}
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private String obtenerNombreDia(int dia) {
		String[] dias = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
		if (dia >= 1 && dia <= 7) {
			return dias[dia];
		} else {
			return "desconocido";
		}
	}

	
	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atrás");
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					vp.setVisible(true);
				}
			});
			btnAtras.setBounds(412, 365, 127, 38);
		}
		return btnAtras;
	}
	private JLabel getLblHorariosDeEmpleado() {
		if (lblHorariosDeEmpleado == null) {
			lblHorariosDeEmpleado = new JLabel("Horario periódico del empleado:");
			lblHorariosDeEmpleado.setBounds(356, 111, 249, 14);
		}
		return lblHorariosDeEmpleado;
	}
	private JScrollPane getScrollPaneEmpleadosNoDep() {
		if (scrollPaneEmpleadosNoDep == null) {
			scrollPaneEmpleadosNoDep = new JScrollPane();
			scrollPaneEmpleadosNoDep.setBounds(48, 133, 262, 200);
			scrollPaneEmpleadosNoDep.setViewportView(getTablaEmpleadosNoDeportivos());
		}
		return scrollPaneEmpleadosNoDep;
	}
	private JScrollPane getScrollPaneHorarioPeriodico() {
		if (scrollPaneHorarioPeriodico == null) {
			scrollPaneHorarioPeriodico = new JScrollPane();
			scrollPaneHorarioPeriodico.setBounds(356, 128, 300, 90);
			scrollPaneHorarioPeriodico.setViewportView(getTablaHorariosPeriodicos());
		}
		return scrollPaneHorarioPeriodico;
	}
	
	private JButton getBtnSeleccionarDia() {
		if (btnSeleccionarDia == null) {
			btnSeleccionarDia = new JButton("SeleccionarDia");
			btnSeleccionarDia.setBounds(48, 60, 115, 31);
		}
		return btnSeleccionarDia;
	}
	private JTextField getTxtFecha() {
		if (TxtFecha == null) {
			TxtFecha = new JTextField();
			TxtFecha.setText(SIN_FECHA);
			TxtFecha.setEditable(false);
			TxtFecha.setBounds(277, 60, 77, 31);
			TxtFecha.setColumns(10);
		}
		return TxtFecha;
	}
	
	private JTable getTablaHorariosEspecificos() {
		if (tablaHorariosEspecificos == null) {
			tablaHorariosEspecificos = new JTable();
		}
		return tablaHorariosEspecificos;
	}
	private JLabel getLblHorariosEspecificos() {
		if (lblHorariosEspecificos == null) {
			lblHorariosEspecificos = new JLabel("Horarios específicos del empleado:");
			lblHorariosEspecificos.setBounds(356, 224, 249, 14);
		}
		return lblHorariosEspecificos;
	}
	private JScrollPane getScrollPaneHorarioEspecifico() {
		if (scrollPaneHorarioEspecifico == null) {
			scrollPaneHorarioEspecifico = new JScrollPane();
			scrollPaneHorarioEspecifico.setBounds(356, 240, 300, 100);
			scrollPaneHorarioEspecifico.setViewportView(getTablaHorariosEspecificos());
		}
		return scrollPaneHorarioEspecifico;
	}
}
