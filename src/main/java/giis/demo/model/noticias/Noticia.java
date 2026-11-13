package giis.demo.model.noticias;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Noticia {

	private int idNoticia;
	private String titular;
	private String subtitulo;
	private LocalDate fecha;
	private String autor;
	private String cuerpo;
	private List<String> imagenes = new ArrayList<>();

	public Noticia(String titular, String subtitulo, LocalDate fecha, String autor, String cuerpo,
			List<String> imagenes) {
		this.titular = titular;
		this.subtitulo = subtitulo;
		this.fecha = fecha;
		this.autor = autor;
		this.cuerpo = cuerpo;
		if (imagenes != null) {
			this.imagenes = new ArrayList<>(imagenes);
		}
	}

	public Noticia(int idNoticia, String titular, String subtitulo, LocalDate fecha, String autor, String cuerpo,
			List<String> imagenes) {
		this(titular, subtitulo, fecha, autor, cuerpo, imagenes);
		this.idNoticia = idNoticia;
	}

	public int getIdNoticia() {
		return idNoticia;
	}

	public void setIdNoticia(int idNoticia) {
		this.idNoticia = idNoticia;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public List<String> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<String> imagenes) {
		this.imagenes = imagenes != null ? new ArrayList<>(imagenes) : new ArrayList<>();
	}

	public void añadirImagen(String ruta) {
		if (ruta != null && !ruta.isBlank()) {
			this.imagenes.add(ruta);
		}
	}
}
