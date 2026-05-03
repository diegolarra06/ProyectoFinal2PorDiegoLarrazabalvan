package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;

public interface AnimalService {

	// 2.3.1 - catálogo con filtros
	List<AnimalDTO> buscarAnimales(Integer id, String nombre, String especie, Integer edadMin, Integer edadMax,
			String tamano, String estado);

	// 2.3.2 - ficha del animal
	AnimalEntity obtenerAnimalPorId(Integer id);

	// 2.2.4.1 - CRUD admin
	Integer insertarAnimal(String nombre, String especie, Integer edad, String tamano, String personalidad,
			String necesidades, String sanitario, String estado);

	Integer actualizarAnimal(Integer id, String nombre, String especie, Integer edad, String tamano,
			String personalidad, String necesidades, String sanitario, String estado);

	Integer borrarAnimal(Integer id);

	// PDF 6.2 - subir imagen de un animal (2.3.2.1)
	Integer subirImagen(Integer idAnimal, MultipartFile archivo);

	Integer borrarImagen(Integer idImagen);

	// 2.3.1 / home: animales disponibles (catálogo público)
	List<AnimalEntity> listarDisponibles();

	// PDF 6.3 - paginación
	Page<AnimalDTO> listarAnimalesPaginados(Pageable pageable);
}
