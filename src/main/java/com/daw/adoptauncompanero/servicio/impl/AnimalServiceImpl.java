package com.daw.adoptauncompanero.servicio.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;
import com.daw.adoptauncompanero.entities.AnimalEntity.EstadoAnimal;
import com.daw.adoptauncompanero.entities.ImagenAnimalEntity;
import com.daw.adoptauncompanero.repositorios.AnimalRepository;
import com.daw.adoptauncompanero.repositorios.ImagenAnimalRepository;
import com.daw.adoptauncompanero.servicio.interfaces.AnimalService;
import com.daw.adoptauncompanero.servicio.interfaces.FileStorageService;

@Service
public class AnimalServiceImpl implements AnimalService {

	@Autowired
	private AnimalRepository animalRepository;

	@Autowired
	private ImagenAnimalRepository imagenRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public List<AnimalDTO> buscarAnimales(Integer id, String nombre, String especie, Integer edadMin, Integer edadMax,
			String tamano, String estado) {
		String n = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
		String e = (especie != null && !especie.trim().isEmpty()) ? especie.trim() : null;
		String t = (tamano != null && !tamano.trim().isEmpty()) ? tamano.trim() : null;
		String s = (estado != null && !estado.trim().isEmpty()) ? estado.trim() : null;

		return animalRepository.buscarAnimalesPorFiltros(id, n, e, edadMin, edadMax, t, s);
	}

	@Override
	public AnimalEntity obtenerAnimalPorId(Integer id) {
		return animalRepository.findById(id).orElse(null);
	}

	@Override
	public Integer insertarAnimal(String nombre, String especie, Integer edad, String tamano, String personalidad,
			String necesidades, String sanitario, String estado) {

		AnimalEntity a = new AnimalEntity();
		a.setNombre(nombre);
		a.setEspecie(especie);
		a.setEdad(edad);
		a.setTamano(tamano);
		a.setPersonalidad(personalidad);
		a.setNecesidadesEspeciales(necesidades);
		a.setEstadoSanitario(sanitario);
		a.setEstado(estado != null ? EstadoAnimal.valueOf(estado) : EstadoAnimal.DISPONIBLE);
		a.setFechaAlta(LocalDateTime.now());

		return animalRepository.save(a).getIdAnimal();
	}

	@Override
	public Integer actualizarAnimal(Integer id, String nombre, String especie, Integer edad, String tamano,
			String personalidad, String necesidades, String sanitario, String estado) {

		AnimalEntity a = animalRepository.findById(id).orElse(null);
		if (a == null)
			return 0;

		a.setNombre(nombre);
		a.setEspecie(especie);
		a.setEdad(edad);
		a.setTamano(tamano);
		a.setPersonalidad(personalidad);
		a.setNecesidadesEspeciales(necesidades);
		a.setEstadoSanitario(sanitario);
		if (estado != null)
			a.setEstado(EstadoAnimal.valueOf(estado));

		return animalRepository.save(a).getIdAnimal();
	}

	@Override
	public Integer borrarAnimal(Integer id) {
		if (!animalRepository.existsById(id))
			return 0;
		animalRepository.deleteById(id);
		return id;
	}

	@Override
	public Integer subirImagen(Integer idAnimal, MultipartFile archivo) {

		AnimalEntity animal = animalRepository.findById(idAnimal).orElse(null);
		if (animal == null)
			return 0;

		String nombreArchivo = fileStorageService.guardarArchivo(archivo);

		ImagenAnimalEntity img = new ImagenAnimalEntity();
		img.setUrlImagen(nombreArchivo);
		img.setAnimal(animal);

		return imagenRepository.save(img).getIdImagen();
	}

	@Override
	public Integer borrarImagen(Integer idImagen) {
		ImagenAnimalEntity img = imagenRepository.findById(idImagen).orElse(null);
		if (img == null)
			return 0;

		fileStorageService.eliminarArchivo(img.getUrlImagen());
		imagenRepository.deleteById(idImagen);
		return idImagen;
	}

	@Override
	public List<AnimalEntity> listarDisponibles() {
		return animalRepository.listarDisponibles();
	}

	@Override
	public Page<AnimalDTO> listarAnimalesPaginados(Pageable pageable) {
		return animalRepository.obtenerAnimalesPaginados(pageable);
	}

	@Override
	public List<String> listarEspeciesDistintas() {
		return animalRepository.listarEspeciesDistintas();
	}
}
