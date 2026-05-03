package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

// =============================================================
// ENTIDAD SOLICITUD DE ADOPCIÓN (2.3.4 Proceso de adopción)
// Funcionalidades cubiertas:
//  - 2.2.3.4 Iniciar solicitud (cliente registrado)
//  - 2.2.4.2 Gestionar solicitudes (admin)
//  - 2.2.4.3 Cambiar estado
//  - 2.4.6 Admin revisa la solicitud
//  - Restricción SQL: un usuario no puede pedir 2 veces el mismo animal
// =============================================================
@Entity
@Table(name = "solicitudes_adopcion")
public class SolicitudAdopcionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_solicitud")
	private Integer idSolicitud;

	@Column(name = "fecha_solicitud")
	private LocalDateTime fechaSolicitud;

	@Column(name = "comentarios", columnDefinition = "TEXT")
	private String comentarios;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioEntity usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_animal", nullable = false)
	private AnimalEntity animal;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_estado", nullable = false)
	private EstadoSolicitudEntity estado;

	// 2.2.5: registro del historial de cambios de estado
	@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
	private List<HistorialEstadoSolicitudEntity> historial;

	// 2.2.4.4 / 2.3.5: gestión de citas con adoptantes
	@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
	private List<CitaAdopcionEntity> citas;

	public SolicitudAdopcionEntity() {
	}

	public SolicitudAdopcionEntity(Integer idSolicitud, LocalDateTime fechaSolicitud, String comentarios,
			UsuarioEntity usuario, AnimalEntity animal, EstadoSolicitudEntity estado) {
		this.idSolicitud = idSolicitud;
		this.fechaSolicitud = fechaSolicitud;
		this.comentarios = comentarios;
		this.usuario = usuario;
		this.animal = animal;
		this.estado = estado;
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

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}

	public AnimalEntity getAnimal() {
		return animal;
	}

	public void setAnimal(AnimalEntity animal) {
		this.animal = animal;
	}

	public EstadoSolicitudEntity getEstado() {
		return estado;
	}

	public void setEstado(EstadoSolicitudEntity estado) {
		this.estado = estado;
	}

	public List<HistorialEstadoSolicitudEntity> getHistorial() {
		return historial;
	}

	public void setHistorial(List<HistorialEstadoSolicitudEntity> historial) {
		this.historial = historial;
	}

	public List<CitaAdopcionEntity> getCitas() {
		return citas;
	}

	public void setCitas(List<CitaAdopcionEntity> citas) {
		this.citas = citas;
	}
}