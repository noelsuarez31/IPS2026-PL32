package giis.demo.ui.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import giis.demo.model.login.Autenticacion;
import giis.demo.model.rol.UsuarioAutenticado;
import giis.demo.util.ApplicationException;

public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelLoggin;
	private JLabel lbCredenciales;
	private JLabel lblUsuario;
	private JLabel lblContrasenia;
	private JTextField tfUsuario;
	private JPasswordField pfContrasenia;
	private JButton btValidar;

	private Autenticacion autenticacion;
	private UsuarioAutenticado usuarioAutenticado;

	private JFrame ventanaPadre;
	private JButton btnCerrar;
	
	public VentanaLogin(JFrame vPadre) {
		this();
		this.ventanaPadre = vPadre;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (ventanaPadre != null) {
					ventanaPadre.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public VentanaLogin() {
		
		this.autenticacion = new Autenticacion();
		
		setTitle("Identificación");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 569, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelLoggin(), BorderLayout.CENTER);
		setLocationRelativeTo(null);

	}

	private JPanel getPanelLoggin() {
		if (panelLoggin == null) {
			panelLoggin = new JPanel();
			panelLoggin.setLayout(null);
			panelLoggin.add(getLbCredenciales());
			panelLoggin.add(getLblUsuario());
			panelLoggin.add(getLblContrasenia());
			panelLoggin.add(getTfUsuario());
			panelLoggin.add(getPfContrasenia());
			panelLoggin.add(getBtValidar());
			panelLoggin.add(getBtnCerrar());
		}
		return panelLoggin;
	}
	private JLabel getLbCredenciales() {
		if (lbCredenciales == null) {
			lbCredenciales = new JLabel("Introduzca sus credenciales:");
			lbCredenciales.setBounds(90, 70, 213, 20);
		}
		return lbCredenciales;
	}
	private JLabel getLblUsuario() {
		if (lblUsuario == null) {
			lblUsuario = new JLabel("Usuario:");
			lblUsuario.setDisplayedMnemonic('S');
			lblUsuario.setLabelFor(getTfUsuario());
			lblUsuario.setBounds(90, 114, 74, 20);
		}
		return lblUsuario;
	}
	private JLabel getLblContrasenia() {
		if (lblContrasenia == null) {
			lblContrasenia = new JLabel("Contraseña:");
			lblContrasenia.setLabelFor(getPfContrasenia());
			lblContrasenia.setDisplayedMnemonic('C');
			lblContrasenia.setBounds(90, 150, 109, 20);
		}
		return lblContrasenia;
	}
	private JTextField getTfUsuario() {
		if (tfUsuario == null) {
			tfUsuario = new JTextField();
			tfUsuario.setToolTipText("Pulse para escribir su usuario");
			tfUsuario.setBounds(197, 111, 230, 26);
			tfUsuario.setColumns(10);
		}
		return tfUsuario;
	}
	private JPasswordField getPfContrasenia() {
		if (pfContrasenia == null) {
			pfContrasenia = new JPasswordField();
			pfContrasenia.setBounds(197, 147, 230, 26);
		}
		return pfContrasenia;
	}
	private JButton getBtValidar() {
		if (btValidar == null) {
			btValidar = new JButton("Validar");
			btValidar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					validar();
				}
			});
			btValidar.setBackground(new Color(128, 255, 128));
			btValidar.setMnemonic('V');
			btValidar.setBounds(285, 272, 115, 29);
		}
		return btValidar;
	}
	
	private void validar() {
	    String usuario = tfUsuario.getText().trim().toLowerCase();
	    char[] contrasenia = pfContrasenia.getPassword();

	    try {
	        if (!checkCredentials(usuario, contrasenia)) {
	            return;
	        }

	        UsuarioAutenticado usuarioA =
	                autenticacion.autenticar(usuario, new String(contrasenia));
	        this.usuarioAutenticado = usuarioA;
	        abrirVentanaPrincipal();

	    } catch (ApplicationException e) {
	        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        pfContrasenia.setText("");

	    } finally {
	        Arrays.fill(contrasenia, ' ');
	    }
	}
	
	private boolean checkCredentials(String usuario, char[] contrasenia) {
		
		return checkUsername(usuario) && checkPassword(contrasenia);

	}
	
	private boolean checkUsername(String usuario) {
		
		if(usuario.isBlank()) {
			
			JOptionPane.showMessageDialog(this, "El campo de usuario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private boolean checkPassword(char[] contrasenia) {
		
		if (contrasenia.length == 0) {
			JOptionPane.showMessageDialog(this, "El campo de contraseña no puede estar vacío", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	UsuarioAutenticado getUsuarioAutenticado() {
		return usuarioAutenticado;
	}
	
	private void abrirVentanaPrincipal() {
		
		VentanaPrincipal vp = new VentanaPrincipal(this);
		vp.setVisible(true);
		this.dispose();
	}
	
	public void reiniciar() {
		
		this.tfUsuario.setText("");
		this.pfContrasenia.setText("");
		
		this.btValidar.setEnabled(true);
		
	}
	
	
	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cerrar();
				}
			});
			btnCerrar.setMnemonic('C');
			btnCerrar.setBackground(new Color(255, 128, 128));
			btnCerrar.setBounds(407, 272, 115, 29);
		}
		return btnCerrar;
	}
	
	private void cerrar() {
		
		ventanaPadre.setVisible(true);
		this.dispose();
		
	}
}
