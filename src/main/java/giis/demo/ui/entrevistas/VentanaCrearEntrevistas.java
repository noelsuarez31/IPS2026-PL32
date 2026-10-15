package giis.demo.ui.entrevistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

import giis.demo.exceptions.InterviewException;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.entrevistas.CrearEntrevistaService;
import giis.demo.model.entrevistas.FranjaHoraria;
import giis.demo.ui.VentanaPrincipal;

public class VentanaCrearEntrevistas extends JFrame {
	
	private VentanaPrincipal vPrincipal;
	private CrearEntrevistaService crearEntrevista;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
	private JPanel pnCentro;
	private JPanel pnInferior;
	private JPanel pnListaJugadoresYCalendario;
	private JPanel pnFranjasDisponibles;
	private JPanel pnDetallesEntrevista;
	private JButton btnCancelar;
	private JButton btnCrearEntrevista;
	private JPanel pnListaJugadores;
	private JList<EmpleadoDeportivo> listJugadoresConFranja;
	private DefaultListModel<EmpleadoDeportivo> modeloListaJugadores;
	private JPanel pnCalendarioDias;
	private JCalendar calendarioDias;
	private JLabel lbListaJugadores;
	private JLabel lbCalendarioDias;
	private JLabel lbListaFranjas;
	private JLabel lbInformacionEntrevista;
	private JPanel pnMedioComunicacion;
	private JLabel lbMedioComunicacion;
	private JTextField txMedioComunicacion;
	private JPanel pnComboFranjas;
	private JComboBox<FranjaHoraria> cbFranjas;
	private JPanel pnCentral;
	private JScrollPane scrListaJugadores;

	public VentanaCrearEntrevistas(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.crearEntrevista = vPrincipal.getCrearEntrevistaService();
		
		setTitle("Añadir entrevistas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 903, 499);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getLbTitulo(), BorderLayout.NORTH);
		contentPane.add(getPnCentro(), BorderLayout.CENTER);
		contentPane.add(getPnInferior(), BorderLayout.SOUTH);
	}

	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Seleccion de entrevistas para medios");
			lbTitulo.setFont(new Font("Tahoma", Font.PLAIN, 17));
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lbTitulo;
	}
	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setLayout(new BorderLayout(0, 0));
			pnCentro.add(getPnListaJugadoresYCalendario(), BorderLayout.NORTH);
			pnCentro.add(getPnCentral(), BorderLayout.CENTER);
		}
		return pnCentro;
	}
	private JPanel getPnInferior() {
		if (pnInferior == null) {
			pnInferior = new JPanel();
			pnInferior.add(getBtnCancelar());
			pnInferior.add(getBtnCrearEntrevista());
		}
		return pnInferior;
	}
	private JPanel getPnListaJugadoresYCalendario() {
		if (pnListaJugadoresYCalendario == null) {
			pnListaJugadoresYCalendario = new JPanel();
			pnListaJugadoresYCalendario.setLayout(new GridLayout(0, 2, 0, 0));
			pnListaJugadoresYCalendario.add(getPnListaJugadores());
			pnListaJugadoresYCalendario.add(getPnCalendarioDias());
		}
		return pnListaJugadoresYCalendario;
	}
	private JPanel getPnFranjasDisponibles() {
		if (pnFranjasDisponibles == null) {
			pnFranjasDisponibles = new JPanel();
			pnFranjasDisponibles.setLayout(new BorderLayout(0, 0));
			pnFranjasDisponibles.add(getLbListaFranjas(), BorderLayout.NORTH);
			pnFranjasDisponibles.add(getPnComboFranjas(), BorderLayout.CENTER);
		}
		return pnFranjasDisponibles;
	}
	private JPanel getPnDetallesEntrevista() {
		if (pnDetallesEntrevista == null) {
			pnDetallesEntrevista = new JPanel();
			pnDetallesEntrevista.setLayout(new BorderLayout(0, 0));
			pnDetallesEntrevista.add(getLbInformacionEntrevista(), BorderLayout.NORTH);
			pnDetallesEntrevista.add(getPnMedioComunicacion());
		}
		return pnDetallesEntrevista;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					vPrincipal.setVisible(true);
				}
			});
			btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		return btnCancelar;
	}
	private JButton getBtnCrearEntrevista() {
		if (btnCrearEntrevista == null) {
			btnCrearEntrevista = new JButton("Crear entrevista");
			btnCrearEntrevista.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					crearEntrevista();
				}
			});
			btnCrearEntrevista.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		return btnCrearEntrevista;
	}
	private JPanel getPnListaJugadores() {
		if (pnListaJugadores == null) {
			pnListaJugadores = new JPanel();
			pnListaJugadores.setLayout(new BorderLayout(0, 0));
			pnListaJugadores.add(getScrListaJugadores(), BorderLayout.CENTER);
			pnListaJugadores.add(getLbListaJugadores(), BorderLayout.NORTH);
		}
		return pnListaJugadores;
	}
	
	private JScrollPane getScrListaJugadores() {
		if (scrListaJugadores == null) {
			scrListaJugadores = new JScrollPane(getListJugadoresConFranja());
		}
		return scrListaJugadores;
	}
	
	private JList<EmpleadoDeportivo> getListJugadoresConFranja() {
		if (listJugadoresConFranja == null) {
			listJugadoresConFranja = new JList<EmpleadoDeportivo>();
			listJugadoresConFranja.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(!e.getValueIsAdjusting()) {
						try {
							gestionDestacarDiasCalendario();
						} catch (InterviewException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				}
			});
			listJugadoresConFranja.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			cargarJugadoresConFranjas();
		}
		return listJugadoresConFranja;
	}

	private JPanel getPnCalendarioDias() {
		if (pnCalendarioDias == null) {
			pnCalendarioDias = new JPanel();
			pnCalendarioDias.setLayout(new BorderLayout(0, 0));
			pnCalendarioDias.add(getCalendarioDias());
			pnCalendarioDias.add(getLbCalendarioDias(), BorderLayout.NORTH);
		}
		return pnCalendarioDias;
	}
	private JCalendar getCalendarioDias() {
		if (calendarioDias == null) {
			calendarioDias = new JCalendar();
			calendarioDias.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					try {
						validarFecha();
						gestionDestacarDiasCalendario();
						gestionCombo();
					} catch (InterviewException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					
				}
			});
			
			calendarioDias.getDayChooser().addPropertyChangeListener("month", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					try {
						gestionDestacarDiasCalendario();
					} catch (InterviewException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}
			});
			
			calendarioDias.getDayChooser().addPropertyChangeListener("year", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					try {
						gestionDestacarDiasCalendario();
					} catch (InterviewException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}
			});
		}
		return calendarioDias;
	}
	private JLabel getLbListaJugadores() {
		if (lbListaJugadores == null) {
			lbListaJugadores = new JLabel("Lista de jugadores con franjas horarias:");
			lbListaJugadores.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		return lbListaJugadores;
	}
	private JLabel getLbCalendarioDias() {
		if (lbCalendarioDias == null) {
			lbCalendarioDias = new JLabel("Dias con franjas del jugador seleccionado:");
			lbCalendarioDias.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		return lbCalendarioDias;
	}
	
	private JLabel getLbListaFranjas() {
		if (lbListaFranjas == null) {
			lbListaFranjas = new JLabel("Franjas del jugador disponibles para el dia seleccionado:");
			lbListaFranjas.setHorizontalAlignment(SwingConstants.CENTER);
			lbListaFranjas.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		return lbListaFranjas;
	}
	private JLabel getLbInformacionEntrevista() {
		if (lbInformacionEntrevista == null) {
			lbInformacionEntrevista = new JLabel("Informacion sobre la entrevista");
			lbInformacionEntrevista.setHorizontalAlignment(SwingConstants.CENTER);
			lbInformacionEntrevista.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		return lbInformacionEntrevista;
	}
	private JPanel getPnMedioComunicacion() {
		if (pnMedioComunicacion == null) {
			pnMedioComunicacion = new JPanel();
			pnMedioComunicacion.add(getLbMedioComunicacion());
			pnMedioComunicacion.add(getTxMedioComunicacion());
		}
		return pnMedioComunicacion;
	}
	private JLabel getLbMedioComunicacion() {
		if (lbMedioComunicacion == null) {
			lbMedioComunicacion = new JLabel("Nombre del medio de comunicacion:");
			lbMedioComunicacion.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		return lbMedioComunicacion;
	}
	private JTextField getTxMedioComunicacion() {
		if (txMedioComunicacion == null) {
			txMedioComunicacion = new JTextField();
			txMedioComunicacion.setFont(new Font("Tahoma", Font.PLAIN, 13));
			txMedioComunicacion.setColumns(10);
		}
		return txMedioComunicacion;
	}
	private JPanel getPnComboFranjas() {
		if (pnComboFranjas == null) {
			pnComboFranjas = new JPanel();
			pnComboFranjas.add(getCbFranjas());
		}
		return pnComboFranjas;
	}
	private JComboBox<FranjaHoraria> getCbFranjas() {
		if (cbFranjas == null) {
			cbFranjas = new JComboBox<FranjaHoraria>();
		}
		return cbFranjas;
	}

	private JPanel getPnCentral() {
		if (pnCentral == null) {
			pnCentral = new JPanel();
			pnCentral.setLayout(new GridLayout(2, 0, 0, 0));
			pnCentral.add(getPnFranjasDisponibles(), BorderLayout.CENTER);
			pnCentral.add(getPnDetallesEntrevista(), BorderLayout.SOUTH);
		}
		return pnCentral;
	}
	
	// METODOS AUXILIARES
	private void crearEntrevista() {
		String nombreMedioCom = getTxMedioComunicacion().getText();
		EmpleadoDeportivo jugadorSeleccionado = getListJugadoresConFranja().getSelectedValue();
		Date fechaUtil = getCalendarioDias().getDate();
		LocalDate fechaSeleccionada = fechaUtil.
				toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		FranjaHoraria franjaSeleccionada = (FranjaHoraria) getCbFranjas().getSelectedItem();
		
		try{
			crearEntrevista.validarYCrearEntrevista(jugadorSeleccionado, fechaSeleccionada, franjaSeleccionada, nombreMedioCom);
			JOptionPane.showMessageDialog(null, "Entrevista creada correctamente");
			gestionarCambios();
		} catch(InterviewException ie) {
			JOptionPane.showMessageDialog(null, ie.getMessage());
		}
	}

	private void gestionarCambios() throws InterviewException {
		cargarJugadoresConFranjas();
		gestionDestacarDiasCalendario();
		gestionCombo();
		getTxMedioComunicacion().setText("");
	}

	private void gestionCombo() throws InterviewException {
		EmpleadoDeportivo jugadorSeleccionado = getListJugadoresConFranja().getSelectedValue();
		Date fechaUtil = getCalendarioDias().getDate();
		LocalDate fechaSeleccionada = fechaUtil.
				toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if(jugadorSeleccionado != null && fechaSeleccionada != null) {
			cargarFranjasDeJugador(jugadorSeleccionado, fechaSeleccionada);
		} else {
			getCbFranjas().setModel(new DefaultComboBoxModel<FranjaHoraria>());
		}
	}
	
	private void cargarFranjasDeJugador(EmpleadoDeportivo jugadorSeleccionado, LocalDate fechaSeleccionada) throws InterviewException {
		List<FranjaHoraria> franjasJugador = crearEntrevista.cargarFranjas(jugadorSeleccionado, fechaSeleccionada);
		DefaultComboBoxModel<FranjaHoraria> modeloComboFranjas = 
				new DefaultComboBoxModel<>(franjasJugador.toArray(new FranjaHoraria[0]));
		getCbFranjas().setModel(modeloComboFranjas);
	}
	
	private void cargarJugadoresConFranjas() {
		this.modeloListaJugadores = new DefaultListModel<EmpleadoDeportivo>();
		List<EmpleadoDeportivo> jugadoresConFranjas = crearEntrevista.getJugadoresConFranjas();
		
		for(EmpleadoDeportivo jugador : jugadoresConFranjas) {
			modeloListaJugadores.addElement(jugador);
		}
		
		getListJugadoresConFranja().setModel(modeloListaJugadores);
		
		// Si no hay jugadores en la lista, limpiamos el calendario para que no 
		// quede nada seleccionado
		if(modeloListaJugadores.isEmpty()) {
			resaltarDiasConFranjas(new ArrayList<>());
		}
	}
	
	private void gestionDestacarDiasCalendario() throws InterviewException {
		EmpleadoDeportivo jugadorSeleccionado = getListJugadoresConFranja().getSelectedValue();
		if(jugadorSeleccionado != null) {
			Calendar cal = getCalendarioDias().getCalendar();
			List<LocalDate> diasConFranjas = crearEntrevista.getDiasConFranja(jugadorSeleccionado, cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));
			resaltarDiasConFranjas(diasConFranjas);
			getCbFranjas().removeAllItems();
		}
	}
	
	public void resaltarDiasConFranjas(List<LocalDate> diasConFranjas) {
		if(calendarioDias != null) {
			// Obtenemos el componente de los días del JCalendar
			JDayChooser dayChooser = calendarioDias.getDayChooser();

			// El JDayChooser almacena los botones de los días en un array interno o panel.
			// Podemos buscar los componentes hijos que sean botones (JButton)
			// correspondientes a los días:
			Component[] components = dayChooser.getDayPanel().getComponents();

			for (Component comp : components) {
				if (comp instanceof JButton) {
					JButton dayButton = (JButton) comp;
					String text = dayButton.getText();

					if (!text.isEmpty()) {
						try {
							int dayNum = Integer.parseInt(text);
							// Obtenemos el año y mes actual que muestra el calendario
							int year = getCalendarioDias().getCalendar().get(Calendar.YEAR);
							int month = getCalendarioDias().getCalendar().get(Calendar.MONTH) + 1;

							LocalDate fechaBoton = LocalDate.of(year, month, dayNum);

							// Si la lista de días con franjas contiene esta fecha, la resaltamos
							if (diasConFranjas.contains(fechaBoton)) {
								dayButton.setBackground(new Color(173, 216, 230)); // Azul claro (o el color que prefieras)
								dayButton.setOpaque(true);
							} else {
								// Restauramos el color por defecto para los días sin franjas
								dayButton.setBackground(null);
								dayButton.setOpaque(false);
							}
						} catch (NumberFormatException e) {
							// Ignorar si el texto del botón no es un número
						}
					}
				}
			}
		}
		
	}
	
	private void validarFecha() throws InterviewException {
		LocalDate fechaSeleccionada = getCalendarioDias().getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
		
		if(fechaSeleccionada.isBefore(LocalDate.now())) {
			JOptionPane.showMessageDialog(null, "Fecha seleccionada invalida. No puede ser anterior a hoy");
		}
		
		Calendar cal = getCalendarioDias().getCalendar();
		List<LocalDate> diasConFranjas = crearEntrevista.getDiasConFranja(getListJugadoresConFranja().getSelectedValue(),
				cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));
		
		if(!diasConFranjas.contains(fechaSeleccionada)) {
			getCbFranjas().removeAllItems();
			return;
		}
		
		cargarFranjasDeJugador(getListJugadoresConFranja().getSelectedValue(), fechaSeleccionada);
	}
}
