package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daw.adoptauncompanero.dtos.NoticiaDTO;
import com.daw.adoptauncompanero.entities.NoticiaEntity;


public interface NoticiaRepository extends JpaRepository<NoticiaEntity, Integer> {

   
    @Query("SELECT new com.daw.adoptauncompanero.dtos.NoticiaDTO(" +
           "n.idNoticia, n.titulo, n.subtitulo, n.contenido, n.imagen, " +
           "n.autor, n.fechaPublicacion, n.publicada) " +
           "FROM NoticiaEntity n " +
           "WHERE n.publicada = true " +
           "ORDER BY n.fechaPublicacion DESC")
    List<NoticiaDTO> listarNoticiasPublicadas();


    @Query("SELECT new com.daw.adoptauncompanero.dtos.NoticiaDTO(" +
           "n.idNoticia, n.titulo, n.subtitulo, n.contenido, n.imagen, " +
           "n.autor, n.fechaPublicacion, n.publicada) " +
           "FROM NoticiaEntity n " +
           "ORDER BY n.fechaPublicacion DESC")
    List<NoticiaDTO> listarTodasNoticias();

    @Query("SELECT new com.daw.adoptauncompanero.dtos.NoticiaDTO(" +
           "n.idNoticia, n.titulo, n.subtitulo, n.contenido, n.imagen, " +
           "n.autor, n.fechaPublicacion, n.publicada) " +
           "FROM NoticiaEntity n " +
           "WHERE n.publicada = true " +
           "ORDER BY n.fechaPublicacion DESC")
    List<NoticiaDTO> listarDestacadas(Pageable pageable);
}