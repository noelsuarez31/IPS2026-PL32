package giis.demo.model.noticias;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import giis.demo.jdbc.JDBC;

public class AñadirNoticiaService {

	private JDBC jdbc = new JDBC();

	public void añadirNoticia(String titular, String subtitulo, LocalDate fecha, String autor, String cuerpo,
			List<String> imagenes) {
		validarDatosNoticia(titular, fecha, cuerpo);

		Noticia noticia = new Noticia(titular, subtitulo, fecha, autor, cuerpo, imagenes);
		try {
			jdbc.insertarNoticia(noticia);
		} catch (SQLException e) {
			throw new RuntimeException("Error al registrar la noticia en la base de datos: " + e.getMessage(), e);
		}
	}

	public void validarDatosNoticia(String titular, LocalDate fecha, String cuerpo) {
		if (titular == null || titular.trim().isEmpty()) {
			throw new IllegalArgumentException("El titular de la noticia es un campo obligatorio.");
		}
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha de publicación es obligatoria.");
		}
		if (fecha.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("La fecha de publicación no puede ser anterior al día actual.");
		}
		if (cuerpo == null || cuerpo.trim().isEmpty()) {
			throw new IllegalArgumentException("El cuerpo de la noticia es un campo obligatorio.");
		}
	}
}