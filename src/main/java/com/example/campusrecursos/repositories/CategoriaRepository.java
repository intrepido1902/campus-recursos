package com.example.campusrecursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.campusrecursos.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
