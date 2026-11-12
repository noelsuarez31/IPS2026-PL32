package giis.demo.model.login;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import giis.demo.util.UnexpectedException;

public class PasswordHasher {

	private static final int ITERACIONES = 120000;
	private static final int LONGITUD_BITS = 256;
	private static final int LONGITUD_SALT_BYTES = 16;

	/**
	 * Genera una sal aleatoria
	 * 
	 * @return la sal codificada en Base64
	 */
	public static String generarSalt() {
		byte[] salt = new byte[LONGITUD_SALT_BYTES];
		new SecureRandom().nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Calcula el hash de una contraseña con la sal dada
	 * 
	 * @param contrasena  contraseña en claro
	 * @param saltBase64  sal en Base64
	 * @return el hash codificado en Base64
	 */
	public static String hash(String contrasena, String saltBase64) {
		try {
			byte[] salt = Base64.getDecoder().decode(saltBase64);
			KeySpec spec = new PBEKeySpec(contrasena.toCharArray(), salt, ITERACIONES, LONGITUD_BITS);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * Comprueba si una contraseña en claro corresponde al hash guardado
	 * 
	 * @return true si coincide, false en caso contrario
	 */
	public static boolean verificar(String contrasena, String saltBase64, String hashGuardado) {
		return hash(contrasena, saltBase64).equals(hashGuardado);
	}
	
}
