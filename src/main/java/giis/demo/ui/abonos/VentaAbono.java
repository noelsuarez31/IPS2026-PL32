package giis.demo.ui.abonos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;
import giis.demo.model.equipo.Equipo;
import giis.demo.ui.VentanaPrincipal;
import giis.demo.model.abonos.VentaDeAbonos;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

/**
 * Create the frame.
 */
public class VentaAbono extends JFrame {

	
	private static final long serialVersionUID = 1L;
	
	private JPanel panelUsuario;
	private JLabel lbNombre;
	private JLabel lblApellidos;
	private JLabel lblDni;
	private JLabel lblEmail;
	private JLabel lbNacimiento;
	private JTextField tfNombre;
	private JTextField tfApellidos;
	private JTextField tfDni;
	private JTextField tfEmail;
	private JSpinner spNacimiento;
	private JPanel panelSeleccion;
	private JPanel panelVisualización;
	private JPanel panelEleccion;
	private JLabel lbEquipo;
	private JComboBox<Equipo> cbEquipo;
	private JLabel lblTribuna;
	private JComboBox<TipoTribuna> cbTribuna;
	private JLabel lblSeccion;
	private JComboBox<TipoSeccion> cbSeccion;
	private JLabel lblFila;
	private JLabel lblAsiento;
	private JSpinner spNacimiento_1;
	private JSpinner spNacimiento_1_1;
	private JPanel panelTabla;
	private JPanel panelLeyenda;
	private JPanel panelPrecio;
	private JLabel lbPrecio;
	private JTextField tfPrecio;
	private JPanel panelBotones;
	private JButton btVolver;
	private JButton btCancelar;
	private JButton btFinalizar;
	
	private JTable tablaAsientos;
	private VentaDeAbonos va;
	
	private JLabel lbColorLibre;
	private JLabel lbLibre;
	private JLabel lbColorNoDisponible;
	private JLabel lbNoDisponible;
	private JLabel lbColorSeleccion;
	private JLabel lbSeleccion;
	
	private VentanaPrincipal vp;
	
	public VentaAbono(VentanaPrincipal vp) {
		
		va = new VentaDeAbonos();
		this.vp = vp;
		
		setTitle("Venta de abonos : registo de usuario");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1007, 639);
		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getPanelUsuario(), BorderLayout.NORTH);
		getContentPane().add(getPanelSeleccion(), BorderLayout.CENTER);
		getContentPane().add(getPanelVisualización(), BorderLayout.EAST);
		getContentPane().add(getPanelEleccion(), BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
	}

	private JPanel getPanelUsuario() {
		
		if (panelUsuario == null) {
			panelUsuario = new JPanel();
			panelUsuario.setPreferredSize(new Dimension(0, 240));
			panelUsuario.setBorder(
					new TitledBorder(null, "Datos del Usuario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelUsuario.setLayout(null);
			panelUsuario.add(getLbNombre());
			panelUsuario.add(getLblApellidos());
			panelUsuario.add(getLblDni());
			panelUsuario.add(getLblEmail());
			panelUsuario.add(getLbNacimiento());
			panelUsuario.add(getTfNombre());
			panelUsuario.add(getTfApellidos());
			panelUsuario.add(getTfDni());
			panelUsuario.add(getTfEmail());
			panelUsuario.add(getSpNacimiento());
		}
		return panelUsuario;
	}

	private JLabel getLbNombre() {
		if (lbNombre == null) {
			lbNombre = new JLabel("Nombre:");
			lbNombre.setBounds(11, 30, 63, 20);
			lbNombre.setDisplayedMnemonic('R');
			lbNombre.setLabelFor(getTfNombre());
		}
		return lbNombre;
	}

	private JLabel getLblApellidos() {
		if (lblApellidos == null) {
			lblApellidos = new JLabel("Apellidos:");
			lblApellidos.setBounds(11, 66, 71, 20);
			lblApellidos.setDisplayedMnemonic('P');
			lblApellidos.setLabelFor(getTfApellidos());
		}
		return lblApellidos;
	}

	private JLabel getLblDni() {
		if (lblDni == null) {
			lblDni = new JLabel("Dni:");
			lblDni.setBounds(11, 102, 30, 20);
			lblDni.setDisplayedMnemonic('N');
			lblDni.setLabelFor(getTfDni());
		}
		return lblDni;
	}

	private JLabel getLblEmail() {
		if (lblEmail == null) {
			lblEmail = new JLabel("Email:");
			lblEmail.setBounds(11, 182, 45, 20);
			lblEmail.setDisplayedMnemonic('M');
			lblEmail.setLabelFor(getTfEmail());
		}
		return lblEmail;
	}

	private JLabel getLbNacimiento() {
		if (lbNacimiento == null) {
			lbNacimiento = new JLabel("Fecha de nacimiento:");
			lbNacimiento.setBounds(11, 142, 150, 20);
			lbNacimiento.setDisplayedMnemonic('F');
			lbNacimiento.setLabelFor(getSpNacimiento());
		}
		return lbNacimiento;
	}

	private JTextField getTfNombre() {
		if (tfNombre == null) {
			tfNombre = new JTextField();
			tfNombre.setBounds(97, 30, 280, 26);
			tfNombre.setColumns(10);
		}
		return tfNombre;
	}

	private JTextField getTfApellidos() {
		if (tfApellidos == null) {
			tfApellidos = new JTextField();
			tfApellidos.setBounds(97, 66, 280, 26);
			tfApellidos.setColumns(10);
		}
		return tfApellidos;
	}

	private JTextField getTfDni() {
		if (tfDni == null) {
			tfDni = new JTextField();
			tfDni.setBounds(97, 102, 280, 26);
			tfDni.setColumns(10);
		}
		return tfDni;
	}

	private JTextField getTfEmail() {
		if (tfEmail == null) {
			tfEmail = new JTextField();
			tfEmail.setBounds(101, 179, 280, 26);
			tfEmail.setColumns(10);
		}
		return tfEmail;
	}

	private JSpinner getSpNacimiento() {
		if (spNacimiento == null) {
			spNacimiento = new JSpinner();
			spNacimiento.setBounds(176, 139, 53, 26);
		}
		return spNacimiento;
	}
	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			panelSeleccion = new JPanel();
			panelSeleccion.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Selecci\u00F3n de asientos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelSeleccion.setLayout(null);
			panelSeleccion.add(getLbEquipo());
			panelSeleccion.add(getCbEquipo());
			panelSeleccion.add(getLblTribuna());
			panelSeleccion.add(getCbTribuna());
			panelSeleccion.add(getLblSeccion());
			panelSeleccion.add(getCbSeccion());
			panelSeleccion.add(getLblFila());
			panelSeleccion.add(getLblAsiento());
			panelSeleccion.add(getSpNacimiento_1());
			panelSeleccion.add(getSpNacimiento_1_1());
		}
		return panelSeleccion;
	}
	private JPanel getPanelVisualización() {
		if (panelVisualización == null) {
			panelVisualización = new JPanel();
			panelVisualización.setPreferredSize(new Dimension(420, 0));
			panelVisualización.setLayout(new BorderLayout(0, 0));
			panelVisualización.add(getPanelTabla(), BorderLayout.CENTER);
			panelVisualización.add(getPanelLeyenda(), BorderLayout.SOUTH);
		}
		return panelVisualización;
	}
	private JPanel getPanelEleccion() {
		if (panelEleccion == null) {
			panelEleccion = new JPanel();
			panelEleccion.setLayout(new BorderLayout(0, 0));
			panelEleccion.add(getPanelPrecio(), BorderLayout.WEST);
			panelEleccion.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panelEleccion;
	}
	private JLabel getLbEquipo() {
		if (lbEquipo == null) {
			lbEquipo = new JLabel("Equipo:");
			lbEquipo.setDisplayedMnemonic('R');
			lbEquipo.setBounds(15, 40, 63, 20);
		}
		return lbEquipo;
	}
	private JComboBox<Equipo> getCbEquipo() {
		if (cbEquipo == null) {
			cbEquipo = new JComboBox<Equipo>();
			try {
				cbEquipo.setModel(new DefaultComboBoxModel<Equipo>(va.getEquiposLocales()));
			} catch (SQLException e) {
				//JOptionPane()				
				e.printStackTrace();
			}
			cbEquipo.setBounds(93, 36, 298, 28);
		}
		return cbEquipo;
	}
	private JLabel getLblTribuna() {
		if (lblTribuna == null) {
			lblTribuna = new JLabel("Tribuna:");
			lblTribuna.setDisplayedMnemonic('R');
			lblTribuna.setBounds(15, 84, 63, 20);
		}
		return lblTribuna;
	}
	private JComboBox<TipoTribuna> getCbTribuna() {
		if (cbTribuna == null) {
			cbTribuna = new JComboBox<TipoTribuna>(va.getTipoTribuna());
			cbTribuna.setBounds(93, 80, 298, 28);
		}
		return cbTribuna;
	}
	private JLabel getLblSeccion() {
		if (lblSeccion == null) {
			lblSeccion = new JLabel("Seccion:");
			lblSeccion.setDisplayedMnemonic('R');
			lblSeccion.setBounds(15, 127, 63, 20);
		}
		return lblSeccion;
	}
	private JComboBox<TipoSeccion> getCbSeccion() {
		if (cbSeccion == null) {
			cbSeccion = new JComboBox<TipoSeccion>(va.getTipoSeccion());
			cbSeccion.setBounds(93, 123, 298, 28);
		}
		return cbSeccion;
	}
	private JLabel getLblFila() {
		if (lblFila == null) {
			lblFila = new JLabel("Fila:");
			lblFila.setDisplayedMnemonic('R');
			lblFila.setBounds(406, 40, 63, 20);
		}
		return lblFila;
	}
	private JLabel getLblAsiento() {
		if (lblAsiento == null) {
			lblAsiento = new JLabel("Asiento:");
			lblAsiento.setDisplayedMnemonic('R');
			lblAsiento.setBounds(406, 84, 63, 20);
		}
		return lblAsiento;
	}
	private JSpinner getSpNacimiento_1() {
		if (spNacimiento_1 == null) {
			spNacimiento_1 = new JSpinner();
			spNacimiento_1.setBounds(480, 38, 53, 26);
		}
		return spNacimiento_1;
	}
	private JSpinner getSpNacimiento_1_1() {
		if (spNacimiento_1_1 == null) {
			spNacimiento_1_1 = new JSpinner();
			spNacimiento_1_1.setBounds(480, 81, 53, 26);
		}
		return spNacimiento_1_1;
	}
	private JPanel getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new JPanel();
			panelTabla.setLayout(new BorderLayout(0,0));
			panelTabla.add(getTablaAsientos(), BorderLayout.CENTER);
		}
		return panelTabla;
	}
	private JPanel getPanelLeyenda() {
		if (panelLeyenda == null) {
			panelLeyenda = new JPanel();
			panelLeyenda.setBorder(new TitledBorder(null, "Leyenda", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelLeyenda.setLayout(new GridLayout(3, 2, 5, 5));
			panelLeyenda.add(getLbColorLibre());
			panelLeyenda.add(getLbLibre());
			panelLeyenda.add(getLbColorNoDisponible());
			panelLeyenda.add(getLbNoDisponible());
			panelLeyenda.add(getLbColorSeleccion());
			panelLeyenda.add(getLbSeleccion());
		}
		return panelLeyenda;
	}
	
	private JLabel getLbColorLibre() {
		if (lbColorLibre == null) {
			lbColorLibre = new JLabel();
			lbColorLibre.setOpaque(true);
			lbColorLibre.setBackground(Color.GREEN);
		}
		return lbColorLibre;
	}
	private JLabel getLbLibre() {
		if (lbLibre == null) {
			lbLibre = new JLabel("Libre");
		}
		return lbLibre;
	}
	private JLabel getLbColorNoDisponible() {
		if (lbColorNoDisponible == null) {
			lbColorNoDisponible = new JLabel();
			lbColorNoDisponible.setOpaque(true);
			lbColorNoDisponible.setBackground(Color.RED);
		}
		return lbColorNoDisponible;
	}
	private JLabel getLbNoDisponible() {
		if (lbNoDisponible == null) {
			lbNoDisponible = new JLabel("No disponible");
		}
		return lbNoDisponible;
	}
	private JLabel getLbColorSeleccion() {
		if (lbColorSeleccion == null) {
			lbColorSeleccion = new JLabel();
			lbColorSeleccion.setOpaque(true);
			lbColorSeleccion.setBackground(Color.BLUE);
		}
		return lbColorSeleccion;
	}
	private JLabel getLbSeleccion() {
		if (lbSeleccion == null) {
			lbSeleccion = new JLabel("Tu asiento");
		}
		return lbSeleccion;
	}
	
	private JPanel getPanelPrecio() {
		if (panelPrecio == null) {
			panelPrecio = new JPanel();
			panelPrecio.add(getLbPrecio());
			panelPrecio.add(getTfPrecio());
		}
		return panelPrecio;
	}
	private JLabel getLbPrecio() {
		if (lbPrecio == null) {
			lbPrecio = new JLabel("Precio:");
		}
		return lbPrecio;
	}
	private JTextField getTfPrecio() {
		if (tfPrecio == null) {
			tfPrecio = new JTextField();
			tfPrecio.setEditable(false);
			tfPrecio.setForeground(new Color(255, 0, 0));
			tfPrecio.setColumns(10);
		}
		return tfPrecio;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.add(getBtVolver());
			panelBotones.add(getBtCancelar());
			panelBotones.add(getBtFinalizar());
		}
		return panelBotones;
	}
	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver");
			btVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						volverAlMenu();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			btVolver.setMnemonic('V');
			btVolver.setToolTipText("Pulsa para volver al menú inicial");
		}
		return btVolver;
	}
	
	private void volverAlMenu() throws SQLException {
		
		this.dispose();
		va.close();
		vp.setVisible(true);
		
	}
	
	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					reiniciar();
				}
			});
			btCancelar.setToolTipText("Cancelar la compra");
			btCancelar.setMnemonic('C');
		}
		return btCancelar;
	}
	private JButton getBtFinalizar() {
		if (btFinalizar == null) {
			btFinalizar = new JButton("Finalizar");
			btFinalizar.setToolTipText("Pulsa para finalizar la compra");
			btFinalizar.setMnemonic('F');
		}
		return btFinalizar;
	}
	
	private void reiniciar() {
		//TODO;
	}
	
	private JTable getTablaAsientos() {
		
	    if (tablaAsientos == null) {
	    	
	        tablaAsientos = new JTable(10, 15);
	        tablaAsientos.setTableHeader(null);
	        tablaAsientos.setRowHeight(22);
	        tablaAsientos.setShowGrid(true);
	        tablaAsientos.setGridColor(Color.BLACK);

	        for (int i = 0; i < 15; i++) {
	            tablaAsientos.getColumnModel().getColumn(i).setPreferredWidth(25);
	        }
	        for (int fila = 0; fila < 10; fila++) {
	            for (int asiento = 0; asiento < 15; asiento++) {
	                tablaAsientos.setValueAt(asiento + 1, fila, asiento);
	            }
	        }

	        tablaAsientos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
	            @Override
	            public Component getTableCellRendererComponent(JTable table, Object value,
	                    boolean isSelected, boolean hasFocus, int row, int column) {
	                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                c.setBackground(Color.GREEN);
	                c.setForeground(Color.BLACK);
	                setHorizontalAlignment(CENTER);
	                return c;
	            }
	        });
	    }
	    return tablaAsientos;
	}
}
