package org.domain.errors;

public class InvalidInsurance extends RuntimeException {
    public InvalidInsurance(String message) {
        super(message);
    }
}
