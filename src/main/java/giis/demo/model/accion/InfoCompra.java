package giis.demo.model.accion;

import giis.demo.model.email.ValidadorEmail;

/**
 * Lo que se le muestra a un comprador identificado: la campaña activa tal y como está en ese
 * momento, quién es y cuántas acciones puede comprar como máximo.
 */
public class InfoCompra {

    private final Campana campana;
    private final String dni;
    private final Accionista accionista; // null si todavia no es accionista
    private final int maximo;
    private final boolean miembroExistente;

    public InfoCompra(Campana campana, String dni, Accionista accionista, int maximo,
    		boolean miembroExistente) {
    	
        this.campana = campana;
        this.dni = dni;
        this.accionista = accionista;
        this.maximo = maximo;
        this.miembroExistente = miembroExistente;
        
    }

    public Campana getCampana() {
        return campana;
    }

    public String getDni() {
        return dni;
    }

    /** null si el comprador todavia no es accionista */
    public Accionista getAccionista() {
        return accionista;
    }

    public boolean esNuevo() {
        return accionista == null;
    }

    /** Maximo de acciones que puede comprar ahora */
    public int getMaximo() {
        return maximo;
    }

    /** Email guardado del accionista si es valido; null si es nuevo o no tiene uno utilizable */
    public String getEmailGuardado() {
        if (accionista != null && ValidadorEmail.esValido(accionista.getEmail()))
            return accionista.getEmail().trim();
        return null;
    }
    
    public boolean tieneMiembro() {
        return miembroExistente;
    }
}