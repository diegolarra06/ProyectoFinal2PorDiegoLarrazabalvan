package com.daw.adoptauncompanero.dtos;

public class AnimalDTO {

	private Integer idAnimal;
	private String nombre;
	private String especie;
	private Integer edad;
	private String tamano;
	private String personalidad;
	private String necesidadesEspeciales;
	private String estadoSanitario;
	private String estado; 
	private String imagenPrincipal; 

	public AnimalDTO() {
	}

	public AnimalDTO(Integer idAnimal, String nombre, String especie, Integer edad, String tamano, String personalidad,
			String necesidadesEspeciales, String estadoSanitario, String estado, String imagenPrincipal) {
		this.idAnimal = idAnimal;
		this.nombre = nombre;
		this.especie = especie;
		this.edad = edad;
		this.tamano = tamano;
		this.personalidad = personalidad;
		this.necesidadesEspeciales = necesidadesEspeciales;
		this.estadoSanitario = estadoSanitario;
		this.estado = estado;
		this.imagenPrincipal = imagenPrincipal;
	}

	public Integer getIdAnimal() {
		return idAnimal;
	}

	public void setIdAnimal(Integer idAnimal) {
		this.idAnimal = idAnimal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getTamano() {
		return tamano;
	}

	public void setTamano(String tamano) {
		this.tamano = tamano;
	}

	public String getPersonalidad() {
		return personalidad;
	}

	public void setPersonalidad(String personalidad) {
		this.personalidad = personalidad;
	}

	public String getNecesidadesEspeciales() {
		return necesidadesEspeciales;
	}

	public void setNecesidadesEspeciales(String necesidadesEspeciales) {
		this.necesidadesEspeciales = necesidadesEspeciales;
	}

	public String getEstadoSanitario() {
		return estadoSanitario;
	}

	public void setEstadoSanitario(String estadoSanitario) {
		this.estadoSanitario = estadoSanitario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getImagenPrincipal() {
		return imagenPrincipal;
	}

	public void setImagenPrincipal(String imagenPrincipal) {
		this.imagenPrincipal = imagenPrincipal;
	}
}
