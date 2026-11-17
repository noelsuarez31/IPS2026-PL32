package giis.demo.model.rol;

import java.util.Set;

import giis.demo.model.empleado.Posicion;

public class UsuarioAutenticado {
	
	private String dni;
	private String name;
	
    private Set<Posicion> posiciones;
    private boolean accionista;

    public UsuarioAutenticado(String dni, String name, Set<Posicion> posiciones, boolean accionista) {
        
    	this.dni = dni;
    	this.name = name;
        this.posiciones = posiciones;
        this.accionista = accionista;
    }

    public String getDni() {
        return dni;
    }

    public Set<Posicion> getPosiciones() {
        return posiciones;
    }

    public boolean esAccionista() {
        return accionista;
    }

    public boolean tienePosicion(Posicion posicion) {
        return posiciones.contains(posicion);
    }

    public boolean esEmpleado() {
        return !posiciones.isEmpty();
    }
    
    public boolean esEmpleadoNoDeportivo() {
        return esEmpleado()
                && !tienePosicion(Posicion.JUGADOR)
                && !tienePosicion(Posicion.ENTRENADOR)
                && !tienePosicion(Posicion.TECNICO_ADICIONAL);
    }

	public String getName() {
		return name;
	}
    
    

}
