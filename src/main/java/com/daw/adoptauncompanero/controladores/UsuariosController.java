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
import com.daw.adoptauncompanero.servicio.interfaces.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/areaPersonal")
	public String areaPersonal(Principal principal, Model model) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		model.addAttribute("usuario", u);
		model.addAttribute("resultado", null);
		return "usuarios/areaPersonal";
	}

	@PostMapping("/areaPersonal/actualizar")
	public String actualizarDatos(@RequestParam Integer id, @RequestParam String nombre,
			@RequestParam(required = false) String telefono, @RequestParam(required = false) String direccion,
			Principal principal, Model model) {

		Integer res = usuarioService.actualizarDatosPersonales(id, nombre, telefono, direccion);
		model.addAttribute("usuario", usuarioService.obtenerPorEmail(principal.getName()));
		model.addAttribute("resultado", res);
		return "usuarios/areaPersonal";
	}

	@GetMapping("/listado")
	public String listadoGet(Model model) {
		model.addAttribute("lista", null);
		return "usuarios/listado";
	}

	@PostMapping("/listado")
	public String listadoPost(@RequestParam(required = false) Integer id, @RequestParam(required = false) String nombre,
			@RequestParam(required = false) String email, Model model) {
		model.addAttribute("lista", usuarioService.buscarUsuarios(id, nombre, email));
		return "usuarios/listado";
	}

	@PostMapping("/borrar")
	public String borrar(@RequestParam Integer id, Model model) {
		Integer res = usuarioService.borrarUsuario(id);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", null);
		return "usuarios/listado";
	}
}
