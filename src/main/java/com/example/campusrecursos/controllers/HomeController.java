package com.example.campusrecursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Pagina de inicio con enlaces a cada modulo, igual en espiritu al InicioServlet
// visto en el ejemplo de servlets, pero ahora resuelta con Spring MVC + Thymeleaf.
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}
