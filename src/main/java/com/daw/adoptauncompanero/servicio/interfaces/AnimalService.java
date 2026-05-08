package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;

public interface AnimalService {

	List<AnimalDTO> buscarAnimales(Integer id, String nombre, String especie, Integer edadMin, Integer edadMax,
			String tamano, String estado);

	AnimalEntity obtenerAnimalPorId(Integer id);

	Integer insertarAnimal(String nombre, String especie, Integer edad, String tamano, String personalidad,
			String necesidades, String sanitario, String estado);

	Integer actualizarAnimal(Integer id, String nombre, String especie, Integer edad, String tamano,
			String personalidad, String necesidades, String sanitario, String estado);

	Integer borrarAnimal(Integer id);

	Integer subirImagen(Integer idAnimal, MultipartFile archivo);

	Integer borrarImagen(Integer idImagen);

	List<AnimalEntity> listarDisponibles();

	Page<AnimalDTO> listarAnimalesPaginados(Pageable pageable);

	java.util.List<String> listarEspeciesDistintas();
}
