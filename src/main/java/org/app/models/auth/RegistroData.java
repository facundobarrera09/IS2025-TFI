package org.app.models.auth;

public class RegistroData {
    private String email;
    private String contraseña;
    private String autoridad;
    private CreateMedico medico;
    private CreateEnfermera enfermera;

    public String getEmail() {
        return email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public CreateMedico getMedico() {
        return medico;
    }

    public CreateEnfermera getEnfermera() {
        return enfermera;
    }
}
