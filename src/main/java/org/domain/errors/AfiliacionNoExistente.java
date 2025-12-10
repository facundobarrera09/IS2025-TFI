package org.domain.errors;

public class AfiliacionNoExistente extends RuntimeException {
    public AfiliacionNoExistente(String message) {
        super(message);
    }
}
