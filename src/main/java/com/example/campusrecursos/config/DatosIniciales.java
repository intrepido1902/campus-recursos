package com.example.campusrecursos.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.campusrecursos.entities.Categoria;
import com.example.campusrecursos.entities.Recurso;
import com.example.campusrecursos.entities.TipoRecurso;
import com.example.campusrecursos.entities.Ubicacion;
import com.example.campusrecursos.repositories.CategoriaRepository;
import com.example.campusrecursos.repositories.RecursoRepository;
import com.example.campusrecursos.repositories.ReservaRepository;
import com.example.campusrecursos.repositories.UbicacionRepository;
import com.example.campusrecursos.entities.Reserva;

// Entregable de la primera entrega: "Programa Batch en Java Spring que cargue la
// base de datos con la informacion inicial requerida para recursos, ubicaciones,
// categorias y demas datos maestros". Se usa el mismo mecanismo que en el
// ejercicio de biblioteca visto en clase: un @Bean CommandLineRunner que corre
// una sola vez al arrancar la aplicacion.
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarDatos(CategoriaRepository categoriaRepository,
                                   UbicacionRepository ubicacionRepository,
                                   RecursoRepository recursoRepository,
                                   ReservaRepository reservaRepository) {
        return args -> {

            // Categorias
            Categoria tecnologia = categoriaRepository.save(new Categoria("Tecnologia"));
            Categoria espaciosAcademicos = categoriaRepository.save(new Categoria("Espacios academicos"));
            Categoria audiovisuales = categoriaRepository.save(new Categoria("Audiovisuales"));

            // Ubicaciones
            Ubicacion edificioJavier = ubicacionRepository.save(new Ubicacion("Bloque Javier", "Piso 3"));
            Ubicacion edificioTecnico = ubicacionRepository.save(new Ubicacion("Edificio Jose Gabriel Maldonado", "Piso 1"));
            Ubicacion biblioteca = ubicacionRepository.save(new Ubicacion("Biblioteca General", "Piso 2"));

            // Recursos: espacios y equipos de ejemplo
            Recurso laboratorio301 = recursoRepository.save(new Recurso(
                    "Laboratorio 301", TipoRecurso.ESPACIO, espaciosAcademicos, edificioJavier,
                    "Laboratorio de computo con 25 puestos", "25 computadores, tablero digital"));

            Recurso salaEstudio2 = recursoRepository.save(new Recurso(
                    "Sala de estudio 2", TipoRecurso.ESPACIO, espaciosAcademicos, biblioteca,
                    "Sala de estudio grupal", "Capacidad para 6 personas, tablero blanco"));

            Recurso videoBeamPortatil = recursoRepository.save(new Recurso(
                    "Video beam portatil #1", TipoRecurso.EQUIPO, tecnologia, edificioTecnico,
                    "Proyector portatil para prestamo", "Resolucion Full HD, incluye cable HDMI"));

            Recurso camaraGrabacion = recursoRepository.save(new Recurso(
                    "Camara de grabacion #1", TipoRecurso.EQUIPO, audiovisuales, edificioTecnico,
                    "Camara para registro de eventos academicos", "4K, incluye tripode"));

            Recurso auditorioCentral = recursoRepository.save(new Recurso(
                    "Auditorio Central", TipoRecurso.AMBOS, espaciosAcademicos, edificioJavier,
                    "Auditorio con equipo audiovisual fijo, tambien se pueden prestar sus microfonos",
                    "Capacidad 120 personas, sonido y proyeccion integrados"));

            // Un par de reservas de ejemplo para que el tablero y los indicadores no arranquen vacios.
            reservaRepository.save(new Reserva(laboratorio301, "Ana Torres",
                    LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
                    LocalDateTime.now().plusDays(1).withHour(10).withMinute(0)));

            reservaRepository.save(new Reserva(salaEstudio2, "Grupo Proyecto Final",
                    LocalDateTime.now().plusDays(2).withHour(14).withMinute(0),
                    LocalDateTime.now().plusDays(2).withHour(16).withMinute(0)));

            reservaRepository.save(new Reserva(auditorioCentral, "Bienestar Universitario",
                    LocalDateTime.now().plusDays(3).withHour(9).withMinute(0),
                    LocalDateTime.now().plusDays(3).withHour(12).withMinute(0)));
        };
    }
}
