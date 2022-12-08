package com.phonepe.paymentgateway.exception;

public class InvalidPaymentConfigurationException extends RuntimeException {
    public InvalidPaymentConfigurationException(String message) {
        super(message);
    }
}
