package giis.demo.ui.noticias;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
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
import javax.swing.border.LineBorder;
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
	private JPanel pnContenedorVisorCentrado;
	private JPanel pnVisorImagenes;
	private JPanel pnMarcoFoto;
	private JLabel lblFotoPrincipal;
	private JPanel pnControlesFoto;
	private JButton btnFotoAnterior;
	private JLabel lblContadorFotos;
	private JButton btnFotoSiguiente;
	private JTextArea taDetalleCuerpo;
	private JPanel pnSurDetalle;
	private JButton btnVolverListadoSur;

	public VentanaVisualizarNoticias(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		if (vPrincipal != null) {
			this.service = vPrincipal.getVisualizarNoticiasService();
		} else {
			this.service = new VisualizarNoticiasService();
		}

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

	// Card 1: Lista de noticias

	public JPanel getPnLista() {
		if (pnLista == null) {
			pnLista = new JPanel(new BorderLayout(0, 10));
			pnLista.add(getPnNorteLista(), BorderLayout.NORTH);
			pnLista.add(getPnCentroLista(), BorderLayout.CENTER);
			pnLista.add(getPnSurLista(), BorderLayout.SOUTH);
		}
		return pnLista;
	}

	public JPanel getPnNorteLista() {
		if (pnNorteLista == null) {
			pnNorteLista = new JPanel();
			pnNorteLista.setLayout(new BoxLayout(pnNorteLista, BoxLayout.Y_AXIS));
			pnNorteLista.add(getLbTituloLista());
			pnNorteLista.add(Box.createVerticalStrut(4));
			pnNorteLista.add(getLbSubtituloLista());
		}
		return pnNorteLista;
	}

	public JLabel getLbTituloLista() {
		if (lbTituloLista == null) {
			lbTituloLista = new JLabel("Tablón de Noticias del Club");
			lbTituloLista.setFont(new Font("Segoe UI", Font.BOLD, 18));
			lbTituloLista.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return lbTituloLista;
	}

	public JLabel getLbSubtituloLista() {
		if (lbSubtituloLista == null) {
			lbSubtituloLista = new JLabel("Consulte toda la actualidad, comunicados oficiales y novedades de la entidad.");
			lbSubtituloLista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			lbSubtituloLista.setForeground(new Color(90, 90, 90));
			lbSubtituloLista.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return lbSubtituloLista;
	}

	public JPanel getPnCentroLista() {
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
			tablaNoticias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaNoticias.setRowHeight(26);
			tablaNoticias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			tablaNoticias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
			tablaNoticias.getTableHeader().setReorderingAllowed(false);
			tablaNoticias.setModel(getModeloTabla());

			tablaNoticias.getColumnModel().getColumn(0).setPreferredWidth(90);
			tablaNoticias.getColumnModel().getColumn(0).setMaxWidth(110);
			tablaNoticias.getColumnModel().getColumn(1).setPreferredWidth(380);
			tablaNoticias.getColumnModel().getColumn(2).setPreferredWidth(140);
			tablaNoticias.getColumnModel().getColumn(3).setPreferredWidth(60);
			tablaNoticias.getColumnModel().getColumn(3).setMaxWidth(75);

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

	public JPanel getPnSurLista() {
		if (pnSurLista == null) {
			pnSurLista = new JPanel(new BorderLayout());
			pnSurLista.add(getBtnVolverPrincipal(), BorderLayout.WEST);

			JPanel pnAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
			pnAcciones.add(getBtnActualizar());
			pnAcciones.add(getBtnLeerNoticia());
			pnSurLista.add(pnAcciones, BorderLayout.EAST);
		}
		return pnSurLista;
	}

	public JButton getBtnVolverPrincipal() {
		if (btnVolverPrincipal == null) {
			btnVolverPrincipal = new JButton("Volver");
			btnVolverPrincipal.setMnemonic('V');
			btnVolverPrincipal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnVolverPrincipal.setToolTipText("Regresar al menú anterior");
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
			btnActualizar.setMnemonic('A');
			btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			btnActualizar.setToolTipText("Recargar la lista de noticias");
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
			btnLeerNoticia = new JButton("Leer noticia");
			btnLeerNoticia.setMnemonic('L');
			btnLeerNoticia.setFont(new Font("Segoe UI", Font.BOLD, 12));
			btnLeerNoticia.setEnabled(false);
			btnLeerNoticia.setToolTipText("Visualizar el contenido completo de la noticia seleccionada");
			btnLeerNoticia.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abrirDetalleNoticiaSeleccionada();
				}
			});
		}
		return btnLeerNoticia;
	}

	// Card 2: Detalle noticia

	public JPanel getPnDetalle() {
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
	        spDetalleScroll.setOpaque(false);
	        spDetalleScroll.getViewport().setOpaque(false);
	       
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
			pnArticulo.setOpaque(false);
			pnArticulo.setBorder(new EmptyBorder(4, 4, 4, 4));

			pnArticulo.add(getPnCabeceraArticulo());
			pnArticulo.add(Box.createVerticalStrut(8));
			pnArticulo.add(getSeparadorDetalle());
			pnArticulo.add(Box.createVerticalStrut(12));
			pnArticulo.add(getPnContenedorVisorCentrado());
			pnArticulo.add(Box.createVerticalStrut(14));
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
			taDetalleTitular.setBackground(new Color(0,0,0,0));
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
			taDetalleSubtitulo.setForeground(new Color(75, 75, 75));
			taDetalleSubtitulo.setLineWrap(true);
			taDetalleSubtitulo.setWrapStyleWord(true);
			taDetalleSubtitulo.setEditable(false);
			taDetalleSubtitulo.setOpaque(false);
			taDetalleSubtitulo.setBackground(new Color(0,0,0,0));
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

	public JPanel getPnContenedorVisorCentrado() {
		if (pnContenedorVisorCentrado == null) {
			pnContenedorVisorCentrado = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			pnContenedorVisorCentrado.setOpaque(false);
			pnContenedorVisorCentrado.setAlignmentX(Component.LEFT_ALIGNMENT);
			pnContenedorVisorCentrado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 290));

			pnContenedorVisorCentrado.add(getPnVisorImagenes());
		}
		return pnContenedorVisorCentrado;
	}

	public JPanel getPnVisorImagenes() {
		if (pnVisorImagenes == null) {
			pnVisorImagenes = new JPanel(new BorderLayout(0, 6));
			pnVisorImagenes.setOpaque(false);
			pnVisorImagenes.setPreferredSize(new Dimension(380, 275));

			pnVisorImagenes.add(getPnMarcoFoto(), BorderLayout.CENTER);
			pnVisorImagenes.add(getPnControlesFoto(), BorderLayout.SOUTH);
		}
		return pnVisorImagenes;
	}

	public JPanel getPnMarcoFoto() {
		if (pnMarcoFoto == null) {
			pnMarcoFoto = new JPanel(new BorderLayout());
			pnMarcoFoto.setBackground(new Color(230, 230, 230));
			pnMarcoFoto.setBorder(new LineBorder(new Color(205, 205, 205), 1));
			pnMarcoFoto.setPreferredSize(new Dimension(380, 235));
			pnMarcoFoto.add(getLblFotoPrincipal(), BorderLayout.CENTER);
		}
		return pnMarcoFoto;
	}

	public JLabel getLblFotoPrincipal() {
		if (lblFotoPrincipal == null) {
			lblFotoPrincipal = new JLabel();
			lblFotoPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
			lblFotoPrincipal.setVerticalAlignment(SwingConstants.CENTER);
		}
		return lblFotoPrincipal;
	}

	public JPanel getPnControlesFoto() {
		if (pnControlesFoto == null) {
			pnControlesFoto = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
			pnControlesFoto.setOpaque(false);
			pnControlesFoto.add(getBtnFotoAnterior());
			pnControlesFoto.add(getLblContadorFotos());
			pnControlesFoto.add(getBtnFotoSiguiente());
		}
		return pnControlesFoto;
	}

	public JButton getBtnFotoAnterior() {
		if (btnFotoAnterior == null) {
			btnFotoAnterior = new JButton("<");
			btnFotoAnterior.setFont(new Font("Segoe UI", Font.BOLD, 12));
			btnFotoAnterior.setMargin(new Insets(2, 8, 2, 8));
			btnFotoAnterior.setToolTipText("Foto anterior");
			btnFotoAnterior.setFocusable(false);
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

	public JLabel getLblContadorFotos() {
		if (lblContadorFotos == null) {
			lblContadorFotos = new JLabel("Foto 1 de 1");
			lblContadorFotos.setFont(new Font("Segoe UI", Font.BOLD, 12));
			lblContadorFotos.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblContadorFotos;
	}

	public JButton getBtnFotoSiguiente() {
		if (btnFotoSiguiente == null) {
			btnFotoSiguiente = new JButton(">");
			btnFotoSiguiente.setFont(new Font("Segoe UI", Font.BOLD, 12));
			btnFotoSiguiente.setMargin(new Insets(2, 8, 2, 8));
			btnFotoSiguiente.setToolTipText("Siguiente foto");
			btnFotoSiguiente.setFocusable(false);
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
			taDetalleCuerpo.setBackground(new Color(0,0,0,0));
			taDetalleCuerpo.setBorder(null);
			taDetalleCuerpo.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return taDetalleCuerpo;
	}

	public JPanel getPnSurDetalle() {
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
			getModeloTabla().setRowCount(0);

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			for (Noticia n : listaNoticias) {
				String fechaStr = (n.getFecha() != null) ? n.getFecha().format(dtf) : "-";
				String autorStr = (n.getAutor() != null && !n.getAutor().isBlank()) ? n.getAutor() : "-";
				int numFotos = (n.getImagenes() != null) ? n.getImagenes().size() : 0;

				getModeloTabla().addRow(new Object[] {
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
		int filaSeleccionada = getTablaNoticias().getSelectedRow();
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
			getTaDetalleSubtitulo().setText(n.getSubtitulo().replace("\\n", " "));
			getTaDetalleSubtitulo().setVisible(true);
		} else {
			getTaDetalleSubtitulo().setText("");
			getTaDetalleSubtitulo().setVisible(false);
		}

		// Reemplazar saltos de línea literales por saltos reales
		String cuerpo = n.getCuerpo();
		if (cuerpo != null) {
			cuerpo = cuerpo.replace("\\n", "\n");
		}
		getTaDetalleCuerpo().setText(cuerpo);

		List<String> fotos = n.getImagenes();
		if (fotos == null || fotos.isEmpty()) {
			getPnContenedorVisorCentrado().setVisible(false);
			getPnVisorImagenes().setVisible(false);
		} else {
			getPnContenedorVisorCentrado().setVisible(true);
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
			getPnContenedorVisorCentrado().setVisible(false);
			getPnVisorImagenes().setVisible(false);
			return;
		}

		getPnContenedorVisorCentrado().setVisible(true);
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
		// Escalado adaptado al marco centrado (376 x 231)
		ImageIcon icono = cargarImagenEscalada(ruta, 376, 231);
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
		this.vPrincipal.setVisible(true);
	}
}