package com.example.campusrecursos.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.campusrecursos.entities.EstadoRecurso;
import com.example.campusrecursos.entities.EstadoReserva;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.entities.Reserva;
import com.example.campusrecursos.repositories.ReservaRepository;

// Requisito 4.b: reservas y validacion de conflictos.
// Todas las validaciones ocurren aqui, en el servidor (requisito 5.c), y no
// dependen de lo que haga o deje de hacer el formulario en el navegador.
@Service
public class ReservaService {
    private final ReservaRepository repositorio;
    private final RecursoService recursoService;

    public ReservaService(ReservaRepository repositorio, RecursoService recursoService) {
        this.repositorio = repositorio;
        this.recursoService = recursoService;
    }

    public List<Reserva> listar() {
        return repositorio.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("La reserva no existe"));
    }

    public Reserva crear(Long recursoId, String solicitante, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Recurso recurso = recursoService.buscarPorId(recursoId);
        validarFechas(fechaInicio, fechaFin);
        validarDisponibilidad(recurso);
        validarSolapamiento(recursoId, fechaInicio, fechaFin, null);

        Reserva reserva = new Reserva(recurso, solicitante, fechaInicio, fechaFin);
        return repositorio.save(reserva);
    }

    // Requisito 4.b.iii: consultar, modificar y cancelar una reserva antes de su inicio.
    public Reserva modificar(Long reservaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Reserva reserva = buscarPorId(reservaId);
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new ReglaNegocioException("Solo se pueden modificar reservas confirmadas");
        }
        validarFechas(fechaInicio, fechaFin);
        validarSolapamiento(reserva.getRecurso().getId(), fechaInicio, fechaFin, reservaId);

        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        return repositorio.save(reserva);
    }

    public void cancelar(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);
        reserva.setEstado(EstadoReserva.CANCELADA);
        repositorio.save(reserva);
    }

    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null || !fechaFin.isAfter(fechaInicio)) {
            throw new ReglaNegocioException("La fecha final debe ser posterior a la fecha inicial");
        }
    }

    // RN-02: un recurso bloqueado no se puede reservar.
    private void validarDisponibilidad(Recurso recurso) {
        if (recurso.getEstado() == EstadoRecurso.BLOQUEADO) {
            throw new ReglaNegocioException("El recurso esta bloqueado y no se puede reservar");
        }
    }

    // RN-01: no puede haber dos reservas activas con horario solapado para el mismo recurso.
    private void validarSolapamiento(Long recursoId, LocalDateTime fechaInicio, LocalDateTime fechaFin, Long reservaId) {
        List<Reserva> conflictos = repositorio.buscarConflictos(recursoId, fechaInicio, fechaFin, reservaId);
        if (!conflictos.isEmpty()) {
            throw new ReglaNegocioException("El recurso ya esta reservado en ese horario");
        }
    }
}
