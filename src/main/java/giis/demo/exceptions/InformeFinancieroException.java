package giis.demo.exceptions;

public class InformeFinancieroException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InformeFinancieroException(String msg) {
        super(msg);
    }
}