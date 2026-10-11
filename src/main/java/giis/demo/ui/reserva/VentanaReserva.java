package giis.demo.ui.reserva;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import giis.demo.ui.VentanaPrincipal;

import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
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
	private JLabel lbInstalacion;
	private JComboBox cbInstalacion;
	private JButton btCancelar;

	/**
	 * Create the frame.
	 */
	public VentanaReserva(VentanaPrincipal vp) {

		this.vp = vp;
		
		setTitle("Reserva de Instalaciones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1044, 432);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelFecha(), BorderLayout.NORTH);
		contentPane.add(getPanelSeleccion(), BorderLayout.SOUTH);
		contentPane.add(getPanelHorario(), BorderLayout.CENTER);
		setLocationRelativeTo(null);

	}

	private JPanel getPanelFecha() {
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

	private JSpinner getSpFecha() {

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

	private JPanel getPanelSeleccionFecha() {
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
			lbFecha.setDisplayedMnemonic('S');
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

	private JSpinner getSpInicio() {

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

	private JSpinner getSpFin() {
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
			spHorario = new JScrollPane();
		}
		return spHorario;
	}

	public void reiniciar() {

		this.getCbInstalacion().setSelectedIndex(0);
		this.getSpFecha().setValue(new Date());
		this.getSpInicio().setValue(hora(8,0));
		this.getSpFin().setValue(hora(9,0));


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

		this.cbInstalacion.setEnabled(false);
		this.spFecha.setEnabled(false);
		
		this.btConsulta.setEnabled(false);
		this.btCancelar.setEnabled(true);
		this.btSiguiente.setEnabled(true);
		
		this.spInicio.setEnabled(true);
		this.spFin.setEnabled(true);
		// TODO
	}

	private int aMinutos(Date d) {
		LocalTime t = d.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		return t.getHour() * 60 + t.getMinute();
	}

	private boolean checkHoras() {
		return aMinutos((Date) spInicio.getValue()) < aMinutos((Date) spFin.getValue());
	}

	private boolean checkDuration() {
		return aMinutos((Date) spFin.getValue()) - aMinutos((Date) spInicio.getValue()) >= 60;
	}

	private void registrarDatosDeUsuario() {

		
		if (!checkHoras()) {
			JOptionPane.showMessageDialog(
		            this,
		            "La hora de inicio no puede ser mayor o igual a la hora final.",
		            "Duración no válida",
		            JOptionPane.WARNING_MESSAGE
		        );
		        return;
		}

		if (!checkDuration()) {
			JOptionPane.showMessageDialog(
		            this,
		            "La reserva debe durar al menos una hora.",
		            "Duración no válida",
		            JOptionPane.WARNING_MESSAGE
		        );
		        return;
		}
		
		
		VentaRegistroUsuario vru = new VentaRegistroUsuario(this);
		vru.setVisible(true);

	}
	
	private void volverAlMenu() {
		
		this.vp.setVisible(true);
		//TODO cerrar
		this.dispose();
	}
	
	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver");
			btVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					volverAlMenu();
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
			lbInstalacion.setDisplayedMnemonic('S');
		}
		return lbInstalacion;
	}
	private JComboBox getCbInstalacion() {
		if (cbInstalacion == null) {
			cbInstalacion = new JComboBox();
			cbInstalacion.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3"}));
			cbInstalacion.setBounds(196, 4, 272, 28);
		}
		return cbInstalacion;
	}
	
	private void cancelar() {
		
		this.cbInstalacion.setEnabled(true);
		this.spFecha.setEnabled(true);
		
		this.btCancelar.setEnabled(false);
		this.btConsulta.setEnabled(true);
		this.btSiguiente.setEnabled(false);

		this.spFin.setEnabled(false);
		this.spInicio.setEnabled(false);
		
		reiniciar();
		
	}
	
	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.setEnabled(false);
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelar();
				}
			});
			btCancelar.setToolTipText("Pulsa para cancelar la reserva");
			btCancelar.setMnemonic('N');
			btCancelar.setBackground(new Color(255, 128, 128));
		}
		return btCancelar;
	}
}
