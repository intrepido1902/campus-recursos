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

import com.example.campusrecursos.services.ReglaNegocioException;
import com.example.campusrecursos.services.RecursoService;
import com.example.campusrecursos.services.ReservaService;

// Requisito 4.b: reservas y validacion de conflictos.
@Controller
public class ReservaController {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final ReservaService reservaService;
    private final RecursoService recursoService;

    public ReservaController(ReservaService reservaService, RecursoService recursoService) {
        this.reservaService = reservaService;
        this.recursoService = recursoService;
    }

    @GetMapping("/reservas")
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        return "reservas/lista";
    }

    @GetMapping("/reservas/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("recursos", recursoService.listar());
        return "reservas/formulario";
    }

    @PostMapping("/reservas")
    public String crear(@RequestParam Long recursoId,
                         @RequestParam String solicitante,
                         @RequestParam String fechaInicio,
                         @RequestParam String fechaFin,
                         RedirectAttributes flash) {
        try {
            reservaService.crear(recursoId, solicitante,
                    LocalDateTime.parse(fechaInicio, FORMATO), LocalDateTime.parse(fechaFin, FORMATO));
            flash.addFlashAttribute("mensaje", "Reserva creada correctamente");
            return "redirect:/reservas";
        } catch (ReglaNegocioException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            return "redirect:/reservas/nueva";
        }
    }

    @GetMapping("/reservas/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("reserva", reservaService.buscarPorId(id));
        return "reservas/editar";
    }

    @PostMapping("/reservas/{id}")
    public String actualizar(@PathVariable Long id,
                              @RequestParam String fechaInicio,
                              @RequestParam String fechaFin,
                              RedirectAttributes flash) {
        try {
            reservaService.modificar(id, LocalDateTime.parse(fechaInicio, FORMATO), LocalDateTime.parse(fechaFin, FORMATO));
            flash.addFlashAttribute("mensaje", "Reserva actualizada correctamente");
        } catch (ReglaNegocioException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            return "redirect:/reservas/" + id + "/editar";
        }
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes flash) {
        reservaService.cancelar(id);
        flash.addFlashAttribute("mensaje", "Reserva cancelada");
        return "redirect:/reservas";
    }
}
