package giis.demo.main;

import giis.demo.exceptions.EmailException;
import giis.demo.model.email.ServicioEmail;

public class ProbarEmail {
    public static void main(String[] args) {
        try {
            new ServicioEmail().enviar("uo306625@uniovi.es", "Prueba del club", "Si lees esto, el envio funciona.");
            System.out.println("Enviado");
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}