package giis.demo.ui.horarios;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.empleado.EmpleadoNoDeportivo;
import giis.demo.model.horarios.GestionHorariosPeriodicos;
import giis.demo.model.horarios.HorarioPeriodico;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.UnexpectedException;
import javax.swing.JTable;

public class VentanaHorarioPeriodico extends JFrame {
	
	private VentanaPrincipal vp;
	private GestionHorariosPeriodicos ghp;

	private static final long serialVersionUID = 1L;
	private JComboBox cbDia;
	private JSpinner spHoraInicio;
	private JSpinner spMinInicio;
	private JSpinner spHoraFin;
	private JSpinner spMinFin;
	private JButton btnGuardar;
	private JLabel lblEmpleados;
	private JLabel lblDiaDeSemana;
	private JLabel lblHoraInicio;
	private JLabel lblMinutoInicio;
	private JLabel lblHoraFinal;
	private JLabel lblMinutoFinal;
	
	
	private JTable tablaEmpleadosNoDeportivos;
	private JTable tablaHorariosPeriodicos;
	private DefaultTableModel modeloEmpleados;
	private DefaultTableModel modeloHorarios;
	private JButton btnAtras;
	private JLabel lblHorariosDeEmpleado;

	/**
	 * Create the panel.
	 */
	public VentanaHorarioPeriodico(VentanaPrincipal vp) {
		this.vp = vp;
		this.ghp = vp.getGestionHorariosPeriodicos();
		
		setTitle("Añadir Horario Periódico");
		setBounds(100, 100, 700, 450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getCbDia());
		getContentPane().add(getSpHoraInicio());
		getContentPane().add(getSpMinInicio());
		getContentPane().add(getSpHoraFin());
		getContentPane().add(getSpMinFin());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getLblEmpleados());
		getContentPane().add(getLblDiaDeSemana());
		getContentPane().add(getLblHoraInicio());
		getContentPane().add(getLblMinutoInicio());
		getContentPane().add(getLblHoraFinal());
		getContentPane().add(getLblMinutoFinal());
		getContentPane().add(getTablaEmpleadosNoDeportivos());
		getContentPane().add(getTablaHorariosPeriodicos());
		getContentPane().add(getBtnAtras());
		getContentPane().add(getLblHorariosDeEmpleado());
		
		
		inicializarTablasYEventos();
	}
	public JComboBox getCbDia() {
		if (cbDia == null) {
			cbDia = new JComboBox();
			cbDia.setModel(new DefaultComboBoxModel<String>(new String[] {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"}));
			cbDia.setBounds(288, 66, 68, 18);
		}
		return cbDia;
	}
	public JSpinner getSpHoraInicio() {
		if (spHoraInicio == null) {
			spHoraInicio = new JSpinner();
			spHoraInicio.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spHoraInicio.setBounds(455, 41, 35, 26);
		}
		return spHoraInicio;
	}
	public JSpinner getSpMinInicio() {
		if (spMinInicio == null) {
			spMinInicio = new JSpinner();
			spMinInicio.setModel(new SpinnerNumberModel(0, 0, 59, 1));
			spMinInicio.setBounds(599, 41, 35, 26);
		}
		return spMinInicio;
	}
	public JSpinner getSpHoraFin() {
		if (spHoraFin == null) {
			spHoraFin = new JSpinner();
			spHoraFin.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spHoraFin.setBounds(455, 77, 35, 26);
		}
		return spHoraFin;
	}
	public JSpinner getSpMinFin() {
		if (spMinFin == null) {
			spMinFin = new JSpinner();
			spMinFin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
			spMinFin.setBounds(599, 79, 35, 26);
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
		
		String dniEmpleado = (String) getTablaEmpleadosNoDeportivos().getValueAt(filaSeleccionada, 0);
		
		int diaSemana = getCbDia().getSelectedIndex() + 1;
		
		int hIni = (Integer) getSpHoraInicio().getValue();
		int mIni = (Integer) getSpMinInicio().getValue();
		String horaInicio = String.format("%02d:%02d", hIni, mIni);
		
		int hFin = (Integer) getSpHoraFin().getValue();
		int mFin = (Integer) getSpMinFin().getValue();
		String horaFin = String.format("%02d:%02d", hFin, mFin);
	
		HorarioPeriodico nuevoHorario = new HorarioPeriodico(0, dniEmpleado, diaSemana, horaInicio, horaFin);
		
		try {
			ghp.validarYAnadirHorario(nuevoHorario);
			JOptionPane.showMessageDialog(this, "Horario periódico guardado con éxito.", "Operación completada", JOptionPane.INFORMATION_MESSAGE);
			
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
	private JLabel getLblDiaDeSemana() {
		if (lblDiaDeSemana == null) {
			lblDiaDeSemana = new JLabel("Dia de semana:");
			lblDiaDeSemana.setBounds(177, 69, 101, 12);
		}
		return lblDiaDeSemana;
	}
	private JLabel getLblHoraInicio() {
		if (lblHoraInicio == null) {
			lblHoraInicio = new JLabel("Hora inicio:");
			lblHoraInicio.setBounds(373, 55, 83, 12);
		}
		return lblHoraInicio;
	}
	private JLabel getLblMinutoInicio() {
		if (lblMinutoInicio == null) {
			lblMinutoInicio = new JLabel("Minuto inicio:");
			lblMinutoInicio.setBounds(513, 55, 76, 12);
		}
		return lblMinutoInicio;
	}
	private JLabel getLblHoraFinal() {
		if (lblHoraFinal == null) {
			lblHoraFinal = new JLabel("Hora final:");
			lblHoraFinal.setBounds(373, 83, 60, 12);
		}
		return lblHoraFinal;
	}
	private JLabel getLblMinutoFinal() {
		if (lblMinutoFinal == null) {
			lblMinutoFinal = new JLabel("Minuto final:");
			lblMinutoFinal.setBounds(513, 83, 76, 12);
		}
		return lblMinutoFinal;
	}
	
	
	public VentanaPrincipal getVp() {
		return this.vp;
	}
	private JTable getTablaEmpleadosNoDeportivos() {
		if (tablaEmpleadosNoDeportivos == null) {
			tablaEmpleadosNoDeportivos = new JTable();
			tablaEmpleadosNoDeportivos.setBounds(48, 133, 249, 200);
		}
		return tablaEmpleadosNoDeportivos;
	}
	private JTable getTablaHorariosPeriodicos() {
		if (tablaHorariosPeriodicos == null) {
			tablaHorariosPeriodicos = new JTable();
			tablaHorariosPeriodicos.setBounds(356, 133, 249, 200);
		}
		return tablaHorariosPeriodicos;
	}
	
	private void inicializarTablasYEventos() {
		// modelo de la tabla de Empleados
		modeloEmpleados = new DefaultTableModel(new Object[]{"DNI", "Nombre", "Apellidos", "Salario", "Posición"}, 0);
		getTablaEmpleadosNoDeportivos().setModel(modeloEmpleados);
		
		//modelo de la tabla de Horarios del empleado seleccionado
		modeloHorarios = new DefaultTableModel(new Object[]{"Día", "Hora Inicio", "Hora Fin"}, 0);
		getTablaHorariosPeriodicos().setModel(modeloHorarios);
		
		
		cargarEmpleados();
		
		// cuando se haga clic en un empleado de la tabla, se cargan sus horarios
		getTablaEmpleadosNoDeportivos().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && getTablaEmpleadosNoDeportivos().getSelectedRow() != -1) {
				String dniSeleccionado = (String) getTablaEmpleadosNoDeportivos().getValueAt(getTablaEmpleadosNoDeportivos().getSelectedRow(), 0);
				cargarHorarios(dniSeleccionado);
			}
		});

		// action listener usando el wrapper
		getBtnGuardar().addActionListener(e -> giis.demo.util.SwingUtil.exceptionWrapper(() -> guardarDatos()));
	}

	private void cargarEmpleados() {
		modeloEmpleados.setRowCount(0); //limpia la tabla
		try {
			List<EmpleadoNoDeportivo> empleados = ghp.obtenerEmpleadosNoDeportivos();
			for (EmpleadoNoDeportivo emp : empleados) {
				modeloEmpleados.addRow(new Object[]{
					emp.getDni(), emp.getNombre(), emp.getApellido(), emp.getSalario(), emp.getPosicion().name()
				});
			}
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private void cargarHorarios(String dni) {
		modeloHorarios.setRowCount(0); // Limpiar tabla
		try {
			List<HorarioPeriodico> horarios = ghp.obtenerHorariosDeEmpleado(dni);
			for (HorarioPeriodico h : horarios) {
				modeloHorarios.addRow(new Object[]{
					obtenerNombreDia(h.getDia_semana()), h.getHora_inicio(), h.getHora_fin()
				});
			}
		} catch (SQLException ex) {
			throw new UnexpectedException(ex);
		}
	}

	private String obtenerNombreDia(int dia) {
		String[] dias = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
		if(dia >= 1 && dia <= 7) {
			return dias[dia];
		}else {
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
			lblHorariosDeEmpleado = new JLabel("Horarios del empleado seleccionado:");
			lblHorariosDeEmpleado.setBounds(356, 113, 249, 12);
		}
		return lblHorariosDeEmpleado;
	}
}
