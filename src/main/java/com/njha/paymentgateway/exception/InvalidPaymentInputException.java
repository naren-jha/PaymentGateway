package com.njha.paymentgateway.exception;

public class InvalidPaymentInputException extends RuntimeException {
    public InvalidPaymentInputException(String message) {
        super(message);
    }
}
