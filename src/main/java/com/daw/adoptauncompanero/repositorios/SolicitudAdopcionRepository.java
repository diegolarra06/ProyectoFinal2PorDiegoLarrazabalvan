package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.SolicitudDTO;
import com.daw.adoptauncompanero.entities.SolicitudAdopcionEntity;


public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcionEntity, Integer> {

	
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

	@Query("""
			    SELECT COUNT(s) FROM SolicitudAdopcionEntity s
			    WHERE s.usuario.idUsuario = :idUsuario AND s.animal.idAnimal = :idAnimal
			""")
	Long contarSolicitudesUsuarioAnimal(@Param("idUsuario") Integer idUsuario, @Param("idAnimal") Integer idAnimal);

	@Query("SELECT COUNT(s) FROM SolicitudAdopcionEntity s WHERE s.estado.nombre = :nombreEstado")
	Long contarPorEstado(@Param("nombreEstado") String nombreEstado);
}
