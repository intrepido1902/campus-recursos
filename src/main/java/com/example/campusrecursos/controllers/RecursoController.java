package com.example.campusrecursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.campusrecursos.entities.EstadoRecurso;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.entities.TipoRecurso;
import com.example.campusrecursos.services.CategoriaService;
import com.example.campusrecursos.services.RecursoService;
import com.example.campusrecursos.services.UbicacionService;

// Requisito 4.a: catalogo de recursos y consulta de disponibilidad.
@Controller
public class RecursoController {
    private final RecursoService recursoService;
    private final CategoriaService categoriaService;
    private final UbicacionService ubicacionService;

    public RecursoController(RecursoService recursoService, CategoriaService categoriaService,
                              UbicacionService ubicacionService) {
        this.recursoService = recursoService;
        this.categoriaService = categoriaService;
        this.ubicacionService = ubicacionService;
    }

    @GetMapping("/recursos")
    public String listar(@RequestParam(required = false) Long categoriaId,
                          @RequestParam(required = false) Long ubicacionId,
                          @RequestParam(required = false) TipoRecurso tipo,
                          @RequestParam(required = false) EstadoRecurso estado,
                          Model model) {
        model.addAttribute("recursos", recursoService.buscarConFiltros(categoriaId, ubicacionId, tipo, estado));
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("ubicaciones", ubicacionService.listar());
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("estados", EstadoRecurso.values());
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("ubicacionId", ubicacionId);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("estadoSeleccionado", estado);
        return "recursos/lista";
    }

    @GetMapping("/recursos/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("recurso", new Recurso());
        cargarCombos(model);
        return "recursos/formulario";
    }

    @PostMapping("/recursos")
    public String crear(@RequestParam String nombre,
                         @RequestParam TipoRecurso tipo,
                         @RequestParam Long categoriaId,
                         @RequestParam Long ubicacionId,
                         @RequestParam(required = false) String descripcion,
                         @RequestParam(required = false) String caracteristicas) {
        recursoService.crear(nombre, tipo, categoriaId, ubicacionId, descripcion, caracteristicas);
        return "redirect:/recursos";
    }

    @GetMapping("/recursos/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("recurso", recursoService.buscarPorId(id));
        cargarCombos(model);
        return "recursos/formulario";
    }

    @PostMapping("/recursos/{id}")
    public String actualizar(@PathVariable Long id,
                              @RequestParam String nombre,
                              @RequestParam TipoRecurso tipo,
                              @RequestParam Long categoriaId,
                              @RequestParam Long ubicacionId,
                              @RequestParam(required = false) String descripcion,
                              @RequestParam(required = false) String caracteristicas,
                              @RequestParam EstadoRecurso estado) {
        recursoService.actualizar(id, nombre, tipo, categoriaId, ubicacionId, descripcion, caracteristicas, estado);
        return "redirect:/recursos";
    }

    @PostMapping("/recursos/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            recursoService.eliminar(id);
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se puede eliminar: el recurso tiene reservas, prestamos o incidentes asociados");
        }
        return "redirect:/recursos";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("ubicaciones", ubicacionService.listar());
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("estados", EstadoRecurso.values());
    }
}
