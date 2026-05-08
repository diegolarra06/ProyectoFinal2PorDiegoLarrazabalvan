package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.entities.ImagenAnimalEntity;


public interface ImagenAnimalRepository extends JpaRepository<ImagenAnimalEntity, Integer> {

	@Query("SELECT i FROM ImagenAnimalEntity i WHERE i.animal.idAnimal = :idAnimal")
	List<ImagenAnimalEntity> listarPorAnimal(@Param("idAnimal") Integer idAnimal);
}