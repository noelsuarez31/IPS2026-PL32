package giis.demo.ui.medico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import giis.demo.model.medico.EvolucionLesion;
import giis.demo.model.medico.GestionLesiones;
import giis.demo.model.medico.Lesion;
import giis.demo.util.ApplicationException;

public class VentanaRegistrarEvolucion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelLesion;
	private JPanel panelRegistro;
	private JPanel panelEvolucionActual;
	private JPanel panelNuevaEntrada;

	private JLabel lbDatosLesion;

	private JScrollPane spTablaEvolucion;
	private JTable tablaEvolucion;
	private DefaultTableModel modeloEvolucion;

	private JLabel lbFecha;
	private JDateChooser dcFecha;
	private JLabel lbValoracion;
	private JComboBox<String> cbValoracion;
	private JLabel lblActualizacion;
	private JScrollPane spActualizacion;
	private JTextArea taActualizacion;
	private JLabel lbEstado;
	private JComboBox<String> cbEstado;
	private JCheckBox cbAlta;
	private JLabel lbFechaAlta;
	private JDateChooser dcFechaAlta;

	private JPanel panelOpciones;
	private JPanel panelBotones;
	private JButton btGuardar;
	private JButton btCancelar;

	private VentanaVisualizacionLesiones vvl;
	private Lesion lesion;
	private GestionLesiones gestionLesiones;

	public VentanaRegistrarEvolucion(VentanaVisualizacionLesiones vvl, Lesion lesion) {
		this.vvl = vvl;
		this.lesion = lesion;
		this.gestionLesiones = vvl.getGestionLesiones();

		setTitle("Registrar Evolución");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 729, 560);
		setMinimumSize(new Dimension(620, 520));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 5));

		contentPane.add(getPanelLesion(), BorderLayout.NORTH);
		contentPane.add(getPanelRegistro(), BorderLayout.CENTER);
		contentPane.add(getPanelOpciones(), BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				volver();
			}
		});

		cargarDatosLesion();
		cargarEvolucionPrevia();
	}

	private JPanel getPanelLesion() {
		if (panelLesion == null) {
			panelLesion = new JPanel();
			panelLesion.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
					"Lesión:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLesion.setPreferredSize(new Dimension(0, 55));
			panelLesion.add(getLbDatosLesion());
		}
		return panelLesion;
	}

	private JLabel getLbDatosLesion() {
		if (lbDatosLesion == null) {
			lbDatosLesion = new JLabel("Información de la lesión no disponible");
			lbDatosLesion.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lbDatosLesion;
	}

	private JPanel getPanelRegistro() {
		if (panelRegistro == null) {
			panelRegistro = new JPanel();
			panelRegistro.setLayout(new BorderLayout(0, 5));
			panelRegistro.add(getPanelEvolucionActual(), BorderLayout.NORTH);
			panelRegistro.add(getPanelNuevaEntrada(), BorderLayout.CENTER);
		}
		return panelRegistro;
	}

	private JPanel getPanelEvolucionActual() {
		if (panelEvolucionActual == null) {
			panelEvolucionActual = new JPanel(new BorderLayout());
			panelEvolucionActual.setBorder(new TitledBorder(null, "Evolución hasta ahora:", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panelEvolucionActual.setPreferredSize(new Dimension(0, 130));
			panelEvolucionActual.add(getSpTablaEvolucion(), BorderLayout.CENTER);
		}
		return panelEvolucionActual;
	}

	private JScrollPane getSpTablaEvolucion() {
		if (spTablaEvolucion == null) {
			spTablaEvolucion = new JScrollPane();
			spTablaEvolucion.setViewportView(getTablaEvolucion());
		}
		return spTablaEvolucion;
	}

	private JTable getTablaEvolucion() {
		if (tablaEvolucion == null) {
			modeloEvolucion = new DefaultTableModel(new Object[][] {},
					new String[] { "Fecha", "Valoración", "Actualización" }) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			tablaEvolucion = new JTable(modeloEvolucion);
			tablaEvolucion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaEvolucion.getColumnModel().getColumn(0).setPreferredWidth(90);
			tablaEvolucion.getColumnModel().getColumn(1).setPreferredWidth(100);
			tablaEvolucion.getColumnModel().getColumn(2).setPreferredWidth(350);
		}
		return tablaEvolucion;
	}

	private JPanel getPanelNuevaEntrada() {
		if (panelNuevaEntrada == null) {
			panelNuevaEntrada = new JPanel();
			panelNuevaEntrada.setBorder(
					new TitledBorder(null, "Nueva Entrada: ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelNuevaEntrada.setLayout(null);

			panelNuevaEntrada.add(getLbFecha());
			panelNuevaEntrada.add(getDcFecha());

			panelNuevaEntrada.add(getLbValoracion());
			panelNuevaEntrada.add(getCbValoracion());

			panelNuevaEntrada.add(getLblActualizacion());
			panelNuevaEntrada.add(getSpActualizacion());

			panelNuevaEntrada.add(getLbEstado());
			panelNuevaEntrada.add(getCbEstado());

			panelNuevaEntrada.add(getCbAlta());
			panelNuevaEntrada.add(getLbFechaAlta());
			panelNuevaEntrada.add(getDcFechaAlta());
		}
		return panelNuevaEntrada;
	}

	private JLabel getLbFecha() {
		if (lbFecha == null) {
			lbFecha = new JLabel("Fecha:");
			lbFecha.setBounds(15, 25, 60, 20);
		}
		return lbFecha;
	}

	private JDateChooser getDcFecha() {
		if (dcFecha == null) {
			dcFecha = new JDateChooser();
			dcFecha.setBounds(110, 25, 140, 22);
			dcFecha.setDate(new Date());
		}
		return dcFecha;
	}

	private JLabel getLbValoracion() {
		if (lbValoracion == null) {
			lbValoracion = new JLabel("Valoración:");
			lbValoracion.setBounds(15, 60, 80, 20);
		}
		return lbValoracion;
	}

	private JComboBox<String> getCbValoracion() {
		if (cbValoracion == null) {
			cbValoracion = new JComboBox<String>();
			cbValoracion.setModel(new DefaultComboBoxModel<String>(new String[] { "Leve", "Moderada", "Grave" }));
			cbValoracion.setBounds(110, 60, 140, 22);
		}
		return cbValoracion;
	}

	private JLabel getLblActualizacion() {
		if (lblActualizacion == null) {
			lblActualizacion = new JLabel("Actualización:");
			lblActualizacion.setBounds(15, 135, 90, 20);
		}
		return lblActualizacion;
	}

	private JScrollPane getSpActualizacion() {
		if (spActualizacion == null) {
			spActualizacion = new JScrollPane();
			spActualizacion.setBounds(110, 135, 460, 60);
			spActualizacion.setViewportView(getTaActualizacion());
		}
		return spActualizacion;
	}

	private JTextArea getTaActualizacion() {
		if (taActualizacion == null) {
			taActualizacion = new JTextArea();
			taActualizacion.setLineWrap(true);
			taActualizacion.setWrapStyleWord(true);
		}
		return taActualizacion;
	}

	private JLabel getLbEstado() {
		if (lbEstado == null) {
			lbEstado = new JLabel("Estado:");
			lbEstado.setBounds(15, 102, 80, 20);
		}
		return lbEstado;
	}

	private JComboBox<String> getCbEstado() {
		if (cbEstado == null) {
			cbEstado = new JComboBox<String>();
			cbEstado.setModel(new DefaultComboBoxModel<String>(
					new String[] { "En tratamiento", "En rehabilitación", "Pendiente de pruebas" }));
			cbEstado.setBounds(110, 102, 170, 22);
		}
		return cbEstado;
	}

	private JCheckBox getCbAlta() {
		if (cbAlta == null) {
			cbAlta = new JCheckBox("Dar de alta al jugador");
			cbAlta.setBounds(15, 202, 170, 23);
			cbAlta.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					boolean seleccionado = cbAlta.isSelected();
					getDcFechaAlta().setEnabled(seleccionado);
					
					if (seleccionado) {
						
						getDcFechaAlta().setDate(new Date());
						getCbEstado().setEnabled(false);
						
						getCbValoracion().setSelectedItem("Leve");
						getCbValoracion().setEnabled(false);
						
					} else {
						
						getDcFechaAlta().setDate(null);
						getCbEstado().setEnabled(true);
						
						getCbValoracion().setEnabled(true);
					}
				}
			});
		}
		return cbAlta;
	}

	private JLabel getLbFechaAlta() {
		if (lbFechaAlta == null) {
			lbFechaAlta = new JLabel("Fecha del alta:");
			lbFechaAlta.setBounds(310, 204, 90, 20);
		}
		return lbFechaAlta;
	}

	private JDateChooser getDcFechaAlta() {
		if (dcFechaAlta == null) {
			dcFechaAlta = new JDateChooser();
			dcFechaAlta.setBounds(410, 203, 140, 22);
			dcFechaAlta.setEnabled(false);
		}
		return dcFechaAlta;
	}

	private JPanel getPanelOpciones() {
		if (panelOpciones == null) {
			panelOpciones = new JPanel(new BorderLayout(0, 0));
			panelOpciones.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panelOpciones;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel(new GridLayout(1, 2, 6, 0));
			panelBotones.add(getBtGuardar());
			panelBotones.add(getBtCancelar());
		}
		return panelBotones;
	}

	private JButton getBtGuardar() {
		if (btGuardar == null) {
			btGuardar = new JButton("Guardar");
			btGuardar.setBackground(new Color(128, 255, 128));
			btGuardar.setMnemonic('G');
			btGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guardarEvolucion();
				}
			});
		}
		return btGuardar;
	}

	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.setMnemonic('C');
			btCancelar.setBackground(new Color(255, 128, 128));
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					volver();
				}
			});
		}
		return btCancelar;
	}

	private void cargarDatosLesion() {

		if (lesion != null) {

			String texto = String.format("Fecha inicio: %s  |  Tipo: %s  |  Origen: %s  |  Estado: %s",

					lesion.getFecha() != null ? lesion.getFecha().toString() : "-",
					lesion.getTipo() != null ? lesion.getTipo() : "-",
					lesion.getOrigen() != null ? lesion.getOrigen() : "-",
					lesion.getEstado() != null ? lesion.getEstado() : "-");

			getLbDatosLesion().setText(texto);

			if (lesion.estaRecuperada()) {

				getCbAlta().setSelected(true);
				getCbAlta().setEnabled(false);
				getCbEstado().setEnabled(false);
				getDcFecha().setEnabled(false);
				getCbValoracion().setEnabled(false);
				getTaActualizacion().setEnabled(false);
				getDcFechaAlta().setEnabled(false);
				getBtGuardar().setEnabled(false);

			} else {

				getCbEstado().setSelectedItem(lesion.getEstado());

			}
		}
	}

	private void cargarEvolucionPrevia() {

		if (lesion != null && gestionLesiones != null) {

			try {

				List<EvolucionLesion> evoluciones = gestionLesiones.getEvolucion(lesion);
				modeloEvolucion.setRowCount(0);

				for (EvolucionLesion ev : evoluciones) {
					modeloEvolucion.addRow(new Object[] { ev.getFecha() != null ? ev.getFecha().toString() : "",
							ev.getValoracion(), ev.getActualizacion() });
				}
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(this, "Error al cargar la evolución previa: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void guardarEvolucion() {

		if (getDcFecha().getDate() == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha para la evolución.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String actualizacion = getTaActualizacion().getText().trim();
		if (actualizacion.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe introducir una descripción de la actualización.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			getTaActualizacion().requestFocus();
			return;
		}

		boolean darAlta = getCbAlta().isSelected();
		if (darAlta && getDcFechaAlta().getDate() == null) {
			JOptionPane.showMessageDialog(this, "Debe indicar la fecha del alta.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String valoracion = (String) getCbValoracion().getSelectedItem();
		String nuevoEstado = darAlta ? Lesion.RECUPERADO : (String) getCbEstado().getSelectedItem();
		LocalDate fecha = aLocalDate(getDcFecha().getDate());
		LocalDate fechaAlta = darAlta ? aLocalDate(getDcFechaAlta().getDate()) : null;

		try {
			gestionLesiones.registrarEvolucion(lesion, fecha, valoracion, actualizacion, nuevoEstado, fechaAlta);
		} catch (ApplicationException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Evolución registrada correctamente.", "Éxito",
				JOptionPane.INFORMATION_MESSAGE);
		vvl.recargarTabla();
		volver();
	}

	private void volver() {
		if (vvl != null) {
			vvl.setVisible(true);
		}
		this.dispose();
	}

	private LocalDate aLocalDate(Date d) {
		return d == null ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}