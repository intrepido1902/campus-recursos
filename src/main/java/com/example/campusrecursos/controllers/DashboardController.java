package com.example.campusrecursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.campusrecursos.services.DashboardService;

// Requisito 4.f: tablero operativo y analitica.
@Controller
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/tablero")
    public String tablero(Model model) {
        model.addAttribute("recursos", dashboardService.estadoDeRecursos());
        model.addAttribute("reservasPorRecurso", dashboardService.reservasPorRecurso());
        model.addAttribute("devolucionesTardias", dashboardService.totalDevolucionesTardias());
        model.addAttribute("totalIncidentes", dashboardService.totalIncidentes());
        model.addAttribute("incidentesPorSeveridad", dashboardService.incidentesPorSeveridad());
        return "tablero";
    }
}
