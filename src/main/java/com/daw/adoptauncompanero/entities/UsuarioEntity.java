package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import jakarta.persistence.*;

// =============================================================
// ENTIDAD USUARIO (PDF 5 - Seguridad y Roles)
// Representa tanto a clientes registrados (2.2.3) como a
// administradores (2.2.4). El rol se determina por la tabla
// intermedia usuario_roles.
// =============================================================
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	// Email único (2.2.3.1: iniciar sesión)
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	// Contraseña encriptada con BCrypt (PDF 5)
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "fecha_registro")
	private LocalDateTime fechaRegistro;

	// Relación con roles (PDF 5: necesario para Spring Security)
	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<UsuarioRolEntity> usuarioRoles;

	// 2.3.3.4: historial de solicitudes del usuario
	@OneToMany(mappedBy = "usuario")
	private List<SolicitudAdopcionEntity> solicitudes;

	// 2.2.3.3 / 2.3.3: animales favoritos del usuario
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FavoritoEntity> favoritos;

	public UsuarioEntity() {
	}

	public UsuarioEntity(Integer idUsuario, String nombre, String email, String password, String telefono,
			String direccion, LocalDateTime fechaRegistro) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.telefono = telefono;
		this.direccion = direccion;
		this.fechaRegistro = fechaRegistro;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Set<UsuarioRolEntity> getUsuarioRoles() {
		return usuarioRoles;
	}

	public void setUsuarioRoles(Set<UsuarioRolEntity> usuarioRoles) {
		this.usuarioRoles = usuarioRoles;
	}

	public List<SolicitudAdopcionEntity> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<SolicitudAdopcionEntity> solicitudes) {
		this.solicitudes = solicitudes;
	}

	public List<FavoritoEntity> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(List<FavoritoEntity> favoritos) {
		this.favoritos = favoritos;
	}
}