package giis.demo.model.email;

import java.util.regex.Pattern;

public class ValidadorEmail {
    
    private static final Pattern FORMATO = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private ValidadorEmail() {
    }

    public static boolean esValido(String email) {
        return email != null && FORMATO.matcher(email.trim()).matches();
    }

}
