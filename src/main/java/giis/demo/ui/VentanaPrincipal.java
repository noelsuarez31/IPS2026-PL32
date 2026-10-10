package giis.demo.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.ui.tienda.VentanaTienda;
import giis.demo.model.entrevistas.CrearFranjaService;
import giis.demo.model.equipo.AñadirEquipoService;
import giis.demo.model.horarios.GestionHorariosPeriodicos;
import giis.demo.ui.entradas.VentanaVentaEntradas;
import giis.demo.ui.equipo.VentanaAñadirEquipo;
import giis.demo.ui.horarios.VentanaHorarioPeriodico;
import giis.demo.ui.entrevistas.VentanaCrearFranjas;

import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {
	
	private GestionaTienda gestionaTienda = new GestionaTienda();
	private AñadirEquipoService añadirEquipo = new AñadirEquipoService();
	private GestionHorariosPeriodicos ghp = new GestionHorariosPeriodicos();
	private CrearFranjaService cfs = new CrearFranjaService();

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnDirector;
	private JMenu mnEmpleado;
	private JMenu mnEncargado;
	private JMenuItem mntmVentaMerchandising;
	private JMenu mnEntrenador;
	private JMenu mnGerente;
	private JMenu mnVendedorDeEntradas;
	private JMenuItem mniVender;
	private JMenu mnTienda;
	private JMenuItem mntmVerTienda;
	private JMenuItem mntmAñadirEquipo;
	private JMenuItem mntmAñadirHorarioPeriodico;
	private JMenuItem mntmCrearFranjas;

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setTitle("Ventana Inicial");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 420);
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbInicio = new JLabel("¡Bienvenido!");
		lbInicio.setBackground(new Color(240, 240, 240));
		lbInicio.setBounds(256, 159, 90, 20);
		contentPane.add(lbInicio);
		
		this.setLocationRelativeTo(null);

	}
	
	public AñadirEquipoService getAñadirEquipoService() {
		return this.añadirEquipo;
	}
	
	public GestionaTienda getGestionaTienda() {
		return this.gestionaTienda;
	}
	
	public GestionHorariosPeriodicos getGestionHorariosPeriodicos() {
		return this.ghp;
	}
	
	private JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnDirector());
			menuBar.add(getMnEmpleado());
			menuBar.add(getMnEncargado());
			menuBar.add(getMnEntrenador());
			menuBar.add(getMnGerente());
			menuBar.add(getMnVendedorDeEntradas());
			menuBar.add(getMnTienda());
		}
		return menuBar;
	}
	private JMenu getMnDirector() {
		if (mnDirector == null) {
			mnDirector = new JMenu("Director");
			mnDirector.setMnemonic('C');
		}
		return mnDirector;
	}
	private JMenu getMnEmpleado() {
		if (mnEmpleado == null) {
			mnEmpleado = new JMenu("Empleado");
			mnEmpleado.setMnemonic('P');
		}
		return mnEmpleado;
	}
	private JMenu getMnEncargado() {
		if (mnEncargado == null) {
			mnEncargado = new JMenu("Encargado");
			mnEncargado.setMnemonic('N');
			mnEncargado.add(getMntmVentaMerchandising());
		}
		return mnEncargado;
	}
	private JMenuItem getMntmVentaMerchandising() {
		if (mntmVentaMerchandising == null) {
			mntmVentaMerchandising = new JMenuItem("Venta merchandising");
		}
		return mntmVentaMerchandising;
	}
	private JMenu getMnEntrenador() {
		if (mnEntrenador == null) {
			mnEntrenador = new JMenu("Entrenador");
			mnEntrenador.setMnemonic('T');
			mnEntrenador.add(getMntmCrearFranjas());
		}
		return mnEntrenador;
	}
	private JMenu getMnGerente() {
		if (mnGerente == null) {
			mnGerente = new JMenu("Gerente");
			mnGerente.setMnemonic('G');
			mnGerente.add(getMntmAñadirEquipo());
			mnGerente.add(getMntmAñadirHorarioPeriodico());
		}
		return mnGerente;
	}
	private JMenu getMnVendedorDeEntradas() {
		if (mnVendedorDeEntradas == null) {
			mnVendedorDeEntradas = new JMenu("Vendedor de Entradas");
			mnVendedorDeEntradas.setMnemonic('V');
			mnVendedorDeEntradas.add(getMniVender());
		}
		return mnVendedorDeEntradas;
	}
	private JMenuItem getMniVender() {
		if (mniVender == null) {
			mniVender = new JMenuItem("Venta de entradas");
			mniVender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
						try {
							abrirVentanaVentaEntradas();
						} catch (SQLException e1) {

							System.err.print("Se ha producido un error al intentar abrir la ventana de ventas");
							e1.printStackTrace();
						}
						
					
				}
			});
		}
		return mniVender;
	}
	
	private void abrirVentanaVentaEntradas() throws SQLException {
		
		VentanaVentaEntradas vve = new VentanaVentaEntradas(this);
		this.setVisible(false);
		vve.setVisible(true);
		
	}
	private JMenu getMnTienda() {
		if (mnTienda == null) {
			mnTienda = new JMenu("Tienda");
			mnTienda.setMnemonic('T');
			mnTienda.add(getMntmVerTienda());
		}
		return mnTienda;
	}
	
	private class GestionaTienda implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			abrirVentanaTienda();
		}
		
	}

	public void abrirVentanaTienda() {
		VentanaTienda vT = new VentanaTienda(this);
		this.setVisible(false);
		vT.setVisible(true);
	}
	private JMenuItem getMntmVerTienda() {
		if (mntmVerTienda == null) {
			mntmVerTienda = new JMenuItem("Ver Tienda");
			mntmVerTienda.addActionListener(gestionaTienda);
		}
		return mntmVerTienda;
	}
	private JMenuItem getMntmAñadirEquipo() {
		if (mntmAñadirEquipo == null) {
			mntmAñadirEquipo = new JMenuItem("Añadir equipo");
			mntmAñadirEquipo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaAñadirEquipo();
				}
			});
		}
		return mntmAñadirEquipo;
	}
	
	private void abrirVentanaAñadirEquipo() {
		VentanaAñadirEquipo vAñadirEquipo = new VentanaAñadirEquipo(this);
		this.setVisible(false);
		vAñadirEquipo.setVisible(true);
		
	}
	private JMenuItem getMntmAñadirHorarioPeriodico() {
		if (mntmAñadirHorarioPeriodico == null) {
			mntmAñadirHorarioPeriodico = new JMenuItem("Añadir horario periódico");
			mntmAñadirHorarioPeriodico.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaAñadirHorarioPeriodico();
				}
			});
			mntmAñadirHorarioPeriodico.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return mntmAñadirHorarioPeriodico;
	}
	
	private void abrirVentanaAñadirHorarioPeriodico() {
		VentanaHorarioPeriodico vHorarioPeriodico = new VentanaHorarioPeriodico(this);
		this.setVisible(false);
		vHorarioPeriodico.setVisible(true);
		
	}
	private JMenuItem getMntmCrearFranjas() {
		if (mntmCrearFranjas == null) {
			mntmCrearFranjas = new JMenuItem("Crear franjas");
			mntmCrearFranjas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaCrearFranjas();
				}
			});
		}
		return mntmCrearFranjas;
	}

	private void abrirVentanaCrearFranjas() {
		VentanaCrearFranjas vFranjas = new VentanaCrearFranjas(this);
		this.setVisible(false);
		vFranjas.setVisible(true);
	}
	
	public CrearFranjaService getCrearFranjaService() {
		return this.cfs;
	}
}
