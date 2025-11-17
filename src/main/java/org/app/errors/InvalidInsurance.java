package org.app.errors;

public class InvalidInsurance extends RuntimeException {
    public InvalidInsurance(String message) {
        super(message);
    }
}
