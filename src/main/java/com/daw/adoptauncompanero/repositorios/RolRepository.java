package com.daw.adoptauncompanero.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.entities.RolEntity;

// =============================================================
// REPOSITORIO ROLES (PDF 5 - Seguridad y Roles)
// Usado para asignar el rol CLIENTE al registrarse y para
// que el admin pueda gestionar los roles si fuese necesario.
// =============================================================
public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    // Buscamos un rol por su nombre (CLIENTE o ADMIN)
    @Query("SELECT r FROM RolEntity r WHERE r.nombre = :nombre")
    RolEntity findByNombre(@Param("nombre") String nombre);
}
