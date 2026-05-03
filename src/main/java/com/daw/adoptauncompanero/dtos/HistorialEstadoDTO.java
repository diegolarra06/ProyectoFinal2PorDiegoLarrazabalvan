package com.daw.adoptauncompanero.dtos;

import java.time.LocalDateTime;

public class HistorialEstadoDTO {

	private Integer idHistorial;
	private Integer idSolicitud;
	private String estadoAnterior;
	private String estadoNuevo;
	private LocalDateTime fechaCambio;
	private String comentarioAdmin;

	public HistorialEstadoDTO() {
	}

	public HistorialEstadoDTO(Integer idHistorial, Integer idSolicitud, String estadoAnterior, String estadoNuevo,
			LocalDateTime fechaCambio, String comentarioAdmin) {
		this.idHistorial = idHistorial;
		this.idSolicitud = idSolicitud;
		this.estadoAnterior = estadoAnterior;
		this.estadoNuevo = estadoNuevo;
		this.fechaCambio = fechaCambio;
		this.comentarioAdmin = comentarioAdmin;
	}

	public Integer getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(Integer idHistorial) {
		this.idHistorial = idHistorial;
	}

	public Integer getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Integer idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public String getEstadoNuevo() {
		return estadoNuevo;
	}

	public void setEstadoNuevo(String estadoNuevo) {
		this.estadoNuevo = estadoNuevo;
	}

	public LocalDateTime getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(LocalDateTime fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public String getComentarioAdmin() {
		return comentarioAdmin;
	}

	public void setComentarioAdmin(String comentarioAdmin) {
		this.comentarioAdmin = comentarioAdmin;
	}
}
