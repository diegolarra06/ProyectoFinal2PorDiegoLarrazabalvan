package com.daw.adoptauncompanero.controladores.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.servicio.interfaces.AnimalService;

@RestController
@RequestMapping("/v1")
public class AnimalesRestController {

	@Autowired
	private AnimalService animalService;

	@GetMapping("/animalesPaginacion")
	public ResponseEntity<Page<AnimalDTO>> listarAnimalesPaginados(
			@PageableDefault(size = 5, sort = "idAnimal") Pageable pageable) {

		Page<AnimalDTO> pagina = animalService.listarAnimalesPaginados(pageable);
		return ResponseEntity.ok(pagina);
	}
}
