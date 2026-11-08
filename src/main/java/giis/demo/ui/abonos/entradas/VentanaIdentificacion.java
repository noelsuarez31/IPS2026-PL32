package giis.demo.ui.abonos.entradas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.model.entradas.VentaDeEntradas;
import giis.demo.ui.VentanaPrincipal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class VentanaIdentificacion extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lbDni;
	private JTextField tfDni;

	private VentanaPrincipal vp;
	private VentaDeEntradas venta;

	/**
	 * Create the dialog.
	 */
	public VentanaIdentificacion(VentanaPrincipal vp) {

		this.vp = vp;
		this.venta = new VentaDeEntradas();

		setTitle("Abonado : Dia del club");
		setModal(true);

		setBounds(100, 100, 551, 341);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLbDni());
		contentPanel.add(getTfDni());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btVolver = new JButton("Volver");
				btVolver.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						volverAlMenu();

					}
				});
				btVolver.setMnemonic('V');
				btVolver.setBackground(new Color(255, 128, 128));
				btVolver.setActionCommand("Cancel");
				buttonPane.add(btVolver);
			}
			{
				JButton btVerificar = new JButton("Verificar");
				btVerificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {

							continuar();

						} catch (SQLException e1) {

							JOptionPane.showMessageDialog(null, "Se ha producido un eror. No se peude continuar");
						}

					}
				});
				btVerificar.setBackground(new Color(128, 255, 128));
				btVerificar.setMnemonic('V');
				btVerificar.setActionCommand("OK");
				buttonPane.add(btVerificar);
				getRootPane().setDefaultButton(btVerificar);

				this.setLocationRelativeTo(null);
			}
		}
	}

	private void continuar() throws SQLException {

		if (!verificarDni()) {
			return;
		}

		if (!venta.comprobarDni(getDni())) {

			JOptionPane.showMessageDialog(null, "El dni no pertenece a ningún abonado.");
			return;
		}

		CompraEntradasAbonados cea = new CompraEntradasAbonados(this);

		this.setVisible(false);
		cea.setVisible(true);
	}

	private boolean verificarDni() {

		String dni = getTfDni().getText().trim();

		if (!dni.matches("\\d{8}[A-Za-z]") || dni.isBlank()) {

			JOptionPane.showMessageDialog(null, "El DNI debe tener 8 números y una letra.");
			return false;
		}

		return true;
	}

	private void volverAlMenu() {

		this.dispose();
		this.vp.setVisible(true);

	}

	private JLabel getLbDni() {
		if (lbDni == null) {
			lbDni = new JLabel("Dni: ");
			lbDni.setDisplayedMnemonic('D');
			lbDni.setLabelFor(getTfDni());
			lbDni.setBounds(10, 110, 46, 14);
		}
		return lbDni;
	}

	private JTextField getTfDni() {
		if (tfDni == null) {
			tfDni = new JTextField();
			tfDni.setBounds(66, 100, 186, 35);
			tfDni.setColumns(10);
		}
		return tfDni;
	}

	/**
	 * Metodo reinicair la aplicacion
	 */
	void reiniciar() {

		this.getTfDni().setText("");

	}

	/**
	 * Obtiene el dni
	 * 
	 * @return dni del abonado
	 */
	public String getDni() {

		if (!verificarDni()) {

			return null;

		}

		return getTfDni().getText().trim().toUpperCase();
	}

	/**
	 * Devuelve la venta de entradas utilizada
	 */
	public VentaDeEntradas getVentaDeEntradas() {
		return venta;
	}
}
