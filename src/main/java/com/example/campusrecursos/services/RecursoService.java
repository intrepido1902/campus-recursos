package com.example.campusrecursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.Categoria;
import com.example.campusrecursos.entities.EstadoRecurso;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.entities.TipoRecurso;
import com.example.campusrecursos.entities.Ubicacion;
import com.example.campusrecursos.repositories.RecursoRepository;

@Service
public class RecursoService {
    private final RecursoRepository repositorio;
    private final CategoriaService categoriaService;
    private final UbicacionService ubicacionService;

    public RecursoService(RecursoRepository repositorio, CategoriaService categoriaService,
                           UbicacionService ubicacionService) {
        this.repositorio = repositorio;
        this.categoriaService = categoriaService;
        this.ubicacionService = ubicacionService;
    }

    // Igual que BibliotecaService.registrarLibro visto en clase: se reciben los
    // ids sueltos desde el formulario y aqui se resuelven las relaciones.
    public Recurso crear(String nombre, TipoRecurso tipo, Long categoriaId, Long ubicacionId,
                          String descripcion, String caracteristicas) {
        Categoria categoria = categoriaService.buscarPorId(categoriaId);
        Ubicacion ubicacion = ubicacionService.buscarPorId(ubicacionId);
        Recurso recurso = new Recurso(nombre, tipo, categoria, ubicacion, descripcion, caracteristicas);
        return repositorio.save(recurso);
    }

    public Recurso actualizar(Long id, String nombre, TipoRecurso tipo, Long categoriaId, Long ubicacionId,
                               String descripcion, String caracteristicas, EstadoRecurso estado) {
        Recurso recurso = buscarPorId(id);
        recurso.setNombre(nombre);
        recurso.setTipo(tipo);
        recurso.setCategoria(categoriaService.buscarPorId(categoriaId));
        recurso.setUbicacion(ubicacionService.buscarPorId(ubicacionId));
        recurso.setDescripcion(descripcion);
        recurso.setCaracteristicas(caracteristicas);
        recurso.setEstado(estado);
        return repositorio.save(recurso);
    }

    public List<Recurso> listar() {
        return repositorio.findAll();
    }

    // Requisito 4.a.iii: filtros basicos por categoria, ubicacion, tipo y estado.
    // Se filtra en memoria porque el catalogo de un prototipo es pequeno; no se
    // necesita una consulta dinamica mas compleja para este alcance.
    public List<Recurso> buscarConFiltros(Long categoriaId, Long ubicacionId, TipoRecurso tipo, EstadoRecurso estado) {
        return repositorio.findAll().stream()
                .filter(r -> categoriaId == null || (r.getCategoria() != null && r.getCategoria().getId().equals(categoriaId)))
                .filter(r -> ubicacionId == null || (r.getUbicacion() != null && r.getUbicacion().getId().equals(ubicacionId)))
                .filter(r -> tipo == null || r.getTipo() == tipo)
                .filter(r -> estado == null || r.getEstado() == estado)
                .toList();
    }

    public Recurso buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("El recurso no existe"));
    }

    public Recurso guardar(Recurso recurso) {
        return repositorio.save(recurso);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    public void cambiarEstado(Long id, EstadoRecurso estado) {
        Recurso recurso = buscarPorId(id);
        recurso.setEstado(estado);
        repositorio.save(recurso);
    }
}
