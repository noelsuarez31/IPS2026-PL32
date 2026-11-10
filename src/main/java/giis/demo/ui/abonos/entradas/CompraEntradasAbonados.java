package giis.demo.ui.abonos.entradas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import giis.demo.exceptions.AbonoNoPermitidoException;
import giis.demo.exceptions.EmailException;
import giis.demo.model.abonos.Abonado;
import giis.demo.model.abonos.Abono;
import giis.demo.model.email.ResumenCompra;
import giis.demo.model.email.ServicioEmail;
import giis.demo.model.email.ValidadorEmail;
import giis.demo.model.entradas.Partido;
import giis.demo.model.entradas.VentaDeEntradas;

public class CompraEntradasAbonados extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelDatosAbonado;
	private JLabel lbNombre;
	private JLabel lblDni;
	private JLabel lblTemporada;
	private JLabel lblCategoria;
	private JLabel lblAsiento;
	private JTextField tfName;
	private JTextField tfDni;
	private JTextField tfSeason;
	private JTextField tfCategory;
	private JTextField tfChair;
	private JPanel panelElección;
	private JPanel panel;
	private JPanel panelPrecio;
	private JLabel lblPrecio;
	private JTextField tfPrecio;
	private JPanel panelBotones;
	private JButton btCancelar;
	private JButton btFinalizar;
	private JLabel lbPartido;
	private JComboBox<Partido> cbPartido;
	private JLabel lbEmail;
	private JTextField tfEmail;

	private VentanaIdentificacion vi;
	private VentaDeEntradas venta;
	private ServicioEmail se;

	/**
	 * Create the frame.
	 */
	public CompraEntradasAbonados(VentanaIdentificacion vi) {

		this.vi = vi;
		this.venta = vi.getVentaDeEntradas();
		this.se = new ServicioEmail();

		setTitle("Abonado : Compra Entrada");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 882, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelDatosAbonado(), BorderLayout.CENTER);
		contentPane.add(getPanelElección(), BorderLayout.EAST);
		contentPane.add(getPanel(), BorderLayout.SOUTH);

		this.setLocationRelativeTo(null);

		rellenarDatos();
	}

	private JPanel getPanelDatosAbonado() {

		if (panelDatosAbonado == null) {
			panelDatosAbonado = new JPanel();
			panelDatosAbonado
					.setBorder(new TitledBorder(null, "Abonado", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatosAbonado.setLayout(null);
			panelDatosAbonado.add(getLbNombre());
			panelDatosAbonado.add(getLblDni());
			panelDatosAbonado.add(getLblTemporada());
			panelDatosAbonado.add(getLblCategoria());
			panelDatosAbonado.add(getLblAsiento());
			panelDatosAbonado.add(getTfName());
			panelDatosAbonado.add(getTfDni());
			panelDatosAbonado.add(getTfSeason());
			panelDatosAbonado.add(getTfCategory());
			panelDatosAbonado.add(getTfChair());
		}
		return panelDatosAbonado;
	}

	private JLabel getLbNombre() {
		if (lbNombre == null) {
			lbNombre = new JLabel("Nombre:");
			lbNombre.setBounds(10, 53, 79, 14);
		}
		return lbNombre;
	}

	private JLabel getLblDni() {
		if (lblDni == null) {
			lblDni = new JLabel("Dni");
			lblDni.setBounds(10, 94, 79, 14);
		}
		return lblDni;
	}

	private JLabel getLblTemporada() {
		if (lblTemporada == null) {
			lblTemporada = new JLabel("Temporada:");
			lblTemporada.setBounds(10, 136, 79, 14);
		}
		return lblTemporada;
	}

	private JLabel getLblCategoria() {
		if (lblCategoria == null) {
			lblCategoria = new JLabel("Categoria");
			lblCategoria.setBounds(10, 180, 79, 14);
		}
		return lblCategoria;
	}

	private JLabel getLblAsiento() {
		if (lblAsiento == null) {
			lblAsiento = new JLabel("Asiento:");
			lblAsiento.setBounds(10, 223, 79, 14);
		}
		return lblAsiento;
	}

	private JTextField getTfName() {
		if (tfName == null) {
			tfName = new JTextField();
			tfName.setEditable(false);
			tfName.setBounds(83, 42, 260, 31);
			tfName.setColumns(10);
		}
		return tfName;
	}

	private JTextField getTfDni() {
		if (tfDni == null) {
			tfDni = new JTextField();
			tfDni.setEditable(false);
			tfDni.setColumns(10);
			tfDni.setBounds(83, 86, 260, 31);
		}
		return tfDni;
	}

	private JTextField getTfSeason() {
		if (tfSeason == null) {
			tfSeason = new JTextField();
			tfSeason.setEditable(false);
			tfSeason.setColumns(10);
			tfSeason.setBounds(83, 128, 260, 30);
		}
		return tfSeason;
	}

	private JTextField getTfCategory() {
		if (tfCategory == null) {
			tfCategory = new JTextField();
			tfCategory.setEditable(false);
			tfCategory.setColumns(10);
			tfCategory.setBounds(83, 170, 260, 34);
		}
		return tfCategory;
	}

	private JTextField getTfChair() {
		if (tfChair == null) {
			tfChair = new JTextField();
			tfChair.setEditable(false);
			tfChair.setColumns(10);
			tfChair.setBounds(83, 213, 260, 34);
		}
		return tfChair;
	}

	private JPanel getPanelElección() {
		if (panelElección == null) {
			panelElección = new JPanel();
			panelElección.setLayout(null);
			panelElección.setPreferredSize(new Dimension(400, 0));
			panelElección.add(getLbPartido());
			panelElección.add(getCbPartido());
			panelElección.add(getLbEmail());
			panelElección.add(getTfEmail());
		}
		return panelElección;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getPanelPrecio(), BorderLayout.WEST);
			panel.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panel;
	}

	private JPanel getPanelPrecio() {
		if (panelPrecio == null) {
			panelPrecio = new JPanel();
			panelPrecio.add(getLblPrecio());
			panelPrecio.add(getTfPrecio());
		}
		return panelPrecio;
	}

	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("Precio:");
		}
		return lblPrecio;
	}

	private JTextField getTfPrecio() {
		if (tfPrecio == null) {
			tfPrecio = new JTextField();
			tfPrecio.setForeground(new Color(255, 0, 0));
			tfPrecio.setEditable(false);
			tfPrecio.setColumns(10);
		}
		return tfPrecio;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.add(getBtCancelar());
			panelBotones.add(getBtFinalizar());
		}
		return panelBotones;
	}

	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					cancelar();

				}
			});
			btCancelar.setBackground(new Color(255, 128, 128));
			btCancelar.setMnemonic('C');
		}
		return btCancelar;
	}

	/**
	 * Cancela la operacion
	 */
	private void cancelar() {

		this.setVisible(false);

		vi.reiniciar();
		vi.setVisible(true);
		this.dispose();

	}

	private JButton getBtFinalizar() {
		if (btFinalizar == null) {
			btFinalizar = new JButton("Finalizar");
			btFinalizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					finalizar();
				}
			});
			btFinalizar.setMnemonic('F');
			btFinalizar.setBackground(new Color(128, 255, 128));
		}
		return btFinalizar;
	}

	private void finalizar() {

		Partido partido = (Partido) getCbPartido().getSelectedItem();
		if (partido == null) {
			return;
		}

		String email = getTfEmail().getText().trim();
		if (!ValidadorEmail.esValido(email)) {
			JOptionPane.showMessageDialog(this, "Introduce un email válido.");
			return;
		}

		int idVenta;

		try {

			idVenta = venta.venderEntradaAbonado(vi.getDni(), partido);

		} catch (AbonoNoPermitidoException e) {

			JOptionPane.showMessageDialog(this, e.getMessage());
			return;

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(this, "Error al registrar la compra: " + e.getMessage());
			return;
		}

		try {

			ResumenCompra resumen = new ResumenCompra("Entrada",
					"Entrada del día del club (nº de venta " + idVenta + ")", LocalDateTime.now(),
					partido.getPrecioAbonado());
			resumen.addLinea("Entrada " + partido + " - " + getTfChair().getText(), 1, partido.getPrecioAbonado());
			se.enviar(email, resumen.getAsunto(), resumen.getCuerpo(), resumen.getCuerpoHtml());

		} catch (EmailException | RuntimeException e) {

			JOptionPane.showMessageDialog(this, "No se ha podido enviar el email al usuario");
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(this, "Entrada vendida correctamente.");

		cancelar();

	}

	private JLabel getLbPartido() {
		if (lbPartido == null) {
			lbPartido = new JLabel("Partido:");
			lbPartido.setBounds(10, 49, 46, 14);
		}
		return lbPartido;
	}

	private JComboBox<Partido> getCbPartido() {

		if (cbPartido == null) {

			cbPartido = new JComboBox<Partido>();

			try {

				cbPartido.setModel(new DefaultComboBoxModel<Partido>(venta.getPartidosDelDiaDelClub(vi.getDni())));

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null,
						"Se ha producido un error al obtener los partidos declarados como día del club");
			}

			cbPartido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarAbono();
				}
			});

			cbPartido.setBounds(56, 45, 334, 22);

		}
		return cbPartido;
	}

	private JLabel getLbEmail() {
		if (lbEmail == null) {
			lbEmail = new JLabel("Email:");
			lbEmail.setBounds(10, 85, 36, 14);
		}
		return lbEmail;
	}

	private JTextField getTfEmail() {

		if (tfEmail == null) {
			tfEmail = new JTextField();
			tfEmail.setBounds(56, 82, 334, 27);
			tfEmail.setColumns(10);
		}
		return tfEmail;
	}

	private void rellenarDatos() {

		String dni = vi.getDni();

		Abonado abonado;

		try {
			abonado = venta.getObtenerDatosAbonado(dni);
		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Se ha producido un error al obtener los datos del abonado");
			return;
		}
		
		if (abonado == null){
			return;
		}

		this.getTfName().setText(abonado.getNombre() + " " + abonado.getApellidos());
		this.getTfDni().setText(abonado.getDni());

		if (getCbPartido().getItemCount() == 0) {
			getBtFinalizar().setEnabled(false);
			JOptionPane.showMessageDialog(this, "No hay partidos del día del club para el equipo de este abonado.");
			return;
		}

		cargarAbono();
	}

	private void cargarAbono() {

		Partido partido = (Partido) getCbPartido().getSelectedItem();
		if (partido == null) {
			return;
		}

		try {
			Abono abono = venta.getAbonoParaPartido(vi.getDni(), partido);

			if (abono == null) {
				limpiarAbono();
				getBtFinalizar().setEnabled(false);
				JOptionPane.showMessageDialog(this, "El cliente no es abonado de este equipo en esta temporada.");
				return;
			}

			getTfSeason().setText(abono.getFechaIncio() + " - " + abono.getFechaFin());
			getTfCategory().setText(abono.getCategoria().toString());
			getTfChair().setText("Tribuna " + abono.getTribuna() + ", Sección " + abono.getSeccion() + ", Fila "
					+ abono.getFila() + ", Asiento " + abono.getAsiento());
			getTfPrecio().setText(partido.getPrecioAbonado() + " €");
			getBtFinalizar().setEnabled(true);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	private void limpiarAbono() {

		getTfSeason().setText("");
		getTfCategory().setText("");
		getTfChair().setText("");
		getTfPrecio().setText("");
	}

}
