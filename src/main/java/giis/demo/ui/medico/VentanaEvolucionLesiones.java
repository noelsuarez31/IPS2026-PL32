package giis.demo.ui.medico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.medico.EvolucionLesion;
import giis.demo.model.medico.Lesion;
import giis.demo.util.ApplicationException;

public class VentanaEvolucionLesiones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelLesion;
	private JTextField tfLesion;
	private JPanel panelEvolucion;
	private JTable tableEvolucion;
	private DefaultTableModel modelo;
	private JPanel panelBoton;
	private JButton btCerrar;
	
	private VentanaVisualizacionLesiones vvl;
	private Lesion lesion;
	private CerrarVentana cv;

	public VentanaEvolucionLesiones(VentanaVisualizacionLesiones vvl, Lesion lesion) {
		
		this.vvl = vvl;
		this.lesion = lesion;
		this.cv = new CerrarVentana();

		setTitle("Portal Médico : Evolución de la lesión");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(5, 10));
		contentPane.add(getPanelLesion(), BorderLayout.NORTH);
		contentPane.add(getPanelEvolucion(), BorderLayout.CENTER);
		contentPane.add(getPanelBoton(), BorderLayout.SOUTH);
		
		this.addWindowListener(cv);
		setLocationRelativeTo(null);
		
		cargarEvolucion();
	}
	
	private JPanel getPanelLesion() {
		if (panelLesion == null) {
			panelLesion = new JPanel(new BorderLayout());
			panelLesion.setBorder(BorderFactory.createTitledBorder("Lesión"));
			panelLesion.add(getTfLesion(), BorderLayout.CENTER);
		}
		return panelLesion;
	}

	private JTextField getTfLesion() {
		if (tfLesion == null) {
			tfLesion = new JTextField(lesion.toString());
			tfLesion.setEditable(false);
		}
		return tfLesion;
	}

	private JPanel getPanelEvolucion() {
		if (panelEvolucion == null) {
			panelEvolucion = new JPanel(new BorderLayout());
			panelEvolucion.setBorder(BorderFactory.createTitledBorder("Evolución"));
			panelEvolucion.add(new JScrollPane(getTableEvolucion()), BorderLayout.CENTER);
		}
		return panelEvolucion;
	}

	private JTable getTableEvolucion() {
		if (tableEvolucion == null) {
			modelo = new DefaultTableModel(new String[] { "Fecha", "Valoración", "Actualización" }, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			};
			tableEvolucion = new JTable(modelo);
			tableEvolucion.getTableHeader().setReorderingAllowed(false);
		}
		return tableEvolucion;
	}

	private JPanel getPanelBoton() {
		if (panelBoton == null) {
			panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			panelBoton.add(getBtCerrar());
		}
		return panelBoton;
	}

	private JButton getBtCerrar() {
		if (btCerrar == null) {
			btCerrar = new JButton("Cerrar");
			btCerrar.setBackground(new Color(255, 128, 128));
			btCerrar.setMnemonic('C');
			btCerrar.addActionListener(e -> dispose());
		}
		return btCerrar;
	}

	private void cargarEvolucion() {
		try {
			List<EvolucionLesion> evolucion = vvl.getGestionLesiones().getEvolucion(lesion);
			for (EvolucionLesion ev : evolucion) {
				modelo.addRow(new Object[] { ev.getFecha().format(vvl.getFormatter()), ev.getValoracion(),
						ev.getActualizacion() });
			}
			if (evolucion.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Esta lesión aún no tiene seguimiento registrado.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (ApplicationException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class CerrarVentana extends WindowAdapter{
		
		@Override
		public void windowClosed(WindowEvent we) {
			VentanaEvolucionLesiones.this.vvl.setVisible(true);
		}
	}

}
