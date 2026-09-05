package com.example.campusrecursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.campusrecursos.entities.Severidad;
import com.example.campusrecursos.entities.TipoIncidente;
import com.example.campusrecursos.services.IncidenteService;
import com.example.campusrecursos.services.RecursoService;

// Requisito 4.d: reporte de danios o fallas.
@Controller
public class IncidenteController {
    private final IncidenteService incidenteService;
    private final RecursoService recursoService;

    public IncidenteController(IncidenteService incidenteService, RecursoService recursoService) {
        this.incidenteService = incidenteService;
        this.recursoService = recursoService;
    }

    @GetMapping("/incidentes")
    public String listar(Model model) {
        model.addAttribute("incidentes", incidenteService.listar());
        return "incidentes/lista";
    }

    @GetMapping("/incidentes/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("recursos", recursoService.listar());
        model.addAttribute("tipos", TipoIncidente.values());
        model.addAttribute("severidades", Severidad.values());
        return "incidentes/formulario";
    }

    @PostMapping("/incidentes")
    public String reportar(@RequestParam Long recursoId,
                            @RequestParam TipoIncidente tipo,
                            @RequestParam Severidad severidad,
                            @RequestParam String descripcion,
                            RedirectAttributes flash) {
        incidenteService.reportar(recursoId, tipo, severidad, descripcion);
        if (severidad == Severidad.CRITICA) {
            flash.addFlashAttribute("mensaje", "Incidente registrado. Por ser critico, el recurso quedo bloqueado (RN-04)");
        } else {
            flash.addFlashAttribute("mensaje", "Incidente registrado correctamente");
        }
        return "redirect:/incidentes";
    }
}
