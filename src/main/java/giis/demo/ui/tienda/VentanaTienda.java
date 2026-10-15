package giis.demo.ui.tienda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.model.tienda.Producto;
import giis.demo.model.tienda.VentaMerchandising;
import giis.demo.ui.VentanaPrincipal;

public class VentanaTienda extends JFrame {

    private static final long serialVersionUID = 1L;
    private VentaMerchandising controladorVenta;
    
    private JPanel contentPane;
    private JPanel pnArticulos;
    private JTextField txPrecio;
    private JTable tablaPedido;
    private DefaultTableModel modeloTablaPedido;
    
    private AccionBotonProducto accionBotonProducto = new AccionBotonProducto();

    public VentanaTienda(VentanaPrincipal ventanaPrincipal, VentaMerchandising venta) {
        this.controladorVenta = venta;
        
        setTitle("Venta de Merchandising");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Evita cerrar toda la app
        setBounds(100, 100, 900, 600);
        setLocationRelativeTo(ventanaPrincipal);
        
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        
        contentPane.add(crearPanelCentral(), BorderLayout.CENTER);
        contentPane.add(crearPanelPedido(), BorderLayout.EAST);
        contentPane.add(crearPanelSur(), BorderLayout.SOUTH);
        
        generarBotonesCatalogo();
        actualizarVistaCesta();
    }

    private JPanel crearPanelCentral() {
        JPanel pnCentral = new JPanel(new BorderLayout());
        pnCentral.setBorder(BorderFactory.createTitledBorder("Catálogo de Productos"));
        
        pnArticulos = new JPanel(new GridLayout(0, 3, 5, 5));
        pnArticulos.setBorder(new LineBorder(Color.GRAY, 1));
        
        JScrollPane scrollArticulos = new JScrollPane(pnArticulos);
        pnCentral.add(scrollArticulos, BorderLayout.CENTER);
        
        return pnCentral;
    }

    private JPanel crearPanelPedido() {
        JPanel pnPedido = new JPanel(new BorderLayout());
        pnPedido.setPreferredSize(new Dimension(350, 0));
        pnPedido.setBorder(BorderFactory.createTitledBorder("Cesta de Compra"));
        
        String[] columnas = {"Producto", "Uds.", "Subtotal"};
        modeloTablaPedido = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tablaPedido = new JTable(modeloTablaPedido);
        tablaPedido.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPedido.getColumnModel().getColumn(0).setPreferredWidth(180);
        tablaPedido.getColumnModel().getColumn(1).setPreferredWidth(50);
        tablaPedido.getColumnModel().getColumn(2).setPreferredWidth(80);
        
        pnPedido.add(new JScrollPane(tablaPedido), BorderLayout.CENTER);
        
        JPanel pnBotonesPedido = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        pnBotonesPedido.add(btnEliminar);
        
        pnPedido.add(pnBotonesPedido, BorderLayout.SOUTH);
        
        return pnPedido;
    }

    private JPanel crearPanelSur() {
        JPanel pnSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        txPrecio = new JTextField("Total: 0.00 €");
        txPrecio.setEditable(false);
        txPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        txPrecio.setBackground(Color.ORANGE);
        txPrecio.setColumns(12);
        txPrecio.setHorizontalAlignment(JTextField.RIGHT);
        
        JButton btnFinalizar = new JButton("Finalizar Compra");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setBackground(new Color(0, 128, 0));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.addActionListener(e -> finalizarCompra());
        
        pnSur.add(txPrecio);
        pnSur.add(btnFinalizar);
        
        return pnSur;
    }

    private void generarBotonesCatalogo() {
        pnArticulos.removeAll();
        for (int i = 0; i < controladorVenta.getCatalogo().size(); i++) {
            Producto p = controladorVenta.getCatalogo().get(i);
            
            JButton btnProducto = new JButton(String.format("<html><center><b>%s</b><br>%.2f €</center></html>", 
                                              p.getNombre(), p.getPrecio()));
            
            // Usamos el ID de BBDD pasado a String
            btnProducto.setActionCommand(String.valueOf(p.getId_producto())); 
            btnProducto.addActionListener(accionBotonProducto);
            
            setImagenAdaptada(btnProducto, "/img/" + p.getId_producto() + ".png");
            
            btnProducto.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnProducto.setHorizontalTextPosition(SwingConstants.CENTER);
            btnProducto.setBackground(Color.WHITE);
            
            pnArticulos.add(btnProducto);
        }
        pnArticulos.revalidate();
        pnArticulos.repaint();
    }

    private void añadirAlPedido(String idProductoStr) {
        int idProducto = Integer.parseInt(idProductoStr);
        Producto p = controladorVenta.getProductoPorId(idProducto);
        if (p != null) {
            controladorVenta.getCesta().añadirProducto(p);
            actualizarVistaCesta();
        }
    }
    
    private void setImagenAdaptada(JButton boton, String rutaImagen) {
        try {
            java.net.URL imgUrl = getClass().getResource(rutaImagen);
            if (imgUrl != null) {
                Image imgOriginal = new ImageIcon(imgUrl).getImage();
                Image imgEscalada = imgOriginal.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imgEscalada));
            } else {
                System.err.println("Aviso: No se encontró la imagen en la ruta " + rutaImagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + rutaImagen);
        }
    }

    private void eliminarProductoSeleccionado() {
        int fila = tablaPedido.getSelectedRow();
        if (fila != -1) {
            String nombreProducto = (String) modeloTablaPedido.getValueAt(fila, 0);
            Producto p = controladorVenta.getProductoPorNombre(nombreProducto);
            
            if (p != null) {
                controladorVenta.getCesta().eliminarProducto(p);
                actualizarVistaCesta();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarVistaCesta() {
        modeloTablaPedido.setRowCount(0); 
        
        for (Map.Entry<Producto, Integer> linea : controladorVenta.getCesta().getLineas().entrySet()) {
            Producto p = linea.getKey();
            int unidades = linea.getValue();
            double subtotal = p.getPrecio() * unidades;
            
            modeloTablaPedido.addRow(new Object[]{
                p.getNombre(), 
                unidades, 
                String.format("%.2f €", subtotal)
            });
        }
        
        txPrecio.setText(String.format("Total: %.2f €", controladorVenta.getCesta().getTotal()));
    }

    private void finalizarCompra() {
        if (controladorVenta.getCesta().isVacia()) {
            JOptionPane.showMessageDialog(this, "La cesta está vacía. Añada productos antes de finalizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Persistir en la base
            controladorVenta.almacenarVenta();
            
            // Mostrar mensaje 
            String resumen = String.format("Resumen de compra:\n\n" +
                                           "Líneas de producto: %d\n" +
                                           "Unidades totales: %d\n" +
                                           "Precio total a cobrar: %.2f €", 
                                           controladorVenta.getCesta().getNumProductosDistintos(),
                                           controladorVenta.getCesta().getTotalUnidades(),
                                           controladorVenta.getCesta().getTotal());
            
            JOptionPane.showMessageDialog(this, resumen, "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar la vista
            controladorVenta.getCesta().vaciar();
            actualizarVistaCesta();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo guardar la venta en la base de datos.\nRevise su conexión.\n" + e.getMessage(), 
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    class AccionBotonProducto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            añadirAlPedido(e.getActionCommand());
        }
    }
}
