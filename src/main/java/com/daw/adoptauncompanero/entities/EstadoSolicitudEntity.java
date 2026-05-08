package com.daw.adoptauncompanero.entities;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "estados_solicitud")
public class EstadoSolicitudEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado")
	private Integer idEstado;

	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;

	@OneToMany(mappedBy = "estado")
	private List<SolicitudAdopcionEntity> solicitudes;

	public EstadoSolicitudEntity() {
	}

	public EstadoSolicitudEntity(Integer idEstado, String nombre) {
		this.idEstado = idEstado;
		this.nombre = nombre;
	}

	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<SolicitudAdopcionEntity> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<SolicitudAdopcionEntity> solicitudes) {
		this.solicitudes = solicitudes;
	}
}