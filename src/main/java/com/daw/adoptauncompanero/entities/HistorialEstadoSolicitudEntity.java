package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;


@Entity
@Table(name = "historial_estados_solicitud")
public class HistorialEstadoSolicitudEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_historial")
	private Integer idHistorial;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_solicitud", nullable = false)
	private SolicitudAdopcionEntity solicitud;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_anterior")
	private EstadoSolicitudEntity estadoAnterior;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_nuevo", nullable = false)
	private EstadoSolicitudEntity estadoNuevo;

	@Column(name = "fecha_cambio")
	private LocalDateTime fechaCambio;

	@Column(name = "comentario_admin")
	private String comentarioAdmin;

	public HistorialEstadoSolicitudEntity() {
	}

	public HistorialEstadoSolicitudEntity(SolicitudAdopcionEntity solicitud, EstadoSolicitudEntity estadoAnterior,
			EstadoSolicitudEntity estadoNuevo, LocalDateTime fechaCambio, String comentarioAdmin) {
		this.solicitud = solicitud;
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

	public SolicitudAdopcionEntity getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudAdopcionEntity solicitud) {
		this.solicitud = solicitud;
	}

	public EstadoSolicitudEntity getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(EstadoSolicitudEntity estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public EstadoSolicitudEntity getEstadoNuevo() {
		return estadoNuevo;
	}

	public void setEstadoNuevo(EstadoSolicitudEntity estadoNuevo) {
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
