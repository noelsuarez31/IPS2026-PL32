package giis.demo.model.login;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

	private static final int ITERATIONS  = 3;
    private static final int MEMORY_KB   = 65536;
    private static final int PARALLELISM = 4;

    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    /**
     * Genera el hash de una contraseña (incluye salt interno automáticamente)
     * @return string PHC que contiene salt + hash — guardar directamente en BBDD
     */
    public static String hash(String contrasena) {
        return argon2.hash(ITERATIONS, MEMORY_KB, PARALLELISM, contrasena.toCharArray());
    }
    /**
     * Verifica una contraseña contra el hash guardado
     */
    public static boolean verificar(String contrasena, String hashGuardado) {
        return argon2.verify(hashGuardado, contrasena.toCharArray());
    }
	
}
