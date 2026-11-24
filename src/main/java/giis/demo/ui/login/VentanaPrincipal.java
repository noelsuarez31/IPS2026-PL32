package giis.demo.ui.login;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import giis.demo.model.informe.GestionInformeFinanciero;
import giis.demo.model.log.GestionLogSesion;
import giis.demo.model.noticias.AñadirNoticiaService;
import giis.demo.model.noticias.VisualizarNoticiasService;
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
import giis.demo.ui.informe.VentanaInformeFinanciero;
import giis.demo.ui.log.VentanaLogSesion;
import giis.demo.ui.mantenimiento.VentanaAsignarMantenimiento;
import giis.demo.ui.medico.VentanaVisualizacionLesiones;
import giis.demo.ui.noticias.VentanaAñadirNoticia;
import giis.demo.ui.noticias.VentanaVisualizarNoticias;
import giis.demo.ui.reserva.VentanaReserva;
import giis.demo.ui.tienda.VentanaTienda;

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

	private JPanel panelOpciones; // antes JScrollPane
	private CardLayout cardLayout;
	private JPanel panelBotones;
	private JPanel panelSubmenu;
	private JLabel lbTituloSubmenu;

	private static final String CARD_PRINCIPAL = "PRINCIPAL";
	private static final String CARD_SUBMENU = "SUBMENU";

	private UsuarioAutenticado usuario;
	private VentanaLogin vl;

	private JPanel panelSalida;
	private JPanel panelCerrarSesin;
	private JButton btLogOut;

	private AñadirNoticiaService añadirNoticiaService = new AñadirNoticiaService();
	private VisualizarNoticiasService visualizarNoticiaService = new VisualizarNoticiasService();

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(VentanaLogin vl) {

		this.vl = vl;
		this.usuario = vl.getUsuarioAutenticado();

		setTitle("Ventana Inicial");
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

		tfName.setText(usuario.getName());

		String posiciones = usuario.getPosiciones().toString();

		if (usuario.esAccionista()) {
			posiciones = "ACCIONISTA";
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

		GestionLogSesion.registrarAccion("Ventana Campañas de Acciones");

		VentanaCampanasAcciones ventana = new VentanaCampanasAcciones(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaHistorialVentas() {

		GestionLogSesion.registrarAccion("Ventana Historial de Ventas");

		VentanaHistorialVentas ventana = new VentanaHistorialVentas(this, new HistorialVentas());
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaAltaEmpleado() {

		GestionLogSesion.registrarAccion("Ventana Alta de Empleado");

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

		GestionLogSesion.registrarAccion("Ventana Gestión de Empleados");

		VentanaGestionEmpleados ventana = new VentanaGestionEmpleados(this, gestorEmpleados);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	/**
	 * Abre la ventana para vender las entradas
	 * 
	 * @throws SQLException
	 */
	private void abrirVentanaVentaEntradas() {

		VentanaVentaEntradas vve;

		try {
			vve = new VentanaVentaEntradas(this);

			this.setVisible(false);
			vve.setVisible(true);

		} catch (SQLException e) {

			GestionLogSesion.registrarAccion("Ventana Venta de Entradas");

			JOptionPane.showMessageDialog(this,
					"No se ha podido abrir la ventana de venta de entradas.\n" + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private class GestionaTienda implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			abrirVentanaTienda();
		}

	}

	public void abrirVentanaTienda() {
		try {

			GestionLogSesion.registrarAccion("Ventana Tienda");

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

		GestionLogSesion.registrarAccion("Ventana Añadir Equipo");

		VentanaAñadirEquipo vAñadirEquipo = new VentanaAñadirEquipo(this);
		this.setVisible(false);
		vAñadirEquipo.setVisible(true);

	}

	private void abrirVentanaAñadirHorarioPeriodico() {

		GestionLogSesion.registrarAccion("Ventana Horario Periódico");

		VentanaHorarioPeriodico vHorarioPeriodico = new VentanaHorarioPeriodico(this);
		this.setVisible(false);
		vHorarioPeriodico.setVisible(true);

	}

	private void abrirVentanaAñadirHorarioEspecifico() {

		GestionLogSesion.registrarAccion("Ventana Horario Específico");

		VentanaHorarioEspecifico vHorarioEspecifico = new VentanaHorarioEspecifico(this);
		this.setVisible(false);
		vHorarioEspecifico.setVisible(true);
	}

	public GestionHorariosEspecificos getGestionHorariosEspecificos() {

		return this.ghe;
	}

	private void abrirVentanaCrearFranjas() {

		GestionLogSesion.registrarAccion("Ventana Crear Franjas");

		VentanaCrearFranjas vFranjas = new VentanaCrearFranjas(this);
		this.setVisible(false);
		vFranjas.setVisible(true);
	}
	
	private void abrirVentanaLesiones() {

		GestionLogSesion.registrarAccion("Ventana Visualización de lesiones");

		VentanaVisualizacionLesiones vLesiones = new VentanaVisualizacionLesiones(this);
		this.setVisible(false);
		vLesiones.setVisible(true);
	}

	public CrearFranjaService getCrearFranjaService() {
		return this.cfs;
	}

	/**
	 * Abre la ventana para realizar la ventana de la reserva
	 * 
	 * @throws SQLException
	 */
	private void abrirVentanaReserva() {

		GestionLogSesion.registrarAccion("Ventana Reserva de Instalaciones");

		VentanaReserva vr;

		try {

			vr = new VentanaReserva(this);

			this.setVisible(false);
			vr.setVisible(true);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this,
					"No se ha podido abrir la ventana de venta de entradas.\n" + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private void abrirVentanaCrearEntrevistas() {

		GestionLogSesion.registrarAccion("Ventana Asignar Entrevistas");

		VentanaCrearEntrevistas vCrearEntrev = new VentanaCrearEntrevistas(this);
		this.setVisible(false);
		vCrearEntrev.setVisible(true);
	}

	public CrearEntrevistaService getCrearEntrevistaService() {
		return this.ces;
	}

	private void abrirVentanaHorarioSemanal() {

		GestionLogSesion.registrarAccion("Ventana Horario Semanal");

		VentanaHorarioSemanal vHorarioSemanal = new VentanaHorarioSemanal(this);
		this.setVisible(false);
		vHorarioSemanal.setVisible(true);
	}

	public GestionHorarioSemanal getGestionHorarioSemanal() {
		return this.ghs;
	}

	private void abrirVentaAbono() {

		GestionLogSesion.registrarAccion("Ventana Comprar Abono");

		VentanaAbono va = new VentanaAbono(this);
		this.setVisible(false);
		va.setVisible(true);
	}

	private void abrirVentanaAñadirMantenimiento() {

		GestionLogSesion.registrarAccion("Ventana Asignar Mantenimiento");

		VentanaAsignarMantenimiento vAsignarMantenimiento = new VentanaAsignarMantenimiento(this);
		this.setVisible(false);
		vAsignarMantenimiento.setVisible(true);
	}

	private void gestionPlanificarPartido() {

		GestionLogSesion.registrarAccion("Ventana Planificar Partido");

		VentanaPlanificarPartido vpp = new VentanaPlanificarPartido(this);
		vpp.setVisible(true);
		this.setVisible(false);
	}

	public PlanificarPartidoService getPlanificarPartidoService() {
		return this.pps;
	}

	private void abrirVentanaIdentificacion() {

		GestionLogSesion.registrarAccion("Ventana Venta de Entradas Abonados");
		VentanaIdentificacion vi = new VentanaIdentificacion(this);

		this.setVisible(false);
		vi.setVisible(true);

	}

	private void abrirVentanaCompraCampana() {

		GestionLogSesion.registrarAccion("Ventana Comprar Acciones de Campaña");
		VentanaCompraCampana ventana = new VentanaCompraCampana(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaPortalAccionista() {

		GestionLogSesion.registrarAccion("Ventana Portal del Accionista");
		VentanaPortalAccionista vpa = new VentanaPortalAccionista(this);

		this.setVisible(false);
		vpa.setVisible(true);
	}

	private void abrirVentanaCompraAcciones() {

		GestionLogSesion.registrarAccion("Ventana Comprar Acciones");
		VentanaCompraAcciones vca = new VentanaCompraAcciones(this);

		this.setVisible(false);
		vca.setVisible(true);
	}

	protected void gestionRegistrarEntrenamiento() {

		GestionLogSesion.registrarAccion("Ventana Registrar Entrenamiento");
		VentanaCrearEntrenamiento ces2 = new VentanaCrearEntrenamiento(this);

		this.setVisible(false);
		ces2.setVisible(true);
	}

	private void abrirVentanaInformeFinanciero() {

		GestionLogSesion.registrarAccion("Ventana Informe Financiero");
		VentanaInformeFinanciero ventana = new VentanaInformeFinanciero(this, new GestionInformeFinanciero());
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaAñadirNoticia() {

		GestionLogSesion.registrarAccion("Ventana Añadir Noticia");
		VentanaAñadirNoticia ventana = new VentanaAñadirNoticia(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}
	
	private void abrirVentanaVisualizarNoticia() {
		GestionLogSesion.registrarAccion("Ventana Visualizar Noticia");
		VentanaVisualizarNoticias ventana = new VentanaVisualizarNoticias(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	private void abrirVentanaLogSesion() {

		GestionLogSesion.registrarAccion("Ventana Consultar Log de la Sesión");
		VentanaLogSesion ventana = new VentanaLogSesion(this);
		this.setVisible(false);
		ventana.setVisible(true);
	}

	public AñadirNoticiaService getAñadirNoticiaService() {
		return añadirNoticiaService;
	}
	
	public VisualizarNoticiasService getVisualizarNoticiasService() {
		return this.visualizarNoticiaService;
	}

	public CrearEntrenamientoService getCrearEntrenamientoService() {
		return this.crearEntrenoService;
	}
	private JPanel getPanelUser() {
		if (panelUser == null) {
			panelUser = new JPanel(new GridLayout(2, 1, 5, 10));
			panelUser.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(null, "Datos:", TitledBorder.LEADING, TitledBorder.TOP, null, null),
					new EmptyBorder(8, 8, 8, 8)));

			JPanel panelNombre = new JPanel(new BorderLayout(5, 0));
			panelNombre.add(getLbName(), BorderLayout.WEST);
			panelNombre.add(getTfName(), BorderLayout.CENTER);

			JPanel panelPosicion = new JPanel(new BorderLayout(5, 0));
			panelPosicion.add(getLbRol(), BorderLayout.WEST);
			panelPosicion.add(getTfRol(), BorderLayout.CENTER);

			panelUser.add(panelNombre);
			panelUser.add(panelPosicion);

			panelUser.setPreferredSize(new Dimension(0, 120));
		}
		return panelUser;
	}

	private JTextField getTfName() {
		if (tfName == null) {
			tfName = new JTextField();
			tfName.setForeground(Color.RED);
			tfName.setEditable(false);
		}
		return tfName;
	}

	private JTextField getTfRol() {
		if (tfRol == null) {
			tfRol = new JTextField();
			tfRol.setForeground(Color.RED);
			tfRol.setEditable(false);
		}
		return tfRol;
	}
	private JLabel getLbName() {
		if (lbName == null) {
			lbName = new JLabel("Nombre: ");
		}
		return lbName;
	}


	private JLabel getLbRol() {
		if (lbRol == null) {
			lbRol = new JLabel("Posición: ");
		}
		return lbRol;
	}
	
	private JPanel getPanelOpciones() {
		if (panelOpciones == null) {
			cardLayout = new CardLayout();
			panelOpciones = new JPanel(cardLayout);
			panelOpciones.setBorder(BorderFactory.createTitledBorder("Acciones"));

			panelBotones = new JPanel();
			panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
			panelBotones.setBorder(new EmptyBorder(15, 15, 15, 15));
			JScrollPane sp1 = new JScrollPane(panelBotones);
			sp1.setBorder(null);
			sp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JPanel carta2 = new JPanel(new BorderLayout(0, 5));

			JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JButton btVolver = new JButton("← Volver");
			btVolver.addActionListener(e -> cardLayout.show(panelOpciones, CARD_PRINCIPAL));
			lbTituloSubmenu = new JLabel();
			cabecera.add(btVolver);
			cabecera.add(lbTituloSubmenu);
			carta2.add(cabecera, BorderLayout.NORTH);

			panelSubmenu = new JPanel();
			panelSubmenu.setLayout(new BoxLayout(panelSubmenu, BoxLayout.Y_AXIS));
			panelSubmenu.setBorder(new EmptyBorder(15, 15, 15, 15));
			JScrollPane sp2 = new JScrollPane(panelSubmenu);
			sp2.setBorder(null);
			sp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			carta2.add(sp2, BorderLayout.CENTER);

			panelOpciones.add(sp1, CARD_PRINCIPAL);
			panelOpciones.add(carta2, CARD_SUBMENU);
		}
		return panelOpciones;
	}

	private void actualizarAccionesDisponibles() {
		
		panelBotones.removeAll();
		cardLayout.show(panelOpciones, CARD_PRINCIPAL);
		
		if (usuario.esEmpleado() ) {
			//AÑADIR LO Q PUEDA VER TODO EL MUNDO
			añadirBoton("Panel de noticias", this::abrirVentanaVisualizarNoticia);
		}
		
		if(usuario.esEmpleadoNoDeportivo()) {
			añadirBoton("Consultar horarios", this::abrirVentanaHorarioSemanal);
		}

		if (usuario.esAccionista()) {
			añadirBoton("Portal accionista", this::abrirVentanaPortalAccionista);
			añadirBoton("Comprar acciones", this::abrirVentanaCompraAcciones);
			añadirBoton("Comprar acciones de campaña", this::abrirVentanaCompraCampana);
		}

		if (usuario.tienePosicion(Posicion.ENTRENADOR)) {
			añadirBoton("Registrar entrenamiento", this::gestionRegistrarEntrenamiento);
			añadirBoton("Crear franjas", this::abrirVentanaCrearFranjas);
		}
		
		if (usuario.tienePosicion(Posicion.MEDICO)) {
			añadirBoton("Visualizar lesiones", this::abrirVentanaLesiones);
		}

		if (usuario.tienePosicion(Posicion.GERENTE)) {

			// EQUIPO
			añadirBoton("Equipo", this::abrirVentanaEquipo);
			// HORARIOS
			añadirBoton("Horarios", this::abrirVentanaHorario);
			// EMPLEADO
			añadirBoton("Empleado", this::abrirVentanaEmpleado);
			// FINANZAS
			añadirBoton("Finanzas", this::abrirVentanaFinanzas);
			//LOG
			añadirBoton("Consultar log de sesión", this::abrirVentanaLogSesion);
		}

		if (usuario.tienePosicion(Posicion.DIRECTOR_DE_COMUNICACIONES)) {
			añadirBoton("Asignar entrevistas", this::abrirVentanaCrearEntrevistas);
			añadirBoton("Añadir noticia", this::abrirVentanaAñadirNoticia);
		}

		if (usuario.tienePosicion(Posicion.GESTOR_DE_INSTALACIONES)) {
			añadirBoton("Reserva de instalaciones", this::abrirVentanaReserva);
		}

		if (usuario.tienePosicion(Posicion.EMPLEADO_DE_TIENDA)) {
			añadirBoton("Realizar compra", this::abrirVentanaTienda);
		}

		if (usuario.tienePosicion(Posicion.VENDEDOR_DE_ENTRADAS)) {
			añadirBoton("Compra de abonos", this::abrirVentaAbono);
			añadirBoton("Venta de entradas", this::abrirVentanaVentaEntradas);
			añadirBoton("Venta de entradas abonados", this::abrirVentanaIdentificacion);
		}

		panelBotones.revalidate();
		panelBotones.repaint();

	}

	private void añadirBoton(String texto, Runnable accion) {
		añadirBoton(panelBotones, texto, accion);
	}

	private void añadirBoton(JPanel destino, String texto, Runnable accion) {
		JButton boton = new JButton(texto);
		boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
		boton.setPreferredSize(new Dimension(500, 45));
		boton.setAlignmentX(CENTER_ALIGNMENT);
		boton.addActionListener(e -> accion.run());

		destino.add(boton);
		destino.add(Box.createVerticalStrut(10));
	}
	

	private JPanel getPanelSalida() {
		if (panelSalida == null) {
			panelSalida = new JPanel();
			panelSalida.setLayout(new BorderLayout(0, 0));
			panelSalida.add(getPanelCerrarSesin(), BorderLayout.EAST);
		}
		return panelSalida;
	}

	private JPanel getPanelCerrarSesin() {
		if (panelCerrarSesin == null) {
			panelCerrarSesin = new JPanel();
			panelCerrarSesin.add(getBtLogOut());
		}
		return panelCerrarSesin;
	}

	private JButton getBtLogOut() {
		if (btLogOut == null) {
			btLogOut = new JButton("Cerrar sesión");
			btLogOut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					cerrarSesion();

				}
			});
			btLogOut.setBackground(new Color(255, 128, 128));
		}
		return btLogOut;
	}

	private void cerrarSesion() {

		vl.setVisible(true);
		vl.reiniciar();

		this.dispose();

	}
	
	private void abrirSubmenu(String titulo, Runnable rellenar) {
		panelSubmenu.removeAll();
		lbTituloSubmenu.setText(titulo);
		rellenar.run();
		panelSubmenu.revalidate();
		panelSubmenu.repaint();
		cardLayout.show(panelOpciones, CARD_SUBMENU);
	}

	private void abrirVentanaEquipo() {
		abrirSubmenu("Equipo", () -> {
			añadirBoton(panelSubmenu, "Añadir equipo", this::abrirVentanaAñadirEquipo);
			añadirBoton(panelSubmenu, "Planificar partido", this::gestionPlanificarPartido);
			añadirBoton(panelSubmenu, "Añadir servicio de mantenimiento", this::abrirVentanaAñadirMantenimiento);
		});
	}

	private void abrirVentanaHorario() {
		abrirSubmenu("Horarios", () -> {
			añadirBoton(panelSubmenu, "Añadir horario periódico", this::abrirVentanaAñadirHorarioPeriodico);
			añadirBoton(panelSubmenu, "Añadir horario específico", this::abrirVentanaAñadirHorarioEspecifico);
		});
	}

	private void abrirVentanaEmpleado() {
		abrirSubmenu("Empleados", () -> {
			añadirBoton(panelSubmenu, "Alta de empleado", this::abrirVentanaAltaEmpleado);
			añadirBoton(panelSubmenu, "Modificar/Eliminar empleado", this::abrirVentanaGestionEmpleados);
		});
	}

	private void abrirVentanaFinanzas() {
		abrirSubmenu("Finanzas", () -> {
			añadirBoton(panelSubmenu, "Historial de ventas", this::abrirVentanaHistorialVentas);
			añadirBoton(panelSubmenu, "Informe financiero", this::abrirVentanaInformeFinanciero);
			añadirBoton(panelSubmenu, "Campañas de acciones", this::abrirVentanaCampanasAcciones);
		});
	}
	
	public UsuarioAutenticado getUsuario() {
		return usuario;
	}
	
}
