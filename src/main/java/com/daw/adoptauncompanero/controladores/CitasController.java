package com.daw.adoptauncompanero.controladores;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.adoptauncompanero.servicio.interfaces.CitaService;

@Controller
@RequestMapping("/citas")
public class CitasController {

	@Autowired
	private CitaService citaService;

	@GetMapping("/listado")
	public String listadoGet(Model model) {
		model.addAttribute("lista", citaService.listarTodasLasCitas());
		return "citas/listado";
	}

	@GetMapping("/programar")
	public String programarGet(Model model) {
		model.addAttribute("resultado", null);
		return "citas/programar";
	}

	@PostMapping("/programar")
	public String programarPost(@RequestParam Integer idSolicitud,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaCita,
			@RequestParam(required = false) String observaciones, Model model) {

		Integer res = citaService.programarCita(idSolicitud, fechaCita, observaciones);
		model.addAttribute("resultado", res);
		return "citas/programar";
	}

	@PostMapping("/actualizar")
	public String actualizar(@RequestParam Integer idCita,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaCita,
			@RequestParam(required = false) String observaciones, Model model) {
		Integer res = citaService.actualizarCita(idCita, fechaCita, observaciones);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", citaService.listarTodasLasCitas());
		return "citas/listado";
	}

	@PostMapping("/borrar")
	public String borrar(@RequestParam Integer idCita, Model model) {
		Integer res = citaService.borrarCita(idCita);
		model.addAttribute("resultado", res);
		model.addAttribute("lista", citaService.listarTodasLasCitas());
		return "citas/listado";
	}
}
