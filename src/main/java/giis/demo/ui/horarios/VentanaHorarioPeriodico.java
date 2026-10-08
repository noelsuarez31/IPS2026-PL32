package giis.demo.ui.horarios;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;

import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import giis.demo.model.horarios.GestionHorariosPeriodicos;
import giis.demo.model.horarios.HorarioPeriodico;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.util.UnexpectedException;

public class VentanaHorarioPeriodico extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtIdEmpleado;
	private JComboBox cbDia;
	private JSpinner spHoraInicio;
	private JSpinner spMinInicio;
	private JSpinner spHoraFin;
	private JSpinner spMinFin;
	private JButton btnGuardar;
	private JLabel lblNewLabel;
	private JLabel lblDiaDeSemana;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_1_1;
	private JLabel lblNewLabel_1_2;
	private JLabel lblNewLabel_1_2_1;
	
	private VentanaPrincipal vp;
	private GestionHorariosPeriodicos ghp;

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
		
		
		getContentPane().add(getTxtIdEmpleado());
		getContentPane().add(getCbDia());
		getContentPane().add(getSpHoraInicio());
		getContentPane().add(getSpMinInicio());
		getContentPane().add(getSpHoraFin());
		getContentPane().add(getSpMinFin());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getLblNewLabel());
		getContentPane().add(getLblDiaDeSemana());
		getContentPane().add(getLblNewLabel_1());
		getContentPane().add(getLblNewLabel_1_1());
		getContentPane().add(getLblNewLabel_1_2());
		getContentPane().add(getLblNewLabel_1_2_1());
	}
	public JTextField getTxtIdEmpleado() {
		if (txtIdEmpleado == null) {
			txtIdEmpleado = new JTextField();
			txtIdEmpleado.setBounds(10, 133, 96, 18);
			txtIdEmpleado.setColumns(10);
		}
		return txtIdEmpleado;
	}
	public JComboBox getCbDia() {
		if (cbDia == null) {
			cbDia = new JComboBox();
			cbDia.setModel(new DefaultComboBoxModel<String>(new String[] {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"}));
			cbDia.setBounds(136, 132, 68, 18);
		}
		return cbDia;
	}
	public JSpinner getSpHoraInicio() {
		if (spHoraInicio == null) {
			spHoraInicio = new JSpinner();
			spHoraInicio.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spHoraInicio.setBounds(281, 82, 29, 20);
		}
		return spHoraInicio;
	}
	public JSpinner getSpMinInicio() {
		if (spMinInicio == null) {
			spMinInicio = new JSpinner();
			spMinInicio.setModel(new SpinnerNumberModel(0, 0, 59, 1));
			spMinInicio.setBounds(379, 82, 29, 20);
		}
		return spMinInicio;
	}
	public JSpinner getSpHoraFin() {
		if (spHoraFin == null) {
			spHoraFin = new JSpinner();
			spHoraFin.setModel(new SpinnerNumberModel(0, 0, 23, 1));
			spHoraFin.setBounds(281, 181, 29, 20);
		}
		return spHoraFin;
	}
	public JSpinner getSpMinFin() {
		if (spMinFin == null) {
			spMinFin = new JSpinner();
			spMinFin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
			spMinFin.setBounds(379, 181, 29, 20);
		}
		return spMinFin;
	}
	public JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setBounds(281, 240, 127, 38);
		}
		return btnGuardar;
	}
	
	private void guardarDatos() {
		int idEmpleado; 
		
		try {
	        idEmpleado = Integer.parseInt(getTxtIdEmpleado().getText());
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "El ID del empleado debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
	        return; 
	    }
		
		// como el indice del combobox empieza en 0 añadimos 1
		int diaSemana = getCbDia().getSelectedIndex() + 1;
		
		// recoger y formatear las horas desde los Spinners
		int hIni = (Integer) getSpHoraInicio().getValue();
		int mIni = (Integer) getSpMinInicio().getValue();
		String horaInicio = String.format("%02d:%02d", hIni, mIni);
		
		int hFin = (Integer) getSpHoraFin().getValue();
		int mFin = (Integer) getSpMinFin().getValue();
		String horaFin = String.format("%02d:%02d", hFin, mFin);
		
		// creamos dto
		HorarioPeriodico nuevoHorario = new HorarioPeriodico(0, idEmpleado, diaSemana, horaInicio, horaFin);
		
		// vvalidamos y añadimos si es correcto
		try {
			ghp.validarYAnadirHorario(nuevoHorario);
			JOptionPane.showMessageDialog(this, "Horario periódico guardado con éxito.", "Operación completada", JOptionPane.INFORMATION_MESSAGE);
			getVp().setVisible(true);
			setVisible(false);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
		
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Empleado");
			lblNewLabel.setBounds(10, 111, 44, 12);
		}
		return lblNewLabel;
	}
	private JLabel getLblDiaDeSemana() {
		if (lblDiaDeSemana == null) {
			lblDiaDeSemana = new JLabel("Dia de semana");
			lblDiaDeSemana.setBounds(136, 111, 68, 12);
		}
		return lblDiaDeSemana;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("Hora inicio");
			lblNewLabel_1.setBounds(228, 85, 60, 12);
		}
		return lblNewLabel_1;
	}
	private JLabel getLblNewLabel_1_1() {
		if (lblNewLabel_1_1 == null) {
			lblNewLabel_1_1 = new JLabel("Min inicio");
			lblNewLabel_1_1.setBounds(320, 85, 60, 12);
		}
		return lblNewLabel_1_1;
	}
	private JLabel getLblNewLabel_1_2() {
		if (lblNewLabel_1_2 == null) {
			lblNewLabel_1_2 = new JLabel("Hora fin");
			lblNewLabel_1_2.setBounds(228, 184, 60, 12);
		}
		return lblNewLabel_1_2;
	}
	private JLabel getLblNewLabel_1_2_1() {
		if (lblNewLabel_1_2_1 == null) {
			lblNewLabel_1_2_1 = new JLabel("Min fin");
			lblNewLabel_1_2_1.setBounds(320, 184, 60, 12);
		}
		return lblNewLabel_1_2_1;
	}
	
	
	public VentanaPrincipal getVp() {
		return this.vp;
	}
}
