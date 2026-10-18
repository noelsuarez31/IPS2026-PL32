package giis.demo.ui.reserva;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.model.reserva.Instalacion;
import giis.demo.model.reserva.Reserva;
import giis.demo.model.reserva.Usuario;

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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
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

		actualizarPrecioYDuracion();

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
			lbTarjeta.setToolTipText("Introduzca su tarjeta para realizar el pago");
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

						if (guardarUsuario()) {

							vr.reiniciar();
							dispose();
						}

					} catch (SQLException e1) {

						JOptionPane.showMessageDialog(contentPane,
								"Se ha producido un error al registrarse la reserva. ", "Error",
								JOptionPane.ERROR_MESSAGE);

						e1.printStackTrace();
					}
				}
			});
			btConfirmar.setBackground(new Color(128, 255, 128));
			btConfirmar.setMnemonic('N');
			btConfirmar.setToolTipText("Confirmar compra");
		}
		return btConfirmar;
	}

	private JButton getBtCancelar() {

		if (btCancelar == null) {

			btCancelar = new JButton("Cancelar");
			btCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					reiniciar();
				}
			});
			btCancelar.setToolTipText("Cancelar");
			btCancelar.setMnemonic('C');
			btCancelar.setBackground(new Color(255, 128, 128));
		}
		return btCancelar;
	}

	/**
	 * Metodo auxiliar para guardar el usuario y hacer la reserva
	 */
	private boolean guardarUsuario() throws SQLException {

		String name = this.getTfName().getText();
		String cardNumber = this.getTfTarjeta().getText();

		if (!checkCampos(name, cardNumber)) {
			JOptionPane.showMessageDialog(contentPane,
					"Alguno de los campos está vacío o no es válido,"
							+ " revise el nombre y la tarjet que debe tener 16 dígitos.",
					null, JOptionPane.WARNING_MESSAGE);

			return false;
		}

		Usuario user = new Usuario(name, cardNumber);
		Instalacion instalacion = (Instalacion) vr.getCbInstalacion().getSelectedItem();

		if (instalacion == null) {

			JOptionPane.showMessageDialog(contentPane, "Debes seleccionar una instalación.", "Error",
					JOptionPane.WARNING_MESSAGE);
			return false;

		}

		Date d = vr.getDcFecha().getDate();
		if (d == null) {
			JOptionPane.showMessageDialog(contentPane, "Debes seleccionar una fecha.", "Error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		LocalDate fecha = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalTime horaInicio = ((Date) vr.getSpInicio().getValue()).toInstant().atZone(ZoneId.systemDefault())
				.toLocalTime();
		LocalTime horaFin = ((Date) vr.getSpFin().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		int idInstalacion = instalacion.getInstalacionId();

		boolean disponible = vr.getRealizarReserva().comprobarDisponibilidad(idInstalacion, fecha, horaInicio, horaFin);

		if (!disponible) {

			JOptionPane.showMessageDialog(contentPane,
					"La instalación ya no está disponible en ese horario. " + "Consulta de nuevo las franjas.",
					"Horario no disponible", JOptionPane.WARNING_MESSAGE);

			dispose();

			return false;
		}

		this.vr.getRealizarReserva().guardarUsuario(user);
		Reserva reserva = new Reserva(fecha, horaInicio, horaFin, 0, LocalDate.now(), user.getUsuarioId(),
				idInstalacion);

		boolean registrada = vr.getRealizarReserva().registrarNuevaReserva(reserva);

		if (!registrada) {
			JOptionPane.showMessageDialog(contentPane, "No se ha podido registrar la reserva.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		JOptionPane.showMessageDialog(contentPane,
				"Reserva realizada correctamente.\n\n" + "Instalación: " + instalacion.getName() + "\n" + "Fecha: "
						+ fecha + "\n" + "Horario: " + horaInicio + " - " + horaFin + "\n" + "Importe: "
						+ reserva.getImporte() + " €",
				"Reserva confirmada", JOptionPane.INFORMATION_MESSAGE);

		return true;
	}

	/**
	 * Comprueba que el nombre del cliente y la tarjeta no esten en blanco
	 * 
	 * @param name
	 * @param cardNumber
	 * @return true si son válidos, false en caso contrario
	 */
	private boolean checkCampos(String name, String cardNumber) {

		String tarjeta = cardNumber.replaceAll("\\s", "");
		return !name.isBlank() && tarjeta.matches("\\d{16}");
	}

	/**
	 * Actualiza las etiquetas de precio y duracion en función de los datos
	 * introducidos en la pantalla anterior
	 */
	private void actualizarPrecioYDuracion() {

		LocalTime horaInicio = ((Date) vr.getSpInicio().getValue()).toInstant().atZone(ZoneId.systemDefault())
				.toLocalTime();
		LocalTime horaFin = ((Date) vr.getSpFin().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

		long minutos = Duration.between(horaInicio, horaFin).toMinutes();
		long horas = minutos / 60;
		double precio = horas * 50.0;

		getTfDuracion().setText(horas + (horas > 1 ? " horas" : " hora"));
		getTtPrecio().setText(String.format("%.2f €", precio));
	}

	/**
	 * Vuelve a la pestaña anterior
	 */
	private void reiniciar() {

		this.dispose();
	}
}
