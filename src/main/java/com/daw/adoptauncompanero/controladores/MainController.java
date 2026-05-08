package com.daw.adoptauncompanero.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "redirect:http://localhost:5173/";
    }

    @GetMapping("/accesoDenegado")
    public String accesoDenegado() {
        return "redirect:http://localhost:5173/";
    }

   
}