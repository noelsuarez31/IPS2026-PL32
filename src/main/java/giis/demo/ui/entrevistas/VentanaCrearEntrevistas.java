package giis.demo.ui.entrevistas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JList;
import com.toedter.calendar.JCalendar;

import giis.demo.model.empleado.EmpleadoDeportivo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;

public class VentanaCrearEntrevistas extends JFrame {

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
	private JPanel pnCalendarioDias;
	private JCalendar calendarioDias;
	private JLabel lbListaJugadores;
	private JLabel lbCalendarioDias;

	public VentanaCrearEntrevistas() {
		setTitle("Añadir entrevistas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 556, 379);
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
			lbTitulo.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lbTitulo;
	}
	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setLayout(new BorderLayout(0, 0));
			pnCentro.add(getPnListaJugadoresYCalendario(), BorderLayout.NORTH);
			pnCentro.add(getPnFranjasDisponibles(), BorderLayout.CENTER);
			pnCentro.add(getPnDetallesEntrevista(), BorderLayout.SOUTH);
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
		}
		return pnFranjasDisponibles;
	}
	private JPanel getPnDetallesEntrevista() {
		if (pnDetallesEntrevista == null) {
			pnDetallesEntrevista = new JPanel();
		}
		return pnDetallesEntrevista;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnCancelar;
	}
	private JButton getBtnCrearEntrevista() {
		if (btnCrearEntrevista == null) {
			btnCrearEntrevista = new JButton("Crear entrevista");
			btnCrearEntrevista.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnCrearEntrevista;
	}
	private JPanel getPnListaJugadores() {
		if (pnListaJugadores == null) {
			pnListaJugadores = new JPanel();
			pnListaJugadores.setLayout(new BorderLayout(0, 0));
			pnListaJugadores.add(getListJugadoresConFranja());
			pnListaJugadores.add(getLbListaJugadores(), BorderLayout.NORTH);
		}
		return pnListaJugadores;
	}
	private JList<EmpleadoDeportivo> getListJugadoresConFranja() {
		if (listJugadoresConFranja == null) {
			listJugadoresConFranja = new JList<EmpleadoDeportivo>();
		}
		return listJugadoresConFranja;
	}
	private JPanel getPnCalendarioDias() {
		if (pnCalendarioDias == null) {
			pnCalendarioDias = new JPanel();
			pnCalendarioDias.add(getCalendarioDias());
			pnCalendarioDias.add(getLbCalendarioDias());
		}
		return pnCalendarioDias;
	}
	private JCalendar getCalendarioDias() {
		if (calendarioDias == null) {
			calendarioDias = new JCalendar();
		}
		return calendarioDias;
	}
	private JLabel getLbListaJugadores() {
		if (lbListaJugadores == null) {
			lbListaJugadores = new JLabel("New label");
		}
		return lbListaJugadores;
	}
	private JLabel getLbCalendarioDias() {
		if (lbCalendarioDias == null) {
			lbCalendarioDias = new JLabel("New label");
		}
		return lbCalendarioDias;
	}
	
	
}
