package com.daw.adoptauncompanero.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.daw.adoptauncompanero.servicio.interfaces.AnimalService;
import com.daw.adoptauncompanero.servicio.interfaces.UsuarioService;

// =============================================================
// CONTROLADOR PRINCIPAL (PDF 5 - 2.6)
// Funcionalidades:
//  - 2.3 Página de inicio con resumen de animales
//  - 2.2.1.5 Registro de cliente
//  - login / accesoDenegado / logout
// =============================================================
@Controller
public class MainController {

	@Autowired
	private AnimalService animalService;
	@Autowired
	private UsuarioService usuarioService;

	// ---------------------------------------------------------
	// 2.3 Página de inicio
	// ---------------------------------------------------------
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("animalesDisponibles", animalService.listarDisponibles());
		return "index";
	}

	@GetMapping("/home")
	public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (userDetails != null) {
			model.addAttribute("usuario", userDetails.getUsername());
			model.addAttribute("rol", userDetails.getAuthorities());
		}
		model.addAttribute("animalesDisponibles", animalService.listarDisponibles());
		return "index";
	}

	// ---------------------------------------------------------
	// LOGIN (PDF 5 - 2.6)
	// ---------------------------------------------------------
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/accesoDenegado")
	public String accesoDenegado() {
		return "accesoDenegado";
	}

	// ---------------------------------------------------------
	// 2.2.1.5 - Registro de nuevo cliente
	// ---------------------------------------------------------
	@GetMapping("/registro")
	public String registroGet(Model model) {
		model.addAttribute("resultado", null);
		return "registro";
	}

	@PostMapping("/registro")
	public String registroPost(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
			@RequestParam(required = false) String telefono, @RequestParam(required = false) String direccion,
			Model model) {

		Integer res = usuarioService.registrarCliente(nombre, email, password, telefono, direccion);
		model.addAttribute("resultado", res);
		return "registro";
	}
}
