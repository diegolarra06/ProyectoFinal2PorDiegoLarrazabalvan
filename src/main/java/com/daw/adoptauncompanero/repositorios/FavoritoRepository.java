package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.FavoritoDTO;
import com.daw.adoptauncompanero.entities.FavoritoEntity;
import com.daw.adoptauncompanero.entities.FavoritoId;


public interface FavoritoRepository extends JpaRepository<FavoritoEntity, FavoritoId> {

    @Query("""
        SELECT new com.daw.adoptauncompanero.dtos.FavoritoDTO(
            a.idAnimal, a.nombre, a.especie,
            CAST(a.estado AS string),
            (SELECT MIN(i.urlImagen) FROM ImagenAnimalEntity i WHERE i.animal = a)
        )
        FROM FavoritoEntity f
        JOIN f.animal a
        WHERE f.usuario.idUsuario = :idUsuario
        ORDER BY f.fechaFavorito DESC
    """)
    List<FavoritoDTO> listarFavoritosPorUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("""
        SELECT COUNT(f) FROM FavoritoEntity f
        WHERE f.usuario.idUsuario = :idUsuario AND f.animal.idAnimal = :idAnimal
    """)
    Long contarFavorito(@Param("idUsuario") Integer idUsuario, @Param("idAnimal") Integer idAnimal);
}
