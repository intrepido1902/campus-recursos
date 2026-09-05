package com.example.campusrecursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.campusrecursos.entities.Categoria;
import com.example.campusrecursos.services.CategoriaService;

// CRUD simple de categorias (requisito 4.a: catalogo y disponibilidad).
@Controller
public class CategoriaController {
    private final CategoriaService servicio;

    public CategoriaController(CategoriaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/categorias")
    public String listar(Model model) {
        model.addAttribute("categorias", servicio.listar());
        model.addAttribute("categoria", new Categoria());
        return "categorias/lista";
    }

    @PostMapping("/categorias")
    public String crear(@ModelAttribute Categoria categoria) {
        servicio.guardar(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/categorias/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", servicio.buscarPorId(id));
        return "categorias/editar";
    }

    @PostMapping("/categorias/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Categoria categoria) {
        categoria.setId(id);
        servicio.guardar(categoria);
        return "redirect:/categorias";
    }

    @PostMapping("/categorias/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
        } catch (RuntimeException ex) {
            // Puede fallar si hay recursos que todavia usan esta categoria (integridad referencial).
            flash.addFlashAttribute("error", "No se puede eliminar: hay recursos asociados a esta categoria");
        }
        return "redirect:/categorias";
    }
}
