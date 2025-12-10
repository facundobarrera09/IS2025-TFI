package org.app.exceptions;

public class InvalidFindOrCreatePaciente extends RuntimeException {
    public InvalidFindOrCreatePaciente(String message) {
        super(message);
    }
}
