package giis.demo.ui.noticias;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.noticias.Noticia;
import giis.demo.model.noticias.VisualizarNoticiasService;
import giis.demo.ui.VentanaPrincipal;

public class VentanaVisualizarNoticias extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vPrincipal;
	private VisualizarNoticiasService service;

	private List<Noticia> listaNoticias = new ArrayList<>();
	private Noticia noticiaSeleccionada;
	private int indiceFotoActual = 0;

	private CardLayout cardLayout;
	private JPanel contentPane;
	private JPanel pnCards;

	// Card 1: Lista de noticias
	private JPanel pnLista;
	private JPanel pnNorteLista;
	private JLabel lbTituloLista;
	private JLabel lbSubtituloLista;
	private JPanel pnCentroLista;
	private JScrollPane spTablaNoticias;
	private JTable tablaNoticias;
	private DefaultTableModel modeloTabla;
	private JPanel pnSurLista;
	private JButton btnVolverPrincipal;
	private JButton btnActualizar;
	private JButton btnLeerNoticia;

	// Card 2: Detalle de la noticia
	private JPanel pnDetalle;
	private JScrollPane spDetalleScroll;
	private JPanel pnArticulo;
	private JPanel pnCabeceraArticulo;
	private JLabel lbDetalleFechaYAutor;
	private JTextArea taDetalleTitular;
	private JTextArea taDetalleSubtitulo;
	private JSeparator separadorDetalle;
	private JPanel pnVisorImagenes;
	private JButton btnFotoAnterior;
	private JLabel lblFotoPrincipal;
	private JButton btnFotoSiguiente;
	private JLabel lblContadorFotos;
	private JTextArea taDetalleCuerpo;
	private JPanel pnSurDetalle;
	private JButton btnVolverListadoSur;

	public VentanaVisualizarNoticias(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.service = vPrincipal.getVisualizarNoticiasService();

		setTitle("Portal de Noticias del Club");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 680);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				volverAVentanaPrincipal();
			}
		});

		setContentPane(getContentPane());

		cargarNoticias();
	}

	@Override
	public JPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel(new BorderLayout());
			contentPane.setBorder(new EmptyBorder(10, 12, 10, 12));
			contentPane.add(getPnCards(), BorderLayout.CENTER);
		}
		return contentPane;
	}

	public CardLayout getCardLayout() {
		if (cardLayout == null) {
			cardLayout = new CardLayout();
		}
		return cardLayout;
	}

	public JPanel getPnCards() {
		if (pnCards == null) {
			pnCards = new JPanel();
			pnCards.setLayout(getCardLayout());
			pnCards.add(getPnLista(), "CARD_LISTA");
			pnCards.add(getPnDetalle(), "CARD_DETALLE");
		}
		return pnCards;
	}

	// Lista de noticias

	private JPanel getPnLista() {
		if (pnLista == null) {
			pnLista = new JPanel(new BorderLayout(0, 10));
			pnLista.add(getPnNorteLista(), BorderLayout.NORTH);
			pnLista.add(getPnCentroLista(), BorderLayout.CENTER);
			pnLista.add(getPnSurLista(), BorderLayout.SOUTH);
		}
		return pnLista;
	}

	private JPanel getPnNorteLista() {
		if (pnNorteLista == null) {
			pnNorteLista = new JPanel(new BorderLayout(0, 2));
			pnNorteLista.add(getLbTituloLista(), BorderLayout.NORTH);
			pnNorteLista.add(getLbSubtituloLista(), BorderLayout.SOUTH);
		}
		return pnNorteLista;
	}

	public JLabel getLbTituloLista() {
		if (lbTituloLista == null) {
			lbTituloLista = new JLabel("Noticias del Club");
			lbTituloLista.setFont(new Font("Segoe UI", Font.BOLD, 18));
		}
		return lbTituloLista;
	}

	public JLabel getLbSubtituloLista() {
		if (lbSubtituloLista == null) {
			lbSubtituloLista = new JLabel("Seleccione una noticia de la lista para leer su contenido completo.");
			lbSubtituloLista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		}
		return lbSubtituloLista;
	}

	private JPanel getPnCentroLista() {
		if (pnCentroLista == null) {
			pnCentroLista = new JPanel(new BorderLayout());
			pnCentroLista.add(getSpTablaNoticias(), BorderLayout.CENTER);
		}
		return pnCentroLista;
	}

	public JScrollPane getSpTablaNoticias() {
		if (spTablaNoticias == null) {
			spTablaNoticias = new JScrollPane();
			spTablaNoticias.setViewportView(getTablaNoticias());
		}
		return spTablaNoticias;
	}

	public JTable getTablaNoticias() {
		if (tablaNoticias == null) {
			tablaNoticias = new JTable();
			tablaNoticias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			tablaNoticias.setRowHeight(26);
			tablaNoticias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaNoticias.setModel(getModeloTabla());

			tablaNoticias.getColumnModel().getColumn(0).setPreferredWidth(100);
			tablaNoticias.getColumnModel().getColumn(0).setMaxWidth(130);
			tablaNoticias.getColumnModel().getColumn(1).setPreferredWidth(480);
			tablaNoticias.getColumnModel().getColumn(2).setPreferredWidth(140);
			tablaNoticias.getColumnModel().getColumn(3).setPreferredWidth(70);
			tablaNoticias.getColumnModel().getColumn(3).setMaxWidth(80);

			tablaNoticias.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						boolean haySeleccion = tablaNoticias.getSelectedRow() != -1;
						getBtnLeerNoticia().setEnabled(haySeleccion);
					}
				}
			});

			tablaNoticias.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && tablaNoticias.getSelectedRow() != -1) {
						abrirDetalleNoticiaSeleccionada();
					}
				}
			});
		}
		return tablaNoticias;
	}

	public DefaultTableModel getModeloTabla() {
		if (modeloTabla == null) {
			String[] columnas = { "Fecha", "Titular", "Autor", "Fotos" };
			modeloTabla = new DefaultTableModel(columnas, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		return modeloTabla;
	}

	private JPanel getPnSurLista() {
		if (pnSurLista == null) {
			pnSurLista = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
			pnSurLista.add(getBtnVolverPrincipal());
			pnSurLista.add(getBtnActualizar());
			pnSurLista.add(getBtnLeerNoticia());
		}
		return pnSurLista;
	}

	public JButton getBtnVolverPrincipal() {
		if (btnVolverPrincipal == null) {
			btnVolverPrincipal = new JButton("Volver");
			btnVolverPrincipal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnVolverPrincipal.setMnemonic('V');
			btnVolverPrincipal.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					volverAVentanaPrincipal();
				}
			});
		}
		return btnVolverPrincipal;
	}

	public JButton getBtnActualizar() {
		if (btnActualizar == null) {
			btnActualizar = new JButton("Actualizar");
			btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnActualizar.setMnemonic('A');
			btnActualizar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cargarNoticias();
				}
			});
		}
		return btnActualizar;
	}

	public JButton getBtnLeerNoticia() {
		if (btnLeerNoticia == null) {
			btnLeerNoticia = new JButton("Leer noticia completa");
			btnLeerNoticia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnLeerNoticia.setMnemonic('L');
			btnLeerNoticia.setEnabled(false);
			btnLeerNoticia.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abrirDetalleNoticiaSeleccionada();
				}
			});
		}
		return btnLeerNoticia;
	}

	// Detalle noticia

	private JPanel getPnDetalle() {
		if (pnDetalle == null) {
			pnDetalle = new JPanel(new BorderLayout(0, 8));
			pnDetalle.add(getSpDetalleScroll(), BorderLayout.CENTER);
			pnDetalle.add(getPnSurDetalle(), BorderLayout.SOUTH);
		}
		return pnDetalle;
	}

	public JScrollPane getSpDetalleScroll() {
		if (spDetalleScroll == null) {
			spDetalleScroll = new JScrollPane();
			spDetalleScroll.setBorder(null);
			spDetalleScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			spDetalleScroll.getVerticalScrollBar().setUnitIncrement(16);
			spDetalleScroll.setViewportView(getPnArticulo());
		}
		return spDetalleScroll;
	}

	public JPanel getPnArticulo() {
		if (pnArticulo == null) {
			pnArticulo = new JPanel();
			pnArticulo.setLayout(new BoxLayout(pnArticulo, BoxLayout.Y_AXIS));
			pnArticulo.setBorder(new EmptyBorder(5, 5, 5, 5));

			pnArticulo.add(getPnCabeceraArticulo());
			pnArticulo.add(Box.createVerticalStrut(8));
			pnArticulo.add(getSeparadorDetalle());
			pnArticulo.add(Box.createVerticalStrut(10));
			pnArticulo.add(getPnVisorImagenes());
			pnArticulo.add(Box.createVerticalStrut(12));
			pnArticulo.add(getTaDetalleCuerpo());
			pnArticulo.add(Box.createVerticalGlue());
		}
		return pnArticulo;
	}

	public JPanel getPnCabeceraArticulo() {
		if (pnCabeceraArticulo == null) {
			pnCabeceraArticulo = new JPanel();
			pnCabeceraArticulo.setLayout(new BoxLayout(pnCabeceraArticulo, BoxLayout.Y_AXIS));
			pnCabeceraArticulo.setOpaque(false);
			pnCabeceraArticulo.setAlignmentX(Component.LEFT_ALIGNMENT);

			pnCabeceraArticulo.add(getLbDetalleFechaYAutor());
			pnCabeceraArticulo.add(Box.createVerticalStrut(4));
			pnCabeceraArticulo.add(getTaDetalleTitular());
			pnCabeceraArticulo.add(Box.createVerticalStrut(4));
			pnCabeceraArticulo.add(getTaDetalleSubtitulo());
		}
		return pnCabeceraArticulo;
	}

	public JLabel getLbDetalleFechaYAutor() {
		if (lbDetalleFechaYAutor == null) {
			lbDetalleFechaYAutor = new JLabel("Publicado el: -");
			lbDetalleFechaYAutor.setFont(new Font("Segoe UI", Font.BOLD, 12));
			lbDetalleFechaYAutor.setForeground(new Color(110, 110, 110));
			lbDetalleFechaYAutor.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return lbDetalleFechaYAutor;
	}

	public JTextArea getTaDetalleTitular() {
		if (taDetalleTitular == null) {
			taDetalleTitular = new JTextArea() {
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getMaximumSize() {
					return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
				}
			};
			taDetalleTitular.setFont(new Font("Segoe UI", Font.BOLD, 18));
			taDetalleTitular.setLineWrap(true);
			taDetalleTitular.setWrapStyleWord(true);
			taDetalleTitular.setEditable(false);
			taDetalleTitular.setOpaque(false);
			taDetalleTitular.setBorder(null);
			taDetalleTitular.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return taDetalleTitular;
	}

	public JTextArea getTaDetalleSubtitulo() {
		if (taDetalleSubtitulo == null) {
			taDetalleSubtitulo = new JTextArea() {
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getMaximumSize() {
					return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
				}
			};
			taDetalleSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
			taDetalleSubtitulo.setForeground(new Color(60, 60, 60));
			taDetalleSubtitulo.setLineWrap(true);
			taDetalleSubtitulo.setWrapStyleWord(true);
			taDetalleSubtitulo.setEditable(false);
			taDetalleSubtitulo.setOpaque(false);
			taDetalleSubtitulo.setBorder(null);
			taDetalleSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return taDetalleSubtitulo;
	}

	public JSeparator getSeparadorDetalle() {
		if (separadorDetalle == null) {
			separadorDetalle = new JSeparator();
			separadorDetalle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
			separadorDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return separadorDetalle;
	}

	public JPanel getPnVisorImagenes() {
		if (pnVisorImagenes == null) {
			pnVisorImagenes = new JPanel(new BorderLayout(8, 4));
			pnVisorImagenes.setBorder(new TitledBorder(new EtchedBorder(), "Imágenes de la noticia"));
			pnVisorImagenes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
			pnVisorImagenes.setPreferredSize(new Dimension(680, 230));
			pnVisorImagenes.setAlignmentX(Component.LEFT_ALIGNMENT);

			pnVisorImagenes.add(getBtnFotoAnterior(), BorderLayout.WEST);
			pnVisorImagenes.add(getLblFotoPrincipal(), BorderLayout.CENTER);
			pnVisorImagenes.add(getBtnFotoSiguiente(), BorderLayout.EAST);
			pnVisorImagenes.add(getLblContadorFotos(), BorderLayout.SOUTH);
		}
		return pnVisorImagenes;
	}

	public JButton getBtnFotoAnterior() {
		if (btnFotoAnterior == null) {
			btnFotoAnterior = new JButton("◀");
			btnFotoAnterior.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnFotoAnterior.setToolTipText("Foto anterior");
			btnFotoAnterior.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (indiceFotoActual > 0) {
						indiceFotoActual--;
						actualizarVisorFotos();
					}
				}
			});
		}
		return btnFotoAnterior;
	}

	public JLabel getLblFotoPrincipal() {
		if (lblFotoPrincipal == null) {
			lblFotoPrincipal = new JLabel();
			lblFotoPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblFotoPrincipal;
	}

	public JButton getBtnFotoSiguiente() {
		if (btnFotoSiguiente == null) {
			btnFotoSiguiente = new JButton("▶");
			btnFotoSiguiente.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnFotoSiguiente.setToolTipText("Siguiente foto");
			btnFotoSiguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<String> fotos = (noticiaSeleccionada != null) ? noticiaSeleccionada.getImagenes() : null;
					if (fotos != null && indiceFotoActual < fotos.size() - 1) {
						indiceFotoActual++;
						actualizarVisorFotos();
					}
				}
			});
		}
		return btnFotoSiguiente;
	}

	public JLabel getLblContadorFotos() {
		if (lblContadorFotos == null) {
			lblContadorFotos = new JLabel("Foto 1 de 1");
			lblContadorFotos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			lblContadorFotos.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblContadorFotos;
	}

	public JTextArea getTaDetalleCuerpo() {
		if (taDetalleCuerpo == null) {
			taDetalleCuerpo = new JTextArea() {
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getMaximumSize() {
					return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
				}
			};
			taDetalleCuerpo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			taDetalleCuerpo.setLineWrap(true);
			taDetalleCuerpo.setWrapStyleWord(true);
			taDetalleCuerpo.setEditable(false);
			taDetalleCuerpo.setOpaque(false);
			taDetalleCuerpo.setBorder(null);
			taDetalleCuerpo.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return taDetalleCuerpo;
	}

	private JPanel getPnSurDetalle() {
		if (pnSurDetalle == null) {
			pnSurDetalle = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
			pnSurDetalle.add(getBtnVolverListadoSur());
		}
		return pnSurDetalle;
	}

	public JButton getBtnVolverListadoSur() {
		if (btnVolverListadoSur == null) {
			btnVolverListadoSur = new JButton("Volver al listado");
			btnVolverListadoSur.setMnemonic('V');
			btnVolverListadoSur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnVolverListadoSur.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					volverAListado();
				}
			});
		}
		return btnVolverListadoSur;
	}

	// Metodos adicionales

	public void cargarNoticias() {
		try {
			listaNoticias = service.obtenerNoticiasOrdenadasPorFecha();
			modeloTabla.setRowCount(0);

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			for (Noticia n : listaNoticias) {
				String fechaStr = (n.getFecha() != null) ? n.getFecha().format(dtf) : "-";
				String autorStr = (n.getAutor() != null && !n.getAutor().isBlank()) ? n.getAutor() : "-";
				int numFotos = (n.getImagenes() != null) ? n.getImagenes().size() : 0;

				modeloTabla.addRow(new Object[] {
					fechaStr,
					n.getTitular(),
					autorStr,
					numFotos
				});
			}

			getBtnLeerNoticia().setEnabled(false);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error al cargar las noticias: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void abrirDetalleNoticiaSeleccionada() {
		int filaSeleccionada = tablaNoticias.getSelectedRow();
		if (filaSeleccionada == -1 || filaSeleccionada >= listaNoticias.size()) {
			JOptionPane.showMessageDialog(this,
					"Debe seleccionar una noticia de la lista para ver su contenido.",
					"Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		noticiaSeleccionada = listaNoticias.get(filaSeleccionada);
		mostrarDetalle(noticiaSeleccionada);
		getCardLayout().show(getPnCards(), "CARD_DETALLE");
		getSpDetalleScroll().getVerticalScrollBar().setValue(0);
	}

	private void mostrarDetalle(Noticia n) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String fechaStr = (n.getFecha() != null) ? n.getFecha().format(dtf) : "-";
		String autorStr = (n.getAutor() != null && !n.getAutor().isBlank()) ? n.getAutor() : "Club";
		getLbDetalleFechaYAutor().setText("Publicado el: " + fechaStr + " | Autor: " + autorStr);

		getTaDetalleTitular().setText(n.getTitular());

		if (n.getSubtitulo() != null && !n.getSubtitulo().isBlank()) {
			getTaDetalleSubtitulo().setText(n.getSubtitulo());
			getTaDetalleSubtitulo().setVisible(true);
		} else {
			getTaDetalleSubtitulo().setText("");
			getTaDetalleSubtitulo().setVisible(false);
		}

		getTaDetalleCuerpo().setText(n.getCuerpo());

		List<String> fotos = n.getImagenes();
		if (fotos == null || fotos.isEmpty()) {
			getPnVisorImagenes().setVisible(false);
		} else {
			getPnVisorImagenes().setVisible(true);
			indiceFotoActual = 0;
			actualizarVisorFotos();
		}
	}

	private void actualizarVisorFotos() {
		if (noticiaSeleccionada == null) {
			return;
		}

		List<String> fotos = noticiaSeleccionada.getImagenes();
		if (fotos == null || fotos.isEmpty()) {
			getPnVisorImagenes().setVisible(false);
			return;
		}

		getPnVisorImagenes().setVisible(true);
		int totalFotos = fotos.size();
		if (indiceFotoActual < 0) {
			indiceFotoActual = 0;
		}
		if (indiceFotoActual >= totalFotos) {
			indiceFotoActual = totalFotos - 1;
		}

		getBtnFotoAnterior().setEnabled(indiceFotoActual > 0);
		getBtnFotoSiguiente().setEnabled(indiceFotoActual < totalFotos - 1);
		getLblContadorFotos().setText("Foto " + (indiceFotoActual + 1) + " de " + totalFotos);

		String ruta = fotos.get(indiceFotoActual);
		ImageIcon icono = cargarImagenEscalada(ruta, 480, 160);
		getLblFotoPrincipal().setIcon(icono);
		if (icono == null) {
			getLblFotoPrincipal().setText("[ " + new File(ruta).getName() + " ]");
		} else {
			getLblFotoPrincipal().setText("");
		}
	}

	private ImageIcon cargarImagenEscalada(String ruta, int maxAncho, int maxAlto) {
		if (ruta == null || ruta.isBlank()) {
			return null;
		}
		File file = new File(ruta);
		if (!file.exists()) {
			return null;
		}

		try {
			ImageIcon original = new ImageIcon(file.getAbsolutePath());
			Image img = original.getImage();
			int origW = img.getWidth(null);
			int origH = img.getHeight(null);
			if (origW <= 0 || origH <= 0) {
				return null;
			}

			double escala = Math.min((double) maxAncho / origW, (double) maxAlto / origH);
			int nuevoW = (int) (origW * escala);
			int nuevoH = (int) (origH * escala);

			Image scaled = img.getScaledInstance(nuevoW, nuevoH, Image.SCALE_SMOOTH);
			return new ImageIcon(scaled);
		} catch (Exception e) {
			return null;
		}
	}

	private void volverAListado() {
		getCardLayout().show(getPnCards(), "CARD_LISTA");
	}

	private void volverAVentanaPrincipal() {
		dispose();
		if (vPrincipal != null) {
			vPrincipal.setVisible(true);
		}
	}
}