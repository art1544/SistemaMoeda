package com.puc.moeda.exception;

public class InvalidRedemptionCodeException extends RuntimeException {
    public InvalidRedemptionCodeException(String message) {
        super(message);
    }
}