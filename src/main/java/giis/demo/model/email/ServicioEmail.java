package giis.demo.model.email;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import giis.demo.exceptions.EmailException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class ServicioEmail {

    public static final String FICHERO_CONFIGURACION = "email.properties";

    public void enviar(String destinatario, String asunto, String cuerpo) throws EmailException {
        Properties cfg = cargarConfiguracion();
        final String usuario = cfg.getProperty("email.usuario");
        final String clave = cfg.getProperty("email.clave");
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank())
            throw new EmailException("Faltan email.usuario o email.clave en " + FICHERO_CONFIGURACION);

        Session sesion = Session.getInstance(cfg, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, clave);
            }
        });
        try {
            MimeMessage mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(usuario, cfg.getProperty("email.remitente", "Tienda del club"), "UTF-8"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario.trim(), false));
            mensaje.setSubject(asunto, "UTF-8");
            mensaje.setText(cuerpo, "UTF-8");
            Transport.send(mensaje);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("No se pudo enviar el email a " + destinatario, e);
        }
    }

    private Properties cargarConfiguracion() throws EmailException {
        Properties cfg = new Properties();
        try (InputStream in = new FileInputStream(FICHERO_CONFIGURACION)) {
            cfg.load(in);
        } catch (IOException e) {
            throw new EmailException("No se encuentra o no se puede leer " + FICHERO_CONFIGURACION, e);
        }
        return cfg;
    }
}