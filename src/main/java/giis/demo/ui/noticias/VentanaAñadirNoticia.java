package giis.demo.ui.noticias;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;

import giis.demo.model.noticias.AñadirNoticiaService;
import giis.demo.ui.VentanaPrincipal;

public class VentanaAñadirNoticia extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vPrincipal;
	private AñadirNoticiaService service;

	private JPanel contentPane;
	private JPanel pnNorte;
	private JPanel pnCentro;
	private JPanel pnSur;

	private JLabel lbTituloVentana;
	private JLabel lbSubtituloVentana;

	private JPanel pnTitular;
	private JLabel lbTitular;
	private JTextField txtTitular;

	private JPanel pnSubtitulo;
	private JLabel lbSubtitulo;
	private JTextField txtSubtitulo;

	private JPanel pnFechaYAutor;
	private JPanel pnFecha;
	private JLabel lbFecha;
	private JDateChooser dcFecha;

	private JPanel pnAutor;
	private JLabel lbAutor;
	private JTextField txtAutor;

	private JPanel pnCuerpo;
	private JLabel lbCuerpo;
	private JScrollPane spCuerpo;
	private JTextArea taCuerpo;

	private JPanel pnImagenes;
	private JPanel pnBotonesImagenes;
	private JButton btnExaminarImagen;
	private JButton btnQuitarImagen;
	private JScrollPane spListaImagenes;
	private JList<String> listImagenes;
	private DefaultListModel<String> modeloListaImagenes;

	private JButton btnLimpiar;
	private JButton btnCancelar;
	private JButton btnGuardar;

	public VentanaAñadirNoticia(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.service = vPrincipal.getAñadirNoticiaService();

		setTitle("Añadir Noticia - Director de Comunicaciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 720);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				volverAVentanaPrincipal();
			}
		});

		setContentPane(getContentPane());
	}

	@Override
	public JPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setBackground(new Color(248, 250, 252));
			contentPane.setBorder(new EmptyBorder(12, 16, 12, 16));
			contentPane.setLayout(new BorderLayout(0, 10));

			contentPane.add(getPnNorte(), BorderLayout.NORTH);
			contentPane.add(getPnCentro(), BorderLayout.CENTER);
			contentPane.add(getPnSur(), BorderLayout.SOUTH);
		}
		return contentPane;
	}

	private JPanel getPnNorte() {
		if (pnNorte == null) {
			pnNorte = new JPanel();
			pnNorte.setOpaque(false);
			pnNorte.setLayout(new BoxLayout(pnNorte, BoxLayout.Y_AXIS));

			pnNorte.add(getLbTituloVentana());
			pnNorte.add(Box.createVerticalStrut(2));
			pnNorte.add(getLbSubtituloVentana());
		}
		return pnNorte;
	}

	private JLabel getLbTituloVentana() {
		if (lbTituloVentana == null) {
			lbTituloVentana = new JLabel("Publicar Noticia en el Portal del Club");
			lbTituloVentana.setFont(new Font("Segoe UI", Font.BOLD, 18));
			lbTituloVentana.setForeground(new Color(15, 23, 42));
		}
		return lbTituloVentana;
	}

	private JLabel getLbSubtituloVentana() {
		if (lbSubtituloVentana == null) {
			lbSubtituloVentana = new JLabel("Complete los campos obligatorios (*) y opcionales para dar de alta la noticia.");
			lbSubtituloVentana.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			lbSubtituloVentana.setForeground(new Color(100, 116, 139));
		}
		return lbSubtituloVentana;
	}

	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setOpaque(false);
			pnCentro.setLayout(new BoxLayout(pnCentro, BoxLayout.Y_AXIS));

			pnCentro.add(getPnTitular());
			pnCentro.add(Box.createVerticalStrut(8));
			pnCentro.add(getPnSubtitulo());
			pnCentro.add(Box.createVerticalStrut(8));
			pnCentro.add(getPnFechaYAutor());
			pnCentro.add(Box.createVerticalStrut(8));
			pnCentro.add(getPnCuerpo());
			pnCentro.add(Box.createVerticalStrut(8));
			pnCentro.add(getPnImagenes());
		}
		return pnCentro;
	}

	private JPanel getPnTitular() {
		if (pnTitular == null) {
			pnTitular = new JPanel(new BorderLayout(0, 3));
			pnTitular.setOpaque(false);
			pnTitular.add(getLbTitular(), BorderLayout.NORTH);
			pnTitular.add(getTxtTitular(), BorderLayout.CENTER);
		}
		return pnTitular;
	}

	private JLabel getLbTitular() {
		if (lbTitular == null) {
			lbTitular = new JLabel("Titular: (*)");
			lbTitular.setFont(new Font("Segoe UI", Font.BOLD, 12));
			lbTitular.setForeground(new Color(30, 41, 59));
		}
		return lbTitular;
	}

	public JTextField getTxtTitular() {
		if (txtTitular == null) {
			txtTitular = new JTextField();
			txtTitular.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			txtTitular.setPreferredSize(new Dimension(0, 26));
		}
		return txtTitular;
	}

	private JPanel getPnSubtitulo() {
		if (pnSubtitulo == null) {
			pnSubtitulo = new JPanel(new BorderLayout(0, 3));
			pnSubtitulo.setOpaque(false);
			pnSubtitulo.add(getLbSubtitulo(), BorderLayout.NORTH);
			pnSubtitulo.add(getTxtSubtitulo(), BorderLayout.CENTER);
		}
		return pnSubtitulo;
	}

	private JLabel getLbSubtitulo() {
		if (lbSubtitulo == null) {
			lbSubtitulo = new JLabel("Subtítulo: (Opcional)");
			lbSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			lbSubtitulo.setForeground(new Color(71, 85, 105));
		}
		return lbSubtitulo;
	}

	public JTextField getTxtSubtitulo() {
		if (txtSubtitulo == null) {
			txtSubtitulo = new JTextField();
			txtSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			txtSubtitulo.setPreferredSize(new Dimension(0, 26));
		}
		return txtSubtitulo;
	}

	private JPanel getPnFechaYAutor() {
		if (pnFechaYAutor == null) {
			pnFechaYAutor = new JPanel(new GridLayout(1, 2, 12, 0));
			pnFechaYAutor.setOpaque(false);
			pnFechaYAutor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
			pnFechaYAutor.add(getPnFecha());
			pnFechaYAutor.add(getPnAutor());
		}
		return pnFechaYAutor;
	}

	private JPanel getPnFecha() {
		if (pnFecha == null) {
			pnFecha = new JPanel(new BorderLayout(0, 3));
			pnFecha.setOpaque(false);
			pnFecha.add(getLbFecha(), BorderLayout.NORTH);
			pnFecha.add(getDcFecha(), BorderLayout.CENTER);
		}
		return pnFecha;
	}

	private JLabel getLbFecha() {
		if (lbFecha == null) {
			lbFecha = new JLabel("Fecha de publicación: (*)");
			lbFecha.setFont(new Font("Segoe UI", Font.BOLD, 12));
			lbFecha.setForeground(new Color(30, 41, 59));
		}
		return lbFecha;
	}

	public JDateChooser getDcFecha() {
		if (dcFecha == null) {
			dcFecha = new JDateChooser();
			dcFecha.setDateFormatString("dd/MM/yyyy");
			dcFecha.setDate(new Date());
			dcFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			dcFecha.setPreferredSize(new Dimension(0, 26));
		}
		return dcFecha;
	}

	private JPanel getPnAutor() {
		if (pnAutor == null) {
			pnAutor = new JPanel(new BorderLayout(0, 3));
			pnAutor.setOpaque(false);
			pnAutor.add(getLbAutor(), BorderLayout.NORTH);
			pnAutor.add(getTxtAutor(), BorderLayout.CENTER);
		}
		return pnAutor;
	}

	private JLabel getLbAutor() {
		if (lbAutor == null) {
			lbAutor = new JLabel("Autor: (Opcional)");
			lbAutor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			lbAutor.setForeground(new Color(71, 85, 105));
		}
		return lbAutor;
	}

	public JTextField getTxtAutor() {
		if (txtAutor == null) {
			txtAutor = new JTextField();
			txtAutor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			txtAutor.setPreferredSize(new Dimension(0, 26));
		}
		return txtAutor;
	}

	private JPanel getPnCuerpo() {
		if (pnCuerpo == null) {
			pnCuerpo = new JPanel(new BorderLayout(0, 3));
			pnCuerpo.setOpaque(false);
			pnCuerpo.add(getLbCuerpo(), BorderLayout.NORTH);
			pnCuerpo.add(getSpCuerpo(), BorderLayout.CENTER);
		}
		return pnCuerpo;
	}

	private JLabel getLbCuerpo() {
		if (lbCuerpo == null) {
			lbCuerpo = new JLabel("Cuerpo de la noticia: (*)");
			lbCuerpo.setFont(new Font("Segoe UI", Font.BOLD, 12));
			lbCuerpo.setForeground(new Color(30, 41, 59));
		}
		return lbCuerpo;
	}

	private JScrollPane getSpCuerpo() {
		if (spCuerpo == null) {
			spCuerpo = new JScrollPane();
			spCuerpo.setPreferredSize(new Dimension(0, 160));
			spCuerpo.setViewportView(getTaCuerpo());
		}
		return spCuerpo;
	}

	public JTextArea getTaCuerpo() {
		if (taCuerpo == null) {
			taCuerpo = new JTextArea();
			taCuerpo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			taCuerpo.setLineWrap(true);
			taCuerpo.setWrapStyleWord(true);
		}
		return taCuerpo;
	}

	private JPanel getPnImagenes() {
		if (pnImagenes == null) {
			pnImagenes = new JPanel(new BorderLayout(0, 4));
			pnImagenes.setBackground(Color.WHITE);
			pnImagenes.setBorder(new CompoundBorder(
					new TitledBorder(new LineBorder(new Color(203, 213, 225)), "Imágenes adjuntas (Opcional)",
							TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
							new Color(51, 65, 85)),
					new EmptyBorder(4, 6, 6, 6)));
			pnImagenes.setPreferredSize(new Dimension(0, 120));

			pnImagenes.add(getPnBotonesImagenes(), BorderLayout.NORTH);
			pnImagenes.add(getSpListaImagenes(), BorderLayout.CENTER);
		}
		return pnImagenes;
	}

	private JPanel getPnBotonesImagenes() {
		if (pnBotonesImagenes == null) {
			pnBotonesImagenes = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
			pnBotonesImagenes.setOpaque(false);

			pnBotonesImagenes.add(getBtnExaminarImagen());
			pnBotonesImagenes.add(getBtnQuitarImagen());
		}
		return pnBotonesImagenes;
	}

	public JButton getBtnExaminarImagen() {
		if (btnExaminarImagen == null) {
			btnExaminarImagen = new JButton("Seleccionar Imagen...");
			btnExaminarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			btnExaminarImagen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					accionExaminarImagen();
				}
			});
		}
		return btnExaminarImagen;
	}

	public JButton getBtnQuitarImagen() {
		if (btnQuitarImagen == null) {
			btnQuitarImagen = new JButton("Quitar Imagen");
			btnQuitarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			btnQuitarImagen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					accionQuitarImagen();
				}
			});
		}
		return btnQuitarImagen;
	}

	private JScrollPane getSpListaImagenes() {
		if (spListaImagenes == null) {
			spListaImagenes = new JScrollPane();
			spListaImagenes.setPreferredSize(new Dimension(100, 60));
			spListaImagenes.setViewportView(getListImagenes());
		}
		return spListaImagenes;
	}

	public JList<String> getListImagenes() {
		if (listImagenes == null) {
			modeloListaImagenes = new DefaultListModel<>();
			listImagenes = new JList<>(modeloListaImagenes);
			listImagenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listImagenes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		}
		return listImagenes;
	}

	private JPanel getPnSur() {
		if (pnSur == null) {
			pnSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
			pnSur.setOpaque(false);
			pnSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

			pnSur.add(getBtnLimpiar());
			pnSur.add(getBtnCancelar());
			pnSur.add(getBtnGuardar());
		}
		return pnSur;
	}

	public JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar campos");
			btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnLimpiar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					accionLimpiar();
				}
			});
		}
		return btnLimpiar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnCancelar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					accionCancelar();
				}
			});
		}
		return btnCancelar;
	}

	public JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar Noticia");
			btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
			btnGuardar.setBackground(new Color(22, 101, 192));
			btnGuardar.setForeground(Color.WHITE);
			btnGuardar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					accionGuardar();
				}
			});
		}
		return btnGuardar;
	}

	private void accionExaminarImagen() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Seleccionar imagen para la noticia");
		chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
		chooser.setMultiSelectionEnabled(true);

		int resultado = chooser.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File[] archivos = chooser.getSelectedFiles();
			if (archivos != null && archivos.length > 0) {
				for (File f : archivos) {
					modeloListaImagenes.addElement(f.getAbsolutePath());
				}
			} else if (chooser.getSelectedFile() != null) {
				modeloListaImagenes.addElement(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	private void accionQuitarImagen() {
		int index = getListImagenes().getSelectedIndex();
		if (index != -1) {
			modeloListaImagenes.remove(index);
		} else {
			JOptionPane.showMessageDialog(this, "Seleccione la imagen de la lista que desea quitar.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void accionLimpiar() {
		int resp = JOptionPane.showConfirmDialog(this, "¿Desea limpiar todos los campos del formulario?",
				"Confirmación", JOptionPane.YES_NO_OPTION);
		if (resp == JOptionPane.YES_OPTION) {
			limpiarFormulario();
		}
	}

	public void limpiarFormulario() {
		getTxtTitular().setText("");
		getTxtSubtitulo().setText("");
		getTxtAutor().setText("");
		getTaCuerpo().setText("");
		getDcFecha().setDate(new Date());
		if (modeloListaImagenes != null) {
			modeloListaImagenes.clear();
		}
	}

	private void accionCancelar() {
		volverAVentanaPrincipal();
	}

	private void volverAVentanaPrincipal() {
		dispose();
		if (vPrincipal != null) {
			vPrincipal.setVisible(true);
		}
	}

	private void accionGuardar() {
		String titular = getTxtTitular().getText().trim();
		Date fechaSeleccionada = getDcFecha().getDate();
		String cuerpo = getTaCuerpo().getText().trim();

		if (titular.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El titular de la noticia es un campo obligatorio.", "Error de Validación",
					JOptionPane.WARNING_MESSAGE);
			getTxtTitular().requestFocus();
			return;
		}

		if (fechaSeleccionada == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de publicación válida.", "Error de Validación",
					JOptionPane.WARNING_MESSAGE);
			getDcFecha().requestFocus();
			return;
		}

		if (cuerpo.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El cuerpo de la noticia es un campo obligatorio.", "Error de Validación",
					JOptionPane.WARNING_MESSAGE);
			getTaCuerpo().requestFocus();
			return;
		}

		String subtitulo = getTxtSubtitulo().getText().trim();
		String autor = getTxtAutor().getText().trim();
		List<String> rutasImagenes = getImagenesSeleccionadas();

		LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			service.añadirNoticia(titular, subtitulo, fecha, autor, cuerpo, rutasImagenes);
			JOptionPane.showMessageDialog(this, "Noticia guardada y publicada con éxito en el portal.", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			limpiarFormulario();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al guardar la noticia: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public List<String> getImagenesSeleccionadas() {
		List<String> lista = new ArrayList<>();
		if (modeloListaImagenes != null) {
			for (int i = 0; i < modeloListaImagenes.size(); i++) {
				lista.add(modeloListaImagenes.getElementAt(i));
			}
		}
		return lista;
	}

	public AñadirNoticiaService getService() {
		return service;
	}
}