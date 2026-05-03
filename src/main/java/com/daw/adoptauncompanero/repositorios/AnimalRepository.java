package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;

// =============================================================
// REPOSITORIO ANIMALES
// Cubre:
//  - 2.3.1 Catálogo con filtros (especie, edad, tamaño, estado, nombre)
//  - 2.2.4.1 Gestión de animales (CRUD admin)
//  - PDF 6.3 Paginación (versión REST del listado)
// =============================================================
public interface AnimalRepository extends JpaRepository<AnimalEntity, Integer> {

	// -----------------------------------------------------------
	// BÚSQUEDA POR FILTROS (2.3.1.5 a 2.3.1.9)
	// Proyección directa sobre AnimalDTO con la primera imagen
	// -----------------------------------------------------------
	@Query("""
			    SELECT new com.daw.adoptauncompanero.dtos.AnimalDTO(
			        a.idAnimal, a.nombre, a.especie, a.edad, a.tamano,
			        a.personalidad, a.necesidadesEspeciales, a.estadoSanitario,
			        CAST(a.estado AS string),
			        (SELECT MIN(i.urlImagen) FROM ImagenAnimalEntity i WHERE i.animal = a)
			    )
			    FROM AnimalEntity a
			    WHERE (:id IS NULL OR a.idAnimal = :id)
			      AND (:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
			      AND (:especie IS NULL OR LOWER(a.especie) = LOWER(:especie))
			      AND (:edadMin IS NULL OR a.edad >= :edadMin)
			      AND (:edadMax IS NULL OR a.edad <= :edadMax)
			      AND (:tamano IS NULL OR LOWER(a.tamano) = LOWER(:tamano))
			      AND (:estado IS NULL OR CAST(a.estado AS string) = :estado)
			    ORDER BY a.idAnimal
			""")
	List<AnimalDTO> buscarAnimalesPorFiltros(@Param("id") Integer id, @Param("nombre") String nombre,
			@Param("especie") String especie, @Param("edadMin") Integer edadMin, @Param("edadMax") Integer edadMax,
			@Param("tamano") String tamano, @Param("estado") String estado);

	// -----------------------------------------------------------
	// LISTADO PAGINADO (PDF 6.3 - Paginación)
	// Para el endpoint REST /v1/animalesPaginacion
	// -----------------------------------------------------------
	@Query("""
			    SELECT new com.daw.adoptauncompanero.dtos.AnimalDTO(
			        a.idAnimal, a.nombre, a.especie, a.edad, a.tamano,
			        a.personalidad, a.necesidadesEspeciales, a.estadoSanitario,
			        CAST(a.estado AS string),
			        (SELECT MIN(i.urlImagen) FROM ImagenAnimalEntity i WHERE i.animal = a)
			    )
			    FROM AnimalEntity a
			""")
	Page<AnimalDTO> obtenerAnimalesPaginados(Pageable pageable);

	// -----------------------------------------------------------
	// SOLO ANIMALES DISPONIBLES (2.3.1 catálogo público / home)
	// -----------------------------------------------------------
	@Query("""
			    SELECT a FROM AnimalEntity a
			    WHERE a.estado = com.daw.adoptauncompanero.entities.AnimalEntity.EstadoAnimal.DISPONIBLE
			    ORDER BY a.fechaAlta DESC
			""")
	List<AnimalEntity> listarDisponibles();
}