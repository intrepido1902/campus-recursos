package com.example.campusrecursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.campusrecursos.entities.Recurso;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
}
