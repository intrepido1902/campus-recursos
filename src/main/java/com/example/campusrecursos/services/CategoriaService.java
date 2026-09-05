package com.example.campusrecursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.Categoria;
import com.example.campusrecursos.repositories.CategoriaRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository repositorio;

    public CategoriaService(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Categoria> listar() {
        return repositorio.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("La categoria no existe"));
    }

    public Categoria guardar(Categoria categoria) {
        return repositorio.save(categoria);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}
