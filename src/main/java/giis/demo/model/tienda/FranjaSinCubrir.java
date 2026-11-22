package giis.demo.model.tienda;

/**
 * Franja de apertura en la que, en algun momento, trabajan menos vendedores de los necesarios
 */
public class FranjaSinCubrir {
    private final FranjaTienda franja;
    private final int vendedores;
    private final boolean enCurso;

    /**
     * @param vendedores los que hay en el peor momento de la franja
     * @param enCurso    si la franja esta abierta ahora mismo
     */
    public FranjaSinCubrir(FranjaTienda franja, int vendedores, boolean enCurso) {
        this.franja = franja;
        this.vendedores = vendedores;
        this.enCurso = enCurso;
    }

    public FranjaTienda getFranja() {
        return franja;
    }

    public int getVendedores() {
        return vendedores;
    }

    public int getFaltan() {
        return HorarioTienda.MIN_VENDEDORES - vendedores;
    }

    public boolean isEnCurso() {
        return enCurso;
    }
}