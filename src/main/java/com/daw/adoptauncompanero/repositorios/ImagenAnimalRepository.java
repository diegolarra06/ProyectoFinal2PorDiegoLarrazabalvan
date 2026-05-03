package com.daw.adoptauncompanero.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.adoptauncompanero.entities.ImagenAnimalEntity;

// =============================================================
// REPOSITORIO IMAGENES (PDF 6.2 - Almacenamiento de archivos)
// Cada imagen está vinculada a un animal y se guarda en /uploads
// =============================================================
public interface ImagenAnimalRepository extends JpaRepository<ImagenAnimalEntity, Integer> {

	@Query("SELECT i FROM ImagenAnimalEntity i WHERE i.animal.idAnimal = :idAnimal")
	List<ImagenAnimalEntity> listarPorAnimal(@Param("idAnimal") Integer idAnimal);
}