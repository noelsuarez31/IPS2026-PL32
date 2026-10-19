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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import giis.demo.model.empleado.EmpleadoDeportivo;
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
	private JComboBox<String> cbCategoriaEquipo;
	private JComboBox<String> cbTipoEquipo;
	private JPanel pnCentro;
	private JPanel pnCuerpoTecnico;
	private JLabel lbPrimerEntrenador;
	private JComboBox<EmpleadoDeportivo> cbPrimerEntrenador;
	private JComboBox<EmpleadoDeportivo> cbSegundoEntrenador;
	private JPanel pnPrimerYSegundoEntrenador;
	private JLabel lbSegundoEntrenador;
	private JPanel pnRestoDeTecnicos;
	private JLabel lbRestoTecnicosTitulo;
	private DefaultListModel<EmpleadoDeportivo> modeloListaRestoTecnicos;
	private JList<EmpleadoDeportivo> listRestoTecnicos;
	private JScrollPane scrRestoTecnicos;
	private JPanel pnJugadores;
	private DefaultListModel<EmpleadoDeportivo> modeloJugadoresDisponibles;
	private JList<EmpleadoDeportivo> listDisponibles;
	private JScrollPane scrDisponibles;
	private DefaultListModel<EmpleadoDeportivo> modeloJugadoresSeleccionados;
	private JList<EmpleadoDeportivo> listSeleccionados;
	private JScrollPane scrSeleccionados;
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
		
		setLocationRelativeTo(null);

		// Cargamos los jugadores segun los valores por defecto de los combo
		cargarJugadoresInicialesPorDefecto();
		
		this.setLocationRelativeTo(null);
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
			lbTipoEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbTipoEquipo;
	}
	
	private JComboBox<String> getCbTipoEquipo() {
		if (cbTipoEquipo == null) {
			cbTipoEquipo = new JComboBox<String>();
			cbTipoEquipo.setModel(new DefaultComboBoxModel<String>(new String[] {"Profesional", "En formacion"}));
			cbTipoEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
			cbTipoEquipo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String seleccionado = (String) cbTipoEquipo.getSelectedItem();
			        if (seleccionado != null) {
			            List<String> categorias = club.obtenerNombreDeCategoriasPorTipo(seleccionado);
			            getCbCategoriaEquipo().setModel(new DefaultComboBoxModel<String>(categorias.toArray(new String[0])));
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
			lbCategoriaEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbCategoriaEquipo;
	}
	
	private JComboBox<String> getCbCategoriaEquipo() {
	    if (cbCategoriaEquipo == null) {
	        cbCategoriaEquipo = new JComboBox<String>();
	        cbCategoriaEquipo.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                if(modeloJugadoresDisponibles == null || modeloJugadoresSeleccionados == null) {
	                    return;
	                }
	                modeloJugadoresDisponibles.clear();
	                modeloJugadoresSeleccionados.clear(); // Limpiamos seleccionados al cambiar de categoría
	                
	                String nombreCategoria = (String) getCbCategoriaEquipo().getSelectedItem();
	                if (nombreCategoria != null) {
	                    CategoriaEquipo categoriaSeleccionada = club.obtenerObjetoCategoria(nombreCategoria);
	                    
	                    if (categoriaSeleccionada != null) {
	                        List<EmpleadoDeportivo> listaJugadoresDisponibles = club.obtenerJugadoresDisponibles(categoriaSeleccionada);
	                        
	                        for (EmpleadoDeportivo jugador : listaJugadoresDisponibles) {
	                            modeloJugadoresDisponibles.addElement(jugador);
	                        }
	                    }
	                }
	            }
	        });
	        cbCategoriaEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
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
	
	private JComboBox<EmpleadoDeportivo> getCbPrimerEntrenador() {
		if (cbPrimerEntrenador == null) {
			cbPrimerEntrenador = new JComboBox<EmpleadoDeportivo>();
			cbPrimerEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
			List<EmpleadoDeportivo> tecnicos = club.obtenerEntrenadoresDisponibles();
			cbPrimerEntrenador.setModel(new DefaultComboBoxModel<EmpleadoDeportivo>(tecnicos.toArray(new EmpleadoDeportivo[0])));
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
	
	private JComboBox<EmpleadoDeportivo> getCbSegundoEntrenador() {
		if (cbSegundoEntrenador == null) {
			cbSegundoEntrenador = new JComboBox<EmpleadoDeportivo>();
			cbSegundoEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
			List<EmpleadoDeportivo> tecnicos = club.obtenerEntrenadoresDisponibles();
			cbSegundoEntrenador.setModel(new DefaultComboBoxModel<EmpleadoDeportivo>(tecnicos.toArray(new EmpleadoDeportivo[0])));
			gestionNoSonIguales(tecnicos);
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
	        // Cambiamos a GridLayout de 1 fila y 2 columnas para poner las dos listas juntas
	        pnJugadores.setLayout(new GridLayout(1, 2, 10, 0));
	        
	        // Panel izquierdo: Disponibles
	        JPanel pnIzq = new JPanel(new BorderLayout());
	        pnIzq.add(new JLabel("Disponibles:", JLabel.CENTER), BorderLayout.NORTH);
	        pnIzq.add(getScrDisponibles(), BorderLayout.CENTER);
	        pnJugadores.add(pnIzq);
	        
	        // Panel derecho: Seleccionados
	        JPanel pnDer = new JPanel(new BorderLayout());
	        pnDer.add(new JLabel("Seleccionados:", JLabel.CENTER), BorderLayout.NORTH);
	        pnDer.add(getScrSeleccionados(), BorderLayout.CENTER);
	        pnJugadores.add(pnDer);
	    }
	    return pnJugadores;
	}

	private JScrollPane getScrDisponibles() {
	    if (scrDisponibles == null) {
	        scrDisponibles = new JScrollPane(getListDisponibles());
	        scrDisponibles.setPreferredSize(new Dimension(0, 120));
	    }
	    return scrDisponibles;
	}

	private JList<EmpleadoDeportivo> getListDisponibles() {
	    if (listDisponibles == null) {
	        modeloJugadoresDisponibles = new DefaultListModel<>();
	        listDisponibles = new JList<EmpleadoDeportivo>(modeloJugadoresDisponibles);
	        listDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        listDisponibles.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        
	        // Evento de doble clic para pasar a seleccionados
	        listDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
	                if (evt.getClickCount() == 2) {
	                    pasarASeleccionados();
	                }
	            }
	        });
	    }
	    return listDisponibles;
	}

	private JScrollPane getScrSeleccionados() {
	    if (scrSeleccionados == null) {
	        scrSeleccionados = new JScrollPane(getListSeleccionados());
	        scrSeleccionados.setPreferredSize(new Dimension(0, 120));
	    }
	    return scrSeleccionados;
	}

	private JList<EmpleadoDeportivo> getListSeleccionados() {
	    if (listSeleccionados == null) {
	        modeloJugadoresSeleccionados = new DefaultListModel<>();
	        listSeleccionados = new JList<EmpleadoDeportivo>(modeloJugadoresSeleccionados);
	        listSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        listSeleccionados.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        
	        // Evento de doble clic para devolver a disponibles
	        listSeleccionados.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
	                if (evt.getClickCount() == 2) {
	                    devolverADisponibles();
	                }
	            }
	        });
	    }
	    return listSeleccionados;
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
					vPrincipal.setVisible(true);
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
			    	crearEquipo();
			    }
			}
			);
		}
		return btnCrearEquipo;
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
			lbNombreEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbNombreEquipo;
	}
	private JTextField getTxNombreEquipo() {
		if (txNombreEquipo == null) {
			txNombreEquipo = new JTextField();
			txNombreEquipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
			txNombreEquipo.setColumns(10);
		}
		return txNombreEquipo;
	}
	
	private void crearEquipo() {
		List<EmpleadoDeportivo> jugadoresSeleccionados = new ArrayList<>();
	    for (int i = 0; i < modeloJugadoresSeleccionados.getSize(); i++) {
	        jugadoresSeleccionados.add(modeloJugadoresSeleccionados.getElementAt(i));
	    }
	    
        List<EmpleadoDeportivo> entrenadoresSeleccionados = new ArrayList<>();
        List<EmpleadoDeportivo> restoTecnicosSeleccionados = new ArrayList<>();
        
        EmpleadoDeportivo primerEntrenador = (EmpleadoDeportivo) getCbPrimerEntrenador().getSelectedItem();
        EmpleadoDeportivo segundoEntrenador = (EmpleadoDeportivo) getCbSegundoEntrenador().getSelectedItem();
        
        if (primerEntrenador != null) {
        	entrenadoresSeleccionados.add(primerEntrenador);
        }
        
        if (segundoEntrenador != null) {
            if(!segundoEntrenador.esMismoEntrenador(primerEntrenador)) {
            	entrenadoresSeleccionados.add(segundoEntrenador);
            } else {
            	JOptionPane.showMessageDialog(null, "El primer y el segundo entrenador no pueden ser la misma persona");
            	return;
            }
        	
        }

        List<EmpleadoDeportivo> restoTecnicosSeleccionadosLista = getListRestoTecnicos().getSelectedValuesList();
        for (EmpleadoDeportivo otroTecnicoMarcado : restoTecnicosSeleccionadosLista) {
            if (!restoTecnicosSeleccionados.contains(otroTecnicoMarcado)) {
                restoTecnicosSeleccionados.add(otroTecnicoMarcado);
            }
        }

        String nombre = (String) getTxNombreEquipo().getText();
        if(nombre == null || nombre.isEmpty()) {
        	JOptionPane.showMessageDialog(null, "El nombre del equipo no puede ser vacio");
        	return;
        }
       
        String tipo = (String) getCbTipoEquipo().getSelectedItem();
        CategoriaEquipo categoria = club.obtenerObjetoCategoria((String) getCbCategoriaEquipo().getSelectedItem());

        try {
            club.añadirEquipo(jugadoresSeleccionados, entrenadoresSeleccionados, restoTecnicosSeleccionados, tipo, categoria, nombre);
            JOptionPane.showMessageDialog(null, "¡Equipo creado con éxito!");
            dispose();
            vPrincipal.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	private void gestionNoSonIguales(List<EmpleadoDeportivo> tecnicos) {
		if (tecnicos != null && tecnicos.size() > 1) {
			String dniPrimerEntrenador = ((EmpleadoDeportivo) getCbPrimerEntrenador().getSelectedItem()).getDni();
			String dniSegundoEntrenador = ((EmpleadoDeportivo) getCbSegundoEntrenador().getSelectedItem()).getDni();
			if (dniPrimerEntrenador.equals(dniSegundoEntrenador)) {
				getCbSegundoEntrenador().setSelectedItem(tecnicos.get(1));
			}
		}
	}
    
    private void pasarASeleccionados() {
        EmpleadoDeportivo seleccionado = listDisponibles.getSelectedValue();
        if (seleccionado != null) {
            modeloJugadoresDisponibles.removeElement(seleccionado);
            modeloJugadoresSeleccionados.addElement(seleccionado);
        }
    }

    private void devolverADisponibles() {
        EmpleadoDeportivo seleccionado = listSeleccionados.getSelectedValue();
        if (seleccionado != null) {
            modeloJugadoresSeleccionados.removeElement(seleccionado);
            modeloJugadoresDisponibles.addElement(seleccionado);
        }
    }
    
    private void cargarJugadoresInicialesPorDefecto() {
        String nombreCategoria = (String) getCbCategoriaEquipo().getSelectedItem();
        if (nombreCategoria != null && modeloJugadoresDisponibles != null) {
            modeloJugadoresDisponibles.clear();
            CategoriaEquipo categoriaSeleccionada = club.obtenerObjetoCategoria(nombreCategoria);
            
            if (categoriaSeleccionada != null) {
                List<EmpleadoDeportivo> listaJugadoresDisponibles = club.obtenerJugadoresDisponibles(categoriaSeleccionada);
                for (EmpleadoDeportivo jugador : listaJugadoresDisponibles) {
                    modeloJugadoresDisponibles.addElement(jugador);
                }
            }
        }
    }
}