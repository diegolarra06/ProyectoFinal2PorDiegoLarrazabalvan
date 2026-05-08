package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;


@Entity
@Table(name = "notificaciones")
public class NotificacionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_notificacion")
	private Integer idNotificacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioEntity usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_solicitud")
	private SolicitudAdopcionEntity solicitud;

	@Column(name = "tipo")
	private String tipo; // EMAIL

	@Column(name = "asunto", nullable = false)
	private String asunto;

	@Column(name = "mensaje", columnDefinition = "TEXT", nullable = false)
	private String mensaje;

	@Column(name = "enviado")
	private Boolean enviado;

	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_envio")
	private LocalDateTime fechaEnvio;

	public NotificacionEntity() {
	}

	public NotificacionEntity(UsuarioEntity usuario, SolicitudAdopcionEntity solicitud, String asunto, String mensaje) {
		this.usuario = usuario;
		this.solicitud = solicitud;
		this.tipo = "EMAIL";
		this.asunto = asunto;
		this.mensaje = mensaje;
		this.enviado = false;
		this.fechaCreacion = LocalDateTime.now();
	}

	public Integer getIdNotificacion() {
		return idNotificacion;
	}

	public void setIdNotificacion(Integer idNotificacion) {
		this.idNotificacion = idNotificacion;
	}

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}

	public SolicitudAdopcionEntity getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudAdopcionEntity solicitud) {
		this.solicitud = solicitud;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Boolean getEnviado() {
		return enviado;
	}

	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
}
