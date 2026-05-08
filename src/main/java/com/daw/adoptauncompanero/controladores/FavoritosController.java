package com.daw.adoptauncompanero.controladores;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.servicio.interfaces.FavoritoService;
import com.daw.adoptauncompanero.servicio.interfaces.UsuarioService;

@Controller
@RequestMapping("/favoritos")
public class FavoritosController {

	@Autowired
	private FavoritoService favoritoService;
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/listado")
	public String listado(Principal principal, Model model) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		model.addAttribute("lista", favoritoService.listarFavoritosPorUsuario(u.getIdUsuario()));
		return "favoritos/listado";
	}

	@PostMapping("/agregar")
	public String agregar(@RequestParam Integer idAnimal, Principal principal, Model model) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		Integer res = favoritoService.agregarFavorito(u.getIdUsuario(), idAnimal);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", favoritoService.listarFavoritosPorUsuario(u.getIdUsuario()));
		return "favoritos/listado";
	}

	@PostMapping("/quitar")
	public String quitar(@RequestParam Integer idAnimal, Principal principal, Model model) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		Integer res = favoritoService.quitarFavorito(u.getIdUsuario(), idAnimal);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", favoritoService.listarFavoritosPorUsuario(u.getIdUsuario()));
		return "favoritos/listado";
	}
}
