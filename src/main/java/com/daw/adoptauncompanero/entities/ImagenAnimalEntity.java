package com.daw.adoptauncompanero.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "imagenes_animales")
public class ImagenAnimalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_imagen")
	private Integer idImagen;

	@Column(name = "url_imagen", nullable = false)
	private String urlImagen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_animal", nullable = false)
	private AnimalEntity animal;

	public ImagenAnimalEntity() {
	}

	public ImagenAnimalEntity(Integer idImagen, String urlImagen, AnimalEntity animal) {
		this.idImagen = idImagen;
		this.urlImagen = urlImagen;
		this.animal = animal;
	}

	public Integer getIdImagen() {
		return idImagen;
	}

	public void setIdImagen(Integer idImagen) {
		this.idImagen = idImagen;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public AnimalEntity getAnimal() {
		return animal;
	}

	public void setAnimal(AnimalEntity animal) {
		this.animal = animal;
	}
}
