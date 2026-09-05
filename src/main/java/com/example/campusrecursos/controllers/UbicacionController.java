package com.example.campusrecursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.campusrecursos.entities.Ubicacion;
import com.example.campusrecursos.services.UbicacionService;

@Controller
public class UbicacionController {
    private final UbicacionService servicio;

    public UbicacionController(UbicacionService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/ubicaciones")
    public String listar(Model model) {
        model.addAttribute("ubicaciones", servicio.listar());
        model.addAttribute("ubicacion", new Ubicacion());
        return "ubicaciones/lista";
    }

    @PostMapping("/ubicaciones")
    public String crear(@ModelAttribute Ubicacion ubicacion) {
        servicio.guardar(ubicacion);
        return "redirect:/ubicaciones";
    }

    @GetMapping("/ubicaciones/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("ubicacion", servicio.buscarPorId(id));
        return "ubicaciones/editar";
    }

    @PostMapping("/ubicaciones/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Ubicacion ubicacion) {
        ubicacion.setId(id);
        servicio.guardar(ubicacion);
        return "redirect:/ubicaciones";
    }

    @PostMapping("/ubicaciones/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se puede eliminar: hay recursos asociados a esta ubicacion");
        }
        return "redirect:/ubicaciones";
    }
}
