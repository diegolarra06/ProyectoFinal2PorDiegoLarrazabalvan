package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Integer> {

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

	@Query("""
			    SELECT a FROM AnimalEntity a
			    WHERE a.estado = com.daw.adoptauncompanero.entities.AnimalEntity.EstadoAnimal.DISPONIBLE
			    ORDER BY a.fechaAlta DESC
			""")
	List<AnimalEntity> listarDisponibles();

	@Query("SELECT DISTINCT a.especie FROM AnimalEntity a " + "WHERE a.especie IS NOT NULL AND a.especie <> '' "
			+ "ORDER BY a.especie ASC")
	List<String> listarEspeciesDistintas();
}