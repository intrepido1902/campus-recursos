package com.example.campusrecursos.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Requisito 4.c: prestamo de equipos y devolucion.
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurso_id")
    private Recurso recurso;

    private String responsable;

    private LocalDateTime fechaEntrega;

    private LocalDateTime fechaLimite;

    // Estado inicial del equipo al momento de entregarlo (ej: "Buen estado").
    private String estadoInicial;

    // Se completan solo cuando el gestor registra la devolucion.
    private LocalDateTime fechaDevolucion;
    private String estadoFinal;
    private String observaciones;

    public Prestamo() {
    }

    public Prestamo(Recurso recurso, String responsable, LocalDateTime fechaEntrega,
                     LocalDateTime fechaLimite, String estadoInicial) {
        this.recurso = recurso;
        this.responsable = responsable;
        this.fechaEntrega = fechaEntrega;
        this.fechaLimite = fechaLimite;
        this.estadoInicial = estadoInicial;
    }

    // Requisito 4.c.iv: identificar si la devolucion fue puntual o tardia.
    public boolean isDevuelto() {
        return fechaDevolucion != null;
    }

    public boolean isTardio() {
        return fechaDevolucion != null && fechaLimite != null && fechaDevolucion.isAfter(fechaLimite);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(String estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
