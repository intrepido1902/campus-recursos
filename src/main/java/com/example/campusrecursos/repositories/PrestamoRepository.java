package com.example.campusrecursos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.campusrecursos.entities.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByRecursoId(Long recursoId);

    // Requisito 4.c.v (RN-03): un recurso prestado no puede entregarse de nuevo
    // hasta registrar su devolucion. Se busca si ya tiene un prestamo abierto.
    Optional<Prestamo> findFirstByRecursoIdAndFechaDevolucionIsNull(Long recursoId);
}
