package com.daw.adoptauncompanero.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daw.adoptauncompanero.servicio.interfaces.EstadisticasService;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping("/panel")
    public String panel(Model model) {
        model.addAttribute("estadisticas", estadisticasService.calcularEstadisticas());
        return "admin/estadisticas";
    }
}
