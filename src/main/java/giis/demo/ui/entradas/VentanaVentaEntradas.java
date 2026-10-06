package giis.demo.ui.entradas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.exceptions.SinDisponibilidadException;
import giis.demo.model.entradas.Butaca;
import giis.demo.model.entradas.Partido;
import giis.demo.model.entradas.VentaDeEntradas;
import giis.demo.model.entradas.enumerados.TipoSeccion;
import giis.demo.model.entradas.enumerados.TipoTribuna;
import giis.demo.ui.VentanaPrincipal;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JLabel lbPrecio;
	private JTextField tfPrecio;
	private JButton btBuscarAsientos;
	private JTextField tfNoAsientosDisponibles;
	private JLabel lbAsientosReservados;
	private JTextField tfAsientosObtenidos;
	private JButton btConfirma;

	private VentaDeEntradas venta;
	private JButton btCancelar;
	private JButton btVolver;

	private VentanaPrincipal vp;

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */
	public VentanaVentaEntradas(VentanaPrincipal vp) throws SQLException {
		setTitle("Compra de entradas");
		setResizable(false);

		this.vp = vp;
		venta = new VentaDeEntradas();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 812, 487);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbSelectTribuna());
		contentPane.add(getCbTribuna());
		contentPane.add(getLbSelectSeccion());
		contentPane.add(getCbSeccion());
		contentPane.add(getLbNumEntradas());
		contentPane.add(getSpNumEntradas());
		contentPane.add(getLbPartido());
		contentPane.add(getCbPartido());
		contentPane.add(getLbPrecio());
		contentPane.add(getTfPrecio());
		contentPane.add(getBtBuscarAsientos());
		contentPane.add(getTfNoAsientosDisponibles());
		contentPane.add(getLbAsientosReservados());
		contentPane.add(getTfAsientosObtenidos());
		contentPane.add(getBtConfirma());
		contentPane.add(getBtCancelar());
		contentPane.add(getBtVolver());
		this.setLocationRelativeTo(null);
		
		if (cbPartido.getItemCount() == 0) {
			tfNoAsientosDisponibles.setText("No hay partidos próximos para vender entradas.");
			btBuscarAsientos.setEnabled(false);
		}

	}

	private JLabel getLbSelectTribuna() {
		if (lbSelectTribuna == null) {
			lbSelectTribuna = new JLabel("Selecciona la tribuna:");
			lbSelectTribuna.setDisplayedMnemonic('T');
			lbSelectTribuna.setLabelFor(getCbTribuna());
			lbSelectTribuna.setBounds(331, 42, 162, 20);
		}
		return lbSelectTribuna;
	}

	private JComboBox<TipoTribuna> getCbTribuna() {
		if (cbTribuna == null) {
			cbTribuna = new JComboBox<TipoTribuna>();
			cbTribuna.setToolTipText("Selecciona la tribuna para la entrada");
			cbTribuna.setModel(new DefaultComboBoxModel<TipoTribuna>(venta.getTipoTribuna()));
			cbTribuna.setBounds(331, 97, 162, 28);
		}
		return cbTribuna;
	}

	private JLabel getLbSelectSeccion() {
		if (lbSelectSeccion == null) {
			lbSelectSeccion = new JLabel("Selecciona la sección:");
			lbSelectSeccion.setLabelFor(getCbSeccion());
			lbSelectSeccion.setDisplayedMnemonic('S');
			lbSelectSeccion.setBounds(586, 42, 162, 20);
		}
		return lbSelectSeccion;
	}

	private JComboBox<TipoSeccion> getCbSeccion() {
		if (cbSeccion == null) {
			cbSeccion = new JComboBox<TipoSeccion>();
			cbSeccion.setModel(new DefaultComboBoxModel<TipoSeccion>(venta.getTipoSeccion()));
			cbSeccion.setToolTipText("Selecciona la seccion para la entrada");
			cbSeccion.setBounds(586, 97, 162, 28);
		}
		return cbSeccion;
	}

	private JLabel getLbNumEntradas() {
		if (lbNumEntradas == null) {
			lbNumEntradas = new JLabel("Número de asientos:");
			lbNumEntradas.setLabelFor(getSpNumEntradas());
			lbNumEntradas.setDisplayedMnemonic('N');
			lbNumEntradas.setBounds(15, 205, 235, 20);
		}
		return lbNumEntradas;
	}

	private JSpinner getSpNumEntradas() {
		if (spNumEntradas == null) {
			spNumEntradas = new JSpinner();
			spNumEntradas.setModel(new SpinnerNumberModel(1, 1, 15, 1));
			spNumEntradas.setBounds(203, 202, 47, 26);
		}
		return spNumEntradas;
	}

	private JLabel getLbPartido() throws SQLException {
		if (lbPartido == null) {
			lbPartido = new JLabel("Selecciona el partido:");
			lbPartido.setLabelFor(getCbPartido());
			lbPartido.setDisplayedMnemonic('P');
			lbPartido.setBounds(15, 42, 162, 20);
		}
		return lbPartido;
	}

	private JComboBox<Partido> getCbPartido() throws SQLException {
		if (cbPartido == null) {
			cbPartido = new JComboBox<Partido>();
			cbPartido.setModel(new DefaultComboBoxModel<Partido>(venta.getPartidos()));
			cbPartido.setToolTipText("Selecciona la tributa para la entrada");
			cbPartido.setBounds(15, 97, 235, 28);
		}
		return cbPartido;
	}

	private JLabel getLbPrecio() {
		if (lbPrecio == null) {
			lbPrecio = new JLabel("Total:");
			lbPrecio.setDisplayedMnemonic('L');
			lbPrecio.setBounds(15, 327, 235, 20);
		}
		return lbPrecio;
	}

	private JTextField getTfPrecio() {
		if (tfPrecio == null) {
			tfPrecio = new JTextField();
			tfPrecio.setBackground(new Color(255, 255, 255));
			tfPrecio.setForeground(Color.RED);
			tfPrecio.setEditable(false);
			tfPrecio.setBounds(77, 324, 146, 26);
			tfPrecio.setColumns(10);
		}
		return tfPrecio;
	}

	private JButton getBtBuscarAsientos() {
		if (btBuscarAsientos == null) {
			btBuscarAsientos = new JButton("Buscar asientos");
			btBuscarAsientos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					try {

						seleccionarAsientos();

					} catch (SQLException e1) {

						JOptionPane.showMessageDialog(null,
								"Se ha producido un error con la base de dato, pruebe otra vez", "Error",
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
			btBuscarAsientos.setBounds(331, 201, 162, 29);
		}
		return btBuscarAsientos;
	}

	private JTextField getTfNoAsientosDisponibles() {
		if (tfNoAsientosDisponibles == null) {
			tfNoAsientosDisponibles = new JTextField();
			tfNoAsientosDisponibles.setBorder(null);
			tfNoAsientosDisponibles.setBackground(new Color(255, 255, 255));
			tfNoAsientosDisponibles.setFont(new Font("Tahoma", Font.PLAIN, 10));
			tfNoAsientosDisponibles.setForeground(Color.RED);
			tfNoAsientosDisponibles.setEditable(false);
			tfNoAsientosDisponibles.setColumns(10);
			tfNoAsientosDisponibles.setBounds(331, 290, 377, 26);
		}
		return tfNoAsientosDisponibles;
	}

	private JLabel getLbAsientosReservados() {
		if (lbAsientosReservados == null) {
			lbAsientosReservados = new JLabel("Asientos:");
			lbAsientosReservados.setDisplayedMnemonic('A');
			lbAsientosReservados.setBounds(15, 400, 235, 20);
		}
		return lbAsientosReservados;
	}

	private JTextField getTfAsientosObtenidos() {
		if (tfAsientosObtenidos == null) {
			tfAsientosObtenidos = new JTextField();
			tfAsientosObtenidos.setBackground(new Color(255, 255, 255));
			tfAsientosObtenidos.setForeground(Color.RED);
			tfAsientosObtenidos.setFont(new Font("Tahoma", Font.PLAIN, 12));
			tfAsientosObtenidos.setEditable(false);
			tfAsientosObtenidos.setColumns(10);
			tfAsientosObtenidos.setBounds(108, 399, 292, 26);
		}
		return tfAsientosObtenidos;
	}

	private JButton getBtConfirma() {
		if (btConfirma == null) {
			btConfirma = new JButton("Confirmar compra");
			btConfirma.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					try {
						mostrarConfirmacionReserva();
					} catch (SQLException sqle) {

						JOptionPane.showMessageDialog(null, "No se ha podido completar la compra, vuelva a intentarlo",
								"Aviso", JOptionPane.WARNING_MESSAGE);

						reiniciar();

						System.err.print(
								"No se ha podido realizar la compra de las entradas por un error " + sqle.getMessage());

					}

				}
			});
			btConfirma.setEnabled(false);
			btConfirma.setMnemonic('F');
			btConfirma.setBackground(new Color(128, 255, 128));
			btConfirma.setBounds(586, 396, 162, 29);
		}
		return btConfirma;
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
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					reiniciar();

				}
			});
			btCancelar.setMnemonic('C');
			btCancelar.setEnabled(false);
			btCancelar.setBackground(new Color(255, 128, 128));
			btCancelar.setBounds(331, 243, 162, 29);
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
	 * Almacena la venta y entradas en la BBDD, crea la ventana de confirmacion
	 * 
	 * @throws SQLException
	 */
	private void mostrarConfirmacionReserva() throws SQLException {

		venta.almacenar(venta.getIdPartido(), venta.getButacasSeleccionadas());

		VentanaVentaConfirmacion vvc = new VentanaVentaConfirmacion(this);
		vvc.setVisible(true);
		this.dispose();

	}

	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver al menú");
			btVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					volverAlMenu();

				}
			});
			btVolver.setToolTipText("Pulsa para volver a la pestaña inicial");
			btVolver.setMnemonic('V');
			btVolver.setBackground(new Color(255, 128, 128));
			btVolver.setBounds(415, 396, 162, 29);
		}
		return btVolver;
	}

	/**
	 * Método para volver al menú principal
	 */
	private void volverAlMenu() {

		this.vp.setVisible(true);
		this.dispose();

	}
}
