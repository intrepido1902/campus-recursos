package com.example.campusrecursos.services;

// Excepcion de negocio simple: se usa para comunicar al controlador por que
// una operacion no se puede realizar (recurso bloqueado, conflicto de horario, etc).
// El controlador la captura y muestra el mensaje al usuario en la misma pantalla.
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
