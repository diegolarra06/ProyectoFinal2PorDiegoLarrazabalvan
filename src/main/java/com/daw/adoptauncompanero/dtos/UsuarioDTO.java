package com.daw.adoptauncompanero.dtos;

//DTO para listado de usuarios (2.2.4.6 control de usuarios) y área personal (2.3.3.1)
public class UsuarioDTO {

 private Integer idUsuario;
 private String nombre;
 private String email;
 private String telefono;
 private String direccion;
 private String rol;

 public UsuarioDTO() {}

 public UsuarioDTO(Integer idUsuario, String nombre, String email, String telefono,
                   String direccion, String rol) {
     this.idUsuario = idUsuario;
     this.nombre = nombre;
     this.email = email;
     this.telefono = telefono;
     this.direccion = direccion;
     this.rol = rol;
 }

 public Integer getIdUsuario() { return idUsuario; }
 public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
 public String getNombre() { return nombre; }
 public void setNombre(String nombre) { this.nombre = nombre; }
 public String getEmail() { return email; }
 public void setEmail(String email) { this.email = email; }
 public String getTelefono() { return telefono; }
 public void setTelefono(String telefono) { this.telefono = telefono; }
 public String getDireccion() { return direccion; }
 public void setDireccion(String direccion) { this.direccion = direccion; }
 public String getRol() { return rol; }
 public void setRol(String rol) { this.rol = rol; }
}
