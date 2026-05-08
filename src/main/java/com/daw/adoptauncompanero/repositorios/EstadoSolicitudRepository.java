package com.daw.adoptauncompanero.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.entities.EstadoSolicitudEntity;


public interface EstadoSolicitudRepository extends JpaRepository<EstadoSolicitudEntity, Integer> {

    @Query("SELECT e FROM EstadoSolicitudEntity e WHERE e.nombre = :nombre")
    EstadoSolicitudEntity findByNombre(@Param("nombre") String nombre);
}