package giis.demo.model.empleado;

public enum Posicion {
	JUGADOR, ENTRENADOR, GERENTE, EMPLEADO_DE_TIENDA, VENDEDOR_DE_ENTRADAS,
		JARDINERIA, COCINA, DIRECTOR_DE_COMUNICACIONES, ENCARGADO_DE_TIENDA,
		GESTOR_DE_INSTALACIONES, TECNICO_ADICIONAL;
	
	@Override
    public String toString() {
        // Formateo para el combobox de la vista
        String formateado = name().replace('_', ' ').toLowerCase();
        return formateado.substring(0, 1).toUpperCase() + formateado.substring(1);
    }
}
