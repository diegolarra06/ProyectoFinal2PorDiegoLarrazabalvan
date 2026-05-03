package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.SolicitudDTO;
import com.daw.adoptauncompanero.entities.SolicitudAdopcionEntity;

// =============================================================
// REPOSITORIO SOLICITUDES (2.3.4 Proceso de adopción)
//  - 2.2.3.5 cliente consulta sus solicitudes
//  - 2.2.4.2 admin gestiona todas las solicitudes
//  - 2.4.6 admin revisa y modifica estado
// =============================================================
public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcionEntity, Integer> {

	// -----------------------------------------------------------
	// LISTADO DE SOLICITUDES POR FILTROS (panel admin)
	// -----------------------------------------------------------
	@Query("""
			    SELECT new com.daw.adoptauncompanero.dtos.SolicitudDTO(
			        s.idSolicitud, s.fechaSolicitud, s.comentarios,
			        u.idUsuario, u.nombre, u.email,
			        a.idAnimal, a.nombre,
			        e.idEstado, e.nombre
			    )
			    FROM SolicitudAdopcionEntity s
			    JOIN s.usuario u
			    JOIN s.animal a
			    JOIN s.estado e
			    WHERE (:id IS NULL OR s.idSolicitud = :id)
			      AND (:idUsuario IS NULL OR u.idUsuario = :idUsuario)
			      AND (:idAnimal IS NULL OR a.idAnimal = :idAnimal)
			      AND (:idEstado IS NULL OR e.idEstado = :idEstado)
			    ORDER BY s.fechaSolicitud DESC
			""")
	List<SolicitudDTO> buscarSolicitudesPorFiltros(@Param("id") Integer id, @Param("idUsuario") Integer idUsuario,
			@Param("idAnimal") Integer idAnimal, @Param("idEstado") Integer idEstado);

	// -----------------------------------------------------------
	// SOLICITUDES DE UN USUARIO (2.3.3.4 área personal)
	// -----------------------------------------------------------
	@Query("""
			    SELECT new com.daw.adoptauncompanero.dtos.SolicitudDTO(
			        s.idSolicitud, s.fechaSolicitud, s.comentarios,
			        u.idUsuario, u.nombre, u.email,
			        a.idAnimal, a.nombre,
			        e.idEstado, e.nombre
			    )
			    FROM SolicitudAdopcionEntity s
			    JOIN s.usuario u
			    JOIN s.animal a
			    JOIN s.estado e
			    WHERE u.idUsuario = :idUsuario
			    ORDER BY s.fechaSolicitud DESC
			""")
	List<SolicitudDTO> listarPorUsuario(@Param("idUsuario") Integer idUsuario);

	// -----------------------------------------------------------
	// VALIDACIÓN: ¿el usuario ya solicitó este animal?
	// (en el SQL hay UNIQUE(id_usuario, id_animal))
	// -----------------------------------------------------------
	@Query("""
			    SELECT COUNT(s) FROM SolicitudAdopcionEntity s
			    WHERE s.usuario.idUsuario = :idUsuario AND s.animal.idAnimal = :idAnimal
			""")
	Long contarSolicitudesUsuarioAnimal(@Param("idUsuario") Integer idUsuario, @Param("idAnimal") Integer idAnimal);

	// Estadísticas (2.2.4.7)
	@Query("SELECT COUNT(s) FROM SolicitudAdopcionEntity s WHERE s.estado.nombre = :nombreEstado")
	Long contarPorEstado(@Param("nombreEstado") String nombreEstado);
}
