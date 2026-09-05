package com.example.campusrecursos.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.campusrecursos.entities.EstadoReserva;
import com.example.campusrecursos.entities.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByRecursoId(Long recursoId);

    // JPQL visto en clase: se usa para la validacion de solapamiento (RN-01).
    // Dos intervalos [inicio1, fin1) y [inicio2, fin2) se solapan si inicio1 < fin2 y fin1 > inicio2.
    @Query("SELECT r FROM Reserva r WHERE r.recurso.id = :recursoId " +
           "AND r.estado = com.example.campusrecursos.entities.EstadoReserva.CONFIRMADA " +
           "AND r.fechaInicio < :fechaFin AND r.fechaFin > :fechaInicio " +
           "AND (:reservaId IS NULL OR r.id <> :reservaId)")
    List<Reserva> buscarConflictos(@Param("recursoId") Long recursoId,
                                    @Param("fechaInicio") LocalDateTime fechaInicio,
                                    @Param("fechaFin") LocalDateTime fechaFin,
                                    @Param("reservaId") Long reservaId);

    long countByRecursoIdAndEstado(Long recursoId, EstadoReserva estado);
}
