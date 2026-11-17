package giis.demo.jdbc.noticias;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import giis.demo.model.noticias.Noticia;

public class NoticiaJdbc {

	private static final String SQL_INSERT_NOTICIA = 
			"INSERT INTO Noticia (titular, subtitulo, fecha, autor, cuerpo) VALUES (?, ?, ?, ?, ?)";

	private static final String SQL_INSERT_IMAGEN = 
			"INSERT INTO ImagenNoticia (id_noticia, ruta_imagen) VALUES (?, ?)";

	public static void insertarNoticia(Connection con, Noticia noticia) throws SQLException {
		boolean autoCommitOriginal = con.getAutoCommit();
		con.setAutoCommit(false);
		try {
			int idNoticiaGenerado;
			try (PreparedStatement pst = con.prepareStatement(SQL_INSERT_NOTICIA, Statement.RETURN_GENERATED_KEYS)) {
				pst.setString(1, noticia.getTitular());

				if (noticia.getSubtitulo() != null && !noticia.getSubtitulo().trim().isEmpty()) {
					pst.setString(2, noticia.getSubtitulo().trim());
				} else {
					pst.setNull(2, Types.VARCHAR);
				}

				pst.setString(3, noticia.getFecha().toString());

				if (noticia.getAutor() != null && !noticia.getAutor().trim().isEmpty()) {
					pst.setString(4, noticia.getAutor().trim());
				} else {
					pst.setNull(4, Types.VARCHAR);
				}

				pst.setString(5, noticia.getCuerpo());

				int filas = pst.executeUpdate();
				if (filas != 1) {
					throw new SQLException("No se pudo insertar la noticia en la base de datos.");
				}

				try (ResultSet rs = pst.getGeneratedKeys()) {
					if (rs.next()) {
						idNoticiaGenerado = rs.getInt(1);
						noticia.setIdNoticia(idNoticiaGenerado);
					} else {
						throw new SQLException("No se pudo obtener el ID generado para la noticia.");
					}
				}
			}

			List<String> imagenes = noticia.getImagenes();
			if (imagenes != null && !imagenes.isEmpty()) {
				try (PreparedStatement pstImg = con.prepareStatement(SQL_INSERT_IMAGEN)) {
					for (String ruta : imagenes) {
						if (ruta != null && !ruta.trim().isEmpty()) {
							pstImg.setInt(1, idNoticiaGenerado);
							pstImg.setString(2, ruta.trim());
							pstImg.executeUpdate();
						}
					}
				}
			}

			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(autoCommitOriginal);
		}
	}
}