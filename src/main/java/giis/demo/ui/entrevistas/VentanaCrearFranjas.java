package giis.demo.ui.entrevistas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import giis.demo.exceptions.NoAvailableSlotException;
import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.entrevistas.CrearFranjaService;
import giis.demo.ui.VentanaPrincipal;

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
	private JLabel lbFecha;
	private JTextField txFecha;
	private JPanel pnHoraInicio;
	private JLabel lbHoraInicio;
	private JTextField txHoraInicio;
	private JPanel pnHoraFin;
	private JLabel lbHoraFin;
	private JTextField txHoraFin;
	private JPanel pnBotones;
	private JButton btnCrearFranja;
	private JButton btnCambiarJugador;
	private JButton btnFinalizar;
	
	public VentanaCrearFranjas(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.crearFranjaService = vPrincipal.getCrearFranjaService();
		
		setTitle("Añadir franjas horarias a jugadores");
		
		setLocationRelativeTo(null);
		getContentPane().add(getPnSuperior(), BorderLayout.NORTH);
		getContentPane().add(getPnCentral(), BorderLayout.CENTER);
		getContentPane().add(getPnInferior(), BorderLayout.SOUTH);
		
		habilitarFormularioFranja(false);
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
			cargarEntrenadoresEnCombo();
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
			pnListaJugadores.add(getLbListaJugadores());
			pnListaJugadores.add(getListJugadores());
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
	
	private JList<EmpleadoDeportivo> getListJugadores() {
		if(listJugadores == null) {
			listJugadores = new JList<EmpleadoDeportivo>();
			listJugadores.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(!e.getValueIsAdjusting()) {
						// TODO EmpleadoDeportivo jugadorSeleccionado = ; (ALMACENAR EN SERVICIO)
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
		}
		return lbFormularioFranja;
	}
	private JPanel getPnFecha() {
		if (pnFecha == null) {
			pnFecha = new JPanel();
			pnFecha.add(getLbFecha());
			pnFecha.add(getTxFecha());
		}
		return pnFecha;
	}
	private JLabel getLbFecha() {
		if (lbFecha == null) {
			lbFecha = new JLabel("Fecha (ej: dd/MM/yyyy):");
		}
		return lbFecha;
	}
	private JTextField getTxFecha() {
		if (txFecha == null) {
			txFecha = new JTextField();
			txFecha.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txFecha.setColumns(10);
		}
		return txFecha;
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
			lbHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lbHoraInicio;
	}
	private JTextField getTxHoraInicio() {
		if (txHoraInicio == null) {
			txHoraInicio = new JTextField();
			txHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 15));
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
			lbHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lbHoraFin;
	}
	private JTextField getTxHoraFin() {
		if (txHoraFin == null) {
			txHoraFin = new JTextField();
			txHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txHoraFin.setColumns(10);
		}
		return txHoraFin;
	}
	private JPanel getPnBotones() {
		if (pnBotones == null) {
			pnBotones = new JPanel();
			pnBotones.add(getBtnCrearFranja());
			pnBotones.add(getBtnCambiarJugador());
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
					} catch(NoAvailableSlotException nsae) {
						JOptionPane.showMessageDialog(null, nsae.getMessage());
					}
				}
			});
			btnCrearFranja.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return btnCrearFranja;
	}
	private JButton getBtnCambiarJugador() {
		if (btnCambiarJugador == null) {
			btnCambiarJugador = new JButton("Cambiar jugador");
			btnCambiarJugador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					habilitarFormularioFranja(false);
				}
			});
			btnCambiarJugador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return btnCambiarJugador;
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
			btnFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return btnFinalizar;
	}
	
	// METODOS AUXILIARES
	
	private void cargarEntrenadoresEnCombo() {
		// LLAMADA A CAPA DE SERVICIO QUE HACE UNA CONSULTA Y 
		// DEVUELVE LOS DNIS DE LOS ENTRENADORES QUE PERTENCEN
		// A EQUIPOS PROFESIONALES
	}
	
	private void mostrarJugadoresEnLista() {
		// SI EL COMBO NO TIENE DNI SELECCIONADO, DEJA LISTA VACIA
		int idEquipo = 0;
		String nifEntrenador = "";
		if((nifEntrenador = (String) getCbDniEntrenadores().getSelectedItem()) != null) {
			idEquipo = crearFranjaService.getIdEquipo(nifEntrenador);
		}
		
		modelo = new DefaultListModel<EmpleadoDeportivo>();
		for(EmpleadoDeportivo jugador : crearFranjaService.obtenerJugadoresEquipoProfesional(idEquipo)) {
			modelo.addElement(jugador);
		}
		
		listJugadores.setModel(modelo);
	}
	
	private void habilitarFormularioFranja(boolean flag) {
		getTxFecha().setEnabled(flag);
		getTxHoraInicio().setEnabled(flag);
		getTxHoraFin().setEnabled(flag);
		getBtnCambiarJugador().setEnabled(flag);
		getBtnCrearFranja().setEnabled(flag);
	}
	
	private void crearFranja() throws NoAvailableSlotException {
		DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate fechaFranja = LocalDate.parse(txFecha.getText().trim(), formatoFecha);
		
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
	    LocalTime horaInicio = LocalTime.parse(txHoraInicio.getText().trim(), formatoHora);
	    LocalTime horaFin = LocalTime.parse(txHoraFin.getText().trim(), formatoHora);
	    
	    crearFranjaService.crearFranjaHoraria(fechaFranja, horaInicio, horaFin);
	}
}
