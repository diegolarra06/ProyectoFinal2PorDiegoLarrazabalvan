package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import com.daw.adoptauncompanero.dtos.FavoritoDTO;

public interface FavoritoService {

    // 2.2.3.3 - guardar como favorito
    Integer agregarFavorito(Integer idUsuario, Integer idAnimal);

    // Quitar de favoritos
    Integer quitarFavorito(Integer idUsuario, Integer idAnimal);

    // 2.3.3.1 - listar favoritos del usuario
    List<FavoritoDTO> listarFavoritosPorUsuario(Integer idUsuario);

    Boolean esFavorito(Integer idUsuario, Integer idAnimal);
}
