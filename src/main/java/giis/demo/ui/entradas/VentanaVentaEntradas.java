package giis.demo.ui.entradas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.model.entradas.Butaca;
import giis.demo.model.entradas.Partido;
import giis.demo.model.entradas.VentaDeEntradas;
import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;
import giis.demo.exceptions.SinDisponibilidadException;
import giis.demo.ui.VentanaPrincipal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class VentanaVentaEntradas extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JLabel lbSelectTribuna;
	private JComboBox<TipoTribuna> cbTribuna;

	private JLabel lbSelectSeccion;
	private JComboBox<TipoSeccion> cbSeccion;

	private JLabel lbNumEntradas;
	private JSpinner spNumEntradas;

	private JLabel lbPartido;
	private JComboBox<Partido> cbPartido;

	private JButton btBuscarAsientos;
	private JTextField tfNoAsientosDisponibles;
	private JButton btCancelar;

	private VentaDeEntradas venta;

	private VentanaPrincipal vp;
	private JTable tablaAsientos;
	private JPanel panelLeyenda;
	private JButton btVerde;
	private JLabel lbLibre;
	private JButton btRojo;
	private JLabel lbOcupado;
	private JPanel panelTitulo;
	private JLabel lbTitulo;
	private JPanel panelInfo;
	private JLabel lbAsientosReservados;
	private JTextField tfAsientosObtenidos;
	private JLabel lbPrecio;
	private JTextField tfPrecio;
	private JPanel panelBotones;
	private JButton btVolver;
	private JButton btConfirma;

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */
	public VentanaVentaEntradas(VentanaPrincipal vp) throws SQLException {

		this.setTitle("Compra de entradas");
		this.setResizable(false);

		this.vp = vp;
		this.venta = new VentaDeEntradas();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 587);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		setContentPane(contentPane);

		contentPane.setLayout(new BorderLayout(10, 10));
		contentPane.add(getPanelSuperior(), BorderLayout.NORTH);
		contentPane.add(getPanelCentral(), BorderLayout.CENTER);
		contentPane.add(getPanelInferior(), BorderLayout.SOUTH);
		contentPane.add(getPanelVisualizacion(), BorderLayout.EAST);

		this.setLocationRelativeTo(null);

		if (cbPartido.getItemCount() == 0) {

			tfNoAsientosDisponibles.setText("No hay partidos próximos para vender entradas.");

			btBuscarAsientos.setEnabled(false);
		}
	}

	/**
	 * Panel superior: Partido, tribuna y sección.
	 * 
	 * @throws SQLException
	 */
	private JPanel getPanelSuperior() throws SQLException {

		JPanel panelSeleccion = new JPanel();

		panelSeleccion.setBackground(Color.WHITE);
		panelSeleccion.setLayout(new GridLayout(2, 3, 20, 20));

		panelSeleccion.add(getLbPartido());

		panelSeleccion.add(getLbSelectTribuna());

		panelSeleccion.add(getLbSelectSeccion());
		panelSeleccion.add(getCbPartido());
		panelSeleccion.add(getCbTribuna());
		panelSeleccion.add(getCbSeccion());

		return panelSeleccion;
	}

	/**
	 * Panel central: Número de entradas, buscar, cancelar y mensaje.
	 */
	private JPanel getPanelCentral() {

		JPanel panelNumAsientos = new JPanel();

		panelNumAsientos.setBackground(Color.WHITE);
		panelNumAsientos.setLayout(null);

		panelNumAsientos.add(getLbNumEntradas());
		panelNumAsientos.add(getSpNumEntradas());

		panelNumAsientos.add(getBtBuscarAsientos());
		panelNumAsientos.add(getBtCancelar());

		panelNumAsientos.add(getTfNoAsientosDisponibles());

		return panelNumAsientos;
	}

	/**
	 * Panel inferior: Asientos, precio, volver y confirmar.
	 */
	private JPanel getPanelInferior() {

		JPanel panelInformacion = new JPanel();

		panelInformacion.setBackground(Color.WHITE);
		panelInformacion.setLayout(new BorderLayout(0, 0));
		panelInformacion.add(getPanelBotones(), BorderLayout.EAST);
		panelInformacion.add(getPanelInfo(), BorderLayout.WEST);

		return panelInformacion;
	}

	private JLabel getLbSelectTribuna() {

		if (lbSelectTribuna == null) {
			lbSelectTribuna = new JLabel("Selecciona la tribuna:");
			lbSelectTribuna.setDisplayedMnemonic('T');
			lbSelectTribuna.setLabelFor(getCbTribuna());
		}

		return lbSelectTribuna;
	}

	private JComboBox<TipoTribuna> getCbTribuna() {

		if (cbTribuna == null) {
			cbTribuna = new JComboBox<TipoTribuna>();
			cbTribuna.setToolTipText("Selecciona la tribuna para la entrada");
			cbTribuna.setModel(new DefaultComboBoxModel<TipoTribuna>(venta.getTipoTribuna()));
		}

		return cbTribuna;
	}

	private JLabel getLbSelectSeccion() {

		if (lbSelectSeccion == null) {
			lbSelectSeccion = new JLabel("Selecciona la sección:");
			lbSelectSeccion.setLabelFor(getCbSeccion());
			lbSelectSeccion.setDisplayedMnemonic('S');
		}

		return lbSelectSeccion;
	}

	private JComboBox<TipoSeccion> getCbSeccion() {

		if (cbSeccion == null) {
			
			cbSeccion = new JComboBox<TipoSeccion>();
			cbSeccion.setModel(new DefaultComboBoxModel<TipoSeccion>(venta.getTipoSeccion()));
			cbSeccion.setToolTipText("Selecciona la sección para la entrada");
		}

		return cbSeccion;
	}

	private JLabel getLbNumEntradas() {

		if (lbNumEntradas == null) {

			lbNumEntradas = new JLabel("Número de asientos:");
			lbNumEntradas.setBounds(22, 50, 152, 30);
			lbNumEntradas.setLabelFor(getSpNumEntradas());
			lbNumEntradas.setDisplayedMnemonic('N');
		}

		return lbNumEntradas;
	}

	private JSpinner getSpNumEntradas() {

		if (spNumEntradas == null) {

			spNumEntradas = new JSpinner();
			spNumEntradas.setBounds(183, 57, 39, 20);
			spNumEntradas.setModel(new SpinnerNumberModel(1, 1, 15, 1));
		}

		return spNumEntradas;
	}

	private JLabel getLbPartido() throws SQLException {

		if (lbPartido == null) {
			lbPartido = new JLabel("Selecciona el partido:");
			lbPartido.setLabelFor(getCbPartido());
			lbPartido.setDisplayedMnemonic('P');
		}

		return lbPartido;
	}

	private JComboBox<Partido> getCbPartido() throws SQLException {

		if (cbPartido == null) {
			cbPartido = new JComboBox<Partido>();
			cbPartido.setModel(new DefaultComboBoxModel<Partido>(venta.getPartidos()));
			cbPartido.setToolTipText("Selecciona el partido para la entrada");
		}

		return cbPartido;
	}

	private JButton getBtBuscarAsientos() {

		if (btBuscarAsientos == null) {
			btBuscarAsientos = new JButton("Buscar asientos");
			btBuscarAsientos.setBounds(256, 56, 130, 28);
			btBuscarAsientos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					try {

						seleccionarAsientos();

					} catch (SQLException e1) {

						JOptionPane.showMessageDialog(null,
								"Se ha producido un error con la base de datos, pruebe otra vez", "Error",
								JOptionPane.ERROR_MESSAGE);

						System.err.print("Se ha producido un error con la BBDD");
						e1.printStackTrace();

					} catch (SinDisponibilidadException sde) {

						getTfNoAsientosDisponibles().setText(sde.getMessage());
						System.err.println(sde.getMessage());
					}
				}
			});

			btBuscarAsientos.setBackground(new Color(128, 255, 128));
			btBuscarAsientos.setMnemonic('B');
		}

		return btBuscarAsientos;
	}

	private JTextField getTfNoAsientosDisponibles() {

		if (tfNoAsientosDisponibles == null) {
			tfNoAsientosDisponibles = new JTextField();
			tfNoAsientosDisponibles.setBounds(22, 182, 304, 13);
			tfNoAsientosDisponibles.setBorder(null);
			tfNoAsientosDisponibles.setBackground(new Color(255, 255, 255));
			tfNoAsientosDisponibles.setFont(new Font("Tahoma", Font.PLAIN, 10));
			tfNoAsientosDisponibles.setForeground(Color.RED);
			tfNoAsientosDisponibles.setEditable(false);
			tfNoAsientosDisponibles.setColumns(25);
		}

		return tfNoAsientosDisponibles;
	}

	private void seleccionarAsientos() throws SQLException, SinDisponibilidadException {

		tfNoAsientosDisponibles.setText("");

		Partido partido = (Partido) this.cbPartido.getSelectedItem();
		TipoTribuna tribuna = (TipoTribuna) this.cbTribuna.getSelectedItem();
		TipoSeccion seccion = (TipoSeccion) this.cbSeccion.getSelectedItem();
		int numEntradas = (int) this.spNumEntradas.getValue();

		List<Butaca> butacasReservadas = venta.seleccionar(partido, tribuna, seccion, numEntradas);

		// Mostrar los asientos reservados

		Butaca primera = butacasReservadas.get(0);
		Butaca ultima = butacasReservadas.get(butacasReservadas.size() - 1);

		if (butacasReservadas.size() == 1) {

			tfAsientosObtenidos.setText("Fila:" + primera.getFila() + ", asiento: " + primera.getAsiento());

		} else {

			tfAsientosObtenidos.setText("Fila:" + primera.getFila() + ", asientos de: " + primera.getAsiento() + " a "
					+ ultima.getAsiento());
		}

		this.tfPrecio.setText("" + numEntradas * VentaDeEntradas.PRICE_ENTRADAS + "€");

		this.cbPartido.setEnabled(false);
		this.cbSeccion.setEnabled(false);
		this.cbTribuna.setEnabled(false);
		this.spNumEntradas.setEnabled(false);

		this.btBuscarAsientos.setEnabled(false);
		this.btCancelar.setEnabled(true);
		this.btConfirma.setEnabled(true);
		this.btVolver.setEnabled(false);
	}

	private JButton getBtCancelar() {

		if (btCancelar == null) {

			btCancelar = new JButton("Cancelar");
			btCancelar.setBounds(256, 90, 130, 28);
			btCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					reiniciar();
				}
			});

			btCancelar.setMnemonic('C');
			btCancelar.setEnabled(false);
			btCancelar.setBackground(new Color(255, 128, 128));
		}

		return btCancelar;
	}

	/**
	 * Reiniciar la interfaz gráfica para una nueva compra.
	 */
	public void reiniciar() {

		this.cbPartido.setEnabled(true);
		this.cbSeccion.setEnabled(true);
		this.cbTribuna.setEnabled(true);
		this.spNumEntradas.setEnabled(true);

		if (cbPartido.getItemCount() > 0) {

			this.cbPartido.setSelectedIndex(0);

		} else {

			tfNoAsientosDisponibles.setText("No hay partidos próximos para vender entradas.");
		}

		this.cbTribuna.setSelectedIndex(0);
		this.cbSeccion.setSelectedIndex(0);
		this.spNumEntradas.setValue(1);

		this.tfAsientosObtenidos.setText("");
		this.tfNoAsientosDisponibles.setText("");
		this.tfPrecio.setText("");

		this.btBuscarAsientos.setEnabled(cbPartido.getItemCount() > 0);
		this.btCancelar.setEnabled(false);
		this.btConfirma.setEnabled(false);
		this.btVolver.setEnabled(true);
	}

	/**
	 * Almacena la venta y entradas en la BBDD, crea la ventana de confirmación.
	 * 
	 * @throws SQLException
	 */
	private void mostrarConfirmacionReserva() throws SQLException {

		venta.almacenar(venta.getIdPartido(), venta.getButacasSeleccionadas());

		VentanaVentaConfirmacion vvc = new VentanaVentaConfirmacion(this);
		
		vvc.setVisible(true);
		venta.close();
		this.dispose();
	}

	/**
	 * Método para volver al menú principal.
	 * 
	 * @throws SQLException
	 */
	private void volverAlMenu() throws SQLException {

		this.vp.setVisible(true);
		venta.close();
		this.dispose();
	}
	private JPanel getPanelVisualizacion() {
		JPanel panelVisualizacion = new JPanel(new BorderLayout(5, 5));
	    panelVisualizacion.setBackground(Color.WHITE);
	    panelVisualizacion.add(getTablaAsientos(), BorderLayout.CENTER);
	    panelVisualizacion.add(getPanelLeyenda(), BorderLayout.SOUTH);
	    panelVisualizacion.add(getPanelTitulo(), BorderLayout.NORTH);

	    return panelVisualizacion;
	}
	
	public JTable getTablaAsientos() {
		
		if(tablaAsientos == null) {
			
			tablaAsientos = new JTable(10, 15);
		    tablaAsientos.setEnabled(false);

		    tablaAsientos.setTableHeader(null);
		    tablaAsientos.setRowHeight(22);

		    for (int i = 0; i < 15; i++) {
		        tablaAsientos.getColumnModel().getColumn(i).setPreferredWidth(25);
		    }

		    for (int fila = 0; fila < 10; fila++) {
		        for (int asiento = 0; asiento < 15; asiento++) {
		            tablaAsientos.setValueAt(asiento + 1, fila, asiento);
		        }
		    }

		    // Renderer para cambiar el aspecto de las celdas
		    tablaAsientos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
		        @Override
		        public Component getTableCellRendererComponent(
		                JTable table, Object value, boolean isSelected,
		                boolean hasFocus, int row, int column) {

		            Component c = super.getTableCellRendererComponent(
		                    table, value, isSelected, hasFocus, row, column);

		            c.setBackground(Color.GREEN);
		            c.setForeground(Color.BLACK);
		            setHorizontalAlignment(CENTER);

		            return c;
		        }
		    });
		    
		}
		return tablaAsientos;
		
	}

	private JPanel getPanelLeyenda() {
		if (panelLeyenda == null) {
			panelLeyenda = new JPanel();
			panelLeyenda.setBackground(new Color(255, 255, 255));
			panelLeyenda.setLayout(new GridLayout(2, 2, 20, 20));
			panelLeyenda.add(getBtVerde());
			panelLeyenda.add(getLbLibre());
			panelLeyenda.add(getBtRojo());
			panelLeyenda.add(getLbOcupado());
		}
		return panelLeyenda;
	}
	private JButton getBtVerde() {
		if (btVerde == null) {
			btVerde = new JButton("");
			btVerde.setBackground(Color.GREEN);
		}
		return btVerde;
	}
	private JLabel getLbLibre() {
		if (lbLibre == null) {
			lbLibre = new JLabel("Libre");
			lbLibre.setBackground(new Color(255, 255, 255));
			lbLibre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbLibre;
	}
	private JButton getBtRojo() {
		if (btRojo == null) {
			btRojo = new JButton("");
			btRojo.setBackground(Color.RED);
			btRojo.setEnabled(false);
		}
		return btRojo;
	}
	private JLabel getLbOcupado() {
		if (lbOcupado == null) {
			lbOcupado = new JLabel("Ocupado");
			lbOcupado.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lbOcupado.setBackground(Color.WHITE);
		}
		return lbOcupado;
	}
	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			panelTitulo = new JPanel();
			panelTitulo.setBackground(new Color(255, 255, 255));
			panelTitulo.add(getLbTitulo());
		}
		return panelTitulo;
	}
	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Visualización de asientos");
			lbTitulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		}
		return lbTitulo;
	}
	private JPanel getPanelInfo() {
		if (panelInfo == null) {
			panelInfo = new JPanel();
			panelInfo.setBackground(new Color(255, 255, 255));
			panelInfo.add(getLbAsientosReservados());
			panelInfo.add(getTfAsientosObtenidos());
			panelInfo.add(getLbPrecio());
			panelInfo.add(getTfPrecio());
		}
		return panelInfo;
	}
	private JLabel getLbAsientosReservados() {
		if (lbAsientosReservados == null) {
			lbAsientosReservados = new JLabel("Asientos:");
			lbAsientosReservados.setDisplayedMnemonic('A');
		}
		return lbAsientosReservados;
	}
	private JTextField getTfAsientosObtenidos() {
		if (tfAsientosObtenidos == null) {
			tfAsientosObtenidos = new JTextField();
			tfAsientosObtenidos.setForeground(Color.RED);
			tfAsientosObtenidos.setFont(new Font("Tahoma", Font.PLAIN, 12));
			tfAsientosObtenidos.setEditable(false);
			tfAsientosObtenidos.setColumns(25);
			tfAsientosObtenidos.setBackground(Color.WHITE);
		}
		return tfAsientosObtenidos;
	}
	private JLabel getLbPrecio() {
		if (lbPrecio == null) {
			lbPrecio = new JLabel("Total:");
			lbPrecio.setDisplayedMnemonic('L');
		}
		return lbPrecio;
	}
	private JTextField getTfPrecio() {
		if (tfPrecio == null) {
			tfPrecio = new JTextField();
			tfPrecio.setForeground(Color.RED);
			tfPrecio.setEditable(false);
			tfPrecio.setColumns(8);
			tfPrecio.setBackground(Color.WHITE);
		}
		return tfPrecio;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setBackground(new Color(255, 255, 255));
			FlowLayout flowLayout = (FlowLayout) panelBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelBotones.add(getBtVolver());
			panelBotones.add(getBtConfirma());
		}
		return panelBotones;
	}
	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver al menú");
			btVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						
						volverAlMenu();
						
					} catch (SQLException e1) {
						
						JOptionPane.showMessageDialog(null,
								"Se ha producido un error al cerrar la conexión con la base de datos", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.err.print("Se ha producido un error al cerrar la conexión con la BBDD");
						e1.printStackTrace();
					}
					
				}
			});
			btVolver.setToolTipText("Pulsa para volver a la pestaña inicial");
			btVolver.setMnemonic('V');
			btVolver.setBackground(new Color(255, 128, 128));
		}
		return btVolver;
	}
	private JButton getBtConfirma() {
		if (btConfirma == null) {
			btConfirma = new JButton("Confirmar compra");
			btConfirma.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						
						mostrarConfirmacionReserva();
						
					} catch (SQLException e1) {
						
						JOptionPane.showMessageDialog(null, "No se ha podido completar la compra, vuelva a intentarlo", "Aviso", 
								JOptionPane.WARNING_MESSAGE); 
						
						reiniciar();
						System.err.println( "No se ha podido realizar la compra de las entradas por un error " + e1.getMessage());
					}
					
				}
			});
			btConfirma.setMnemonic('F');
			btConfirma.setEnabled(false);
			btConfirma.setBackground(new Color(128, 255, 128));
		}
		return btConfirma;
	}
}