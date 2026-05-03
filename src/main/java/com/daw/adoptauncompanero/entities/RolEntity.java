package com.daw.adoptauncompanero.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//=============================================================
//ENTIDAD ROL (PDF 5 - Seguridad y Roles)
//Mapea la tabla 'roles' que define los roles del sistema:
//CLIENTE (cliente registrado, 2.2.3) y ADMIN (administrador, 2.2.4)
//=============================================================
@Entity
@Table(name = "roles")
public class RolEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Integer idRol;

	@Column(name = "nombre", unique = true, nullable = false)
	private String nombre;

	// Relación inversa: muchos usuarios pueden tener este rol
	// Se configura mediante la tabla intermedia usuario_roles
	@OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
	private Set<UsuarioRolEntity> usuarioRoles;

	public RolEntity() {
	}

	public RolEntity(Integer idRol, String nombre) {
		this.idRol = idRol;
		this.nombre = nombre;
	}

	public Integer getIdRol() {
		return idRol;
	}

	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<UsuarioRolEntity> getUsuarioRoles() {
		return usuarioRoles;
	}

	public void setUsuarioRoles(Set<UsuarioRolEntity> usuarioRoles) {
		this.usuarioRoles = usuarioRoles;
	}
}