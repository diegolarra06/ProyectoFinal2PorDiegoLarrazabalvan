package com.daw.adoptauncompanero.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.servicio.interfaces.AnimalService;

@Controller
@RequestMapping("/animales")
public class AnimalesController {

	@Autowired
	private AnimalService animalService;

	@GetMapping("/catalogo")
	public String catalogoGet(Model model) {

		model.addAttribute("lista", animalService.buscarAnimales(null, null, null, null, null, null, null));
		return "animales/catalogo";
	}

	@PostMapping("/catalogo")
	public String catalogoPost(@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String especie, @RequestParam(required = false) Integer edadMin,
			@RequestParam(required = false) Integer edadMax, @RequestParam(required = false) String tamano,
			@RequestParam(required = false) String estado, Model model) {

		model.addAttribute("lista",
				animalService.buscarAnimales(null, nombre, especie, edadMin, edadMax, tamano, estado));
		return "animales/catalogo";
	}

	@GetMapping("/ficha/{id}")
	public String fichaAnimal(@PathVariable Integer id, Model model) {
		model.addAttribute("animal", animalService.obtenerAnimalPorId(id));
		return "animales/ficha";
	}

	@GetMapping("/insertar")
	public String insertarGet(Model model) {
		model.addAttribute("resultado", null);
		return "animales/insertarAnimal";
	}

	@PostMapping("/insertar")
	public String insertarPost(@RequestParam String nombre, @RequestParam String especie,
			@RequestParam(required = false) Integer edad, @RequestParam(required = false) String tamano,
			@RequestParam(required = false) String personalidad,
			@RequestParam(required = false) String necesidadesEspeciales,
			@RequestParam(required = false) String estadoSanitario, @RequestParam(required = false) String estado,
			Model model) {

		Integer res = animalService.insertarAnimal(nombre, especie, edad, tamano, personalidad, necesidadesEspeciales,
				estadoSanitario, estado);
		model.addAttribute("resultado", res);
		return "animales/insertarAnimal";
	}

	@GetMapping("/formularioActualizar")
	public String formularioActualizarGet(Model model) {
		model.addAttribute("lista", null);
		model.addAttribute("resultado", null);
		return "animales/actualizarAnimal";
	}

	@PostMapping("/formularioActualizar")
	public String formularioActualizarPost(@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String nombre, @RequestParam(required = false) String especie,
			Model model) {
		model.addAttribute("lista", animalService.buscarAnimales(id, nombre, especie, null, null, null, null));
		model.addAttribute("resultado", null);
		return "animales/actualizarAnimal";
	}

	@PostMapping("/actualizar")
	public String actualizarPost(@RequestParam Integer id, @RequestParam String nombre, @RequestParam String especie,
			@RequestParam(required = false) Integer edad, @RequestParam(required = false) String tamano,
			@RequestParam(required = false) String personalidad,
			@RequestParam(required = false) String necesidadesEspeciales,
			@RequestParam(required = false) String estadoSanitario, @RequestParam(required = false) String estado,
			Model model) {

		Integer res = animalService.actualizarAnimal(id, nombre, especie, edad, tamano, personalidad,
				necesidadesEspeciales, estadoSanitario, estado);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", null);
		return "animales/actualizarAnimal";
	}

	@GetMapping("/formularioBorrar")
	public String formularioBorrarGet(Model model) {
		model.addAttribute("lista", null);
		model.addAttribute("resultado", null);
		return "animales/borrarAnimal";
	}

	@PostMapping("/formularioBorrar")
	public String formularioBorrarPost(@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String nombre, Model model) {
		model.addAttribute("lista", animalService.buscarAnimales(id, nombre, null, null, null, null, null));
		model.addAttribute("resultado", null);
		return "animales/borrarAnimal";
	}

	@PostMapping("/borrar")
	public String borrarPost(@RequestParam Integer id, Model model) {
		Integer res = animalService.borrarAnimal(id);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", null);
		return "animales/borrarAnimal";
	}

	@GetMapping("/imagenes/{idAnimal}")
	public String imagenesGet(@PathVariable Integer idAnimal, Model model) {
		model.addAttribute("animal", animalService.obtenerAnimalPorId(idAnimal));
		model.addAttribute("resultado", null);
		return "animales/subirImagen";
	}

	@PostMapping("/imagenes/{idAnimal}/subir")
	public String subirImagen(@PathVariable Integer idAnimal, @RequestParam("archivo") MultipartFile archivo,
			Model model) {

		Integer res = animalService.subirImagen(idAnimal, archivo);
		model.addAttribute("animal", animalService.obtenerAnimalPorId(idAnimal));
		model.addAttribute("resultado", res);
		return "animales/subirImagen";
	}

	@PostMapping("/imagenes/{idAnimal}/borrar/{idImagen}")
	public String borrarImagen(@PathVariable Integer idAnimal, @PathVariable Integer idImagen, Model model) {
		Integer res = animalService.borrarImagen(idImagen);
		model.addAttribute("animal", animalService.obtenerAnimalPorId(idAnimal));
		model.addAttribute("resultado", res);
		return "animales/subirImagen";
	}
}
