package com.daw.adoptauncompanero.dtos;


public class FavoritoDTO {

	private Integer idAnimal;
	private String nombreAnimal;
	private String especie;
	private String estado;
	private String imagenPrincipal;

	public FavoritoDTO() {
	}

	public FavoritoDTO(Integer idAnimal, String nombreAnimal, String especie, String estado, String imagenPrincipal) {
		this.idAnimal = idAnimal;
		this.nombreAnimal = nombreAnimal;
		this.especie = especie;
		this.estado = estado;
		this.imagenPrincipal = imagenPrincipal;
	}

	public Integer getIdAnimal() {
		return idAnimal;
	}

	public void setIdAnimal(Integer idAnimal) {
		this.idAnimal = idAnimal;
	}

	public String getNombreAnimal() {
		return nombreAnimal;
	}

	public void setNombreAnimal(String nombreAnimal) {
		this.nombreAnimal = nombreAnimal;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
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