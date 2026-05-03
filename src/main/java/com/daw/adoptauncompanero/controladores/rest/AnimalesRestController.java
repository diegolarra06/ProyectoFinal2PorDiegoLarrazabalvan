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

// =============================================================
// PDF 6.3 - API REST con paginación
// Endpoint público para listar animales con paginación.
// Ejemplos:
//   GET /v1/animalesPaginacion
//   GET /v1/animalesPaginacion?page=1&size=10
//   GET /v1/animalesPaginacion?page=0&size=5&sort=nombre,asc
// =============================================================
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
