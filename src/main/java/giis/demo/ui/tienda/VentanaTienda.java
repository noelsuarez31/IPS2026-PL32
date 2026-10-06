package giis.demo.ui.tienda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import giis.demo.model.tienda.Producto;
import giis.demo.model.tienda.Venta;
import giis.demo.ui.VentanaPrincipal;

public class VentanaTienda extends JFrame{

	private static final long serialVersionUID = 1L;
    private Venta controladorVenta;
    private VentanaPrincipal vP;
    
    private JPanel contentPane;
    private JPanel pnArticulos;
    private JTextField txPrecio;
    private JList<Producto> listPedido;
    private DefaultListModel<Producto> modeloListPedido;
    
    private AccionBotonProducto accionBotonProducto = new AccionBotonProducto();
    private ProcesaBotonFiltro accionFiltro = new ProcesaBotonFiltro();

    public VentanaTienda(VentanaPrincipal ventanaPrincipal) {
        this.vP = ventanaPrincipal;
        this.controladorVenta = new Venta();
        
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(ventanaPrincipal);
        
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        
        contentPane.add(crearPanelCentral(), BorderLayout.CENTER);
        contentPane.add(crearPanelFiltros(), BorderLayout.WEST);
        contentPane.add(crearPanelSur(), BorderLayout.SOUTH);
        contentPane.add(crearPanelPedido(), BorderLayout.EAST);
        
        inicializarTienda();
    }

    private JPanel crearPanelCentral() {
        JPanel pnCentral = new JPanel(new BorderLayout());
        pnArticulos = new JPanel(new GridLayout(0, 4, 5, 5));
        pnArticulos.setBorder(new LineBorder(Color.GRAY, 1));
        
        JScrollPane scrollArticulos = new JScrollPane(pnArticulos);
        pnCentral.add(scrollArticulos, BorderLayout.CENTER);
        
        generarBotonesCatalogo();
        return pnCentral;
    }

    private JPanel crearPanelPedido() {
        JPanel pnPedido = new JPanel(new BorderLayout());
        pnPedido.setPreferredSize(new Dimension(250, 0));
        pnPedido.setBorder(BorderFactory.createTitledBorder("Tu Pedido"));
        
        modeloListPedido = new DefaultListModel<>();
        listPedido = new JList<>(modeloListPedido);
        listPedido.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Permite borrar con la tecla Suprimir
        listPedido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    eliminarProductoSeleccionado();
                }
            }
        });
        
        pnPedido.add(new JScrollPane(listPedido), BorderLayout.CENTER);
        return pnPedido;
    }

    private JPanel crearPanelSur() {
        JPanel pnSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txPrecio = new JTextField("Total: 0.00€");
        txPrecio.setEditable(false);
        txPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        txPrecio.setBackground(Color.ORANGE);
        txPrecio.setColumns(10);
        
        JButton btnVaciar = new JButton("Vaciar Cesta");
        btnVaciar.addActionListener(e -> vaciarCesta());
        
        pnSur.add(btnVaciar);
        pnSur.add(txPrecio);
        return pnSur;
    }

    private JPanel crearPanelFiltros() {
        JPanel pnFiltro = new JPanel(new GridLayout(4, 1, 0, 5));
        pnFiltro.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        String[] categorias = {"Todos", "Ropa", "Accesorios"}; // Ajustar según tus datos
        for (String cat : categorias) {
            JButton btnFiltro = new JButton(cat);
            btnFiltro.setActionCommand(cat);
            btnFiltro.addActionListener(accionFiltro);
            pnFiltro.add(btnFiltro);
        }
        return pnFiltro;
    }

    private void generarBotonesCatalogo() {
        pnArticulos.removeAll();
        for (int i = 0; i < controladorVenta.getCatalogo().size(); i++) {
            Producto p = controladorVenta.getCatalogo().get(i);
            JButton btnProducto = new JButton("<html><center>" + p.getNombre() + "<br>" + p.getPrecio() + "€</center></html>");
            btnProducto.setActionCommand(String.valueOf(i));
            btnProducto.addActionListener(accionBotonProducto);
            // Si tienes imágenes, configúralas aquí como en tu código original
            pnArticulos.add(btnProducto);
        }
        pnArticulos.revalidate();
        pnArticulos.repaint();
    }

    private void inicializarTienda() {
        vaciarCesta();
        aplicarFiltro("Todos");
    }

    private void añadirAlPedido(int indexCatalogo) {
        Producto p = controladorVenta.getProductoPorPosicion(indexCatalogo);
        if (p != null) {
            controladorVenta.getCesta().añadirProducto(p);
            modeloListPedido.addElement(p);
            actualizarPrecio();
        }
    }

    private void eliminarProductoSeleccionado() {
        int index = listPedido.getSelectedIndex();
        if (index != -1) {
            Producto p = modeloListPedido.getElementAt(index);
            controladorVenta.getCesta().eliminarProducto(p);
            modeloListPedido.remove(index);
            actualizarPrecio();
        }
    }

    private void vaciarCesta() {
        controladorVenta.getCesta().vaciar();
        modeloListPedido.removeAllElements();
        actualizarPrecio();
    }

    private void actualizarPrecio() {
        txPrecio.setText(String.format("Total: %.2f€", controladorVenta.getCesta().getTotal()));
    }

    private void aplicarFiltro(String categoria) {
        for (int i = 0; i < pnArticulos.getComponentCount(); i++) {
            Component c = pnArticulos.getComponent(i);
            if (c instanceof JButton) {
                boolean cumple = controladorVenta.cumpleFiltro(i, categoria);
                c.setEnabled(cumple);
            }
        }
    }

    class AccionBotonProducto implements ActionListener {
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = Integer.parseInt(e.getActionCommand());
            añadirAlPedido(index);
        }
    }

    class ProcesaBotonFiltro implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            aplicarFiltro(e.getActionCommand());
        }
    }

}
