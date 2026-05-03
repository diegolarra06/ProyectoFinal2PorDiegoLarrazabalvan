package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.UsuarioDTO;
import com.daw.adoptauncompanero.entities.UsuarioEntity;

// =============================================================
// REPOSITORIO USUARIOS (PDF 5 - Seguridad)
// Funcionalidades cubiertas:
//  - Login (busca por email, que es nuestro "username")
//  - 2.2.4.6 Gestión de usuarios registrados (admin)
// =============================================================
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

	// Necesario para Spring Security: buscamos al usuario por email
	@Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email")
	UsuarioEntity findByEmail(@Param("email") String email);

	// Comprobamos si un email ya existe (registro)
	@Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.email = :email")
	Long contarPorEmail(@Param("email") String email);

	// Listado de usuarios para el panel de admin (2.2.4.6)
	// Proyectamos directamente sobre UsuarioDTO (PDF 3.10)
	@Query("""
			    SELECT new com.daw.adoptauncompanero.dtos.UsuarioDTO(
			        u.idUsuario, u.nombre, u.email, u.telefono, u.direccion,
			        (SELECT r.nombre FROM UsuarioRolEntity ur JOIN ur.rol r WHERE ur.usuario = u)
			    )
			    FROM UsuarioEntity u
			    WHERE (:id IS NULL OR u.idUsuario = :id)
			      AND (:nombre IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
			      AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
			    ORDER BY u.idUsuario
			""")
	List<UsuarioDTO> buscarUsuariosPorFiltros(@Param("id") Integer id, @Param("nombre") String nombre,
			@Param("email") String email);
}
