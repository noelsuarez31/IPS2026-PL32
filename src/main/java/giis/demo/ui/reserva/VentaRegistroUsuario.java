package giis.demo.ui.reserva;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class VentaRegistroUsuario extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private VentanaReserva vr;
	private JPanel panelUsuario;
	private JLabel lbName;
	private JTextField tfName;
	private JLabel lbTarjeta;
	private JTextField tfTarjeta;
	private JPanel panelResumen;
	private JPanel panelInfo;
	private JPanel panelDuracion;
	private JLabel lbDuracion;
	private JTextField tfDuracion;
	private JPanel panelPrecio;
	private JLabel lbPrecio;
	private JTextField tfPrecio;
	private JPanel panelBotones;
	private JButton btConfirmar;
	private JButton btCancelar;
	

	/**
	 * Create the frame.
	 */
	public VentaRegistroUsuario(VentanaReserva vr) {
		setTitle("Confirmación de la reserva");
		
		this.vr = vr;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 687, 414);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelUsuario(), BorderLayout.CENTER);
		contentPane.add(getPanelResumen(), BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		setModal(true);

	}

	private JPanel getPanelUsuario() {
		if (panelUsuario == null) {
			panelUsuario = new JPanel();
			panelUsuario.setLayout(null);
			panelUsuario.add(getLbName());
			panelUsuario.add(getTfName());
			panelUsuario.add(getLbTarjeta());
			panelUsuario.add(getTfTarjeta());
		}
		return panelUsuario;
	}
	private JLabel getLbName() {
		if (lbName == null) {
			lbName = new JLabel("Nombre:");
			lbName.setDisplayedMnemonic('N');
			lbName.setLabelFor(getTfName());
			lbName.setToolTipText("Introduzca su nombre");
			lbName.setBounds(15, 55, 69, 20);
		}
		return lbName;
	}
	private JTextField getTfName() {
		if (tfName == null) {
			tfName = new JTextField();
			tfName.setToolTipText("Introduzca su nombre");
			tfName.setBounds(99, 52, 345, 26);
			tfName.setColumns(10);
		}
		return tfName;
	}
	private JLabel getLbTarjeta() {
		if (lbTarjeta == null) {
			lbTarjeta = new JLabel("Tarjeta:");
			lbTarjeta.setLabelFor(getTfTarjeta());
			lbTarjeta.setToolTipText("Introduzca su nombre");
			lbTarjeta.setDisplayedMnemonic('T');
			lbTarjeta.setBounds(15, 93, 69, 20);
		}
		return lbTarjeta;
	}
	private JTextField getTfTarjeta() {
		if (tfTarjeta == null) {
			tfTarjeta = new JTextField();
			tfTarjeta.setToolTipText("Introduzca su nombre");
			tfTarjeta.setColumns(10);
			tfTarjeta.setBounds(99, 90, 345, 26);
		}
		return tfTarjeta;
	}
	private JPanel getPanelResumen() {
		if (panelResumen == null) {
			panelResumen = new JPanel();
			panelResumen.setLayout(new BorderLayout(0, 0));
			panelResumen.add(getPanelInfo(), BorderLayout.NORTH);
			panelResumen.add(getPanelBotones(), BorderLayout.SOUTH);
		}
		return panelResumen;
	}
	private JPanel getPanelInfo() {
		if (panelInfo == null) {
			panelInfo = new JPanel();
			panelInfo.setLayout(new GridLayout(0, 2, 0, 0));
			panelInfo.add(getPanelDuracion());
			panelInfo.add(getPanelPrecio());
		}
		return panelInfo;
	}
	private JPanel getPanelDuracion() {
		if (panelDuracion == null) {
			panelDuracion = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelDuracion.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelDuracion.add(getLbDuracion());
			panelDuracion.add(getTfDuracion());
		}
		return panelDuracion;
	}
	private JLabel getLbDuracion() {
		if (lbDuracion == null) {
			lbDuracion = new JLabel("Duración:");
		}
		return lbDuracion;
	}
	private JTextField getTfDuracion() {
		if (tfDuracion == null) {
			tfDuracion = new JTextField();
			tfDuracion.setEditable(false);
			tfDuracion.setForeground(new Color(255, 0, 0));
			tfDuracion.setColumns(10);
		}
		return tfDuracion;
	}
	private JPanel getPanelPrecio() {
		if (panelPrecio == null) {
			panelPrecio = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelPrecio.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelPrecio.add(getLbPrecio());
			panelPrecio.add(getTtPrecio());
		}
		return panelPrecio;
	}
	private JLabel getLbPrecio() {
		if (lbPrecio == null) {
			lbPrecio = new JLabel("Precio:");
		}
		return lbPrecio;
	}
	private JTextField getTtPrecio() {
		if (tfPrecio == null) {
			tfPrecio = new JTextField();
			tfPrecio.setEditable(false);
			tfPrecio.setForeground(Color.RED);
			tfPrecio.setColumns(10);
		}
		return tfPrecio;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelBotones.add(getBtCancelar());
			panelBotones.add(getBtConfirmar());
		}
		return panelBotones;
	}
	private JButton getBtConfirmar() {
		
		if (btConfirmar == null) {
			btConfirmar = new JButton("Confirmar");
			btConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						
						if(guardarUsuario()) {
							
							reiniciar();
							
						}
						
					} catch (SQLException e1) {
						
						JOptionPane.showMessageDialog(
					            contentPane,
					            "Se ha producido un error al guardar el usuario. ",
					            null,
					            JOptionPane.ERROR_MESSAGE);
						
						e1.printStackTrace();
					}
				}
			});
			btConfirmar.setBackground(new Color(128, 255, 128));
			btConfirmar.setMnemonic('C');
			btConfirmar.setToolTipText("Confirmar compra");
		}
		return btConfirmar;
	}
	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						reiniciar();
					} catch (SQLException e1) {
						
						JOptionPane.showMessageDialog(
					            contentPane,
					            "Se ha producido el error al cargar las instalaciones disponibles.",
					            null,
					            JOptionPane.WARNING_MESSAGE
					        );
						e1.printStackTrace();
					}
				}
			});
			btCancelar.setToolTipText("Cancelar");
			btCancelar.setMnemonic('C');
			btCancelar.setBackground(new Color(255, 128, 128));
		}
		return btCancelar;
	}
	
	private boolean guardarUsuario() throws SQLException {
		
		String name = this.getTfName().getText();
		
		String cardNumber = this.getTfTarjeta().getText();
		
		if(!checkCampos(name, cardNumber)){
			JOptionPane.showMessageDialog(
		            contentPane,
		            "Alguno de los campos está vacío.",
		            null,
		            JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		boolean isStored = this.vr.gerRealizarReserva().guardarUsuario(name, cardNumber);
		
		if(!isStored) {
			
			JOptionPane.showMessageDialog(
		            contentPane,
		            "Se ha producido un error al guardar al usuario",
		            null,
		            JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		JOptionPane.showMessageDialog(
	            contentPane,
	            "Usuario registrado correctamente",
	            "Registro completado",
	            JOptionPane.INFORMATION_MESSAGE);
		
		return true;
	}
	
	/**
	 * Comprueba que el nombre del cliente y la tarjeta no esten en blanco
	 * @param name
	 * @param cardNumber
	 * @return true si son válidos, false en caso contrario
	 */
	private boolean checkCampos(String name, String cardNumber) {
		
		return !name.isBlank() && !cardNumber.isBlank();
	}

	private void reiniciar() throws SQLException {
		
		this.vr.reiniciar();
		this.dispose();
	}
}
