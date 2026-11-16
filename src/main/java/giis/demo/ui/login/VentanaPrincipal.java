package giis.demo.ui.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import giis.demo.model.contrataciones.GestorEmpleados;
import giis.demo.model.empleado.Posicion;
import giis.demo.model.entrevistas.CrearEntrevistaService;
import giis.demo.model.entrevistas.CrearFranjaService;
import giis.demo.model.equipo.AñadirEquipoService;
import giis.demo.model.equipo.CrearEntrenamientoService;
import giis.demo.model.equipo.PlanificarPartidoService;
import giis.demo.model.historial.HistorialVentas;
import giis.demo.model.horarios.GestionHorarioSemanal;
import giis.demo.model.horarios.GestionHorariosEspecificos;
import giis.demo.model.horarios.GestionHorariosPeriodicos;
import giis.demo.model.rol.UsuarioAutenticado;
import giis.demo.model.tienda.VentaMerchandising;
import giis.demo.ui.abonos.VentanaAbono;
import giis.demo.ui.abonos.entradas.VentanaIdentificacion;
import giis.demo.ui.accion.VentanaCampanasAcciones;
import giis.demo.ui.accion.VentanaCompraAcciones;
import giis.demo.ui.accion.VentanaCompraCampana;
import giis.demo.ui.accion.VentanaPortalAccionista;
import giis.demo.ui.contrataciones.VentanaAltaEmpleado;
import giis.demo.ui.contrataciones.VentanaGestionEmpleados;
import giis.demo.ui.entradas.VentanaVentaEntradas;
import giis.demo.ui.entrevistas.VentanaCrearEntrevistas;
import giis.demo.ui.entrevistas.VentanaCrearFranjas;
import giis.demo.ui.equipo.VentanaAñadirEquipo;
import giis.demo.ui.equipo.VentanaCrearEntrenamiento;
import giis.demo.ui.equipo.VentanaPlanificarPartido;
import giis.demo.ui.historial.VentanaHistorialVentas;
import giis.demo.ui.horarios.VentanaHorarioEspecifico;
import giis.demo.ui.horarios.VentanaHorarioPeriodico;
import giis.demo.ui.horarios.VentanaHorarioSemanal;
import giis.demo.ui.mantenimiento.VentanaAsignarMantenimiento;
import giis.demo.ui.reserva.VentanaReserva;
import giis.demo.ui.tienda.VentanaTienda;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {

	private GestionaTienda gestionaTienda = new GestionaTienda();
	private AñadirEquipoService añadirEquipo = new AñadirEquipoService();
	private GestionHorariosPeriodicos ghp = new GestionHorariosPeriodicos();
	private GestionHorariosEspecificos ghe = new GestionHorariosEspecificos();
	private GestionHorarioSemanal ghs = new GestionHorarioSemanal();
	private CrearFranjaService cfs = new CrearFranjaService();
	private CrearEntrevistaService ces = new CrearEntrevistaService();
	private GestorEmpleados gestorEmpleados = new GestorEmpleados();
	private CrearEntrenamientoService crearEntrenoService = new CrearEntrenamientoService();
	private PlanificarPartidoService pps = new PlanificarPartidoService();

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelUser;
	private JLabel lbName;
	private JTextField tfName;
	private JLabel lbRol;
	private JTextField tfRol;
	private JPanel panelOpciones;

	private UsuarioAutenticado usuario;
	private JPanel panelSalida;

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(UsuarioAutenticado user) {

		this.usuario = user;

		setTitle("Ventana Inicial");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 420);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelUser(), BorderLayout.NORTH);
		contentPane.add(getPanelOpciones(), BorderLayout.CENTER);
		contentPane.add(getPanelSalida(), BorderLayout.SOUTH);

		actualizarDatosUsuario();
		actualizarAccionesDisponibles();

		this.setLocationRelativeTo(null);

	}

	private void actualizarDatosUsuario() {

		tfName.setText(usuario.getDni());

		String posiciones = usuario.getPosiciones().toString();

		if (usuario.esAccionista()) {
			if (!posiciones.isEmpty()) {
				posiciones += " - ACCIONISTA";
			} else {
				posiciones = "ACCIONISTA";
			}
		}

		tfRol.setText(posiciones);
	}

	public AñadirEquipoService getAñadirEquipoService() {
		return this.añadirEquipo;
	}

	public GestionaTienda getGestionaTienda() {
		return this.gestionaTienda;
	}

	public GestionHorariosPeriodicos getGestionHorariosPeriodicos() {
		return this.ghp;
	}

	private void abrirVentanaCampanasAcciones() {
		VentanaCampanasAcciones ventana = new VentanaCampanasAcciones(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaHistorialVentas() {
		VentanaHistorialVentas ventana = new VentanaHistorialVentas(this, new HistorialVentas());
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaAltaEmpleado() {
		VentanaAltaEmpleado ventana = new VentanaAltaEmpleado(gestorEmpleados);
		this.setVisible(false);
		ventana.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				VentanaPrincipal.this.setVisible(true);
			}
		});
		ventana.setVisible(true);
	}

	private void abrirVentanaGestionEmpleados() {
		VentanaGestionEmpleados ventana = new VentanaGestionEmpleados(this, gestorEmpleados);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	/**
	 * Abre la ventana para vender las entradas
	 * 
	 * @throws SQLException
	 */
	private void abrirVentanaVentaEntradas() throws SQLException {

		VentanaVentaEntradas vve = new VentanaVentaEntradas(this);
		this.setVisible(false);
		vve.setVisible(true);

	}

	private class GestionaTienda implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			abrirVentanaTienda();
		}

	}

	public void abrirVentanaTienda() {
		try {
			// Instanciar lógica
			VentaMerchandising ventaActiva = new VentaMerchandising();

			// Cargar BBDD a memoria
			ventaActiva.cargarCatalogo();

			// Pasarle el controlador
			VentanaTienda vT = new VentanaTienda(this, ventaActiva);
			this.setVisible(false);
			vT.setVisible(true);

		} catch (SQLException ex) {

			javax.swing.JOptionPane.showMessageDialog(this,
					"Error al conectar con la base de datos para cargar el catálogo:\n" + ex.getMessage(),
					"Error Crítico", javax.swing.JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void abrirVentanaAñadirEquipo() {
		VentanaAñadirEquipo vAñadirEquipo = new VentanaAñadirEquipo(this);
		this.setVisible(false);
		vAñadirEquipo.setVisible(true);

	}

	private void abrirVentanaAñadirHorarioPeriodico() {
		VentanaHorarioPeriodico vHorarioPeriodico = new VentanaHorarioPeriodico(this);
		this.setVisible(false);
		vHorarioPeriodico.setVisible(true);

	}

	private void abrirVentanaAñadirHorarioEspecifico() {
		VentanaHorarioEspecifico vHorarioEspecifico = new VentanaHorarioEspecifico(this);
		this.setVisible(false);
		vHorarioEspecifico.setVisible(true);
	}

	public GestionHorariosEspecificos getGestionHorariosEspecificos() {

		return this.ghe;
	}

	private void abrirVentanaCrearFranjas() {
		VentanaCrearFranjas vFranjas = new VentanaCrearFranjas(this);
		this.setVisible(false);
		vFranjas.setVisible(true);
	}

	public CrearFranjaService getCrearFranjaService() {
		return this.cfs;
	}

	/**
	 * Abre la ventana para realizar la ventana de la reserva
	 * 
	 * @throws SQLException
	 */
	private void abrirVentanaReserva() throws SQLException {

		VentanaReserva vr = new VentanaReserva(this);
		this.setVisible(false);
		vr.setVisible(true);
	}

	private void abrirVentanaCrearEntrevistas() {
		VentanaCrearEntrevistas vCrearEntrev = new VentanaCrearEntrevistas(this);
		this.setVisible(false);
		vCrearEntrev.setVisible(true);
	}

	public CrearEntrevistaService getCrearEntrevistaService() {
		return this.ces;
	}

	private void abrirVentanaHorarioSemanal() {
		VentanaHorarioSemanal vHorarioSemanal = new VentanaHorarioSemanal(this);
		this.setVisible(false);
		vHorarioSemanal.setVisible(true);
	}

	public GestionHorarioSemanal getGestionHorarioSemanal() {
		return this.ghs;
	}

	private void abrirVentaAbono() {

		VentanaAbono va = new VentanaAbono(this);
		this.setVisible(false);
		va.setVisible(true);
	}

	private void abrirVentanaAñadirMantenimiento() {
		VentanaAsignarMantenimiento vAsignarMantenimiento = new VentanaAsignarMantenimiento(this);
		this.setVisible(false);
		vAsignarMantenimiento.setVisible(true);
	}

	private void gestionPlanificarPartido() {
		VentanaPlanificarPartido vpp = new VentanaPlanificarPartido(this);
		vpp.setVisible(true);
		this.setVisible(false);
	}

	public PlanificarPartidoService getPlanificarPartidoService() {
		return this.pps;
	}

	private void abrirVentanaIdentificacion() {

		VentanaIdentificacion vi = new VentanaIdentificacion(this);

		this.setVisible(false);
		vi.setVisible(true);

	}

	private void abrirVentanaCompraCampana() {
		VentanaCompraCampana ventana = new VentanaCompraCampana(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaPortalAccionista() {
		VentanaPortalAccionista vpa = new VentanaPortalAccionista(this);
		this.setVisible(false);
		vpa.setVisible(true);
	}

	private void abrirVentanaCompraAcciones() {
		VentanaCompraAcciones vca = new VentanaCompraAcciones(this);
		this.setVisible(false);
		vca.setVisible(true);
	}

	protected void gestionRegistrarEntrenamiento() {
		VentanaCrearEntrenamiento ces2 = new VentanaCrearEntrenamiento(this);
		this.setVisible(false);
		ces2.setVisible(true);
	}

	public CrearEntrenamientoService getCrearEntrenamientoService() {
		return this.crearEntrenoService;
	}

	private JPanel getPanelUser() {

		if (panelUser == null) {

			panelUser = new JPanel();
			panelUser.setBorder(new TitledBorder(null, "Datos:", TitledBorder.LEADING, TitledBorder.TOP, null, null));

	        panelUser.setLayout(new GridLayout(2, 1, 5, 5));

	        JPanel panelDni = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        panelDni.add(getLbName());
	        panelDni.add(getTfName());

	        JPanel panelPosicion = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        panelPosicion.add(getLbRol());
	        panelPosicion.add(getTfRol());

	        panelUser.add(panelDni);
	        panelUser.add(panelPosicion);

		}
		return panelUser;
	}

	private JLabel getLbName() {
		if (lbName == null) {
			lbName = new JLabel("DNI: ");
		}
		return lbName;
	}

	private JTextField getTfName() {
		if (tfName == null) {
			tfName = new JTextField();
			tfName.setForeground(new Color(255, 0, 0));
			tfName.setEditable(false);
		}
		return tfName;
	}

	private JLabel getLbRol() {
		if (lbRol == null) {
			lbRol = new JLabel("Posición: ");
		}
		return lbRol;
	}

	private JTextField getTfRol() {
		if (tfRol == null) {

			tfRol = new JTextField();
			tfRol.setForeground(new Color(255, 0, 0));
			tfRol.setEditable(false);
		}
		return tfRol;
	}

	private JPanel getPanelOpciones() {
		if (panelOpciones == null) {
			panelOpciones = new JPanel();
			panelOpciones
					.setBorder(new TitledBorder(null, "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelOpciones.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
		}
		return panelOpciones;
	}

	private void actualizarAccionesDisponibles() {
		panelOpciones.removeAll();

		if (usuario.tienePosicion(Posicion.MEDICO)) {
			añadirBoton("Consultar horarios", this::abrirVentanaHorarioSemanal);
		}

		if (usuario.tienePosicion(Posicion.ENTRENADOR)) {
			añadirBoton("Registrar entrenamiento", this::gestionRegistrarEntrenamiento);
			añadirBoton("Crear franjas", this::abrirVentanaCrearFranjas);
		}

		if (usuario.tienePosicion(Posicion.GERENTE)) {
			añadirBoton("Añadir equipo", this::abrirVentanaAñadirEquipo);
			añadirBoton("Alta de empleado", this::abrirVentanaAltaEmpleado);
			añadirBoton("Gestionar empleados", this::abrirVentanaGestionEmpleados);
		}

		if (usuario.esAccionista()) {
			añadirBoton("Portal accionista", this::abrirVentanaPortalAccionista);
			añadirBoton("Comprar acciones", this::abrirVentanaCompraAcciones);
			añadirBoton("Comprar acciones de campaña", this::abrirVentanaCompraCampana);
		}

		panelOpciones.revalidate();
		panelOpciones.repaint();
	}

	private void añadirBoton(String texto, Runnable accion) {

		JButton boton = new JButton(texto);
		boton.addActionListener(e -> accion.run());

		panelOpciones.add(boton);
	}
	private JPanel getPanelSalida() {
		if (panelSalida == null) {
			panelSalida = new JPanel();
			panelSalida.setLayout(new BorderLayout(0, 0));
		}
		return panelSalida;
	}
}
