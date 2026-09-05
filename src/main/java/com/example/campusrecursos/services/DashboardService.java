package com.example.campusrecursos.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.Prestamo;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.repositories.IncidenteRepository;
import com.example.campusrecursos.repositories.PrestamoRepository;
import com.example.campusrecursos.repositories.RecursoRepository;
import com.example.campusrecursos.repositories.ReservaRepository;

// Requisito 4.f: tablero operativo y analitica.
// Se calcula todo en memoria a partir de lo que ya esta en la base de datos;
// para el volumen de datos de un prototipo esto es mas que suficiente y evita
// tener que escribir consultas de agregacion mas avanzadas.
@Service
public class DashboardService {
    private final RecursoRepository recursoRepository;
    private final ReservaRepository reservaRepository;
    private final PrestamoRepository prestamoRepository;
    private final IncidenteRepository incidenteRepository;

    public DashboardService(RecursoRepository recursoRepository, ReservaRepository reservaRepository,
                             PrestamoRepository prestamoRepository, IncidenteRepository incidenteRepository) {
        this.recursoRepository = recursoRepository;
        this.reservaRepository = reservaRepository;
        this.prestamoRepository = prestamoRepository;
        this.incidenteRepository = incidenteRepository;
    }

    public List<Recurso> estadoDeRecursos() {
        return recursoRepository.findAll();
    }

    // Requisito 4.f.ii: numero de reservas por recurso.
    public List<ConteoReservas> reservasPorRecurso() {
        return recursoRepository.findAll().stream()
                .map(r -> new ConteoReservas(r.getNombre(), reservaRepository.findByRecursoId(r.getId()).size()))
                .sorted(Comparator.comparingLong(ConteoReservas::total).reversed())
                .toList();
    }

    // Requisito 4.f.iii: indicador adicional, devoluciones tardias.
    public long totalDevolucionesTardias() {
        return prestamoRepository.findAll().stream()
                .filter(Prestamo::isTardio)
                .count();
    }

    public long totalIncidentes() {
        return incidenteRepository.findAll().size();
    }

    public Map<String, Long> incidentesPorSeveridad() {
        return incidenteRepository.findAll().stream()
                .collect(Collectors.groupingBy(i -> i.getSeveridad().name(), Collectors.counting()));
    }

    public record ConteoReservas(String nombreRecurso, long total) {
    }
}
