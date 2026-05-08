package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import com.daw.adoptauncompanero.dtos.HistorialEstadoDTO;
import com.daw.adoptauncompanero.dtos.SolicitudDTO;

public interface SolicitudService {

    Integer iniciarSolicitud(Integer idUsuario, Integer idAnimal, String comentarios);

    List<SolicitudDTO> buscarSolicitudes(Integer id, Integer idUsuario, Integer idAnimal, Integer idEstado);

    List<SolicitudDTO> listarSolicitudesPorUsuario(Integer idUsuario);

    Integer cambiarEstado(Integer idSolicitud, Integer idEstadoNuevo, String comentarioAdmin);

    Integer cancelarSolicitud(Integer idSolicitud, Integer idUsuario);

    List<HistorialEstadoDTO> obtenerHistorial(Integer idSolicitud);
}
