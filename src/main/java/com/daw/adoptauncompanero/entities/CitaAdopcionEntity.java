package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "citas_adopcion")
public class CitaAdopcionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cita")
	private Integer idCita;

	@Column(name = "fecha_cita", nullable = false)
	private LocalDateTime fechaCita;

	@Column(name = "observaciones", columnDefinition = "TEXT")
	private String observaciones;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_solicitud", nullable = false)
	private SolicitudAdopcionEntity solicitud;

	public CitaAdopcionEntity() {
	}

	public CitaAdopcionEntity(Integer idCita, LocalDateTime fechaCita, String observaciones,
			SolicitudAdopcionEntity solicitud) {
		this.idCita = idCita;
		this.fechaCita = fechaCita;
		this.observaciones = observaciones;
		this.solicitud = solicitud;
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

	public SolicitudAdopcionEntity getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudAdopcionEntity solicitud) {
		this.solicitud = solicitud;
	}
}