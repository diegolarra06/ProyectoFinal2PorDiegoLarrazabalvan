package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.UsuarioDTO;
import com.daw.adoptauncompanero.entities.UsuarioEntity;


public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

	@Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email")
	UsuarioEntity findByEmail(@Param("email") String email);

	@Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.email = :email")
	Long contarPorEmail(@Param("email") String email);

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
