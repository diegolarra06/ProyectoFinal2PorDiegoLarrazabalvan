package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.CitaDTO;
import com.daw.adoptauncompanero.entities.CitaAdopcionEntity;


public interface CitaAdopcionRepository extends JpaRepository<CitaAdopcionEntity, Integer> {

    @Query("""
        SELECT new com.daw.adoptauncompanero.dtos.CitaDTO(
            c.idCita, c.fechaCita, c.observaciones,
            c.solicitud.idSolicitud,
            c.solicitud.usuario.nombre,
            c.solicitud.animal.nombre
        )
        FROM CitaAdopcionEntity c
        ORDER BY c.fechaCita ASC
    """)
    List<CitaDTO> listarTodasLasCitas();

    @Query("""
        SELECT new com.daw.adoptauncompanero.dtos.CitaDTO(
            c.idCita, c.fechaCita, c.observaciones,
            c.solicitud.idSolicitud,
            c.solicitud.usuario.nombre,
            c.solicitud.animal.nombre
        )
        FROM CitaAdopcionEntity c
        WHERE c.solicitud.usuario.idUsuario = :idUsuario
        ORDER BY c.fechaCita ASC
    """)
    List<CitaDTO> listarCitasPorUsuario(@Param("idUsuario") Integer idUsuario);
}
