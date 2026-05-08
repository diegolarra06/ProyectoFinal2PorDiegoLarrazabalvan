package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.entities.NotificacionEntity;


public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Integer> {

    @Query("""
        SELECT n FROM NotificacionEntity n
        WHERE n.usuario.idUsuario = :idUsuario
        ORDER BY n.fechaCreacion DESC
    """)
    List<NotificacionEntity> listarPorUsuario(@Param("idUsuario") Integer idUsuario);
}
