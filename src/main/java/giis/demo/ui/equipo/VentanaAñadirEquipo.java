package giis.demo.ui.equipo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.empleado.EmpleadoDeportivo;
import giis.demo.model.empleado.Entrenador;
import giis.demo.model.equipo.AñadirEquipoService;
import giis.demo.model.equipo.CategoriaEquipo;
import giis.demo.ui.VentanaPrincipal;

public class VentanaAñadirEquipo extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private VentanaPrincipal vPrincipal;
	private AñadirEquipoService club;
	
	private JPanel contentPane;
	private JPanel pnSuperior;
	private JPanel pnTipoEquipo;
	private JLabel lbTipoEquipo;
	private JPanel pnCategoriaEquipo;
	private JLabel lbCategoriaEquipo;
	private JComboBox<CategoriaEquipo> cbCategoriaEquipo;
	private JComboBox<String> cbTipoEquipo;
	private JPanel pnCentro;
	private JPanel pnCuerpoTecnico;
	private JLabel lbPrimerEntrenador;
	private JComboBox<Entrenador> cbPrimerEntrenador;
	private JComboBox<Entrenador> cbSegundoEntrenador;
	private JPanel pnPrimerYSegundoEntrenador;
	private JLabel lbSegundoEntrenador;
	private JPanel pnRestoDeTecnicos;
	private JLabel lbRestoTecnicosTitulo;
	private DefaultListModel<EmpleadoDeportivo> modeloListaRestoTecnicos;
	private JList<EmpleadoDeportivo> listRestoTecnicos;
	private JScrollPane scrRestoTecnicos;
	private JPanel pnJugadores;
	private JScrollPane scrJugadores;
	private DefaultTableModel modeloJugadores;
	private JTable tbJugadores;
	private JScrollPane scrPanelPrincipal;
	private JPanel pnInferior;
	private JButton btnAtras;
	private JButton btnCrearEquipo;
	private JPanel pnNombreEquipo;
	private JLabel lbNombreEquipo;
	private JTextField txNombreEquipo;

	public VentanaAñadirEquipo(VentanaPrincipal vPrincipal) {
		this.vPrincipal = vPrincipal;
		this.club = this.vPrincipal.getAñadirEquipoService();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\IkerNuevo\\Downloads\\uniovi_solo_escudo_color.png"));
		setTitle("Creacion de equipos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 886, 577);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(getPnSuperior(), BorderLayout.NORTH);
		contentPane.add(getScrPanelPrincipal(), BorderLayout.CENTER);
		contentPane.add(getPnInferior(), BorderLayout.SOUTH);
	}
	
	private JPanel getPnSuperior() {
		if (pnSuperior == null) {
			pnSuperior = new JPanel();
			pnSuperior.setLayout(new GridLayout(0, 3, 0, 0));
			pnSuperior.add(getPnNombreEquipo());
			pnSuperior.add(getPnTipoEquipo());
			pnSuperior.add(getPnCategoriaEquipo());
		}
		return pnSuperior;
	}
	
	private JPanel getPnTipoEquipo() {
		if (pnTipoEquipo == null) {
			pnTipoEquipo = new JPanel();
			pnTipoEquipo.add(getLbTipoEquipo());
			pnTipoEquipo.add(getCbTipoEquipo());
		}
		return pnTipoEquipo;
	}
	
	private JLabel getLbTipoEquipo() {
		if (lbTipoEquipo == null) {
			lbTipoEquipo = new JLabel("Tipo de Equipo:");
			lbTipoEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lbTipoEquipo;
	}
	
	private JComboBox<String> getCbTipoEquipo() {
		if (cbTipoEquipo == null) {
			cbTipoEquipo = new JComboBox<String>();
			cbTipoEquipo.setModel(new DefaultComboBoxModel<String>(new String[] {"Profesional", "En formacion"}));
			cbTipoEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
			cbTipoEquipo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String seleccionado = (String) cbTipoEquipo.getSelectedItem();
			        if (seleccionado != null) {
			            List<CategoriaEquipo> categorias = club.obtenerCategoriasPorTipo(seleccionado);
			            getCbCategoriaEquipo().setModel(new DefaultComboBoxModel<CategoriaEquipo>(categorias.toArray(new CategoriaEquipo[0])));
			            if(getCbCategoriaEquipo().getItemCount() > 0) {
			            	getCbCategoriaEquipo().setSelectedIndex(0);
			            }
			        }
				}
			});
			if (cbTipoEquipo.getItemCount() > 0) {
				cbTipoEquipo.setSelectedIndex(0); 
			}
		}
		return cbTipoEquipo;
	}
	
	private JPanel getPnCategoriaEquipo() {
		if (pnCategoriaEquipo == null) {
			pnCategoriaEquipo = new JPanel();
			pnCategoriaEquipo.add(getLbCategoriaEquipo());
			pnCategoriaEquipo.add(getCbCategoriaEquipo());
		}
		return pnCategoriaEquipo;
	}
	
	private JLabel getLbCategoriaEquipo() {
		if (lbCategoriaEquipo == null) {
			lbCategoriaEquipo = new JLabel("Categoria del Equipo:");
			lbCategoriaEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lbCategoriaEquipo;
	}
	
	private JComboBox<CategoriaEquipo> getCbCategoriaEquipo() {
		if (cbCategoriaEquipo == null) {
			cbCategoriaEquipo = new JComboBox<CategoriaEquipo>();
			cbCategoriaEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return cbCategoriaEquipo;
	}
	
	private JScrollPane getScrPanelPrincipal() {
		if (scrPanelPrincipal == null) {
			scrPanelPrincipal = new JScrollPane(getPnCentro());
			scrPanelPrincipal.getVerticalScrollBar().setUnitIncrement(16);
		}
		return scrPanelPrincipal;
	}
	
	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setLayout(new BorderLayout(0, 0));
			pnCentro.add(getPnCuerpoTecnico(), BorderLayout.NORTH);
			pnCentro.add(getPnJugadores(), BorderLayout.CENTER);
		}
		return pnCentro;
	}
	
	private JPanel getPnCuerpoTecnico() {
		if (pnCuerpoTecnico == null) {
			pnCuerpoTecnico = new JPanel();
			pnCuerpoTecnico.setBorder(new TitledBorder(null, "Cuerpo T\u00E9cnico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnCuerpoTecnico.setLayout(new BorderLayout(0, 0));
			pnCuerpoTecnico.add(getPnPrimerYSegundoEntrenador(), BorderLayout.NORTH);
			pnCuerpoTecnico.add(getPnRestoDeTecnicos(), BorderLayout.CENTER);
		}
		return pnCuerpoTecnico;
	}
	
	private JPanel getPnPrimerYSegundoEntrenador() {
		if (pnPrimerYSegundoEntrenador == null) {
			pnPrimerYSegundoEntrenador = new JPanel();
			pnPrimerYSegundoEntrenador.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			pnPrimerYSegundoEntrenador.add(getLbPrimerEntrenador());
			pnPrimerYSegundoEntrenador.add(getCbPrimerEntrenador());
			pnPrimerYSegundoEntrenador.add(getLbSegundoEntrenador());
			pnPrimerYSegundoEntrenador.add(getCbSegundoEntrenador());
		}
		return pnPrimerYSegundoEntrenador;
	}
	
	private JLabel getLbPrimerEntrenador() {
		if (lbPrimerEntrenador == null) {
			lbPrimerEntrenador = new JLabel("Primer entrenador:");
			lbPrimerEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lbPrimerEntrenador;
	}
	
	private JComboBox<Entrenador> getCbPrimerEntrenador() {
		if (cbPrimerEntrenador == null) {
			cbPrimerEntrenador = new JComboBox<Entrenador>();
			cbPrimerEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
			List<Entrenador> tecnicos = club.obtenerEntrenadoresDisponibles();
			cbPrimerEntrenador.setModel(new DefaultComboBoxModel<Entrenador>(tecnicos.toArray(new Entrenador[0])));
		}
		return cbPrimerEntrenador;
	}
	
	private JLabel getLbSegundoEntrenador() {
		if (lbSegundoEntrenador == null) {
			lbSegundoEntrenador = new JLabel("Segundo entrenador:");
			lbSegundoEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lbSegundoEntrenador;
	}
	
	private JComboBox<Entrenador> getCbSegundoEntrenador() {
		if (cbSegundoEntrenador == null) {
			cbSegundoEntrenador = new JComboBox<Entrenador>();
			cbSegundoEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
			List<Entrenador> tecnicos = club.obtenerEntrenadoresDisponibles();
			cbSegundoEntrenador.setModel(new DefaultComboBoxModel<Entrenador>(tecnicos.toArray(new Entrenador[0])));
		}
		return cbSegundoEntrenador;
	}
	
	private JPanel getPnRestoDeTecnicos() {
		if (pnRestoDeTecnicos == null) {
			pnRestoDeTecnicos = new JPanel(new BorderLayout(0, 3));
			pnRestoDeTecnicos.add(getLbRestoTecnicosTitulo(), BorderLayout.NORTH);
			pnRestoDeTecnicos.add(getScrRestoTecnicos(), BorderLayout.CENTER);
		}
		return pnRestoDeTecnicos;
	}
	
	private JLabel getLbRestoTecnicosTitulo() {
		if (lbRestoTecnicosTitulo == null) {
			lbRestoTecnicosTitulo = new JLabel("Si quieres añadir técnicos adicionales, seleccionalos:");
			lbRestoTecnicosTitulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbRestoTecnicosTitulo;
	}
	
	private JScrollPane getScrRestoTecnicos() {
		if (scrRestoTecnicos == null) {
			scrRestoTecnicos = new JScrollPane(getListRestoTecnicos());
			scrRestoTecnicos.setPreferredSize(new Dimension(0, 90));
		}
		return scrRestoTecnicos;
	}
	
	private JList<EmpleadoDeportivo> getListRestoTecnicos() {
		if (listRestoTecnicos == null) {
			modeloListaRestoTecnicos = new DefaultListModel<>();
			List<EmpleadoDeportivo> listaRestoTecnicosDatos = club.obtenerRestoTecnicos();
			for (EmpleadoDeportivo tecnico : listaRestoTecnicosDatos) {
			    modeloListaRestoTecnicos.addElement(tecnico);
			}
			listRestoTecnicos = new JList<EmpleadoDeportivo>(modeloListaRestoTecnicos);
			listRestoTecnicos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			listRestoTecnicos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return listRestoTecnicos;
	}
	
	private JPanel getPnJugadores() {
		if (pnJugadores == null) {
			pnJugadores = new JPanel();
			pnJugadores.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Seleccion de jugadores (Min.7)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			pnJugadores.setLayout(new BorderLayout(0, 0));
			pnJugadores.add(getScrJugadores(), BorderLayout.CENTER);
		}
		return pnJugadores;
	}
	
	private JScrollPane getScrJugadores() {
		if (scrJugadores == null) {
			scrJugadores = new JScrollPane();
			scrJugadores.setViewportView(getTbJugadores());
		}
		return scrJugadores;
	}
	
	private JTable getTbJugadores() {
		if (tbJugadores == null) {
			tbJugadores = new JTable(getModeloJugadores());
			CategoriaEquipo categoriaSeleccionada = club.obtenerObjetoCategoria((String) getCbCategoriaEquipo().getSelectedItem());
			añadirJugadoresATabla(getModeloJugadores(), categoriaSeleccionada);
		}
		return tbJugadores;
	}
	
	private DefaultTableModel getModeloJugadores() {
		if (modeloJugadores == null) {
			modeloJugadores = new DefaultTableModel(
	            new Object[]{"Seleccionar", "Nombre", "Edad", "Posición"}, 0
	        ) {
	            @Override
	            public Class<?> getColumnClass(int columnIndex) {
	                if (columnIndex == 0) return Boolean.class;
	                return super.getColumnClass(columnIndex);
	            }

	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return column == 0;
	            }
	        };
		}
		return modeloJugadores;
	}
	
	private JPanel getPnInferior() {
		if (pnInferior == null) {
			pnInferior = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnInferior.getLayout();
			flowLayout.setHgap(15);
			flowLayout.setAlignment(FlowLayout.RIGHT);
			pnInferior.add(getBtnAtras());
			pnInferior.add(getBtnCrearEquipo());
		}
		return pnInferior;
	}
	
	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atras");
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btnAtras.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return btnAtras;
	}
	
	private JButton getBtnCrearEquipo() {
		if (btnCrearEquipo == null) {
			btnCrearEquipo = new JButton("Crear equipo");
			btnCrearEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
			btnCrearEquipo.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        List<EmpleadoDeportivo> jugadoresSeleccionados = new ArrayList<>();
			        for (int i = 0; i < getModeloJugadores().getRowCount(); i++) {
			            Boolean seleccionado = (Boolean) getModeloJugadores().getValueAt(i, 0);
			            if (seleccionado != null && seleccionado) {
			                try {
								jugadoresSeleccionados.add(club.obtenerJugadoresDisponibles((CategoriaEquipo) getCbCategoriaEquipo().getSelectedItem()).get(i));
							} catch (Exception ex) {
					            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
					        }
			            }
			        }

			        List<Entrenador> entrenadoresSeleccionados = new ArrayList<>();
			        List<EmpleadoDeportivo> restoTecnicosSeleccionados = new ArrayList<>();
			        
			        Entrenador primerEntrenador = (Entrenador) getCbPrimerEntrenador().getSelectedItem();
			        Entrenador segundoEntrenador = (Entrenador) getCbSegundoEntrenador().getSelectedItem();
			        
			        if (primerEntrenador != null) {
			        	entrenadoresSeleccionados.add(primerEntrenador);
			        }
			        
			        if (segundoEntrenador != null && !segundoEntrenador.equals(primerEntrenador)) {
			            entrenadoresSeleccionados.add(segundoEntrenador);
			        }

			        List<EmpleadoDeportivo> restoTecnicosSeleccionadosLista = getListRestoTecnicos().getSelectedValuesList();
			        for (EmpleadoDeportivo otroTecnicoMarcado : restoTecnicosSeleccionadosLista) {
			            if (!restoTecnicosSeleccionados.contains(otroTecnicoMarcado)) {
			                restoTecnicosSeleccionados.add(otroTecnicoMarcado);
			            }
			        }

			        String nombre = (String) getTxNombreEquipo().getText();
			        String tipo = (String) getCbTipoEquipo().getSelectedItem();
			        CategoriaEquipo categoria = (CategoriaEquipo) getCbCategoriaEquipo().getSelectedItem();

			        try {
			            club.añadirEquipo(jugadoresSeleccionados, entrenadoresSeleccionados, restoTecnicosSeleccionados, tipo, categoria, nombre);
			            JOptionPane.showMessageDialog(null, "¡Equipo creado con éxito!");
			        } catch (Exception ex) {
			            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
			        }
			    }
			});
		}
		return btnCrearEquipo;
	}

	private void añadirJugadoresATabla(DefaultTableModel modeloJugadores, CategoriaEquipo categoria) {
		List<EmpleadoDeportivo> listaJugadores = this.club.obtenerJugadoresDisponibles(categoria);
        for (EmpleadoDeportivo jugador : listaJugadores) {
            modeloJugadores.addRow(new Object[]{
                false,
                jugador.getNombre() + " " + jugador.getApellido(), 
                jugador.calcularEdad(), 
                jugador.getPosicion()
            });
        }
	}
	private JPanel getPnNombreEquipo() {
		if (pnNombreEquipo == null) {
			pnNombreEquipo = new JPanel();
			pnNombreEquipo.add(getLbNombreEquipo());
			pnNombreEquipo.add(getTxNombreEquipo());
		}
		return pnNombreEquipo;
	}
	private JLabel getLbNombreEquipo() {
		if (lbNombreEquipo == null) {
			lbNombreEquipo = new JLabel("Nombre del equipo:");
			lbNombreEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lbNombreEquipo;
	}
	private JTextField getTxNombreEquipo() {
		if (txNombreEquipo == null) {
			txNombreEquipo = new JTextField();
			txNombreEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
			txNombreEquipo.setColumns(10);
		}
		return txNombreEquipo;
	}
}