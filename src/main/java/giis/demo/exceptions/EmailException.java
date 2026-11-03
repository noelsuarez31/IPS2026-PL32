package giis.demo.exceptions;

// SE COMPRUEBA
public class EmailException extends Exception {
    private static final long serialVersionUID = 1L;

    public EmailException(String mensaje) {
        super(mensaje);
    }

    public EmailException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
