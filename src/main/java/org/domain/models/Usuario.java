package org.domain.models;

import org.domain.errors.UsuarioNoAutorizado;

import java.util.regex.Pattern;

public class Usuario {
    private final String email;
    private final String hashContraseña;
    private final Autoridad autoridad;

    private Medico medico;
    private Enfermera enfermera;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private void validarDatos(String email, String hashContraseña, Autoridad autoridad) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        if (hashContraseña == null) {
            throw new IllegalArgumentException("hashContraseña no puede ser nulo");
        }

        if (autoridad == null) {
            throw new IllegalArgumentException("Autoridad no puede ser nulo");
        }
    }

    public Usuario(String email, String hashContraseña, Autoridad autoridad, Medico medico) {
        validarDatos(email, hashContraseña, autoridad);

        if (autoridad != Autoridad.MEDICO) {
            throw new IllegalArgumentException("Si la autoridad es médico, medico debe estar definido");
        }

        this.email = email;
        this.hashContraseña = hashContraseña;
        this.autoridad = autoridad;
        this.medico = medico;
    }

    public Usuario(String email, String hashContraseña, Autoridad autoridad, Enfermera enfermero) {
        validarDatos(email, hashContraseña, autoridad);

        if (autoridad != Autoridad.ENFERMERO) {
            throw new IllegalArgumentException("Si la autoridad es enfermero, enfermero debe estar definido");
        }

        this.email = email;
        this.hashContraseña = hashContraseña;
        this.autoridad = autoridad;
        this.enfermera = enfermero;
    }

    public Usuario(String email, String hashContraseña, Autoridad autoridad) {
        validarDatos(email, hashContraseña, autoridad);

        this.email = email;
        this.hashContraseña = hashContraseña;
        this.autoridad = autoridad;
    }

    // Getters
    public String getEmail() { return email; }
    public String getHashContraseña() { return hashContraseña; }
    public Autoridad getAutoridad() { return autoridad; }

    public Medico getMedico() {
        if (autoridad != Autoridad.MEDICO) {
            throw new UsuarioNoAutorizado(UsuarioNoAutorizado.PROHIBIDO);
        }
        return medico;
    }

    public Enfermera getEnfermera() {
        if (autoridad != Autoridad.ENFERMERO) {
            throw new UsuarioNoAutorizado(UsuarioNoAutorizado.PROHIBIDO);
        }
        return enfermera;
    }
}
