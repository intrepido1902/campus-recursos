package com.example.campusrecursos.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.EstadoRecurso;
import com.example.campusrecursos.entities.Prestamo;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.repositories.PrestamoRepository;

// Requisito 4.c: prestamo de equipos y devolucion.
@Service
public class PrestamoService {
    private final PrestamoRepository repositorio;
    private final RecursoService recursoService;

    public PrestamoService(PrestamoRepository repositorio, RecursoService recursoService) {
        this.repositorio = repositorio;
        this.recursoService = recursoService;
    }

    public List<Prestamo> listar() {
        return repositorio.findAll();
    }

    public Prestamo buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("El prestamo no existe"));
    }

    public Prestamo registrarEntrega(Long recursoId, String responsable, LocalDateTime fechaLimite, String estadoInicial) {
        Recurso recurso = recursoService.buscarPorId(recursoId);

        // RN-02: no se puede prestar un recurso bloqueado.
        if (recurso.getEstado() == EstadoRecurso.BLOQUEADO) {
            throw new ReglaNegocioException("El recurso esta bloqueado y no se puede prestar");
        }
        // RN-03: no se puede prestar un recurso que ya figura como prestado.
        if (repositorio.findFirstByRecursoIdAndFechaDevolucionIsNull(recursoId).isPresent()) {
            throw new ReglaNegocioException("El recurso ya esta prestado; debe registrarse la devolucion primero");
        }

        Prestamo prestamo = new Prestamo(recurso, responsable, LocalDateTime.now(), fechaLimite, estadoInicial);
        prestamo = repositorio.save(prestamo);

        recursoService.cambiarEstado(recursoId, EstadoRecurso.PRESTADO);
        return prestamo;
    }

    public Prestamo registrarDevolucion(Long prestamoId, String estadoFinal, String observaciones) {
        Prestamo prestamo = buscarPorId(prestamoId);
        if (prestamo.isDevuelto()) {
            throw new ReglaNegocioException("Este prestamo ya fue devuelto");
        }
        prestamo.setFechaDevolucion(LocalDateTime.now());
        prestamo.setEstadoFinal(estadoFinal);
        prestamo.setObservaciones(observaciones);
        repositorio.save(prestamo);

        // Al devolver, el recurso vuelve a estar disponible (si no estaba bloqueado por otra causa).
        Recurso recurso = prestamo.getRecurso();
        if (recurso.getEstado() == EstadoRecurso.PRESTADO) {
            recursoService.cambiarEstado(recurso.getId(), EstadoRecurso.DISPONIBLE);
        }
        return prestamo;
    }
}
