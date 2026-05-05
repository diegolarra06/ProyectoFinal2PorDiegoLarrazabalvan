package com.daw.adoptauncompanero.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// =============================================================
// CONTROLADOR PRINCIPAL
// Como el frontend lo lleva Vue (puerto 5173), este controlador
// solo redirige las rutas raíz al frontend Vue.
//
// El registro y login REALES se hacen vía:
//   - POST /login            → Spring Security (procesa el login)
//   - POST /api/registro     → endpoint REST de registro (en ApiRestController)
//   - GET  /api/me           → datos del usuario logueado (en ApiRestController)
//
// Ya NO se usan plantillas Thymeleaf en este controlador.
// =============================================================
@Controller
public class MainController {

    // GET / → redirige al front Vue (página de inicio)
    @GetMapping("/")
    public String index() {
        return "redirect:http://localhost:5173/";
    }

    // GET /accesoDenegado → redirige al front Vue
    @GetMapping("/accesoDenegado")
    public String accesoDenegado() {
        return "redirect:http://localhost:5173/";
    }

   
}