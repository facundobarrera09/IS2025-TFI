package org.domain.models;


public class Usuario {
    private final String email;
    private final String contraseñaHash;
    private final String autoridad;

    public Usuario(String email, String contraseñaHash, String autoridad) {
        this.email = email;
        this.contraseñaHash = contraseñaHash;
        this.autoridad = autoridad;
    }

    // Getters
    public String getEmail() { return email; }
    public String getContraseñaHash() { return contraseñaHash; }
    public String getAutoridad() { return autoridad; }
}
