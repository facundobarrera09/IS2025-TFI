package org.app.exceptions;

public class InvalidCreateIngreso extends RuntimeException {
    public InvalidCreateIngreso(String message) {
        super(message);
    }
}
