package com.example.campusrecursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.Ubicacion;
import com.example.campusrecursos.repositories.UbicacionRepository;

@Service
public class UbicacionService {
    private final UbicacionRepository repositorio;

    public UbicacionService(UbicacionRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Ubicacion> listar() {
        return repositorio.findAll();
    }

    public Ubicacion buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("La ubicacion no existe"));
    }

    public Ubicacion guardar(Ubicacion ubicacion) {
        return repositorio.save(ubicacion);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}
