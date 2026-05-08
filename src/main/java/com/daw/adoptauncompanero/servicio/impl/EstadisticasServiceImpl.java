package com.daw.adoptauncompanero.servicio.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.adoptauncompanero.dtos.EstadisticasDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;
import com.daw.adoptauncompanero.repositorios.AnimalRepository;
import com.daw.adoptauncompanero.repositorios.SolicitudAdopcionRepository;
import com.daw.adoptauncompanero.servicio.interfaces.EstadisticasService;

@Service
public class EstadisticasServiceImpl implements EstadisticasService {

	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private SolicitudAdopcionRepository solicitudRepository;

	@Override
	public EstadisticasDTO calcularEstadisticas() {

		long total = animalRepository.count();
		long adoptados = animalRepository.findAll().stream()
				.filter(a -> a.getEstado() == AnimalEntity.EstadoAnimal.ADOPTADO).count();
		long disponibles = animalRepository.findAll().stream()
				.filter(a -> a.getEstado() == AnimalEntity.EstadoAnimal.DISPONIBLE).count();
		double porcentaje = total > 0 ? (adoptados * 100.0 / total) : 0.0;

		String especieMasAdoptada = animalRepository.findAll().stream()
				.filter(a -> a.getEstado() == AnimalEntity.EstadoAnimal.ADOPTADO)
				.collect(java.util.stream.Collectors.groupingBy(AnimalEntity::getEspecie,
						java.util.stream.Collectors.counting()))
				.entrySet().stream().max(java.util.Map.Entry.comparingByValue()).map(java.util.Map.Entry::getKey)
				.orElse("—");

		long totalSolicitudes = solicitudRepository.count();
		long aprobadas = solicitudRepository.contarPorEstado("APROBADA")
				+ solicitudRepository.contarPorEstado("FINALIZADA");
		long rechazadas = solicitudRepository.contarPorEstado("RECHAZADA");

		return new EstadisticasDTO(total, adoptados, disponibles, porcentaje, totalSolicitudes, aprobadas, rechazadas,
				especieMasAdoptada);
	}
}
