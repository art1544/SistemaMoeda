package com.puc.moeda.exception;

public class RedemptionCodeAlreadyUsedException extends RuntimeException {
    public RedemptionCodeAlreadyUsedException(String message) {
        super(message);
    }
}