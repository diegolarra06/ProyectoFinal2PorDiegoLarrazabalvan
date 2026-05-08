package com.daw.adoptauncompanero.dtos;

import java.time.LocalDateTime;

public class CitaDTO {

	private Integer idCita;
	private LocalDateTime fechaCita;
	private String observaciones;
	private Integer idSolicitud;
	private String nombreUsuario;
	private String nombreAnimal;

	public CitaDTO() {
	}

	public CitaDTO(Integer idCita, LocalDateTime fechaCita, String observaciones, Integer idSolicitud,
			String nombreUsuario, String nombreAnimal) {
		this.idCita = idCita;
		this.fechaCita = fechaCita;
		this.observaciones = observaciones;
		this.idSolicitud = idSolicitud;
		this.nombreUsuario = nombreUsuario;
		this.nombreAnimal = nombreAnimal;
	}

	public Integer getIdCita() {
		return idCita;
	}

	public void setIdCita(Integer idCita) {
		this.idCita = idCita;
	}

	public LocalDateTime getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(LocalDateTime fechaCita) {
		this.fechaCita = fechaCita;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Integer idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombreAnimal() {
		return nombreAnimal;
	}

	public void setNombreAnimal(String nombreAnimal) {
		this.nombreAnimal = nombreAnimal;
	}
}
