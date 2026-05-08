package com.daw.adoptauncompanero.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.entities.RolEntity;


public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    @Query("SELECT r FROM RolEntity r WHERE r.nombre = :nombre")
    RolEntity findByNombre(@Param("nombre") String nombre);
}
