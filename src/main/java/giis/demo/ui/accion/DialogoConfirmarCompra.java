package giis.demo.ui.accion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Ventana emergente (modal) de confirmación con el resumen de la compra de acciones
 * (Nº Acciones e Importe).
 */
public class DialogoConfirmarCompra extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel lblNumeroAccionesTexto;
	private JLabel lblNumeroAcciones;
	private JLabel lblImporteTexto;
	private JLabel lblImporte;

	private JButton btnCancelar;
	private JButton btnConfirmar;

	private boolean confirmado = false;

	public DialogoConfirmarCompra(JFrame parent, int numAcciones, BigDecimal importeTotal) {
		super(parent, "Confirmar compra", true);
		inicializarComponentes(numAcciones, importeTotal);
	}

	public DialogoConfirmarCompra(JFrame parent, int numAcciones, String vendedores, BigDecimal importeTotal) {
		this(parent, numAcciones, importeTotal);
	}

	private void inicializarComponentes(int numAcciones, BigDecimal importeTotal) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 340, 190);
		setMinimumSize(new Dimension(320, 170));

		JPanel contentPane = new JPanel(new BorderLayout(10, 15));
		contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
		setContentPane(contentPane);

		// Panel Central: Datos del resumen de compra
		JPanel panelDatos = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.anchor = GridBagConstraints.WEST;

		// Fila 1: Nº Acciones
		gbc.gridx = 0;
		gbc.gridy = 0;
		panelDatos.add(getLblNumeroAccionesTexto(), gbc);

		gbc.gridx = 1;
		getLblNumeroAcciones().setText(String.valueOf(numAcciones));
		panelDatos.add(getLblNumeroAcciones(), gbc);

		// Fila 2: Importe
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelDatos.add(getLblImporteTexto(), gbc);

		gbc.gridx = 1;
		String strImporte = (importeTotal != null)
				? String.format(Locale.GERMAN, "%.2f €", importeTotal)
				: "0,00 €";
		getLblImporte().setText(strImporte);
		panelDatos.add(getLblImporte(), gbc);

		contentPane.add(panelDatos, BorderLayout.CENTER);

		// Panel Sur: Botones Cancelar y Confirmar
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
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

		getBtnConfirmar().addActionListener(e -> {
			confirmado = true;
			dispose();
		});
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	// ==================== GETTERS COMPONENTES ====================

	private JLabel getLblNumeroAccionesTexto() {
		if (lblNumeroAccionesTexto == null) {
			lblNumeroAccionesTexto = new JLabel("Nº Acciones :");
			lblNumeroAccionesTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblNumeroAccionesTexto;
	}

	private JLabel getLblNumeroAcciones() {
		if (lblNumeroAcciones == null) {
			lblNumeroAcciones = new JLabel("0");
			lblNumeroAcciones.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblNumeroAcciones.setForeground(new Color(0, 102, 204));
		}
		return lblNumeroAcciones;
	}

	private JLabel getLblImporteTexto() {
		if (lblImporteTexto == null) {
			lblImporteTexto = new JLabel("Importe :");
			lblImporteTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblImporteTexto;
	}

	private JLabel getLblImporte() {
		if (lblImporte == null) {
			lblImporte = new JLabel("0,00 €");
			lblImporte.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblImporte.setForeground(new Color(0, 102, 204));
		}
		return lblImporte;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setPreferredSize(new Dimension(105, 30));
		}
		return btnCancelar;
	}

	private JButton getBtnConfirmar() {
		if (btnConfirmar == null) {
			btnConfirmar = new JButton("Confirmar");
			btnConfirmar.setPreferredSize(new Dimension(105, 30));
		}
		return btnConfirmar;
	}
}
