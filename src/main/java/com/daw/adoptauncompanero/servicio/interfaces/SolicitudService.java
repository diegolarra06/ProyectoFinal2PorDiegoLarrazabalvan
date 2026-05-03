package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import com.daw.adoptauncompanero.dtos.HistorialEstadoDTO;
import com.daw.adoptauncompanero.dtos.SolicitudDTO;

public interface SolicitudService {

    // 2.2.3.4 - cliente inicia una solicitud
    Integer iniciarSolicitud(Integer idUsuario, Integer idAnimal, String comentarios);

    // 2.2.4.2 / 2.4.6 - admin lista solicitudes
    List<SolicitudDTO> buscarSolicitudes(Integer id, Integer idUsuario, Integer idAnimal, Integer idEstado);

    // 2.2.3.5 - cliente consulta sus solicitudes
    List<SolicitudDTO> listarSolicitudesPorUsuario(Integer idUsuario);

    // 2.2.4.3 - admin cambia estado (genera historial + email automático)
    Integer cambiarEstado(Integer idSolicitud, Integer idEstadoNuevo, String comentarioAdmin);

    // 2.2.3.7 - cliente cancela solicitud en estado inicial
    Integer cancelarSolicitud(Integer idSolicitud, Integer idUsuario);

    // 2.4.9 - historial de cambios (lo ve el cliente y el admin)
    List<HistorialEstadoDTO> obtenerHistorial(Integer idSolicitud);
}
