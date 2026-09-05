package com.example.campusrecursos.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.campusrecursos.services.PrestamoService;
import com.example.campusrecursos.services.ReglaNegocioException;
import com.example.campusrecursos.services.RecursoService;

// Requisito 4.c: prestamo de equipos y devolucion.
@Controller
public class PrestamoController {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final PrestamoService prestamoService;
    private final RecursoService recursoService;

    public PrestamoController(PrestamoService prestamoService, RecursoService recursoService) {
        this.prestamoService = prestamoService;
        this.recursoService = recursoService;
    }

    @GetMapping("/prestamos")
    public String listar(Model model) {
        model.addAttribute("prestamos", prestamoService.listar());
        return "prestamos/lista";
    }

    @GetMapping("/prestamos/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("recursos", recursoService.listar());
        return "prestamos/formulario";
    }

    @PostMapping("/prestamos")
    public String registrarEntrega(@RequestParam Long recursoId,
                                    @RequestParam String responsable,
                                    @RequestParam String fechaLimite,
                                    @RequestParam String estadoInicial,
                                    RedirectAttributes flash) {
        try {
            prestamoService.registrarEntrega(recursoId, responsable, LocalDateTime.parse(fechaLimite, FORMATO), estadoInicial);
            flash.addFlashAttribute("mensaje", "Entrega registrada correctamente");
            return "redirect:/prestamos";
        } catch (ReglaNegocioException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            return "redirect:/prestamos/nuevo";
        }
    }

    @GetMapping("/prestamos/{id}/devolver")
    public String formularioDevolucion(@PathVariable Long id, Model model) {
        model.addAttribute("prestamo", prestamoService.buscarPorId(id));
        return "prestamos/devolver";
    }

    @PostMapping("/prestamos/{id}/devolver")
    public String registrarDevolucion(@PathVariable Long id,
                                       @RequestParam String estadoFinal,
                                       @RequestParam(required = false) String observaciones,
                                       RedirectAttributes flash) {
        try {
            prestamoService.registrarDevolucion(id, estadoFinal, observaciones);
            flash.addFlashAttribute("mensaje", "Devolucion registrada correctamente");
        } catch (ReglaNegocioException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/prestamos";
    }
}
