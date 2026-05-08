package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.dtos.NoticiaDTO;
import com.daw.adoptauncompanero.entities.NoticiaEntity;

public interface NoticiaService {

    List<NoticiaDTO> listarPublicadas();
    List<NoticiaDTO> listarTodas();
    List<NoticiaDTO> listarDestacadas(int cantidad);
    NoticiaEntity obtenerPorId(Integer idNoticia);

    Integer crearNoticia(String titulo, String subtitulo, String contenido,
                         String autor, Boolean publicada);
    Integer actualizarNoticia(Integer idNoticia, String titulo, String subtitulo,
                              String contenido, String autor, Boolean publicada);
    Integer borrarNoticia(Integer idNoticia);

    Integer subirImagenNoticia(Integer idNoticia, MultipartFile archivo);
}