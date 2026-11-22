package giis.demo.ui.equipo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.equipo.ImportarPartidosService;
import giis.demo.model.equipo.PartidoImportadoDTO;
import giis.demo.ui.login.VentanaPrincipal;

public class VentanaImportarPartidos extends JFrame {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal vPrincipal;
	private ImportarPartidosService service;

	// Componentes principales
	private JPanel contentPane;
	private JPanel pnNorte;
	private JLabel lbArchivo;
	private JTextField txRutaArchivo;
	private JButton btnExaminar;

	private JPanel pnCentro;
	private JScrollPane spTabla;
	private JTable tablaPartidos;
	private DefaultTableModel modeloTabla;
	private JPanel pnInfoTabla;
	private JLabel lbTotalPartidos;

	private JPanel pnSur;
	private JLabel lbEstado;
	private JPanel pnBotonesSur;
	private JButton btnVolver;
	private JButton btnImportar;
	
	private List<PartidoImportadoDTO> partidosCargados = new ArrayList<>();

	private File archivoSeleccionado;

	public VentanaImportarPartidos(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.service = (vPrincipal != null) ? vPrincipal.getImportarPartidosService() : new ImportarPartidosService();
		
		setTitle("Importar Partidos desde Archivo Externo (Gerente)");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 580);
		setLocationRelativeTo(null);
		setContentPane(getContentPane());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				volver();
			}
		});
	}

	@Override
	public JPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
			contentPane.setLayout(new BorderLayout(0, 10));
			contentPane.add(getPnNorte(), BorderLayout.NORTH);
			contentPane.add(getPnCentro(), BorderLayout.CENTER);
			contentPane.add(getPnSur(), BorderLayout.SOUTH);
		}
		return contentPane;
	}

	// Panel norte
	private JPanel getPnNorte() {
		if (pnNorte == null) {
			pnNorte = new JPanel();
			pnNorte.setBorder(new CompoundBorder(
					new TitledBorder(null, "Origen de Datos (.csv / .json)", 
							TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12), null),
					new EmptyBorder(6, 10, 6, 10)));
			pnNorte.setLayout(new BorderLayout(10, 0));
			pnNorte.add(getLbArchivo(), BorderLayout.WEST);
			pnNorte.add(getTxRutaArchivo(), BorderLayout.CENTER);
			pnNorte.add(getBtnExaminar(), BorderLayout.EAST);
		}
		return pnNorte;
	}

	private JLabel getLbArchivo() {
		if (lbArchivo == null) {
			lbArchivo = new JLabel("Archivo seleccionado:");
			lbArchivo.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbArchivo.setPreferredSize(new Dimension(140, 28));
		}
		return lbArchivo;
	}

	public JTextField getTxRutaArchivo() {
		if (txRutaArchivo == null) {
			txRutaArchivo = new JTextField();
			txRutaArchivo.setEditable(false);
			txRutaArchivo.setFont(new Font("Tahoma", Font.PLAIN, 12));
			txRutaArchivo.setPreferredSize(new Dimension(200, 28));
			txRutaArchivo.setColumns(10);
		}
		return txRutaArchivo;
	}

	public JButton getBtnExaminar() {
		if (btnExaminar == null) {
			btnExaminar = new JButton("Examinar");
			btnExaminar.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnExaminar.setPreferredSize(new Dimension(110, 28));
			btnExaminar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					seleccionarArchivo();
				}
			});
		}
		return btnExaminar;
	}

	// Panel centro
	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setBorder(new TitledBorder(null, "Previsualización de Partidos en el Archivo", 
					TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12), null));
			pnCentro.setLayout(new BorderLayout(0, 5));
			pnCentro.add(getSpTabla(), BorderLayout.CENTER);
			pnCentro.add(getPnInfoTabla(), BorderLayout.SOUTH);
		}
		return pnCentro;
	}

	private JScrollPane getSpTabla() {
		if (spTabla == null) {
			spTabla = new JScrollPane();
			spTabla.setViewportView(getTablaPartidos());
		}
		return spTabla;
	}

	public JTable getTablaPartidos() {
		if (tablaPartidos == null) {
			tablaPartidos = new JTable(getModeloTabla());
			tablaPartidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaPartidos.setRowHeight(22);
			tablaPartidos.getTableHeader().setReorderingAllowed(false);
			if (tablaPartidos.getColumnModel().getColumnCount() >= 8) {
				tablaPartidos.getColumnModel().getColumn(0).setPreferredWidth(125);
				tablaPartidos.getColumnModel().getColumn(1).setPreferredWidth(125);
				tablaPartidos.getColumnModel().getColumn(2).setPreferredWidth(85);
				tablaPartidos.getColumnModel().getColumn(3).setPreferredWidth(75);
				tablaPartidos.getColumnModel().getColumn(4).setPreferredWidth(75);
				tablaPartidos.getColumnModel().getColumn(5).setPreferredWidth(130);
				tablaPartidos.getColumnModel().getColumn(6).setPreferredWidth(60);
				tablaPartidos.getColumnModel().getColumn(7).setPreferredWidth(250);
			}
		}
		return tablaPartidos;
	}

	public DefaultTableModel getModeloTabla() {
		if (modeloTabla == null) {
			String[] columnas = {
				"Equipo Local", "Equipo Visitante", "Fecha", "Hora Inicio", "Hora Fin", "Instalación", "Válido", "Motivo"
			};
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

	private JPanel getPnInfoTabla() {
		if (pnInfoTabla == null) {
			pnInfoTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			pnInfoTabla.add(getLbTotalPartidos());
		}
		return pnInfoTabla;
	}

	public JLabel getLbTotalPartidos() {
		if (lbTotalPartidos == null) {
			lbTotalPartidos = new JLabel("Partidos leídos: 0");
			lbTotalPartidos.setFont(new Font("Tahoma", Font.ITALIC, 11));
		}
		return lbTotalPartidos;
	}

	// Panel sur
	private JPanel getPnSur() {
		if (pnSur == null) {
			pnSur = new JPanel();
			pnSur.setLayout(new BorderLayout(10, 0));
			pnSur.add(getLbEstado(), BorderLayout.WEST);
			pnSur.add(getPnBotonesSur(), BorderLayout.EAST);
		}
		return pnSur;
	}

	public JLabel getLbEstado() {
		if (lbEstado == null) {
			lbEstado = new JLabel("Seleccione un archivo .csv o .json para comenzar.");
			lbEstado.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbEstado.setPreferredSize(new Dimension(500, 28));
		}
		return lbEstado;
	}

	private JPanel getPnBotonesSur() {
		if (pnBotonesSur == null) {
			pnBotonesSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
			pnBotonesSur.add(getBtnVolver());
			pnBotonesSur.add(getBtnImportar());
		}
		return pnBotonesSur;
	}

	public JButton getBtnVolver() {
		if (btnVolver == null) {
			btnVolver = new JButton("Volver");
			btnVolver.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnVolver.setPreferredSize(new Dimension(100, 28));
			btnVolver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					volver();
				}
			});
		}
		return btnVolver;
	}

	public JButton getBtnImportar() {
		if (btnImportar == null) {
			btnImportar = new JButton("Importar Partidos");
			btnImportar.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnImportar.setPreferredSize(new Dimension(150, 28));
			btnImportar.setEnabled(false); // Deshabilitado hasta que haya archivo cargado
			btnImportar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ejecutarImportacion();
				}
			});
		}
		return btnImportar;
	}

	// Metodos adicionales
	private void seleccionarArchivo() {
		JFileChooser chooser = new JFileChooser();
		
		// Abre directamente la carpeta de recursos del proyecto
		File dirFiles = new File("src/main/resources/files");
		if (dirFiles.exists()) {
			chooser.setCurrentDirectory(dirFiles);
		} else {
			chooser.setCurrentDirectory(new File("src/main/resources"));
		}

		chooser.setDialogTitle("Seleccionar archivo de partidos");
		chooser.setFileFilter(new FileNameExtensionFilter("Archivos de partidos (*.csv, *.json)", "csv", "json"));
		chooser.setAcceptAllFileFilterUsed(false);

		int resultado = chooser.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			archivoSeleccionado = chooser.getSelectedFile();
			getTxRutaArchivo().setText(archivoSeleccionado.getAbsolutePath());

			try {
				partidosCargados = service.cargarPartidosDesdeArchivo(archivoSeleccionado);
				actualizarTablaPartidos();
				int validos = 0;
				for (PartidoImportadoDTO p : partidosCargados) {
					if (p.isValido()) {
						validos++;
					}
				}
				getLbEstado().setText("Archivo cargado: " + archivoSeleccionado.getName() 
						+ " (" + validos + " de " + partidosCargados.size() + " válidos)");
			} catch (Exception ex) {
				partidosCargados.clear();
				actualizarTablaPartidos();
				JOptionPane.showMessageDialog(this, ex.getMessage(), 
						"Error de formato", JOptionPane.ERROR_MESSAGE);
				getLbEstado().setText("Error al cargar el archivo seleccionado.");
				getBtnImportar().setEnabled(false);
			}
		}
	}
	
	private void actualizarTablaPartidos() {
		DefaultTableModel modelo = getModeloTabla();
		modelo.setRowCount(0);
		int validosCount = 0;
		for (PartidoImportadoDTO p : partidosCargados) {
			if (p.isValido()) {
				validosCount++;
			}
			modelo.addRow(new Object[] {
				p.getEquipoLocal(), p.getEquipoVisitante(), 
				p.getFechaFormateada(), p.getHoraInicioFormateada(), 
				p.getHoraFinFormateada(), p.getInstalacion(),
				p.getValidoTexto(), p.getMotivo()
			});
		}
		getLbTotalPartidos().setText("Partidos leídos: " + partidosCargados.size() + " (" + validosCount + " válidos)");
		getBtnImportar().setEnabled(validosCount > 0);
	}

	private void ejecutarImportacion() {
		try {
			List<PartidoImportadoDTO> importados = service.importarPartidosValidos(partidosCargados);
			int totalImportados = importados.size();
			
			if (totalImportados == 0) {
				JOptionPane.showMessageDialog(this, 
						"No se ha importado ningún partido porque ninguno de los partidos cargados es válido.", 
						"Sin partidos válidos", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Dejar solo los inválidos en la lista y en la tabla
			partidosCargados.removeAll(importados);
			actualizarTablaPartidos();

			if (partidosCargados.isEmpty()) {
				JOptionPane.showMessageDialog(this, 
						"¡Se han importado los " + totalImportados + " partidos válidos correctamente a la base de datos!", 
						"Importación completada", JOptionPane.INFORMATION_MESSAGE);
				getBtnImportar().setEnabled(false);
				getLbEstado().setText("Importación completada: " + totalImportados + " partidos importados.");
			} else {
				JOptionPane.showMessageDialog(this, 
						"Se han importado " + totalImportados + " partidos válidos correctamente.\n"
						+ "Permanecen en la tabla " + partidosCargados.size() + " partidos no válidos debido a colisiones o errores.", 
						"Importación parcial", JOptionPane.INFORMATION_MESSAGE);
				getBtnImportar().setEnabled(false);
				getLbEstado().setText("Importados " + totalImportados + " partidos. Quedan " + partidosCargados.size() + " partidos no válidos.");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error durante la importación:\n" + ex.getMessage(), 
					"Error de importación", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void volver() {
		this.dispose();
		if (vPrincipal != null) {
			vPrincipal.setVisible(true);
		}
	}
}