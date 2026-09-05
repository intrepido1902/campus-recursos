package com.example.campusrecursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.EstadoRecurso;
import com.example.campusrecursos.entities.Incidente;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.entities.Severidad;
import com.example.campusrecursos.entities.TipoIncidente;
import com.example.campusrecursos.repositories.IncidenteRepository;

// Requisito 4.d: reporte de danios o fallas.
@Service
public class IncidenteService {
    private final IncidenteRepository repositorio;
    private final RecursoService recursoService;

    public IncidenteService(IncidenteRepository repositorio, RecursoService recursoService) {
        this.repositorio = repositorio;
        this.recursoService = recursoService;
    }

    public List<Incidente> listar() {
        return repositorio.findAll();
    }

    public Incidente reportar(Long recursoId, TipoIncidente tipo, Severidad severidad, String descripcion) {
        Recurso recurso = recursoService.buscarPorId(recursoId);

        Incidente incidente = new Incidente(recurso, tipo, severidad, descripcion);
        incidente = repositorio.save(incidente);

        // RN-04: un incidente critico bloquea el recurso hasta que un gestor lo revise.
        if (severidad == Severidad.CRITICA) {
            recursoService.cambiarEstado(recursoId, EstadoRecurso.BLOQUEADO);
        }
        return incidente;
    }
}
