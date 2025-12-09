package org.domain.models;


import java.util.regex.Pattern;

public class Usuario {
    private final String email;
    private final String hashContraseña;
    private final Autoridad autoridad;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Usuario(String email, String hashContraseña, Autoridad autoridad) {
        // Validar email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        if (hashContraseña == null) {
            throw new IllegalArgumentException("hashContraseña no puede ser nulo");
        }

        if (autoridad == null) {
            throw new IllegalArgumentException("Autoridad no puede ser nulo");
        }

        this.email = email;
        this.hashContraseña = hashContraseña;
        this.autoridad = autoridad;
    }

    // Getters
    public String getEmail() { return email; }
    public String getHashContraseña() { return hashContraseña; }
    public Autoridad getAutoridad() { return autoridad; }
}
