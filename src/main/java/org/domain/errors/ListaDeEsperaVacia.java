package org.domain.errors;

public class ListaDeEsperaVacia extends RuntimeException {
    public ListaDeEsperaVacia(String message) {
        super(message);
    }
}
