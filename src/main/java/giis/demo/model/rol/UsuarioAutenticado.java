package giis.demo.model.rol;

import java.util.Set;

import giis.demo.model.empleado.Posicion;

public class UsuarioAutenticado {
	
	private String dni;
    private Set<Posicion> posiciones;
    private boolean accionista;

    public UsuarioAutenticado(String dni, Set<Posicion> posiciones, boolean accionista) {
        this.dni = dni;
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

}
