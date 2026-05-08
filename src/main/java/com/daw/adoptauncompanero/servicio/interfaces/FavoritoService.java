package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import com.daw.adoptauncompanero.dtos.FavoritoDTO;

public interface FavoritoService {

    Integer agregarFavorito(Integer idUsuario, Integer idAnimal);

    Integer quitarFavorito(Integer idUsuario, Integer idAnimal);

    List<FavoritoDTO> listarFavoritosPorUsuario(Integer idUsuario);

    Boolean esFavorito(Integer idUsuario, Integer idAnimal);
}
