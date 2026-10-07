package giis.demo.ui.entradas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaVentaConfirmacion extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbConfirmacion;
	private JButton btFinalizar;

	private VentanaVentaEntradas vve;
	
	/**
	 * Create the frame.
	 */
	public VentanaVentaConfirmacion(VentanaVentaEntradas vve) {
		setModal(true);
		setTitle("Confirmación de compra");
		
		this.vve = vve;
		
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 593, 360);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbConfirmacion());
		contentPane.add(getBtFinalizar());
		this.setLocationRelativeTo(null);
	}

	private JLabel getLbConfirmacion() {
		if (lbConfirmacion == null) {
			lbConfirmacion = new JLabel("Se ha realizado la compra de las entradas.");
			lbConfirmacion.setBounds(163, 131, 337, 20);
		}
		return lbConfirmacion;
	}
	private JButton getBtFinalizar() {
		if (btFinalizar == null) {
			btFinalizar = new JButton("Finalizar");
			btFinalizar.setMnemonic('F');
			btFinalizar.setToolTipText("Pulse para finalizar");
			btFinalizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					reiniciarProceso();
					
				}
			});
			btFinalizar.setBackground(new Color(128, 255, 128));
			btFinalizar.setBounds(447, 276, 115, 29);
		}
		return btFinalizar;
	}
	
	/**
	 * Prepara la interfaz para una nueva compra
	 */
	private void reiniciarProceso() {
		
		this.dispose();
		
		vve.reiniciar();
		vve.setVisible(true);
		
	}
}
