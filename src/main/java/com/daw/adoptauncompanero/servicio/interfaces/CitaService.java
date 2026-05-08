package com.daw.adoptauncompanero.servicio.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.daw.adoptauncompanero.dtos.CitaDTO;

public interface CitaService {

    Integer programarCita(Integer idSolicitud, LocalDateTime fechaCita, String observaciones);

    Integer actualizarCita(Integer idCita, LocalDateTime fechaCita, String observaciones);

    Integer borrarCita(Integer idCita);

    List<CitaDTO> listarTodasLasCitas();

    List<CitaDTO> listarCitasPorUsuario(Integer idUsuario);
}