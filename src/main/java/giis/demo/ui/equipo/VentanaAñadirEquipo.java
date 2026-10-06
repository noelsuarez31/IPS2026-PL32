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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.exceptions.TeamException;
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
	private JTable tbJugadores;
	private JList<EmpleadoDeportivo> listRestoTecnicos;
	private JComboBox<String> cbTipoEquipo;
	private JPanel pnCentro;
	private JPanel pnCuerpoTecnico;
	private	JLabel lbPrimerEntrenador;
	private JComboBox<Entrenador> cbPrimerEntrenador;
	private JComboBox<Entrenador> cbSegundoEntrenador;
	private JPanel pnPrimerYSegundoEntrenador;
	private JLabel lbSegundoEntrenador;
	private JPanel pnRestoDeTecnicos;
	private JLabel lbRestoTecnicosTitulo;
	private DefaultListModel<EmpleadoDeportivo> modeloListaRestoTecnicos;
	private JScrollPane scrRestoTecnicos;
	private JPanel pnJugadores;
	private JScrollPane scrJugadores;
	private DefaultTableModel modeloJugadores;
	private JScrollPane scrPanelPrincipal;
	private JPanel pnInferior;
	private JButton btnAtras;
	private JButton btnCrearEquipo;
	
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
		
	
		pnSuperior = new JPanel();
		contentPane.add(pnSuperior, BorderLayout.NORTH);
		pnSuperior.setLayout(new GridLayout(0, 2, 0, 0));
		
		pnTipoEquipo = new JPanel();
		pnSuperior.add(pnTipoEquipo);
		
		lbTipoEquipo = new JLabel("Tipo de Equipo:");
		lbTipoEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		pnTipoEquipo.add(lbTipoEquipo);
		
		pnCategoriaEquipo = new JPanel();
		pnSuperior.add(pnCategoriaEquipo);
		
		lbCategoriaEquipo = new JLabel("Categoria del Equipo:");
		lbCategoriaEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		pnCategoriaEquipo.add(lbCategoriaEquipo);
		
		cbCategoriaEquipo = new JComboBox<CategoriaEquipo>();
		cbCategoriaEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		pnCategoriaEquipo.add(cbCategoriaEquipo);
		
		cbTipoEquipo = new JComboBox<String>();
		cbTipoEquipo.setModel(new DefaultComboBoxModel<String>( new String[] {"Profesional", "En formacion"}));
		
		cbTipoEquipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String seleccionado = (String) cbTipoEquipo.getSelectedItem();
		        
		        if (seleccionado != null) {
		            List<CategoriaEquipo> categorias = club.obtenerCategoriasPorTipo(seleccionado);
		            cbCategoriaEquipo.setModel(new DefaultComboBoxModel<CategoriaEquipo>(categorias.toArray(new CategoriaEquipo[0])));
		            
		            if(cbCategoriaEquipo.getItemCount() > 0) {
		            	cbCategoriaEquipo.setSelectedIndex(0);
		            }
		        }
			}
		});
		
		// Para que haya uno seleccionado
		if (cbTipoEquipo.getItemCount() > 0) {
			cbTipoEquipo.setSelectedIndex(0); 
		}
		
		cbTipoEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		pnTipoEquipo.add(cbTipoEquipo);
		
		pnCentro = new JPanel();
		contentPane.add(pnCentro, BorderLayout.CENTER);
		pnCentro.setLayout(new BorderLayout(0, 0));
		
		pnCuerpoTecnico = new JPanel();
		pnCuerpoTecnico.setBorder(new TitledBorder(null, "Cuerpo T\u00E9cnico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnCentro.add(pnCuerpoTecnico, BorderLayout.NORTH);
		
		lbPrimerEntrenador = new JLabel("Primer entrenador:");
		lbPrimerEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		cbPrimerEntrenador = new JComboBox<Entrenador>();
		cbPrimerEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		cbSegundoEntrenador = new JComboBox<Entrenador>();
		cbSegundoEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		List<Entrenador> tecnicos = club.obtenerEntrenadoresDisponibles();
		pnCuerpoTecnico.setLayout(new BorderLayout(0, 0));
		cbPrimerEntrenador.setModel(new DefaultComboBoxModel<Entrenador>(tecnicos.toArray(new Entrenador[0])));
		cbSegundoEntrenador.setModel(new DefaultComboBoxModel<Entrenador>(tecnicos.toArray(new Entrenador[0])));
		
		pnPrimerYSegundoEntrenador = new JPanel();
		
		lbSegundoEntrenador = new JLabel("Segundo entrenador:");
		lbSegundoEntrenador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pnPrimerYSegundoEntrenador.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pnPrimerYSegundoEntrenador.add(lbPrimerEntrenador);
		pnPrimerYSegundoEntrenador.add(cbPrimerEntrenador);
		pnPrimerYSegundoEntrenador.add(lbSegundoEntrenador);
		pnPrimerYSegundoEntrenador.add(cbSegundoEntrenador);
		
		pnCuerpoTecnico.add(pnPrimerYSegundoEntrenador, BorderLayout.NORTH);
		
		pnRestoDeTecnicos = new JPanel(new BorderLayout(0,3));
		
		lbRestoTecnicosTitulo = new JLabel("Si quieres añadir técnicos adicionales, seleccionalos:");
		lbRestoTecnicosTitulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnRestoDeTecnicos.add(lbRestoTecnicosTitulo, BorderLayout.NORTH);
		
		modeloListaRestoTecnicos = new DefaultListModel<>();
		List<EmpleadoDeportivo> listaRestoTecnicos = club.obtenerRestoTecnicos();
		for (EmpleadoDeportivo tecnico : listaRestoTecnicos) {
		    modeloListaRestoTecnicos.addElement(tecnico);
		}
		
		listRestoTecnicos = new JList<EmpleadoDeportivo>(modeloListaRestoTecnicos);
		// Habilitamos la selección múltiple 
		listRestoTecnicos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listRestoTecnicos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		scrRestoTecnicos = new JScrollPane(listRestoTecnicos);
		scrRestoTecnicos.setPreferredSize(new Dimension(0, 90)); // Altura compacta controlada
		pnRestoDeTecnicos.add(scrRestoTecnicos, BorderLayout.CENTER);
		
		pnCuerpoTecnico.add(pnRestoDeTecnicos);
		
		pnJugadores = new JPanel();
		pnJugadores.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Seleccion de jugadores (Min.7)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnCentro.add(pnJugadores, BorderLayout.CENTER);
		pnJugadores.setLayout(new BorderLayout(0, 0));
		
		scrJugadores = new JScrollPane();
		pnJugadores.add(scrJugadores, BorderLayout.CENTER);
		
		/* Configuramos el modelo de la tabla de jugadores */
		modeloJugadores = new DefaultTableModel(
	            new Object[]{"Seleccionar", "Nombre", "Edad", "Posición"}, 0
	        ) {
	            @Override
	            public Class<?> getColumnClass(int columnIndex) {
	                if (columnIndex == 0) return Boolean.class; // Checkbox
	                return super.getColumnClass(columnIndex);
	            }

	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return column == 0; // Solo deja clickear el checkbox
	            }
	        };
		
		tbJugadores = new JTable(modeloJugadores);
		
		/* Añadimos los jugadores */
		CategoriaEquipo categoriaSeleccionada = club.obtenerObjetoCategoria((String) cbCategoriaEquipo.getSelectedItem());
		añadirJugadoresATabla(modeloJugadores, categoriaSeleccionada);
		scrJugadores.setViewportView(tbJugadores);
		
		scrPanelPrincipal = new JScrollPane(pnCentro);
		scrPanelPrincipal.getVerticalScrollBar().setUnitIncrement(16);
		contentPane.add(scrPanelPrincipal, BorderLayout.CENTER);
		
		pnInferior = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pnInferior.getLayout();
		flowLayout.setHgap(15);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(pnInferior, BorderLayout.SOUTH);
		
		btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				dispose();
			}
		});
		btnAtras.setFont(new Font("Tahoma", Font.PLAIN, 18));
		pnInferior.add(btnAtras);
		
		btnCrearEquipo = new JButton("Crear equipo");
		btnCrearEquipo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		btnCrearEquipo.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // --- 1. RECOGER JUGADORES (Código de antes) ---
		        List<EmpleadoDeportivo> jugadoresSeleccionados = new ArrayList<>();
		        for (int i = 0; i < modeloJugadores.getRowCount(); i++) {
		            Boolean seleccionado = (Boolean) modeloJugadores.getValueAt(i, 0);
		            if (seleccionado != null && seleccionado) {
		                jugadoresSeleccionados.add(club.obtenerJugadoresDisponibles((CategoriaEquipo) cbCategoriaEquipo.getSelectedItem()).get(i));
		            }
		        }

		        // --- 2. RECOGER ENTRENADORES ---
		        List<Entrenador> entrenadoresSeleccionados = new ArrayList<>();
		        List<EmpleadoDeportivo> restoTecnicosSeleccionados = new ArrayList<>();
		        
		        // A. Coger los obligatorios de los ComboBox
		        Entrenador primerEntrenador = (Entrenador) cbPrimerEntrenador.getSelectedItem();
		        Entrenador segundoEntrenador = (Entrenador) cbSegundoEntrenador.getSelectedItem();
		        
		        if (primerEntrenador != null) {
		        	entrenadoresSeleccionados.add(primerEntrenador);
		        }
		        
		        // Evitar que elijan al mismo tío de primer y segundo entrenador
		        if (segundoEntrenador != null && !segundoEntrenador.equals(primerEntrenador)) {
		            entrenadoresSeleccionados.add(segundoEntrenador);
		        }

		        // B. Coger los técnicos adicionales seleccionados en la JList de forma directa
		        List<EmpleadoDeportivo> restoTecnicosSeleccionadosLista = listRestoTecnicos.getSelectedValuesList();
		        for (EmpleadoDeportivo otroTecnicoMarcado : restoTecnicosSeleccionadosLista) {
		            // Solo lo añadimos si no estaba ya elegido en los combos principales
		            if (!restoTecnicosSeleccionados.contains(otroTecnicoMarcado)) {
		                restoTecnicosSeleccionados.add(otroTecnicoMarcado);
		            }
		        }

		        // --- 3. RECOGER TIPO Y CATEGORÍA ---
		        String tipo = (String) cbTipoEquipo.getSelectedItem();
		        CategoriaEquipo categoria = (CategoriaEquipo) cbCategoriaEquipo.getSelectedItem();

		        // --- 4. LLAMAR A LA CAPA DE NEGOCIO ---
		        try {
		            club.añadirEquipoProfesional(jugadoresSeleccionados, entrenadoresSeleccionados, tipo, categoria);
		            JOptionPane.showMessageDialog(null, "¡Equipo creado con éxito!");
		            // Aquí podrías vaciar el formulario o cerrar la ventana
		        } catch (TeamException ex) {
		            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		pnInferior.add(btnCrearEquipo);

	}

	private void añadirJugadoresATabla(DefaultTableModel modeloJugadores, CategoriaEquipo categoria) {
		List<EmpleadoDeportivo> listaJugadores = this.club.obtenerJugadoresDisponibles(categoria);
        
        for (EmpleadoDeportivo jugador : listaJugadores) {
            modeloJugadores.addRow(new Object[]{
                false, // Checkbox desmarcado por defecto
                jugador.getNombre() + " " + jugador.getApellido(), 
                jugador.calcularEdad(), 
                jugador.getPosicion()
            });
        }
	}

}
