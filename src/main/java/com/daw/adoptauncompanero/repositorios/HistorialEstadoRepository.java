package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.HistorialEstadoDTO;
import com.daw.adoptauncompanero.entities.HistorialEstadoSolicitudEntity;


public interface HistorialEstadoRepository extends JpaRepository<HistorialEstadoSolicitudEntity, Integer> {

    @Query("""
        SELECT new com.daw.adoptauncompanero.dtos.HistorialEstadoDTO(
            h.idHistorial, h.solicitud.idSolicitud,
            CASE WHEN h.estadoAnterior IS NULL THEN 'INICIO' ELSE h.estadoAnterior.nombre END,
            h.estadoNuevo.nombre,
            h.fechaCambio, h.comentarioAdmin
        )
        FROM HistorialEstadoSolicitudEntity h
        WHERE h.solicitud.idSolicitud = :idSolicitud
        ORDER BY h.fechaCambio ASC
    """)
    List<HistorialEstadoDTO> listarHistorialPorSolicitud(@Param("idSolicitud") Integer idSolicitud);
}
