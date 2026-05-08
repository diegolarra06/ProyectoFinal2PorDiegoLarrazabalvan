package com.daw.adoptauncompanero.dtos;

import java.time.LocalDateTime;

public class SolicitudDTO {

	private Integer idSolicitud;
	private LocalDateTime fechaSolicitud;
	private String comentarios;
	private Integer idUsuario;
	private String nombreUsuario;
	private String emailUsuario;
	private Integer idAnimal;
	private String nombreAnimal;
	private Integer idEstado;
	private String nombreEstado;

	public SolicitudDTO() {
	}

	public SolicitudDTO(Integer idSolicitud, LocalDateTime fechaSolicitud, String comentarios, Integer idUsuario,
			String nombreUsuario, String emailUsuario, Integer idAnimal, String nombreAnimal, Integer idEstado,
			String nombreEstado) {
		this.idSolicitud = idSolicitud;
		this.fechaSolicitud = fechaSolicitud;
		this.comentarios = comentarios;
		this.idUsuario = idUsuario;
		this.nombreUsuario = nombreUsuario;
		this.emailUsuario = emailUsuario;
		this.idAnimal = idAnimal;
		this.nombreAnimal = nombreAnimal;
		this.idEstado = idEstado;
		this.nombreEstado = nombreEstado;
	}

	public Integer getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Integer idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public LocalDateTime getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Integer getIdAnimal() {
		return idAnimal;
	}

	public void setIdAnimal(Integer idAnimal) {
		this.idAnimal = idAnimal;
	}

	public String getNombreAnimal() {
		return nombreAnimal;
	}

	public void setNombreAnimal(String nombreAnimal) {
		this.nombreAnimal = nombreAnimal;
	}

	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
}
