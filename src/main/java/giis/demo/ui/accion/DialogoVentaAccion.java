package giis.demo.ui.accion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

/**
 * Ventana emergente (modal) para fijar el precio de venta de una acción.
 */
public class DialogoVentaAccion extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel lblPregunta;
	private JSpinner spPrecio;
	private JLabel lblEuro;
	private JButton btnCancelar;
	private JButton btnConfirmar;

	private boolean confirmado = false;
	private BigDecimal precioVenta = null;

	public DialogoVentaAccion(JFrame parent) {
		super(parent, "Venta de acción", true);
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 180);
		setMinimumSize(new Dimension(300, 160));

		JPanel contentPane = new JPanel(new BorderLayout(10, 15));
		contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
		setContentPane(contentPane);

		// Zona Norte: Pregunta
		contentPane.add(getLblPregunta(), BorderLayout.NORTH);

		// Zona Centro: Campo de precio con JSpinner y símbolo €
		JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 5));
		panelInput.add(getSpPrecio());
		panelInput.add(getLblEuro());
		contentPane.add(panelInput, BorderLayout.CENTER);

		// Zona Sur: Botones Cancelar y Confirmar
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		panelBotones.add(getBtnCancelar());
		panelBotones.add(getBtnConfirmar());
		contentPane.add(panelBotones, BorderLayout.SOUTH);

		configurarEventos();
		setLocationRelativeTo(getParent());
	}

	private void configurarEventos() {
		getBtnCancelar().addActionListener(e -> {
			confirmado = false;
			dispose();
		});

		getBtnConfirmar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> confirmarPrecio()));
	}

	private void confirmarPrecio() {
		double valor = ((Number) getSpPrecio().getValue()).doubleValue();
		if (valor <= 0) {
			throw new ApplicationException("El precio por acción debe ser mayor que cero.");
		}
		this.precioVenta = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
		this.confirmado = true;
		dispose();
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	// ==================== GETTERS COMPONENTES ====================

	private JLabel getLblPregunta() {
		if (lblPregunta == null) {
			lblPregunta = new JLabel("¿Qué precio quieres poner?");
			lblPregunta.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblPregunta.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblPregunta;
	}

	private JSpinner getSpPrecio() {
		if (spPrecio == null) {
			SpinnerNumberModel model = new SpinnerNumberModel(10.00, 0.01, 1000000.00, 1.00);
			spPrecio = new JSpinner(model);
			spPrecio.setPreferredSize(new Dimension(85, 26));
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spPrecio, "0.00");
			spPrecio.setEditor(editor);
		}
		return spPrecio;
	}

	private JLabel getLblEuro() {
		if (lblEuro == null) {
			lblEuro = new JLabel("€");
			lblEuro.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		return lblEuro;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setPreferredSize(new Dimension(100, 28));
		}
		return btnCancelar;
	}

	private JButton getBtnConfirmar() {
		if (btnConfirmar == null) {
			btnConfirmar = new JButton("Confirmar");
			btnConfirmar.setPreferredSize(new Dimension(100, 28));
		}
		return btnConfirmar;
	}
}
