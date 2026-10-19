package giis.demo.ui.entrevistas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JCalendar;

import giis.demo.exceptions.NoAvailableSlotException;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.entrevistas.CrearFranjaService;
import giis.demo.ui.VentanaPrincipal;
import java.time.ZoneId;

public class VentanaCrearFranjas extends JFrame{
	private VentanaPrincipal vPrincipal;
	private CrearFranjaService crearFranjaService;
	
	private static final long serialVersionUID = 1L;
	private JPanel pnSuperior;
	private JLabel lbSeleccionDni;
	private JComboBox<String> cbDniEntrenadores;
	private JPanel pnCentral;
	private JPanel pnListaJugadores;
	private JPanel pnFormularioFranja;
	private JPanel pnInferior;
	private JLabel lbListaJugadores;
	private JList<EmpleadoDeportivo> listJugadores;
	private DefaultListModel<EmpleadoDeportivo> modelo;
	private JLabel lbFormularioFranja;
	private JPanel pnFecha;
	private JPanel pnHoraInicio;
	private JLabel lbHoraInicio;
	private JTextField txHoraInicio;
	private JPanel pnHoraFin;
	private JLabel lbHoraFin;
	private JTextField txHoraFin;
	private JPanel pnBotones;
	private JButton btnCrearFranja;
	private JButton btnFinalizar;
	private JScrollPane scrLista;
	private JCalendar calendarioFecha;
	
	public VentanaCrearFranjas(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.crearFranjaService = vPrincipal.getCrearFranjaService();
		
		setTitle("Añadir franjas horarias a jugadores");
		pack();
		setSize(800,600);
		setLocationRelativeTo(null);
		getContentPane().add(getPnSuperior(), BorderLayout.NORTH);
		getContentPane().add(getPnCentral(), BorderLayout.CENTER);
		getContentPane().add(getPnInferior(), BorderLayout.SOUTH);
		
		habilitarFormularioFranja(false);
		cargarEntrenadoresEnCombo();
	}

	private JPanel getPnSuperior() {
		if(pnSuperior == null) {
			pnSuperior = new JPanel();
			pnSuperior.add(getLbSeleccionDni());
			pnSuperior.add(getCbDniEntrenadores());
		}
		
		return pnSuperior;
	}
	
	private JLabel getLbSeleccionDni() {
		if(lbSeleccionDni == null) {
			lbSeleccionDni = new JLabel("Seleccione su DNI para añadir franjas a sus jugadores:");
			lbSeleccionDni.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		
		return lbSeleccionDni;
	}
	
	// SI HAY QUE COGER EL ENTRENADOR, CONSULTA ADICIONAL
	private JComboBox<String> getCbDniEntrenadores() {
		if(cbDniEntrenadores == null) {
			cbDniEntrenadores = new JComboBox<String>();
			cbDniEntrenadores.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarJugadoresEnLista();
				}
			});
			cbDniEntrenadores.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		
		return cbDniEntrenadores;
	}

	private JPanel getPnCentral() {
		if (pnCentral == null) {
			pnCentral = new JPanel();
			pnCentral.setLayout(new GridLayout(0, 2, 0, 0));
			pnCentral.add(getPnListaJugadores());
			pnCentral.add(getPnFormularioFranja());
		}
		return pnCentral;
	}
	private JPanel getPnListaJugadores() {
		if (pnListaJugadores == null) {
			pnListaJugadores = new JPanel();
			pnListaJugadores.setLayout(new BorderLayout(0, 0));
			pnListaJugadores.add(getLbListaJugadores(), BorderLayout.NORTH);
			pnListaJugadores.add(getScrLista(), BorderLayout.CENTER);
		}
		return pnListaJugadores;
	}
	private JPanel getPnFormularioFranja() {
		if (pnFormularioFranja == null) {
			pnFormularioFranja = new JPanel();
			pnFormularioFranja.setEnabled(false);
			pnFormularioFranja.setLayout(new BoxLayout(pnFormularioFranja, BoxLayout.Y_AXIS));
			pnFormularioFranja.add(getLbFormularioFranja());
			pnFormularioFranja.add(getPnFecha());
			pnFormularioFranja.add(getPnHoraInicio());
			pnFormularioFranja.add(getPnHoraFin());
			pnFormularioFranja.add(getPnBotones());
		}
		return pnFormularioFranja;
	}
	private JPanel getPnInferior() {
		if (pnInferior == null) {
			pnInferior = new JPanel();
			pnInferior.setLayout(new BorderLayout(0, 0));
			pnInferior.add(getBtnFinalizar(), BorderLayout.EAST);
		}
		return pnInferior;
	}
	private JLabel getLbListaJugadores() {
		if (lbListaJugadores == null) {
			lbListaJugadores = new JLabel("Seleccione el jugador:");
			lbListaJugadores.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lbListaJugadores;
	}
	
	private JScrollPane getScrLista() {
		if (scrLista == null) {
			scrLista = new JScrollPane(getListJugadores());
		}
		return scrLista;
	}
	
	private JList<EmpleadoDeportivo> getListJugadores() {
		if(listJugadores == null) {
			listJugadores = new JList<EmpleadoDeportivo>();
			listJugadores.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(!e.getValueIsAdjusting()) {
						EmpleadoDeportivo jugadorSeleccionado = listJugadores.getSelectedValue();
						crearFranjaService.setJugadorSeleccionado(jugadorSeleccionado);
						habilitarFormularioFranja(true);
					}
				}
			});
			listJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listJugadores.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return listJugadores;
	}
	private JLabel getLbFormularioFranja() {
		if (lbFormularioFranja == null) {
			lbFormularioFranja = new JLabel("Introduzca los datos de la franja:");
			lbFormularioFranja.setHorizontalAlignment(SwingConstants.CENTER);
			lbFormularioFranja.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lbFormularioFranja.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lbFormularioFranja;
	}
	private JPanel getPnFecha() {
		if (pnFecha == null) {
			pnFecha = new JPanel();
			pnFecha.add(getCalendarioFecha());
		}
		return pnFecha;
	}
	
	private JCalendar getCalendarioFecha() {
	    if (calendarioFecha == null) {
	        calendarioFecha = new JCalendar();
	    }
	    return calendarioFecha;
	}
	
	private JPanel getPnHoraInicio() {
		if (pnHoraInicio == null) {
			pnHoraInicio = new JPanel();
			pnHoraInicio.add(getLbHoraInicio());
			pnHoraInicio.add(getTxHoraInicio());
		}
		return pnHoraInicio;
	}
	private JLabel getLbHoraInicio() {
		if (lbHoraInicio == null) {
			lbHoraInicio = new JLabel("Hora inicio (ej:HH:mm)");
			lbHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 17));
		}
		return lbHoraInicio;
	}
	private JTextField getTxHoraInicio() {
		if (txHoraInicio == null) {
			txHoraInicio = new JTextField();
			txHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 17));
			txHoraInicio.setColumns(10);
		}
		return txHoraInicio;
	}
	private JPanel getPnHoraFin() {
		if (pnHoraFin == null) {
			pnHoraFin = new JPanel();
			pnHoraFin.add(getLbHoraFin());
			pnHoraFin.add(getTxHoraFin());
		}
		return pnHoraFin;
	}
	private JLabel getLbHoraFin() {
		if (lbHoraFin == null) {
			lbHoraFin = new JLabel("Hora fin (ej:HH:mm)");
			lbHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 17));
		}
		return lbHoraFin;
	}
	private JTextField getTxHoraFin() {
		if (txHoraFin == null) {
			txHoraFin = new JTextField();
			txHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 17));
			txHoraFin.setColumns(10);
		}
		return txHoraFin;
	}
	private JPanel getPnBotones() {
		if (pnBotones == null) {
			pnBotones = new JPanel();
			pnBotones.add(getBtnCrearFranja());
		}
		return pnBotones;
	}
	private JButton getBtnCrearFranja() {
		if (btnCrearFranja == null) {
			btnCrearFranja = new JButton("Crear franja");
			btnCrearFranja.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						crearFranja();
						JOptionPane.showMessageDialog(null, "Franja creada correctamente");
					} catch(NoAvailableSlotException nsae) {
						JOptionPane.showMessageDialog(null, nsae.getMessage());
					}
				}
			});
			btnCrearFranja.setFont(new Font("Tahoma", Font.PLAIN, 17));
		}
		return btnCrearFranja;
	}
	private JButton getBtnFinalizar() {
		if (btnFinalizar == null) {
			btnFinalizar = new JButton("Finalizar");
			btnFinalizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					vPrincipal.setVisible(true);
				}
			});
			btnFinalizar.setHorizontalAlignment(SwingConstants.RIGHT);
			btnFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btnFinalizar;
	}
	
	// METODOS AUXILIARES
	private void cargarEntrenadoresEnCombo() {
		List<String> dnis = crearFranjaService.obtenerDnisEntrenadoresDeEquiposProfesionales();
		DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>(dnis.toArray(new String[0]));
		cbDniEntrenadores.setModel(modeloCombo);
		
		mostrarJugadoresEnLista();
	}
	
	private void mostrarJugadoresEnLista() {
		// SI EL COMBO NO TIENE DNI SELECCIONADO, DEJA LISTA VACIA
		int idEquipo = 0;
		String nifEntrenador;
		if((nifEntrenador = (String) getCbDniEntrenadores().getSelectedItem()) != null) {
			idEquipo = crearFranjaService.getIdEquipo(nifEntrenador);
			
			modelo = new DefaultListModel<EmpleadoDeportivo>();
			for(EmpleadoDeportivo jugador : crearFranjaService.obtenerJugadoresEquipoProfesional(idEquipo)) {
				modelo.addElement(jugador);
			}
			
			listJugadores.setModel(modelo);
		}
	}
	
	private void habilitarFormularioFranja(boolean flag) {
	    getCalendarioFecha().setEnabled(flag);
	    getTxHoraInicio().setEnabled(flag);
	    getTxHoraFin().setEnabled(flag);
	    getBtnCrearFranja().setEnabled(flag);
	    
	    getTxHoraInicio().setText("");
	    getTxHoraFin().setText("");
	}
	
	private void crearFranja() throws NoAvailableSlotException {
		Date fechaUtil = getCalendarioFecha().getDate();
	    if (fechaUtil == null) {
	        throw new NoAvailableSlotException("Debe seleccionar una fecha en el calendario");
	    }
	    LocalDate fechaFranja = fechaUtil.toInstant()
	            .atZone(ZoneId.systemDefault())
	            .toLocalDate();
		
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		if(getTxHoraInicio().getText() == null || getTxHoraInicio().getText().isBlank() 
				|| getTxHoraFin().getText() == null || getTxHoraFin().getText().isBlank()) {
			throw new NoAvailableSlotException("Los campos de las horas no pueden estar vacio");
		}
	    LocalTime horaInicio;
	    LocalTime horaFin;
	    try {
	        horaInicio = LocalTime.parse(getTxHoraInicio().getText().trim(), formatoHora);
	        horaFin = LocalTime.parse(getTxHoraFin().getText().trim(), formatoHora);
	    } catch (DateTimeParseException dtpe) {
	        throw new NoAvailableSlotException("El formato de las horas debe ser HH:mm válido (entre 00:00 y 23:59).");
	    }
	    
	    crearFranjaService.crearFranjaHoraria(fechaFranja, horaInicio, horaFin);
	    
	    habilitarFormularioFranja(false);
	}

}
