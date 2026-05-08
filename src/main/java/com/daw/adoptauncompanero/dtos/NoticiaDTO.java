package com.daw.adoptauncompanero.dtos;

import java.time.LocalDateTime;


public class NoticiaDTO {

	private Integer idNoticia;
	private String titulo;
	private String subtitulo;
	private String contenido;
	private String imagen;
	private String autor;
	private LocalDateTime fechaPublicacion;
	private Boolean publicada;

	public NoticiaDTO() {
	}

	public NoticiaDTO(Integer idNoticia, String titulo, String subtitulo, String contenido, String imagen, String autor,
			LocalDateTime fechaPublicacion, Boolean publicada) {
		this.idNoticia = idNoticia;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.contenido = contenido;
		this.imagen = imagen;
		this.autor = autor;
		this.fechaPublicacion = fechaPublicacion;
		this.publicada = publicada;
	}

	// Getters y setters
	public Integer getIdNoticia() {
		return idNoticia;
	}

	public void setIdNoticia(Integer idNoticia) {
		this.idNoticia = idNoticia;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Boolean getPublicada() {
		return publicada;
	}

	public void setPublicada(Boolean publicada) {
		this.publicada = publicada;
	}
}