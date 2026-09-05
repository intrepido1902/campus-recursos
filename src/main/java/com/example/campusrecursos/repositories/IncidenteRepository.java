package com.example.campusrecursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.campusrecursos.entities.Incidente;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    List<Incidente> findByRecursoId(Long recursoId);
}
