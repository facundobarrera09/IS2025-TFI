package org.domain.errors;

public class PacienteYaIngresado extends RuntimeException {
    public PacienteYaIngresado(String message) {
        super(message);
    }
}
