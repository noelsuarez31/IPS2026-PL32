package giis.demo.ui.login;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.demo.model.accion.Campana;
import giis.demo.model.accion.GestionCompraCampana;
import giis.demo.model.log.GestionLogSesion;
import giis.demo.ui.accion.VentanaCompraCampana;

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;
	private JLabel lbBienvenido;
	private JPanel panelInferior;
	private JPanel panelOpciones;
	private JButton btCompraAcciones;
	private JButton btIniciarSesion;

	public VentanaInicio() {
		setTitle("Ventana Inicial");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelCentral(), BorderLayout.CENTER);
		contentPane.add(getPanelInferior(), BorderLayout.SOUTH);

		setLocationRelativeTo(null);
	}

	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new BorderLayout());
			panelCentral.add(getLbBienvenido_1(), BorderLayout.CENTER);
		}
		return panelCentral;
	}

	private JLabel getLbBienvenido_1() {
		if (lbBienvenido == null) {
			lbBienvenido = new JLabel("¡Bienvenido!", SwingConstants.CENTER);
			lbBienvenido.setFont(new Font("Tahoma", Font.BOLD, 18));
		}
		return lbBienvenido;
	}

	private JPanel getPanelInferior() {
		if (panelInferior == null) {
			panelInferior = new JPanel();
			panelInferior.setLayout(new BorderLayout(0, 0));
			panelInferior.add(getPanelOpciones(), BorderLayout.EAST);
		}
		return panelInferior;
	}

	private JPanel getPanelOpciones() {
		if (panelOpciones == null) {
			panelOpciones = new JPanel();
			panelOpciones.setLayout(new GridLayout(1, 0, 8, 0));
			panelOpciones.add(getBtCompraAcciones());
			panelOpciones.add(getBtIniciarSesion());
		}
		return panelOpciones;
	}

	private JButton getBtCompraAcciones() {
		if (btCompraAcciones == null) {
			btCompraAcciones = new JButton("Compra acciones");
			btCompraAcciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					comprobarYAccederCompraAcciones();
				}
			});
		}
		return btCompraAcciones;
	}

	private void comprobarYAccederCompraAcciones() {
		try {
			Campana campana = new GestionCompraCampana().obtenerCampanaActiva();

			if (campana == null) {
				JOptionPane.showMessageDialog(this,
						"Actualmente no hay ninguna campaña de venta de acciones activa.",
						"Sin campaña activa", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (campana.getFase() < Campana.ULTIMA_FASE) {
				JOptionPane.showMessageDialog(this,
						"La campaña actual está en la Fase " + campana.getFase() + ".\n"
								+ "En esta fase solo los accionistas registrados pueden comprar acciones.\n"
								+ "Por favor, inicie sesión con su cuenta.",
						"Acceso restringido a accionistas", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			GestionLogSesion.registrarAccion("Ventana Comprar Acciones de Campaña (Público)");
			VentanaCompraCampana ventana = new VentanaCompraCampana(VentanaInicio.this);
			VentanaInicio.this.setVisible(false);
			ventana.setVisible(true);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this,
					"Error al comprobar las campañas activas:\n" + ex.getMessage(),
					"Error de conexión", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JButton getBtIniciarSesion() {
		if (btIniciarSesion == null) {
			btIniciarSesion = new JButton("Inicio de Sesión");
			btIniciarSesion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					VentanaLogin vl = new VentanaLogin(VentanaInicio.this);
					VentanaInicio.this.setVisible(false);
					vl.setVisible(true);
				}
			});
		}
		return btIniciarSesion;
	}
}