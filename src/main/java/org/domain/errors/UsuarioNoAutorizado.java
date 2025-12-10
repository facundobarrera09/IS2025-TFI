package org.domain.errors;

public class UsuarioNoAutorizado extends RuntimeException {
    public static final String PROHIBIDO = "El usuario no tiene permiso de llevar a cabo esta tarea";
    public UsuarioNoAutorizado(String message) {
        super(message);
    }
}
