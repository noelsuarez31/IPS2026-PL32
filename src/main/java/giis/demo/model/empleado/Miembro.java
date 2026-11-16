package giis.demo.model.empleado;

import giis.demo.exceptions.EmployeeException;
public abstract class Miembro {

    private String usuario;
    private String contrasena;
    private String dni;

    public Miembro(String usuario, String contrasena, String dni) {

        if (usuario == null || usuario.isBlank()) {
            throw new EmployeeException("usuario no puede ser null ni blanco");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new EmployeeException("contrasena no puede ser null ni blanco");
        }

        if (dni == null || dni.isBlank()) {
            throw new EmployeeException("dni no puede ser null ni blanco");
        }

        this.usuario = usuario;
        this.contrasena = contrasena;
        this.dni = dni;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getDni() {
        return dni;
    }
}