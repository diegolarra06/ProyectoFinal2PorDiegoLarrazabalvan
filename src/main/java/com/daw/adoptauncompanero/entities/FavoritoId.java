package com.daw.adoptauncompanero.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FavoritoId implements Serializable {

	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "id_animal")
	private Integer idAnimal;

	public FavoritoId() {
	}

	public FavoritoId(Integer idUsuario, Integer idAnimal) {
		this.idUsuario = idUsuario;
		this.idAnimal = idAnimal;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdAnimal() {
		return idAnimal;
	}

	public void setIdAnimal(Integer idAnimal) {
		this.idAnimal = idAnimal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FavoritoId))
			return false;
		FavoritoId that = (FavoritoId) o;
		return Objects.equals(idUsuario, that.idUsuario) && Objects.equals(idAnimal, that.idAnimal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUsuario, idAnimal);
	}
}