package org.domain.errors;

public class ElementoNoEncontrado extends RuntimeException {
    public ElementoNoEncontrado(String message) {
        super(message);
    }
}
