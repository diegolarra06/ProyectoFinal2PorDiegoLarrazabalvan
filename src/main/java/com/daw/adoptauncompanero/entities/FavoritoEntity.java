package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

// =============================================================
// ENTIDAD FAVORITO (2.2.3.3 - Guardar como favorito)
// Permite al cliente registrado marcar animales como favoritos
// (2.3.2.4) y consultarlos desde su área personal (2.3.3.1).
// =============================================================
@Entity
@Table(name = "favoritos")
public class FavoritoEntity {

	@EmbeddedId
	private FavoritoId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idUsuario")
	@JoinColumn(name = "id_usuario")
	private UsuarioEntity usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idAnimal")
	@JoinColumn(name = "id_animal")
	private AnimalEntity animal;

	@Column(name = "fecha_favorito")
	private LocalDateTime fechaFavorito;

	public FavoritoEntity() {
	}

	public FavoritoEntity(UsuarioEntity usuario, AnimalEntity animal) {
		this.usuario = usuario;
		this.animal = animal;
		this.id = new FavoritoId(usuario.getIdUsuario(), animal.getIdAnimal());
		this.fechaFavorito = LocalDateTime.now();
	}

	public FavoritoId getId() {
		return id;
	}

	public void setId(FavoritoId id) {
		this.id = id;
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

	public LocalDateTime getFechaFavorito() {
		return fechaFavorito;
	}

	public void setFechaFavorito(LocalDateTime fechaFavorito) {
		this.fechaFavorito = fechaFavorito;
	}
}